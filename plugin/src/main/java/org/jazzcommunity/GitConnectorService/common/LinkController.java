package org.jazzcommunity.GitConnectorService.common;

import com.ibm.team.links.common.IReference;
import com.ibm.team.links.common.IURIReference;
import com.ibm.team.links.common.registry.IEndPointDescriptor;
import com.ibm.team.process.common.IProjectArea;
import com.ibm.team.process.common.IProjectAreaHandle;
import com.ibm.team.repository.common.IItem;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.repository.common.query.IItemQuery;
import com.ibm.team.repository.common.query.ast.IDynamicItemQueryModel;
import com.ibm.team.repository.service.IRepositoryItemService;
import com.ibm.team.repository.service.TeamRawService;
import com.ibm.team.workitem.common.QueryIterator.ReadMode;
import com.ibm.team.workitem.common.expression.AttributeExpression;
import com.ibm.team.workitem.common.expression.IQueryableAttribute;
import com.ibm.team.workitem.common.expression.IQueryableAttributeFactory;
import com.ibm.team.workitem.common.expression.QueryableAttributes;
import com.ibm.team.workitem.common.expression.Term;
import com.ibm.team.workitem.common.expression.Term.Operator;
import com.ibm.team.workitem.common.internal.util.ItemQueryIterator;
import com.ibm.team.workitem.common.model.AttributeOperation;
import com.ibm.team.workitem.common.model.IWorkItem;
import com.ibm.team.workitem.common.model.IWorkItemReferences;
import com.ibm.team.workitem.common.query.IQueryResult;
import com.ibm.team.workitem.common.query.IResolvedResult;
import com.ibm.team.workitem.service.IAuditableServer;
import com.ibm.team.workitem.service.IQueryServer;
import com.ibm.team.workitem.service.IWorkItemServer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jazzcommunity.GitConnectorService.dcc.data.WorkItemLink;

public class LinkController {
  private final GitLink[] linkTypes;
  private final TeamRawService teamService;

  public LinkController(TeamRawService teamService) {
    this.linkTypes = GitLink.values();
    this.teamService = teamService;
  }

  public LinkController(GitLink linkType, TeamRawService teamService) {
    this(new GitLink[] {linkType}, teamService);
  }

  public LinkController(GitLink[] linkTypes, TeamRawService teamService) {
    this.linkTypes = linkTypes;
    this.teamService = teamService;
  }

  public Collection<WorkItemLink> collect(boolean includeArchived) throws TeamRepositoryException {
    IQueryServer service = teamService.getService(IQueryServer.class);
    List<IProjectAreaHandle> handles = getProjectAreaHandles();
    ArrayList<WorkItemLink> links = new ArrayList<>(1 << 4);

    for (IProjectAreaHandle handle : handles) {
      IProjectArea area = getProjectArea(handle);
      logProjectArea(area);

      // uninitialized project areas cause all kinds of weird behaviour and errors
      if (!area.isInitialized()) {
        continue;
      }

      // do not include archived project areas unless explicitly stated in dcc job
      if (area.isArchived() && !includeArchived) {
        continue;
      }

      Term query = createLinkQuery(handle);
      IQueryResult<IResolvedResult<IWorkItem>> results =
          service.getResolvedExpressionResults(handle, query, IWorkItem.FULL_PROFILE);
      results.setLimit(Integer.MAX_VALUE);

      while (results.hasNext(null)) {
        IResolvedResult<IWorkItem> result = results.next(null);
        IWorkItem item = result.getItem();

        // yes, this is a bit complicated, but it's what it takes to unwrap work item references
        IWorkItemServer itemService = teamService.getService(IWorkItemServer.class);
        IWorkItemReferences references = itemService.resolveWorkItemReferences(item, null);
        for (IEndPointDescriptor descriptor : references.getTypes()) {
          for (IReference reference : references.getReferences(descriptor)) {
            if (reference.isURIReference() && isGitLink(reference)) {
              IURIReference uriReference = (IURIReference) reference;
              WorkItemLink link =
                  new WorkItemLink(
                      item.getItemId(),
                      area.getItemId().getUuidValue(),
                      reference.getLink().getLinkTypeId(),
                      uriReference.getURI());

              links.add(link);
            }
          }
        }
      }
    }

    return links;
  }

  private boolean isGitLink(IReference reference) {
    for (GitLink type : linkTypes) {
      if (reference.getLink().getLinkTypeId().equals(type.toString())) {
        return true;
      }
    }

    return false;
  }

  private Term createLinkQuery(IProjectAreaHandle handle) throws TeamRepositoryException {
    List<IQueryableAttribute> attributes = getLinkAttributes(handle);
    Term gitLinks = orTerm(handle, attributes);
    Term projectArea = projectAreaTerm(handle);
    Term combined = new Term(Operator.AND);
    combined.add(gitLinks);
    combined.add(projectArea);
    return combined;
  }

  /**
   * Get the properly typed item of a project area handle. This is needed if you want the full
   * information for a project area, such as it's name.
   *
   * @param handle Handle of the project area to get more information from
   * @return Project Area object
   */
  private IProjectArea getProjectArea(IProjectAreaHandle handle) throws TeamRepositoryException {
    IRepositoryItemService service = teamService.getService(IRepositoryItemService.class);
    IItem item = service.fetchItem(handle, null);
    return (IProjectArea) item.getFullState();
  }

  /**
   * Create term including logical OR of all supplied link attributes
   *
   * @param handle Project area to create term for
   * @param attributes Attributes for which to create expressions
   * @return Term including all query expressions for the current project area
   */
  private Term orTerm(IProjectAreaHandle handle, List<IQueryableAttribute> attributes) {
    Term term = new Term(Operator.OR);
    List<AttributeExpression> expressions = mapExpressions(handle, attributes);

    for (AttributeExpression expression : expressions) {
      term.add(expression);
    }

    return term;
  }

  private Term projectAreaTerm(IProjectAreaHandle handle) throws TeamRepositoryException {
    Term term = new Term(Operator.AND);
    IQueryableAttribute projectAreaAttribute = getProjectAreaAttribute(handle);
    AttributeExpression projectAreaExpression =
        new AttributeExpression(projectAreaAttribute, AttributeOperation.EQUALS, handle);
    term.add(projectAreaExpression);
    return term;
  }

  private IQueryableAttribute getProjectAreaAttribute(IProjectAreaHandle handle)
      throws TeamRepositoryException {
    // I need to include a term which restricts the project area here.
    // I think all of this attribute building stuff can probably be extracted as well.
    IQueryableAttributeFactory factory = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE);
    return factory.findAttribute(
        handle,
        IWorkItem.PROJECT_AREA_PROPERTY,
        teamService.getService(IAuditableServer.class),
        null);
  }

  /**
   * Create mapping expressions for all attributes and a project area.
   *
   * @param handle Project area to create expressions for
   * @param attributes Attributes to create expressions for
   * @return Query expressions that can be used to execute a query
   */
  private List<AttributeExpression> mapExpressions(
      IProjectAreaHandle handle, List<IQueryableAttribute> attributes) {
    ArrayList<AttributeExpression> expressions = new ArrayList<>();

    for (IQueryableAttribute attribute : attributes) {
      // this handle here can be null and everything works somehow, so I'm not even sure it needs
      // to be passed in the first place. I'm leaving it in for now.
      AttributeExpression expression =
          new AttributeExpression(attribute, AttributeOperation.LINK_EXISTS, handle);
      expressions.add(expression);
    }

    return expressions;
  }

  /**
   * create queryable attributes for every git link type
   *
   * @return Collection of queryable git link attributes
   */
  private List<IQueryableAttribute> getLinkAttributes(IProjectAreaHandle handle)
      throws TeamRepositoryException {
    List<IQueryableAttribute> attributes = new ArrayList<>();
    IQueryableAttributeFactory factory = QueryableAttributes.getFactory(IWorkItem.ITEM_TYPE);

    for (GitLink type : linkTypes) {
      IQueryableAttribute attribute =
          factory.findAttribute(
              handle, type.asTarget(), teamService.getService(IAuditableServer.class), null);
      attributes.add(attribute);
    }

    return attributes;
  }

  /**
   * Get all project area handles available to the current user. At least, I think that's what this
   * does...
   */
  private List<IProjectAreaHandle> getProjectAreaHandles() throws TeamRepositoryException {
    IDynamicItemQueryModel model = IProjectArea.ITEM_TYPE.getQueryModel();
    IItemQuery query = IItemQuery.FACTORY.newInstance(model);
    IAuditableServer server = teamService.getService(IAuditableServer.class);
    ItemQueryIterator<IProjectAreaHandle> iterator =
        new ItemQueryIterator<>(server, query, null, null, ReadMode.COMMITTED);
    return iterator.toList(null);
  }

  private void logProjectArea(IProjectArea pa) {
    String initialized = pa.isInitialized() ? "initialized" : "uninitialized";
    String archived = pa.isArchived() ? "archived" : "not archived";
    String message =
        String.format(
            "Project Area '%s %s' is %s and %s",
            pa.getName(), pa.getItemId(), initialized, archived);
    teamService.getLog().info(message);
  }
}

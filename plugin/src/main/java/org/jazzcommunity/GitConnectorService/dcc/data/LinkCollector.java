package org.jazzcommunity.GitConnectorService.dcc.data;

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

public class LinkCollector {
  /**
   * Currently available git link types which map to queryable attributes. Maybe create an api
   * endpoint for this instead? And sort of keep track of this using the collector service?
   * TODO: Extract to Enum
   */
  private final String[] linkTypes = {
    "link:com.ibm.team.git.workitem.linktype.gitCommit:target",
    "link:org.jazzcommunity.git.link.git_issue:target",
    "link:org.jazzcommunity.git.link.git_mergerequest:target"
  };

  private TeamRawService teamService;

  public LinkCollector(TeamRawService teamService) {
    this.teamService = teamService;
  }

  private void logProjectArea(IProjectArea pa) {
    String initialized = pa.isInitialized() ? "initialized" : "uninitialized";
    String archived = pa.isArchived() ? "archived" : "not archived";
    String message =
        String.format(
            "Project Area %s %s is %s and %s", pa.getName(), pa.getItemId(), initialized, archived);
    teamService.getLog().warn(message);
  }

  public Collection<WorkItemLinkFactory> collect() throws TeamRepositoryException {
    ArrayList<WorkItemLinkFactory> links = new ArrayList<>();

    IQueryServer service = teamService.getService(IQueryServer.class);
    IRepositoryItemService repositoryItemService =
        teamService.getService(IRepositoryItemService.class);

    List<IProjectAreaHandle> handles = getProjectAreaHandles();
    for (IProjectAreaHandle handle : handles) {
      IProjectArea area = getProjectArea(handle);
      logProjectArea(area);

      // Workaround for 'broken' project areas for which we don't really know what the actual
      // problem is.
      if (!area.isInitialized()) {
        continue;
      }

      List<IQueryableAttribute> attributes = getLinkAttributes(handle);
      Term gitLinks = orTerm(handle, attributes);
      Term projectArea = projectAreaTerm(handle);

      // how the hell can you define that I want both of these?
      // is it not allowed to be an expression, but a query?
      // or...seriously... do I have to take the logic apart manually and do
      // (link AND pa) OR (link AND pa) OR (link AND pa)?
      // No, this seems to work. Term -> Extension
      Term combined = new Term(Operator.AND);
      combined.add(gitLinks);
      combined.add(projectArea);

      IQueryResult<IResolvedResult<IWorkItem>> results =
          service.getResolvedExpressionResults(handle, combined, IWorkItem.FULL_PROFILE);

      // overcome query result limit:
      // https://rsjazz.wordpress.com/2012/10/29/using-work-item-queris-for-automation/
      results.setLimit(Integer.MAX_VALUE);

      // at this point, we should have all the work item results that we want to have
      while (results.hasNext(null)) {
        IResolvedResult<IWorkItem> result = results.next(null);
        // this is the work item we are looking at links for
        IWorkItem item = result.getItem();
        // object for aggregating link data
        WorkItemLinkFactory workItemLinkFactory =
            new WorkItemLinkFactory(
                area.getName(), item.getId(), item.getItemId(), item.getHTMLSummary());
        // adding this shouldn't be manual, and probably the whole data structure(s) used here
        // should be a lot more refined
        links.add(workItemLinkFactory);

        // this resolves work item references
        IWorkItemServer itemService = teamService.getService(IWorkItemServer.class);
        IWorkItemReferences references = itemService.resolveWorkItemReferences(item, null);
        for (IEndPointDescriptor descriptor : references.getTypes()) {
          // filter for git links, there needs to be a nicer way of doing this
          if (descriptor.getLinkType().getLinkTypeId().contains("git")) {
            for (IReference reference : references.getReferences(descriptor)) {
              if (reference.isURIReference()) {
                IURIReference uriRef = (IURIReference) reference;
                workItemLinkFactory.addLink(uriRef.getComment(), uriRef.getURI());
              }
            }
          }
        }
      }
    }

    return links;
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

    for (String type : linkTypes) {
      IQueryableAttribute attribute =
          factory.findAttribute(handle, type, teamService.getService(IAuditableServer.class), null);
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
}

package org.jazzcommunity.GitConnectorService;

import com.siemens.bt.jazz.services.base.BaseService;
import com.siemens.bt.jazz.services.base.configuration.Configuration;
import com.siemens.bt.jazz.services.base.configuration.preset.ContentConfigurator;
import com.siemens.bt.jazz.services.base.configuration.preset.EncodingConfigurator;
import com.siemens.bt.jazz.services.base.router.Router;
import java.nio.charset.StandardCharsets;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.ccm.inject.LinkTypeInjector;
import org.jazzcommunity.GitConnectorService.ccm.service.VersionService;
import org.jazzcommunity.GitConnectorService.ccm.service.development.RegisterRepositoryService;
import org.jazzcommunity.GitConnectorService.ccm.service.gitlab.IssueLinkService;
import org.jazzcommunity.GitConnectorService.ccm.service.gitlab.IssuePreviewService;
import org.jazzcommunity.GitConnectorService.ccm.service.gitlab.RequestLinkService;
import org.jazzcommunity.GitConnectorService.ccm.service.gitlab.RequestPreviewService;
import org.jazzcommunity.GitConnectorService.ccm.service.proxy.ProxyService;
import org.jazzcommunity.GitConnectorService.ccm.service.resource.ImageService;
import org.jazzcommunity.GitConnectorService.dcc.service.CommitService;
import org.jazzcommunity.GitConnectorService.dcc.service.IssueLinkCollectionService;
import org.jazzcommunity.GitConnectorService.dcc.service.IssueService;
import org.jazzcommunity.GitConnectorService.dcc.service.MergeRequestLinkCollectionService;
import org.jazzcommunity.GitConnectorService.dcc.service.MergeRequestService;

/**
 * Entry point for the Service, called by the Jazz class loader.
 *
 * <p>This class must be implemented for enabling plug-ins to run inside Jazz. The implemented
 * interface corresponds to the component in {@code plugin.xml}, and this service is therefore the
 * provided service by the interface.
 */
public class GitConnectorService extends BaseService implements IGitConnectorService {

  /**
   * Constructs a new Service
   *
   * <p>This constructor is only called by the Jazz class loader.
   */
  public GitConnectorService() {
    super();
    LinkTypeInjector.injectCustomLinks();
    addCcmRoutes(router);
    addDccRoutes(router);
  }

  private void addDccRoutes(Router router) {
    EncodingConfigurator utf = new EncodingConfigurator(StandardCharsets.UTF_8.name());
    ContentConfigurator xml = new ContentConfigurator(ContentType.APPLICATION_XML.toString());
    Configuration response = new Configuration(utf, xml);
    
    router.get("dcc/commits", CommitService.class, response);
    router.get("dcc/issues", IssueService.class, response);
    router.get("dcc/merge-requests", MergeRequestService.class, response);
    router.get("dcc/links/issues", IssueLinkCollectionService.class, response);
    router.get("dcc/links/merge-requests", MergeRequestLinkCollectionService.class, response);
  }

  private void addCcmRoutes(Router router) {
    router.get("gitlab/{host}/project/{projectId}/issue/{issueId}/link", IssueLinkService.class);
    router.get(
        "gitlab/{host}/project/{projectId}/issue/{issueId}/preview", IssuePreviewService.class);

    router.get(
        "gitlab/{host}/project/{projectId}/merge-request/{mergeRequestId}/link",
        RequestLinkService.class);
    router.get(
        "gitlab/{host}/project/{projectId}/merge-request/{mergeRequestId}/preview",
        RequestPreviewService.class);

    router.get("info/version", VersionService.class);

    router.get("img/{filename}", ImageService.class);

    router.get("proxy/{host}", ProxyService.class);
    router.post("proxy/{host}", ProxyService.class);

    // this service should only work in debug modes
    router.post("repositories", RegisterRepositoryService.class);

    /**
     * This code is purposely commented out and not deleted! We have decided to use the IBM rich
     * hovers for now, but potential support will remain built into the service until the decision
     * is final, depending on how IBM will proceed with their own git integration support.
     *
     * <p>Note that these services use the legacy way of defining path parameters.
     */
    //        router.addService(HttpMethod.GET,
    // ".*/gitlab/[a-zA-Z.]+/project/[0-9]+/commit/[^\\/]+/link.*",
    //                new RestFactory(CommitLinkService.class));
    //        router.addService(HttpMethod.GET,
    // ".*/gitlab/[a-zA-Z.]+/project/[0-9]+/commit/[^\\/]+/preview.*",
    //                new RestFactory(CommitPreviewService.class));
  }
}

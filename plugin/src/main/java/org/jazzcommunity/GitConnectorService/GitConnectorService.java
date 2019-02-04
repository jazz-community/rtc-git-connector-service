package org.jazzcommunity.GitConnectorService;

import com.siemens.bt.jazz.services.base.BaseService;
import org.jazzcommunity.GitConnectorService.ccm.inject.LinkTypeInjector;
import org.jazzcommunity.GitConnectorService.ccm.service.VersionService;
import org.jazzcommunity.GitConnectorService.ccm.service.gitlab.IssueLinkService;
import org.jazzcommunity.GitConnectorService.ccm.service.gitlab.IssuePreviewService;
import org.jazzcommunity.GitConnectorService.ccm.service.gitlab.RequestLinkService;
import org.jazzcommunity.GitConnectorService.ccm.service.gitlab.RequestPreviewService;
import org.jazzcommunity.GitConnectorService.ccm.service.proxy.ProxyService;
import org.jazzcommunity.GitConnectorService.ccm.service.resource.ImageService;

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

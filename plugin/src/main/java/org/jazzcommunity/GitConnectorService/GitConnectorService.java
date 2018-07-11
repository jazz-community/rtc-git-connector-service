package org.jazzcommunity.GitConnectorService;

import com.ibm.team.workitem.common.model.WorkItemLinkTypes;
import com.siemens.bt.jazz.services.base.BaseService;
import org.jazzcommunity.GitConnectorService.service.VersionService;
import org.jazzcommunity.GitConnectorService.service.gitlab.IssueLinkService;
import org.jazzcommunity.GitConnectorService.service.gitlab.IssuePreviewService;
import org.jazzcommunity.GitConnectorService.service.gitlab.RequestLinkService;
import org.jazzcommunity.GitConnectorService.service.gitlab.RequestPreviewService;
import org.jazzcommunity.GitConnectorService.service.resource.ImageService;

import java.lang.reflect.Field;
import java.util.HashSet;

/**
 * Entry point for the Service, called by the Jazz class loader.
 * <p>
 * <p>This class must be implemented for enabling plug-ins to run inside Jazz. The implemented interface corresponds to
 * the component in {@code plugin.xml}, and this service is therefore the provided service by the interface.</p>
 */
public class GitConnectorService extends BaseService implements IGitConnectorService {

    private static void setDeletable() throws NoSuchFieldException, IllegalAccessException {
        Field deletable = WorkItemLinkTypes.class.getDeclaredField("USER_DELETABLE");
        deletable.setAccessible(true);
        HashSet<String> set = (HashSet<String>) deletable.get(new WorkItemLinkTypes());
        System.out.println(set);
        set.add("org.jazzcommunity.git.link.git_issue");
        set.add("org.jazzcommunity.git.link.git_mergerequest");
    }

    private static void setValid() throws NoSuchFieldException, IllegalAccessException {
        Field valid = WorkItemLinkTypes.class.getDeclaredField("ALL_LINK_TYPES");
        valid.setAccessible(true);
        HashSet<String> set = (HashSet<String>) valid.get(new WorkItemLinkTypes());
        System.out.println(set);
        set.add("org.jazzcommunity.git.link.git_issue");
        set.add("org.jazzcommunity.git.link.git_mergerequest");
        WorkItemLinkTypes.isValidLinkType("org.jazzcommunity.git.link.git_issue");
    }
    /**
     * Constructs a new Service
     * <p>This constructor is only called by the Jazz class loader.</p>
     */
    public GitConnectorService() {
        super();
        
        try {
            setDeletable();
            setValid();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            this.getLog().error("Unable to inject valid link types");
        }

        router.get(
                "gitlab/{host}/project/{projectId}/issue/{issueId}/link",
                IssueLinkService.class);
        router.get(
                "gitlab/{host}/project/{projectId}/issue/{issueId}/preview",
                IssuePreviewService.class);

        router.get(
                "gitlab/{host}/project/{projectId}/merge-request/{mergeRequestId}/link",
                RequestLinkService.class);
        router.get(
                "gitlab/{host}/project/{projectId}/merge-request/{mergeRequestId}/preview",
                RequestPreviewService.class);

        router.get(
                "info/version",
                VersionService.class);

        router.get(
                "img/{filename}",
                ImageService.class);


        /**
         * This code is purposely commented out and not deleted!
         * We have decided to use the IBM rich hovers for now, but potential support will remain built into
         * the service until the decision is final, depending on how IBM will proceed with their own git
         * integration support.
         *
         * Note that these services use the legacy way of defining path parameters.
         */
//        router.addService(HttpMethod.GET, ".*/gitlab/[a-zA-Z.]+/project/[0-9]+/commit/[^\\/]+/link.*",
//                new RestFactory(CommitLinkService.class));
//        router.addService(HttpMethod.GET, ".*/gitlab/[a-zA-Z.]+/project/[0-9]+/commit/[^\\/]+/preview.*",
//                new RestFactory(CommitPreviewService.class));
    }
}

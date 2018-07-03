package org.jazzcommunity.GitConnectorService;

import com.ibm.team.jfs.app.http.util.HttpConstants.HttpMethod;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestAction;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.jazzcommunity.GitConnectorService.base.rest.RestActionBuilder;
import org.jazzcommunity.GitConnectorService.base.router.CustomRouter;
import org.jazzcommunity.GitConnectorService.builder.VersionService;
import org.jazzcommunity.GitConnectorService.builder.gitlab.IssueLinkService;
import org.jazzcommunity.GitConnectorService.builder.gitlab.IssuePreviewService;
import org.jazzcommunity.GitConnectorService.builder.gitlab.RequestLinkService;
import org.jazzcommunity.GitConnectorService.builder.gitlab.RequestPreviewService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Entry point for the Service, called by the Jazz class loader.
 * <p>
 * <p>This class must be implemented for enabling plug-ins to run inside Jazz. The implemented interface corresponds to
 * the component in {@code plugin.xml}, and this service is therefore the provided service by the interface.</p>
 */
public class GitConnectorService extends TeamRawService implements IGitConnectorService {

    private final CustomRouter router = new CustomRouter();

    /**
     * Constructs a new Service
     * <p>This constructor is only called by the Jazz class loader.</p>
     */
    public GitConnectorService() {
        super();
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

        /**
         * This code is purposely commented out and not deleted!
         * We have decided to use the IBM rich hovers for now, but potential support will remain built into
         * the service until the decision is final, depending on how IBM will proceed with their own git
         * integration support.
         */
//        router.addService(HttpMethod.GET, ".*/gitlab/[a-zA-Z.]+/project/[0-9]+/commit/[^\\/]+/link.*",
//                new RestFactory(CommitLinkService.class));
//        router.addService(HttpMethod.GET, ".*/gitlab/[a-zA-Z.]+/project/[0-9]+/commit/[^\\/]+/preview.*",
//                new RestFactory(CommitPreviewService.class));
    }

    @Override
    public void perform_GET(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException {
        performAction(uri, request, response);
    }

    @Override
    public void perform_POST(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException {
        performAction(uri, request, response);
    }

    protected void performAction(String uri, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            RestActionBuilder builder = prepareRequest(uri, request, response);
            RestAction action = builder.create();
            action.execute();
        } catch (IOException e) {
            // This will need extra logging, but we quench it for now to allow non-authorized requests
            // without spamming the server log. Sorry :-*
            //throw e;
        } catch (Exception e) {
            // catch everything and log. Makes sure that there is no checked exception from our service back
            // to jazz, except for the expected IOException when the response isn't writable. We need to make
            // sure that our plug-in conforms to the contract that no exceptions bubble out into the system.
            super.getLog().error(e);
            this.http500return(request, response, e);
        }
    }

    protected final RestActionBuilder prepareRequest(
            String uri,
            HttpServletRequest request,
            HttpServletResponse response) {

        HttpMethod method = HttpMethod.fromString(request.getMethod());
        @SuppressWarnings("unchecked")
        RestRequest restRequest = new RestRequest(method, uri, request.getParameterMap());
        return router.prepareAction(
                this,
                this.getLog(),
                request,
                response,
                restRequest);
    }
}

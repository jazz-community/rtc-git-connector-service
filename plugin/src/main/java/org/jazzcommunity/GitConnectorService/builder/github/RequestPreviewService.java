package org.jazzcommunity.GitConnectorService.builder.github;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.base.rest.AbstractRestService;
import org.jazzcommunity.GitConnectorService.base.rest.PathParameters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestPreviewService extends AbstractRestService {

    public RequestPreviewService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService, PathParameters pathParameters) {
        super(log, request, response, restRequest, parentService, pathParameters);
    }

    @Override
    public void execute() throws Exception {
        throw new RuntimeException("Not implemented");
    }
}

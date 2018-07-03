package org.jazzcommunity.GitConnectorService.builder.github;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import org.apache.commons.logging.Log;

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

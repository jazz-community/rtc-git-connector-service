package org.jazzcommunity.GitConnectorService.base.rest;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class DefaultRestService extends AbstractRestService {
    public DefaultRestService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService, PathParameters pathParameters) {
        super(log, request, response, restRequest, parentService, pathParameters);
    }

    @Override
    public void execute() throws Exception {
        this.response.setStatus(HttpStatus.SC_NOT_IMPLEMENTED);
        this.response.setContentType("text");
        PrintWriter writer = this.response.getWriter();
        String answer = String.format("The requested service \"%s\" doesn't exist for method \"%s\".",
                this.restRequest,
                this.request.getMethod());
        writer.write(answer);
    }
}

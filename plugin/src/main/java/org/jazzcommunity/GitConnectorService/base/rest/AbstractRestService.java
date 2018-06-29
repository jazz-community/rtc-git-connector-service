package org.jazzcommunity.GitConnectorService.base.rest;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestAction;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractRestService implements RestAction {
    protected Log log;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected RestRequest restRequest;
    protected TeamRawService parentService;
    protected PathParameters pathParameters;

    public AbstractRestService(
            Log log,
            HttpServletRequest request,
            HttpServletResponse response,
            RestRequest restRequest,
            TeamRawService parentService,
            PathParameters pathParameters) {
        this.log = log;
        this.request = request;
        this.response = response;
        this.restRequest = restRequest;
        this.parentService = parentService;
        this.pathParameters = pathParameters;
    }
}

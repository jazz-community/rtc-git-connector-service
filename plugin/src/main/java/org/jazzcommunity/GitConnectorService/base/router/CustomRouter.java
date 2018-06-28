package org.jazzcommunity.GitConnectorService.base.router;

import com.ibm.team.jfs.app.http.util.HttpConstants;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.base.rest.RestActionBuilder;
import org.jazzcommunity.GitConnectorService.base.router.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomRouter implements Router {
    private final ServiceMap map = new ServiceMap();

    @Override
    public void addService(
            HttpConstants.HttpMethod method,
            ServiceFactory serviceFactory) {

        map.add(method, serviceFactory.getPath(), serviceFactory);
    }

    @Override
    public RestActionBuilder prepareAction(
            TeamRawService parentService,
            Log log,
            HttpServletRequest request,
            HttpServletResponse response,
            RestRequest restRequest) {

        return map.getFactory(request, restRequest.toString()).getBuilder()
                .setParentService(parentService)
                .setLog(log)
                .setRequest(request)
                .setResponse(response)
                .setRestRequest(restRequest);
    }
}

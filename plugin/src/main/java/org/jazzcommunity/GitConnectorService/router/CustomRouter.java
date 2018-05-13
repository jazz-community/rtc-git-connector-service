package org.jazzcommunity.GitConnectorService.router;

import com.ibm.team.jfs.app.http.util.HttpConstants;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestActionBuilder;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import com.siemens.bt.jazz.services.base.router.Router;
import com.siemens.bt.jazz.services.base.router.factory.ServiceFactory;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomRouter implements Router {
    private final ServiceMap map = new ServiceMap();

    @Override
    public void addService(HttpConstants.HttpMethod method,
                           String path,
                           ServiceFactory serviceFactory) {
        map.add(method, path, serviceFactory);
    }

    @Override
    public RestActionBuilder prepareAction(TeamRawService parentService,
                                           Log log,
                                           HttpServletRequest request,
                                           HttpServletResponse response,
                                           RestRequest restRequest) {

        return map.getFactory(request).getBuilder()
                .setParentService(parentService)
                .setLog(log)
                .setRequest(request)
                .setResponse(response)
                .setRestRequest(restRequest);
    }
}

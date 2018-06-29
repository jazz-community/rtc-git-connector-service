package org.jazzcommunity.GitConnectorService.base.router;

import com.ibm.team.jfs.app.http.util.HttpConstants.HttpMethod;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.base.rest.RestActionBuilder;
import org.jazzcommunity.GitConnectorService.base.router.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Router {
    void addService(HttpMethod method, ServiceFactory factory);
    void get(ServiceFactory factory);
    void put(ServiceFactory factory);
    void post(ServiceFactory factory);
    void delete(ServiceFactory factory);

    RestActionBuilder prepareAction(
            TeamRawService parentService,
            Log log,
            HttpServletRequest request,
            HttpServletResponse response,
            RestRequest restRequest);
}

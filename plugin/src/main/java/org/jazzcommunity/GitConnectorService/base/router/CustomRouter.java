package org.jazzcommunity.GitConnectorService.base.router;

import com.ibm.team.jfs.app.http.util.HttpConstants;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.jazzcommunity.GitConnectorService.base.rest.AbstractRestService;
import org.jazzcommunity.GitConnectorService.base.rest.RestActionBuilder;
import org.jazzcommunity.GitConnectorService.base.router.factory.RestFactory;
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

    // It might actually be nice to inject the rest factory type / a factory for
    // rest services into the constructor, which is then used for all mappings.
    @Override
    public void get(String path, Class<? extends AbstractRestService> service) {
        addService(
                HttpConstants.HttpMethod.GET,
                new RestFactory(path, service));
    }

    @Override
    public void put(String path, Class<? extends AbstractRestService> service) {
        addService(
                HttpConstants.HttpMethod.PUT,
                new RestFactory(path, service));
    }

    @Override
    public void post(String path, Class<? extends AbstractRestService> service) {
        addService(
                HttpConstants.HttpMethod.POST,
                new RestFactory(path, service));
    }

    @Override
    public void delete(String path, Class<? extends AbstractRestService> service) {
        addService(
                HttpConstants.HttpMethod.DELETE,
                new RestFactory(path, service));
    }

    @Override
    public void get(ServiceFactory factory) {
        addService(HttpConstants.HttpMethod.GET, factory);
    }

    @Override
    public void put(ServiceFactory factory) {
        addService(HttpConstants.HttpMethod.PUT, factory);
    }

    @Override
    public void post(ServiceFactory factory) {
        addService(HttpConstants.HttpMethod.POST, factory);
    }

    @Override
    public void delete(ServiceFactory factory) {
        addService(HttpConstants.HttpMethod.DELETE, factory);
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

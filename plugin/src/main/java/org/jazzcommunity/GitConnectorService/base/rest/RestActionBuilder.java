package org.jazzcommunity.GitConnectorService.base.rest;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import com.siemens.bt.jazz.services.base.rest.RestAction;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RestActionBuilder {
    protected final Class<? extends AbstractRestService> serviceClass;
    protected final String path;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    // not sure about which log to use here...
    protected Log log;
    protected RestRequest restRequest;
    protected TeamRawService parentService;

    public RestActionBuilder(Class<? extends AbstractRestService> serviceClass, String path) {
        this.serviceClass = serviceClass;
        this.path = path;
    }

    public RestActionBuilder setLog(Log log) {
        this.log = log;
        return this;
    }

    public RestActionBuilder setRequest(HttpServletRequest request) {
        this.request = request;
        return this;
    }

    public RestActionBuilder setResponse(HttpServletResponse response) {
        this.response = response;
        return this;
    }

    public RestActionBuilder setRestRequest(RestRequest restRequest) {
        this.restRequest = restRequest;
        return this;
    }

    public RestActionBuilder setParentService(TeamRawService parentService) {
        this.parentService = parentService;
        return this;
    }

    public RestAction create() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends AbstractRestService> constructor = this.serviceClass.getConstructor(Log.class, HttpServletRequest.class, HttpServletResponse.class, RestRequest.class, TeamRawService.class);
        return (RestAction)constructor.newInstance(this.log, this.request, this.response, this.restRequest, this.parentService);
    }

}

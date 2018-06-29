package org.jazzcommunity.GitConnectorService.base.router.factory;

import org.jazzcommunity.GitConnectorService.base.rest.AbstractRestService;
import org.jazzcommunity.GitConnectorService.base.rest.RestActionBuilder;

public class RestFactory implements ServiceFactory {
    protected final Class<? extends AbstractRestService> serviceClass;
    protected final String path;

    public RestFactory(String path, Class<? extends AbstractRestService> serviceClass) {
        this.serviceClass = serviceClass;
        this.path = path;
    }

    @Override
    public RestActionBuilder getBuilder() {
        return new RestActionBuilder(this.serviceClass, path);
    }

    @Override
    public String getPath() {
        return path;
    }
}

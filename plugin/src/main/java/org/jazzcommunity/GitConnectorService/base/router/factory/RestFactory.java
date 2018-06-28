package org.jazzcommunity.GitConnectorService.base.router.factory;

import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import org.jazzcommunity.GitConnectorService.base.rest.RestActionBuilder;

public class RestFactory implements ServiceFactory {
    protected final Class<? extends AbstractRestService> serviceClass;
    protected final String path;

    public RestFactory(Class<? extends AbstractRestService> serviceClass, String path) {
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

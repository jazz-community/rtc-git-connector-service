package org.jazzcommunity.GitConnectorService.base.router.factory;

public interface ServiceFactory {
    org.jazzcommunity.GitConnectorService.base.rest.RestActionBuilder getBuilder();
    String getPath();
}

package org.jazzcommunity.GitConnectorService.oslc.type;

import org.modelmapper.ModelMapper;

public class ContributorBuilder {
    private ContributorBuilder() {
    }

    public static <TContributor> TContributor from(String name, String url, Class<TContributor> to) {
        ContributorPrototype prototype = new ContributorPrototype(name, url);
        return new ModelMapper().map(prototype, to);
    }
}

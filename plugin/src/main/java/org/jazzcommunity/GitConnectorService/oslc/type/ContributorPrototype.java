package org.jazzcommunity.GitConnectorService.oslc.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContributorPrototype {
    private final String name;
    private final String url;
    private final ArrayList<Object> types;

    public ContributorPrototype(String name, String url) {
        this.name = name;
        this.url = url;
        // We can use this approach to keep things simple and generic because the
        // rdf:type in contributor is just static data anyway. It's not beautiful,
        // but it works just fine.
        Map<Object, Object> resources = new HashMap<>();
        resources.put("rdf:resource", "http://xmlns.com/foaf/0.1/Person");
        this.types = new ArrayList<>();
        types.add(resources);
    }

    public String getFoafName() {
        return name;
    }

    public String getRdfAbout() {
        return url;
    }

    public ArrayList<Object> getRdfType() {
        return types;
    }
}

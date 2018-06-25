package org.jazzcommunity.GitConnectorService.oslc.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RdfTypePrototype {
    private final ArrayList<Object> types;
    private final String identifier;
    private final String title;
    private final String iconUrl;

    public RdfTypePrototype(String title, String iconUrl) {
        this.title = title;
        this.identifier = title.toLowerCase();
        this.iconUrl = iconUrl;
        // Maybe we can extract the types Object as well, as we use the same code in
        // ContributorPrototype
        Map<Object, Object> resources = new HashMap<>();
        resources.put("rdf:resource", "http://jazz.net/xmlns/prod/jazz/rtc/cm/1.0/Type");
        this.types = new ArrayList<>();
        types.add(resources);
    }

    public String getDctermsIdentifier() {
        return identifier;
    }

    public String getDctermsTitle() {
        return title;
    }

    public ArrayList<Object> getRdfType() {
        return types;
    }

    public String getRtcCmIconUrl() {
        return iconUrl;
    }
}

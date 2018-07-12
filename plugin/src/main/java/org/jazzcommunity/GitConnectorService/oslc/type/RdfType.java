package org.jazzcommunity.GitConnectorService.oslc.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// We can use this approach to keep things simple and generic because the
// rdf:type in contributor is just static data anyway. It's not beautiful,
// but it works just fine.
public class RdfType {
  List<Object> types = new ArrayList<>();

  public RdfType(String resource) {
    HashMap<Object, Object> resources = new HashMap<>();
    resources.put("rdf:resource", resource);
    types.add(resources);
  }

  public List<Object> getTypes() {
    return types;
  }
}

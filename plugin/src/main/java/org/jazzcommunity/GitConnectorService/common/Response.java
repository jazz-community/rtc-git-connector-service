package org.jazzcommunity.GitConnectorService.common;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.eclipse.persistence.jaxb.JAXBContext;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

public final class Response {
  // I know this isn't nice, but it's the most sensible way to create as few contexts as possible
  // Creating marshallers on the other hand is cheap, so this is used as a cache for created
  // marshallers
  // https://stackoverflow.com/questions/7400422/jaxb-creating-context-and-marshallers-cost
  private static final Map<Class, JAXBContext> CONTEXTS = new HashMap<>();

  private Response() {}

  public static <T> Marshaller xmlMarshallFactory(Class<T> type) throws JAXBException {
    // This _must_ use the org.eclipse.persistence JAXB Context for additional features
    if (!CONTEXTS.containsKey(type)) {
      org.eclipse.persistence.jaxb.JAXBContext context =
          (JAXBContext)
              JAXBContextFactory.createContext(new Class[] {type}, Collections.emptyMap());
      CONTEXTS.put(type, context);
    }
    Marshaller marshaller = CONTEXTS.get(type).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    return marshaller;
  }
}

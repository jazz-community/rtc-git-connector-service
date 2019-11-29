package org.jazzcommunity.GitConnectorService.common;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.eclipse.persistence.jaxb.JAXBContextFactory;

public final class Response {
  private Response() {}

  // turn this into a factory method for use from different services
  public static <T> void marshallXml(HttpServletResponse response, T answer)
      throws JAXBException, IOException {
    // This must use the org.eclipse.persistence JAXB Context for additional features
    Marshaller marshaller =
        JAXBContextFactory.createContext(new Class[] {answer.getClass()}, Collections.emptyMap())
            .createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
    marshaller.marshal(answer, response.getWriter());
  }
}

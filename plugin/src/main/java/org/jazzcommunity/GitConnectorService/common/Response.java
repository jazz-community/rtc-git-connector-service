package org.jazzcommunity.GitConnectorService.common;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public final class Response {
  private Response() {}

  public static <T> void marshallXml(HttpServletResponse response, T answer)
      throws JAXBException, IOException {
    Marshaller marshaller = JAXBContext.newInstance(answer.getClass()).createMarshaller();
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    marshaller.marshal(answer, response.getWriter());
  }
}

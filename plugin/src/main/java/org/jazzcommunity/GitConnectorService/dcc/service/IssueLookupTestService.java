package org.jazzcommunity.GitConnectorService.dcc.service;

import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import org.apache.commons.logging.Log;
import org.apache.http.entity.ContentType;
import org.jazzcommunity.GitConnectorService.dcc.xml.Issues;

public class IssueLookupTestService extends AbstractRestService {

  public IssueLookupTestService(
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      RestRequest restRequest,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(log, request, response, restRequest, parentService, pathParameters);
  }

  @Override
  public void execute() throws Exception {
    response.setContentType(ContentType.APPLICATION_XML.toString());
    response.setCharacterEncoding("UTF-8");
    JAXBContext.newInstance(Issues.class).createMarshaller();
  }

  public String encodeProject(URL url) throws UnsupportedEncodingException {
    String path = url.getPath();
    // remove leading slash and optional file ending
    path = path.replaceFirst("^\\/", "").replaceAll(".git$", "");
    // url-encode project path so that it can be used for navigation in gitlab
    return URLEncoder.encode(path, "UTF-8");
  }
}

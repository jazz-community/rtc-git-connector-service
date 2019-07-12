package org.jazzcommunity.GitConnectorService.ccm.service.resource;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import java.net.URL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

public class ImageService extends AbstractRestService {
  public ImageService(
      String uri,
      Log log,
      HttpServletRequest request,
      HttpServletResponse response,
      TeamRawService parentService,
      PathParameters pathParameters) {
    super(uri, log, request, response, parentService, pathParameters);
  }

  @Override
  public void execute() throws Exception {
    try {
      String filename = String.format("images/%s", pathParameters.get("filename"));
      URL url = Resources.getResource(filename);
      ByteSource source = Resources.asByteSource(url);

      response.setContentType(MediaType.ANY_IMAGE_TYPE.toString());
      source.copyTo(response.getOutputStream());
      response.getOutputStream().close();
    } catch (IllegalArgumentException e) {
      response.setStatus(HttpStatus.SC_NOT_FOUND);
    }
  }
}

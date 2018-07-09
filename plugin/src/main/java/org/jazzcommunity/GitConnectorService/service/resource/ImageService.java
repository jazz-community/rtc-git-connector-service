package org.jazzcommunity.GitConnectorService.service.resource;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import org.apache.commons.logging.Log;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;

public class ImageService extends AbstractRestService {
    public ImageService(
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
        try {
            String filename = String.format("images/%s", pathParameters.get("filename"));
            URL url = Resources.getResource(filename);
            ByteSource source = Resources.asByteSource(url);

            response.setContentType(MediaType.ANY_IMAGE_TYPE.toString());
            source.copyTo(response.getOutputStream());
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.SC_NOT_FOUND);
        }
    }
}

package org.jazzcommunity.GitConnectorService.ccm.service;

import com.google.common.net.MediaType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.parameters.PathParameters;
import com.siemens.bt.jazz.services.base.rest.parameters.RestRequest;
import com.siemens.bt.jazz.services.base.rest.service.AbstractRestService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;

public class VersionService extends AbstractRestService {
  private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public VersionService(
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
    Version version = FrameworkUtil.getBundle(getClass()).getVersion();
    response.setContentType(MediaType.JSON_UTF_8.toString());
    response.getWriter().write(gson.toJson(new VersionAdapter(version)));
  }

  private static final class VersionAdapter {
    private final int major;
    private final int minor;
    private final int micro;
    private final String qualifier;
    private final String version;

    public VersionAdapter(Version version) {
      major = version.getMajor();
      minor = version.getMinor();
      micro = version.getMicro();
      qualifier = version.getQualifier();
      this.version = version.toString();
    }
  }
}

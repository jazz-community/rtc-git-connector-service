package org.jazzcommunity.GitConnectorService.builder;

import com.google.gson.*;
import com.ibm.team.repository.service.TeamRawService;
import com.siemens.bt.jazz.services.base.rest.AbstractRestService;
import com.siemens.bt.jazz.services.base.rest.RestRequest;
import org.apache.commons.logging.Log;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.Version;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VersionService extends AbstractRestService {
    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public VersionService(Log log, HttpServletRequest request, HttpServletResponse response, RestRequest restRequest, TeamRawService parentService) {
        super(log, request, response, restRequest, parentService);
    }

    @Override
    public void execute() throws Exception {
        Version version = FrameworkUtil.getBundle(getClass()).getVersion();
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

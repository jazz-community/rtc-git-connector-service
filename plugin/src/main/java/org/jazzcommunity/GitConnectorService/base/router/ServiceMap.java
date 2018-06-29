package org.jazzcommunity.GitConnectorService.base.router;

import com.ibm.team.jfs.app.http.util.HttpConstants;
import org.jazzcommunity.GitConnectorService.base.rest.DefaultRestService;
import org.jazzcommunity.GitConnectorService.base.router.factory.RestFactory;
import org.jazzcommunity.GitConnectorService.base.router.factory.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ServiceMap {
    private Map<String, Map<HttpConstants.HttpMethod, ServiceFactory>> map = new HashMap<>();

    public ServiceFactory getFactory(HttpServletRequest request, String uri) {
        for (String path : map.keySet()) {
            String regex = path.replaceAll("\\{[^\\/]+\\}", "([^\\/]+)");
            Pattern pattern = Pattern.compile(regex);
            if (pattern.matcher(uri).matches()) {
                return map
                        .get(path)
                        .get(HttpConstants.HttpMethod.fromString(request.getMethod()));
            }
        }

        return new RestFactory("", DefaultRestService.class);
    }

    public void add(HttpConstants.HttpMethod method, String path, ServiceFactory serviceFactory) {
        if (!map.containsKey(path)) {
            map.put(path, new HashMap<HttpConstants.HttpMethod, ServiceFactory>());
        }

        map.get(path).put(method, serviceFactory);
    }
}

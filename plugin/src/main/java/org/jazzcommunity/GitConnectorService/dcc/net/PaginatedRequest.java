package org.jazzcommunity.GitConnectorService.dcc.net;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * Adapter class for a normal servlet request, providing pagination information for data collection
 * jobs. This is necessary for breaking down large payloads, but support is not directly built into
 * the dcc jobs. Instead, only rudimentary support for paginated responses with href and rel are
 * possible. Subsequent calls are made to the url returned to dcc, but pagination and url writing
 * has to be handled here.
 */
public final class PaginatedRequest {

  private final HttpServletRequest request;
  private final int start;
  private final int end;

  private PaginatedRequest(HttpServletRequest request, int start, int end) {
    this.request = request;
    this.start = start;
    this.end = end;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public boolean isPaginated() {
    return start != 0;
  }

  public URI getNext() throws URISyntaxException {
    String query = request.getQueryString() != null ? request.getQueryString() : "";
    List<NameValuePair> pairs = URLEncodedUtils.parse(query, Charset.forName("UTF-8"));

    return new URIBuilder()
        .setScheme(request.getScheme())
        .setHost(request.getRemoteHost())
        .setPath(request.getRequestURI())
        .setParameters(pairs)
        .setParameter("size", String.valueOf(end - start))
        // TODO: This might have to be end + 1
        .setParameter("pos", String.valueOf(end))
        .build();
  }

  @Override
  public String toString() {
    return "PaginatedRequest{" + "start=" + start + ", end=" + end + '}';
  }

  public static PaginatedRequest fromRequest(HttpServletRequest request) {
    String size = request.getParameter("size");
    String pos = request.getParameter("pos");

    if (size == null) {
      return new PaginatedRequest(request, 0, 0);
    }

    if (pos == null) {
      return new PaginatedRequest(request, 0, Integer.parseInt(size));
    }

    int s = Integer.parseInt(size);
    int p = Integer.parseInt(pos);
    return new PaginatedRequest(request, p, p + s);
  }
}

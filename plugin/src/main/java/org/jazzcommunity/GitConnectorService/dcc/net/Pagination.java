package org.jazzcommunity.GitConnectorService.dcc.net;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;

public final class Pagination {

  private final String uri;
  private final List<NameValuePair> pairs;
  private final int start;
  private final int end;

  private Pagination(String uri, List<NameValuePair> pairs, int start, int end) {
    this.uri = uri;
    this.pairs = pairs;
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
    return new URIBuilder()
        .setPath(uri)
        .setParameters(pairs)
        .setParameter("size", String.valueOf(end - start))
        // TODO: This might have to be end + 1
        .setParameter("pos", String.valueOf(end))
        .build();
  }

  @Override
  public String toString() {
    return "Pagination{" + "start=" + start + ", end=" + end + '}';
  }

  public static Pagination fromRequest(HttpServletRequest request) {
    String size = request.getParameter("size");
    String pos = request.getParameter("pos");
    List<NameValuePair> pairs =
        URLEncodedUtils.parse(request.getQueryString(), Charset.forName("UTF-8"));

    if (size == null) {
      return new Pagination(request.getRequestURI(), pairs, 0, 0);
    } else if (size != null && pos == null) {
      return new Pagination(request.getRequestURI(), pairs, 0, Integer.parseInt(size));
    }
    int s = Integer.parseInt(size);
    int p = Integer.parseInt(pos);
    return new Pagination(request.getRequestURI(), pairs, p, p + s);
  }
}

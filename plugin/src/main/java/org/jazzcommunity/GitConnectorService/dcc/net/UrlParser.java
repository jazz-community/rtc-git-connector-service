package org.jazzcommunity.GitConnectorService.dcc.net;

import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * This is the reverse counter-part of org.jazzcommunity.GitConnectorService.net.UrlBuilder for
 * parsing url information out of a link, so that the information can be used with the minigit
 * library for fetching json data from gitlab/hub
 */
public class UrlParser {

  /*
   * starting with anything up until the name of the service
   * 1: name of git service
   * 2: url of git service
   * 3: project id
   * 4: type of git artifact
   * 5: relative id of git artifact
   */
  private static String REMOTE_PATTERN =
      //      1           2                     3           4           5
      ".*\\/([^\\/]*)\\/([^\\/]*)\\/project\\/([^\\/]*)\\/([^\\/]*)\\/([^\\/]*)\\/link$";

  public static RemoteUrl parseRemote(URI uri) {
    Pattern pattern = Pattern.compile(REMOTE_PATTERN);
    Matcher matcher = pattern.matcher(uri.getPath());
    matcher.find();
    return new RemoteUrl(
        matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
  }

  public static DataUrl parseData(URI uri) {
    List<NameValuePair> parameters = URLEncodedUtils.parse(uri, "UTF-8");

    for (NameValuePair parameter : parameters) {
      if (parameter.getName().contains("value")) {
        return new DataUrl(parameter.getValue());
      }
    }

    String error =
        String.format("Invalid data url. Doesn't contain commit data. URL: %s", uri.toString());
    throw new IllegalArgumentException(error);
  }
}

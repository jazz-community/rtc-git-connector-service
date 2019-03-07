package org.jazzcommunity.GitConnectorService.dcc.data;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import javax.xml.bind.DatatypeConverter;

// actually, this should probably be renamed back again, it should only just decode the data,
// conversion to json can / should be handled somewhere else
public class BinaryJsonDecoder {
  /**
   * This uses the java 7 version of handling base64 data. There are reports about this not working
   * with java versions newer than java 8. For newer java versions, please use java.util.Base64 for
   * handling base 64 encoded data.
   *
   * <p>see:
   * https://stackoverflow.com/questions/14413169/which-java-library-provides-base64-encoding-decoding
   */
  public static String decode(String payload) throws IOException {
    String decoded = base64UrlDecoder(payload);
    byte[] parsed = DatatypeConverter.parseBase64Binary(decoded);
    return decompress(parsed);
  }

  private static String decompress(byte[] data) throws IOException {
    try (ByteArrayInputStream bytes = new ByteArrayInputStream(data);
        GZIPInputStream stream = new GZIPInputStream(bytes)) {
      ByteSource source =
          new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
              return stream;
            }
          };

      return source.asCharSource(Charsets.UTF_8).read();
    }
  }

  private static String base64UrlDecoder(String string) {
    return string.replace('-', '+').replace('_', '/').replace('.', '=');
  }
}

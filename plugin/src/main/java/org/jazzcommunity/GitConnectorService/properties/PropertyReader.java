package org.jazzcommunity.GitConnectorService.properties;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertyReader {
  private final Properties properties;

  public PropertyReader() throws IOException {
    this("config.properties");
  }

  public PropertyReader(String fileName) throws IOException {
    URL resource = Resources.getResource(fileName);
    ByteSource source = Resources.asByteSource(resource);
    properties = new Properties();
    try (InputStream stream = source.openStream()) {
      properties.load(stream);
    }
  }

  public String get(String key) {
    return properties.getProperty(key, "INVALID_PROPERTY_KEY");
  }
}

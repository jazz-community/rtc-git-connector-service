package org.jazzcommunity.GitConnectorService.properties;

import com.google.common.io.ByteSource;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class PropertyReader {
  private final Properties properties;

  public PropertyReader() {
    this("config.properties");
  }

  public PropertyReader(String fileName) {
    URL resource = Resources.getResource(fileName);
    ByteSource source = Resources.asByteSource(resource);
    properties = new Properties();
    try (InputStream stream = source.openStream()) {
      properties.load(stream);
    } catch (IOException e) {
      // If this exception is thrown, it is most likely a configuration issue.
      // There should never be a case where reading from a non-existing configuration
      // file should happen.
      throw new RuntimeException("Configuration file not found. Properties will be invalid");
    }
  }

  public String get(String key) {
    return properties.getProperty(key, "INVALID_PROPERTY_KEY");
  }
}

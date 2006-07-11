package org.apache.myfaces.tobago;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * User: lofwyr
 * Date: 10.07.2006 17:42:19
 */
public class TobagoVersion {


  private static final Log LOG = LogFactory.getLog(TobagoVersion.class);

  private String name;

  public TobagoVersion() {
    try {
      InputStream pom = getClass().getClassLoader().getResourceAsStream(
          "META-INF/maven/org.apache.myfaces.tobago/tobago-core/pom.properties");
      Properties properties = new Properties();
      properties.load(pom);
      name = properties.getProperty("version");
    } catch (IOException e) {
      LOG.warn("No version info found.", e);
    }
  }

  public String getName() {
    return name;
  }
}

package org.apache.myfaces.tobago.example.demo;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The server info class makes some information about the system and application available in the demo.
 * This is enabled by default, but can be disabled by configuration in a properties file with the key
 * <code>{@value #ENABLED_KEY}</code>.
 * The default file name is <code>{@value #CONFIG_FILE_DEFAULT}</code>.
 * The file name can be changed with a system property with name <code>{@value #CONFIG_FILE}</code>.
 *
 */
public class ServerInfo {

  private static final Log LOG = LogFactory.getLog(ServerInfo.class);

  private static final String CONFIG_FILE = "org.apache.myfaces.tobago.example.demo.config.file";
  private static final String CONFIG_FILE_DEFAULT = "/etc/tobago-example-demo.properties";
  private static final String ENABLED_KEY = "server.info.enabled";

  private String version;

  /**
   * Enabled the Server Info. May be disabled for security reasons. Default is true.
   */
  private boolean enabled = true;

  public ServerInfo() {
    String file = System.getProperty(CONFIG_FILE);
    try {
      if (file == null) {
        file = CONFIG_FILE_DEFAULT;
      }
      LOG.info("Loading config from file '" + file + "'");
      Properties config = new Properties();
      config.load(new FileInputStream(file));
      enabled = Boolean.parseBoolean(config.getProperty(ENABLED_KEY));
    } catch (IOException e) {
      LOG.warn("Can't load config: " + e.getMessage());
    }
    // the tobago version should be set in any case
    LOG.info("server.info.enabled=" + enabled);
    version = Package.getPackage("org.apache.myfaces.tobago.component").getImplementationVersion();
  }

  public String getServerInfo() {
    if (enabled) {
      return ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getServerInfo();
    } else {
      return null;
    }
  }

  public Properties getSystemProperties() {
    if (enabled) {
      return System.getProperties();
    } else {
      return null;
    }
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}

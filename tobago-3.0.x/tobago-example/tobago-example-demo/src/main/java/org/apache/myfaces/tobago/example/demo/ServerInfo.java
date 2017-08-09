/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * The server info class makes some information about the system and application available in the demo.
 * This is enabled by default, but can be disabled by configuration in a properties file with the key
 * <code>{@value #ENABLED_KEY}</code>.
 * The default file name is <code>{@value #CONFIG_FILE_DEFAULT}</code>.
 * The file name can be changed with a system property with name <code>{@value #CONFIG_FILE}</code>.
 */
@ApplicationScoped
@Named("info")
public class ServerInfo {

  private static final Logger LOG = LoggerFactory.getLogger(ServerInfo.class);

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
      final Properties config = new Properties();
      config.load(new FileInputStream(file));
      enabled = Boolean.parseBoolean(config.getProperty(ENABLED_KEY));
    } catch (final IOException e) {
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
    return enabled ? System.getProperties() : null;
  }

  public List<Map.Entry<Object, Object>> getSystemPropertiesAsList() {
    return enabled ? new ArrayList<Map.Entry<Object, Object>>(getSystemProperties().entrySet()) : null;
  }

  public String getVersion() {
    return version;
  }

  public String getJsfTitle() {
    return enabled ? FacesContext.class.getPackage().getImplementationTitle() : null;
  }

  public String getJsfVersion() {
    return enabled ? FacesContext.class.getPackage().getImplementationVersion() : null;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getStableVersion() {
    if (version.endsWith("-SNAPSHOT")) {
      StringTokenizer tokenizer = new StringTokenizer(version, ".-");
      int major = Integer.parseInt(tokenizer.nextToken());
      int minor = Integer.parseInt(tokenizer.nextToken());
      int fix = Integer.parseInt(tokenizer.nextToken());
      if (fix == 0) {
        return major + "." + (minor - 1) + ".0";
      } else {
        return major + "." + minor + "." + (fix - 1);
      }
    } else {
      return version;
    }
  }
}

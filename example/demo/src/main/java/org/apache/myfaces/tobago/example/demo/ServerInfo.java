package org.apache.myfaces.tobago.example.demo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

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

public class ServerInfo {

  private static final Log LOG = LogFactory.getLog(ServerInfo.class);

  private String version;

  private boolean enabled;

  public ServerInfo() {
    InputStream pom = null;
    try {
      pom = getClass().getClassLoader().getResourceAsStream(
          "META-INF/maven/org.apache.myfaces.tobago/tobago-core/pom.properties");
      Properties properties = new Properties();
      properties.load(pom);
      version = properties.getProperty("version");
    } catch (IOException e) {
      LOG.warn("No version info found.", e);
    } finally {
      IOUtils.closeQuietly(pom);
    }
  }

  public String getServerInfo() {
    if (enabled) {
      return ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getServerInfo();
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

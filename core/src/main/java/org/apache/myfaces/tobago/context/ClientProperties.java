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

package org.apache.myfaces.tobago.context;

/*
 * Created: 23.07.2002 14:21:58
 * $Id$
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CLIENT_PROPERTIES;
import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClientProperties implements Serializable {
  private static final long serialVersionUID = -6719319982485268698L;
  private static final String CLIENT_PROPERTIES_IN_SESSION = ClientProperties.class.getName();

  private static final Log LOG = LogFactory.getLog(ClientProperties.class);

  private String contentType = "html";
  // TODO transient
  private Theme theme;
  private UserAgent userAgent = UserAgent.DEFAULT;
  private boolean debugMode;

  private String id;

  private ClientProperties(TobagoConfig tobagoConfig) {
    theme = tobagoConfig.getDefaultTheme();
    updateId();
  }

  private ClientProperties(FacesContext facesContext) {

    ExternalContext externalContext = facesContext.getExternalContext();

    // content type
    String accept = (String) externalContext.getRequestHeaderMap().get("Accept");
    if (accept != null) {
      if (accept.indexOf("text/vnd.wap.wml") > -1) {
        contentType = "wml";
      }
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("contentType='" + contentType + "' from header "
          + "Accept='" + accept + "'");
    }

    // user agent
    String requestUserAgent
        = (String) externalContext.getRequestHeaderMap().get("User-Agent");
    this.userAgent = UserAgent.getInstance(requestUserAgent);
    if (LOG.isInfoEnabled()) {
      LOG.info("userAgent='" + this.userAgent + "' from header "
          + "'User-Agent: " + requestUserAgent + "'");
    }
    // debug mode
    // to enable the debug mode for a user, put a
    // "to-ba-go" custom locale to your browser
    String acceptLanguage
        = (String) externalContext.getRequestHeaderMap().get("Accept-Language");
    if (acceptLanguage != null) {
      this.debugMode = acceptLanguage.indexOf("to-ba-go") > -1;
    }
    if (LOG.isInfoEnabled()) {
      LOG.info("debug-mode=" + debugMode);
    }
    // theme
    String requestTheme
        = (String) externalContext.getRequestParameterMap().get("tobago.theme");
    TobagoConfig config = TobagoConfig.getInstance(facesContext);
    this.theme = config.getTheme(requestTheme);
    if (LOG.isInfoEnabled()) {
      LOG.info("theme='" + theme.getName() + "' from requestParameter "
          + "tobago.theme='" + requestTheme + "'");
    }
    updateId();
  }

  private void updateId() {

    StringBuilder buffer = new StringBuilder();
    buffer.append(getContentType());
    buffer.append('/');
    buffer.append(getTheme().getName());
    buffer.append('/');
    buffer.append(getUserAgent());
    id = buffer.toString();
    UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
    if (viewRoot instanceof org.apache.myfaces.tobago.component.UIViewRoot) {
      ((org.apache.myfaces.tobago.component.UIViewRoot) viewRoot).updateRendererCachePrefix();
    }
  }

  public static ClientProperties getDefaultInstance(FacesContext facesContext) {
    return new ClientProperties(TobagoConfig.getInstance(facesContext));
  }

  public static ClientProperties getInstance(UIViewRoot viewRoot) {

    ClientProperties instance = (ClientProperties)
        viewRoot.getAttributes().get(ATTR_CLIENT_PROPERTIES);
    if (instance == null) {
      LOG.error("No ClientProperties instance found creating new one");
      return getInstance(FacesContext.getCurrentInstance());
    }
    return instance;
  }

  public static ClientProperties getInstance(FacesContext facesContext) {

    ExternalContext context = facesContext.getExternalContext();

    boolean hasSession = context.getSession(false) != null;

    ClientProperties client = null;

    if (hasSession) {
      client = (ClientProperties) context.getSessionMap().get(
          CLIENT_PROPERTIES_IN_SESSION);
    }
    if (client == null) {
      client = new ClientProperties(facesContext);
      if (hasSession) {
        context.getSessionMap().put(CLIENT_PROPERTIES_IN_SESSION, client);
      }
    }
    return client;
  }

  public static List<String> getLocaleList(
      Locale locale, boolean propertyPathMode) {

    String string = locale.toString();
    String prefix = propertyPathMode ? "" : "_";
    List<String> locales = new ArrayList<String>(4);
    locales.add(prefix + string);
    int underscore;
    while ((underscore = string.lastIndexOf('_')) > 0) {
      string = string.substring(0, underscore);
      locales.add(prefix + string);
    }

    locales.add(propertyPathMode ? "default" : ""); // default suffix

    return locales;
  }

  public String getId() {
    return id;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
    updateId();
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
    updateId();
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(UserAgent userAgent) {
    this.userAgent = userAgent;
    updateId();
  }

  public boolean isDebugMode() {
    return debugMode;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

}

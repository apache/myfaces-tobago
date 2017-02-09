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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.util.CookieUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@RequestScoped
public class TobagoContext {

  public static final String BEAN_NAME = "tobagoContext";

  private static final Logger LOG = LoggerFactory.getLogger(TobagoContext.class);

  private static final TobagoResourceBundle RESOURCE_BUNDLE = new TobagoResourceBundle();
  private static final TobagoMessageBundle MESSAGE_BUNDLE = new TobagoMessageBundle();

  private Theme theme;
  private UserAgent userAgent;

  public TobagoResourceBundle getResourceBundle() {
    return RESOURCE_BUNDLE;
  }

  public TobagoMessageBundle getMessageBundle() {
    return MESSAGE_BUNDLE;
  }

  public TobagoConfig getTobagoConfig() {
    return TobagoConfig.getInstance(FacesContext.getCurrentInstance());
  }

  public Theme getTheme() {

    if (theme == null) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final ExternalContext externalContext = facesContext.getExternalContext();

      final String themeName;
      Object request = externalContext.getRequest();
      if (request instanceof HttpServletRequest) {
        themeName = CookieUtils.getThemeNameFromCookie((HttpServletRequest) request);
      } else {
        themeName = null;
      }

      theme = getTobagoConfig().getTheme(themeName);
      if (LOG.isDebugEnabled()) {
        LOG.debug("theme='{}'", theme.getName());
      }
    }

    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  public UserAgent getUserAgent() {

    if (userAgent == null) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final ExternalContext externalContext = facesContext.getExternalContext();

      final String requestUserAgent = externalContext.getRequestHeaderMap().get("User-Agent");
      userAgent = UserAgent.getInstance(requestUserAgent);
      if (LOG.isDebugEnabled()) {
        LOG.debug("userAgent='" + userAgent + "' from header " + "'User-Agent: " + requestUserAgent + "'");
      }
    }

    return userAgent;
  }

  public void setUserAgent(UserAgent userAgent) {
    this.userAgent = userAgent;
  }

  public static TobagoContext getInstance(FacesContext facesContext) {
    return (TobagoContext) VariableResolverUtils.resolveVariable(facesContext, BEAN_NAME);
  }
}

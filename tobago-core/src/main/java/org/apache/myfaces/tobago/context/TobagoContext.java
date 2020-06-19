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

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.ResourceBundle;

@Named
@RequestScoped
public class TobagoContext implements Serializable {

  public static final String BEAN_NAME = "tobagoContext";

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private TobagoConfig tobagoConfig;
  private Theme theme;
  private UserAgent userAgent;
  private String focusId;
  private String enctype;

  /**
   * @deprecated since 5.0.0. Please use {@link org.apache.myfaces.tobago.util.ResourceUtils#getString} in Java or
   * #{tobagoResourceBundle.key} in Facelets.
   */
  @Deprecated
  public ResourceBundle getResourceBundle() {
    final UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
    final Locale locale = viewRoot != null
        ? viewRoot.getLocale() : FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
    return ResourceBundle.getBundle("tobagoResourceBundle", locale);
  }

  /**
   * @deprecated since 5.0.0. Please use {@link org.apache.myfaces.tobago.util.MessageUtils}.
   */
  @Deprecated
  public ResourceBundle getMessageBundle() {
    final UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
    final Locale locale = viewRoot != null
        ? viewRoot.getLocale() : FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
    return ResourceBundle.getBundle("org.apache.myfaces.tobago.context.TobagoMessage", locale);
  }

  /**
   * @deprecated since 5.0.0. Please get/inject {@link TobagoConfig} directly by CDI.
   */
  @Deprecated
  public TobagoConfig getTobagoConfig() {
    return tobagoConfig;
  }

  public Theme getTheme() {

    if (theme == null) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final ExternalContext externalContext = facesContext.getExternalContext();

      final String themeName;
      final Object request = externalContext.getRequest();
      if (request instanceof HttpServletRequest) {
        themeName = CookieUtils.getThemeNameFromCookie((HttpServletRequest) request);
      } else {
        themeName = null;
      }

      theme = tobagoConfig.getTheme(themeName);
      if (LOG.isDebugEnabled()) {
        LOG.debug("theme='{}'", theme.getName());
      }
    }

    return theme;
  }

  public void setTheme(final Theme theme) {
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

  public void setUserAgent(final UserAgent userAgent) {
    this.userAgent = userAgent;
  }

  public String getFocusId() {
    return focusId;
  }

  public void setFocusId(final String focusId) {
    this.focusId = focusId;
  }

  public String getEnctype() {
    return enctype;
  }

  public void setEnctype(final String enctype) {
    this.enctype = enctype;
  }

  public static TobagoContext getInstance(final FacesContext facesContext) {
    return (TobagoContext) VariableResolverUtils.resolveVariable(facesContext, BEAN_NAME);
  }
}

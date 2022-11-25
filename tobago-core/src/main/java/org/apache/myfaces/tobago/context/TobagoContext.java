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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.util.CookieUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Named
@RequestScoped
public class TobagoContext implements Serializable {

  public static final String BEAN_NAME = "tobagoContext";

  public static final String FOCUS_ID_KEY = "tobago.focusId";
  public static final String ENCTYPE_KEY = "tobago.enctype";

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * @deprecated since 5.0.0. Please use {@link org.apache.myfaces.tobago.util.ResourceUtils#getString} in Java or
   * #{tobagoResourceBundle.key} in Facelets.
   */
  @Deprecated
  public ResourceBundle getResourceBundle() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    final Locale locale = viewRoot != null
        ? viewRoot.getLocale() : facesContext.getApplication().getDefaultLocale();
    return ResourceBundle.getBundle("tobagoResourceBundle", locale);
  }

  /**
   * @deprecated since 5.0.0. Please use {@link org.apache.myfaces.tobago.util.MessageUtils}.
   */
  @Deprecated
  public ResourceBundle getMessageBundle() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    final Locale locale = viewRoot != null
        ? viewRoot.getLocale() : facesContext.getApplication().getDefaultLocale();
    return ResourceBundle.getBundle("org.apache.myfaces.tobago.context.TobagoMessage", locale);
  }

  public TobagoConfig getTobagoConfig() {
    return TobagoConfig.getInstance(FacesContext.getCurrentInstance());
  }

  public Theme getTheme() {
    // load it from faces context
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    Theme theme = (Theme) facesContext.getAttributes().get(Theme.THEME_KEY);
    if (theme != null) {
      return theme;
    }

    final ExternalContext externalContext = facesContext.getExternalContext();
    final Object request = externalContext.getRequest();
    final Object session = externalContext.getSession(false);

    // load theme from session
    if (session instanceof HttpSession && getTobagoConfig().isThemeSession()) {
      final String themeName = (String) ((HttpSession) session).getAttribute(Theme.THEME_KEY);
      theme = getTobagoConfig().getThemeIfExists(themeName);
      if (LOG.isDebugEnabled()) {
        LOG.debug("from session theme='{}'", theme.getName());
      }
    }

    // or load it from cookie
    if (theme == null) {
      if (request instanceof HttpServletRequest && getTobagoConfig().isThemeCookie()) {
        final String themeName = CookieUtils.getThemeNameFromCookie((HttpServletRequest) request);
        theme = getTobagoConfig().getTheme(themeName);
        if (LOG.isDebugEnabled()) {
          LOG.debug("from cookie theme='{}'", theme.getName());
        }
      }
    }

    // or use default
    if (theme == null) {
      theme = getTobagoConfig().getDefaultTheme();
    }

    facesContext.getAttributes().put(Theme.THEME_KEY, theme);

    return theme;
  }

  public void setTheme(final Theme theme) {
    // save in faces context
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    facesContext.getAttributes().put(Theme.THEME_KEY, theme);

    final ExternalContext externalContext = facesContext.getExternalContext();
    final Object request = externalContext.getRequest();
    final Object response = externalContext.getResponse();
    final Object session = externalContext.getSession(false);

    // save theme in cookie
    if (response instanceof HttpServletResponse && request instanceof HttpServletRequest
        && getTobagoConfig().isThemeCookie()) {
      CookieUtils.setThemeNameToCookie((HttpServletRequest) request, (HttpServletResponse) response, theme.getName());
    }
    // save theme in session
    if (session instanceof HttpSession && getTobagoConfig().isThemeSession()) {
      ((HttpSession) session).setAttribute(Theme.THEME_KEY, theme.getName());
    }
  }

  public UserAgent getUserAgent() {

    UserAgent userAgent;

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();

    final String requestUserAgent = externalContext.getRequestHeaderMap().get("User-Agent");
    userAgent = UserAgent.getInstance(requestUserAgent);
    if (LOG.isDebugEnabled()) {
      LOG.debug("userAgent='" + userAgent + "' from header " + "'User-Agent: " + requestUserAgent + "'");
    }

    return userAgent;
  }

  public void setUserAgent(final UserAgent userAgent) {
    LOG.warn("Setting user agent ignored! param={}", userAgent);
  }

  public String getFocusId() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return (String) facesContext.getAttributes().get(FOCUS_ID_KEY);
  }

  public void setFocusId(final String focusId) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    facesContext.getAttributes().put(FOCUS_ID_KEY, focusId);
  }

  public String getEnctype() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return (String) facesContext.getAttributes().get(ENCTYPE_KEY);
  }

  public void setEnctype(final String enctype) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    facesContext.getAttributes().put(ENCTYPE_KEY, enctype);
  }

  /**
   * @since 5.4.0
   */
  public Boolean getFocusOnError() {
    final Boolean focusOnError
        = (Boolean) FacesContext.getCurrentInstance().getAttributes().get(Attributes.focusOnError);
    if (focusOnError != null) {
      return focusOnError;
    } else {
      // todo: get default from TobagoConfig
      return true;
    }
  }

  /**
   * @since 5.4.0
   */
  public void setFocusOnError(final Boolean focusOnError) {
    FacesContext.getCurrentInstance().getAttributes().put(Attributes.focusOnError, focusOnError);
  }

  /**
   * @since 5.4.0
   */
  public Integer getWaitOverlayDelayFull() {
    final Integer waitOverlayDelayFull
        = (Integer) FacesContext.getCurrentInstance().getAttributes().get(Attributes.waitOverlayDelayFull);
    if (waitOverlayDelayFull != null) {
      return waitOverlayDelayFull;
    } else {
      // todo: get default from TobagoConfig
      return 1000;
    }
  }

  /**
   * @since 5.4.0
   */
  public void setWaitOverlayDelayFull(final Integer waitOverlayDelayFull) {
    FacesContext.getCurrentInstance().getAttributes().put(Attributes.waitOverlayDelayFull, waitOverlayDelayFull);
  }

  /**
   * @since 5.4.0
   */
  public Integer getWaitOverlayDelayAjax() {
    final Integer waitOverlayDelayAjax
        = (Integer) FacesContext.getCurrentInstance().getAttributes().get(Attributes.waitOverlayDelayAjax);
    if (waitOverlayDelayAjax != null) {
      return waitOverlayDelayAjax;
    } else {
      // todo: get default from TobagoConfig
      return 1000;
    }
  }

  /**
   * @since 5.4.0
   */
  public void setWaitOverlayDelayAjax(final Integer waitOverlayDelayAjax) {
    FacesContext.getCurrentInstance().getAttributes().put(Attributes.waitOverlayDelayAjax, waitOverlayDelayAjax);
  }

  public static TobagoContext getInstance(final FacesContext facesContext) {
    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
    if (requestMap.containsKey(BEAN_NAME)) {
      return (TobagoContext) requestMap.get(BEAN_NAME);
    } else {
      final TobagoContext tobagoContext = new TobagoContext();
      requestMap.put(BEAN_NAME, tobagoContext);
      return tobagoContext;
    }
  }
}

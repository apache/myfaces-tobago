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

import org.apache.commons.lang.ObjectUtils;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.context.ClientPropertiesKey;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The ClientProperties contains data, which are individual for each user.
 * It is stored in the session by default, but the application can override this in the faces-config.xml.
 * <p/>
 * The managed bean name which is Tobago using for the instance is {@link #MANAGED_BEAN_NAME}.
 * <p/>
 * Please use {@link org.apache.myfaces.tobago.util.VariableResolverUtils#resolveClientProperties(FacesContext)}
 * to access to the users client properties.
 */

public class ClientProperties implements Serializable {

  /**
   * The name of the managed bean
   */
  public static final String MANAGED_BEAN_NAME = "tobagoClientProperties";

  private static final long serialVersionUID = 2L;

  private static final Logger LOG = LoggerFactory.getLogger(ClientProperties.class);

  private String contentType = "html";
  private Theme theme;
  private UserAgent userAgent = UserAgent.DEFAULT;
  private boolean debugMode;

  private Locale locale;

  private Measure verticalScrollbarWeight;
  private Measure horizontalScrollbarWeight;

  /** 
   * managed bean constructor
   */
  public ClientProperties() {
    this(FacesContext.getCurrentInstance());
  }

  /**
   * @deprecated since 1.5.
   */
  private ClientProperties(TobagoConfig tobagoConfig) {
    theme = tobagoConfig.getDefaultTheme();
    reset();
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
    if (LOG.isDebugEnabled()) {
      LOG.debug("contentType='" + contentType + "' from header " + "Accept='" + accept + "'");
    }

    // user agent
    String requestUserAgent = (String) externalContext.getRequestHeaderMap().get("User-Agent");
    this.userAgent = UserAgent.getInstance(requestUserAgent);
    if (LOG.isDebugEnabled()) {
      LOG.debug("userAgent='" + this.userAgent + "' from header " + "'User-Agent: " + requestUserAgent + "'");
    }

    // theme
    String requestTheme = (String) externalContext.getRequestParameterMap().get("tobago.theme");
    TobagoConfig config = TobagoConfig.getInstance(facesContext);
    // TODO log error if tobago config is not initialized
    this.theme = config.getTheme(requestTheme);
    if (LOG.isDebugEnabled()) {
      LOG.debug("theme='" + theme.getName() + "' from requestParameter " + "tobago.theme='" + requestTheme + "'");
    }

    reset();
  }

  /**
   * @deprecated since 1.5.
   */
  @Deprecated
  public static ClientProperties getDefaultInstance(FacesContext facesContext) {
    return new ClientProperties(TobagoConfig.getInstance(facesContext));
  }

  /**
   * @deprecated since 1.5. Please use
   * {@link #getInstance(javax.faces.context.FacesContext)}
   */
  @Deprecated
  public static ClientProperties getInstance(UIViewRoot viewRoot) {
    return getInstance(FacesContext.getCurrentInstance());
  }

  public static ClientProperties getInstance(FacesContext facesContext) {
    return (ClientProperties) VariableResolverUtils.resolveVariable(facesContext, MANAGED_BEAN_NAME);
  }

  /**
   * @deprecated since 1.5. Please use 
   * {@link org.apache.myfaces.tobago.util.LocaleUtils#getLocaleSuffixList(java.util.Locale)} 
   */
  @Deprecated
  public static List<String> getLocaleList(Locale locale, boolean propertyPathMode) {
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

  private void reset() {
    ClientPropertiesKey.reset(FacesContext.getCurrentInstance());
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
    reset();
  }

  public Theme getTheme() {
    return theme;
  }
  
  public void setTheme(Theme theme) {
    this.theme = theme;
    reset();
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(UserAgent userAgent) {
    this.userAgent = userAgent;
    reset();
  }

  public boolean isDebugMode() {
    return debugMode;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

  public Locale getLocale() {
    return locale;
  }

  /**
   * Holds the locale of the user, which is located in the UIViewRoot.
   * This setter should not be called from the application directly, 
   * but via {@link UIViewRoot#setLocale(Locale locale)} 
   */
  public void setLocale(Locale locale) {
    // set locale will be called "too often" from the JSF
    if (!ObjectUtils.equals(this.locale, locale)) {
      this.locale = locale;
      reset();
    }
  }

  public Measure getVerticalScrollbarWeight() {
    return verticalScrollbarWeight;
  }

  public void setVerticalScrollbarWeight(Measure verticalScrollbarWeight) {
    this.verticalScrollbarWeight = verticalScrollbarWeight;
  }

  public Measure getHorizontalScrollbarWeight() {
    return horizontalScrollbarWeight;
  }

  public void setHorizontalScrollbarWeight(Measure horizontalScrollbarWeight) {
    this.horizontalScrollbarWeight = horizontalScrollbarWeight;
  }

  public void updateUserAgent(FacesContext facesContext) {
    ExternalContext externalContext = facesContext.getExternalContext();
    String requestUserAgent = (String) externalContext.getRequestHeaderMap().get("User-Agent");
    final UserAgent newUserAgent = UserAgent.getInstance(requestUserAgent);
    if (newUserAgent != userAgent) {
      userAgent = newUserAgent;
      reset();
    }
  }
}

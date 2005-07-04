/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: 23.07.2002 14:21:58
 * $Id$
 */
package com.atanion.tobago.context;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.config.TobagoConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.io.Serializable;

public class ClientProperties implements Serializable {

// ///////////////////////////////////////////// constants

  private static final String CLIENT_PROPERTIES_IN_SESSION
      = ClientProperties.class.getName();

  private static final Log LOG = LogFactory.getLog(ClientProperties.class);

// ///////////////////////////////////////////// attributes

  private String contentType = "html";
  private Theme theme;
  private UserAgent userAgent = UserAgent.DEFAULT;
  private boolean debugMode;

  private String id;

// ///////////////////////////////////////////// constructors

  private ClientProperties(FacesContext facesContext) {

    ExternalContext externalContext = facesContext.getExternalContext();

    // content type
    String accept = (String) externalContext.getRequestHeaderMap().get("Accept");
    if (accept != null) {
      if (accept.indexOf("text/vnd.wap.wml") > -1) {
        contentType = "wml";
      }
    }
    LOG.info("contentType='" + contentType + "' from header "
        + "Accept='" + accept + "'");

    // user agent
    String userAgent
        = (String) externalContext.getRequestHeaderMap().get("User-Agent");
    this.userAgent = UserAgent.getInstance(userAgent);
    LOG.info("userAgent='" + this.userAgent + "' from header "
        + "User-Agent='" + userAgent + "'");

    // debug mode
    // to enable the debug mode for a user, put a
    // "to-ba-go" custom locale to your browser
    String acceptLanguage
        = (String) externalContext.getRequestHeaderMap().get("Accept-Language");
    if (acceptLanguage != null) {
      this.debugMode = acceptLanguage.indexOf("to-ba-go") > -1;
    }
    LOG.info("debug-mode=" + debugMode);

    // theme
    String theme
        = (String) externalContext.getRequestParameterMap().get("tobago.theme");
    this.theme = TobagoConfig.getInstance(facesContext).getTheme(theme);
    LOG.info("theme='" + this.theme + "' from requestParameter "
        + "tobago.theme='" + theme + "'");
    updateId();
  }

  private void updateId() {

    StringBuffer buffer = new StringBuffer();
    buffer.append(getContentType());
    buffer.append('/');
    buffer.append(getTheme());
    buffer.append('/');
    buffer.append(getUserAgent());
    id =  buffer.toString();
    final UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
    if (viewRoot instanceof com.atanion.tobago.component.UIViewRoot) {
      ((com.atanion.tobago.component.UIViewRoot)viewRoot).updateRendererCachePrefix();
    }
  }

// ///////////////////////////////////////////// logic

  public static ClientProperties getInstance(UIViewRoot viewRoot) {

    ClientProperties instance = (ClientProperties)
        viewRoot.getAttributes().get(TobagoConstants.ATTR_CLIENT_PROPERTIES);
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

  public static List getLocaleList(Locale locale, boolean propertyPathMode) {

    String string = locale.toString();
    String prefix = propertyPathMode ? "" : "_";
    List locales = new ArrayList(4);
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

// ///////////////////////////////////////////// bean getter + setter

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

  // /////////////////////////////////////////////
}

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
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

public class ClientProperties {

// ///////////////////////////////////////////// constants

  private static final String CLIENT_PROPERTIES_IN_SESSION
      = ClientProperties.class.getName();

  private static final Log LOG = LogFactory.getLog(ClientProperties.class);

// ///////////////////////////////////////////// attributes

  private Locale locale = Locale.US;
  private String contentType = "html";
  private Theme theme;
  private UserAgent userAgent = UserAgent.DEFAULT;
  private boolean debugMode;

// ///////////////////////////////////////////// constructors

  private ClientProperties(ExternalContext context) {

    String accept = (String) context.getRequestHeaderMap().get("Accept");
    if (accept != null) {
      if (accept.indexOf("text/vnd.wap.wml") > -1) {
        contentType = "wml";
      }
    }
    LOG.info("contentType='" + contentType + "' from header "
        + "Accept='" + accept + "'");

    String explicitLocale
        = (String) context.getRequestParameterMap().get("tobago.locale");
    if (explicitLocale != null) {
      locale = new Locale(explicitLocale);
    }
    String acceptLanguage
        = (String) context.getRequestHeaderMap().get("Accept-Language");
    if (locale == null) {
      if (acceptLanguage != null) {
        locale = parseAcceptLanguageHeader(acceptLanguage)[0];
      }
    }
    LOG.info("locale='" + locale + "' from header "
        + "Accept-Language='" + acceptLanguage + "' or parameter "
        + "tobago.locale='" + explicitLocale + "'");

    String userAgent
        = (String) context.getRequestHeaderMap().get("User-Agent");
    this.userAgent = UserAgent.getInstance(userAgent);
    LOG.info("userAgent='" + this.userAgent + "' from header "
        + "User-Agent='" + userAgent + "'");

    // to enable the debug mode for a user, put a
    // "to-ba-go" custom locale to your browser
    if (acceptLanguage != null) {
      this.debugMode = acceptLanguage.indexOf("to-ba-go") > -1;
    }
    LOG.info("debug-mode=" + debugMode);

    String theme
        = (String) context.getRequestParameterMap().get("tobago.theme");
    if (theme != null) {
      this.theme = TobagoConfig.getInstance(context).getTheme(theme);
    } else {
      this.theme = TobagoConfig.getInstance(context).getDefaultTheme();
    }
    LOG.info("theme='" + this.theme + "' from requestParameter "
        + "tobago.theme='" + theme + "'");
  }

// ///////////////////////////////////////////// logic

  public static ClientProperties getInstance(UIViewRoot viewRoot) {

    ClientProperties instance = (ClientProperties)
        viewRoot.getAttributes().get(TobagoConstants.ATTR_CLIENT_PROPERTIES);
    if (instance != null) {
      instance.setLocale(viewRoot.getLocale());
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
      client = new ClientProperties(context);
      if (hasSession) {
        context.getSessionMap().put(CLIENT_PROPERTIES_IN_SESSION, client);
      }
    }
    return client;
  }

  private static Locale[] parseAcceptLanguageHeader(
      String httpHeaderAcceptLanguage) {
    StringTokenizer tokenizer
        = new StringTokenizer(httpHeaderAcceptLanguage, ",");
    Locale[] locales = new Locale[tokenizer.countTokens()];

    int i = 0;
    while (tokenizer.hasMoreTokens()) {
      StringTokenizer localeTokenizer =
          new StringTokenizer(tokenizer.nextToken().trim(), "-;", true);
      Locale locale = null;
      if (localeTokenizer.hasMoreTokens()) {
        String language = localeTokenizer.nextToken().trim();
        if (language.length() > 2) {
          language = language.substring(0, 2);
        }
        String country = "";
        String token = ";";
        if (localeTokenizer.hasMoreTokens()) {
          token = localeTokenizer.nextToken().trim();
        }
        if (token.equals("-") && localeTokenizer.hasMoreTokens()) {
          country = localeTokenizer.nextToken().trim();
        }
        locale = new Locale(language, country);
      }
      locales[i++] = locale;
    }
    for (int j = 0; j < locales.length; j++) {
      Locale locale = locales[j];
      if (LOG.isDebugEnabled()) {
        LOG.debug("LOCALE " + locale);
      }
    }

    return locales;
  }

  public static List getLocaleList(String locale, boolean propertyPathMode) {

    String prefix = propertyPathMode ? "" : "_";
    List locales = new Vector(4);
    locales.add(prefix + locale);
    int underscore;
    while ((underscore = locale.lastIndexOf('_')) > 0) {
      locale = locale.substring(0, underscore);
      locales.add(prefix + locale);
    }

    locales.add(propertyPathMode ? "default" : ""); // default suffix

    return locales;
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(getContentType());
    buffer.append('/');
    buffer.append(getTheme());
    buffer.append('/');
    buffer.append(getUserAgent());
    buffer.append('/');
    buffer.append(getLocale());
    return buffer.toString();
  }

// ///////////////////////////////////////////// bean getter + setter

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(UserAgent userAgent) {
    this.userAgent = userAgent;
  }

  public boolean isDebugMode() {
    return debugMode;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

  // /////////////////////////////////////////////
}

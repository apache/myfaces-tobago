/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: 23.07.2002 14:21:58
 * $Id$
 */
package com.atanion.tobago.context;

import com.atanion.tobago.config.TobagoConfig;
import com.atanion.tobago.TobagoConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

public class ClientProperties {

// ///////////////////////////////////////////// constants

  private static final String IN_SESSION_KEY = ClientProperties.class.getName();

  private static final Log LOG = LogFactory.getLog(ClientProperties.class);

// ///////////////////////////////////////////// attributes

  private Locale locale = Locale.US;
  private String contentType = "html";
  private String theme;
  private UserAgent userAgent = UserAgent.DEFAULT;
  private boolean jspComment = false;
  private boolean debugMode;

// ///////////////////////////////////////////// constructors

  private ClientProperties(HttpServletRequest request, HttpSession session) {

//    log.debug("*************************************************");
//    log.debug("* Request headers");
//    log.debug("*************************************************");
//    Enumeration e = request.getHeaderNames();
//    while (e.hasMoreElements()) {
//      String name = (String) e.nextElement();
//      log.debug("* " + name + " = " + request.getHeader(name));
//    }
//    log.debug("*************************************************");


    String accept = request.getHeader("Accept");
    LOG.info("Accept = '" + accept + "'");
    if (accept != null) {
      if (accept.indexOf("text/vnd.wap.wml") > -1) {
        contentType = "wml";
      }
    }

    String language = request.getHeader("Accept-Language");
    LOG.info("Accept-Language = '" + language + "'");
    if (language != null) {
      locale = parseAcceptLanguageHeader(language)[0];
    }
    LOG.info("Locale = " + locale);

    String userAgent = request.getHeader("User-Agent");
    LOG.info("User-Agent = '" + userAgent + "'");
    this.userAgent = UserAgent.getInstance(userAgent);
    LOG.info("User-Agent = " + this.userAgent);

    String debugModeString = session.getServletContext()
        .getInitParameter(TobagoConstants.CONTEXT_PARAM_DEBUG_MODE);
    debugMode = Boolean.valueOf(debugModeString).booleanValue();
    LOG.info("debug-mode = " + debugMode);

    setTheme(request.getParameter("tobago.theme"));
  }

// ///////////////////////////////////////////// logic

  public static ClientProperties getInstanceFromSession(
      HttpServletRequest request) {

    HttpSession session = request.getSession();
    ClientProperties client
        = (ClientProperties) session.getAttribute(IN_SESSION_KEY);
    if (client == null) {
      client = new ClientProperties(request, session);
      session.setAttribute(IN_SESSION_KEY, client);
    }
    return client;
  }

  public static ClientProperties getInstance(FacesContext facesContext) {
    return getInstanceFromSession(
        (HttpServletRequest) facesContext.getExternalContext().getRequest());
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
      LOG.debug("LOCALE " +  locale);
    }

    return locales;
  }

  public static List getLocaleList(
      String locale, boolean propertyPathMode) {

    String prefix = propertyPathMode ? "" : "_";
    List locales = new Vector(4);
    locales.add(prefix + locale);
    int underscore;
    while ((underscore = locale.lastIndexOf('_')) > 0) {
      locale = locale.substring(0, underscore);
      locales.add(prefix + locale);
    }

//    String variant = locale.getVariant();
//    String country = locale.getCountry();
//    String language = locale.getLanguage();
//    if (variant.length() > 0) {
//      locales.add((propertyPathMode ? "" : "_") + language + "_" + country + "_" + variant);
//    }
//    if (country.length() > 0) {
//      locales.add((propertyPathMode ? "" : "_") + language + "_" + country);
//    }
//    if (language.length() > 0) {
//      locales.add((propertyPathMode ? "" : "_") + language);
//    }

    locales.add(propertyPathMode ? "default" : ""); // default suffix

    return locales;
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

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = TobagoConfig.getInstance().getTheme(theme).getName();
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(UserAgent userAgent) {
    this.userAgent = userAgent;
  }

  public boolean isJspComment() {
    return jspComment;
  }

  public void setJspComment(boolean jspComment) {
    this.jspComment = jspComment;
  }

  public boolean isDebugMode() {
    return debugMode;
  }

  public void setDebugMode(boolean debugMode) {
    this.debugMode = debugMode;
  }

  // /////////////////////////////////////////////
}

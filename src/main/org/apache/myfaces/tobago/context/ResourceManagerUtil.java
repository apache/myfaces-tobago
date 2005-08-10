/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.03.2004 12:47:02.
 * $Id$
 */
package com.atanion.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResourceManagerUtil {

  private static final Log LOG = LogFactory.getLog(ResourceManagerUtil.class);

  public static ResourceManager getResourceManager(FacesContext facesContext) {
    return (ResourceManager) facesContext.getExternalContext()
        .getApplicationMap().get(ResourceManager.RESOURCE_MANAGER);
  }

  public static ResourceManager getResourceManager(ServletContext servletContext) {
    return (ResourceManager) servletContext
        .getAttribute(ResourceManager.RESOURCE_MANAGER);
  }

  public static String getProperty(
      FacesContext facesContext, String bundle, String key, boolean forceResult) {
    return getProperty(facesContext, bundle, key, null, forceResult);
  }
  public static String getProperty(
      FacesContext facesContext, String bundle, String key) {
    return getProperty(facesContext, bundle, key, null, true);
  }
  public static String getProperty(
      FacesContext facesContext, String bundle, String key, Locale locale) {
    return getProperty(facesContext, bundle, key, locale, true);
  }
  public static String getProperty(
      FacesContext facesContext, String bundle, String key, Locale locale, boolean forceResult) {
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String result = getResourceManager(facesContext).getProperty(viewRoot, bundle, key, locale);
    if (result == null && forceResult) {
      return "???" + key + "???";
    } else {
      return result;
    }
  }

  public static String getJsp(FacesContext facesContext, String key) {
    UIViewRoot viewRoot = facesContext.getViewRoot();
    return getResourceManager(facesContext).getJsp(viewRoot, key);
  }

  public static String getImage(FacesContext facesContext, String name) {
    return getImage(facesContext, name, false);
  }

  public static String getImage(FacesContext facesContext, String name,
      boolean ignoreMissing) {
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String image = getResourceManager(facesContext).getImage(
        viewRoot, name, ignoreMissing);
    if (image != null) {
      return facesContext.getExternalContext().getRequestContextPath() + image;
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Can't find image for \"" + name + "\"");
      }
      return null;
    }
  }

  public static List<String> getStyles(FacesContext facesContext, String name) {
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] styles = getResourceManager(facesContext).getStyles(viewRoot, name);

    return addContextPath(styles, contextPath);
  }

  private static List<String> addContextPath(String[] strings, String contextPath) {
    List<String> withContext = new ArrayList<String>(strings.length);
    for (int i = 0; i < strings.length; i++) {
      withContext.add(contextPath + strings[i]);
    }
    return withContext;
  }

  public static List<String> getScripts(FacesContext facesContext, String name) {
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String scripts[]
        = getResourceManager(facesContext).getScripts(viewRoot, name);
    return addContextPath(scripts, contextPath);
  }

  public static String getDisabledImage(FacesContext facesContext, String image) {
    final int dotIndex = image.lastIndexOf('.');
    String name = image.substring(0, dotIndex);
    String postfix = image.substring(dotIndex);
    return getImage(facesContext, name + "Disabled" + postfix, true);
  }
}
/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.03.2004 12:47:02.
 * $Id$
 */
package com.atanion.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

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
      FacesContext facesContext, String bundle, String key) {
    String clientProperties = ClientProperties.getInstance(
        facesContext.getViewRoot()).toString();
    String result = getResourceManager(facesContext).getProperty(clientProperties, bundle, key);
    if (result != null) {
      return result;
    } else {
      return "???" + key + "???";
    }
  }

  public static String getJsp(FacesContext facesContext, String key) {
    String clientProperties = ClientProperties.getInstance(
        facesContext.getViewRoot()).toString();
    return getResourceManager(facesContext).getJsp(clientProperties, key);
  }

  public static String getImage(FacesContext facesContext, String name) {
    return getImage(facesContext, name, false);
  }

  public static String getImage(FacesContext facesContext, String name,
      boolean ignoreMissing) {
    String clientProperties = ClientProperties.getInstance(
        facesContext.getViewRoot()).toString();
    String image = getResourceManager(facesContext).getImage(
        clientProperties, name, ignoreMissing);
    if (image != null) {
      return facesContext.getExternalContext().getRequestContextPath() + image;
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Can't find image for \"" + name + "\"");
      }
      return null;
    }
  }

  public static List getStyles(FacesContext facesContext, String name) {
    String clientProperties = ClientProperties.getInstance(
        facesContext.getViewRoot()).toString();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] styles = getResourceManager(facesContext).getStyles(clientProperties,
        name);

    List withContext = new ArrayList(styles.length);
    for (int i = 0; i < styles.length; i++) {
      withContext.add(contextPath + styles[i]);
    }
    return withContext;
  }

  public static String getScript(FacesContext facesContext, String name) {
    String clientProperties = ClientProperties.getInstance(
        facesContext.getViewRoot()).toString();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    return contextPath +
        getResourceManager(facesContext).getScript(clientProperties, name);
  }

}
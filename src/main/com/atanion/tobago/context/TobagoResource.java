/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.03.2004 12:47:02.
 * $Id$
 */
package com.atanion.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class TobagoResource {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TobagoResource.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public static String getProperty(
      FacesContext facesContext, String bundle, String key) {
    String clientProperties = ClientProperties.getInstance(
        facesContext.getViewRoot()).toString();
    String result = ResourceManager.getInstance().getProperty(clientProperties, bundle, key);
    if (result != null) {
      return result;
    } else {
      return "???" + key + "???";
    }
  }

  public static String getJsp(FacesContext facesContext, String key) {
    String clientProperties = ClientProperties.getInstance(
        facesContext.getViewRoot()).toString();
    return ResourceManager.getInstance().getJsp(clientProperties, key);
  }

  public static String getImage(FacesContext facesContext, String name) {
    return getImage(facesContext, name, false);
  }

  public static String getImage(FacesContext facesContext, String name,
      boolean ignoreMissing) {
    String clientProperties = ClientProperties.getInstance(
        facesContext.getViewRoot()).toString();
    String image = ResourceManager.getInstance().getImage(
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
    String[] styles = ResourceManager.getInstance().getStyles(clientProperties,
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
        ResourceManager.getInstance().getScript(clientProperties, name);
  }

// ///////////////////////////////////////////// bean getter + setter

}

/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.03.2004 12:47:02.
 * $Id$
 */
package com.atanion.tobago.context;

import javax.faces.context.FacesContext;
import java.util.List;
import java.util.ArrayList;

public class TobagoResource {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public static String getProperty(
      FacesContext facesContext, String bundle, String key) {
    String renderKitId = facesContext.getViewRoot().getRenderKitId();
    String result = ResourceManager.getInstance().getProperty(renderKitId, bundle, key);
    if (result != null) {
      return result;
    } else {
      return "???" + key + "???";
    }
  }

  public static String getJsp(FacesContext facesContext, String key) {
    String renderKitId = facesContext.getViewRoot().getRenderKitId();
    return ResourceManager.getInstance().getJsp(renderKitId, key);
  }

  public static String getImage(FacesContext facesContext, String name) {
    String renderKitId = facesContext.getViewRoot().getRenderKitId();
    String returnValue = facesContext.getExternalContext().getRequestContextPath() +
        ResourceManager.getInstance().getImage(renderKitId, name);
    return returnValue;
  }

  public static List getStyles(FacesContext facesContext, String name) {
    String renderKitId = facesContext.getViewRoot().getRenderKitId();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] styles = ResourceManager.getInstance().getStyles(renderKitId,
        name);

    List withContext = new ArrayList(styles.length);
    for (int i = 0; i < styles.length; i++) {
      withContext.add(contextPath + styles[i]);
    }
    return withContext;
  }

  public static String getScript(FacesContext facesContext, String name) {
    String renderKitId = facesContext.getViewRoot().getRenderKitId();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    return contextPath +
        ResourceManager.getInstance().getScript(renderKitId, name);
  }


// ///////////////////////////////////////////// bean getter + setter

}

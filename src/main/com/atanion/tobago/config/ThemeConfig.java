/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 26.04.2004 at 19:57:16.
  * $Id$
  */
package com.atanion.tobago.config;


import com.atanion.tobago.context.ResourceManager;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.util.SystemUtils;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.FactoryFinder;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Iterator;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class ThemeConfig {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ThemeConfig.class);

// ///////////////////////////////////////////// attribute

  private static ThemeConfig instance;

  private Map valueMap;

// ///////////////////////////////////////////// constructor

  private ThemeConfig() {
    valueMap = new HashMap();
  }

// ///////////////////////////////////////////// code

  public static synchronized ThemeConfig getInstance() {
    if (instance == null) {
      instance = new ThemeConfig();
    }
    return instance;
  }

  public int getValue(FacesContext facesContext, UIComponent component, String name) {
    String rendererType;
    if (component != null) {
      rendererType = component.getRendererType();
    } else {
      rendererType = "DEFAULT";
    }
    String mapKey = facesContext.getViewRoot().getRenderKitId() + "/"
        + rendererType + "/" + name;
    Integer value = (Integer) valueMap.get(mapKey);
    if (value == null) {
      value = createValue(facesContext, component, name);
      valueMap.put(mapKey, value);
    }
    return value.intValue();
  }

  private Integer createValue(FacesContext facesContext, UIComponent component,
      String name) {

    String renderKitId = facesContext.getViewRoot().getRenderKitId();

    RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(
        "javax.faces.render.RenderKitFactory");
    RenderKit renderKit = rkFactory.getRenderKit(facesContext, renderKitId);
    String family;
    String rendererType;
    if (component != null) {
      family = component.getFamily();
      rendererType = component.getRendererType();
    } else {
      family = UIInput.COMPONENT_FAMILY;
      rendererType = "TextBox";
    }
    Renderer renderer = (Renderer) renderKit.getRenderer(family, rendererType);

    Class clazz = renderer.getClass();
    if (LOG.isDebugEnabled()) {
      LOG.debug("search for " + name + " in " + clazz.getName());
    }
    while (clazz != null) {
      String tag = getTagName(clazz);
      if (LOG.isDebugEnabled()) {
        LOG.debug("try " + tag);
      }

      ResourceManager resourceManager = ResourceManager.getInstance();
      String property = resourceManager.getProperty(
          renderKitId, "tobago-theme-config" , tag + "." + name);

      if (property != null && property.length() > 0) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("found " + property);
        }
        return new Integer(property);
      }
      clazz = clazz.getSuperclass();
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("found null");
    }
    return null;
  }

  private String getTagName(Class clazz) {
    String className = SystemUtils.getPlainClassName(clazz);
    if (className.equals(SystemUtils.getPlainClassName(RendererBase.class))) {
      return "Tobago";
    }
    else if (className.endsWith("Renderer")) {
      return className.substring(0, className.lastIndexOf("Renderer"));
    }
    else if (className.endsWith("RendererBase")) {
      return className.substring(0, className.lastIndexOf("RendererBase")) + "Base";
    }
    return null;
  }

  /*
  private Integer createValue(String renderKitId, String tag, String name) {
    StringTokenizer st = new StringTokenizer(renderKitId, "/");
    String contentType = st.nextToken();
    String theme = st.nextToken();
    String userAgent = st.nextToken();

    for (Iterator themes = getThemes(theme); themes.hasNext();) {
      String actualTheme = (String) themes.next();
      for (Iterator userAgents = getUserAgents(userAgent); userAgents.hasNext();) {
        String actualUserAgent = (String) userAgents.next();
        for (Iterator tags = getTags(tag); tags.hasNext();) {
          String actualTag = (String) tags.next();
          Integer value = getValue(contentType, actualTheme, actualUserAgent,
              actualTag, name);
          if (value != null) {
            return value;
          }
        }
      }
    }



    return null;
  }

  private Integer getValue(String contentType, String theme,
      String userAgent, String tag, String name) {
    Map map = (Map) configMap.get(contentType);
    if (map != null) {
      map = (Map) map.get(theme);
    }
    if (map != null) {
      map = (Map) map.get(userAgent);
    }
    if (map != null) {
      map = (Map) map.get(tag);
    }
    if (map != null) {
      Integer value =  (Integer) map.get(name);
      if (value != null) {
        return value;
      }
    }

    return null;
  }
  */

// ///////////////////////////////////////////// bean getter + setter

}
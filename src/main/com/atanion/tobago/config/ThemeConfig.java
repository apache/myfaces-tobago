/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 26.04.2004 at 19:57:16.
  * $Id$
  */
package com.atanion.tobago.config;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.context.ResourceManager;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.TobagoRenderKit;
import com.atanion.util.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import java.util.Map;

public class ThemeConfig {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(ThemeConfig.class);

  public static final String THEME_CONFIG_CACHE
      = "com.atanion.tobago.config.ThemeConfig.CACHE";

// ----------------------------------------------------------- business methods

  public static int getValue(FacesContext facesContext, UIComponent component,
      String name) {
    String rendererType;
    if (component != null) {
      rendererType = component.getRendererType();
    } else {
      rendererType = "DEFAULT";
    }
    String clientProperties = facesContext.getViewRoot().getAttributes().get(
        TobagoConstants.ATTR_CLIENT_PROPERTIES).toString();
    String mapKey = clientProperties + "/"
        + rendererType + "/" + name;
    Map cache = (Map) facesContext.getExternalContext()
        .getApplicationMap().get(THEME_CONFIG_CACHE);
    Integer value = (Integer) cache.get(mapKey);
    if (value == null) {
      value = createValue(facesContext, component, name);
      cache.put(mapKey, value);
    }
    return value.intValue();
  }

  private static Integer createValue(FacesContext facesContext, UIComponent component,
      String name) {
    RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(
        "javax.faces.render.RenderKitFactory");
    RenderKit renderKit = rkFactory.getRenderKit(facesContext,
        TobagoRenderKit.RENDER_KIT_ID);
    String family;
    String rendererType;
    if (component != null) {
      family = component.getFamily();
      rendererType = component.getRendererType();
    } else {
      family = UIInput.COMPONENT_FAMILY;
      rendererType = "TextBox";
    }
    Renderer renderer = renderKit.getRenderer(family, rendererType);

    Class clazz = renderer.getClass();
    if (LOG.isDebugEnabled()) {
      LOG.debug("search for '" + name + "' in '" + clazz.getName() + "'");
    }
    ResourceManager resourceManager
        = ResourceManagerUtil.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    ClientProperties clientProperties
        = ClientProperties.getInstance(viewRoot);
    while (clazz != null) {
      String tag = getTagName(clazz);
      if (LOG.isDebugEnabled()) {
        LOG.debug("try " + tag);
      }

      String property = resourceManager.getThemeProperty(clientProperties,
          "tobago-theme-config", tag + "." + name);

      if (property != null && property.length() > 0) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("found " + property);
        }
        return new Integer(property);
      }
      clazz = clazz.getSuperclass();
    }
    LOG.error("Theme property not found for renderer: " + renderer.getClass() +
        " with clientProperties='" + clientProperties + "'");
    return null;
  }

  private static String getTagName(Class clazz) {
    String className = SystemUtils.getPlainClassName(clazz);
    if (className.equals(SystemUtils.getPlainClassName(RendererBase.class))) {
      return "Tobago";
    } else if (className.endsWith("Renderer")) {
      return className.substring(0, className.lastIndexOf("Renderer"));
    } else if (className.endsWith("RendererBase")) {
      return className.substring(0, className.lastIndexOf("RendererBase")) +
          "Base";
    }
    return null;
  }

// ///////////////////////////////////////////// bean getter + setter
}
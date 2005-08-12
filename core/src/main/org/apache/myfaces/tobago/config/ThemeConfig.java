/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 26.04.2004 at 19:57:16.
  * $Id$
  */
package org.apache.myfaces.tobago.config;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.util.Locale;
import java.util.Map;

public class ThemeConfig {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(ThemeConfig.class);

  public static final String THEME_CONFIG_CACHE
      = "org.apache.myfaces.tobago.config.ThemeConfig.CACHE";

// ----------------------------------------------------------- business methods

  public static int getValue(FacesContext facesContext, UIComponent component,
      String name) {

    CacheKey key = new CacheKey(facesContext.getViewRoot(), component, name);
    Map cache = (Map) facesContext.getExternalContext()
        .getApplicationMap().get(THEME_CONFIG_CACHE);

    Integer value = (Integer) cache.get(key);
    if (value == null) {
      value = createValue(facesContext, component, name);
      cache.put(key, value);
    }
    return value.intValue();
  }


  private static Integer createValue(FacesContext facesContext,
      UIComponent component, String name) {
    String family;
    String rendererType;
    if (component != null) {
      family = component.getFamily();
      rendererType = component.getRendererType();
    } else {
      family = UIInput.COMPONENT_FAMILY;
      rendererType = TobagoConstants.RENDERER_TYPE_IN;
    }
    Renderer renderer = ComponentUtil.getRenderer(facesContext, family, rendererType);

    Class clazz = renderer.getClass();
    if (LOG.isDebugEnabled()) {
      LOG.debug("search for '" + name + "' in '" + clazz.getName() + "'");
    }
    ResourceManager resourceManager
        = ResourceManagerUtil.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    while (clazz != null) {
      String tag = getTagName(clazz);
      if (LOG.isDebugEnabled()) {
        LOG.debug("try " + tag);
      }

      String property = resourceManager.getThemeProperty(viewRoot,
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
        " with clientProperties='" + ClientProperties.getInstance(viewRoot).getId() + "'" +
        " and locale='" + viewRoot.getLocale() + "'");
    return null;
  }

  private static String getTagName(Class clazz) {
    String className = ClassUtils.getShortClassName(clazz);
    if (className.equals(ClassUtils.getShortClassName(RendererBase.class))) {
      return "Tobago";
    } else if (className.endsWith("Renderer")) {
      return className.substring(0, className.lastIndexOf("Renderer"));
    } else if (className.endsWith("RendererBase")) {
      return className.substring(0, className.lastIndexOf("RendererBase")) +
          "Base";
    }
    return null;
  }


  private static class CacheKey {
    private String  clientProperties;
    private Locale locale;
    private String rendererType;
    private String name;

    public CacheKey(UIViewRoot viewRoot, UIComponent component, String name) {
      this.clientProperties = ClientProperties.getInstance(viewRoot).getId();
      this.locale = viewRoot.getLocale();
      if (component != null) {
        rendererType = component.getRendererType();
      } else {
        rendererType = "DEFAULT";
      }
      this.name = name;
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final CacheKey cacheKey = (CacheKey) o;

      if (!clientProperties.equals(cacheKey.clientProperties)) return false;
      if (!locale.equals(cacheKey.locale)) return false;
      if (!name.equals(cacheKey.name)) return false;
      if (!rendererType.equals(cacheKey.rendererType)) return false;

      return true;
    }

    public int hashCode() {
      int result;
      result = clientProperties.hashCode();
      result = 29 * result + locale.hashCode();
      result = 29 * result + rendererType.hashCode();
      result = 29 * result + name.hashCode();
      return result;
    }
  }

}

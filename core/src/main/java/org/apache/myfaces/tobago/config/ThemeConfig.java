package org.apache.myfaces.tobago.config;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class ThemeConfig {

  private static final Log LOG = LogFactory.getLog(ThemeConfig.class);

  public static final String THEME_CONFIG_CACHE = "org.apache.myfaces.tobago.config.ThemeConfig.CACHE";

  /**
   * @deprecated since 1.5.0, please use ThemeConfig.getMeasure()
   */
  @Deprecated
  public static int getValue(FacesContext facesContext, UIComponent component, String name) {
    Deprecation.LOG.warn("please use ThemeConfig.getMeasure()");
    return getMeasure(facesContext, component.getRendererType(), ArrayUtils.EMPTY_STRING_ARRAY, name).getPixel();
  }

  /**
   * @deprecated since 1.5.0, please use ThemeConfig.getMeasure()
   */
  @Deprecated
  public static boolean hasValue(FacesContext facesContext, UIComponent component, String name) {
    Deprecation.LOG.warn("please use ThemeConfig.getMeasure()");
    return getMeasure(facesContext, component.getRendererType(), ArrayUtils.EMPTY_STRING_ARRAY, name) != null;
  }

  public static Measure getMeasure(FacesContext facesContext, Configurable configurable, String name) {
    return getMeasure(facesContext, configurable.getRendererType(), configurable.getMarkup(), name);
  }

  private static Measure getMeasure(FacesContext facesContext, String rendererType, String[] markup, String name) {
    CacheKey key = new CacheKey(facesContext.getViewRoot(), rendererType, markup, name);
    Map<CacheKey, Measure> cache
        = (Map<CacheKey, Measure>) facesContext.getExternalContext().getApplicationMap().get(THEME_CONFIG_CACHE);

    if (!cache.containsKey(key)) {
      ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
      UIViewRoot viewRoot = facesContext.getViewRoot();
      String property = resourceManager.getThemeProperty(viewRoot, "tobago-theme-config", rendererType + "." + name);
      if (property != null) {
        Measure value = Measure.parse(property);
        for (String m : markup) {
          String mValue = resourceManager.getThemeProperty(viewRoot, "tobago-theme-config",
              rendererType + "[" + m + "]" + "." + name);
          if (mValue != null) {
            value.add(Measure.parse(mValue));
          }
        }
        cache.put(key, value);
        return value;
      } else {
        cache.put(key, null); // to mark that this value is undefined
        return null;
      }
    } else {
      return cache.get(key);
    }
  }

  private static final class CacheKey {

    private String clientProperties;
    private Locale locale;
    private String rendererType;
    private String name;
    private String[] markup;

    public CacheKey(UIViewRoot viewRoot, UIComponent component, String name) {
      this.clientProperties = ClientProperties.getInstance(viewRoot).getId();
      this.locale = viewRoot.getLocale();
      rendererType = component.getRendererType();
      this.name = name;
    }

    public CacheKey(UIViewRoot viewRoot, String rendererType, String[] markup, String name) {
      this.clientProperties = ClientProperties.getInstance(viewRoot).getId();
      this.locale = viewRoot.getLocale();
      this.rendererType = rendererType;
      this.markup = (String[]) ArrayUtils.clone(markup);
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      CacheKey cacheKey = (CacheKey) o;

      if (!clientProperties.equals(cacheKey.clientProperties)) {
        return false;
      }
      if (!locale.equals(cacheKey.locale)) {
        return false;
      }
      if (!Arrays.equals(markup, cacheKey.markup)) {
        return false;
      }
      if (!name.equals(cacheKey.name)) {
        return false;
      }
      if (!rendererType.equals(cacheKey.rendererType)) {
        return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      int result = clientProperties.hashCode();
      result = 31 * result + locale.hashCode();
      result = 31 * result + rendererType.hashCode();
      result = 31 * result + name.hashCode();
      result = 31 * result + Arrays.hashCode(markup);
      return result;
    }

    @Override
    public String toString() {
      return "CacheKey(" + clientProperties
          + "," + locale
          + "," + rendererType
          + "," + Arrays.toString(markup)
          + "," + name + ')';
    }
  }
}

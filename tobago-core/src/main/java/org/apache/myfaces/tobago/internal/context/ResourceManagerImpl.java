package org.apache.myfaces.tobago.internal.context;

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

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.UserAgent;
import org.apache.myfaces.tobago.internal.config.TobagoConfigImpl;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.util.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManagerImpl implements ResourceManager {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceManagerImpl.class);
  private static final String PROPERTY = "property";
  private static final String JSP = "jsp";
  private static final String TAG = "tag";
  private static final String MINIMIZE_SUFFIX = ".min";
  private boolean production;

  private final Map<String, String> resourceList 
      = new ConcurrentHashMap<String, String>(100, 0.75f, 1);

  private final Map<RendererCacheKey, Renderer> rendererCache 
      = new ConcurrentHashMap<RendererCacheKey, Renderer>(100, 0.75f, 1);
  private final Map<ImageCacheKey, StringValue> imageCache 
      = new ConcurrentHashMap<ImageCacheKey, StringValue>(100, 0.75f, 1);
  private final Map<JspCacheKey, String> jspCache 
      = new ConcurrentHashMap<JspCacheKey, String>(100, 0.75f, 1);
  private final Map<MiscCacheKey, String[]> miscCache 
      = new ConcurrentHashMap<MiscCacheKey, String[]>(100, 0.75f, 1);
  private final Map<PropertyCacheKey, StringValue> propertyCache 
      = new ConcurrentHashMap<PropertyCacheKey, StringValue>(100, 0.75f, 1);
  private final Map<ThemeConfigCacheKey, MeasureValue> themeCache 
      = new ConcurrentHashMap<ThemeConfigCacheKey, MeasureValue>(100, 0.75f, 1);
  
  private TobagoConfigImpl tobagoConfig;

  public ResourceManagerImpl(TobagoConfigImpl tobagoConfig) {
    this.tobagoConfig = tobagoConfig;
    this.production = tobagoConfig.getProjectStage() == ProjectStage.Production;
  }

  public void add(String resourceKey) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("adding resourceKey = '{}'", resourceKey);
    }
    resourceList.put(resourceKey, "");
  }

  public void add(String resourceKey, String value) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("adding resourceKey = '{}' value= '{}'", resourceKey, value);
    }
    resourceList.put(resourceKey, value);
  }

  @Deprecated
  public String getJsp(UIViewRoot viewRoot, String name) {
    String result = null;
    if (name != null) {

      ClientPropertiesKey clientKey = ClientPropertiesKey.get(FacesContext.getCurrentInstance());
      JspCacheKey cacheKey = new JspCacheKey(clientKey, name);

      result = jspCache.get(cacheKey);
      if (result != null) {
        return result;
      }
      try {
        result = (String) getPaths(clientKey, "",
            JSP, name, "", false, true, true, null, true, false).get(0);
        jspCache.put(cacheKey, result);
      } catch (Exception e) {
        LOG.error("name = '" + name + "' clientProperties = '" + clientKey.toString() + "'", e);
      }
    }
    return result;
  }

  @Deprecated
  public String getProperty(UIViewRoot viewRoot, String bundle, String propertyKey) {
    return getProperty(FacesContext.getCurrentInstance(), bundle, propertyKey);
  }

  public String getProperty(FacesContext facesContext, String bundle, String propertyKey) {

    if (bundle != null && propertyKey != null) {
      ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
      PropertyCacheKey cacheKey = new PropertyCacheKey(clientKey, bundle, propertyKey);
      
      StringValue result = propertyCache.get(cacheKey);
      if (result == null) {
        List properties = getPaths(clientKey, "", PROPERTY, bundle, "", false, true, false, propertyKey, true, false);
        if (properties != null) {
          result = new StringValue((String) properties.get(0));
        } else {
          result = StringValue.NULL;
        }
        propertyCache.put(cacheKey, result);
      }
      return result.getValue();
    }
    return null;
  }

  @Deprecated
  public Renderer getRenderer(UIViewRoot viewRoot, String rendererType) {
    return getRenderer(FacesContext.getCurrentInstance(), rendererType);
  }

  public Renderer getRenderer(FacesContext facesContext, String rendererType) {
    Renderer renderer = null;

    if (rendererType != null) {
      ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
      RendererCacheKey cacheKey = new RendererCacheKey(clientKey, rendererType);

      renderer = rendererCache.get(cacheKey);
      if (renderer != null) {
        return renderer;
      }
      String simpleClassName = null;
      try {
        simpleClassName = getRendererClassName(rendererType);
        List<Class> classes = getPaths(clientKey, "", TAG, simpleClassName, "", false, true, true, null, false, false);
        if (classes != null && !classes.isEmpty()) {
          Class clazz = classes.get(0);
          renderer = (Renderer) clazz.newInstance();
          rendererCache.put(cacheKey, renderer);
        } else {
          LOG.error("Don't find any RendererClass for " + simpleClassName + ". Please check you configuration.");
        }
      } catch (InstantiationException e) {
        LOG.error("name = '" + simpleClassName + "' clientProperties = '" + clientKey.toString() + "'", e);
      } catch (IllegalAccessException e) {
        LOG.error("name = '" + simpleClassName + "' clientProperties = '" + clientKey.toString() + "'", e);
      }
    }
    return renderer;
  }
  
  @Deprecated
  public String[] getScripts(UIViewRoot viewRoot, String name) {
    return getScripts(FacesContext.getCurrentInstance(), name);
  }
  
  public String[] getScripts(FacesContext facesContext, String name) {
    return getStrings(facesContext, name, null);
  }

  @Deprecated
  public String[] getStyles(UIViewRoot viewRoot, String name) {
    return getStyles(FacesContext.getCurrentInstance(), name);
  }

  public String[] getStyles(FacesContext facesContext, String name) {
    return getStrings(facesContext, name, null);
  }

  @Deprecated
  public String getThemeProperty(UIViewRoot viewRoot, String bundle, String propertyKey) {
    if (bundle != null && propertyKey != null) {

      ClientPropertiesKey clientKey = ClientPropertiesKey.get(FacesContext.getCurrentInstance());
      PropertyCacheKey cacheKey = new PropertyCacheKey(clientKey, bundle, propertyKey);

      StringValue result = propertyCache.get(cacheKey);
      if (result == null) {
        List properties = getPaths(clientKey, "", PROPERTY, bundle, "", false, true, false, propertyKey, true, true);
        if (properties != null) {
          result = new StringValue((String) properties.get(0));
        } else {
          result = StringValue.NULL;
        }
        propertyCache.put(cacheKey, result);
      }
      return result.getValue();
    }
    return null;
  }

  public Measure getThemeMeasure(FacesContext facesContext, Configurable configurable, String name) {
    return getThemeMeasure(facesContext, configurable.getRendererType(), configurable.getCurrentMarkup(), name);
  }

  public Measure getThemeMeasure(FacesContext facesContext, String rendererType, Markup markup, String name) {

    ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
    ThemeConfigCacheKey cacheKey = new ThemeConfigCacheKey(clientKey, rendererType, markup, name);

    MeasureValue result = themeCache.get(cacheKey);

    if (result == null) {
      List properties = getPaths(clientKey, "", PROPERTY, "tobago-theme-config", "",
          false, true, false, rendererType + "." + name, true, true);
      if (properties != null) {
        Measure measure = Measure.valueOf(properties.get(0));

        if (markup != null) {
          for (String m : markup) {
            List mProperties = getPaths(clientKey, "", PROPERTY, "tobago-theme-config", "",
                false, true, false, rendererType + "[" + m + "]" + "." + name, true, true);
            if (mProperties != null) {
              final Measure summand = Measure.valueOf(mProperties.get(0));
              measure = measure.add(summand);
            }
          }
        }

        result = new MeasureValue(measure);
      } else {
        result = MeasureValue.NULL;  // to mark and cache that this value is undefined.
      }
      themeCache.put(cacheKey, result);
    }
    return result.getValue();
  }
  
  @Deprecated
  public String getImage(UIViewRoot viewRoot, String name) {
    return getImage(FacesContext.getCurrentInstance(), name);
  }
  
  public String getImage(FacesContext facesContext, String name) {
    return getImage(facesContext, name, false);
  }

  @Deprecated
  public String getImage(UIViewRoot viewRoot, String name, boolean ignoreMissing) {
    return getImage(FacesContext.getCurrentInstance(), name, ignoreMissing);
  }

  public String getImage(FacesContext facesContext, String name, boolean ignoreMissing) {
    if (name != null) {
      int dot = name.lastIndexOf('.');
      if (dot == -1) {
        dot = name.length();
      }

      ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
      ImageCacheKey cacheKey = new ImageCacheKey(clientKey, name);

      StringValue result = imageCache.get(cacheKey);
      if (result == null) {
        List paths = getPaths(clientKey, "", null, name.substring(0, dot),
            name.substring(dot), false, true, true, null, true, ignoreMissing);
        if (paths != null) {
          result = new StringValue((String) paths.get(0));
        } else {
          result = StringValue.NULL;
        }
        imageCache.put(cacheKey, result);
      }
      if (LOG.isDebugEnabled()) {
        if (result.getValue() == null) {
          LOG.debug("Can't find image for \"{}\"", name);
        }
      }

      return result.getValue();
    }

    return null;
  }

  private List getPaths(
      ClientPropertiesKey clientkey, String prefix, String subDir, String name, String suffix,
      boolean reverseOrder, boolean single, boolean returnKey, String key, boolean returnStrings,
      boolean ignoreMissing) {
    List matches = new ArrayList();
    String contentType = clientkey.getContentType();
    Theme theme = clientkey.getTheme();
    UserAgent browser = clientkey.getUserAgent();
    List<String> locales = LocaleUtils.getLocaleSuffixList(clientkey.getLocale());

    // check first the local web application directory
    for (String localeSuffix : locales) {
      if (production) {
        boolean found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
            name, MINIMIZE_SUFFIX, localeSuffix, suffix, key);
        if (found && (single || !returnStrings)) {
          return matches;
        }
        if (!found) {
          found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
              name, null, localeSuffix, suffix, key);
          if (found && (single || !returnStrings)) {
            return matches;
          }
        }
      } else {
        boolean found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
            name, null, localeSuffix, suffix, key);
        if (found && (single || !returnStrings)) {
          return matches;
        }
      }
    }

    // after that check the whole resources tree
    // e.g. 1. application, 2. library or renderkit
    for (Theme currentTheme : theme.getFallbackList()) {// theme loop
      for (String resourceDirectory : tobagoConfig.getResourceDirs()) {
        for (String browserType : browser.getFallbackList()) { // browser loop
          for (String localeSuffix : locales) { // locale loop
            if (production) {
              boolean found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
                  resourceDirectory, contentType, currentTheme, browserType, subDir, name, MINIMIZE_SUFFIX,
                  localeSuffix, suffix, key);
              if (found && (single || !returnStrings)) {
                return matches;
              }
              if (!found) {
                found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
                    resourceDirectory, contentType, currentTheme, browserType, subDir, name, null,
                    localeSuffix, suffix, key);
                if (found && (single || !returnStrings)) {
                  return matches;
                }
              }
            } else {
              boolean found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
                  resourceDirectory, contentType, currentTheme, browserType, subDir, name, null,
                  localeSuffix, suffix, key);
              if (found && (single || !returnStrings)) {
                return matches;
              }
            }
          }
        }
      }
    }

    if (matches.isEmpty()) {
      if (!ignoreMissing) {
        LOG.error("Path not found, and no fallback. Using empty string.\n"
            + "resourceDirs = '" + tobagoConfig.getResourceDirs()
            + "' contentType = '" + contentType
            + "' theme = '" + theme.getName()
            + "' browser = '" + browser
            + "' subDir = '" + subDir
            + "' name = '" + name
            + "' suffix = '" + suffix
            + "' key = '" + key
            + "'"/*, new Exception()*/);
      }
      return null;
    } else {
      return matches;
    }
  }

  private boolean checkPath(
      String prefix, boolean reverseOrder, boolean returnKey, boolean returnStrings,
      List matches, String name, String minimizeSuffix, String localeSuffix, String extension, String key) {
    String path = makePath(name, minimizeSuffix, localeSuffix, extension, key);
    if (returnStrings && resourceList.containsKey(path)) {
      String result =
          returnKey
              ? prefix + path
              : prefix + resourceList.get(path);

      if (reverseOrder) {
        matches.add(0, result);
      } else {
        matches.add(result);
      }
      if (LOG.isTraceEnabled()) {
        LOG.trace("testing path: {} *", path); // match
      }

      return true;
    } else if (!returnStrings) {
      try {
        path = path.substring(1).replace('/', '.');
        Class clazz = Class.forName(path);
        if (LOG.isTraceEnabled()) {
          LOG.trace("testing path: " + path + " *"); // match
        }
        matches.add(clazz);
        return true;
      } catch (ClassNotFoundException e) {
        // not found
        if (LOG.isTraceEnabled()) {
          LOG.trace("testing path: " + path); // no match
        }
      }
    } else {
      if (LOG.isTraceEnabled()) {
        LOG.trace("testing path: " + path); // no match
      }
    }
    return false;
  }

  private boolean checkPath(
      String prefix, boolean reverseOrder, boolean returnKey, boolean returnStrings,
      List matches,  String resourceDirectory, String contentType, Theme currentTheme, String browserType,
      String subDir, String name, String minimizeSuffix, String localeSuffix, String suffix, String key) {
    String path = makePath(resourceDirectory, contentType, currentTheme, browserType, subDir, name, minimizeSuffix,
        localeSuffix, suffix, key, null);
    if (returnStrings && resourceList.containsKey(path)) {
      String result;
      if (prefix.length() == 0 && returnKey && resourceDirectory.equals(currentTheme.getResourcePath())) {
        result = makePath(resourceDirectory, contentType, currentTheme, browserType, subDir, name, minimizeSuffix,
            localeSuffix, suffix, key, currentTheme.getVersion());
      } else {
        result = returnKey
            ? prefix + path : prefix + resourceList.get(path);
      }
      if (reverseOrder) {
        matches.add(0, result);
      } else {
        matches.add(result);
      }
      if (LOG.isTraceEnabled()) {
        LOG.trace("testing path: {} *", path); // match
      }

      return true;
    } else if (!returnStrings) {
      try {
        path = path.substring(1).replace('/', '.');
        Class clazz = Class.forName(path);
        if (LOG.isTraceEnabled()) {
          LOG.trace("testing path: " + path + " *"); // match
        }
        matches.add(clazz);
        return true;
      } catch (ClassNotFoundException e) {
        // not found
        if (LOG.isTraceEnabled()) {
          LOG.trace("testing path: " + path); // no match
        }
      }
    } else {
      if (LOG.isTraceEnabled()) {
        LOG.trace("testing path: " + path); // no match
      }
    }
    return false;
  }

  private String makePath(
      String project, String language, Theme theme, String browser, String subDir,
      String name, String minimizeSuffix, String localeSuffix, String extension, String key, String version) {
    StringBuilder searchtext = new StringBuilder(64);

    searchtext.append('/');
    searchtext.append(project);
    if (version != null) {
      searchtext.append('/');
      searchtext.append(version);
    }
    searchtext.append('/');
    searchtext.append(language);
    searchtext.append('/');
    searchtext.append(theme.getName());
    searchtext.append('/');
    searchtext.append(browser);
    if (subDir != null) {
      searchtext.append('/');
      searchtext.append(subDir);
    }
    searchtext.append('/');
    searchtext.append(name);
    if (minimizeSuffix != null) {
      searchtext.append(minimizeSuffix);
    }
    searchtext.append(localeSuffix);
    searchtext.append(extension);
    if (key != null) {
      searchtext.append('/');
      searchtext.append(key);
    }

    return searchtext.toString();
  }

  private String makePath(
      String name, String minimizeSuffix, String localeSuffix, String extension, String key) {
    StringBuilder searchtext = new StringBuilder(64);

    searchtext.append('/');
    searchtext.append(name);
    if (minimizeSuffix != null) {
      searchtext.append(minimizeSuffix);
    }
    searchtext.append(localeSuffix);
    searchtext.append(extension);
    if (key != null) {
      searchtext.append('/');
      searchtext.append(key);
    }

    return searchtext.toString();
  }

  private String getRendererClassName(String rendererType) {
    String name;
    if (LOG.isDebugEnabled()) {
      LOG.debug("rendererType = '{}'", rendererType);
    }
    if ("javax.faces.Text".equals(rendererType)) { // TODO: find a better way
      name = RendererTypes.OUT;
    } else {
      name = rendererType;
    }
    name = name + "Renderer";
    if (name.startsWith("javax.faces.")) { // FIXME: this is a hotfix from jsf1.0beta to jsf1.0fr
      LOG.warn("patching renderer from {}", name);
      name = name.substring("javax.faces.".length());
      LOG.warn("patching renderer to {}", name);
    }
    return name;
  }

  private String[] getStrings(FacesContext facesContext, String name, String type) {
    String[] result = new String[0];
    if (name != null) {
      int dot = name.lastIndexOf('.');
      if (dot == -1) {
        dot = name.length();
      }

      ClientPropertiesKey key = ClientPropertiesKey.get(facesContext);
      MiscCacheKey miscKey = new MiscCacheKey(key, name);
      String[] cacheResult = miscCache.get(miscKey);
      if (cacheResult != null) {
        return cacheResult;
      }
      try {
        List matches = getPaths(key, "", type,
            name.substring(0, dot), name.substring(dot), true, false, true, null, true, false);
        if (matches != null) {
          result = (String[]) matches.toArray(new String[matches.size()]);
        }
        miscCache.put(miscKey, result);
      } catch (Exception e) {
        LOG.error("name = '" + name + "' clientProperties = '" + key.toString() + "'", e);
      }
    }
    return result;
  }

}

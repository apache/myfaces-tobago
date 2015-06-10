/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.context;

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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManagerImpl implements ResourceManager {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceManagerImpl.class);
  private static final String PROPERTY = "property";
  private static final String JSP = "jsp";
  private static final String TAG = "tag";
  private static final String MINIMIZE_SUFFIX = ".min";

  public static final String[] EXT_NONE = new String[]{""};
  public static final String[] EXT_JS = new String[]{".js"};
  public static final String[] EXT_CSS = new String[]{".css"};
  public static final String[] EXT_GIF = new String[]{".gif"};
  public static final String[] EXT_PNG = new String[]{".png"};
  public static final String[] EXT_JPG = new String[]{".jpg"};
  public static final String[] EXT_ICO = new String[]{".ico"};
  public static final String[] EXT_IMAGES = new String[]{".png", ".gif", ".jpg"};
  public static final String[] EXT_JSP= new String[]{".jsp"};
  public static final String[] EXT_JSPX= new String[]{".jspx"};
  public static final String[] EXT_XHTML= new String[]{".xhtml"};

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
  private final Map<String, String[]> extensionCache
      = new ConcurrentHashMap<String, String[]>(10, 0.75f, 1);

  private TobagoConfigImpl tobagoConfig;

  public ResourceManagerImpl(final TobagoConfigImpl tobagoConfig) {
    this.tobagoConfig = tobagoConfig;
    this.production = tobagoConfig.getProjectStage() == ProjectStage.Production;

    extensionCache.put("", EXT_NONE);
    extensionCache.put(".js", EXT_JS);
    extensionCache.put(".css", EXT_CSS);
    extensionCache.put(".gif", EXT_GIF);
    extensionCache.put(".png", EXT_PNG);
    extensionCache.put(".jpg", EXT_JPG);
    extensionCache.put(".ico", EXT_ICO);
    extensionCache.put(".jsp", EXT_JSP);
    extensionCache.put(".jspx", EXT_JSPX);
    extensionCache.put(".xhtml", EXT_XHTML);
  }

  public void add(final String resourceKey) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("adding resourceKey = '{}'", resourceKey);
    }
    resourceList.put(resourceKey, "");
  }

  public void add(final String resourceKey, final String value) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("adding resourceKey = '{}' value= '{}'", resourceKey, value);
    }
    resourceList.put(resourceKey, value);
  }

  @Deprecated
  public String getJsp(final UIViewRoot viewRoot, final String name) {
    String result = null;
    if (name != null) {

      final ClientPropertiesKey clientKey = ClientPropertiesKey.get(FacesContext.getCurrentInstance());
      final JspCacheKey cacheKey = new JspCacheKey(clientKey, name);

      result = jspCache.get(cacheKey);
      if (result != null) {
        return result;
      }
      try {
        result = (String) getPaths(clientKey, "",
            JSP, name, EXT_NONE, false, true, true, null, true, false).get(0);
        jspCache.put(cacheKey, result);
      } catch (final Exception e) {
        LOG.error("name = '" + name + "' clientProperties = '" + clientKey.toString() + "'", e);
      }
    }
    return result;
  }

  @Deprecated
  public String getProperty(final UIViewRoot viewRoot, final String bundle, final String propertyKey) {
    return getProperty(FacesContext.getCurrentInstance(), bundle, propertyKey);
  }

  public String getProperty(final FacesContext facesContext, final String bundle, final String propertyKey) {

    if (bundle != null && propertyKey != null) {
      final ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
      final PropertyCacheKey cacheKey = new PropertyCacheKey(clientKey, bundle, propertyKey);

      StringValue result = propertyCache.get(cacheKey);
      if (result == null) {
        final List properties
            = getPaths(clientKey, "", PROPERTY, bundle, EXT_NONE, false, true, false, propertyKey, true, false);
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
  public Renderer getRenderer(final UIViewRoot viewRoot, final String rendererType) {
    return getRenderer(FacesContext.getCurrentInstance(), rendererType);
  }

  public Renderer getRenderer(final FacesContext facesContext, final String rendererType) {
    Renderer renderer = null;

    if (rendererType != null) {
      final ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
      final RendererCacheKey cacheKey = new RendererCacheKey(clientKey, rendererType);

      renderer = rendererCache.get(cacheKey);
      if (renderer != null) {
        return renderer;
      }
      String simpleClassName = null;
      try {
        simpleClassName = getRendererClassName(rendererType);
        final List<Class> classes
            = getPaths(clientKey, "", TAG, simpleClassName, EXT_NONE, false, true, true, null, false, false);
        if (classes != null && !classes.isEmpty()) {
          final Class clazz = classes.get(0);
          renderer = (Renderer) clazz.newInstance();
          rendererCache.put(cacheKey, renderer);
        } else {
          LOG.error("Don't find any RendererClass for " + simpleClassName + ". Please check you configuration.");
        }
      } catch (final InstantiationException e) {
        LOG.error("name = '" + simpleClassName + "' clientProperties = '" + clientKey.toString() + "'", e);
      } catch (final IllegalAccessException e) {
        LOG.error("name = '" + simpleClassName + "' clientProperties = '" + clientKey.toString() + "'", e);
      }
    }
    return renderer;
  }

  @Deprecated
  public String[] getScripts(final UIViewRoot viewRoot, final String name) {
    return getScripts(FacesContext.getCurrentInstance(), name);
  }

  public String[] getScripts(final FacesContext facesContext, final String name) {
    return getStrings(facesContext, name, null);
  }

  @Deprecated
  public String[] getStyles(final UIViewRoot viewRoot, final String name) {
    return getStyles(FacesContext.getCurrentInstance(), name);
  }

  public String[] getStyles(final FacesContext facesContext, final String name) {
    return getStrings(facesContext, name, null);
  }

  @Deprecated
  public String getThemeProperty(final UIViewRoot viewRoot, final String bundle, final String propertyKey) {
    if (bundle != null && propertyKey != null) {

      final ClientPropertiesKey clientKey = ClientPropertiesKey.get(FacesContext.getCurrentInstance());
      final PropertyCacheKey cacheKey = new PropertyCacheKey(clientKey, bundle, propertyKey);

      StringValue result = propertyCache.get(cacheKey);
      if (result == null) {
        final List properties
            = getPaths(clientKey, "", PROPERTY, bundle, EXT_NONE, false, true, false, propertyKey, true, true);
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

  public Measure getThemeMeasure(final FacesContext facesContext, final Configurable configurable, final String name) {
    return getThemeMeasure(facesContext, configurable.getRendererType(), configurable.getCurrentMarkup(), name);
  }

  /**
   * The default should not be needed, use defaulting from the theme mechanism.
   */
  public Measure getThemeMeasure(
      final FacesContext facesContext, final Configurable configurable, final String name, final Measure defaultValue) {
    final Measure measure = getThemeMeasure(facesContext, configurable, name);
    if (measure != null) {
      return measure;
    } else {
//      LOG.warn("Using default-value for configurable='" + configurable + "' name='" + name + "'");
      return defaultValue;
    }
  }

  public Measure getThemeMeasure(
      final FacesContext facesContext, final String rendererType, final Markup markup, final String name) {

    final ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
    final ThemeConfigCacheKey cacheKey = new ThemeConfigCacheKey(clientKey, rendererType, markup, name);

    MeasureValue result = themeCache.get(cacheKey);

    if (result == null) {
      final List properties = getPaths(clientKey, "", PROPERTY, "tobago-theme-config", EXT_NONE,
          false, true, false, rendererType + "." + name, true, true);

      Measure measure = null;
      if (properties != null) {
        measure = Measure.valueOf(properties.get(0));
      }

      if (markup != null) {
        for (final String m : markup) {
          final List mProperties = getPaths(clientKey, "", PROPERTY, "tobago-theme-config", EXT_NONE,
              false, true, false, rendererType + "[" + m + "]" + "." + name, true, true);
          if (mProperties != null) {
            final Measure summand = Measure.valueOf(mProperties.get(0));
            measure = summand.add(measure);
          }
        }
      }

      if (measure != null) {
        result = new MeasureValue(measure);
      } else {
        result = MeasureValue.NULL;  // to mark and cache that this value is undefined.
      }
      themeCache.put(cacheKey, result);
    }
    return result.getValue();
  }

  @Deprecated
  public String getImage(final UIViewRoot viewRoot, final String name) {
    return getImage(FacesContext.getCurrentInstance(), name);
  }

  @Deprecated
  public String getImage(final FacesContext facesContext, final String name) {
    return getImage(facesContext, name, false);
  }

  @Deprecated
  public String getImage(final UIViewRoot viewRoot, final String name, final boolean ignoreMissing) {
    return getImage(FacesContext.getCurrentInstance(), name, ignoreMissing);
  }

  /**
   * {@inheritDoc}
   */
  @Deprecated
  public String getImage(
      final FacesContext facesContext, final String nameWithExtension, final boolean ignoreMissing) {
    if (nameWithExtension != null) {
      int dot = nameWithExtension.lastIndexOf('.');
      if (dot == -1) {
        dot = nameWithExtension.length();
      }
      return
          getImage(facesContext, nameWithExtension.substring(0, dot), nameWithExtension.substring(dot), ignoreMissing);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public String getImage(
      final FacesContext facesContext, final String name, final String extension, final boolean ignoreMissing) {
    if (name != null) {
      final String[] extensions;
      if (extension == null) {
        extensions = EXT_IMAGES;
      } else {
        extensions = getExtensions(extension);
      }

      final ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
      final ImageCacheKey cacheKey = new ImageCacheKey(clientKey, name, extension);

      StringValue result = imageCache.get(cacheKey);
      if (result == null) {
        final List paths
            = getPaths(clientKey, "", null, name, extensions, false, true, true, null, true, ignoreMissing);
        if (paths != null) {
          result = new StringValue((String) paths.get(0));
        } else {
          result = StringValue.NULL;
        }
        imageCache.put(cacheKey, result);
      }
      if (LOG.isDebugEnabled()) {
        if (result.getValue() == null) {
          LOG.debug("Can't find image for '{}'", name);
        }
      }

      return result.getValue();
    }

    return null;
  }

  private List getPaths(
      final ClientPropertiesKey clientKey, final String prefix, final String subDir, final String name,
      final String[] extensions, final boolean reverseOrder, final boolean single, final boolean returnKey,
      final String key, final boolean returnStrings, boolean ignoreMissing) {
    final List matches = new ArrayList();
    final String contentType = clientKey.getContentType();
    final Theme theme = clientKey.getTheme();
    final UserAgent browser = clientKey.getUserAgent();
    final List<String> locales = LocaleUtils.getLocaleSuffixList(clientKey.getLocale());

    // check first the local web application directory
    for (final String localeSuffix : locales) {
      for (final String extension : extensions) {
        if (production) {
          boolean found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
              name, MINIMIZE_SUFFIX, localeSuffix, extension, key);
          if (found && (single || !returnStrings)) {
            return matches;
          }
          if (!found) {
            found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
                name, null, localeSuffix, extension, key);
            if (found && (single || !returnStrings)) {
              return matches;
            }
          }
        } else {
          final boolean found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
              name, null, localeSuffix, extension, key);
          if (found && (single || !returnStrings)) {
            return matches;
          }
        }
      }
    }

    // after that check the whole resources tree
    // e.g. 1. application, 2. library or renderkit
    for (final Theme currentTheme : theme.getFallbackList()) {// theme loop
      for (final String resourceDirectory : tobagoConfig.getResourceDirs()) {
        for (final String browserType : browser.getFallbackList()) { // browser loop
          for (final String localeSuffix : locales) { // locale loop
            for (final String extension : extensions) { // extensions loop
              if (production) {
                boolean found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
                    resourceDirectory, contentType, currentTheme, browserType, subDir, name, MINIMIZE_SUFFIX,
                    localeSuffix, extension, key);
                if (found && (single || !returnStrings)) {
                  return matches;
                }
                if (!found) {
                  found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
                      resourceDirectory, contentType, currentTheme, browserType, subDir, name, null,
                      localeSuffix, extension, key);
                  if (found && (single || !returnStrings)) {
                    return matches;
                  }
                }
              } else {
                final boolean found = checkPath(prefix, reverseOrder, returnKey, returnStrings, matches,
                    resourceDirectory, contentType, currentTheme, browserType, subDir, name, null,
                    localeSuffix, extension, key);
                if (found && (single || !returnStrings)) {
                  return matches;
                }
              }
            }
          }
        }
      }
    }

    if (matches.isEmpty()) {

      // XXX hack for Tobago 2.0.x backward compatibility: renaming of style.css to tobago.css
      // XXX style.css should be collected, but missing should be ignored
      if ("style/style".equals(name) && EXT_CSS == extensions) {
        ignoreMissing = true;
      }

      if (!production && !ignoreMissing) {
        LOG.warn("Path not found, and no fallback. Using empty string.\n"
            + "resourceDirs = '" + tobagoConfig.getResourceDirs()
            + "' contentType = '" + contentType
            + "' theme = '" + theme.getName()
            + "' browser = '" + browser
            + "' subDir = '" + subDir
            + "' name = '" + name
            + "' extension = '" + Arrays.toString(extensions)
            + "' key = '" + key
            + "'"/*, new Exception()*/);
      }
      return null;
    } else {
      return matches;
    }
  }

  private boolean checkPath(
      final String prefix, final boolean reverseOrder, final boolean returnKey, final boolean returnStrings,
      final List matches, final String name, final String minimizeSuffix, final String localeSuffix,
      final String extension, final String key) {
    String path = makePath(name, minimizeSuffix, localeSuffix, extension, key);
    if (returnStrings && resourceList.containsKey(path)) {
      final String result =
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
        final Class clazz = Class.forName(path);
        if (LOG.isTraceEnabled()) {
          LOG.trace("testing path: " + path + " *"); // match
        }
        matches.add(clazz);
        return true;
      } catch (final ClassNotFoundException e) {
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
      final String prefix, final boolean reverseOrder, final boolean returnKey, final boolean returnStrings,
      final List matches, final String resourceDirectory, final String contentType, final Theme currentTheme,
      final String browserType, final String subDir, final String name, final String minimizeSuffix,
      final String localeSuffix, final String extension, final String key) {
    String path = makePath(resourceDirectory, contentType, currentTheme, browserType, subDir, name, minimizeSuffix,
        localeSuffix, extension, key, null);
    if (returnStrings && resourceList.containsKey(path)) {
      final String result;
      if (prefix.length() == 0 && returnKey && resourceDirectory.equals(currentTheme.getResourcePath())) {
        result = makePath(resourceDirectory, contentType, currentTheme, browserType, subDir, name, minimizeSuffix,
            localeSuffix, extension, key, currentTheme.getVersion());
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
        final Class clazz = Class.forName(path);
        if (LOG.isTraceEnabled()) {
          LOG.trace("testing path: " + path + " *"); // match
        }
        matches.add(clazz);
        return true;
      } catch (final ClassNotFoundException e) {
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
      final String project, final String language, final Theme theme, final String browser, final String subDir,
      final String name, final String minimizeSuffix, final String localeSuffix, final String extension,
      final String key, final String version) {
    final StringBuilder searchtext = new StringBuilder(64);

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
      final String name, final String minimizeSuffix, final String localeSuffix, final String extension,
      final String key) {
    final StringBuilder searchtext = new StringBuilder(64);

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

  private String getRendererClassName(final String rendererType) {
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

  private String[] getStrings(final FacesContext facesContext, final String name, final String type) {
    String[] result = new String[0];
    if (name != null) {
      final int dot = name.lastIndexOf('.');
      final String nameWithoutExtension;
      final String[] extensions;
      if (dot == -1) {
        nameWithoutExtension = name;
        extensions = EXT_NONE;
      } else {
        nameWithoutExtension = name.substring(0, dot);
        final String extension = name.substring(dot);
        extensions = getExtensions(extension);
      }

      final ClientPropertiesKey key = ClientPropertiesKey.get(facesContext);
      final MiscCacheKey miscKey = new MiscCacheKey(key, name);
      final String[] cacheResult = miscCache.get(miscKey);
      if (cacheResult != null) {
        return cacheResult;
      }
      try {
        final List matches = getPaths(key, "", type,
            nameWithoutExtension, extensions, true, false, true, null, true, false);
        if (matches != null) {
          result = (String[]) matches.toArray(new String[matches.size()]);
        }
        miscCache.put(miscKey, result);
      } catch (final Exception e) {
        LOG.error("name = '" + name + "' clientProperties = '" + key.toString() + "'", e);
      }
    }
    return result;
  }

  private String[] getExtensions(final String extension) {
    final String[] extensions;
    final String[] cached = extensionCache.get(extension);
    if (cached != null) {
      extensions = cached;
    } else {
      extensions = new String[] {extension};
      extensionCache.put(extension, extensions);
      if (LOG.isInfoEnabled()) {
        LOG.info("Adding extension '{}' to cache.", extension);
      }
    }
    return extensions;
  }

  @Override
  public String toString() {
    return "ResourceManagerImpl{"
        + "production=" + production
        + ", resourceList=" + resourceList.size()
        + ", rendererCache=" + rendererCache.size()
        + ", imageCache=" + imageCache.size()
        + ", jspCache=" + jspCache.size()
        + ", miscCache=" + miscCache.size()
        + ", propertyCache=" + propertyCache.size()
        + ", themeCache=" + themeCache.size()
        + ", extensionCache=" + extensionCache.size()
        + '}';
  }
}

package org.apache.myfaces.tobago.context;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

// FIXME: is this class thread-safe?

public class ResourceManagerImpl implements ResourceManager {

  private static final Log LOG = LogFactory.getLog(ResourceManagerImpl.class);
  private static final String PROPERTY = "property";
  private static final String JSP = "jsp";
  private static final String TAG = "tag";

  private final HashMap<String, String> resourceList;

  private final HashMap<RendererCacheKey, Renderer> rendererCache = new HashMap<RendererCacheKey, Renderer>();
  private final HashMap<ImageCacheKey, String> imageCache = new HashMap<ImageCacheKey, String>();
  private final HashMap<JspCacheKey, String> jspCache = new HashMap<JspCacheKey, String>();
  private final HashMap<MiscCacheKey, String[]> miscCache = new HashMap<MiscCacheKey, String[]>();
  private final HashMap<PropertyCacheKey, String> propertyCache = new HashMap<PropertyCacheKey, String>();

  private TobagoConfig tobagoConfig;

  public ResourceManagerImpl(TobagoConfig tobagoConfig) {
    resourceList = new HashMap<String, String>();
    this.tobagoConfig = tobagoConfig;
  }

  public void add(String resourceKey) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("adding resourceKey = '" + resourceKey + "'");
    }
    resourceList.put(resourceKey, "");
  }

  public void add(String resourceKey, String value) {
    if (LOG.isDebugEnabled()) {
      LOG.debug(
          "adding resourceKey = '" + resourceKey + "' value='" + value + "'");
    }
    resourceList.put(resourceKey, value);
  }


  public String getImage(UIViewRoot viewRoot, String name) {
    return getImage(viewRoot, name, false);
  }

  public String getImage(UIViewRoot viewRoot, String name, boolean ignoreMissing) {
    String result = null;
    if (name != null) {
      int dot = name.lastIndexOf('.');
      if (dot == -1) {
        dot = name.length();
      }
      CacheKey key = getCacheKey(viewRoot);

      ImageCacheKey imageKey = new ImageCacheKey(key, name);

      result = imageCache.get(imageKey);
      if (result == null) {
        // TODO: cache null values
        try {
          List paths = getPaths(key.getClientProperties(), key.getLocale(), "", null, name.substring(0, dot),
              name.substring(dot), false, true, true, null, true, ignoreMissing);
          if (paths != null) {
            result = (String) paths.get(0);
          }
          // TODO: cache null values
          imageCache.put(imageKey, result);
        } catch (Exception e) {
          LOG.error("name = '" + name + "' clientProperties = '" + key.getClientPropertyId() + "'", e);
        }
      }
    }

    if (result == null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Can't find image for \"" + name + "\"");
      }
    }

    return result;
  }

  private CacheKey getCacheKey(UIViewRoot viewRoot) {
    CacheKey key;
    if (viewRoot instanceof org.apache.myfaces.tobago.component.UIViewRoot) {
      key = ((org.apache.myfaces.tobago.component.UIViewRoot) viewRoot).getRendererCacheKey();
    } else {
      ClientProperties clientProperties = ClientProperties.getInstance(viewRoot);
      Locale locale;
      if (viewRoot != null) {
        locale = viewRoot.getLocale();
      } else {
        LOG.error("No ViewRoot available calculate Locale for CacheKey.");
        locale =
            FacesContext.getCurrentInstance().getApplication().getViewHandler()
                .calculateLocale(FacesContext.getCurrentInstance());
      }
      key = new CacheKey(clientProperties, locale);
    }
    return key;
  }

  public String getJsp(UIViewRoot viewRoot, String name) {
    String result = null;
    if (name != null) {
      CacheKey key = getCacheKey(viewRoot);

      JspCacheKey jspKey = new JspCacheKey(key, name);

      result = jspCache.get(jspKey);
      if (result != null) {
        return result;
      }
      try {
        result = (String) getPaths(key.getClientProperties(), key.getLocale(), "",
            JSP, name, "", false, true, true, null, true, false).get(0);
        jspCache.put(jspKey, result);
      } catch (Exception e) {
        LOG.error("name = '" + name + "' clientProperties = '" + key.getClientPropertyId() + "'", e);
      }
    }
    return result;
  }

  public String getProperty(
      UIViewRoot viewRoot, String bundle, String propertyKey) {
    String result = null;
    if (bundle != null && propertyKey != null) {
      CacheKey key = getCacheKey(viewRoot);

      PropertyCacheKey propertyCacheKey = new PropertyCacheKey(key, bundle, propertyKey);
      result = propertyCache.get(propertyCacheKey);
      if (result != null) {
        return result;
      }
      List properties = getPaths(key.getClientProperties(), key.getLocale(), "", PROPERTY, bundle,
          "", false, true, false, propertyKey, true, false);
      if (properties != null) {
        result = (String) properties.get(0);
      } else {
        result = null;
      }
      propertyCache.put(propertyCacheKey, result);
    }
    return result;
  }

  private List getPaths(
      ClientProperties clientProperties, Locale locale, String prefix, String subDir, String name, String suffix,
      boolean reverseOrder, boolean single, boolean returnKey, String key, boolean returnStrings,
      boolean ignoreMissing) {
    List matches = new ArrayList();
    String contentType = clientProperties.getContentType();
    Theme theme = clientProperties.getTheme();
    UserAgent browser = clientProperties.getUserAgent();
    List<String> locales = ClientProperties.getLocaleList(locale, false);

    String path;

    // e.g. 1. application, 2. library or renderkit
    for (Theme themeName : theme.getFallbackList()) { // theme loop
      for (String resourceDirectory : tobagoConfig.getResourceDirs()) {
        for (String browserType : browser.getFallbackList()) { // browser loop
          for (String localeSuffix : locales) { // locale loop
            path = makePath(resourceDirectory,
                contentType,
                themeName,
                browserType,
                subDir,
                name,
                localeSuffix,
                suffix,
                key);
            if (checkPath(prefix, reverseOrder, single, returnKey, returnStrings, matches, path)) {
              return matches;
            }
          }
        }
      }
    }
    for (String localeSuffix : locales) { // locale loop
      path = makePath(name, localeSuffix, suffix, key);
      if (checkPath(prefix, reverseOrder, single, returnKey, returnStrings, matches, path)) {
        return matches;
      }
    }
    if (matches.isEmpty()) {
      if (!ignoreMissing) {
        LOG.error("Path not found, and no fallback. Using empty string.\n"
            + "resourceDirs = '" + tobagoConfig.getResourceDirs()
            + "' contentType = '" + contentType
            + "' theme = '" + theme
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
      String prefix, boolean reverseOrder, boolean single, boolean returnKey, boolean returnStrings,
      List matches, String path) {
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
      if (LOG.isDebugEnabled()) {
        LOG.debug("testing path: " + path + " *"); // match
      }

      if (single) {
        return true;
      }
    } else if (!returnStrings) {
      try {
        path = path.substring(1).replace('/', '.');
        Class clazz = Class.forName(path);
        if (LOG.isDebugEnabled()) {
          LOG.debug("testing path: " + path + " *"); // match
        }
        matches.add(clazz);
        return true;
      } catch (ClassNotFoundException e) {
        // not found
        if (LOG.isDebugEnabled()) {
          LOG.debug("testing path: " + path); // no match
        }
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("testing path: " + path); // no match
      }
    }
    return false;
  }

  private String makePath(
      String project, String language, Theme theme, String browser,
      String subDir, String name, String localeSuffix, String extension, String key) {
    StringBuilder searchtext = new StringBuilder(64);

    searchtext.append('/');
    searchtext.append(project);
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
    searchtext.append(localeSuffix);
    searchtext.append(extension);
    if (key != null) {
      searchtext.append('/');
      searchtext.append(key);
    }

    return searchtext.toString();
  }

  private String makePath(
      String name, String localeSuffix, String extension, String key) {
    StringBuilder searchtext = new StringBuilder(64);

    searchtext.append('/');
    searchtext.append(name);
    searchtext.append(localeSuffix);
    searchtext.append(extension);
    if (key != null) {
      searchtext.append('/');
      searchtext.append(key);
    }

    return searchtext.toString();
  }

  public Renderer getRenderer(UIViewRoot viewRoot, String rendererType) {
    Renderer renderer = null;

    if (rendererType != null) {
      CacheKey key = getCacheKey(viewRoot);

      RendererCacheKey rendererKey = new RendererCacheKey(key, rendererType);
      renderer = rendererCache.get(rendererKey);
      if (renderer != null) {
        return renderer;
      }
      String simpleClassName = null;
      try {
        simpleClassName = getRendererClassName(rendererType);
        List<Class> classes = getPaths(key.getClientProperties(), key.getLocale(), "", TAG, simpleClassName, "",
            false, true, true, null, false, false);
        if (classes != null && !classes.isEmpty()) {
          Class clazz = classes.get(0);
          renderer = (Renderer) clazz.newInstance();
          rendererCache.put(rendererKey, renderer);
        } else {
          LOG.error("Don't find any RendererClass for " + simpleClassName + ". Please check you configuration.");
        }
      } catch (InstantiationException e) {
        LOG.error("name = '" + simpleClassName + "' clientProperties = '" + key.getClientPropertyId() + "'", e);
      } catch (IllegalAccessException e) {
        LOG.error("name = '" + simpleClassName + "' clientProperties = '" + key.getClientPropertyId() + "'", e);
      }
    }
    return renderer;
  }


  private String getRendererClassName(String rendererType) {
    String name;
    if (LOG.isDebugEnabled()) {
      LOG.debug("rendererType = '" + rendererType + "'");
    }
    if ("javax.faces.Text".equals(rendererType)) { // TODO: find a better way
      name = RendererTypes.OUT;
    } else {
      name = rendererType;
    }
    name = name + "Renderer";
    if (name.startsWith("javax.faces.")) { // FIXME: this is a hotfix from jsf1.0beta to jsf1.0fr
      LOG.warn("patching renderer from " + name);
      name = name.substring("javax.faces.".length());
      LOG.warn("patching renderer to   " + name);
    }
    return name;
  }

  public String[] getScripts(UIViewRoot viewRoot, String name) {
    return getStrings(viewRoot, name, null);
  }

  public String[] getStyles(UIViewRoot viewRoot, String name) {
    return getStrings(viewRoot, name, null);
  }

  private String[] getStrings(UIViewRoot viewRoot, String name, String type) {
    String[] result = null;
    if (name != null) {
      int dot = name.lastIndexOf('.');
      if (dot == -1) {
        dot = name.length();
      }
      CacheKey key = getCacheKey(viewRoot);
      MiscCacheKey miscKey = new MiscCacheKey(key, name);
      result = miscCache.get(miscKey);
      if (result != null) {
        return result;
      }
      try {
        List matches = getPaths(key.getClientProperties(), key.getLocale(), "", type,
            name.substring(0, dot), name.substring(dot), true, false, true, null, true, false);
        result = (String[]) matches.toArray(new String[matches.size()]);
        miscCache.put(miscKey, result);
      } catch (Exception e) {
        LOG.error("name = '" + name + "' clientProperties = '" + key.getClientPropertyId() + "'", e);
      }
    }
    return result;
  }

  public String getThemeProperty(UIViewRoot viewRoot, String bundle, String propertyKey) {
    String result = null;
    if (bundle != null && propertyKey != null) {
      CacheKey key = getCacheKey(viewRoot);

      PropertyCacheKey propertyCacheKey = new PropertyCacheKey(key, bundle, propertyKey);
      result = propertyCache.get(propertyCacheKey);
      if (result != null) {
        return result;
      }
      List properties = getPaths(key.getClientProperties(), key.getLocale(), "", PROPERTY,
          bundle, "", false, true, false, propertyKey, true, true);
      if (properties != null) {
        result = (String) properties.get(0);
      } else {
        result = null;
      }
      propertyCache.put(propertyCacheKey, result);
    }
    return result;
  }

  public static CacheKey getRendererCacheKey(ClientProperties clientProperties, Locale locale) {
    return new CacheKey(clientProperties, locale);
  }


  private static final class ImageCacheKey {
    private CacheKey cacheKey;
    private String name;
    private int hashCode;

    private ImageCacheKey(CacheKey cacheKey, String name) {
      this.name = name;
      this.cacheKey = cacheKey;
      hashCode = calcHashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      ImageCacheKey that = (ImageCacheKey) o;

      return cacheKey.equals(that.cacheKey) && name.equals(that.name);

    }

    private int calcHashCode() {
      int result;
      result = cacheKey.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }
  }

  private static final class JspCacheKey {
    private final CacheKey cacheKey;
    private final String name;
    private final int hashCode;

    private JspCacheKey(CacheKey cacheKey, String name) {
      this.cacheKey = cacheKey;
      this.name = name;
      hashCode = calcHashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      JspCacheKey that = (JspCacheKey) o;

      return cacheKey.equals(that.cacheKey) && name.equals(that.name);

    }

    private int calcHashCode() {
      int result;
      result = cacheKey.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }
  }

  private static final class PropertyCacheKey {
    private final CacheKey cacheKey;
    private final String name;
    private final String key;
    private final int hashCode;

    private PropertyCacheKey(CacheKey cacheKey, String name, String key) {
      this.cacheKey = cacheKey;
      this.name = name;
      this.key = key;
      hashCode = calcHashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      PropertyCacheKey that = (PropertyCacheKey) o;

      return cacheKey.equals(that.cacheKey) && key.equals(that.key) && name.equals(that.name);

    }

    private int calcHashCode() {
      int result;
      result = cacheKey.hashCode();
      result = 31 * result + name.hashCode();
      result = 31 * result + key.hashCode();
      return result;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return "PropertyCacheKey(" + cacheKey + "," + name + "," + key + "," + hashCode + '}';
    }
  }

  private static final class MiscCacheKey {
    private final CacheKey cacheKey;
    private final String name;
    private final int hashCode;

    private MiscCacheKey(CacheKey cacheKey, String name) {
      this.cacheKey = cacheKey;
      this.name = name;
      hashCode = calcHashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      MiscCacheKey that = (MiscCacheKey) o;

      return cacheKey.equals(that.cacheKey) && name.equals(that.name);

    }

    private int calcHashCode() {
      int result;
      result = cacheKey.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return "MiscCacheKey(" + cacheKey + "," + name + "," + hashCode + ')';
    }
  }

  private static final class RendererCacheKey {
    private final CacheKey cacheKey;
    private final String name;
    private final int hashCode;

    private RendererCacheKey(CacheKey cacheKey, String name) {
      this.cacheKey = cacheKey;
      this.name = name;
      hashCode = calcHashCode();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      RendererCacheKey that = (RendererCacheKey) o;

      return cacheKey.equals(that.cacheKey) && name.equals(that.name);

    }

    private int calcHashCode() {
      int result;
      result = cacheKey.hashCode();
      result = 31 * result + name.hashCode();
      return result;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return "RendererCacheKey(" + cacheKey + "," + name + "," + hashCode + ')';
    }
  }

  public static final class CacheKey implements Serializable {
    private final ClientProperties clientProperties;
    private final Locale locale;
    private final int hashCode;

    private CacheKey(ClientProperties clientProperties, Locale locale) {
      this.clientProperties = clientProperties;
      if (locale == null) { //  FIXME: should not happen, but does.
        LOG.warn("locale == null");
        locale = Locale.getDefault();
      }
      this.locale = locale;
      hashCode = calcHashCode();
    }

    public String getClientPropertyId() {
      return clientProperties.getId();
    }

    public ClientProperties getClientProperties() {
      return clientProperties;
    }

    public Locale getLocale() {
      return locale;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      CacheKey cacheKey = (CacheKey) o;

      return clientProperties.getId().equals(cacheKey.clientProperties.getId()) && locale.equals(cacheKey.locale);

    }

    private int calcHashCode() {
      int result;
      result = clientProperties.getId().hashCode();
      result = 31 * result + locale.hashCode();
      return result;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return "CacheKey(" + clientProperties + "," + locale + "," + hashCode + ')';
    }
  }
}

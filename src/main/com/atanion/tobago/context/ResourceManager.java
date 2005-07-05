/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved.
 * $Id$
 */
package com.atanion.tobago.context;

import com.atanion.tobago.config.TobagoConfig;
import com.atanion.tobago.renderkit.TobagoRenderKit;
import com.atanion.tobago.TobagoConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.render.Renderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

// fixme: is this class thread-safe?

public class ResourceManager {

// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(ResourceManager.class);

  public static final String RESOURCE_MANAGER
      = "com.atanion.tobago.context.ResourceManager";

// ----------------------------------------------------------------- attributes

  private final Properties resourceList;

  private final Cache cache;

  private final List resourceDirectories;

  private final List classDirectories;

  private TobagoConfig tobagoConfig;

// --------------------------------------------------------------- constructors

  public ResourceManager() {
    resourceList = new Properties();
    cache = new Cache();
    resourceDirectories = new ArrayList(2);
    classDirectories = new ArrayList(1);
    classDirectories.add(TobagoRenderKit.PACKAGE_PREFIX.replace('.', '/'));
  }

// ----------------------------------------------------------- business methods

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

  public void addResourceDirectory(String resourceDirectory) {
    this.resourceDirectories.add(resourceDirectory);
    if (LOG.isDebugEnabled()) {
      LOG.debug(
          "Adding resourceDirectory '" + resourceDirectory + "' to " + this);
    }
  }

  public double getCacheCoverage() {
    return cache.found + cache.miss > 0
        ? cache.found / (double) (cache.found + cache.miss) : 0;
  }

  public String getImage(UIViewRoot viewRoot, String name,
      boolean ignoreMissing) {
//    final String type = "image";
    String result = null;
    if (name != null) {
      final String type = null;
      int dot = name.lastIndexOf('.');
      if (dot == -1) {
        dot = name.length();
      }
      String clientPropertyId = ClientProperties.getInstance(viewRoot).getId();
      Locale locale = viewRoot.getLocale();

      result = null;
//    String key = key(clientProperties, locale, type, name);
      ImageCacheKey key = new ImageCacheKey(clientPropertyId, locale, name);
//    Log.debug("key=" + key);
      if ((result = (String) cache.get(key)) != null) {
        // todo: cache null values
        return result;
      }
      try {
        List paths = getPaths(clientPropertyId, locale, resourceDirectories, "", type,
            name.substring(0, dot), name.substring(dot), false, true, true, null,
            true, ignoreMissing);
        if (paths != null) {
          result = (String) paths.get(0);
        }
        // todo: cache null values
        cache.put(key, result);
      } catch (Exception e) {
        LOG.error("name = '" + name
            + "' clientProperties = '" + clientPropertyId + "'", e);
      }
    }

    return result;
  }

  public String getJsp(UIViewRoot viewRoot, String name) {
    final String type = "jsp";
    String result = null;
    if (name != null) {
      String clientPropertyId = ClientProperties.getInstance(viewRoot).getId();
      Locale locale = viewRoot.getLocale();

//    String key = key(clientProperties, locale, type, name);

      JspCacheKey key = new JspCacheKey(clientPropertyId, locale, name);
//    Log.debug("key=" + key);
      if ((result = (String) cache.get(key)) != null) {
        return result;
      }
      try {
        result = (String) getPaths(clientPropertyId, locale, resourceDirectories, "",
            type, name,
            "", false, true, true, null, true, false).get(0);
        cache.put(key, result);
      } catch (Exception e) {
        LOG.error(
            "name = '" + name + "' clientProperties = '" + clientPropertyId +
            "'",
            e);
      }
    }
    return result;
  }

  public String getProperty(UIViewRoot viewRoot, String bundle,
      String propertyKey) {
    return getProperty(viewRoot, bundle, propertyKey, null);
  }
  public String getProperty(UIViewRoot viewRoot, String bundle,
      String propertyKey, Locale locale) {
    final String type = "property";
    String result = null;
    if (bundle != null && propertyKey != null) {
      String clientPropertyId = ClientProperties.getInstance(viewRoot).getId();
      if (locale == null) {
        locale = viewRoot.getLocale();
      }
//    String key = key(clientProperties, locale, type, bundle, propertyKey);

      PropertyCacheKey key = new PropertyCacheKey(clientPropertyId, locale, bundle, propertyKey);
      if ((result = (String) cache.get(key)) != null) {
        return result;
      }
      List properties = getPaths(clientPropertyId, locale, resourceDirectories, "", type, bundle,
          "", false, true, false, propertyKey, true, false);
      if (properties != null) {
        result = (String) properties.get(0);
      } else {
        result = null;
      }
      cache.put(key, result);
    }
    return result;
  }

  private String key(String clientProperties, Locale locale,
      String type, String name, String key) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(clientProperties);
    buffer.append('/');
    buffer.append(locale);
    buffer.append('/');
    buffer.append(type);
    buffer.append('/');
    buffer.append(name);
    buffer.append('/');
    buffer.append(key);
    return buffer.toString();
  }

  private List getPaths(String clientProperties, Locale locale, List mainDirectories, String prefix,
      String subDir, String name, String suffix,
      boolean reverseOrder, boolean single, boolean returnKey,
      String key, boolean returnStrings, boolean ignoreMissing) {
    List matches = new ArrayList();

    StringTokenizer tokenizer = new StringTokenizer(clientProperties, "/");
    String contentType = tokenizer.nextToken();
    Theme theme = tobagoConfig.getTheme(tokenizer.nextToken());
    UserAgent browser = UserAgent.getInstanceForId(tokenizer.nextToken());
    List locales = ClientProperties.getLocaleList(locale, false);

    String path;

    // e.g. 1. application, 2. library or renderkit
    Iterator mainDirectoryIterator = mainDirectories.iterator();
    while (mainDirectoryIterator.hasNext()) {
      String resourceDirectory = (String) mainDirectoryIterator.next();
      Iterator themeIterator = theme.getFallbackList().iterator();
      while (themeIterator.hasNext()) { // theme loop
        Theme themeName = (Theme) themeIterator.next();
        Iterator browserIterator = browser.iterator();
        while (browserIterator.hasNext()) { // browser loop
          String browserType = (String) browserIterator.next();
          Iterator localeIterator = locales.iterator();
          while (localeIterator.hasNext()) { // locale loop
            String localeSuffix = (String) localeIterator.next();
            path = makePath(resourceDirectory,
                contentType,
                themeName,
                browserType,
                subDir,
                name,
                localeSuffix,
                suffix,
                key);
            if (LOG.isDebugEnabled()) {
              LOG.debug("testing path: " + path);
            }
            if (returnStrings && resourceList.containsKey(path)) {
              String result = prefix;

              if (returnKey) {
                result += path;
              } else {
                result += (String) resourceList.get(path);
              }

              if (reverseOrder) {
                matches.add(0, result);
              } else {
                matches.add(result);
              }

              if (single) {
                return matches;
              }
            } else if (!returnStrings) {
              try {
                path = path.substring(1).replace('/', '.');
                Class clazz = Class.forName(path);
                matches.add(clazz);
                return matches;
              } catch (ClassNotFoundException e) {
                // not found
              }
            }
          }
        }
      }
    }
    Iterator localeIterator = locales.iterator();
    while (localeIterator.hasNext()) { // locale loop
      String localeSuffix = (String) localeIterator.next();
      path = makePath(name, localeSuffix, suffix, key);
      if (LOG.isDebugEnabled()) {
        LOG.debug("testing path: " + path);
      }
      if (returnStrings && resourceList.containsKey(path)) {
        String result = prefix;

        if (returnKey) {
          result += path;
        } else {
          result += (String) resourceList.get(path);
        }

        if (reverseOrder) {
          matches.add(0, result);
        } else {
          matches.add(result);
        }

        if (single) {
          return matches;
        }
      } else if (!returnStrings) {
        try {
          path = path.substring(1).replace('/', '.');
          Class clazz = Class.forName(path);
          matches.add(clazz);
          return matches;
        } catch (ClassNotFoundException e) {
          // not found
        }
      }
    }
    if (matches.size() == 0) {
      if (!ignoreMissing) {
        LOG.error("Path not found, and no fallback. Using empty string.\n"
            + "mainDirs = '" + mainDirectories
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

  private String makePath(String project,
      String language, Theme theme, String browser,
      String subDir, String name, String localeSuffix, String extension,
      String key) {
    StringBuffer searchtext = new StringBuffer();

    searchtext.append('/');
    searchtext.append(project);
    searchtext.append('/');
    searchtext.append(language);
    searchtext.append('/');
    searchtext.append(theme);
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
    StringBuffer searchtext = new StringBuffer();

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

  public Renderer getRenderer(UIViewRoot viewRoot, String name) {
    Renderer renderer = null;
    final String clientPropertyId;
    final Locale locale;
    final String type = "tag";
//    final Object key;
    RendererCacheKey key;
    if (name != null) {
      if (viewRoot instanceof com.atanion.tobago.component.UIViewRoot) {
//        key = new StringBuffer(((com.atanion.tobago.component.UIViewRoot)viewRoot)
//            .getRendererCachePrefix()).append(name).toString();
//      key = new RendererCacheKey(clientPropertyId, locale, name);
        key = ((com.atanion.tobago.component.UIViewRoot)viewRoot).getRendererCacheKey();
        key.setName(name);

        if ((renderer = (Renderer) cache.get(key)) != null) {
          return renderer;
        }

        clientPropertyId = ClientProperties.getInstance(viewRoot).getId();
        locale = viewRoot.getLocale();
        key = new RendererCacheKey(clientPropertyId, locale, name);
      } else {
        clientPropertyId = ClientProperties.getInstance(viewRoot).getId();
        locale = viewRoot.getLocale();
//      key = key(clientPropertyId, locale, type, name);

        key = new RendererCacheKey(clientPropertyId, locale, name);
        if ((renderer = (Renderer) cache.get(key)) != null) {
          return renderer;
        }
      }


//    Log.debug("key=" + key);

      try {
        name = getRendererClassName(name);
        Class clazz = (Class) getPaths(clientPropertyId, locale, classDirectories, "", type, name,
            "", false, true, true, null, false, false).get(0);
        renderer = (Renderer) clazz.newInstance();
        cache.put(key, renderer);
      } catch (Exception e) {
        LOG.error(
            "name = '" + name + "' clientProperties = '" + clientPropertyId +
                "'",
            e);
        throw new RuntimeException(name, e);
      }
    }
    return renderer;
  }



  private String getRendererClassName(String rendererType) {
    String name;
    if (LOG.isDebugEnabled()) {
      LOG.debug("rendererType = '" + rendererType + "'");
    }
    if ("javax.faces.Text".equals(rendererType)) { // todo: find a better way
      name = TobagoConstants.RENDERER_TYPE_OUT;
    } else {
      name = rendererType;
    }
    name = name + "Renderer";
    if (name.startsWith("javax.faces.")) { // fixme: this is a hotfix from jsf1.0beta to jsf1.0fr
      LOG.warn("patching renderer from " + name);
      name = name.substring("javax.faces.".length());
      LOG.warn("patching renderer to   " + name);
    }
    return name;
  }

/*
    public Renderer getRenderer(UIViewRoot viewRoot, String name) {
      final String type = "tag";
      Renderer renderer;

      String clientPropertiesID = ClientProperties.getInstance(viewRoot).getId();
      Locale locale = viewRoot.getLocale();
      String key = key(clientPropertiesID, locale, type, name);
  //    Log.debug("key=" + key);
      if ((renderer = (Renderer) cache.get(key)) != null) {
        return renderer;
      }

      try {
        Class clazz = (Class) getPaths(clientPropertiesID, locale, classDirectories, "", type, name,
            "", false, true, true, null, false, false).get(0);
        renderer = (Renderer) clazz.newInstance();
        cache.put(key, renderer);
      } catch (Exception e) {
        LOG.error(
            "name = '" + name + "' clientPropertiesID = '" + clientPropertiesID +
            "'",
            e);
        throw new RuntimeException(name, e);
      }
      return renderer;
    }
  */

  private String key(String clientProperties, Locale locale,
      String type, String name) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(clientProperties);
    buffer.append('/');
    buffer.append(locale);
    if (type != null) {
      buffer.append('/');
      buffer.append(type);
    }
    buffer.append('/');
    buffer.append(name);
    return buffer.toString();
  }

/*
  public String getScript(String clientPropertiesID, String name) {
    final String type = "script";
    int dot = name.lastIndexOf('.');
    if (dot == -1) {
      dot = name.length();
    }
    String result;
    String key = key(clientPropertiesID, type, name);
//    Log.debug("key=" + key);
    if ((result = (String) cache.get(key)) != null) {
      return result;
    }
    try {
      result = (String) getPaths(clientPropertiesID, resourceDirectories, "",
          type, name.substring(0, dot),
          name.substring(dot), false, true, true, null, true, false).get(0);
      cache.put(key, result);
    } catch (Exception e) {
      LOG.error(
          "name = '" + name + "' clientPropertiesID = '" + clientPropertiesID +
          "'",
          e);
    }
    return result;
  }
*/

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
      String clientPropertyId = ClientProperties.getInstance(viewRoot).getId();
      Locale locale = viewRoot.getLocale();
//    String key = key(clientProperties, locale, type, name);
      CacheKey key = new CacheKey(clientPropertyId, locale, name);
//    Log.debug("key=" + key);
      if ((result = (String[]) cache.get(key)) != null) {
        return result;
      }
      try {
        List matches = getPaths(clientPropertyId, locale, resourceDirectories, "",
            type, name.substring(0, dot),
            name.substring(dot), true, false, true, null, true, false);
        result = (String[]) matches.toArray(new String[matches.size()]);
        cache.put(key, result);
      } catch (Exception e) {
        LOG.error(
            "name = '" + name + "' clientProperties = '" + clientPropertyId +
            "'",
            e);
      }
    }
    return result;
  }

  public String getThemeProperty(UIViewRoot viewRoot,
      String bundle, String propertyKey) {
    final String type = "property";
    String result = null;
    if (bundle != null && propertyKey != null) {
      String clientPropertyId = ClientProperties.getInstance(viewRoot).getId();
      Locale locale = viewRoot.getLocale();
      PropertyCacheKey key = new PropertyCacheKey(clientPropertyId, locale, bundle, propertyKey);
      if ((result = (String) cache.get(key)) != null) {
        return result;
      }
      List properties = getPaths(clientPropertyId, locale, resourceDirectories, "", type, bundle,
          "", false, true, false, propertyKey, true, true);
      if (properties != null) {
        result = (String) properties.get(0);
      } else {
        result = null;
      }
      cache.put(key, result);
    }
    return result;
  }

// ------------------------------------------------------------ getter + setter

  public List getResourceDirectories() {
    return resourceDirectories;
  }

  public void setTobagoConfig(TobagoConfig tobagoConfig) {
    this.tobagoConfig = tobagoConfig;
  }

// -------------------------------------------------------------- inner classes

  private class Cache extends HashMap {

    int found;

    int miss;

    public Object put(Object key, Object value) {
      return super.put(key, value);
    }

    public Object get(Object key) {
      Object value = super.get(key);
      if (value != null) {
        found++;
      } else {
        miss++;
      }
      return value;
    }
  }

// ----------------------------------------------------------- cacheKey classes


  public static final class RendererCacheKey {
    private final String clientPropertyId;
    private final Locale locale;
    private String name;

    public RendererCacheKey(String clientPropertyId, Locale locale, String name) {
      this.clientPropertyId = clientPropertyId;
      this.locale = locale;
      this.name = name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final RendererCacheKey that = (RendererCacheKey) o;

      if (!name.equals(that.name)) return false;
      if (!locale.equals(that.locale)) return false;
      if (!clientPropertyId.equals(that.clientPropertyId)) return false;

      return true;
    }

    public int hashCode() {
      int result;
      result = clientPropertyId.hashCode();
      result = 29 * result + locale.hashCode();
      result = 29 * result + name.hashCode();
      return result;
    }
  }

  private final class ImageCacheKey {
    private final String clientPropertyId;
    private final Locale locale;
    private final String name;

    public ImageCacheKey(String clientPropertyId, Locale locale, String name) {
      this.clientPropertyId = clientPropertyId;
      this.locale = locale;
      this.name = name;
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final ImageCacheKey that = (ImageCacheKey) o;

      if (!name.equals(that.name)) return false;
      if (!locale.equals(that.locale)) return false;
      if (!clientPropertyId.equals(that.clientPropertyId)) return false;

      return true;
    }

    public int hashCode() {
      int result;
      result = clientPropertyId.hashCode();
      result = 29 * result + (locale != null ? locale.hashCode() : 0);
      result = 29 * result + name.hashCode();
      return result;
    }
  }

  private final class JspCacheKey {
    private final String clientPropertyId;
    private final Locale locale;
    private final String name;

    public JspCacheKey(String clientPropertyId, Locale locale, String name) {
      this.clientPropertyId = clientPropertyId;
      this.locale = locale;
      this.name = name;
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final JspCacheKey that = (JspCacheKey) o;

      if (!name.equals(that.name)) return false;
      if (!locale.equals(that.locale)) return false;
      if (!clientPropertyId.equals(that.clientPropertyId)) return false;

      return true;
    }

    public int hashCode() {
      int result;
      result = clientPropertyId.hashCode();
      result = 29 * result + (locale != null ? locale.hashCode() : 0);
      result = 29 * result + name.hashCode();
      return result;
    }
  }

  private final class PropertyCacheKey{
    private final String clientPropertyId;
    private final Locale locale;
    private final String name;
    private final String key;

    public PropertyCacheKey(String clientPropertyId, Locale locale, String name, String key) {
      this.clientPropertyId = clientPropertyId;
      this.locale = locale;
      this.name = name;
      this.key = key;
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final PropertyCacheKey that = (PropertyCacheKey) o;

      if (!key.equals(that.key)) return false;
      if (!name.equals(that.name)) return false;
      if (!locale.equals(that.locale)) return false;
      if (!clientPropertyId.equals(that.clientPropertyId)) return false;

      return true;
    }

    public int hashCode() {
      int result;
      result = clientPropertyId.hashCode();
      result = 29 * result + locale.hashCode();
      result = 29 * result + name.hashCode();
      result = 29 * result + key.hashCode();
      return result;
    }
  }



  private final class CacheKey{
    private final String clientPropertyId;
    private final Locale locale;
    private final String name;

    public CacheKey(String clientPropertyId, Locale locale, String name) {
      this.clientPropertyId = clientPropertyId;
      this.locale = locale;
      this.name = name;
    }

    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      final CacheKey cacheKey = (CacheKey) o;

      if (!name.equals(cacheKey.name)) return false;
      if (!locale.equals(cacheKey.locale)) return false;
      if (!clientPropertyId.equals(cacheKey.clientPropertyId)) return false;

      return true;
    }

    public int hashCode() {
      int result;
      result = clientPropertyId.hashCode();
      result = 29 * result + locale.hashCode();
      result = 29 * result + name.hashCode();
      return result;
    }
  }
}


/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved.
 * $Id$
 */
package com.atanion.tobago.context;

import com.atanion.tobago.config.TobagoConfig;
import com.atanion.tobago.renderkit.TobagoRenderKit;
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
    final String type = null;
    int dot = name.lastIndexOf('.');
    if (dot == -1) {
      dot = name.length();
    }
    String clientProperties = ClientProperties.getInstance(viewRoot).getId();
    Locale locale = viewRoot.getLocale();

    String result = null;
    String key = key(clientProperties, locale, type, name);
//    Log.debug("key=" + key);
    if ((result = (String) cache.get(key)) != null) {
      // todo: cache null values
      return result;
    }
    try {
      List paths = getPaths(clientProperties, locale, resourceDirectories, "", type,
          name.substring(0, dot), name.substring(dot), false, true, true, null,
          true, ignoreMissing);
      if (paths != null) {
        result = (String) paths.get(0);
      }
      // todo: cache null values
      cache.put(key, result);
    } catch (Exception e) {
      LOG.error("name = '" + name
          + "' clientProperties = '" + clientProperties + "'", e);
    }

    return result;
  }

  public String getJsp(UIViewRoot viewRoot, String name) {
    final String type = "jsp";
    String result;
    String clientProperties = ClientProperties.getInstance(viewRoot).getId();
    Locale locale = viewRoot.getLocale();

    String key = key(clientProperties, locale, type, name);
//    Log.debug("key=" + key);
    if ((result = (String) cache.get(key)) != null) {
      return result;
    }
    try {
      result = (String) getPaths(clientProperties, locale, resourceDirectories, "",
          type, name,
          "", false, true, true, null, true, false).get(0);
      cache.put(key, result);
    } catch (Exception e) {
      LOG.error(
          "name = '" + name + "' clientProperties = '" + clientProperties +
          "'",
          e);
    }
    return result;
  }

  public String getProperty(UIViewRoot viewRoot, String bundle,
      String propertyKey) {
    final String type = "property";
    String result;
    String clientProperties = ClientProperties.getInstance(viewRoot).getId();
    Locale locale = viewRoot.getLocale();
    String key = key(clientProperties, locale, type, bundle, propertyKey);
    if ((result = (String) cache.get(key)) != null) {
      return result;
    }
    List properties = getPaths(clientProperties, locale, resourceDirectories, "", type, bundle,
        "", false, true, false, propertyKey, true, false);
    if (properties != null) {
      result = (String) properties.get(0);
    } else {
      result = null;
    }
    cache.put(key, result);
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
      Iterator themeIterator = theme.fallbackIterator();
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
    final String type = "tag";
    Renderer renderer;

    String clientProperties = ClientProperties.getInstance(viewRoot).getId();
    Locale locale = viewRoot.getLocale();
    String key = key(clientProperties, locale, type, name);
//    Log.debug("key=" + key);
    if ((renderer = (Renderer) cache.get(key)) != null) {
      return renderer;
    }

    try {
      Class clazz = (Class) getPaths(clientProperties, locale, classDirectories, "", type, name,
          "", false, true, true, null, false, false).get(0);
      renderer = (Renderer) clazz.newInstance();
      cache.put(key, renderer);
    } catch (Exception e) {
      LOG.error(
          "name = '" + name + "' clientProperties = '" + clientProperties +
          "'",
          e);
      throw new RuntimeException(name, e);
    }
    return renderer;
  }

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
  public String getScript(String clientProperties, String name) {
    final String type = "script";
    int dot = name.lastIndexOf('.');
    if (dot == -1) {
      dot = name.length();
    }
    String result;
    String key = key(clientProperties, type, name);
//    Log.debug("key=" + key);
    if ((result = (String) cache.get(key)) != null) {
      return result;
    }
    try {
      result = (String) getPaths(clientProperties, resourceDirectories, "",
          type, name.substring(0, dot),
          name.substring(dot), false, true, true, null, true, false).get(0);
      cache.put(key, result);
    } catch (Exception e) {
      LOG.error(
          "name = '" + name + "' clientProperties = '" + clientProperties +
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
    int dot = name.lastIndexOf('.');
    if (dot == -1) {
      dot = name.length();
    }
    String clientProperties = ClientProperties.getInstance(viewRoot).getId();
    Locale locale = viewRoot.getLocale();
    String key = key(clientProperties, locale, type, name);

    List matches = getPaths(clientProperties, locale, resourceDirectories, "",
        type, name.substring(0, dot),
        name.substring(dot), true, false, true, null, true, false);
    String[] result;
//    Log.debug("key=" + key);
    if ((result = (String[]) cache.get(key)) != null) {
      return result;
    }
    try {
      result = (String[]) matches.toArray(new String[matches.size()]);
      cache.put(key, result);
    } catch (Exception e) {
      LOG.error(
          "name = '" + name + "' clientProperties = '" + clientProperties +
          "'",
          e);
    }
    return result;
  }

  public String getThemeProperty(UIViewRoot viewRoot,
      String bundle, String propertyKey) {
    final String type = "property";
    String result;
    String clientProperties = ClientProperties.getInstance(viewRoot).getId();
    Locale locale = viewRoot.getLocale();
    String key = key(clientProperties, locale, type, bundle, propertyKey);
    if ((result = (String) cache.get(key)) != null) {
      return result;
    }
    List properties = getPaths(clientProperties, locale, resourceDirectories, "", type, bundle,
        "", false, true, false, propertyKey, true, true);
    if (properties != null) {
      result = (String) properties.get(0);
    } else {
      result = null;
    }
    cache.put(key, result);
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
}


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
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.UserAgent;
import org.apache.myfaces.tobago.internal.config.TobagoConfigImpl;
import org.apache.myfaces.tobago.util.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceManagerImpl implements ResourceManager {

  private static final Logger LOG = LoggerFactory.getLogger(ResourceManagerImpl.class);
  private static final String PROPERTY = "property";
  private static final String MINIMIZE_SUFFIX = ".min";

  private static final String[] EXT_NONE = new String[]{""};
  private static final String[] EXT_JS = new String[]{".js"};
  private static final String[] EXT_CSS = new String[]{".css"};
  private static final String[] EXT_GIF = new String[]{".gif"};
  private static final String[] EXT_PNG = new String[]{".png"};
  private static final String[] EXT_JPG = new String[]{".jpg"};
  private static final String[] EXT_ICO = new String[]{".ico"};
  private static final String[] EXT_IMAGES = new String[]{".png", ".gif", ".jpg"};
  private static final String[] EXT_XHTML= new String[]{".xhtml"};

  private boolean production;

  private final Map<String, String> resourceList
      = new ConcurrentHashMap<String, String>(100, 0.75f, 1);

  private final Map<ImageCacheKey, StringValue> imageCache
      = new ConcurrentHashMap<ImageCacheKey, StringValue>(100, 0.75f, 1);
  private final Map<MiscCacheKey, String[]> miscCache
      = new ConcurrentHashMap<MiscCacheKey, String[]>(100, 0.75f, 1);
  private final Map<PropertyCacheKey, StringValue> propertyCache
      = new ConcurrentHashMap<PropertyCacheKey, StringValue>(100, 0.75f, 1);
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

  @Override
  public String getProperty(final FacesContext facesContext, final String bundle, final String propertyKey) {

    if (bundle != null && propertyKey != null) {
      final ClientPropertiesKey clientKey = ClientPropertiesKey.get(facesContext);
      final PropertyCacheKey cacheKey = new PropertyCacheKey(clientKey, bundle, propertyKey);

      StringValue result = propertyCache.get(cacheKey);
      if (result == null) {
        final List properties
            = getPaths(clientKey, PROPERTY, bundle, EXT_NONE, false, true, false, propertyKey, false);
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

  @Override
  public String[] getScripts(final FacesContext facesContext, final String name) {
    return getStrings(facesContext, name, null);
  }

  @Override
  public String[] getStyles(final FacesContext facesContext, final String name) {
    return getStrings(facesContext, name, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  @Override
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
            = getPaths(clientKey, null, name, extensions, false, true, true, null, ignoreMissing);
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
      final ClientPropertiesKey clientKey, final String subDir, final String name,
      final String[] extensions, final boolean reverseOrder, final boolean single, final boolean returnKey,
      final String key, boolean ignoreMissing) {
    final List<String> matches = new ArrayList<String>();
    final String contentType = clientKey.getContentType();
    final Theme theme = clientKey.getTheme();
    final UserAgent browser = clientKey.getUserAgent();
    final List<String> locales = LocaleUtils.getLocaleSuffixList(clientKey.getLocale());

    // check first the local web application directory
    for (final String localeSuffix : locales) {
      for (final String extension : extensions) {
        if (production) {
          boolean found = checkPath(reverseOrder, returnKey, matches,
              name, MINIMIZE_SUFFIX, localeSuffix, extension, key);
          if (found && single) {
            return matches;
          }
          if (!found) {
            found = checkPath(reverseOrder, returnKey, matches,
                name, null, localeSuffix, extension, key);
            if (found && single) {
              return matches;
            }
          }
        } else {
          final boolean found = checkPath(reverseOrder, returnKey, matches,
              name, null, localeSuffix, extension, key);
          if (found && single) {
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
                boolean found = checkPath(reverseOrder, returnKey, matches,
                    resourceDirectory, contentType, currentTheme, browserType, subDir, name, MINIMIZE_SUFFIX,
                    localeSuffix, extension, key);
                if (found && single) {
                  return matches;
                }
                if (!found) {
                  found = checkPath(reverseOrder, returnKey, matches,
                      resourceDirectory, contentType, currentTheme, browserType, subDir, name, null,
                      localeSuffix, extension, key);
                  if (found && single) {
                    return matches;
                  }
                }
              } else {
                final boolean found = checkPath(reverseOrder, returnKey, matches,
                    resourceDirectory, contentType, currentTheme, browserType, subDir, name, null,
                    localeSuffix, extension, key);
                if (found && single) {
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
        LOG.warn("Path not found, and no fallback (using empty string) "
            + "resourceDirs='" + tobagoConfig.getResourceDirs()
            + "' contentType='" + contentType
            + "' theme='" + theme.getName()
            + "' browser='" + browser
            + "' subDir='" + subDir
            + "' name='" + name
            + "' extension='" + Arrays.toString(extensions)
            + "' key='" + key
            + "'"/*, new Exception()*/);
      }
      return null;
    } else {
      return matches;
    }
  }

  private boolean checkPath(
      final boolean reverseOrder, final boolean returnKey,
      final List<String> matches, final String name, final String minimizeSuffix, final String localeSuffix,
      final String extension, final String key) {
    String path = makePath(name, minimizeSuffix, localeSuffix, extension, key);
    if (resourceList.containsKey(path)) {
      final String result =
          returnKey
              ? path
              : resourceList.get(path);

      if (reverseOrder) {
        matches.add(0, result);
      } else {
        matches.add(result);
      }
      if (LOG.isTraceEnabled()) {
        LOG.trace("testing path: {} *", path); // match
      }

      return true;
    } else {
      if (LOG.isTraceEnabled()) {
        LOG.trace("testing path: " + path); // no match
      }
    }
    return false;
  }

  private boolean checkPath(
      final boolean reverseOrder, final boolean returnKey,
      final List<String> matches, final String resourceDirectory, final String contentType, final Theme currentTheme,
      final String browserType, final String subDir, final String name, final String minimizeSuffix,
      final String localeSuffix, final String extension, final String key) {
    String path = makePath(resourceDirectory, contentType, currentTheme, browserType, subDir, name, minimizeSuffix,
        localeSuffix, extension, key, null);
    if (resourceList.containsKey(path)) {
      final String result;
      if (returnKey && resourceDirectory.equals(currentTheme.getResourcePath())) {
        result = makePath(resourceDirectory, contentType, currentTheme, browserType, subDir, name, minimizeSuffix,
            localeSuffix, extension, key, currentTheme.getVersion());
      } else {
        result = returnKey ? path : resourceList.get(path);
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
    final StringBuilder search = new StringBuilder(64);

    search.append('/');
    search.append(project);
    if (version != null) {
      search.append('/');
      search.append(version);
    }
    search.append('/');
    search.append(language);
    search.append('/');
    search.append(theme.getName());
    search.append('/');
    search.append(browser);
    if (subDir != null) {
      search.append('/');
      search.append(subDir);
    }
    search.append('/');
    search.append(name);
    if (minimizeSuffix != null) {
      search.append(minimizeSuffix);
    }
    search.append(localeSuffix);
    search.append(extension);
    if (key != null) {
      search.append('/');
      search.append(key);
    }

    return search.toString();
  }

  private String makePath(
      final String name, final String minimizeSuffix, final String localeSuffix, final String extension,
      final String key) {
    final StringBuilder search = new StringBuilder(64);

    search.append('/');
    search.append(name);
    if (minimizeSuffix != null) {
      search.append(minimizeSuffix);
    }
    search.append(localeSuffix);
    search.append(extension);
    if (key != null) {
      search.append('/');
      search.append(key);
    }

    return search.toString();
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
        final List matches = getPaths(key, type, nameWithoutExtension, extensions, true, false, true, null, false);
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
        + ", imageCache=" + imageCache.size()
        + ", miscCache=" + miscCache.size()
        + ", propertyCache=" + propertyCache.size()
        + ", extensionCache=" + extensionCache.size()
        + '}';
  }
}

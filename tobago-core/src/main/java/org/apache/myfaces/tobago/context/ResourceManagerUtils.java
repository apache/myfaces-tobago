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

package org.apache.myfaces.tobago.context;

import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ResourceManagerUtils {

  private ResourceManagerUtils() {
    // no instance
  }

  public static String getProperty(final FacesContext facesContext, final String bundle, final String key) {
    return ResourceManagerFactory.getResourceManager(facesContext).getProperty(facesContext, bundle, key);
  }

  public static String getPropertyNotNull(final FacesContext facesContext, final String bundle, final String key) {
    final String result
        = ResourceManagerFactory.getResourceManager(facesContext).getProperty(facesContext, bundle, key);
    if (result == null) {
      return "???" + key + "???";
    } else {
      return result;
    }
  }

  /**
   * Searches for an image and return it with the context path
   */
  public static String getImageWithPath(final FacesContext facesContext, final String name) {
    return getImageWithPath(facesContext, name, false);
  }

  /**
   * Searches for an image and return it with the context path
   */
  public static String getImageWithPath(
      final FacesContext facesContext, final String name, final boolean ignoreMissing) {
    final String image
        = ResourceManagerFactory.getResourceManager(facesContext).getImage(facesContext, name, ignoreMissing);
    if (image == null) {
      return null;
    } else {
      return facesContext.getExternalContext().getRequestContextPath() + image;
    }
  }

  public static List<String> getStyles(final FacesContext facesContext, final String name) {
    final String contextPath = facesContext.getExternalContext().getRequestContextPath();
    final String[] styles = ResourceManagerFactory.getResourceManager(facesContext).getStyles(facesContext, name);
    return addContextPath(styles, contextPath);
  }

  private static List<String> addContextPath(final String[] strings, final String contextPath) {
    final List<String> withContext = new ArrayList<String>(strings.length);
    for (final String string : strings) {
      withContext.add(contextPath + string);
    }
    return withContext;
  }

  public static List<String> getScripts(final FacesContext facesContext, final String name) {
    final String contextPath = facesContext.getExternalContext().getRequestContextPath();
    final String[] scripts = ResourceManagerFactory.getResourceManager(facesContext).getScripts(facesContext, name);
    return addContextPath(scripts, contextPath);
  }

  public static String getScriptsAsJSArray(final FacesContext facesContext, final String[] names) {
    final List<String> fileNames = new ArrayList<String>();
    for (final String name : names) {
      fileNames.addAll(getScripts(facesContext, name));
    }
    return toJSArray(fileNames);
  }

  public static String getStylesAsJSArray(final FacesContext facesContext, final String[] names) {
    final List<String> fileNames = new ArrayList<String>();
    for (final String name : names) {
      fileNames.addAll(getStyles(facesContext, name));
    }
    return toJSArray(fileNames);
  }

  public static String toJSArray(final List<String> list) {
    final StringBuilder sb = new StringBuilder();
    for (final String name : list) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append('\'');
      sb.append(name);
      sb.append('\'');
    }
    return "[" + sb.toString() + "]";
  }

  public static String getDisabledImageWithPath(final FacesContext facesContext, final String image,
                                                final boolean ignoreMissing) {
    final String filename = ResourceUtils.addPostfixToFilename(image, "Disabled");
    return getImageWithPath(facesContext, filename, ignoreMissing);
  }

  /**
   * Blank page e. g. useful to set src of iframes (to prevent https problems in ie, see TOBAGO-538)
   */
  public static String getBlankPage(final FacesContext facesContext) {
    return facesContext.getExternalContext().getRequestContextPath()
        + "/org/apache/myfaces/tobago/renderkit/html/standard/blank.html";
  }

  public static String getPageWithoutContextPath(final FacesContext facesContext, final String name) {
    return ResourceManagerFactory.getResourceManager(facesContext).getImage(facesContext, name);
  }

  public static Measure getThemeMeasure(
      final FacesContext facesContext, final Configurable configurable, final String name) {
    return ResourceManagerFactory.getResourceManager(facesContext).getThemeMeasure(
        facesContext, configurable.getRendererType(), configurable.getCurrentMarkup(), name);
  }

  /**
   * Detects if the value is an absolute resource or if the value has to be processed by the theme mechanism. A resource
   * will be treated as absolute, if the value starts with HTTP:, HTTPS:, FTP: or a slash. The case will be ignored by
   * this check. Null values will return true.
   *
   * @param value the given resource link.
   * @return true if it is an external or absolute resource.
   */
  public static boolean isAbsoluteResource(final String value) {
    if (value == null) {
      return true;
    }
    final String upper = value.toUpperCase(Locale.ENGLISH);
    return (upper.startsWith("/")
        || upper.startsWith("HTTP:")
        || upper.startsWith("HTTPS:")
        || upper.startsWith("FTP:"));
  }

  public static String getImageOrDisabledImageWithPath(
      final FacesContext facesContext, final String image, final boolean disabled) {
    return getImageOrDisabledImageWithPath(facesContext, image, disabled, false);
  }

  public static String getImageOrDisabledImageWithPath(
          final FacesContext facesContext, final String image, final boolean disabled, final boolean ignoreMissing) {
    String imageWithPath = null;
    if (disabled) {
      imageWithPath = ResourceManagerUtils.getDisabledImageWithPath(facesContext, image, ignoreMissing);
    }
    if (imageWithPath == null) {
      imageWithPath = ResourceManagerUtils.getImageWithPath(facesContext, image, ignoreMissing);
    }
    return imageWithPath;
  }
}

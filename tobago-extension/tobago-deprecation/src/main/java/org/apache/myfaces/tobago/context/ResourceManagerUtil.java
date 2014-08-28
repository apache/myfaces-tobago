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
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated please use {@link ResourceManagerUtils}
 */
@Deprecated
public class ResourceManagerUtil {

  private ResourceManagerUtil() {
    // no instance
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getProperty(final FacesContext facesContext, final String bundle, final String key) {
    return ResourceManagerFactory.getResourceManager(facesContext).getProperty(facesContext, bundle, key);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
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
   * @deprecated please use {@link ResourceManagerUtils}
   */
  public static String getImageWithPath(final FacesContext facesContext, final String name) {
    return ResourceManagerUtils.getImageWithPath(facesContext, name);
  }

  /**
   * Searches for an image and return it with the context path
   * @deprecated please use {@link ResourceManagerUtils}
   */
  public static String getImageWithPath(
      final FacesContext facesContext, final String name, final boolean ignoreMissing) {
    return ResourceManagerUtils.getImageWithPath(facesContext, name, ignoreMissing);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static List<String> getStyles(final FacesContext facesContext, final String name) {
    return ResourceManagerUtils.getStyles(facesContext, name);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  private static List<String> addContextPath(final String[] strings, final String contextPath) {
    final List<String> withContext = new ArrayList<String>(strings.length);
    for (final String string : strings) {
      withContext.add(contextPath + string);
    }
    return withContext;
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static List<String> getScripts(final FacesContext facesContext, final String name) {
    final String contextPath = facesContext.getExternalContext().getRequestContextPath();
    final String[] scripts = ResourceManagerFactory.getResourceManager(facesContext).getScripts(facesContext, name);
    return addContextPath(scripts, contextPath);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getScriptsAsJSArray(final FacesContext facesContext, final String[] names) {
    final List<String> fileNames = new ArrayList<String>();
    for (final String name : names) {
      fileNames.addAll(getScripts(facesContext, name));
    }
    return toJSArray(fileNames);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getStylesAsJSArray(final FacesContext facesContext, final String[] names) {
    final List<String> fileNames = new ArrayList<String>();
    for (final String name : names) {
      fileNames.addAll(getStyles(facesContext, name));
    }
    return toJSArray(fileNames);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
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

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getDisabledImageWithPath(final FacesContext facesContext, final String image) {
    final String filename = ResourceUtils.addPostfixToFilename(image, "Disabled");
    return getImageWithPath(facesContext, filename, true);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getBlankPage(final FacesContext facesContext) {
    return ResourceManagerUtils.getBlankPage(facesContext);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getPageWithoutContextPath(final FacesContext facesContext, final String name) {
    return ResourceManagerUtils.getPageWithoutContextPath(facesContext, name);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static Measure getThemeMeasure(
      final FacesContext facesContext, final Configurable configurable, final String name) {
    return ResourceManagerUtils.getThemeMeasure(facesContext, configurable, name);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static boolean isAbsoluteResource(final String value) {
    return ResourceManagerUtils.isAbsoluteResource(value);
  }
  
}

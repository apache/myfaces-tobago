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

import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResourceManagerUtils {

  private ResourceManagerUtils() {
    // no instance
  }

  public static String getProperty(FacesContext facesContext, String bundle, String key) {
    return     org.apache.myfaces.tobago.internal.context.ResourceManagerFactory
        .getResourceManager(facesContext).getProperty(facesContext, bundle, key);
  }

  public static String getPropertyNotNull(FacesContext facesContext, String bundle, String key) {
    String result = org.apache.myfaces.tobago.internal.context.ResourceManagerFactory
        .getResourceManager(facesContext).getProperty(facesContext, bundle, key);
    if (result == null) {
      return "???" + key + "???";
    } else {
      return result;
    }
  }

  /**
   * Searches for an image and return it with the context path
   */
  public static String getImageWithPath(FacesContext facesContext, String name) {
    return facesContext.getExternalContext().getRequestContextPath()
        + org.apache.myfaces.tobago.internal.context.ResourceManagerFactory
        .getResourceManager(facesContext).getImage(facesContext, name);
  }

  /**
   * Searches for an image and return it with the context path
   */
  public static String getImageWithPath(FacesContext facesContext, String name, boolean ignoreMissing) {
    String image = org.apache.myfaces.tobago.internal.context.ResourceManagerFactory
        .getResourceManager(facesContext).getImage(facesContext, name, ignoreMissing);
    if (image == null) {
      return null;
    } else {
      return facesContext.getExternalContext().getRequestContextPath() + image;
    }
  }

  public static List<String> getStyles(FacesContext facesContext, String name) {
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] styles = org.apache.myfaces.tobago.internal.context.ResourceManagerFactory
        .getResourceManager(facesContext).getStyles(facesContext, name);
    return addContextPath(styles, contextPath);
  }

  private static List<String> addContextPath(String[] strings, String contextPath) {
    List<String> withContext = new ArrayList<String>(strings.length);
    for (String string : strings) {
      withContext.add(contextPath + string);
    }
    return withContext;
  }

  public static List<String> getScripts(FacesContext facesContext, String name) {
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] scripts = org.apache.myfaces.tobago.internal.context.ResourceManagerFactory
        .getResourceManager(facesContext).getScripts(facesContext, name);
    return addContextPath(scripts, contextPath);
  }

  public static String getScriptsAsJSArray(FacesContext facesContext, String[] names) {
    List<String> fileNames = new ArrayList<String>();
    for (String name : names) {
      fileNames.addAll(getScripts(facesContext, name));
    }
    return toJSArray(fileNames);
  }

  public static String getStylesAsJSArray(FacesContext facesContext, String[] names) {
    List<String> fileNames = new ArrayList<String>();
    for (String name : names) {
      fileNames.addAll(getStyles(facesContext, name));
    }
    return toJSArray(fileNames);
  }

  public static String toJSArray(List<String> list) {
    StringBuilder sb = new StringBuilder();
    for (String name : list) {
      if (sb.length() > 0) {
        sb.append(", ");
      }
      sb.append('\'');
      sb.append(name);
      sb.append('\'');
    }
    return "[" + sb.toString() + "]";
  }

  public static String getDisabledImageWithPath(FacesContext facesContext, String image) {
    String filename = ResourceUtils.addPostfixToFilename(image, "Disabled");
    return getImageWithPath(facesContext, filename, true);
  }

  /**
   * Blank page e. g. useful to set src of iframes (to prevent https problems in ie, see TOBAGO-538)
   */
  public static String getBlankPage(FacesContext facesContext) {
    return facesContext.getExternalContext().getRequestContextPath()
        + "/org/apache/myfaces/tobago/renderkit/html/standard/blank.html";
  }

  public static String getPageWithoutContextPath(FacesContext facesContext, String name) {
    return org.apache.myfaces.tobago.internal.context.ResourceManagerFactory
        .getResourceManager(facesContext).getImage(facesContext, name);
  }
  
  public static Measure getThemeMeasure(FacesContext facesContext, Configurable configurable, String name) {
    return org.apache.myfaces.tobago.internal.context.ResourceManagerFactory
        .getResourceManager(facesContext).getThemeMeasure(
        facesContext, configurable.getRendererType(), configurable.getCurrentMarkup(), name);
  }

  /**
   * Detects if the value is an absolute resource or if the value has to be processed by the
   * theme mechanism. A resource will be treated as absolute, if the value starts with HTTP:, HTTPS:, FTP: or a slash.
   * The case will be ignored by this check. Null values will return true.
   *
   * @param value the given resource link.
   * @return true if it is an external or absolute resource.
   */
  public static boolean isAbsoluteResource(String value) {
    if (value == null) {
      return true;
    }
    String upper = value.toUpperCase(Locale.ENGLISH);
    return (upper.startsWith("/")
        || upper.startsWith("HTTP:")
        || upper.startsWith("HTTPS:")
        || upper.startsWith("FTP:"));
  }

  public static String getImageOrDisabledImageWithPath(FacesContext facesContext, String image, boolean disabled) {
    String imageWithPath = null;
    if (disabled) {
      imageWithPath = ResourceManagerUtils.getDisabledImageWithPath(facesContext, image);
    }
    if (imageWithPath == null) {
      imageWithPath = ResourceManagerUtils.getImageWithPath(facesContext, image);
    }
    return imageWithPath;
  }
}

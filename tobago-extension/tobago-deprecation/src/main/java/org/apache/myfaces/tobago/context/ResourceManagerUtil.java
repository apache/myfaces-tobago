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
  public static String getProperty(FacesContext facesContext, String bundle, String key) {
    return ResourceManagerFactory.getResourceManager(facesContext).getProperty(facesContext, bundle, key);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getPropertyNotNull(FacesContext facesContext, String bundle, String key) {
    String result = ResourceManagerFactory.getResourceManager(facesContext).getProperty(facesContext, bundle, key);
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
  public static String getImageWithPath(FacesContext facesContext, String name) {
    return facesContext.getExternalContext().getRequestContextPath()
        + ResourceManagerFactory.getResourceManager(facesContext).getImage(facesContext, name);
  }

  /**
   * Searches for an image and return it with the context path
   * @deprecated please use {@link ResourceManagerUtils}
   */
  public static String getImageWithPath(FacesContext facesContext, String name, boolean ignoreMissing) {
    String image = ResourceManagerFactory.getResourceManager(facesContext).getImage(facesContext, name, ignoreMissing);
    if (image == null) {
      return null;
    } else {
      return facesContext.getExternalContext().getRequestContextPath() + image;
    }
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static List<String> getStyles(FacesContext facesContext, String name) {
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] styles = ResourceManagerFactory.getResourceManager(facesContext).getStyles(facesContext, name);
    return addContextPath(styles, contextPath);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  private static List<String> addContextPath(String[] strings, String contextPath) {
    List<String> withContext = new ArrayList<String>(strings.length);
    for (String string : strings) {
      withContext.add(contextPath + string);
    }
    return withContext;
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static List<String> getScripts(FacesContext facesContext, String name) {
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] scripts = ResourceManagerFactory.getResourceManager(facesContext).getScripts(facesContext, name);
    return addContextPath(scripts, contextPath);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getScriptsAsJSArray(FacesContext facesContext, String[] names) {
    List<String> fileNames = new ArrayList<String>();
    for (String name : names) {
      fileNames.addAll(getScripts(facesContext, name));
    }
    return toJSArray(fileNames);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getStylesAsJSArray(FacesContext facesContext, String[] names) {
    List<String> fileNames = new ArrayList<String>();
    for (String name : names) {
      fileNames.addAll(getStyles(facesContext, name));
    }
    return toJSArray(fileNames);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
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

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getDisabledImageWithPath(FacesContext facesContext, String image) {
    String filename = ResourceUtils.addPostfixToFilename(image, "Disabled");
    return getImageWithPath(facesContext, filename, true);
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getBlankPage(FacesContext facesContext) {
    return facesContext.getExternalContext().getRequestContextPath()
        + "/org/apache/myfaces/tobago/renderkit/html/standard/blank.html";
  }

  /**
   * @deprecated please use {@link ResourceManagerUtils}
   */
  @Deprecated
  public static String getPageWithoutContextPath(FacesContext facesContext, String name) {
    return ResourceManagerFactory.getResourceManager(facesContext).getImage(facesContext, name);
  }

/**
 * @deprecated please use {@link ResourceManagerUtils}
 */
@Deprecated
  public static Measure getThemeMeasure(FacesContext facesContext, Configurable configurable, String name) {
    return ResourceManagerFactory.getResourceManager(facesContext).getThemeMeasure(
        facesContext, configurable.getRendererType(), configurable.getMarkup(), name);
  }

}

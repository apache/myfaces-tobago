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

import org.apache.myfaces.tobago.renderkit.html.CommandRendererHelper;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class ResourceManagerUtil {

  private ResourceManagerUtil() {
  }

  public static String getProperty(
      FacesContext facesContext, String bundle, String key) {
    return ResourceManagerFactory.getResourceManager(facesContext)
        .getProperty(facesContext.getViewRoot(), bundle, key);
  }

  public static String getPropertyNotNull(
      FacesContext facesContext, String bundle, String key) {
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String result = ResourceManagerFactory.getResourceManager(facesContext)
        .getProperty(viewRoot, bundle, key);
    if (result == null) {
      return "???" + key + "???";
    } else {
      return result;
    }
  }

  /**
   * Searchs for an image and return it with the context path
   */
  public static String getImageWithPath(
      FacesContext facesContext, String name) {
    return facesContext.getExternalContext().getRequestContextPath()
        + ResourceManagerFactory.getResourceManager(facesContext)
        .getImage(facesContext.getViewRoot(), name);
  }

  public static String getBlankPage(FacesContext context) {
    return  context.getExternalContext().getRequestContextPath()
        + "/org/apache/myfaces/tobago/renderkit/html/standard/blank.html";
  }

  /**
   * Searchs for an image and return it with the context path
   */
  public static String getImageWithPath(
      FacesContext facesContext, String name, boolean ignoreMissing) {
    String image = ResourceManagerFactory.getResourceManager(facesContext)
        .getImage(facesContext.getViewRoot(), name, ignoreMissing);
    if (image == null) {
      return null;
    } else {
      return facesContext.getExternalContext().getRequestContextPath() + image;
    }
  }

  public static List<String> getStyles(FacesContext facesContext, String name) {
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] styles = ResourceManagerFactory.getResourceManager(facesContext).getStyles(viewRoot, name);
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
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    String[] scripts = ResourceManagerFactory.getResourceManager(facesContext)
        .getScripts(viewRoot, name);
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
    int dotIndex = image.lastIndexOf('.');
    String name = image.substring(0, dotIndex);
    String postfix = image.substring(dotIndex);
    return getImageWithPath(facesContext, name + "Disabled" + postfix, true);
  }

  public static String getImageWithPath(FacesContext facesContext, String image, CommandRendererHelper helper) {
    String imageWithPath = null;
    if (helper.isDisabled()) {
      imageWithPath = getDisabledImageWithPath(facesContext, image);
    }
    if (imageWithPath == null) {
      imageWithPath = getImageWithPath(facesContext, image);
    }
    return imageWithPath;
  }
}

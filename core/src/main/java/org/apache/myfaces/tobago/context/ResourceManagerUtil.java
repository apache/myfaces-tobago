/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 30.03.2004 12:47:02.
 * $Id$
 */
package org.apache.myfaces.tobago.context;

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

  public static String getDisabledImageWithPath(FacesContext facesContext, String image) {
    int dotIndex = image.lastIndexOf('.');
    String name = image.substring(0, dotIndex);
    String postfix = image.substring(dotIndex);
    return getImageWithPath(facesContext, name + "Disabled" + postfix, true);
  }
}

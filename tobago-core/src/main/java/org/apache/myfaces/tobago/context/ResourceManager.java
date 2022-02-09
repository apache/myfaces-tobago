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

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

public interface ResourceManager {

  /**
   * @deprecated by API
   */
  @Deprecated
  String getJsp(UIViewRoot viewRoot, String name);

  /**
   * @deprecated by API
   */
  @Deprecated
  String getProperty(UIViewRoot viewRoot, String bundle, String propertyKey);

  String getProperty(FacesContext facesContext, String bundle, String propertyKey);

  /**
   * @deprecated by API
   */
  @Deprecated
  Renderer getRenderer(UIViewRoot viewRoot, String rendererType);

  Renderer getRenderer(FacesContext facesContext, String rendererType);

  /**
   * @deprecated by API
   */
  @Deprecated
  String[] getScripts(UIViewRoot viewRoot, String name);

  String[] getScripts(FacesContext facesContext, String name);

  /**
   * @deprecated by API
   */
  @Deprecated
  String[] getStyles(UIViewRoot viewRoot, String name);

  String[] getStyles(FacesContext facesContext, String name);

  /**
   * @deprecated by API
   */
  @Deprecated
  String getThemeProperty(UIViewRoot viewRoot, String bundle, String propertyKey);

  Measure getThemeMeasure(FacesContext facesContext, Configurable configurable, String name);

  Measure getThemeMeasure(FacesContext facesContext, String rendererType, Markup markup, String name);

  /**
   * @deprecated by API
   */
  @Deprecated
  String getImage(UIViewRoot viewRoot, String name);

  /**
   * @deprecated Please use {@link org.apache.myfaces.tobago.context.ResourceManagerUtils}
   */
  @Deprecated
  String getImage(FacesContext facesContext, String name);

  /**
   * @deprecated by API
   */
  @Deprecated
  String getImage(UIViewRoot viewRoot, String name, boolean ignoreMissing);

  /**
   * @param facesContext the current FacesContext
   * @param nameWithExtension The name with extension
   * @param ignoreMissing if set to false, an error message will be logged, when image is missing
   * @return the full file path
   * @deprecated xxx
   */
  @Deprecated
  String getImage(FacesContext facesContext, String nameWithExtension, boolean ignoreMissing);

  /**
   * @param facesContext the current FacesContext
   * @param name The name without extension
   * @param extension The file extension inclusive dot, like ".png"
   * @param ignoreMissing if set to false, an error message will be logged, when image is missing
   * @return the full file path
   */
  String getImage(
      FacesContext facesContext, String name, String extension, boolean ignoreMissing);

}

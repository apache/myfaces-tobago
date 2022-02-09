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

package org.apache.myfaces.tobago.config;

import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.internal.util.Deprecation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @deprecated xxx
 */
@Deprecated
public class ThemeConfig {

  private ThemeConfig() {
  }

  /**
   * @deprecated since 1.5.0, please use ThemeConfig.getMeasure()
   */
  @Deprecated
  public static int getValue(final FacesContext facesContext, final UIComponent component, final String name) {
    Deprecation.LOG.warn("please use ThemeConfig.getMeasure()");
    return ResourceManagerFactory.getResourceManager(facesContext).getThemeMeasure(
        facesContext, component.getRendererType(), null, name).getPixel();
  }

  /**
   * @deprecated since 1.5.0, please use ThemeConfig.getMeasure()
   */
  @Deprecated
  public static boolean hasValue(final FacesContext facesContext, final UIComponent component, final String name) {
    Deprecation.LOG.warn("please use ThemeConfig.getMeasure()");
    return ResourceManagerFactory.getResourceManager(facesContext).getThemeMeasure(
        facesContext, component.getRendererType(), null, name) != null;
  }
}

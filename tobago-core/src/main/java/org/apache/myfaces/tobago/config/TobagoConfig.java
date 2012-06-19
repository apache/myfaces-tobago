package org.apache.myfaces.tobago.config;

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

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.context.Theme;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.List;

public abstract class TobagoConfig {

  public static final String TOBAGO_CONFIG = "org.apache.myfaces.tobago.config.TobagoConfig";

  public static TobagoConfig getInstance(FacesContext facesContext) {
    return (TobagoConfig) facesContext.getExternalContext().getApplicationMap().get(TOBAGO_CONFIG);
  }

  public static TobagoConfig getInstance(ServletContext servletContext) {
    return (TobagoConfig) servletContext.getAttribute(TOBAGO_CONFIG);
  }

  public abstract Theme getTheme(String name);

  public abstract List<Theme> getSupportedThemes();

/*
  // todo: should this be part of the api?
  public abstract void addResourceDir(String resourceDir);
*/

/*
  // todo: should this be part of the api?
  public abstract List<String> getResourceDirs();
*/

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  public abstract boolean isAjaxEnabled();

  public abstract Theme getDefaultTheme();

/*
  // todo: should this be part of the api?
  public abstract RenderersConfig getRenderersConfig();
*/

  public abstract ProjectStage getProjectStage();

/*
  // todo: should this be part of the api?
  public abstract List<Theme> getThemeDefinitions();
*/

  public abstract boolean isCreateSessionSecret();

  public abstract boolean isCheckSessionSecret();

  public abstract boolean isPreventFrameAttacks();

}

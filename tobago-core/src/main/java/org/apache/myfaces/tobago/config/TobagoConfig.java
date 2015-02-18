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

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.config.ContentSecurityPolicy;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

public abstract class TobagoConfig {

  public static final String TOBAGO_CONFIG = "org.apache.myfaces.tobago.config.TobagoConfig";

  public static TobagoConfig getInstance(final FacesContext facesContext) {
    return (TobagoConfig) facesContext.getExternalContext().getApplicationMap().get(TOBAGO_CONFIG);
  }

  public static TobagoConfig getInstance(final ServletContext servletContext) {
    return (TobagoConfig) servletContext.getAttribute(TOBAGO_CONFIG);
  }

  public abstract Theme getTheme(final String name);

  public abstract List<Theme> getSupportedThemes();

  public abstract Theme getDefaultTheme();

  public abstract ProjectStage getProjectStage();

  public abstract boolean isCreateSessionSecret();

  public abstract boolean isCheckSessionSecret();

  public abstract boolean isPreventFrameAttacks();

  public abstract ContentSecurityPolicy getContentSecurityPolicy();

  public abstract boolean isSetNosniffHeader();

  public abstract Sanitizer getSanitizer();

  public abstract boolean isAutoAccessKeyFromLabel();

  public abstract Map<String, String> getMimeTypes();

  /**
   * For backward compatibility. The default is false.
   */
  public abstract boolean isClassicDateTimePicker();
}

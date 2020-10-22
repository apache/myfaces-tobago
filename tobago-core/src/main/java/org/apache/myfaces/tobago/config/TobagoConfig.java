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

import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.config.ContentSecurityPolicy;
import org.apache.myfaces.tobago.internal.config.SecurityAnnotation;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.CDI;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

public abstract class TobagoConfig {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfig.class);

  /**
   * @deprecated Since 5.0.0. Please use CDI.
   */
  @Deprecated
  public static final String TOBAGO_CONFIG = "org.apache.myfaces.tobago.config.TobagoConfig";

  /**
   * @deprecated Since 5.0.0. Please use CDI.
   */
  @Deprecated
  public static TobagoConfig getInstance(final FacesContext facesContext) {
    TobagoConfig tobagoConfig = null;
    try {
      tobagoConfig = CDI.current().select(TobagoConfig.class).get();
    } catch (Exception e) {
      LOG.warn("No CDI!");
    }
    if (tobagoConfig != null) {
      return tobagoConfig;
    } else {
      // XXX not nice: this happens while unit tests and whenever???
      return (TobagoConfig) facesContext.getExternalContext().getApplicationMap().get(TOBAGO_CONFIG);
    }
  }

  /**
   * @deprecated Since 5.0.0. Please use CDI.
   */
  @Deprecated
  public static TobagoConfig getInstance(final ServletContext servletContext) {
    return CDI.current().select(TobagoConfig.class).get();
  }

  public abstract Theme getTheme(final String name);

  public abstract List<Theme> getSupportedThemes();

  public abstract Theme getDefaultTheme();

  public abstract boolean isCreateSessionSecret();

  public abstract boolean isCheckSessionSecret();

  /**
   * @deprecated But needed to support frame security in IE11. Is replaced by CSP "frame-ancestors".
   */
  @Deprecated
  public abstract boolean isPreventFrameAttacks();

  public abstract ContentSecurityPolicy getContentSecurityPolicy();

  public abstract boolean isSetNosniffHeader();

  public abstract SecurityAnnotation getSecurityAnnotation();

  public abstract Sanitizer getSanitizer();

  public abstract boolean isDecodeLineFeed();

  public abstract Map<String, String> getMimeTypes();
}

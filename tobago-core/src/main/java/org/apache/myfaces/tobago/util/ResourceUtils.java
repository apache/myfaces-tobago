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

package org.apache.myfaces.tobago.util;

import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceUtils {

  public static final String TOBAGO_RESOURCE_BUNDLE = "tobagoResourceBundle";
  private static final String TOBAGO_RESOURCE_BUNDLE_BASE_NAME =
      "org.apache.myfaces.tobago.context.TobagoResourceBundle";

  public static String getString(final String key) {
    return getString(TOBAGO_RESOURCE_BUNDLE, key);
  }

  public static String getString(final FacesContext facesContext, final String key) {
    return getString(facesContext, TOBAGO_RESOURCE_BUNDLE, key);
  }

  public static String getString(final FacesContext facesContext, final Application application, final String key) {
    return getString(facesContext, application, TOBAGO_RESOURCE_BUNDLE, key);
  }

  public static String getString(final String bundle, final String key) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return getString(facesContext, bundle, key);
  }

  public static String getString(final FacesContext facesContext, final String bundle, final String key) {
    return getString(facesContext, facesContext.getApplication(), bundle, key);
  }

  public static String getString(
      final FacesContext facesContext, final Application application, final String bundle, final String key) {
    final ResourceBundle resourceBundle = application.getResourceBundle(facesContext, bundle);

    if (resourceBundle.containsKey(key) || !TOBAGO_RESOURCE_BUNDLE.equals(bundle)) {
      return resourceBundle.getString(key);
    } else {
      final Locale locale = facesContext.getViewRoot().getLocale();
      final ResourceBundle defaultBundle = ResourceBundle.getBundle(
          TOBAGO_RESOURCE_BUNDLE_BASE_NAME, locale, ResourceUtils.class.getClassLoader());
      return defaultBundle.getString(key);
    }
  }
}

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

import java.util.ResourceBundle;

public class ResourceUtils {

  public static final String TOBAGO_RESOURCE_BUNDLE = "tobagoResourceBundle";

  public static String getString(final String key) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    final ResourceBundle resourceBundle = application.getResourceBundle(facesContext, TOBAGO_RESOURCE_BUNDLE);
    return resourceBundle.getString(key);
  }

  public static String getString(final FacesContext facesContext, final String key) {
    return facesContext.getApplication().getResourceBundle(facesContext, TOBAGO_RESOURCE_BUNDLE).getString(key);
  }

  public static String getString(final FacesContext facesContext, final Application application, final String key) {
    return application.getResourceBundle(facesContext, TOBAGO_RESOURCE_BUNDLE).getString(key);
  }

  public static String getString(final String bundle, final String key) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return facesContext.getApplication().getResourceBundle(facesContext, bundle).getString(key);
  }

  public static String getString(final FacesContext facesContext, final String bundle, final String key) {
    return facesContext.getApplication().getResourceBundle(facesContext, bundle).getString(key);
  }

  public static String getString(
      final FacesContext facesContext, final Application application, final String bundle, final String key) {
    return application.getResourceBundle(facesContext, bundle).getString(key);
  }
}

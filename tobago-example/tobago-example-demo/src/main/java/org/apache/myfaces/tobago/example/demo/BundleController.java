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

package org.apache.myfaces.tobago.example.demo;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.apache.myfaces.tobago.util.ResourceUtils.TOBAGO_RESOURCE_BUNDLE;

@Named
@SessionScoped
public class BundleController implements Serializable {

  private List<BundleEntry> resources = new ArrayList<>();
  private List<BundleEntry> messages = new ArrayList<>();

  public void clear(@Observes LocaleChanged event) {
    resources.clear();
    messages.clear();
  }

  public List<BundleEntry> getResources() {
    if (resources.size() == 0) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final ResourceBundle bundle =
          facesContext.getApplication().getResourceBundle(facesContext, TOBAGO_RESOURCE_BUNDLE);
      final Enumeration<String> keys = bundle.getKeys();
      while (keys.hasMoreElements()) {
        final String key = keys.nextElement();
        resources.add(new BundleEntry(key, bundle.getString(key)));
      }
    }
    return resources;
  }

  public List<BundleEntry> getMessages() {
    if (messages.size() == 0) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final Application application = facesContext.getApplication();
      final String bundleName = application.getMessageBundle();
      final Locale locale = facesContext.getViewRoot() != null
          ? facesContext.getViewRoot().getLocale() : application.getDefaultLocale();
      ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
      final Enumeration<String> keys = bundle.getKeys();
      while (keys.hasMoreElements()) {
        final String key = keys.nextElement();
        messages.add(new BundleEntry(key, bundle.getString(key)));
      }
    }
    return messages;
  }

}

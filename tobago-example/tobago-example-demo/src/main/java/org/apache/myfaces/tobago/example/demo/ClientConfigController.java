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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Named
@SessionScoped
public class ClientConfigController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(ClientConfigController.class);

  private Locale locale;

  public ClientConfigController() {

    final FacesContext facesContext = FacesContext.getCurrentInstance();

    // locale

    final UIViewRoot viewRoot = facesContext.getViewRoot();
    if (viewRoot != null) {
      locale = viewRoot.getLocale();
    } else {
      locale = facesContext.getApplication().getDefaultLocale();
    }
  }

  public String submit() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("invoke!!!");
    }
    return null;
  }

// ///////////////////////////////////////////// logic

  public List<SelectItem> getLocaleItems() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    final Locale defaultLocale = application.getDefaultLocale();
    final Iterator supportedLocales = application.getSupportedLocales();

    boolean defaultInList = false;
    final List<SelectItem> localeItems = new ArrayList<>();
    while (supportedLocales.hasNext()) {
      final Locale locale = (Locale) supportedLocales.next();
      localeItems.add(createLocaleItem(locale));
      if (locale.equals(defaultLocale)) {
        defaultInList = true;
      }
    }
    // If the default is already in the list, don't add it.
    // Background: Must the default be in the supported list? Yes or No?
    // This question is not specified explicit and different implemented in the RI and MyFaces
    if (!defaultInList && defaultLocale != null) {
      localeItems.add(0, createLocaleItem(defaultLocale));
    }
    return localeItems;
  }

  private SelectItem createLocaleItem(final Locale localeItem) {
    if (locale != null) {
      return new SelectItem(localeItem, localeItem.getDisplayName(locale));
    } else {
      return new SelectItem(localeItem, localeItem.getDisplayName(localeItem));
    }
  }

  public static ClientConfigController getCurrentInstance(
      final FacesContext facesContext, final String beanName) {
    return (ClientConfigController) facesContext.getApplication()
        .getVariableResolver().resolveVariable(facesContext, beanName);
  }

  public Locale getLocale() {
    return locale;
  }

  public String getLocalizedLocale() {
    if (locale != null) {
      return locale.getDisplayName(locale);
    } else {
      return null;
    }
  }

  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

}

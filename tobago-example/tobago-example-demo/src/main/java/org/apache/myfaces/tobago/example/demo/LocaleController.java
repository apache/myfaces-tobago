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

import org.apache.myfaces.tobago.internal.util.ObjectUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Event;
import jakarta.faces.application.Application;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Named
@SessionScoped
public class LocaleController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private Locale locale;

  @Inject
  private Event<LocaleChanged> events;

  public LocaleController() {

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
      LOG.debug("submit locale");
    }
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    if (viewRoot != null) {
      return viewRoot.getViewId();
    } else {
      facesContext.addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_WARN, "ViewRoot not found!", null));
      return Outcome.CONCEPT_LOCALE.toString();
    }
  }

// ///////////////////////////////////////////// logic

  public List<SelectItem> getLocaleItems() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    final Locale defaultLocale = application.getDefaultLocale();
    final Iterator<Locale> supportedLocales = application.getSupportedLocales();

    boolean defaultInList = false;
    final List<SelectItem> localeItems = new ArrayList<>();
    while (supportedLocales.hasNext()) {
      final Locale supported = (Locale) supportedLocales.next();
      localeItems.add(createLocaleItem(supported));
      if (supported.equals(defaultLocale)) {
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

  public String getImageCountryName() {
    final String language = locale.getLanguage();
    final String country = locale.getCountry();
    final String suffix
        = StringUtils.isNotBlank(language) && StringUtils.isNotBlank(country) ? "_" + language + "_" + country : "";
    String url = "/image/country" + suffix + ".png";
    final ServletContext servletContext =
        (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    URL resource = null;
    try {
      resource = servletContext.getResource(url);
    } catch (final MalformedURLException e) {
      // ignore
    }
    if (resource == null) {
      url = "/image/country.png";
    }
    return url;
  }

  public void setLocale(final Locale locale) {
    if (!ObjectUtils.equals(this.locale, locale)) {
      events.fire(new LocaleChanged());
    }
    this.locale = locale;
  }

}

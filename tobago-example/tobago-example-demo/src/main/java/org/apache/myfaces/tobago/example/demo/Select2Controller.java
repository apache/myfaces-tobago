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

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@SessionScoped
@Named
public class Select2Controller implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(Select2Controller.class);

  private LocaleConverter localeConverter = new LocaleConverter();

  private NumberConverter numberConverter = new NumberConverter();

  private String one2Value;

  private String one3Value;

  private Locale one4Locale;

  private List<Locale> many8Locales;

  private List<Integer> many9Numbers;

  private List<String> many7Countries;

  private List<SelectItem> items;

  public Select2Controller() {
    items = new ArrayList<SelectItem>();
    items.add(new SelectItem("letter", "Letter"));
    items.add(new SelectItem("phone", "Phone"));
    items.add(new SelectItem("eMail", "eMail"));
    items.add(new SelectItem("fax", "Fax"));
  }

  public List<SelectItem> getItems() {
    return items;
  }

  public List<SelectItem> getLocaleItems() {
    return items;
  }

  public Converter getLocaleConverter() {
    return localeConverter;
  }

  public List<AutoSuggestItem> suggestLocale(final UIInput input) {
    return localeConverter.getSuggestLocale((String) input.getSubmittedValue());
  }

  public String getOne2Value() {
    return one2Value;
  }

  public void setOne2Value(String one2Value) {
    this.one2Value = one2Value;
  }

  public String getOne3Value() {
    return one3Value;
  }

  public void setOne3Value(String one3Value) {
    this.one3Value = one3Value;
  }

  public Locale getOne4Locale() {
    LOG.warn("get one4Locale = \"{}\"", one4Locale);
    return one4Locale;
  }

  public void setOne4Locale(Locale one4Locale) {
    LOG.warn("set one4Locale = \"{}\"", one4Locale);
    this.one4Locale = one4Locale;
  }

  public SelectItem[] getOne4LocaleItem() {
    if (one4Locale != null) {
      Locale displayLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
      return new SelectItem[] {new SelectItem(one4Locale, one4Locale.getDisplayName(displayLocale))};
    } else {
      return new SelectItem[0];
    }
  }


  public List<String> getMany7Countries() {
    return many7Countries;
  }

  public void setMany7Countries(List<String> many7Countries) {
    this.many7Countries = many7Countries;
  }

  public List<SelectItem> getMany7CountryItems() {
    if (many7Countries != null && !many7Countries.isEmpty()) {
      List<SelectItem> selectItems = new ArrayList<SelectItem>();
      for (String locale : many7Countries) {
        selectItems.add(new SelectItem(locale));
      }
      return selectItems;
    } else {
      return Collections.emptyList();
    }
  }

  public List<Locale> getMany8Locales() {
    LOG.warn("get many8Locales = \"{}\"", many8Locales);
    return many8Locales;
  }

  public void setMany8Locales(List<Locale> many8Locales) {
    LOG.warn("set many8Locales = \"{}\"", many8Locales);
    this.many8Locales = many8Locales;
  }

  public List<SelectItem> getMany8LocaleItems() {
    if (many8Locales != null && !many8Locales.isEmpty()) {
      Locale displayLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
      List<SelectItem> selectItems = new ArrayList<SelectItem>();
      for (Locale locale : many8Locales) {
        selectItems.add(new SelectItem(locale, locale.getDisplayName(displayLocale)));
      }
      return selectItems;
    } else {
      return Collections.emptyList();
    }
  }

  public List<Integer> getMany9Numbers() {
    LOG.warn("get many9Numbers = \"{}\"", many9Numbers);
    return many9Numbers;
  }

  public void setMany9Numbers(List<Integer> many9Numbers) {
    LOG.warn("set many9Numbers = \"{}\"", many9Numbers);
    this.many9Numbers = many9Numbers;
  }

  public List<SelectItem> getMany9NumbersItems() {
    if (many9Numbers != null && !many9Numbers.isEmpty()) {
      List<SelectItem> selectItems = new ArrayList<SelectItem>();
      for (Integer number : many9Numbers) {
        selectItems.add(new SelectItem(number, Integer.toString(number)));
      }
      return selectItems;
    } else {
      return Collections.emptyList();
    }
  }

  public NumberConverter getNumberConverter() {
    return numberConverter;
  }

  private class NumberConverter implements Converter {

    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException e) {
        throw new ConverterException(e.getMessage(), e);
      }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
      try {
        return Integer.toString((Integer) value);
      } catch (Exception e) {
        throw new ConverterException(e.getMessage(), e);
      }
    }
  }

  private class LocaleConverter implements Converter {

    private Map<String, Locale> localeMap;

    private LocaleConverter() {
      localeMap = new HashMap<String, Locale>();
      for (final Locale locale : Locale.getAvailableLocales()) {
        localeMap.put(locale.toString(), locale);
      }
    }

    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value)
        throws ConverterException {
      if (value != null) {
        Locale locale = localeMap.get(value);
        if (locale != null) {
          return locale;
        } else {
          Locale displayLocale = facesContext.getViewRoot().getLocale();
          for (Locale mapLocale : localeMap.values()) {
            if (mapLocale.getDisplayName(displayLocale).equals(value)) {
              return mapLocale;
            }
          }
          throw new ConverterException("Could not convert \"" + value + "\" to Locale");
        }
      } else {
        return null;
      }
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) throws ConverterException {
      if (o != null) {
        return ((Locale) o).toString();
      } else {
        return null;
      }
    }

    public List<AutoSuggestItem> getSuggestLocale(String prefix) {
      LOG.info("Creating items for prefix: '" + prefix + "'");
      Locale displayLocale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
      final List<AutoSuggestItem> result = new ArrayList<AutoSuggestItem>();
      for (final Locale locale : localeMap.values()) {
        if (StringUtils.startsWithIgnoreCase(locale.getDisplayName(displayLocale), prefix)) {
          AutoSuggestItem suggestItem = new AutoSuggestItem();
          suggestItem.setValue(locale.toString());
          suggestItem.setLabel(locale.getDisplayName(displayLocale));
          result.add(suggestItem);
        }
        if (result.size() > 100) { // this value should be greater than the value of the input control
          break;
        }
      }
      return result;
    }


  }
}

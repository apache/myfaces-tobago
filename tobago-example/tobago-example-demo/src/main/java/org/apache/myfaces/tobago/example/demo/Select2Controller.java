package org.apache.myfaces.tobago.example.demo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.SelectItem;

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

  private String one2Value;

  private String one3Value;

  private Locale one4Locale;

  private List<Locale> many8Locales;

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
      List<SelectItem> items = new ArrayList<SelectItem>();
      for (String locale : many7Countries) {
        items.add(new SelectItem(locale));
      }
      return items;
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
      List<SelectItem> items = new ArrayList<SelectItem>();
      for (Locale locale : many8Locales) {
        items.add(new SelectItem(locale, locale.getDisplayName(displayLocale)));
      }
      return items;
    } else {
      return Collections.emptyList();
    }
  }

  private class LocaleConverter implements Converter {

    private Map<String, Locale> localeMap;

    public LocaleConverter() {
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

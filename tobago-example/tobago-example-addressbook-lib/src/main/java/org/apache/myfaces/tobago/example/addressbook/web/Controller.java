package org.apache.myfaces.tobago.example.addressbook.web;

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

/*
 * Created 29.11.2004 17:36:20.
 * $Id$
 */

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.example.addressbook.Address;
import org.apache.myfaces.tobago.example.addressbook.AddressDao;
import org.apache.myfaces.tobago.example.addressbook.Picture;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Component("controller")
@Scope("session")
public class Controller {

  private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

  private static final String OUTCOME_LIST = "list";
  private static final String OUTCOME_EDITOR = "editor";

  private List<Address> currentAddressList;
  private Address currentAddress;
  private SheetState selectedAddresses;

  private String searchCriterion;

  private Locale language;

  private List<SelectItem> languages = new ArrayList<SelectItem>();

  private Countries countries;

  private Theme theme;

  private List<SelectItem> themeItems = new ArrayList<SelectItem>();

  private boolean simple;

  private boolean renderPopup;
  private boolean renderFirstName = true;
  private boolean renderLastName = true;
  private boolean renderDayOfBirth = true;

  @Resource(name = "addressDao")
  private AddressDao addressDao;

  private FileItem uploadedFile;
  private boolean renderFileUploadPopup;

  @PostConstruct
  public void init() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();
    language = application.getDefaultLocale();
    countries.init(language);
    facesContext.getExternalContext().getSession(true);
    initLanguages();

    TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);
    List<Theme> themes = new ArrayList<Theme>(tobagoConfig.getSupportedThemes());
    themes.add(0, tobagoConfig.getDefaultTheme());
    themeItems = new ArrayList<SelectItem>();
    for (Theme theme : themes) {
      themeItems.add(new SelectItem(theme, theme.getDisplayName()));
    }

    ClientProperties client = VariableResolverUtils.resolveClientProperties(facesContext);
    theme = client.getTheme();
    currentAddressList = addressDao.findAddresses(searchCriterion);
  }

  public void setAddressDao(AddressDao addressDao) {
    this.addressDao = addressDao;
  }

  public void sheetSorter(ActionEvent event) {
    if (event instanceof SortActionEvent) {
      SortActionEvent sortEvent = (SortActionEvent) event;
      UIColumn column = (UIColumn) sortEvent.getColumn();

      SheetState sheetState = ((UISheet) sortEvent.getSheet()).getSheetState(FacesContext.getCurrentInstance());
      currentAddressList = addressDao.findAddresses(searchCriterion, column.getId(), sheetState.isAscending());
    }
  }

  public String search() {
    currentAddressList = addressDao.findAddresses(searchCriterion);
    return OUTCOME_LIST;
  }

  public String createAddress() {
    LOG.debug("action: createAddress");
    currentAddress = new Address();
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Locale locale = facesContext.getViewRoot().getLocale();
    // XXX use better datatype for countries than Locale
    if (Locale.GERMAN.equals(locale)) {
      locale = Locale.GERMANY;
    }
    currentAddress.setCountry(locale);
    return OUTCOME_EDITOR;
  }

  public String addDummyAddresses() throws IOException {
    for (int i=0; i<100; ++i) {
      currentAddress = RandomAddressGenerator.generateAddress();
      store();
    }
    return OUTCOME_LIST;
  }

  public String editAddress() {
    LOG.debug("action: editAddress");
    List<Integer> selection = selectedAddresses.getSelectedRows();
    if (selection.size() != 1) {
      FacesMessage error = new FacesMessage("Please select exactly one address.");
      FacesContext.getCurrentInstance().addMessage(null, error);
      return null;
    }
    currentAddress = currentAddressList.get(selection.get(0));
    return OUTCOME_EDITOR;
  }

  public String deleteAddresses() {
    List<Integer> selection = selectedAddresses.getSelectedRows();
    if (selection.size() < 1) {
      FacesMessage error = new FacesMessage("Please select at least one address.");
      FacesContext.getCurrentInstance().addMessage(null, error);
      return null;
    }
    Collections.sort(selection); // why?
    for (int i = selection.size() - 1; i >= 0; i--) {
      Address address = currentAddressList.get(selection.get(i));
      addressDao.removeAddress(address);
    }
    selectedAddresses.resetSelected();
    currentAddressList = addressDao.findAddresses(searchCriterion);
    return OUTCOME_LIST;
  }

  public String store() {
    LOG.debug("action: storeAddress");
    currentAddress = addressDao.updateAddress(currentAddress);
    selectedAddresses.resetSelected();
    currentAddressList = addressDao.findAddresses(searchCriterion);
    return OUTCOME_LIST;
  }

  public String cancel() {
    currentAddressList = addressDao.findAddresses(searchCriterion);
    return OUTCOME_LIST;
  }

  public String languageChangedList() {
    initLanguages();
    return OUTCOME_LIST;
  }

  public String themeChanged() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ClientProperties client = VariableResolverUtils.resolveClientProperties(facesContext);
    client.setTheme(theme);
    return null;
  }

  public String getCurrentAddressPictureUrl() {
     return (currentAddress != null && currentAddress.getPicture() != null)
         ? FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/picture?id=XXXX"
         :"image/empty-portrait.png";

   }
  
  public void validatePhoneNumber(
      FacesContext context, UIComponent component, Object value) {
    String phoneNumber = (String) value;
    if (phoneNumber == null || phoneNumber.length() == 0) {
      return;
    }
    if (!phoneNumber.matches("\\+?[0-9 ]*(\\([0-9 ]*\\))?[0-9 ]*")) {
      throw new ValidatorException(MessageUtils.createErrorMessage(
          "validatorPhone", context));
    }
  }

  public List getCurrentAddressList() {
    return currentAddressList;
  }

  public Address getCurrentAddress() {
    return currentAddress;
  }

  public SheetState getSelectedAddresses() {
    return selectedAddresses;
  }

  public void setSelectedAddresses(SheetState selectedAddresses) {
    this.selectedAddresses = selectedAddresses;
  }

  public boolean isRenderFileUploadPopup() {
    return renderFileUploadPopup;
  }

  public void setRenderFileUploadPopup(boolean renderFileUploadPopup) {
    LOG.debug(">>> " + renderFileUploadPopup);
    this.renderFileUploadPopup = renderFileUploadPopup;
  }

  public String cancelFileUploadPopup() {
    setRenderFileUploadPopup(false);
    return null;
  }

  public void setRenderPopup(boolean renderPopup) {
    this.renderPopup = renderPopup;
  }

  public boolean isRenderPopup() {
    return renderPopup;
  }

  public String selectColumns() {
    setRenderPopup(true);
    return OUTCOME_LIST;
  }

  public String okFileUpload() {
    setRenderFileUploadPopup(false);
    Picture picture = new Picture(uploadedFile.getContentType(), uploadedFile.get());
    currentAddress.setPicture(picture);
    return null;
  }

  public String cancelFileUpload() {
    setRenderFileUploadPopup(false);
    return null;
  }

  public String cancelPopup() {
    setRenderPopup(false);
    return OUTCOME_LIST;
  }

  public String logout() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return "logout";
  }

  public String getVersion() {
    return getClass().getPackage().getImplementationVersion();
  }

  public Locale getLanguage() {
    return language;
  }

  public String getDisplayLanguage() {
    return language.getDisplayName(language);
  }

  public void setLanguage(Locale language) {
    this.language = language;
  }

  public List<SelectItem> getLanguages() {
    return languages;
  }

  public void setLanguages(List<SelectItem> languages) {
    this.languages = languages;
  }

  public Countries getCountries() {
    return countries;
  }

  @Resource(name = "countries")
  public void setCountries(Countries countries) {
    this.countries = countries;
  }

  public List<SelectItem> getThemeItems() {
    return themeItems;
  }

  public Theme getTheme() {
    return theme;
  }

  public void setTheme(Theme theme) {
    this.theme = theme;
  }

  public boolean isSimple() {
    return simple;
  }

  public void setSimple(boolean simple) {
    this.simple = simple;
  }

  public boolean isRenderFirstName() {
    return renderFirstName;
  }

  public void setRenderFirstName(boolean renderFirstName) {
    this.renderFirstName = renderFirstName;
  }

  public boolean isRenderLastName() {
    return renderLastName;
  }

  public void setRenderLastName(boolean renderLastName) {
    this.renderLastName = renderLastName;
  }

  public boolean isRenderDayOfBirth() {
    return renderDayOfBirth;
  }

  public void setRenderDayOfBirth(boolean renderDayOfBirth) {
    this.renderDayOfBirth = renderDayOfBirth;
  }

  public FileItem getUploadedFile() {
    return uploadedFile;
  }

  public void setUploadedFile(FileItem uploadedFile) {
    this.uploadedFile = uploadedFile;
  }

  public String getSearchCriterion() {
    return searchCriterion;
  }

  public void setSearchCriterion(String searchCriterion) {
    this.searchCriterion = searchCriterion;
  }

  public String popupFileUpload() {
    setRenderFileUploadPopup(true);
    return null;
  }

  public String languageChanged() {
    countries.init(language);
    initLanguages();
/*
    // reinit date converter
    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIViewRoot viewRoot = facesContext.getViewRoot();
    EditableValueHolder component = (EditableValueHolder)
        viewRoot.findComponent(":page:dayOfBirth");
    if (component != null) {
      DateTimeConverter converter = (DateTimeConverter) component.getConverter();
      converter.setPattern(MessageUtils.getLocalizedString(facesContext, "editor_date_pattern"));
    }
*/
    return null;
  }

  private void initLanguages() {
    languages.clear();
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Application application = facesContext.getApplication();
    Iterator supportedLocales = application.getSupportedLocales();
    while (supportedLocales.hasNext()) {
      Locale locale = (Locale) supportedLocales.next();
      SelectItem item = new SelectItem(locale, locale.getDisplayName(language));
      languages.add(item);
    }
    Collections.sort(languages, new SelectItemComparator());
  }

}

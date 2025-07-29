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

package org.apache.myfaces.tobago.example.demo.overview;

import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.example.data.LocaleList;
import org.apache.myfaces.tobago.example.data.Salutation;
import org.apache.myfaces.tobago.model.SelectItem;
import org.apache.myfaces.tobago.model.Selectable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

@WindowScoped
@Named(value = "overviewController")
public class OverviewController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(OverviewController.class);

  private static final String[] TREE_SELECT_MODE_KEYS = {
      Selectable.NONE.getValue(),
      Selectable.SINGLE.getValue(),
      Selectable.SINGLE_LEAF_ONLY.getValue(),
      Selectable.MULTI.getValue(),
      Selectable.MULTI_LEAF_ONLY.getValue(),
      Selectable.MULTI_CASCADE.getValue()
  };

  private static final String[] TREELISTBOX_SELECT_MODE_KEYS = {
      Selectable.SINGLE.getValue(),
      Selectable.SINGLE_LEAF_ONLY.getValue(),
      Selectable.SIBLING_LEAF_ONLY.getValue()
  };

  private String radioValue;
  private Currency[] currencyItems;

  private Salutation singleValue;

  private Salutation[] multiValue;

/*
  @Required
*/
  private String basicInput;

  private String suggestInput;

  private String placeholder;

  private String basicArea = "";

  private Date basicDate = new Date();

  private Date basicTime = new Date();

  private String treeSelectMode;

  private String treeListboxSelectMode;

  private Integer treeTabsState;

  private String lastAction;

  private SheetConfig sheetConfig;

  private String toolbarIconSize;

  private SelectItem[] toolbarIconItems;

  private String toolbarTextPosition;

  private SelectItem[] toolbarTextItems;


  public OverviewController() {
    radioValue = "JPY";
    currencyItems = new Currency[]{
        Currency.getInstance("JPY"),
        Currency.getInstance("TTD"),
        Currency.getInstance("USD"),
        Currency.getInstance("EUR")
    };
    singleValue = Salutation.UNKNOWN;
    treeSelectMode = TREE_SELECT_MODE_KEYS[3];
    treeListboxSelectMode = TREELISTBOX_SELECT_MODE_KEYS[0];
    multiValue = new Salutation[0];
    treeTabsState = 0;
    sheetConfig = new SheetConfig();
    final String[] toolbarIconKeys
        = {UIToolBar.ICON_OFF, UIToolBar.ICON_SMALL, UIToolBar.ICON_BIG};
    toolbarIconItems = new SelectItem[toolbarIconKeys.length];
    for (int i = 0; i < toolbarIconKeys.length; i++) {
      toolbarIconItems[i] = new SelectItem(toolbarIconKeys[i], toolbarIconKeys[i]);
    }
    toolbarIconSize = UIToolBar.ICON_SMALL;

    final String[] toolbarTextKeys =
        {UIToolBar.LABEL_OFF, UIToolBar.LABEL_BOTTOM, UIToolBar.LABEL_RIGHT};
    toolbarTextItems = new SelectItem[toolbarTextKeys.length];
    for (int i = 0; i < toolbarTextKeys.length; i++) {
      toolbarTextItems[i] = new SelectItem(toolbarTextKeys[i], toolbarTextKeys[i]);
    }
    toolbarTextPosition = UIToolBar.LABEL_BOTTOM;
  }

  private static SelectItem[] getSalutationSelectItems(final String bundle) {
    final Salutation[] salutations = Salutation.values();
    final SelectItem[] items = new SelectItem[salutations.length];
    for (int i = 0; i < items.length; i++) {
      String label = ResourceManagerUtils.getProperty(
          FacesContext.getCurrentInstance(), bundle, salutations[i].getKey());
      if (LOG.isTraceEnabled()) {
        LOG.trace("label = " + label + "");
      }
      if (label == null) {
        label = salutations[i].getKey();
      }
      items[i] = new SelectItem(salutations[i], label);
    }
    return items;
  }

  private static SelectItem[] getSelectItems(final String[] keys, final String bundle) {
    final SelectItem[] items = new SelectItem[keys.length];
    for (int i = 0; i < items.length; i++) {
      String label = ResourceManagerUtils.getProperty(
          FacesContext.getCurrentInstance(), bundle, keys[i]);
      if (LOG.isTraceEnabled()) {
        LOG.trace("label = " + label + "");
      }
      if (label == null) {
        label = keys[i];
      }
      items[i] = new SelectItem(keys[i], label);
    }
    return items;
  }

  public void click(final ActionEvent actionEvent) {
    LOG.info("click the action listener");
    lastAction = actionEvent.getComponent().getId();
  }

  public void resetColumnWidths(final ActionEvent event) {
    final UISheet sheet = (UISheet) event.getComponent().findComponent("sheet");
    if (sheet != null) {
      sheet.resetColumnWidths();
    } else {
      LOG.warn("Didn't find sheet component!");
    }
  }

  public String ping() {
    LOG.debug("ping invoked");
    return null;
  }

  public void customValidator(final FacesContext context, final UIComponent component, final Object value)
      throws ValidatorException {
    if (value == null) {
      return;
    }
    if (!"tobago".equalsIgnoreCase(value.toString())) {
      throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please type in 'Tobago'",
          "Please type in 'Tobago'"));
    }
  }

  public boolean getShowPopup() {
    return "popupButton".equals(lastAction) || "popupButton2".equals(lastAction);
  }

  public Currency[] getCurrencyItems() {
    return currencyItems;
  }

  public SelectItem[] getItems() {
    return getSalutationSelectItems("overview");
  }

  public SelectItem[] getItems2() {
    return getSelectItems(TREE_SELECT_MODE_KEYS, "overview");

  }


  public SelectItem[] getTreeSelectModeItems() {
    return getSelectItems(TREE_SELECT_MODE_KEYS, "overview");

  }

  public SelectItem[] getTreeListboxSelectModeItems() {
    return getSelectItems(TREELISTBOX_SELECT_MODE_KEYS, "overview");

  }

  public String getRadioValue() {
    return radioValue;
  }

  public void setRadioValue(final String radioValue) {
    this.radioValue = radioValue;
  }

  public Salutation getSingleValue() {
    return singleValue;
  }

  public void setSingleValue(final Salutation singleValue) {
    this.singleValue = singleValue;
  }

  public Salutation[] getMultiValue() {
    return multiValue;
  }

  public void setMultiValue(final Salutation[] multiValue) {
    this.multiValue = multiValue;
  }

  public Date getBasicDate() {
    return basicDate;
  }

  public void setBasicDate(final Date basicDate) {
    this.basicDate = basicDate;
  }

  public Date getBasicTime() {
    return basicTime;
  }

  public void setBasicTime(final Date basicTime) {
    this.basicTime = basicTime;
  }

  public String getTreeSelectMode() {
    return treeSelectMode;
  }

  public void setTreeSelectMode(final String treeSelectMode) {
    this.treeSelectMode = treeSelectMode;
  }

  public String getTreeListboxSelectMode() {
    return treeListboxSelectMode;
  }

  public void setTreeListboxSelectMode(final String treeListboxSelectMode) {
    this.treeListboxSelectMode = treeListboxSelectMode;
  }

  public String getBasicInput() {
    return basicInput;
  }

  public void setBasicInput(final String basicInput) {
    this.basicInput = basicInput;
  }

  public String getSuggestInput() {
    return suggestInput;
  }

  public void setSuggestInput(final String suggestInput) {
    this.suggestInput = suggestInput;
  }

  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(final String placeholder) {
    this.placeholder = placeholder;
  }

  public String getBasicArea() {
    return basicArea;
  }

  public void setBasicArea(final String basicArea) {
    this.basicArea = basicArea;
  }

  public String getLastAction() {
    return lastAction;
  }

  public Integer getTreeTabsState() {
    return treeTabsState;
  }

  public void setTreeTabsState(final Integer treeTabsState) {
    this.treeTabsState = treeTabsState;
  }

  public SheetConfig getSheetConfig() {
    return sheetConfig;
  }

  public void setSheetConfig(final SheetConfig sheetConfig) {
    this.sheetConfig = sheetConfig;
  }

    public String getToolbarIconSize() {
        return toolbarIconSize;
    }

    public void setToolbarIconSize(final String toolbarIconSize) {
        this.toolbarIconSize = toolbarIconSize;
    }

    public SelectItem[] getToolbarIconItems() {
        return toolbarIconItems;
    }

    public void setToolbarIconItems(final SelectItem[] toolbarIconItems) {
        this.toolbarIconItems = toolbarIconItems;
    }

    public String getToolbarTextPosition() {
        return toolbarTextPosition;
    }

    public void setToolbarTextPosition(final String toolbarTextPosition) {
        this.toolbarTextPosition = toolbarTextPosition;
    }

    public SelectItem[] getToolbarTextItems() {
        return toolbarTextItems;
    }

    public void setToolbarTextItems(final SelectItem[] toolbarTextItems) {
        this.toolbarTextItems = toolbarTextItems;
    }

  public List<String> getInputSuggestItems(final UIInput component) {
    final String prefix = (String) component.getSubmittedValue();
    LOG.info("Creating items for prefix: '" + prefix + "'");
    final List<String> result = new ArrayList<String>();
    for (final String name : LocaleList.COUNTRY_LANGUAGE) {
      if (StringUtils.startsWithIgnoreCase(name, prefix)) {
        result.add(name);
      }
      if (result.size() > 100) { // this value should be greater than the value of the input control
        break;
      }
    }
    return result;
  }

  public String noop() {
    LOG.info("noop");
    return null;
  }
}

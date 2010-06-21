package org.apache.myfaces.tobago.example.demo.overview;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.example.demo.model.Salutation;
import org.apache.myfaces.tobago.example.demo.model.solar.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/*
 * Created 19.05.2004 18:47:47.
 * $Id$
 */

public class OverviewController {

  private static final Log LOG = LogFactory.getLog(OverviewController.class);

  private static final String[] TREE_SELECT_MODE_KEYS = {
      "none",
      "single",
      "singleLeafOnly",
      "multi",
      "multiLeafOnly"
  };

  private static final String[] TREELISTBOX_SELECT_MODE_KEYS = {
      "single",
      "singleLeafOnly",
      "siblingLeafOnly"
  };

  private Salutation radioValue;

  private Salutation singleValue;

  private Salutation[] multiValue;

  private String basicInput = "";

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
    radioValue = Salutation.UNKNOWN;
    singleValue = Salutation.UNKNOWN;
    treeSelectMode = TREE_SELECT_MODE_KEYS[0];
    treeListboxSelectMode = TREELISTBOX_SELECT_MODE_KEYS[0];
    multiValue = new Salutation[0];
    treeTabsState = 0;
    sheetConfig = new SheetConfig();
    String[] toolbarIconKeys
        = {ToolBarTag.ICON_OFF, ToolBarTag.ICON_SMALL, ToolBarTag.ICON_BIG};
    toolbarIconItems = new SelectItem[toolbarIconKeys.length];
    for (int i = 0; i < toolbarIconKeys.length; i++) {
      toolbarIconItems[i] = new SelectItem(toolbarIconKeys[i], toolbarIconKeys[i]);
    }
    toolbarIconSize = ToolBarTag.ICON_SMALL;

    String[] toolbarTextKeys =
        {ToolBarTag.LABEL_OFF, ToolBarTag.LABEL_BOTTOM, ToolBarTag.LABEL_RIGHT};
    toolbarTextItems = new SelectItem[toolbarTextKeys.length];
    for (int i = 0; i < toolbarTextKeys.length; i++) {
      toolbarTextItems[i] = new SelectItem(toolbarTextKeys[i], toolbarTextKeys[i]);
    }
    toolbarTextPosition = ToolBarTag.LABEL_BOTTOM;
  }

  private static SelectItem[] getSalutationSelectItems(ResourceManager resourceManager, String resource) {
    Salutation[] salutations = Salutation.values();
    SelectItem[] items = new SelectItem[salutations.length];
    for (int i = 0; i < items.length; i++) {
      String label = resourceManager.getProperty(
          FacesContext.getCurrentInstance().getViewRoot(), resource, salutations[i].getKey());
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

  private static SelectItem[] getSelectItems(
      String[] keys, ResourceManager resourceManager, String resource) {
    SelectItem[] items = new SelectItem[keys.length];
    for (int i = 0; i < items.length; i++) {
      String label = resourceManager.getProperty(
          FacesContext.getCurrentInstance().getViewRoot(), resource, keys[i]);
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

  public void click(ActionEvent actionEvent) {
    lastAction = actionEvent.getComponent().getId();
  }

  public void resetColumnWidths(ActionEvent event) {
    final UIData sheet = (UIData) event.getComponent().findComponent("sheet");
    if (sheet != null) {
      sheet.resetColumnWidths();
    } else {
      LOG.warn("Didn't find sheet component!");
    }
  }

  public void sheetSorter(ActionEvent event) {
    if (event instanceof SortActionEvent) {
      SortActionEvent sortEvent = (SortActionEvent) event;
      UIData sheet = sortEvent.getSheet();
      SheetState sheetState
          = sheet.getSheetState(FacesContext.getCurrentInstance());
      String columnId = sheetState.getSortedColumnId();
      List<SolarObject> list = (List<SolarObject>) sheet.getValue();
      SolarObject sun = list.remove(0);

      Comparator<SolarObject> comparator = null;

      if ("name".equals(columnId)) {
        comparator = new Comparator<SolarObject>() {
          public int compare(SolarObject o1, SolarObject o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
          }
        };
      } else if ("orbit".equals(columnId)) {
        comparator = new Comparator<SolarObject>() {
          public int compare(SolarObject o1, SolarObject o2) {
            return o1.getOrbit().compareToIgnoreCase(o2.getOrbit());
          }
        };
      } else if ("population".equals(columnId)) {
        comparator = new Comparator<SolarObject>() {
          public int compare(SolarObject o1, SolarObject o2) {
            Integer i1 = -1;
            try {
              i1 = new Integer(o1.getPopulation().replaceAll("\\D", "").trim());
            } catch (NumberFormatException e) {
              // ignore
            }
            Integer i2 = -1;
            try {
              i2 = new Integer(o2.getPopulation().replaceAll("\\D", "").trim());
            } catch (NumberFormatException e) {
              // ignore
            }
            return i1.compareTo(i2);
          }
        };
      } else if ("distance".equals(columnId)) {
        comparator = new Comparator<SolarObject>() {
          public int compare(SolarObject o1, SolarObject o2) {
            return o1.getDistance().compareTo(o2.getDistance());
          }
        };
      } else if ("period".equals(columnId)) {
        comparator = new Comparator<SolarObject>() {
          public int compare(SolarObject o1, SolarObject o2) {
            return o1.getPeriod().compareTo(o2.getPeriod());
          }
        };
      }

      Collections.sort(list, comparator);
      if (!sheetState.isAscending()) {
        Collections.reverse(list);
      }

      list.add(0, sun);
    }
  }

  public String ping() {
    LOG.debug("ping invoked");
    return null;
  }

  public void customValidator(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    if (value == null) {
      return;
    }
    if (!"tobago".equalsIgnoreCase(value.toString())) {
      throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please type in 'Tobago'",
          "Please type in 'Tobago'"));
    }
  }

  public void processTabChange(TabChangeEvent tabChangeEvent) {
    int oldIndex = tabChangeEvent.getOldTabIndex();
    int newIndex = tabChangeEvent.getNewTabIndex();
    LOG.error("Tab index changed from Tab " + oldIndex + " to Tab " + newIndex);
  }

  public boolean getShowPopup() {
    return "popupButton".equals(lastAction) || "popupButton2".equals(lastAction);
  }

  public SelectItem[] getItems() {
    ResourceManager resourceManager = ResourceManagerFactory
        .getResourceManager(FacesContext.getCurrentInstance());
    return getSalutationSelectItems(resourceManager, "overview");
  }

  public SelectItem[] getItems2() {
    ResourceManager resourceManager = ResourceManagerFactory
        .getResourceManager(FacesContext.getCurrentInstance());
    return getSelectItems(TREE_SELECT_MODE_KEYS, resourceManager, "overview");

  }


  public SelectItem[] getTreeSelectModeItems() {
    ResourceManager resourceManager = ResourceManagerFactory
        .getResourceManager(FacesContext.getCurrentInstance());
    return getSelectItems(TREE_SELECT_MODE_KEYS, resourceManager, "overview");

  }

  public SelectItem[] getTreeListboxSelectModeItems() {
    ResourceManager resourceManager = ResourceManagerFactory
        .getResourceManager(FacesContext.getCurrentInstance());
    return getSelectItems(TREELISTBOX_SELECT_MODE_KEYS, resourceManager, "overview");

  }

  public Salutation getRadioValue() {
    return radioValue;
  }

  public void setRadioValue(Salutation radioValue) {
    this.radioValue = radioValue;
  }

  public Salutation getSingleValue() {
    return singleValue;
  }

  public void setSingleValue(Salutation singleValue) {
    this.singleValue = singleValue;
  }

  public Salutation[] getMultiValue() {
    return multiValue;
  }

  public void setMultiValue(Salutation[] multiValue) {
    this.multiValue = multiValue;
  }

  public Date getBasicDate() {
    return basicDate;
  }

  public void setBasicDate(Date basicDate) {
    this.basicDate = basicDate;
  }

  public Date getBasicTime() {
    return basicTime;
  }

  public void setBasicTime(Date basicTime) {
    this.basicTime = basicTime;
  }

  public String getTreeSelectMode() {
    return treeSelectMode;
  }

  public void setTreeSelectMode(String treeSelectMode) {
    this.treeSelectMode = treeSelectMode;
  }

  public String getTreeListboxSelectMode() {
    return treeListboxSelectMode;
  }

  public void setTreeListboxSelectMode(String treeListboxSelectMode) {
    this.treeListboxSelectMode = treeListboxSelectMode;
  }

  public String getBasicInput() {
    return basicInput;
  }

  public void setBasicInput(String basicInput) {
    this.basicInput = basicInput;
  }

  public String getBasicArea() {
    return basicArea;
  }

  public void setBasicArea(String basicArea) {
    this.basicArea = basicArea;
  }

  public String getLastAction() {
    return lastAction;
  }

  public Integer getTreeTabsState() {
    return treeTabsState;
  }

  public void setTreeTabsState(Integer treeTabsState) {
    this.treeTabsState = treeTabsState;
  }

  public SheetConfig getSheetConfig() {
    return sheetConfig;
  }

  public void setSheetConfig(SheetConfig sheetConfig) {
    this.sheetConfig = sheetConfig;
  }

  public String getToolbarIconSize() {
    return toolbarIconSize;
  }

  public void setToolbarIconSize(String toolbarIconSize) {
    this.toolbarIconSize = toolbarIconSize;
  }

  public SelectItem[] getToolbarIconItems() {
    return toolbarIconItems;
  }

  public void setToolbarIconItems(SelectItem[] toolbarIconItems) {
    this.toolbarIconItems = toolbarIconItems;
  }

  public String getToolbarTextPosition() {
    return toolbarTextPosition;
  }

  public void setToolbarTextPosition(String toolbarTextPosition) {
    this.toolbarTextPosition = toolbarTextPosition;
  }

  public SelectItem[] getToolbarTextItems() {
    return toolbarTextItems;
  }

  public void setToolbarTextItems(SelectItem[] toolbarTextItems) {
    this.toolbarTextItems = toolbarTextItems;
  }

  public List<String> getInputSuggestItems(String prefix) {
    LOG.info("Creating items for prefix: '" + prefix + "'");
    List<String> li = new ArrayList<String>();
    for (int i = 1; i <= 6; i++) {
      li.add(prefix + i);
    }
    return li;
  }
}

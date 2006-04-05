package org.apache.myfaces.tobago.example.demo.overview;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
 * Created 19.05.2004 18:47:47.
 * $Id: OverviewController.java 1269 2005-08-08 20:20:19 +0200 (Mo, 08 Aug 2005) lofwyr $
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.example.demo.model.solar.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class OverviewController {

  private static final Log LOG = LogFactory.getLog(OverviewController.class);

  private static final String[] ITEM_KEYS = {
    "basic_itemUnknown",
    "basic_itemMr",
    "basic_itemMrs",
  };

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

  private String radioValue;

  private String singleValue;

  private String[] multiValue;

  private String basicInput = "";

  private String basicArea = "";

  private Date basicDate = new Date();

  private Date basicTime = new Date();

  private String treeSelectMode;

  private String treeListboxSelectMode;

  private Integer treeTabsState;

  private String lastAction;

  private SheetConfig sheetConfig;


  public OverviewController() {
    radioValue = ITEM_KEYS[0];
    singleValue = ITEM_KEYS[0];
    treeSelectMode = TREE_SELECT_MODE_KEYS[0];
    treeListboxSelectMode = TREELISTBOX_SELECT_MODE_KEYS[0];
    multiValue = new String[0];
    treeTabsState = 0;
    sheetConfig = new SheetConfig();
  }

  private static SelectItem[] getSelectItems(
      String[] keys, ResourceManager resourceManager, String resource) {
    SelectItem[] items = new SelectItem[keys.length];
    for (int i = 0; i < items.length; i++) {
      String label = resourceManager.getProperty(
          FacesContext.getCurrentInstance().getViewRoot(), resource, keys[i]);
      LOG.info("label = " + label + "");
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
        comparator = new Comparator() {
          public int compare(Object o1, Object o2) {
            String s1 = ((SolarObject) o1).getName();
            String s2 = ((SolarObject) o2).getName();
            return s1.compareToIgnoreCase(s2);
          }
        };
      } else if ("orbit".equals(columnId)) {
        comparator = new Comparator() {
          public int compare(Object o1, Object o2) {
            String s1 = ((SolarObject) o1).getOrbit();
            String s2 = ((SolarObject) o2).getOrbit();
            return s1.compareToIgnoreCase(s2);
          }
        };
      } else if ("population".equals(columnId)) {
        comparator = new Comparator() {
          public int compare(Object o1, Object o2) {
            String s1 = ((SolarObject) o1).getPopulation();
            String s2 = ((SolarObject) o2).getPopulation();
            Integer i1 = -1;
            try {
              i1 = new Integer(s1.replaceAll("\\D", "").trim());
            } catch (NumberFormatException e) { }
            Integer i2 = -1;
            try {
              i2 = new Integer(s2.replaceAll("\\D", "").trim());
            } catch (NumberFormatException e) { }
            return i1.compareTo(i2);
          }
        };
      } else if ("distance".equals(columnId)) {
        comparator = new Comparator() {
          public int compare(Object o1, Object o2) {
            Integer i1 = ((SolarObject) o1).getDistance();
            Integer i2 = ((SolarObject) o2).getDistance();
            return i1.compareTo(i2);
          }
        };
      } else if ("period".equals(columnId)) {
        comparator = new Comparator() {
          public int compare(Object o1, Object o2) {
            Double i1 = ((SolarObject) o1).getPeriod();
            Double i2 = ((SolarObject) o2).getPeriod();
            return i1.compareTo(i2);
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

  public boolean getShowPopup() {
    return "popupButton".equals(lastAction) || "popupButton2".equals(lastAction);
  }

  public SelectItem[] getItems() {
    ResourceManager resourceManager = ResourceManagerFactory
        .getResourceManager(FacesContext.getCurrentInstance());
    return getSelectItems(ITEM_KEYS, resourceManager, "overview");
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

  public String getRadioValue() {
    return radioValue;
  }

  public void setRadioValue(String radioValue) {
    this.radioValue = radioValue;
  }

  public String getSingleValue() {
    return singleValue;
  }

  public void setSingleValue(String singleValue) {
    this.singleValue = singleValue;
  }

  public String[] getMultiValue() {
    return multiValue;
  }

  public void setMultiValue(String[] multiValue) {
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

  public List<String> getInputSuggestItems(String prefix) {
    LOG.info("createing items for prefix :\"" + prefix + "\"");
    List<String> li = new ArrayList<String>();
    li.add(prefix+1);
    li.add(prefix+2);
    li.add(prefix+3);
    li.add(prefix+4);
    li.add(prefix+5);
    li.add(prefix+6);
    return li;
  }
}

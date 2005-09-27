/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 19.05.2004 18:47:47.
 * $Id: OverviewController.java 1269 2005-08-08 20:20:19 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.example.demo.overview;

import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.util.Date;

public class OverviewController {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(OverviewController.class);

  private static final String[] ITEM_KEYS = {
    "basic_itemUnknown",
    "basic_itemMr",
    "basic_itemMrs",
  };

// ///////////////////////////////////////////// attribute

  private String radioValue;

  private String singleValue;

  private String[] multiValue;

  private String basicInput = "";

  private String basicArea = "";

  private Date basicDate = new Date();

  private Date basicTime = new Date();

  private String lastAction;

// ///////////////////////////////////////////// constructor

  public OverviewController() {
    radioValue = ITEM_KEYS[0];
    singleValue = ITEM_KEYS[0];
    multiValue = new String[0];
  }

// ///////////////////////////////////////////// action

// ///////////////////////////////////////////// util

  private static SelectItem[] getSelectItems(
      String[] keys, ResourceManager resourceManager, String resource) {
    SelectItem[] items = new SelectItem[keys.length];
    for (int i = 0; i < items.length; i++) {
      String label = resourceManager.getProperty(
          FacesContext.getCurrentInstance().getViewRoot(), resource, keys[i]);
      items[i] = new SelectItem(keys[i], label);
    }
    return items;
  }

  public void click(ActionEvent actionEvent) {
    lastAction = actionEvent.getComponent().getId();
  }

  public boolean getShowPopup() {
    return "popupButton".equals(lastAction) || "popupButton2".equals(lastAction) ;
  }

// ///////////////////////////////////////////// getter + setter

  public SelectItem[] getItems() {
    ResourceManager resourceManager = ResourceManagerFactory
        .getResourceManager(FacesContext.getCurrentInstance());
    return getSelectItems(ITEM_KEYS, resourceManager, "overview");
  }

// ///////////////////////////////////////////// bean getter + setter

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
}

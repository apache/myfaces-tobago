package org.apache.myfaces.tobago.example.reference;

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

import org.apache.myfaces.tobago.model.AutoSuggestExtensionItem;
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.AutoSuggestItems;

import javax.faces.component.UIInput;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputSuggestController {

  private static final Logger LOG = LoggerFactory.getLogger(InputSuggestController.class);

  private String simpleValue;
  private String zipValue;
  private String cityValue;

  private List<SelectItem> regionItems;

  private String region;

  private String[][] suggestData = {
      {"26127 Oldenburg", "26127", "Oldenburg", "Niedersachsen"},
      {"26203 Wardenburg", "26203", "Wardenburg", "Niedersachsen"},
      {"26160 Bad Zwischenahn", "26160", "Bad Zwischenahn", "Niedersachsen"},
      {"26655 Westerstede", "26655", "Westerstede", "Niedersachsen"},
      {"26919 Brake (Unterweser)", "26919", "Brake", "Niedersachsen"},
      {"57462 Olpe", "57462", "Olpe", "Nordrhein-Westfalen"},
      {"23758 Oldenburg (Holstein)", "23758", "Oldenburg", "Schleswig Holstein"},
      {"99628 Olbersleben", "99628", "Olbersleben", "Tueringen"},
      {"25860 Olderup", "25860", "Olderup", "Schleswig Holstein"},
      {"66851 Olenkorb", "66851", "Olenkorb", "Rheinland-Pfalz"}
  };


  public String getSimpleValue() {
    return simpleValue;
  }

  public void setSimpleValue(String simpleValue) {
    this.simpleValue = simpleValue;
  }

  public String getZipValue() {
    return zipValue;
  }

  public void setZipValue(String zipValue) {
    this.zipValue = zipValue;
  }

  public String getCityValue() {
    return cityValue;
  }

  public void setCityValue(String cityValue) {
    this.cityValue = cityValue;
  }

  public List<SelectItem> getRegionItems() {
    if (regionItems == null) {
      regionItems = new ArrayList<SelectItem>(17);
      regionItems.add(new SelectItem("Please select"));
      regionItems.add(new SelectItem("Baden-Württemberg"));
      regionItems.add(new SelectItem("Bayern"));
      regionItems.add(new SelectItem("Berlin"));
      regionItems.add(new SelectItem("Brandenburg"));
      regionItems.add(new SelectItem("Bremen"));
      regionItems.add(new SelectItem("Hamburg"));
      regionItems.add(new SelectItem("Hessen"));
      regionItems.add(new SelectItem("Mecklenburg-Vorpommern"));
      regionItems.add(new SelectItem("Niedersachsen"));
      regionItems.add(new SelectItem("Nordrhein-Westfalen"));
      regionItems.add(new SelectItem("Rheinland-Pfalz"));
      regionItems.add(new SelectItem("Saarland"));
      regionItems.add(new SelectItem("Sachsen"));
      regionItems.add(new SelectItem("Sachsen-Anhalt"));
      regionItems.add(new SelectItem("Schleswig Holstein"));
      regionItems.add(new SelectItem("Tueringen"));
    }
    return regionItems;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public List<String> getSimpleSuggestItems(UIInput component) {
    String prefix = (String) component.getSubmittedValue();
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

  public AutoSuggestItems getZipSuggestItems(UIInput component) {
    String prefix = (String) component.getSubmittedValue();
    AutoSuggestItems item = new AutoSuggestItems();
    item.setItems(getSuggestItems(prefix, true));
    item.setNextFocusId("page:txarea");
    return item;
  }



  public AutoSuggestItems getCitySuggestItems(UIInput component) {
    String prefix = (String) component.getSubmittedValue();
    AutoSuggestItems item = new AutoSuggestItems();
    item.setItems(getSuggestItems(prefix, false));
    item.setNextFocusId("page:txarea");
    return item;
  }

  private List<AutoSuggestItem> getSuggestItems(String prefix, boolean zip) {
    List<AutoSuggestItem> items = new ArrayList<AutoSuggestItem>();
    for (String[] dataRow : suggestData) {
      if (zip) {
        if (dataRow[1].startsWith(prefix)) {
          AutoSuggestItem item = new AutoSuggestItem();
          item.setLabel(dataRow[0]);
          item.setValue(dataRow[1]);
          List<AutoSuggestExtensionItem> extensionItems = new ArrayList<AutoSuggestExtensionItem>(2);
          extensionItems.add(createExtensionItem("page:isCity", dataRow[2]));
          if (dataRow[3] != null) {
            extensionItems.add(createExtensionItem("page:isState", dataRow[3]));
          }
          item.setExtensionItems(extensionItems);
          items.add(item);
        }
      } else {
        if (dataRow[2].toLowerCase().startsWith(prefix.toLowerCase())) {
          AutoSuggestItem item = new AutoSuggestItem();
          item.setLabel(dataRow[0]);
          item.setValue(dataRow[2]);
          List<AutoSuggestExtensionItem> extensionItems = new ArrayList<AutoSuggestExtensionItem>(2);
          extensionItems.add(createExtensionItem("page:isZip", dataRow[1]));
          if (dataRow[3] != null) {
            extensionItems.add(createExtensionItem("page:isState", dataRow[3]));
          }
          item.setExtensionItems(extensionItems);
          items.add(item);
        }
      }
    }

    return items;
  }

  private AutoSuggestExtensionItem createExtensionItem(String id, String value) {
    AutoSuggestExtensionItem extItem = new AutoSuggestExtensionItem();
    extItem.setId(id);
    extItem.setValue(value);
    return extItem;
  }
}

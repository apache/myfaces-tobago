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

import org.apache.commons.lang3.RandomUtils;
import org.apache.myfaces.tobago.model.AutoSuggestExtensionItem;
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.AutoSuggestItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class InputSuggestController implements Serializable {

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

  public void setSimpleValue(final String simpleValue) {
    this.simpleValue = simpleValue;
  }

  public String getZipValue() {
    return zipValue;
  }

  public void setZipValue(final String zipValue) {
    this.zipValue = zipValue;
  }

  public String getCityValue() {
    return cityValue;
  }

  public void setCityValue(final String cityValue) {
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

  public void setRegion(final String region) {
    this.region = region;
  }

  public List<String> getSimpleSuggestItems(final UIInput component) {
    String prefix = (String) component.getSubmittedValue();
    LOG.info("Creating items for prefix '" + prefix + "'");
    if (prefix == null) {
      prefix = "";
    }
    final List<String> list = new ArrayList<String>();
    final int n = RandomUtils.insecure().randomInt(0, 10);
    for (int i = 0; i < n; i++) {
      list.add(prefix + i);
    }
    return list;
  }

  public AutoSuggestItems getZipSuggestItems(final UIInput component) {
    final String prefix = (String) component.getSubmittedValue();
    final AutoSuggestItems item = new AutoSuggestItems();
    item.setItems(getSuggestItems(prefix, true));
    item.setNextFocusId("page:txarea");
    return item;
  }



  public AutoSuggestItems getCitySuggestItems(final UIInput component) {
    final String prefix = (String) component.getSubmittedValue();
    final AutoSuggestItems item = new AutoSuggestItems();
    item.setItems(getSuggestItems(prefix, false));
    item.setNextFocusId("page:txarea");
    return item;
  }

  private List<AutoSuggestItem> getSuggestItems(final String prefix, final boolean zip) {
    final List<AutoSuggestItem> items = new ArrayList<AutoSuggestItem>();
    for (final String[] dataRow : suggestData) {
      if (zip) {
        if (dataRow[1].startsWith(prefix)) {
          final AutoSuggestItem item = new AutoSuggestItem();
          item.setLabel(dataRow[0]);
          item.setValue(dataRow[1]);
          final List<AutoSuggestExtensionItem> extensionItems = new ArrayList<AutoSuggestExtensionItem>(2);
          extensionItems.add(createExtensionItem("page:isCity", dataRow[2]));
          if (dataRow[3] != null) {
            extensionItems.add(createExtensionItem("page:isState", dataRow[3]));
          }
          item.setExtensionItems(extensionItems);
          items.add(item);
        }
      } else {
        if (dataRow[2].toLowerCase().startsWith(prefix.toLowerCase())) {
          final AutoSuggestItem item = new AutoSuggestItem();
          item.setLabel(dataRow[0]);
          item.setValue(dataRow[2]);
          final List<AutoSuggestExtensionItem> extensionItems = new ArrayList<AutoSuggestExtensionItem>(2);
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

  private AutoSuggestExtensionItem createExtensionItem(final String id, final String value) {
    final AutoSuggestExtensionItem extItem = new AutoSuggestExtensionItem();
    extItem.setId(id);
    extItem.setValue(value);
    return extItem;
  }
}

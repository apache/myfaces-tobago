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

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.model.SelectItem;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIData;
import jakarta.faces.event.FacesEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// XXX former @ViewAccessScoped
@SessionScoped
@Named
public class SheetController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final SelectItem[] SHEET_SELECTABLE;

  @Inject
  private AstroData astroData;

  static {
    final List<Selectable> collect = new ArrayList<>();
    for (final Selectable selectable : Selectable.values()) {
      if (selectable.isSupportedBySheet()) {
        collect.add(selectable);
      }
    }
    SHEET_SELECTABLE = new SelectItem[collect.size()];
    for (int i = 0; i < collect.size(); i++) {
      final Selectable selectable = collect.get(i);
      SHEET_SELECTABLE[i] = new SelectItem(selectable, selectable.name());
    }
  }

  private List<SolarObject> solarList;
  private List<SolarObject> hugeSolarList;
  private SheetState sheetState;
  private SolarObject selectedSolarObject;
  private boolean automaticLayout;
  private List<Markup> markup;
  private int columnEventSample;
  private Selectable selectable = Selectable.multi;

  @PostConstruct
  private void init() {
    solarList = astroData.findAll().collect(Collectors.toList());

    int j = 1;
    hugeSolarList = new ArrayList<>();
    while (true) {
      for (final SolarObject solarObject : solarList) {
        final SolarObject solarObjectClone = new SolarObject(solarObject);
        hugeSolarList.add(solarObjectClone);
        solarObjectClone.setName("#" + j++ + " " + solarObject.getName());

        if (j > 10000) {
          return;
        }
      }
    }
  }

  public List<SolarObject> getSolarList() {
    return solarList;
  }

  public List<SolarObject> getHugeSolarList() {
    return hugeSolarList;
  }

  public SheetState getSheetState() {
    return sheetState;
  }

  public void setSheetState(final SheetState sheetState) {
    this.sheetState = sheetState;
  }

  public void selectSolarObject(final FacesEvent actionEvent) {
    LOG.info("actionEvent=" + actionEvent);
    final UIData data = ComponentUtils.findAncestor(actionEvent.getComponent(), UIData.class);
    if (data != null) {
      selectedSolarObject = (SolarObject) data.getRowData();
      LOG.info("Selected: " + selectedSolarObject.getName());
    } else {
      selectedSolarObject = null;
      LOG.info("Deselect.");
    }
  }

  public SolarObject getSelectedSolarObject() {
    return selectedSolarObject;
  }

  public boolean isAutomaticLayout() {
    return automaticLayout;
  }

  public void setAutomaticLayout(final boolean automaticLayout) {
    this.automaticLayout = automaticLayout;
  }

  public List<Markup> getMarkup() {
    return markup;
  }

  public void setMarkup(final List<Markup> markup) {
    this.markup = markup;
  }

  public void setColumnEventSample(final int columnEventSample) {
    this.columnEventSample = columnEventSample;
  }

  public int getColumnEventSample() {
    return columnEventSample;
  }

  public Selectable getSelectable() {
    return selectable;
  }

  public void setSelectable(final Selectable selectable) {
    this.selectable = selectable;
  }

  public SelectItem[] getSelectableModes() {
    return SHEET_SELECTABLE;
  }
}

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

package org.apache.myfaces.tobago.example.demo.test.sheet;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIData;
import jakarta.faces.event.FacesEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.myfaces.tobago.example.demo.AstroData;
import org.apache.myfaces.tobago.example.demo.SolarObject;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@SessionScoped
@Named
public class SelectableUseCaseController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  private String columnEvent = "ajax";
  private Selectable selectable = Selectable.multi;
  private List<SolarObject> solarList;
  private SheetState sheetState = new SheetState(0);
  private SolarObject selectedSolarObject;

  @PostConstruct
  private void init() {
    solarList = astroData.findAll().collect(Collectors.toList());
    selectedSolarObject = solarList.get(0);
  }

  public void setColumnEvent(final String columnEvent) {
    this.columnEvent = columnEvent;
  }

  public String getColumnEvent() {
    return columnEvent;
  }

  public Selectable getSelectable() {
    return selectable;
  }

  public void setSelectable(final Selectable selectable) {
    this.selectable = selectable;
  }

  public void reset() {
    sheetState = new SheetState(0);
    selectedSolarObject = solarList.get(0);
  }

  public List<SolarObject> getSolarList() {
    return solarList;
  }

  public SheetState getSheetState() {
    return sheetState;
  }

  public void setSheetState(SheetState sheetState) {
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
}

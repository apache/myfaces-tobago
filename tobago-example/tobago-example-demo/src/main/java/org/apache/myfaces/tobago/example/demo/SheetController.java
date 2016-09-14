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
import org.apache.myfaces.tobago.example.data.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Named
public class SheetController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(SheetController.class);

  private List<SolarObject> solarList;
  private SheetState sheetState;
  private SolarObject selectedSolarObject;
  private boolean automaticLayout;
  private List<Markup> markup;
  private int columnEventSample;

  public SheetController() {
    solarList = SolarObject.getList();
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

  public void selectSolarObject(final ActionEvent actionEvent) {
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

  public int getNumberOfSelections() {
    return sheetState.getSelectedRows().size();
  }

  public int getSelectedRowNumber() {
    if (sheetState.getSelectedRows().size() <= 0) {
      return -1;
    } else {
      return sheetState.getSelectedRows().get(0);
    }
  }

  public boolean isAutomaticLayout() {
    return automaticLayout;
  }

  public void setAutomaticLayout(boolean automaticLayout) {
    this.automaticLayout = automaticLayout;
  }

  public List<Markup> getMarkup() {
    return markup;
  }

  public void setMarkup(List<Markup> markup) {
    this.markup = markup;
  }

  public void setColumnEventSample(int columnEventSample) {
    this.columnEventSample = columnEventSample;
  }

  public int getColumnEventSample() {
    return columnEventSample;
  }
}

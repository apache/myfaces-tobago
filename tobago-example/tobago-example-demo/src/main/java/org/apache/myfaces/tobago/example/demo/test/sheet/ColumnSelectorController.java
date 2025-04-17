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

import org.apache.myfaces.tobago.example.demo.AstroData;
import org.apache.myfaces.tobago.example.demo.SolarObject;
import org.apache.myfaces.tobago.model.Selectable;
import org.apache.myfaces.tobago.model.SheetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SessionScoped
@Named
public class ColumnSelectorController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  private List<SolarObject> solarList = new ArrayList<>();
  private int rows = 5;
  private boolean lazy = false;
  private SheetState sheetState = new SheetState(0);
  private Selectable sheetSelectable = Selectable.multi;
  private Selectable columnSelectorSelectable = Selectable.multi;
  private boolean columnSelectorDisabled = false;
  private boolean columnSelectorRendered = true;
  private boolean columnPanelRendered = false;

  @PostConstruct
  private void init() {
    solarList = astroData.findAll().collect(Collectors.toList());
  }

  private void reset() {
    rows = 5;
    lazy = false;
    sheetState = new SheetState(0);
    sheetSelectable = Selectable.multi;
    columnSelectorSelectable = Selectable.multi;
    columnSelectorDisabled = false;
    columnSelectorRendered = true;
    columnPanelRendered = false;
  }

  public void setupDefaultSheet() {
    reset();
    columnSelectorRendered = false;
  }

  public void setupSelectableNone() {
    reset();
    sheetSelectable = Selectable.none;
    columnSelectorRendered = false;
  }

  public void setupSelectableSingle() {
    reset();
    sheetSelectable = Selectable.single;
  }

  public void setupSelectableSingleOrNone() {
    reset();
    sheetSelectable = Selectable.singleOrNone;
  }

  public void setupSelectableMulti() {
    reset();
  }

  public void setupLazy() {
    reset();
    rows = 0;
    lazy = true;
  }

  public void setupLazyColumnPanel() {
    setupLazy();
    columnPanelRendered = true;
  }

  public void setupDisabledColumnSelector() {
    reset();
    List<Integer> selectedRows = new ArrayList<>();
    selectedRows.add(2);
    sheetState.setSelectedRows(selectedRows);
    columnSelectorDisabled = true;
  }

  public void setupColumnPanel() {
    reset();
    columnPanelRendered = true;
  }

  public List<SolarObject> getSolarList() {
    return solarList;
  }

  public void setSolarList(List<SolarObject> solarList) {
    this.solarList = solarList;
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public boolean isLazy() {
    return lazy;
  }

  public void setLazy(boolean lazy) {
    this.lazy = lazy;
  }

  public SheetState getSheetState() {
    return sheetState;
  }

  public void setSheetState(SheetState sheetState) {
    this.sheetState = sheetState;
  }

  public Selectable getSheetSelectable() {
    return sheetSelectable;
  }

  public void setSheetSelectable(Selectable sheetSelectable) {
    this.sheetSelectable = sheetSelectable;
  }

  public Selectable getColumnSelectorSelectable() {
    return columnSelectorSelectable;
  }

  public void setColumnSelectorSelectable(Selectable columnSelectorSelectable) {
    this.columnSelectorSelectable = columnSelectorSelectable;
  }

  public boolean isColumnSelectorDisabled() {
    return columnSelectorDisabled;
  }

  public void setColumnSelectorDisabled(boolean columnSelectorDisabled) {
    this.columnSelectorDisabled = columnSelectorDisabled;
  }

  public boolean isColumnSelectorRendered() {
    return columnSelectorRendered;
  }

  public void setColumnSelectorRendered(boolean columnSelectorRendered) {
    this.columnSelectorRendered = columnSelectorRendered;
  }

  public boolean isColumnPanelRendered() {
    return columnPanelRendered;
  }

  public void setColumnPanelRendered(boolean columnPanelRendered) {
    this.columnPanelRendered = columnPanelRendered;
  }
}

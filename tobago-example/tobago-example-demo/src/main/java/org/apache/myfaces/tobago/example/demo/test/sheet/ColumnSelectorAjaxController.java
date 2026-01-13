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
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.myfaces.tobago.example.demo.AstroData;
import org.apache.myfaces.tobago.example.demo.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SessionScoped
@Named
public class ColumnSelectorAjaxController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  private List<SolarObject> solarList = new ArrayList<>();
  private SheetState sheetState = new SheetState(0);
  private boolean columnSelectorImmediate = false;
  private boolean columnSelectorAjaxDisabled = false;
  private int savedSelectedRows = 0;

  @PostConstruct
  private void init() {
    solarList = astroData.findAll().collect(Collectors.toList());
  }

  public void reset(boolean immediate, boolean ajaxDisabled) {
    sheetState = new SheetState(0);
    columnSelectorImmediate = immediate;
    columnSelectorAjaxDisabled = ajaxDisabled;
  }

  public List<SolarObject> getSolarList() {
    return solarList;
  }

  public void setSolarList(List<SolarObject> solarList) {
    this.solarList = solarList;
  }

  public SheetState getSheetState() {
    return sheetState;
  }

  public void setSheetState(SheetState sheetState) {
    this.sheetState = sheetState;
  }

  public boolean isColumnSelectorImmediate() {
    return columnSelectorImmediate;
  }

  public void setColumnSelectorImmediate(boolean columnSelectorImmediate) {
    this.columnSelectorImmediate = columnSelectorImmediate;
  }

  public boolean isColumnSelectorAjaxDisabled() {
    return columnSelectorAjaxDisabled;
  }

  public void setColumnSelectorAjaxDisabled(boolean columnSelectorAjaxDisabled) {
    this.columnSelectorAjaxDisabled = columnSelectorAjaxDisabled;
  }

  public void saveSelectedRows() {
    savedSelectedRows = sheetState.getSelectedRows().size();
  }

  public int getSavedSelectedRows() {
    return savedSelectedRows;
  }
}

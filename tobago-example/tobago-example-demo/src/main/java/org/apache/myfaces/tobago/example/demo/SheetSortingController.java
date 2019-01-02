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

import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.model.SheetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SessionScoped
@Named
public class SheetSortingController extends SheetController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public void sheetSorter(final ActionEvent event) {
    if (event instanceof SortActionEvent) {
      final SortActionEvent sortEvent = (SortActionEvent) event;
      final UISheet sheet = (UISheet) sortEvent.getComponent();
      final SheetState sheetState = sheet.getSheetState(FacesContext.getCurrentInstance());
      final List<SolarObject> list = (List<SolarObject>) sheet.getValue();
      sheetSorter(sheetState, list);
    }
  }

  private void sheetSorter(final SheetState sheetState, final List<SolarObject> list) {
    final String columnId = sheetState.getSortedColumnId();

    LOG.info("Sorting column '{}'", columnId);

    if ("customColumnName".equals(columnId)) {
      list.sort(Comparator.comparing(SolarObject::getName, String.CASE_INSENSITIVE_ORDER));
    } else if ("customColumnPeriod".equals(columnId)) {
      list.sort(Comparator.comparing(d -> Math.abs(d.getPeriod())));
    } else if ("customColumnYear".equals(columnId)) {
      list.sort(Comparator.comparing(d -> d.getDiscoverYear() != null ? d.getDiscoverYear() : 0));
    }

    if (!sheetState.isAscending()) {
      Collections.reverse(list);
    }
  }
}

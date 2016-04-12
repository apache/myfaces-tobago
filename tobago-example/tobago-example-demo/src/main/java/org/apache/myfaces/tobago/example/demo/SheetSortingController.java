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
import org.apache.myfaces.tobago.example.data.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SessionScoped
@Named
public class SheetSortingController extends SheetController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(SheetSortingController.class);

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

    Comparator<SolarObject> comparator = null;

    if ("namecol".equals(columnId)) {
      comparator = new Comparator<SolarObject>() {
        @Override
        public int compare(final SolarObject o1, final SolarObject o2) {
          return o1.getName().compareToIgnoreCase(o2.getName());
        }
      };
    } else if ("periodcol".equals(columnId)) {
      comparator = new Comparator<SolarObject>() {
        @Override
        public int compare(final SolarObject o1, final SolarObject o2) {
          Double period1 = Math.abs(o1.getPeriod());
          Double period2 = Math.abs(o2.getPeriod());
          return period1.compareTo(period2);
        }
      };
    } else if ("yearcol".equals(columnId)) {
      comparator = new Comparator<SolarObject>() {
        @Override
        public int compare(final SolarObject o1, final SolarObject o2) {
          Integer discoverYear1 = o1.getDiscoverYear() != null ? o1.getDiscoverYear() : 0;
          Integer discoverYear2 = o2.getDiscoverYear() != null ? o2.getDiscoverYear() : 0;
          return discoverYear1.compareTo(discoverYear2);
        }
      };
    }

    Collections.sort(list, comparator);
    if (!sheetState.isAscending()) {
      Collections.reverse(list);
    }
  }
}

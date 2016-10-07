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
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.convert.DateTimeConverter;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@SessionScoped
@Named
public class SheetController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(SheetController.class);

  private List<SolarObject> solarList;
  private List<SolarObject> hugeSolarList;
  private SheetState sheetState;
  private SolarObject selectedSolarObject;
  private boolean automaticLayout;
  private List<Markup> markup;
  private int columnEventSample;

  public SheetController() {
    solarList = SolarObject.getList();

    hugeSolarList = new ArrayList<SolarObject>();
    for (int i = 1; i <= 12; i++) {
      for (SolarObject solarObject : solarList) {
        SolarObject solarObjectClone = new SolarObject(solarObject);
        solarObjectClone.setName(solarObject.getName() + " (" + i + ". entry)");
        hugeSolarList.add(solarObjectClone);
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

  public javax.faces.convert.Converter getYearConverter() {

    final DateTimeConverter dateTimeConverter = new DateTimeConverter() {

      @Override
      public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        final Date date = (Date) super.getAsObject(facesContext, uiComponent, value);
        final Calendar calendar = GregorianCalendar.getInstance(facesContext.getViewRoot().getLocale());
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
      }

      @Override
      public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        final Calendar calendar = GregorianCalendar.getInstance(facesContext.getViewRoot().getLocale());
        calendar.set(Calendar.YEAR, (Integer) value);
        final Date date = calendar.getTime();
        return super.getAsString(facesContext, uiComponent, date);
      }
    };

    dateTimeConverter.setPattern("yyyy");
    return dateTimeConverter;
  }
}

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

package org.apache.myfaces.tobago.example.test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.model.SelectItem;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SessionController {

  private static final Logger LOG = LoggerFactory.getLogger(SessionController.class);

  private TabChangeListener tabChangeListener;

  private Integer selectedIndex0;

  private Integer selectedIndex1;

  private Integer selectedIndex2;

  private String value;

  private Date date;

  private Date validityStart;
  private Date validityEnd;

  private boolean suppressProcessing;

  private List<Row> suppressProcessingList = Arrays.asList(new Row(), new Row());

  public static class Row {
    private boolean suppressProcessing;
    private String input;

    public boolean isSuppressProcessing() {
      return suppressProcessing;
    }

    public void setSuppressProcessing(final boolean suppressProcessing) {
      this.suppressProcessing = suppressProcessing;
    }

    public String getInput() {
      return input;
    }

    public void setInput(final String input) {
      this.input = input;
    }
  }

  public Date getDate() {
    return date;
  }

  public void setDate(final Date date) {
    this.date = date;
  }

  public SelectItem[] getDateItems() {
    final SelectItem[] items = new SelectItem[2];
    final DateFormat format = DateFormat.getDateInstance(SimpleDateFormat.SHORT, Locale.GERMANY);
    format.setTimeZone(TimeZone.getTimeZone("GMT"));
    try {
      Date date = format.parse("12.10.2009");
      items[0] = new SelectItem(date);
      date = format.parse("13.10.2009");
      items[1] = new SelectItem(date);
    } catch (final ParseException e) {
      LOG.error("", e);
    }
    return items;
  }

  public void actionListener(final ActionEvent e) {
    UIComponent component = e.getComponent();
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof UIParameter) {
        LOG.error("{}", ((UIParameter) child).getValue());
      }
    }
    while ((component = component.getParent()) != null) {
      if (component instanceof UISheet) {
        LOG.error("{}", ((UISheet) component).getRowIndex());
      }
    }
  }

  public SessionController() {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(2002, 0, 1);
    validityStart = calendar.getTime();
  }

  public String checkDates() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    if (validityEnd.before(validityStart)) {
      final String message = "End date before start date.";
      facesContext.addMessage("page:validityEnd",
          new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
    }
    return "messages";
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public TabChangeListener getTabChangeListener() {
    LOG.info("getTabChangeListener {}",  tabChangeListener);
    return tabChangeListener;
  }

  public void setTabChangeListener(final TabChangeListener tabChangeListener) {
    LOG.info("Setting TabChangeListener {}",  tabChangeListener);
    this.tabChangeListener = tabChangeListener;
  }

  public Integer getSelectedIndex0() {
    return selectedIndex0;
  }

  public void setSelectedIndex0(final Integer selectedIndex0) {
    this.selectedIndex0 = selectedIndex0;
  }

  public Integer getSelectedIndex1() {
    return selectedIndex1;
  }

  public void setSelectedIndex1(final Integer selectedIndex1) {
    this.selectedIndex1 = selectedIndex1;
  }

  public Integer getSelectedIndex2() {
    return selectedIndex2;
  }

  public void setSelectedIndex2(final Integer selectedIndex2) {
    this.selectedIndex2 = selectedIndex2;
  }

  public Date getValidityStart() {
    return validityStart;
  }

  public void setValidityStart(final Date validityStart) {
    this.validityStart = validityStart;
  }

  public Date getValidityEnd() {
    return validityEnd;
  }

  public void setValidityEnd(final Date validityEnd) {
    this.validityEnd = validityEnd;
  }

  public boolean isSuppressProcessing() {
    return suppressProcessing;
  }

  public void setSuppressProcessing(final boolean suppressProcessing) {
    this.suppressProcessing = suppressProcessing;
  }

  public List<Row> getSuppressProcessingList() {
    return suppressProcessingList;
  }

  public void setSuppressProcessingList(final List<Row> suppressProcessingList) {
    this.suppressProcessingList = suppressProcessingList;
  }
}

package org.apache.myfaces.tobago.example.test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.event.TabChangeListener;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.Calendar;
import java.util.Date;

public class SessionController {

  private static final Log LOG = LogFactory.getLog(SessionController.class);

  private TabChangeListener tabChangeListener;

  private Integer selectedIndex0;

  private Integer selectedIndex1;

  private Integer selectedIndex2;

  private String value;

  private Date validityStart;
  private Date validityEnd;

  public SessionController() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2002, 0, 1);
    validityStart = calendar.getTime();
  }

  public String checkDates() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    if (validityEnd.before(validityStart)) {
      String message = "End date before start date.";
      facesContext.addMessage("page:validityEnd",
          new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
    }
    return "messages";
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public TabChangeListener getTabChangeListener() {
    LOG.info("getTabChangeListener " + tabChangeListener);
    return tabChangeListener;
  }

  public void setTabChangeListener(TabChangeListener tabChangeListener) {
    LOG.info("Setting TabChangeListener " + tabChangeListener);
    this.tabChangeListener = tabChangeListener;
  }

  public Integer getSelectedIndex0() {
    return selectedIndex0;
  }

  public void setSelectedIndex0(Integer selectedIndex0) {
    this.selectedIndex0 = selectedIndex0;
  }

  public Integer getSelectedIndex1() {
    return selectedIndex1;
  }

  public void setSelectedIndex1(Integer selectedIndex1) {
    this.selectedIndex1 = selectedIndex1;
  }

  public Integer getSelectedIndex2() {
    return selectedIndex2;
  }

  public void setSelectedIndex2(Integer selectedIndex2) {
    this.selectedIndex2 = selectedIndex2;
  }

  public Date getValidityStart() {
    return validityStart;
  }

  public void setValidityStart(Date validityStart) {
    this.validityStart = validityStart;
  }

  public Date getValidityEnd() {
    return validityEnd;
  }

  public void setValidityEnd(Date validityEnd) {
    this.validityEnd = validityEnd;
  }
}

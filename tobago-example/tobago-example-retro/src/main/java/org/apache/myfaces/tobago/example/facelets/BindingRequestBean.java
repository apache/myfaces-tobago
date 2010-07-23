package org.apache.myfaces.tobago.example.facelets;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.model.SelectItem;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.component.UISelectOne;

/**
 * Date: Sep 22, 2007
 * Time: 8:47:48 AM
 */
public class BindingRequestBean {
  private static final Logger LOG = LoggerFactory.getLogger(BindingRequestBean.class);
  private String selectOne;
  private UISelectOne uiSelectOne;
  private UIPanel uiPanel;
  private String input;

  private SelectItem [] items = {new SelectItem("Test1", "Test1"), new SelectItem("Test2", "Test2")};


  public UIPanel getUiPanel() {
    LOG.error("getUiPanel " + uiPanel);
    return uiPanel;
  }

  public void setUiPanel(UIPanel uiPanel) {
    LOG.error("setUiPanel " + uiPanel);
    this.uiPanel = uiPanel;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String getSelectOne() {
    return selectOne;
  }

  public void setSelectOne(String selectOne) {
    this.selectOne = selectOne;
  }

  public UISelectOne getUiSelectOne() {
    return uiSelectOne;
  }

  public void setUiSelectOne(UISelectOne uiSelectOne) {
    this.uiSelectOne = uiSelectOne;
  }

  public SelectItem[] getItems() {
    return items;
  }

  public void setItems(SelectItem[] items) {
    this.items = items;
  }

  public void processValueChange(ValueChangeEvent e) {
    LOG.error("{}", e);
  }

  public void actionListener(ActionEvent e) {
    LOG.error("{}", e);
  }
}

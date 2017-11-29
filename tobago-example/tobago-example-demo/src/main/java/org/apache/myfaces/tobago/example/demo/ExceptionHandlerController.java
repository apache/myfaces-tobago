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

import org.apache.myfaces.tobago.example.data.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;
import java.util.List;

@RequestScoped
@Named
public class ExceptionHandlerController {
  private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

  private String value;
  private List<SolarObject> solarList;
  private SheetState sheetState;

  public ExceptionHandlerController() {
    solarList = SolarObject.getList();
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public void update(final AjaxBehaviorEvent event) {
    if (((String) event.getComponent().getAttributes().get("value")).contains("x")) {
      throw new RuntimeException("This exception is thrown, because the input value was 'x'.");
    }
    LOG.info("AjaxBehaviorEvent called. Current value: '{}'", value);
  }

  public List<SolarObject> getSolarList() {
    return solarList;
  }

  public SheetState getSheetState() {
    return sheetState;
  }

  public void setSheetState(final SheetState sheetState) {
    this.sheetState = sheetState;
  }

  public void preRenderViewListener(final ComponentSystemEvent event) {
    if (sheetState != null && sheetState.getFirst() > 20) {
      sheetState.setFirst(0);
      throw new NullPointerException("This exception is thrown, because page 7 or higher is selected.");
    }
  }
}

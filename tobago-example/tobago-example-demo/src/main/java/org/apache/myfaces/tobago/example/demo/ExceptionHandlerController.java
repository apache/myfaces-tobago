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

import org.apache.myfaces.tobago.model.SheetState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Named
public class ExceptionHandlerController {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String value;
  private List<SolarObject> solarList;
  private SheetState sheetState;

  @Inject
  private AstroData astroData;

  @PostConstruct
  private void init() {
    solarList = astroData.findAll().collect(Collectors.toList());
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public void update(final AjaxBehaviorEvent event) {
    if (((String) event.getComponent().getAttributes().get("value")).contains("x")) {
      throw new DemoException("This exception is thrown, because the input value was 'x'.");
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

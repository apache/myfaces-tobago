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

package org.apache.myfaces.tobago.example.demo.test.checkbox;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.myfaces.tobago.example.demo.AstroData;
import org.apache.myfaces.tobago.example.demo.SolarObject;
import org.apache.myfaces.tobago.model.SheetState;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Named
public class CheckboxController implements Serializable {

  @Inject
  private AstroData astroData;

  private List<String> animals = new ArrayList<>();
  private List<SolarObject> solarList;
  private SheetState sheetState = new SheetState(1);

  @PostConstruct
  private void init() {
    animals.add("dog");
    animals.add("rabbit");

    solarList = astroData.findAll().collect(Collectors.toList());
    sheetState.getSelectedRows().add(1);
    sheetState.getSelectedRows().add(3);
  }

  public List<String> getAnimals() {
    return animals;
  }

  public void setAnimals(final List<String> animals) {
    this.animals = animals;
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
}

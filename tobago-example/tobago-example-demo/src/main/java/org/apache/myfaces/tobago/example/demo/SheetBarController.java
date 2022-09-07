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

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.myfaces.tobago.model.SheetState;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@SessionScoped
@Named
public class SheetBarController implements Serializable {

  @Inject
  private AstroData astroData;

  private List<SolarObject> solarList;

  private SheetState state;

  @PostConstruct
  private void init() {
    solarList = astroData.findAll().collect(Collectors.toList());
  }

  public List<SolarObject> getSolarList() {
    return solarList;
  }

  public SheetState getState() {
    return state;
  }

  public void setState(SheetState state) {
    this.state = state;
  }

  public void selectAllOnServer() {
    final List<Integer> selectedRows = state.getSelectedRows();
    selectedRows.clear();
    for (int i = 0; i < solarList.size(); i++) {
      selectedRows.add(i);
    }
  }

  public void deselectAllOnServer() {
    final List<Integer> selectedRows = state.getSelectedRows();
    selectedRows.clear();
  }

  public void all() {
    init();
  }

  public void starsOnly() {
    solarList = astroData.findAll().filter(object -> object.getType() == SolarType.STAR).collect(Collectors.toList());
  }

  public void planetsOnly() {
    solarList = astroData.findAll().filter(object -> object.getType() == SolarType.PLANET).collect(Collectors.toList());
  }

  public void moonsOnly() {
    solarList = astroData.findAll().filter(object -> object.getType() == SolarType.MOON).collect(Collectors.toList());
  }
}

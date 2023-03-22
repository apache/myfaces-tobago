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
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class SelectManyListController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  private List<SolarObject> planets;
  private SolarObject[] selectedPlanets;

  private String filterType = "contains";
  private final List<String> names = new ArrayList<>();
  private String[] selectedNames = new String[0];

  @PostConstruct
  public void init() {
    planets = astroData.getSatellites("Sun");
    resetSelectedPlanets();

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        Thread.currentThread().getContextClassLoader().getResourceAsStream(
            "org/apache/myfaces/tobago/example/demo/names.txt"), StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!line.startsWith("#") && line.length() > 0) {
          names.add(line);
        }
      }
    } catch (Exception e) {
      LOG.error("Can't load names", e);
    }
  }

  public List<SolarObject> getPlanets() {
    return planets;
  }

  public void resetSelectedPlanets() {
    selectedPlanets = new SolarObject[]{planets.get(1), planets.get(2), planets.get(4)};
  }

  public SolarObject[] getSelectedPlanets() {
    return selectedPlanets;
  }

  public void setSelectedPlanets(SolarObject[] selectedPlanets) {
    this.selectedPlanets = selectedPlanets;
  }

  public String getFilterType() {
    return filterType;
  }

  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }

  public List<String> getNames() {
    if (names.size() < 1) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Names not loaded!", null));
      return new ArrayList<>();
    }
    return names;
  }

  public String[] getSelectedNames() {
    return selectedNames;
  }

  public void setSelectedNames(String[] selectedNames) {
    this.selectedNames = selectedNames;
  }
}

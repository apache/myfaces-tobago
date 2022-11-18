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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@Named
public class SelectManyShuttleController implements Serializable {

  @Inject
  private AstroData astroData;

  private List<SolarObject> planets;
  private SolarObject[] selectedPlanets = new SolarObject[0];
  private List<String> stars = Arrays.asList("Proxima Centauri", "Alpha Centauri", "Wolf 359", "Sirius");
  private String[] selectedStars = new String[0];
  private int countPageReload = 0;

  @PostConstruct
  public void init() {
    planets = astroData.getSatellites("Sun");
  }

  public List<SolarObject> getPlanets() {
    return planets;
  }

  public SolarObject[] getSelectedPlanets() {
    return selectedPlanets;
  }

  public void setSelectedPlanets(final SolarObject[] selectedPlanets) {
    this.selectedPlanets = selectedPlanets;
  }

  public String getSelectedPlanetsAsString() {
    return Arrays.toString(selectedPlanets);
  }

  public List<String> getStars() {
    return stars;
  }

  public String[] getSelectedStars() {
    return selectedStars;
  }

  public void setSelectedStars(final String[] selectedStars) {
    this.selectedStars = selectedStars;
  }

  public String getSelectedStarsAsString() {
    return Arrays.toString(selectedStars);
  }

  public int getCountPageReload() {
    return countPageReload++;
  }
}

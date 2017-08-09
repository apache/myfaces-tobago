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

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@Named
public class SelectManyShuttleController implements Serializable {

  private List<SolarObject> planets = new ArrayList<SolarObject>();
  private SolarObject[] selectedPlanets = new SolarObject[0];
  private List<String> stars = Arrays.asList("Proxima Centauri", "Alpha Centauri", "Wolf 359", "Sirius");
  private String[] selectedStars = new String[0];

  public SelectManyShuttleController() {
    planets = SolarObject.getSatellites("Sun");
  }

  public List<SolarObject> getPlanets() {
    return planets;
  }

  public SolarObject[] getSelectedPlanets() {
    return selectedPlanets;
  }

  public void setSelectedPlanets(SolarObject[] selectedPlanets) {
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

  public void setSelectedStars(String[] selectedStars) {
    this.selectedStars = selectedStars;
  }

  public String getSelectedStarsAsString() {
    return Arrays.toString(selectedStars);
  }
}

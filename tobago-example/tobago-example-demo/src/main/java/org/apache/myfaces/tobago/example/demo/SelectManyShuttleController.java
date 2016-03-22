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

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@Named
public class SelectManyShuttleController implements Serializable {

  private List<Planet> planets = new ArrayList<Planet>();
  private String[] selectedPlanets = new String[0];
  private List<String> stars = Arrays.asList("Proxima Centauri", "Alpha Centauri", "Wolf 359", "Sirius");
  private String[] selectedStars = new String[0];

  public SelectManyShuttleController() {
    planets.add(new Planet("Mercury", 0.387));
    planets.add(new Planet("Venus", 0.723));
    planets.add(new Planet("Earth", 1));
    planets.add(new Planet("Mars", 1.524));
    planets.add(new Planet("Jupiter", 5.203));
    planets.add(new Planet("Saturn", 9.537));
    planets.add(new Planet("Uranus", 19.191));
    planets.add(new Planet("Neptun", 30.069));
  }

  public List<Planet> getPlanets() {
    return planets;
  }

  public String[] getSelectedPlanets() {
    return selectedPlanets;
  }

  public void setSelectedPlanets(String[] selectedPlanets) {
    this.selectedPlanets = selectedPlanets;
  }

  public String getSelectedPlanetsAsString() {
    String retValue = "";
    for (String selectedPlanet : selectedPlanets) {
      for (Planet planet : planets) {
        if (planet.getName().equals(selectedPlanet)) {
          retValue = retValue.concat(planet.getName() + " (" + planet.getAu() + "); ");
        }
      }
    }
    return retValue;
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

  public class Planet {
    private String name;
    private double au;

    public Planet(String name, double au) {
      this.name = name;
      this.au = au;
    }

    public String getName() {
      return name;
    }

    public double getAu() {
      return au;
    }
  }
}

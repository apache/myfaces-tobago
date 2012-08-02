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

package org.apache.myfaces.tobago.example.data;

/*
 * Created 20.09.2002 at 10:54:32.
 * $Id$
 */

import java.util.ArrayList;
import java.util.List;

public class Planet {

  public static final Planet MERCURY
      = new Planet("NN", "NN", "NN", "NN", new ArrayList());
  public static final Planet VENUS
      = new Planet("NN", "NN", "NN", "NN", new ArrayList());
  public static final Planet EARTH
      = new Planet("NN", "NN", "NN", "NN", new ArrayList());
  public static final Planet MARS
      = new Planet("6794", "6.421e26", "227940000", "686.98", SolarObject.getSatellites("Mars"));

  private String diameter;
  private String mass;
  private String sunDistance;
  private String timeOfCirculation;
  private List moons;

  public Planet(Planet planet) {
    this.diameter = planet.diameter;
    this.mass = planet.mass;
    this.sunDistance = planet.sunDistance;
    this.timeOfCirculation = planet.timeOfCirculation;
    this.moons = planet.moons;
  }

  public Planet(String diameter, String mass, String sunDistance,
      String timeOfCirculation, List moons) {
    this.diameter = diameter;
    this.mass = mass;
    this.sunDistance = sunDistance;
    this.timeOfCirculation = timeOfCirculation;
    this.moons = moons;
  }

  public String getDiameter() {
    return diameter;
  }

  public void setDiameter(String diameter) {
    this.diameter = diameter;
  }

  public String getMass() {
    return mass;
  }

  public void setMass(String mass) {
    this.mass = mass;
  }

  public String getSunDistance() {
    return sunDistance;
  }

  public void setSunDistance(String sunDistance) {
    this.sunDistance = sunDistance;
  }

  public String getTimeOfCirculation() {
    return timeOfCirculation;
  }

  public void setTimeOfCirculation(String timeOfCirculation) {
    this.timeOfCirculation = timeOfCirculation;
  }

  public List getMoons() {
    return moons;
  }

  public void setMoons(List moons) {
    this.moons = moons;
  }
}

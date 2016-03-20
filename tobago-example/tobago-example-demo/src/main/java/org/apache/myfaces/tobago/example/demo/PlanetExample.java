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

import org.apache.myfaces.tobago.model.SelectItem;

public abstract class PlanetExample {

  private SelectItem[] planets;
  private SelectItem[] earthmoons;
  private SelectItem[] marsmoons;
  private SelectItem[] jupitermoons;
  private int planet;

  public PlanetExample() {
    planets = new SelectItem[]{
            new SelectItem(0, "Earth"),
            new SelectItem(1, "Mars"),
            new SelectItem(2, "Jupiter")};
    earthmoons = new SelectItem[]{
            new SelectItem(0, "Moon")
    };
    marsmoons = new SelectItem[]{
            new SelectItem(0, "Phobos"),
            new SelectItem(1, "Deimos")
    };
    jupitermoons = new SelectItem[]{
            new SelectItem(0, "Europa"),
            new SelectItem(1, "Ganymed"),
            new SelectItem(2, "Io"),
            new SelectItem(3, "Kallisto")
    };
  }

  public SelectItem[] getPlanets() {
    return planets;
  }

  public int getPlanet() {
    return planet;
  }

  public void setPlanet(int planet) {
    this.planet = planet;
  }

  public SelectItem[] getMoons() {
    switch (planet) {
      case 0:
        return earthmoons;
      case 1:
        return marsmoons;
      case 2:
        return jupitermoons;
      default:
        return new SelectItem[0];
    }
  }
}

/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * Created 20.09.2002 at 10:40:44.
 * $Id: Solar.java 1271 2005-08-08 20:44:11 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.demo.model.solar;

import java.util.List;
import java.util.ArrayList;

public class Solar {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private List<Planet> planets;

// ///////////////////////////////////////////// constructor

  public Solar() {
    planets = new ArrayList<Planet>();
    planets.add(new Planet(Planet.MARS));
  }

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter

  public List<Planet> getPlanets() {
    return planets;
  }

  public void setPlanets(List<Planet> planets) {
    this.planets = planets;
  }
}

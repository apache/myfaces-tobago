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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class AstroDataUnitTest {

  //  @Inject // todo
  private AstroData astroData = new AstroData();

  @Test
  public void testTerrestrialPlanets() {
    final List<SelectItem> terrestrialPlanets = astroData.getTerrestrialPlanets();
    Assertions.assertEquals(4, terrestrialPlanets.size());
    Assertions.assertEquals("Mercury", terrestrialPlanets.get(0).getValue().toString());
  }

  @Test
  public void testGiantPlanets() {
    final List<SelectItem> giantPlanets = astroData.getGiantPlanets();
    Assertions.assertEquals(4, giantPlanets.size());
    Assertions.assertEquals("Jupiter", giantPlanets.get(0).getValue().toString());
  }

  @Test
  public void testOrbits() {
    final Map<String, SolarObject> all = astroData.findAllAsMap();
    for (SolarObject solarObject : all.values()) {
      // every orbit must be inside the list
      final String orbit = solarObject.getOrbit();
      Assertions.assertTrue(orbit.equals("-") || all.containsKey(orbit));
    }
  }
}

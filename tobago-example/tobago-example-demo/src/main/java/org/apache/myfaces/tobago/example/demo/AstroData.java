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
import org.apache.myfaces.tobago.model.SelectItem;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Named
public class AstroData {

  private SelectItem[] planets;
  private SelectItem[] terrestrialPlanets;
  private SelectItem[] giantPlanets;

  public AstroData() {
    planets = createSelectItems(SolarObject.findByName(
        "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"));
    terrestrialPlanets = createSelectItems(SolarObject.findByName("Mercury", "Venus", "Earth", "Mars"));
    giantPlanets = createSelectItems(SolarObject.findByName("Jupiter", "Saturn", "Uranus", "Neptune"));
  }

  private SelectItem[] createSelectItems(List<SolarObject> objects) {
    List<SelectItem> list = new ArrayList<>();
    for (SolarObject object : objects) {
      list.add(new SelectItem(object, object.getName()));
    }
    return list.toArray(new SelectItem[list.size()]);
  }

  public SelectItem[] getPlanets() {
    return planets;
  }

  public SelectItem[] getTerrestrialPlanets() {
    return terrestrialPlanets;
  }

  public SelectItem[] getGiantPlanets() {
    return giantPlanets;
  }

  public String namesFromArray(SolarObject[] objects) {
    if (objects == null) {
      return null;
    } else {
      StringBuilder builder = new StringBuilder();
      for (SolarObject object : objects) {
        builder.append(object.getName());
        builder.append(", ");
      }
      if (builder.length() >= 2) {
        builder.delete(builder.length() - 2, builder.length());
      }
      return builder.toString();
    }
  }
}

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

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Named
public class SelectManyCheckboxController implements Serializable {

  private List<String> animals = new ArrayList<>();
  private List<String> numbers = new ArrayList<>();

  private SolarObject[] selectedTerrestrialPlanet;
  private SolarObject[] selectedGiantPlanet;

  public List getAnimals() {
    return animals;
  }

  public void setAnimals(final List<String> animals) {
    this.animals = animals;
  }

  public String getAnimal() {
    String retValue = "";
    for (final String s : animals) {
      retValue = retValue.concat(s + " ");
    }
    return retValue;
  }

  public List<String> getNumbers() {
    return numbers;
  }

  public void setNumbers(final List<String> numbers) {
    this.numbers = numbers;
  }

  public int getResult() {
    int result = 0;
    for (final String number : numbers) {
      result += Integer.valueOf(number);
    }
    return result;
  }

  public SolarObject[] getSelectedTerrestrialPlanet() {
    return selectedTerrestrialPlanet;
  }

  public void setSelectedTerrestrialPlanet(SolarObject[] selectedTerrestrialPlanet) {
    this.selectedTerrestrialPlanet = selectedTerrestrialPlanet;
  }

  public SolarObject[] getSelectedGiantPlanet() {
    return selectedGiantPlanet;
  }

  public void setSelectedGiantPlanet(SolarObject[] selectedGiantPlanet) {
    this.selectedGiantPlanet = selectedGiantPlanet;
  }
}

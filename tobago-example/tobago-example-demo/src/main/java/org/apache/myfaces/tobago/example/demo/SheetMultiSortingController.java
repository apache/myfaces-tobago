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

import org.apache.myfaces.tobago.model.SheetState;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SessionScoped
@Named
public class SheetMultiSortingController implements Serializable {

  private static final String[] NAMES = {"Avery", "James", "Riley"};
  private static final String[] SPECIES = {"Cat", "Goat", "Pig"};
  private static final String[] GENDER = {"male", "female"};
  private final List<Animal> animalList;
  private final SheetState sheetState;

  public SheetMultiSortingController() {
    animalList = new ArrayList<>();

    int age = 10;

    for (final String name : NAMES) {
      for (final String species : SPECIES) {
        for (String gender : GENDER) {
          animalList.add(new Animal(name, species, gender, age));
          age++;
        }
      }
    }

    sheetState = new SheetState(3);
    shuffle();
  }

  public void shuffle() {
    Collections.shuffle(animalList);
    sheetState.resetSortState();
  }

  public void sortByApi() {
    sheetState.resetSortState();
    sheetState.updateSortState("age");
    sheetState.updateSortState("species");
    sheetState.updateSortState("species");
    sheetState.updateSortState("gender");
  }

  public SheetState getSheetState() {
    return sheetState;
  }

  public List<Animal> getAnimalList() {
    return animalList;
  }

  public static class Animal {
    private String name;
    private String species;
    private String gender;
    private int age;

    public Animal(String name, String species, String gender, int age) {
      this.name = name;
      this.species = species;
      this.gender = gender;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getSpecies() {
      return species;
    }

    public void setSpecies(String species) {
      this.species = species;
    }

    public String getGender() {
      return gender;
    }

    public void setGender(String gender) {
      this.gender = gender;
    }

    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }
}

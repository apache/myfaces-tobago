/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 20.09.2002 at 10:40:44.
 * $Id: Solar.java 1271 2005-08-08 20:44:11 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package com.atanion.tobago.demo.model.solar;

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

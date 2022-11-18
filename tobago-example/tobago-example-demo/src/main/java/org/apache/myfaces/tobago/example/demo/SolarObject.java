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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.event.AjaxBehaviorEvent;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SolarObject implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String name;

  private String number;

  private String orbit;

  private Integer distance;

  private Double period;

  private Double incl;

  private Double eccen;

  private String discoverer;

  private Integer discoverYear;

  private String population;

  private List<Element> chemicalComposition;

  private SolarType type;

  public SolarObject(
      final String name, final String number, final String orbit, final Integer distance, final Double period,
      final Double incl, final Double eccen, final String discoverer, final Integer discoverYear) {
    this.name = name;
    this.number = number;
    this.orbit = orbit;
    this.distance = distance;
    this.period = period;
    this.incl = incl;
    this.eccen = eccen;
    this.discoverer = discoverer;
    this.discoverYear = discoverYear;
    this.population = "Earth".equals(name) ? "~ 8.000.000.000" : "0";
  }

  public SolarObject(final SolarObject solarObject) {
    this.name = solarObject.getName();
    this.number = solarObject.getNumber();
    this.orbit = solarObject.getOrbit();
    this.distance = solarObject.getDistance();
    this.period = solarObject.getPeriod();
    this.incl = solarObject.getIncl();
    this.eccen = solarObject.getEccen();
    this.discoverer = solarObject.getDiscoverer();
    this.discoverYear = solarObject.getDiscoverYear();
    this.population = getPopulation();
    this.chemicalComposition = chemicalComposition != null
        ? chemicalComposition.stream().map(Element::new).collect(Collectors.toList())
        : null;
  }

  public String getName() {
    return name;
  }

  public void update(final AjaxBehaviorEvent event) {
    LOG.info("AjaxBehaviorEvent called. New value: '{}' event: {}", name, event);
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getMarkup() {
    if (name.equals("Sun")) {
      return "sun";
    } else {
      if (orbit.equals("Sun")) {
        return "planet";
      } else {
        return "moon";
      }
    }
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(final String number) {
    this.number = number;
  }

  public String getOrbit() {
    return orbit;
  }

  public void setOrbit(final String orbit) {
    this.orbit = orbit;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(final Integer distance) {
    this.distance = distance;
  }

  public Double getPeriod() {
    return period;
  }

  public void setPeriod(final Double period) {
    this.period = period;
  }

  public Double getIncl() {
    return incl;
  }

  public void setIncl(final Double incl) {
    this.incl = incl;
  }

  public Double getEccen() {
    return eccen;
  }

  public void setEccen(final Double eccen) {
    this.eccen = eccen;
  }

  public String getDiscoverer() {
    return discoverer;
  }

  public void setDiscoverer(final String discoverer) {
    this.discoverer = discoverer;
  }

  public Integer getDiscoverYear() {
    return discoverYear;
  }

  public void setDiscoverYear(final Integer discoverYear) {
    this.discoverYear = discoverYear;
  }

  public String getPopulation() {
    return population;
  }

  public void setPopulation(final String population) {
    this.population = population;
  }

  public List<Element> getChemicalComposition() {
    return chemicalComposition != null ? chemicalComposition : Collections.emptyList();
  }

  public void setChemicalComposition(final List<Element> chemicalComposition) {
    this.chemicalComposition = chemicalComposition;
  }

  public SolarType getType() {
    if (type == null) {
      type = name.equals("Sun") ? SolarType.STAR : orbit.equals("Sun") ? SolarType.PLANET : SolarType.MOON;
    }
    return type;
  }

  public String toString() {
    return name;
  }
}

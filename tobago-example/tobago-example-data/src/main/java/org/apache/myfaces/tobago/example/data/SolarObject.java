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

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolarObject {

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
    this.population = "Earth".equals(name) ? "ca. 6.800.000.000" : "0";
  }

  public String getName() {
    return name;
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
    return chemicalComposition != null ? chemicalComposition : Collections.<Element>emptyList();
  }

  public void setChemicalComposition(final List<Element> chemicalComposition) {
    this.chemicalComposition = chemicalComposition;
  }

  public String toString() {
    return name;
  }

  public boolean isSelectionDisabled() {
    return number.equals("II");
  }

  public static SolarObject[] getArray() {
    return DATA;
  }

  public static List<SolarObject> getList() {
    final SolarObject[] array = getArray();
    final List<SolarObject> list = new ArrayList<SolarObject>(array.length);
    Collections.addAll(list, array);
    return list;
  }

  public static DefaultMutableTreeNode getTree() {
    final SolarObject[] array = getArray();
    final Map<String, DefaultMutableTreeNode> cache = new HashMap<String, DefaultMutableTreeNode>();
    for (final SolarObject solar : array) {
      final DefaultMutableTreeNode node = new DefaultMutableTreeNode(solar);
      cache.put(solar.getName(), node);
      final String orbitName = solar.getOrbit();
      if (orbitName.equals("-")) {
        continue;
      }
      // adds a solar object as node to its orbit as tree child.
      cache.get(orbitName).add(node);
    }
    return cache.get("Sun");
  }

  public static List<SolarObject> getSatellites(final String center) {
    final List<SolarObject> collect = new ArrayList<SolarObject>();
    final SolarObject[] all = getArray();
    for (final SolarObject anAll : all) {
      if (anAll.getOrbit().equals(center)) {
        collect.add(anAll);
      }
    }
    return collect;
  }

  // TODO: optimize
  public static SolarObject find(String name) {
    for (SolarObject solarObject : DATA) {
      if (solarObject.getName().equals(name)) {
        return solarObject;
      }
    }
    return null;
  }


  public static final SolarObject SUN = new SolarObject("Sun", "-", "-", 0, 0.0, 0.0, 0.0, "-", null);
  public static final SolarObject EARTH =new SolarObject("Earth", "III", "Sun", 149600, 365.26, 0.00, 0.02, "-", null);
  public static final SolarObject MOON = new SolarObject("Moon", "I", "Earth", 384, 27.32, 5.14, 0.05, "-", null);

  public static final SolarObject[] DATA = {
      SUN,
      new SolarObject("Mercury", "I", "Sun", 57910, 87.97, 7.00, 0.21, "-", null),
      new SolarObject("Venus", "II", "Sun", 108200, 224.70, 3.39, 0.01, "-", null),
      EARTH,
      new SolarObject("Mars", "IV", "Sun", 227940, 686.98, 1.85, 0.09, "-", null),
      new SolarObject("Jupiter", "V", "Sun", 778330, 4332.71, 1.31, 0.05, "-", null),
      new SolarObject("Saturn", "VI", "Sun", 1429400, 10759.50, 2.49, 0.06, "-", null),
      new SolarObject("Uranus", "VII", "Sun", 2870990, 30685.0, 0.77, 0.05, "Herschel", 1781),
      new SolarObject("Neptune", "VIII", "Sun", 4504300, 60190.0, 1.77, 0.01, "Adams", 1846),
      new SolarObject("Pluto", "IX", "Sun", 5913520, 90800.0, 17.15, 0.25, "Tombaugh", 1930),
      MOON,
      new SolarObject("Phobos", "I", "Mars", 9, 0.32, 1.00, 0.02, "Hall", 1877),
      new SolarObject("Deimos", "II", "Mars", 23, 1.26, 1.80, 0.00, "Hall", 1877),
      new SolarObject("Metis", "XVI", "Jupiter", 128, 0.29, 0.00, 0.00, "Synnott", 1979),
      new SolarObject("Adrastea", "XV", "Jupiter", 129, 0.30, 0.00, 0.00, "Jewitt", 1979),
      new SolarObject("Amalthea", "V", "Jupiter", 181, 0.50, 0.40, 0.00, "Barnard", 1892),
      new SolarObject("Thebe", "XIV", "Jupiter", 222, 0.67, 0.80, 0.02, "Synnott", 1979),
      new SolarObject("Io", "I", "Jupiter", 422, 1.77, 0.04, 0.00, "Galileo", 1610),
      new SolarObject("Europa", "II", "Jupiter", 671, 3.55, 0.47, 0.01, "Galileo", 1610),
      new SolarObject("Ganymede", "III", "Jupiter", 1070, 7.15, 0.19, 0.00, "Galileo", 1610),
      new SolarObject("Callisto", "IV", "Jupiter", 1883, 16.69, 0.28, 0.01, "Galileo", 1610),
      new SolarObject("Themisto", "XVIII", "Jupiter", 7507, 130.02, null, null, "Sheppard", 2000),
      new SolarObject("Leda", "XIII", "Jupiter", 11094, 238.72, 27.00, 0.15, "Kowal", 1974),
      new SolarObject("Himalia", "VI", "Jupiter", 11480, 250.57, 28.00, 0.16, "Perrine", 1904),
      new SolarObject("Lysithea", "X", "Jupiter", 11720, 259.22, 29.00, 0.11, "Nicholson", 1938),
      new SolarObject("Elara", "VII", "Jupiter", 11737, 259.65, 28.00, 0.21, "Perrine", 1905),
      new SolarObject("Ananke", "XII", "Jupiter", 21200, -629.770, 147.00, 0.17, "Nicholson", 1951),
      new SolarObject("Carme", "XI", "Jupiter", 22600, -702.30, 163.00, 0.21, "Nicholson", 1938),
      new SolarObject("Pasiphae", "VIII", "Jupiter", 23500, -708.0, 147.00, 0.38, "Melotte", 1908),
      new SolarObject("Sinope", "IX", "Jupiter", 23700, -758.9, 153.00, 0.28, "Nicholson", 1914),
      new SolarObject("Iocaste", "XXIV", "Jupiter", 20216, 631.5, null, null, "Sheppard", 2000),
      new SolarObject("Harpalyke", "XXII", "Jupiter", 21132, 623.3, null, null, "Sheppard", 2000),
      new SolarObject("Praxidike", "XXVII", "Jupiter", 20964, 625.3, null, null, "Sheppard", 2000),
      new SolarObject("Taygete", "XX", "Jupiter", 23312, 732.2, null, null, "Sheppard", 2000),
      new SolarObject("Chaldene", "XXI", "Jupiter", 23387, 723.8, null, null, "Sheppard", 2000),
      new SolarObject("Kalyke", "XXIII", "Jupiter", 23745, 743.0, null, null, "Sheppard", 2000),
      new SolarObject("Callirrhoe", "XVII", "Jupiter", 24100, 758.8, null, null, "Sheppard", 2000),
      new SolarObject("Megaclite", "XIX", "Jupiter", 23911, 752.8, null, null, "Sheppard", 2000),
      new SolarObject("Isonoe", "XXVI", "Jupiter", 23078, 725.5, null, null, "Sheppard", 2000),
      new SolarObject("Erinome", "XXV", "Jupiter", 23168, 728.3, null, null, "Sheppard", 2000),
      new SolarObject("Pan", "XVIII", "Saturn", 134, 0.58, 0.00, 0.00, "Showalter", 1990),
      new SolarObject("Atlas", "XV", "Saturn", 138, 0.60, 0.00, 0.00, "Terrile", 1980),
      new SolarObject("Prometheus", "XVI", "Saturn", 139, 0.61, 0.00, 0.00, "Collins", 1980),
      new SolarObject("Pandora", "XVII", "Saturn", 142, 0.63, 0.00, 0.00, "Collins", 1980),
      new SolarObject("Epimetheus", "XI", "Saturn", 151, 0.69, 0.34, 0.01, "Walker", 1980),
      new SolarObject("Janus", "X", "Saturn", 151, 0.69, 0.14, 0.01, "Dollfus", 1966),
      new SolarObject("Mimas", "I", "Saturn", 186, 0.94, 1.53, 0.02, "Herschel", 1789),
      new SolarObject("Enceladus", "II", "Saturn", 238, 1.37, 0.02, 0.00, "Herschel", 1789),
      new SolarObject("Tethys", "III", "Saturn", 295, 1.89, 1.09, 0.00, "Cassini", 1684),
      new SolarObject("Telesto", "XIII", "Saturn", 295, 1.89, 0.00, 0.00, "Smith", 1980),
      new SolarObject("Calypso", "XIV", "Saturn", 295, 1.89, 0.00, 0.00, "Pascu", 1980),
      new SolarObject("Dione", "IV", "Saturn", 377, 2.74, 0.02, 0.00, "Cassini", 1684),
      new SolarObject("Helene", "XII", "Saturn", 377, 2.74, 0.20, 0.01, "Laques", 1980),
      new SolarObject("Rhea", "V", "Saturn", 527, 4.52, 0.35, 0.00, "Cassini", 1672),
      new SolarObject("Titan", "VI", "Saturn", 1222, 15.95, 0.33, 0.03, "Huygens", 1655),
      new SolarObject("Hyperion", "VII", "Saturn", 1481, 21.28, 0.43, 0.10, "Bond", 1848),
      new SolarObject("Iapetus", "VIII", "Saturn", 3561, 79.33, 14.72, 0.03, "Cassini", 1671),
      new SolarObject("Phoebe", "IX", "Saturn", 12952, -550.48, 175.30, 0.16, "Pickering", 1898),
      new SolarObject("Cordelia", "VI", "Uranus", 50, 0.34, 0.14, 0.00, "Voyager 2", 1986),
      new SolarObject("Ophelia", "VII", "Uranus", 54, 0.38, 0.09, 0.00, "Voyager 2", 1986),
      new SolarObject("Bianca", "VIII", "Uranus", 59, 0.43, 0.16, 0.00, "Voyager 2", 1986),
      new SolarObject("Cressida", "IX", "Uranus", 62, 0.46, 0.04, 0.00, "Voyager 2", 1986),
      new SolarObject("Desdemona", "X", "Uranus", 63, 0.47, 0.16, 0.00, "Voyager 2", 1986),
      new SolarObject("Juliet", "XI", "Uranus", 64, 0.49, 0.06, 0.00, "Voyager 2", 1986),
      new SolarObject("Portia", "XII", "Uranus", 66, 0.51, 0.09, 0.00, "Voyager 2", 1986),
      new SolarObject("Rosalind", "XIII", "Uranus", 70, 0.56, 0.28, 0.00, "Voyager 2", 1986),
      new SolarObject("Belinda", "XIV", "Uranus", 75, 0.62, 0.03, 0.00, "Voyager 2", 1986),
      new SolarObject("1986U10", "", "Uranus", 76, 0.64, null, null, "Karkoschka", 1999),
      new SolarObject("Puck", "XV", "Uranus", 86, 0.76, 0.31, 0.00, "Voyager 2", 1985),
      new SolarObject("Miranda", "V", "Uranus", 130, 1.41, 4.22, 0.00, "Kuiper", 1948),
      new SolarObject("Ariel", "I", "Uranus", 191, 2.52, 0.00, 0.00, "Lassell", 1851),
      new SolarObject("Umbriel", "II", "Uranus", 266, 4.14, 0.00, 0.00, "Lassell", 1851),
      new SolarObject("Titania", "III", "Uranus", 436, 8.71, 0.00, 0.00, "Herschel", 1787),
      new SolarObject("Oberon", "IV", "Uranus", 583, 13.46, 0.00, 0.00, "Herschel", 1787),
      new SolarObject("Caliban", "XVI", "Uranus", 7169, -579.39, 140., 0.08, "Gladman", 1997),
      new SolarObject("Stephano", "XX", "Uranus", 7948, -677.48, 143., 0.24, "Gladman", 1999),
      new SolarObject("Sycorax", "XVII", "Uranus", 12213, -1283.48, 153., 0.51, "Nicholson", 1997),
      new SolarObject("Prospero", "XVIII", "Uranus", 16568, -1962.95, 152., 0.44, "Holman", 1999),
      new SolarObject("Setebos", "XIX", "Uranus", 17681, -2196.35, 158., 0.57, "Kavelaars", 1999),
      new SolarObject("Naiad", "III", "Neptune", 48, 0.29, 0.00, 0.00, "Voyager 2", 1989),
      new SolarObject("Thalassa", "IV", "Neptune", 50, 0.31, 4.50, 0.00, "Voyager 2", 1989),
      new SolarObject("Despina", "V", "Neptune", 53, 0.33, 0.00, 0.00, "Voyager 2", 1989),
      new SolarObject("Galatea", "VI", "Neptune", 62, 0.43, 0.00, 0.00, "Voyager 2", 1989),
      new SolarObject("Larissa", "VII", "Neptune", 74, 0.55, 0.00, 0.00, "Reitsema", 1989),
      new SolarObject("Proteus", "VIII", "Neptune", 118, 1.12, 0.00, 0.00, "Voyager 2", 1989),
      new SolarObject("Triton", "I", "Neptune", 355, -5.88, 157.00, 0.00, "Lassell", 1846),
      new SolarObject("Nereid", "II", "Neptune", 5513, 360.13, 29.00, 0.75, "Kuiper", 1949),
      new SolarObject("Charon", "I", "Pluto", 20, 6.39, 98.80, 0.00, "Christy", 1978)
  };

  static {
    List<Element> sun = Arrays.asList(
        new Element("Hydrogen", 0.74),
        new Element("Helium", 0.25)
    );
    SUN.setChemicalComposition(sun);
    List<Element> earth = Arrays.asList(
        new Element("Silica", 0.60),
        new Element("Alimina", 0.15),
        new Element("Lime", 0.05)
    );
    EARTH.setChemicalComposition(earth);
    List<Element> moon = Arrays.asList(
        new Element("Silica", 0.45),
        new Element("Alimina", 0.24),
        new Element("Lime", 0.16)
    );
    MOON.setChemicalComposition(moon);
  }


}

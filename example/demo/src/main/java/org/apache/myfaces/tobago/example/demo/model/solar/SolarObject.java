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

package org.apache.myfaces.tobago.example.demo.model.solar;

/**
 * User: weber
 * Date: Nov 5, 2002
 * Time: 7:15:14 PM
 */

import java.util.ArrayList;
import java.util.List;

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

  public SolarObject(String[] strings) {
    this.name = strings[0];
    this.number = strings[1];
    this.orbit = strings[2];
    try {
      this.distance = Integer.valueOf(strings[3]);
    } catch (NumberFormatException e) {
      new Integer(0);
    }
    try {
      this.period = Double.valueOf(strings[4]);
    } catch (NumberFormatException e) {
      new Double(0);
    }
    try {
      this.incl = Double.valueOf(strings[5]);
    } catch (NumberFormatException e) {
      new Double(0);
    }
    try {
      this.eccen = Double.valueOf(strings[6]);
    } catch (NumberFormatException e) {
      new Double(0);
    }
    this.discoverer = strings[7];
    try {
      this.discoverYear = Integer.valueOf(strings[8]);
    } catch (NumberFormatException e) {
      new Integer(0);
    }
    if (strings[0].equals("Earth")) {
      population = "ca.5500000000";
    } else {
      population = "0";
    }
  }

  public String getName() {
    return name;
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

  public void setName(String name) {
    this.name = name;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getOrbit() {
    return orbit;
  }

  public void setOrbit(String orbit) {
    this.orbit = orbit;
  }

  public Integer getDistance() {
    return distance;
  }

  public void setDistance(Integer distance) {
    this.distance = distance;
  }

  public Double getPeriod() {
    return period;
  }

  public void setPeriod(Double period) {
    this.period = period;
  }

  public Double getIncl() {
    return incl;
  }

  public void setIncl(Double incl) {
    this.incl = incl;
  }

  public Double getEccen() {
    return eccen;
  }

  public void setEccen(Double eccen) {
    this.eccen = eccen;
  }

  public String getDiscoverer() {
    return discoverer;
  }

  public void setDiscoverer(String discoverer) {
    this.discoverer = discoverer;
  }

  public Integer getDiscoverYear() {
    return discoverYear;
  }

  public void setDiscoverYear(Integer discoverYear) {
    this.discoverYear = discoverYear;
  }

  public String getPopulation() {
    return population;
  }

  public void setPopulation(String population) {
    this.population = population;
  }

  public boolean isSelectionDisabled() {
    return number.equals("II");
  }

  public static SolarObject[] getArray() {
    SolarObject[] array = new SolarObject[STRINGS.length];
    for (int i = 0; i < array.length; i++) {
      array[i] = new SolarObject(STRINGS[i]);
    }
    return array;
  }


  public static List<SolarObject> getList() {
    SolarObject[] array = getArray();
    List<SolarObject> list = new ArrayList<SolarObject>(array.length);
    for (SolarObject object : array) {
      list.add(object);
    }
    return list;
  }

    public static List<SolarObject> getSatellites(String center) {
    List<SolarObject> collect = new ArrayList<SolarObject>();
    SolarObject[] all = getArray();
    for (SolarObject anAll : all) {
      if (anAll.getOrbit().equals(center)) {
        collect.add(anAll);
      }
    }
    return collect;
  }

  private static final String[][] STRINGS =
        {
          {"Sun",         "-",          "-",        "0",     "0",       "0",     "0",             "-",       "0"},
          {"Mercury",    "I",         "Sun",    "57910",    "87.97",    "7.00",  "0.21",          "-",       "-"},
          {"Venus",      "II",        "Sun",   "108200",   "224.70",    "3.39",  "0.01",          "-",       "-"},
          {"Earth",      "III",       "Sun",   "149600",   "365.26",    "0.00",  "0.02",          "-",       "-"},
          {"Mars",       "IV",        "Sun",   "227940",   "686.98",    "1.85",  "0.09",          "-",       "-"},
          {"Jupiter",    "V",         "Sun",   "778330",  "4332.71",    "1.31",  "0.05",          "-",       "-"},
          {"Saturn",     "VI",        "Sun",  "1429400", "10759.50",    "2.49",  "0.06",          "-",       "-"},
          {"Uranus",     "VII",       "Sun",  "2870990", "30685.00",    "0.77",  "0.05",   "Herschel",    "1781"},
          {"Neptune",    "VIII",      "Sun",  "4504300", "60190.00",    "1.77",  "0.01",      "Adams",    "1846"},
          {"Pluto",      "IX",        "Sun",  "5913520", "90800",      "17.15",  "0.25",   "Tombaugh",    "1930"},
          {"Moon",       "I",       "Earth",      "384",    "27.32",    "5.14",  "0.05",          "-",       "-"},
          {"Phobos",     "I",        "Mars",        "9",     "0.32",    "1.00",  "0.02",       "Hall",    "1877"},
          {"Deimos",     "II",       "Mars",       "23",     "1.26",    "1.80",  "0.00",       "Hall",    "1877"},
          {"Metis",      "XVI",   "Jupiter",      "128",     "0.29",    "0.00",  "0.00",    "Synnott",    "1979"},
          {"Adrastea",   "XV",    "Jupiter",      "129",     "0.30",    "0.00",  "0.00",     "Jewitt",    "1979"},
          {"Amalthea",   "V",     "Jupiter",      "181",     "0.50",    "0.40",  "0.00",    "Barnard",    "1892"},
          {"Thebe",      "XIV",   "Jupiter",      "222",     "0.67",    "0.80",  "0.02",    "Synnott",    "1979"},
          {"Io",         "I",     "Jupiter",      "422",     "1.77",    "0.04",  "0.00",    "Galileo",    "1610"},
          {"Europa",     "II",    "Jupiter",      "671",     "3.55",    "0.47",  "0.01",    "Galileo",    "1610"},
          {"Ganymede",   "III",   "Jupiter",     "1070",     "7.15",    "0.19",  "0.00",    "Galileo",    "1610"},
          {"Callisto",   "IV",    "Jupiter",     "1883",    "16.69",    "0.28",  "0.01",    "Galileo",    "1610"},
          {"Themisto",   "XVIII", "Jupiter",     "7507",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Leda",       "XIII",  "Jupiter",    "11094",   "238.72",   "27.00",  "0.15",      "Kowal",    "1974"},
          {"Himalia",    "VI",    "Jupiter",    "11480",   "250.57",   "28.00",  "0.16",    "Perrine",    "1904"},
          {"Lysithea",   "X",     "Jupiter",    "11720",   "259.22",   "29.00",  "0.11",  "Nicholson",    "1938"},
          {"Elara",      "VII",   "Jupiter",    "11737",   "259.65",   "28.00",  "0.21",    "Perrine",    "1905"},
          {"Ananke",     "XII",   "Jupiter",    "21200",  "-631",     "147.00",  "0.17",  "Nicholson",    "1951"},
          {"Carme",      "XI",    "Jupiter",    "22600",  "-692",     "163.00",  "0.21",  "Nicholson",    "1938"},
          {"Pasiphae",   "VIII",  "Jupiter",    "23500",  "-735",     "147.00",  "0.38",    "Melotte",    "1908"},
          {"Sinope",     "IX",    "Jupiter",    "23700",  "-758",     "153.00",  "0.28",  "Nicholson",    "1914"},
          {"Iocaste",    "XXIV",  "Jupiter",    "20216",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Harpalyke",  "XXII",  "Jupiter",    "21132",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Praxidike",  "XXVII", "Jupiter",    "20964",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Taygete",    "XX",    "Jupiter",    "23312",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Chaldene",   "XXI",   "Jupiter",    "23387",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Kalyke",     "XXIII", "Jupiter",    "23745",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Callirrhoe", "XVII",  "Jupiter",    "24100",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Megaclite",  "XIX",   "Jupiter",    "23911",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Isonoe",     "XXVI",  "Jupiter",    "23078",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Erinome",    "XXV",   "Jupiter",    "23168",     "0",           "",      "",   "Sheppard",    "2000"},
          {"Pan",        "XVIII",  "Saturn",      "134",     "0.58",    "0.00",  "0.00",  "Showalter",    "1990"},
          {"Atlas",      "XV",     "Saturn",      "138",     "0.60",    "0.00",  "0.00",    "Terrile",    "1980"},
          {"Prometheus", "XVI",    "Saturn",      "139",     "0.61",    "0.00",  "0.00",    "Collins",    "1980"},
          {"Pandora",    "XVII",   "Saturn",      "142",     "0.63",    "0.00",  "0.00",    "Collins",    "1980"},
          {"Epimetheus", "XI",     "Saturn",      "151",     "0.69",    "0.34",  "0.01",     "Walker",    "1980"},
          {"Janus",      "X",      "Saturn",      "151",     "0.69",    "0.14",  "0.01",    "Dollfus",    "1966"},
          {"Mimas",      "I",      "Saturn",      "186",     "0.94",    "1.53",  "0.02",   "Herschel",    "1789"},
          {"Enceladus",  "II",     "Saturn",      "238",     "1.37",    "0.02",  "0.00",   "Herschel",    "1789"},
          {"Tethys",     "III",    "Saturn",      "295",     "1.89",    "1.09",  "0.00",    "Cassini",    "1684"},
          {"Telesto",    "XIII",   "Saturn",      "295",     "1.89",    "0.00",  "0.00",      "Smith",    "1980"},
          {"Calypso",    "XIV",    "Saturn",      "295",     "1.89",    "0.00",  "0.00",      "Pascu",    "1980"},
          {"Dione",      "IV",     "Saturn",      "377",     "2.74",    "0.02",  "0.00",    "Cassini",    "1684"},
          {"Helene",     "XII",    "Saturn",      "377",     "2.74",    "0.20",  "0.01",     "Laques",    "1980"},
          {"Rhea",       "V",      "Saturn",      "527",     "4.52",    "0.35",  "0.00",    "Cassini",    "1672"},
          {"Titan",      "VI",     "Saturn",     "1222",    "15.95",    "0.33",  "0.03",    "Huygens",    "1655"},
          {"Hyperion",   "VII",    "Saturn",     "1481",    "21.28",    "0.43",  "0.10",       "Bond",    "1848"},
          {"Iapetus",    "VIII",   "Saturn",     "3561",    "79.33",   "14.72",  "0.03",    "Cassini",    "1671"},
          {"Phoebe",     "IX",     "Saturn",    "12952",  "-550.48",  "175.30",  "0.16",  "Pickering",    "1898"},
          {"Cordelia",   "VI",     "Uranus",       "50",     "0.34",    "0.14",  "0.00",  "Voyager 2",    "1986"},
          {"Ophelia",    "VII",    "Uranus",       "54",     "0.38",    "0.09",  "0.00",  "Voyager 2",    "1986"},
          {"Bianca",     "VIII",   "Uranus",       "59",     "0.43",    "0.16",  "0.00",  "Voyager 2",    "1986"},
          {"Cressida",   "IX",     "Uranus",       "62",     "0.46",    "0.04",  "0.00",  "Voyager 2",    "1986"},
          {"Desdemona",  "X",      "Uranus",       "63",     "0.47",    "0.16",  "0.00",  "Voyager 2",    "1986"},
          {"Juliet",     "XI",     "Uranus",       "64",     "0.49",    "0.06",  "0.00",  "Voyager 2",    "1986"},
          {"Portia",     "XII",    "Uranus",       "66",     "0.51",    "0.09",  "0.00",  "Voyager 2",    "1986"},
          {"Rosalind",   "XIII",   "Uranus",       "70",     "0.56",    "0.28",  "0.00",  "Voyager 2",    "1986"},
          {"Belinda",    "XIV",    "Uranus",       "75",     "0.62",    "0.03",  "0.00",  "Voyager 2",    "1986"},
          {"1986U10",    "",       "Uranus",       "76",     "0.64",        "",      "", "Karkoschka",    "1999"},
          {"Puck",       "XV",     "Uranus",       "86",     "0.76",    "0.31",  "0.00",  "Voyager 2",    "1985"},
          {"Miranda",    "V",      "Uranus",      "130",     "1.41",    "4.22",  "0.00",     "Kuiper",    "1948"},
          {"Ariel",      "I",      "Uranus",      "191",     "2.52",    "0.00",  "0.00",    "Lassell",    "1851"},
          {"Umbriel",    "II",     "Uranus",      "266",     "4.14",    "0.00",  "0.00",    "Lassell",    "1851"},
          {"Titania",    "III",    "Uranus",      "436",     "8.71",    "0.00",  "0.00",   "Herschel",    "1787"},
          {"Oberon",     "IV",     "Uranus",      "583",    "13.46",    "0.00",  "0.00",   "Herschel",    "1787"},
          {"Caliban",    "XVI",    "Uranus",     "7169",  "-580",     "140.",    "0.08",    "Gladman",    "1997"},
          {"Stephano",   "XX",     "Uranus",     "7948",  "-674",     "143.",    "0.24",    "Gladman",    "1999"},
          {"Sycorax",    "XVII" ,  "Uranus",    "12213", "-1289",     "153.",    "0.51",  "Nicholson",    "1997"},
          {"Prospero",   "XVIII",  "Uranus",    "16568", "-2019",     "152.",    "0.44",     "Holman",    "1999"},
          {"Setebos",    "XIX",    "Uranus",    "17681", "-2239",     "158.",    "0.57",  "Kavelaars",    "1999"},
          {"Naiad",      "III",   "Neptune",       "48",     "0.29",    "0.00",  "0.00",  "Voyager 2",    "1989"},
          {"Thalassa",   "IV",    "Neptune",       "50",     "0.31",    "4.50",  "0.00",  "Voyager 2",    "1989"},
          {"Despina",    "V",     "Neptune",       "53",     "0.33",    "0.00",  "0.00",  "Voyager 2",    "1989"},
          {"Galatea",    "VI",    "Neptune",       "62",     "0.43",    "0.00",  "0.00",  "Voyager 2",    "1989"},
          {"Larissa",    "VII",   "Neptune",       "74",     "0.55",    "0.00",  "0.00",  "Reitsema",     "1989"},
          {"Proteus",    "VIII",  "Neptune",      "118",     "1.12",    "0.00",  "0.00",  "Voyager 2",    "1989"},
          {"Triton",     "I",     "Neptune",      "355",    "-5.88",  "157.00",  "0.00",    "Lassell",    "1846"},
          {"Nereid",     "II",    "Neptune",     "5513",   "360.13",   "29.00",  "0.75",     "Kuiper",    "1949"},
          {"Charon",     "I",       "Pluto",       "20",     "6.39",   "98.80",  "0.00",    "Christy",    "1978"}
        };

}

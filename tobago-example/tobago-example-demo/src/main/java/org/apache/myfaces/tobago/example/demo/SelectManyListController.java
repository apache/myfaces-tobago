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

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class SelectManyListController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  private List<SolarObject> planets;
  private SolarObject[] selected1 = new SolarObject[0];
  private SolarObject[] selected2 = new SolarObject[0];
  private SolarObject[] selected3 = new SolarObject[0];
  private SolarObject[] selected4 = new SolarObject[0];
  private List<String> names;
  private String[] selected5 = new String[0];

  private String filterType;

  @PostConstruct
  public void init() {
    planets = astroData.getSatellites("Sun");

    names = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        Thread.currentThread().getContextClassLoader().getResourceAsStream(
            "org/apache/myfaces/tobago/example/demo/names.txt"), StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (!line.startsWith("#") && line.length() > 0) {
          names.add(line);
        }
      }
    } catch (Exception e) {
      LOG.error("Can't load names", e);
    }
  }

  public List<SolarObject> getPlanets() {
    return planets;
  }

  public SolarObject[] getSelected1() {
    return selected1;
  }

  public void setSelected1(SolarObject[] selected1) {
    this.selected1 = selected1;
  }

  public SolarObject[] getSelected2() {
    return selected2;
  }

  public void setSelected2(SolarObject[] selected2) {
    this.selected2 = selected2;
  }

  public SolarObject[] getSelected3() {
    return selected3;
  }

  public void setSelected3(SolarObject[] selected3) {
    this.selected3 = selected3;
  }

  public SolarObject[] getSelected4() {
    return selected4;
  }

  public void setSelected4(SolarObject[] selected4) {
    this.selected4 = selected4;
  }

  public List<String> getNames() {
    if (names.size() < 1) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Names not loaded!", null));
      return new ArrayList<>();
    }
    return names;
  }

  public String[] getSelected5() {
    return selected5;
  }

  public void setSelected5(String[] selected5) {
    this.selected5 = selected5;
  }

  public String getFilterType() {
    return filterType;
  }

  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }
}

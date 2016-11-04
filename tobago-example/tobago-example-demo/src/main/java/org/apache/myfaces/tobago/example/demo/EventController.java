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
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SessionScoped
@Named
public class EventController implements Serializable {

  private int action = 0;
  private int actionListener = 0;
  private int ajaxListener = 0;
  private int valueChangeListener = 0;
  private List<SolarObject> planets = new ArrayList<SolarObject>();
  private String selectedPlanet;

  public EventController() {
    planets.add(new SolarObject("Mercury", "I", "Sun", 57910, 87.97, 7.00, 0.21, "-", null));
    planets.add(new SolarObject("Venus", "II", "Sun", 108200, 224.70, 3.39, 0.01, "-", null));
    planets.add(new SolarObject("Earth", "III", "Sun", 149600, 365.26, 0.00, 0.02, "-", null));
    planets.add(new SolarObject("Mars", "IV", "Sun", 227940, 686.98, 1.85, 0.09, "-", null));
    planets.add(new SolarObject("Jupiter", "V", "Sun", 778330, 4332.71, 1.31, 0.05, "-", null));
    planets.add(new SolarObject("Saturn", "VI", "Sun", 1429400, 10759.50, 2.49, 0.06, "-", null));
    planets.add(new SolarObject("Uranus", "VII", "Sun", 2870990, 30685.0, 0.77, 0.05, "Herschel", 1781));
    planets.add(new SolarObject("Neptune", "VIII", "Sun", 4504300, 60190.0, 1.77, 0.01, "Adams", 1846));
  }

  public void reset() {
    action = 0;
    actionListener = 0;
    ajaxListener = 0;
    valueChangeListener = 0;
    selectedPlanet = null;
  }

  public void action() {
    action++;
  }

  public void actionListener(final ActionEvent event) {
    actionListener++;
  }

  public void ajaxListener(final AjaxBehaviorEvent event) {
    ajaxListener++;
  }

  public void valueChangeListener(final ValueChangeEvent event) {
    valueChangeListener++;
  }

  public int getActionCount() {
    return action;
  }

  public int getActionListenerCount() {
    return actionListener;
  }

  public int getAjaxListenerCount() {
    return ajaxListener;
  }

  public int getValueChangeListenerCount() {
    return valueChangeListener;
  }

  public long getCurrentTimestamp() {
    return new Date().getTime();
  }

  public List<SolarObject> getPlanets() {
    return planets;
  }

  public String getSelectedPlanet() {
    return selectedPlanet;
  }

  public void setSelectedPlanet(String selectedPlanet) {
    this.selectedPlanet = selectedPlanet;
  }

  public void selectPlanet(final ActionEvent actionEvent) {
    final UIData data = ComponentUtils.findAncestor(actionEvent.getComponent(), UIData.class);
    selectedPlanet = data != null ? ((SolarObject) data.getRowData()).getName() : null;
  }
}

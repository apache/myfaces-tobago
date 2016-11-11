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

import org.apache.myfaces.tobago.component.UIBar;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UIRow;
import org.apache.myfaces.tobago.component.UITextarea;
import org.apache.myfaces.tobago.example.data.SolarObject;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SessionScoped
@Named
public class EventController implements Serializable {

  private List<EventsOnComponent> eventsOnComponents = new ArrayList<EventsOnComponent>();
  private EventsOnComponent selectedComponent;
  private int action = 0;
  private int actionListener = 0;
  private int ajaxListener = 0;
  private int valueChangeListener = 0;
  private List<SolarObject> planets = new ArrayList<SolarObject>();
  private String selectedPlanet;

  public EventController() {
    eventsOnComponents.add(new EventsOnComponent("bar", new UIBar().getEventNames()));
    eventsOnComponents.add(new EventsOnComponent("button", new UIButton().getEventNames()));
    eventsOnComponents.add(new EventsOnComponent("in", new UIIn().getEventNames()));
    eventsOnComponents.add(new EventsOnComponent("row", new UIRow().getEventNames()));
    eventsOnComponents.add(new EventsOnComponent("textarea", new UITextarea().getEventNames()));

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

  public List<EventsOnComponent> getEventsOnComponents() {
    return eventsOnComponents;
  }

  public EventsOnComponent getSelectedComponent() {
    return selectedComponent;
  }

  public void setSelectedComponent(EventsOnComponent selectedComponent) {
    this.selectedComponent = selectedComponent;
  }

  public void selectComponent(final ActionEvent actionEvent) {
    final UIData data = ComponentUtils.findAncestor(actionEvent.getComponent(), UIData.class);
    selectedComponent = data != null ? ((EventsOnComponent) data.getRowData()) : null;
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

  public void selectPlanet(final FacesEvent actionEvent) {
    final UIData data = ComponentUtils.findAncestor(actionEvent.getComponent(), UIData.class);
    selectedPlanet = data != null ? ((SolarObject) data.getRowData()).getName() : null;
  }

  public class EventsOnComponent {
    private String tagName;
    private Set<String> eventNames = new TreeSet<String>();

    public EventsOnComponent(String tagName, Collection<String> eventNames) {
      this.tagName = tagName;
      if (eventNames != null) {
        this.eventNames.addAll(eventNames);
      }
    }

    public String getTagName() {
      return tagName;
    }

    public Set<String> getEventNames() {
      return eventNames;
    }

    public String getChangeEvent() {
      return getCommonEventString(CommonEvent.change);
    }

    public String getClickEvents() {
      return getCommonEventString(CommonEvent.click, CommonEvent.dblclick);
    }

    public String getFocusEvents() {
      return getCommonEventString(CommonEvent.focus, CommonEvent.blur);
    }

    public String getKeyEvents() {
      return getCommonEventString(CommonEvent.keydown, CommonEvent.keypress, CommonEvent.keyup);
    }

    public String getMouseEvents() {
      return getCommonEventString(CommonEvent.mousedown, CommonEvent.mousemove, CommonEvent.mouseout,
          CommonEvent.mouseover, CommonEvent.mouseup);
    }

    public String getSelectEvent() {
      return getCommonEventString(CommonEvent.select);
    }

    private String getCommonEventString(CommonEvent... commonEvents) {
      boolean allTrue = true;
      boolean allFalse = true;

      for (CommonEvent commonEvent : commonEvents) {
        if (eventNames.contains(commonEvent.name())) {
          allFalse = false;
        } else {
          allTrue = false;
        }
      }

      if (allTrue) {
        return "x";
      } else if (allFalse) {
        return "-";
      } else {
        return concatStrings(eventNames);
      }
    }

    public String getSpecialEvents() {
      Set<String> specialEventNames = new TreeSet<String>();

      for (String eventName : eventNames) {
        boolean isSpecialEvent = true;
        for (CommonEvent commonEvent : CommonEvent.values()) {
          if (eventName.equals(commonEvent.name())) {
            isSpecialEvent = false;
          }
        }

        if (isSpecialEvent) {
          specialEventNames.add(eventName);
        }
      }

      return specialEventNames.size() > 0 ? concatStrings(specialEventNames) : "";
    }

    private String concatStrings(Set<String> strings) {
      StringBuilder stringBuilder = new StringBuilder();

      int i = 0;
      for (String string : strings) {
        i++;
        stringBuilder.append(string);
        if (i < strings.size()) {
          stringBuilder.append(", ");
        }
      }
      return stringBuilder.toString();
    }
  }

  private enum CommonEvent {
    change,
    click, dblclick,
    focus, blur,
    keydown, keypress, keyup,
    mousedown, mousemove, mouseout, mouseover, mouseup,
    select
  }
}

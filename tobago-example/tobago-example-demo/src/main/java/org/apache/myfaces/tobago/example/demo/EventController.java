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
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UIButtons;
import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UIColumnNode;
import org.apache.myfaces.tobago.component.UIColumnSelector;
import org.apache.myfaces.tobago.component.UIDate;
import org.apache.myfaces.tobago.component.UIEvent;
import org.apache.myfaces.tobago.component.UIFigure;
import org.apache.myfaces.tobago.component.UIFile;
import org.apache.myfaces.tobago.component.UIFlexLayout;
import org.apache.myfaces.tobago.component.UIFlowLayout;
import org.apache.myfaces.tobago.component.UIFooter;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIHeader;
import org.apache.myfaces.tobago.component.UIHidden;
import org.apache.myfaces.tobago.component.UIImage;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UILabel;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UILinks;
import org.apache.myfaces.tobago.component.UIMessages;
import org.apache.myfaces.tobago.component.UIObject;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.UIProgress;
import org.apache.myfaces.tobago.component.UIRow;
import org.apache.myfaces.tobago.component.UISection;
import org.apache.myfaces.tobago.component.UISegmentLayout;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.component.UISelectManyCheckbox;
import org.apache.myfaces.tobago.component.UISelectManyListbox;
import org.apache.myfaces.tobago.component.UISelectManyShuttle;
import org.apache.myfaces.tobago.component.UISelectOneChoice;
import org.apache.myfaces.tobago.component.UISelectOneListbox;
import org.apache.myfaces.tobago.component.UISelectOneRadio;
import org.apache.myfaces.tobago.component.UISeparator;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.component.UISuggest;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.component.UITextarea;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeIcon;
import org.apache.myfaces.tobago.component.UITreeIndent;
import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeSelect;
import org.apache.myfaces.tobago.util.ComponentUtils;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.UIData;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.inject.Named;

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

  private final List<EventsOnComponent> eventsOnComponents = new ArrayList<>();
  private EventsOnComponent selectedComponent;
  private String eventName;
  private int action = 0;
  private int actionListener = 0;
  private int ajaxListener = 0;
  private int valueChangeListener = 0;
  private final List<SolarObject> planets = new ArrayList<>();

  public EventController() {
    eventsOnComponents.add(new EventsOnComponent(new UIBar()));
    eventsOnComponents.add(new EventsOnComponent(new UIBox()));
    eventsOnComponents.add(new EventsOnComponent(new UIButton()));
    eventsOnComponents.add(new EventsOnComponent(new UIButtons()));
    eventsOnComponents.add(new EventsOnComponent(new UIColumn()));
    eventsOnComponents.add(new EventsOnComponent(new UIColumnNode()));
    eventsOnComponents.add(new EventsOnComponent(new UIColumnSelector()));
    eventsOnComponents.add(new EventsOnComponent(new UIDate()));
    eventsOnComponents.add(new EventsOnComponent(new UIEvent()));
    eventsOnComponents.add(new EventsOnComponent(new UIFigure()));
    eventsOnComponents.add(new EventsOnComponent(new UIFile()));
    eventsOnComponents.add(new EventsOnComponent(new UIFlexLayout()));
    eventsOnComponents.add(new EventsOnComponent(new UIFlowLayout()));
    eventsOnComponents.add(new EventsOnComponent(new UIFooter()));
    eventsOnComponents.add(new EventsOnComponent(new UIForm()));
    eventsOnComponents.add(new EventsOnComponent(new UIGridLayout()));
    eventsOnComponents.add(new EventsOnComponent(new UIHeader()));
    eventsOnComponents.add(new EventsOnComponent(new UIHidden()));
    eventsOnComponents.add(new EventsOnComponent(new UIImage()));
    eventsOnComponents.add(new EventsOnComponent(new UIIn()));
    eventsOnComponents.add(new EventsOnComponent(new UILabel()));
    eventsOnComponents.add(new EventsOnComponent(new UILink()));
    eventsOnComponents.add(new EventsOnComponent(new UILinks()));
    eventsOnComponents.add(new EventsOnComponent(new UIMessages()));
    eventsOnComponents.add(new EventsOnComponent(new UIObject()));
    eventsOnComponents.add(new EventsOnComponent(new UIOut()));
    eventsOnComponents.add(new EventsOnComponent(new UIPage()));
    eventsOnComponents.add(new EventsOnComponent(new UIPanel()));
    eventsOnComponents.add(new EventsOnComponent(new UIPopup()));
    eventsOnComponents.add(new EventsOnComponent(new UIProgress()));
    eventsOnComponents.add(new EventsOnComponent(new UIRow()));
    eventsOnComponents.add(new EventsOnComponent(new UISection()));
    eventsOnComponents.add(new EventsOnComponent(new UISegmentLayout()));
    eventsOnComponents.add(new EventsOnComponent(new UISelectBooleanCheckbox()));
    eventsOnComponents.add(new EventsOnComponent(new UISelectManyCheckbox()));
    eventsOnComponents.add(new EventsOnComponent(new UISelectManyListbox()));
    eventsOnComponents.add(new EventsOnComponent(new UISelectManyShuttle()));
    eventsOnComponents.add(new EventsOnComponent(new UISelectOneChoice()));
    eventsOnComponents.add(new EventsOnComponent(new UISelectOneListbox()));
    eventsOnComponents.add(new EventsOnComponent(new UISelectOneRadio()));
    eventsOnComponents.add(new EventsOnComponent(new UISeparator()));
    eventsOnComponents.add(new EventsOnComponent(new UISheet()));
    eventsOnComponents.add(new EventsOnComponent(new UISuggest()));
    eventsOnComponents.add(new EventsOnComponent(new UITab()));
    eventsOnComponents.add(new EventsOnComponent(new UITabGroup()));
    eventsOnComponents.add(new EventsOnComponent(new UITextarea()));
    eventsOnComponents.add(new EventsOnComponent(new UITree()));
    eventsOnComponents.add(new EventsOnComponent(new UITreeIcon()));
    eventsOnComponents.add(new EventsOnComponent(new UITreeIndent()));
    eventsOnComponents.add(new EventsOnComponent(new UITreeLabel()));
    eventsOnComponents.add(new EventsOnComponent(new UITreeListbox()));
    eventsOnComponents.add(new EventsOnComponent(new UITreeSelect()));

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
  }

  public List<EventsOnComponent> getEventsOnComponents() {
    return eventsOnComponents;
  }

  public EventsOnComponent getSelectedComponent() {
    return selectedComponent;
  }

  public String getEventName() {
    return eventName;
  }

  public void selectComponent(final ActionEvent actionEvent) {
    final UIData data = ComponentUtils.findAncestor(actionEvent.getComponent(), UIData.class);
    selectedComponent = data != null ? ((EventsOnComponent) data.getRowData()) : null;
    eventName = actionEvent.getComponent().getAttributes().get("eventName").toString();
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

  public static class EventsOnComponent implements Serializable {
    private final String tagName;
    private final Collection<String> eventNames = new TreeSet<>();

    EventsOnComponent(final UIComponentBase component) {
      final String simpleName = component.getClass().getSimpleName();
      tagName = simpleName.substring(2, 3).toLowerCase() + simpleName.substring(3);
      if (component.getEventNames() != null) {
        this.eventNames.addAll(component.getEventNames());
      }
    }

    public String getTagName() {
      return tagName;
    }

    public boolean hasBlurEvent() {
      return eventNames.contains(CommonEvent.blur.name());
    }

    public boolean hasChangeEvent() {
      return eventNames.contains(CommonEvent.change.name());
    }

    public boolean hasClickEvent() {
      return eventNames.contains(CommonEvent.click.name());
    }

    public boolean hasDblclickEvent() {
      return eventNames.contains(CommonEvent.dblclick.name());
    }

    public boolean hasFocusEvent() {
      return eventNames.contains(CommonEvent.focus.name());
    }

    public boolean hasKeydownEvent() {
      return eventNames.contains(CommonEvent.keydown.name());
    }

    public boolean hasKeypressEvent() {
      return eventNames.contains(CommonEvent.keypress.name());
    }

    public boolean hasKeyupEvent() {
      return eventNames.contains(CommonEvent.keyup.name());
    }

    public boolean hasMousedownEvent() {
      return eventNames.contains(CommonEvent.mousedown.name());
    }

    public boolean hasMousemoveEvent() {
      return eventNames.contains(CommonEvent.mousemove.name());
    }

    public boolean hasMouseoutEvent() {
      return eventNames.contains(CommonEvent.mouseout.name());
    }

    public boolean hasMouseoverEvent() {
      return eventNames.contains(CommonEvent.mouseover.name());
    }

    public boolean hasMouseupEvent() {
      return eventNames.contains(CommonEvent.mouseup.name());
    }

    public boolean hasSelectEvent() {
      return eventNames.contains(CommonEvent.select.name());
    }

    public String getSpecialEvents() {
      final Set<String> specialEventNames = new TreeSet<>();

      for (final String name : eventNames) {
        boolean isSpecialEvent = true;
        for (final CommonEvent commonEvent : CommonEvent.values()) {
          if (name.equals(commonEvent.name())) {
            isSpecialEvent = false;
            break;
          }
        }

        if (isSpecialEvent) {
          specialEventNames.add(name);
        }
      }

      return specialEventNames.size() > 0 ? concatStrings(specialEventNames) : "";
    }

    private String concatStrings(final Set<String> strings) {
      final StringBuilder stringBuilder = new StringBuilder();

      int i = 0;
      for (final String string : strings) {
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

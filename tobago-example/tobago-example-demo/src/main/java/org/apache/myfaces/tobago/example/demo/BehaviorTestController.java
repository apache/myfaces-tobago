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

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

import java.io.Serializable;

@SessionScoped
@Named
public class BehaviorTestController implements Serializable {

  private int buttonActionCounter;
  private int buttonActionListenerCounter;
  private int actionCounter1;
  private int actionListenerCounter1;
  private int ajaxListenerCounter1;
  private int actionCounter2;
  private int actionListenerCounter2;
  private int ajaxListenerCounter2;
  private int actionCounter3;
  private int actionListenerCounter3;
  private int ajaxListenerCounter3;
  private int selector;
  private boolean eventEnabled1;
  private boolean eventEnabled2;
  private boolean eventEnabled3;
  private boolean ajaxEnabled1;
  private boolean ajaxEnabled2;
  private boolean ajaxEnabled3;

  public BehaviorTestController() {
    reset();
  }

  public void reset() {
    buttonActionCounter = 0;
    buttonActionListenerCounter = 0;
    actionCounter1 = 0;
    actionListenerCounter1 = 0;
    ajaxListenerCounter1 = 0;
    actionCounter2 = 0;
    actionListenerCounter2 = 0;
    ajaxListenerCounter2 = 0;
    actionCounter3 = 0;
    actionListenerCounter3 = 0;
    ajaxListenerCounter3 = 0;
  }

  public void countButtonAction() {
    buttonActionCounter++;
  }

  public void countButtonActionListener(ActionEvent actionEvent) {
    buttonActionListenerCounter++;
  }

  public void countAction1() {
    actionCounter1++;
  }

  public void countActionListener1(ActionEvent actionEvent) {
    actionListenerCounter1++;
  }

  public void countAjaxListener1(AjaxBehaviorEvent ajaxBehaviorEvent) {
    ajaxListenerCounter1++;
  }

  public void countAction2() {
    actionCounter2++;
  }

  public void countActionListener2(ActionEvent actionEvent) {
    actionListenerCounter2++;
  }

  public void countAjaxListener2(AjaxBehaviorEvent ajaxBehaviorEvent) {
    ajaxListenerCounter2++;
  }

  public void countAction3() {
    actionCounter3++;
  }

  public void countActionListener3(ActionEvent actionEvent) {
    actionListenerCounter3++;
  }

  public void countAjaxListener3(AjaxBehaviorEvent ajaxBehaviorEvent) {
    ajaxListenerCounter3++;
  }

  public int getSelector() {
    return selector;
  }

  public void setSelector(int selector) {
    this.selector = selector;
  }

  public void submitSelection() {
    eventEnabled1 = false;
    eventEnabled2 = false;
    eventEnabled3 = false;
    ajaxEnabled1 = false;
    ajaxEnabled2 = false;
    ajaxEnabled3 = false;

    switch (selector) {
      case 1:
        eventEnabled1 = true;
        break;
      case 2:
        eventEnabled2 = true;
        ajaxEnabled3 = true;
        break;
      case 3:
        eventEnabled3 = true;
        ajaxEnabled1 = true;
        ajaxEnabled2 = true;
        ajaxEnabled3 = true;
        break;
      default:
    }
  }

  public int getButtonActionCounter() {
    return buttonActionCounter;
  }

  public int getButtonActionListenerCounter() {
    return buttonActionListenerCounter;
  }

  public int getActionCounter1() {
    return actionCounter1;
  }

  public int getActionListenerCounter1() {
    return actionListenerCounter1;
  }

  public int getAjaxListenerCounter1() {
    return ajaxListenerCounter1;
  }

  public int getActionCounter2() {
    return actionCounter2;
  }

  public int getActionListenerCounter2() {
    return actionListenerCounter2;
  }

  public int getAjaxListenerCounter2() {
    return ajaxListenerCounter2;
  }

  public int getActionCounter3() {
    return actionCounter3;
  }

  public int getActionListenerCounter3() {
    return actionListenerCounter3;
  }

  public int getAjaxListenerCounter3() {
    return ajaxListenerCounter3;
  }

  public boolean isEventEnabled1() {
    return eventEnabled1;
  }

  public boolean isEventEnabled2() {
    return eventEnabled2;
  }

  public boolean isEventEnabled3() {
    return eventEnabled3;
  }

  public boolean isAjaxEnabled1() {
    return ajaxEnabled1;
  }

  public boolean isAjaxEnabled2() {
    return ajaxEnabled2;
  }

  public boolean isAjaxEnabled3() {
    return ajaxEnabled3;
  }
}

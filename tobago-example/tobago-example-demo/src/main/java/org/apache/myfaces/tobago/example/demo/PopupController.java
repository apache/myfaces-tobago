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
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalTime;

@SessionScoped
@Named
public class PopupController implements Serializable {

  private boolean popup1Collapsed = true;
  private String popup1Text;
  private String popup2Text;
  private String time;

  public boolean isPopup1Collapsed() {
    return popup1Collapsed;
  }

  public void setPopup1Collapsed(final boolean popup1Collapsed) {
    this.popup1Collapsed = popup1Collapsed;
  }

  public void openPopup1() {
    popup1Collapsed = false;
  }

  public void closePopup1() {
    popup1Collapsed = true;
  }

  public String getPopup1Text() {
    return popup1Text;
  }

  public void setPopup1Text(final String popup1Text) {
    this.popup1Text = popup1Text;
  }

  public String getPopup2Text() {
    return popup2Text;
  }

  public void setPopup2Text(final String popup2Text) {
    this.popup2Text = popup2Text;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public void refreshContent(final AjaxBehaviorEvent event) throws InterruptedException {
    Thread.sleep(2000);
    time = LocalTime.now().toString();
  }
}

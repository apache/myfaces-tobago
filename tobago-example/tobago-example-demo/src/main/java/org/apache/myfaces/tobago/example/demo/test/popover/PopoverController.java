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

package org.apache.myfaces.tobago.example.demo.test.popover;

import org.apache.myfaces.tobago.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;

@SessionScoped
@Named(value = "testPopoverController")
public class PopoverController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final SelectItem[] cancelableEvents = new SelectItem[]{
      new SelectItem("None"),
      new SelectItem("tobago.popover.show"),
      new SelectItem("tobago.popover.hide")};
  private String cancelEvent = "None";
  private int show = 0;
  private int shown = 0;
  private int hide = 0;
  private int hidden = 0;

  public SelectItem[] getCancelableEvents() {
    return cancelableEvents;
  }

  public String getCancelEvent() {
    return cancelEvent;
  }

  public void setCancelEvent(String cancelEvent) {
    this.cancelEvent = cancelEvent;
  }

  public void increaseShow(final AjaxBehaviorEvent event) {
    show++;
  }

  public void increaseShown(final AjaxBehaviorEvent event) {
    shown++;
  }

  public void increaseHide(final AjaxBehaviorEvent event) {
    hide++;
  }

  public void increaseHidden(final AjaxBehaviorEvent event) {
    hidden++;
  }

  public int getShow() {
    return show;
  }

  public int getShown() {
    return shown;
  }

  public int getHide() {
    return hide;
  }

  public int getHidden() {
    return hidden;
  }
}

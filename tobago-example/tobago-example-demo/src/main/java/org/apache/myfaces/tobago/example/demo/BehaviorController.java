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

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class BehaviorController implements Serializable {

  private String ajax;
  private String event;
  private int counter;

  public String getAjax() {
    return ajax;
  }

  public void setAjax(final String ajax) {
    this.ajax = ajax;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(final String event) {
    this.event = event;
  }

  public int getCounter() {
    return counter;
  }

  public void countUp(final AjaxBehaviorEvent event) {
    counter++;
  }

  public void countUp(final ActionEvent event) {
    counter++;
  }
}

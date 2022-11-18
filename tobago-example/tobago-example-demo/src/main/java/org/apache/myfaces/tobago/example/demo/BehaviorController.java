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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;

@SessionScoped
@Named
public class BehaviorController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String ajax;
  private String event;
  private String output;
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

  public String getOutput() {
    return output;
  }

  public void setOutput(String output) {
    this.output = output;
  }

  public void countUp(final AjaxBehaviorEvent ajaxBehaviorEvent) {
    LOG.info("ajaxBehaviorEvent=" + ajaxBehaviorEvent);
    counter++;
  }

  public void countUp(final ActionEvent actionEvent) {
    LOG.info("actionEvent=" + actionEvent);
    counter++;
  }

  public void eventOutput(final AjaxBehaviorEvent ajaxBehaviorEvent) {
    LOG.info("ajaxBehaviorEvent=" + ajaxBehaviorEvent);
    this.output = "Ajax";
  }

  public void eventOutput(final ActionEvent actionEvent) {
    LOG.info("actionEvent=" + actionEvent);
    this.output = "Event";
  }
}

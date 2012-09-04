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

package org.apache.myfaces.tobago.renderkit.html;

/**
 * @since 1.6.0
 */
// XXX work in progress
public class Command {

  private Boolean transition;
  private String target;
  private String url;
  private String[] partially;
  private String focus;
  private String confirmation;
  private Integer delay;
  private Popup popup;
  /**
   * @deprecated
   */
  @Deprecated
  private String script;

  public Command(
      Boolean transition, String target, String url, String[] partially, String focus, String confirmation,
      Integer delay, Popup popup) {
    this.transition = transition;
    this.target = target;
    this.url = url;
    this.partially = partially;
    this.focus = focus;
    this.confirmation = confirmation;
    this.delay = delay;
    this.popup = popup;
  }

  public Boolean getTransition() {
    return transition;
  }

  public void setTransition(Boolean transition) {
    this.transition = transition;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String[] getPartially() {
    return partially;
  }

  public void setPartially(String[] partially) {
    this.partially = partially;
  }

  public String getFocus() {
    return focus;
  }

  public void setFocus(String focus) {
    this.focus = focus;
  }

  public String getConfirmation() {
    return confirmation;
  }

  public void setConfirmation(String confirmation) {
    this.confirmation = confirmation;
  }

  public Integer getDelay() {
    return delay;
  }

  public void setDelay(Integer delay) {
    this.delay = delay;
  }

  public Popup getPopup() {
    return popup;
  }

  public void setPopup(Popup popup) {
    this.popup = popup;
  }

  /**
   * @deprecated
   */
  public String getScript() {
    return script;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public void setScript(String script) {
    this.script = script;
  }

}

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

package org.apache.myfaces.tobago.internal.renderkit;

import org.apache.myfaces.tobago.internal.util.StringUtils;

/**
 * @since 2.0.0
 */
public class Command {

  private String clientId;
  private String fieldId;
  private Boolean transition;
  private String target;
  private String execute;
  private String render;
  private String confirmation;
  private Integer delay;
  private Collapse collapse;
  private Boolean omit;
  private Boolean resetValues;
  private Boolean stopPropagation;
  private String customEventName;
  private Boolean immediate;

  public Command(
      final String clientId, final String fieldId, final Boolean transition, final String target, final String execute,
      final String render, final String confirmation, final Integer delay,
      final Collapse collapse, final Boolean omit, final Boolean stopPropagation, final String customEventName,
      final Boolean immediate) {
    this(clientId, fieldId, transition, target, execute, render, confirmation, delay, collapse, omit, stopPropagation,
        customEventName);
    this.immediate = immediate;
  }

  /**
   * @deprecated Please use
   * {@link Command#Command(String, String, Boolean, String, String, String, String, Integer, Collapse, Boolean,
   * Boolean, String, Boolean)}
   */
  @Deprecated(since = "6.7.2")
  public Command(
      final String clientId, final String fieldId, final Boolean transition, final String target, final String execute,
      final String render, final String confirmation, final Integer delay,
      final Collapse collapse, final Boolean omit, final Boolean stopPropagation, final String customEventName) {
    this.clientId = clientId;
    this.fieldId = fieldId;
    this.transition = transition != null ? transition : true;
    this.target = target;
    setExecute(execute);
    setRender(render);
    this.confirmation = confirmation;
    this.delay = delay;
    this.collapse = collapse;
    this.omit = omit;
    this.stopPropagation = stopPropagation;
    this.customEventName = customEventName;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getFieldId() {
    return fieldId;
  }

  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  public Boolean getTransition() {
    return transition;
  }

  public void setTransition(final Boolean transition) {
    this.transition = transition;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(final String target) {
    this.target = target;
  }

  public String getExecute() {
    return execute;
  }

  public void setExecute(final String execute) {
    if (StringUtils.isNotBlank(execute)) {
      this.execute = execute;
    }
  }

  public String getRender() {
    return render;
  }

  public void setRender(final String render) {
    if (StringUtils.isNotBlank(render)) {
      this.render = render;
    }
  }

  public String getConfirmation() {
    return confirmation;
  }

  public void setConfirmation(final String confirmation) {
    this.confirmation = confirmation;
  }

  public Integer getDelay() {
    return delay;
  }

  public void setDelay(final Integer delay) {
    this.delay = delay;
  }

  public Collapse getCollapse() {
    return collapse;
  }

  public void setCollapse(final Collapse collapse) {
    this.collapse = collapse;
  }

  public Boolean getOmit() {
    return omit;
  }

  public void setOmit(final Boolean omit) {
    this.omit = omit;
  }

  public Boolean getResetValues() {
    return resetValues;
  }

  public void setResetValues(final Boolean resetValues) {
    this.resetValues = resetValues;
  }

  public Boolean getStopPropagation() {
    return stopPropagation;
  }

  public String getCustomEventName() {
    return customEventName;
  }

  public Boolean getImmediate() {
    return immediate;
  }

  public void setImmediate(Boolean immediate) {
    this.immediate = immediate;
  }

  public void merge(final Command c) {

    //XXX TBD: check if this is okay.
    // we need at least this for "execute" and "render" in the moment.

    if (clientId == null) {
      clientId = c.clientId;
    }
    if (fieldId == null) {
      fieldId = c.fieldId;
    }
    if (transition == null) {
      transition = c.transition;
    }
    if (target == null) {
      target = c.target;
    }
    if (execute != null) {
      if (c.execute != null) {
        execute += " " + c.execute;
      }
    } else {
      execute = c.execute;
    }
    if (render != null) {
      if (c.render != null) {
        render += " " + c.render;
      }
    } else {
      render = c.render;
    }
    if (confirmation == null) {
      confirmation = c.confirmation;
    }
    if (delay == null) {
      delay = c.delay;
    }
    if (collapse == null) {
      collapse = c.collapse;
    }
    if (omit == null) {
      omit = c.omit;
    }
    if (resetValues == null) {
      resetValues = c.resetValues;
    }
    if (stopPropagation == null) {
      stopPropagation = c.stopPropagation;
    }
    if (customEventName == null) {
      customEventName = c.customEventName;
    }
    if (immediate == null) {
      immediate = c.immediate;
    }
  }
}

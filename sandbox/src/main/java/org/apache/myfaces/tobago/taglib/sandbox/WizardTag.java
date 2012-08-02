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

package org.apache.myfaces.tobago.taglib.sandbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIWizard;
import org.apache.myfaces.tobago.taglib.component.TobagoTag;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public final class WizardTag extends TobagoTag {
  private static final Log LOG = LogFactory.getLog(WizardTag.class);
  private String controller;
  private String outcome;
  private String var;
  private String title;
  private String allowJumpForward;

  @Override
  public String getComponentType() {
    return UIWizard.COMPONENT_TYPE;
  }
  @Override
  public String getRendererType() {
    return "Wizard";
  }

  @Override
  protected void setProperties(UIComponent uiComponent) {
    super.setProperties(uiComponent);
    UIWizard component = (UIWizard) uiComponent;
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = context.getApplication();
    if (controller != null && isValueReference(controller)) {
      component.setValueBinding("controller", application.createValueBinding(controller));
    }
    if (outcome != null) {
      if (isValueReference(outcome)) {
        component.setValueBinding("outcome", application.createValueBinding(outcome));
      } else {
        component.setOutcome(outcome);
      }
    }
    if (var != null) {
      if (isValueReference(var)) {
        component.setValueBinding("var", application.createValueBinding(var));
      } else {
        component.setVar(var);
      }
    }
    if (title != null) {
      if (isValueReference(title)) {
        component.setValueBinding("title", application.createValueBinding(title));
      } else {
        component.setTitle(title);
      }
    }
    if (allowJumpForward != null) {
      if (isValueReference(allowJumpForward)) {
        component.setValueBinding("allowJumpForward", application.createValueBinding(allowJumpForward));
      } else {
        component.setAllowJumpForward(Boolean.valueOf(allowJumpForward));
      }
    }

  }

  public String getController() {
    return controller;
  }

  public void setController(String controller) {
    this.controller = controller;
  }

  public String getOutcome() {
    return outcome;
  }

  public void setOutcome(String outcome) {
    this.outcome = outcome;
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAllowJumpForward() {
    return allowJumpForward;
  }

  public void setAllowJumpForward(String allowJumpForward) {
    this.allowJumpForward = allowJumpForward;
  }



  @Override
  public void release() {
    super.release();
    controller = null;
    outcome = null;
    var = null;
    title = null;
    allowJumpForward = null;
  }
}

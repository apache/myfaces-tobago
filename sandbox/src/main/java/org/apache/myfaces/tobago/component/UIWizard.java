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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.event.FacesEventWrapper;
import org.apache.myfaces.tobago.model.Wizard;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.io.IOException;

public class UIWizard extends UIPanel implements OnComponentCreated {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Wizard";

  // todo: how to use a auto-generated controller?
  private Wizard controller;

  private String var;

  private String outcome;
  private String title;
  private Boolean allowJumpForward;

  @Override
  public void processDecodes(FacesContext facesContext) {
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().put(var, getController());
    }
    super.processDecodes(facesContext);
  }

  @Override
  public void decode(FacesContext facesContext) {
    super.decode(facesContext);
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().remove(var);
    }
  }

  @Override
  public void queueEvent(FacesEvent event) {
    super.queueEvent(new FacesEventWrapper(event, this));
  }

  @Override
  public void broadcast(FacesEvent event) throws AbortProcessingException {
    if (event instanceof FacesEventWrapper) {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      if (var != null) {
        facesContext.getExternalContext().getRequestMap().put(var, getController());
      }
      FacesEvent originalEvent = ((FacesEventWrapper) event).getWrappedFacesEvent();
      originalEvent.getComponent().broadcast(originalEvent);
      if (var != null) {
        facesContext.getExternalContext().getRequestMap().remove(var);
      }
    } else {
      super.broadcast(event);
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().put(var, getController());
    }
    super.encodeBegin(facesContext);
  }


  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {
    super.encodeEnd(facesContext);
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().remove(var);
    }
  }

  public void onComponentCreated() {
    Wizard wizard = getController();
    wizard.register();
    if (getOutcome() != null) {
      getController().getCurrentStep().setOutcome(getOutcome());
    }
    if (getTitle() != null) {
      getController().getCurrentStep().setTitle(getTitle());
    }
  }

  @Override
  public Object saveState(FacesContext facesContext) {
    Object[] state = new Object[3];
    state[0] = super.saveState(facesContext);
    state[1] = var;
    state[2] = controller;
    return state;
  }

  @Override
  public void restoreState(FacesContext facesContext, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(facesContext, values[0]);
    var = (String) values[1];
    controller = (Wizard) values[2];
  }

  public Wizard getController() {
    if (controller != null) {
      return controller;
    }
    ValueBinding vb = getValueBinding("controller"); // xxx const
    if (vb != null) {
      return (Wizard) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public void setController(Wizard controller) {
    this.controller = controller;
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public java.lang.String getOutcome() {
    if (outcome != null) {
      return outcome;
    }
    javax.faces.el.ValueBinding vb = getValueBinding("outcome");
    if (vb != null) {
      return (java.lang.String) vb.getValue(getFacesContext());
    }
    return null;
  }


  public void setOutcome(String outcome) {
    this.outcome = outcome;
  }

  public String getTitle() {
    if (title != null) {
      return title;
    }
    javax.faces.el.ValueBinding vb = getValueBinding("title");
    if (vb != null) {
      return (java.lang.String) vb.getValue(getFacesContext());
    }
    return null;
  }


  public void setTitle(String title) {
    this.title = title;
  }

  public Boolean isAllowJumpForward() {
    if (allowJumpForward != null) {
      return allowJumpForward;
    }
    javax.faces.el.ValueBinding vb = getValueBinding("allowJumpForward");
    if (vb != null) {
      Boolean bool = (Boolean) vb.getValue(getFacesContext());
      if (bool != null) {
        return bool;
      }
    }
    return false;
  }

  public void setAllowJumpForward(Boolean allowJumpForward) {
    this.allowJumpForward = allowJumpForward;
//    getController().removeForwardSteps();
  }
}

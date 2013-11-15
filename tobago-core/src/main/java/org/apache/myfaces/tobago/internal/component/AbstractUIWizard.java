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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.event.FacesEventWrapper;
import org.apache.myfaces.tobago.model.Wizard;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.io.IOException;

public abstract class AbstractUIWizard extends AbstractUIPanel implements OnComponentCreated {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Wizard";

  // todo: how to use a auto-generated controller?
  private Wizard controller;

  private String var;

  private String outcome;
  private String title;
  private Boolean allowJumpForward;

  @Override
  public void processDecodes(final FacesContext facesContext) {
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().put(var, getController());
    }
    super.processDecodes(facesContext);
  }

  @Override
  public void decode(final FacesContext facesContext) {
    super.decode(facesContext);
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().remove(var);
    }
  }

  @Override
  public void queueEvent(final FacesEvent event) {
    super.queueEvent(new FacesEventWrapper(event, this));
  }

  @Override
  public void broadcast(final FacesEvent event) throws AbortProcessingException {
    if (event instanceof FacesEventWrapper) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      if (var != null) {
        facesContext.getExternalContext().getRequestMap().put(var, getController());
      }
      final FacesEvent originalEvent = ((FacesEventWrapper) event).getWrappedFacesEvent();
      originalEvent.getComponent().broadcast(originalEvent);
      if (var != null) {
        facesContext.getExternalContext().getRequestMap().remove(var);
      }
    } else {
      super.broadcast(event);
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().put(var, getController());
    }
    super.encodeBegin(facesContext);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {
    super.encodeEnd(facesContext);
    if (var != null) {
      facesContext.getExternalContext().getRequestMap().remove(var);
    }
  }

  public void onComponentCreated(final FacesContext context, final UIComponent parent) {
    final Wizard wizard = getController();
    wizard.register();
    if (getOutcome() != null) {
      getController().getCurrentStep().setOutcome(getOutcome());
    }
    if (getTitle() != null) {
      getController().getCurrentStep().setTitle(getTitle());
    }
  }

  @Override
  public Object saveState(final FacesContext facesContext) {
    final Object[] state = new Object[3];
    state[0] = super.saveState(facesContext);
    state[1] = var;
    state[2] = controller;
    return state;
  }

  @Override
  public void restoreState(final FacesContext facesContext, final Object state) {
    final Object[] values = (Object[]) state;
    super.restoreState(facesContext, values[0]);
    var = (String) values[1];
    controller = (Wizard) values[2];
  }

  public Wizard getController() {
    if (controller != null) {
      return controller;
    }
    final ValueBinding vb = getValueBinding("controller"); // xxx const
    if (vb != null) {
      return (Wizard) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public void setController(final Wizard controller) {
    this.controller = controller;
  }

  public String getVar() {
    return var;
  }

  public void setVar(final String var) {
    this.var = var;
  }

  public java.lang.String getOutcome() {
    if (outcome != null) {
      return outcome;
    }
    final javax.faces.el.ValueBinding vb = getValueBinding("outcome");
    if (vb != null) {
      return (java.lang.String) vb.getValue(getFacesContext());
    }
    return null;
  }


  public void setOutcome(final String outcome) {
    this.outcome = outcome;
  }

  public String getTitle() {
    if (title != null) {
      return title;
    }
    final javax.faces.el.ValueBinding vb = getValueBinding("title");
    if (vb != null) {
      return (java.lang.String) vb.getValue(getFacesContext());
    }
    return null;
  }


  public void setTitle(final String title) {
    this.title = title;
  }

  public boolean isAllowJumpForward() {
    if (allowJumpForward != null) {
      return allowJumpForward;
    }
    final javax.faces.el.ValueBinding vb = getValueBinding("allowJumpForward");
    if (vb != null) {
      final Boolean bool = (Boolean) vb.getValue(getFacesContext());
      if (bool != null) {
        return bool;
      }
    }
    return false;
  }

  public void setAllowJumpForward(final Boolean allowJumpForward) {
    this.allowJumpForward = allowJumpForward;
//    getController().removeForwardSteps();
  }
}

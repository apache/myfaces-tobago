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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

import static org.apache.myfaces.tobago.internal.util.Deprecation.LOG;

/**
 * @since 2.0.0
 */
public class Command {

  /**
   * The action is only needed if the action is not the HTML element itself.
   */
  private String action;
  private Boolean transition;
  private String target;
  private String partially;
  private String execute;
  private String render;
  private String focus;
  private String confirmation;
  private Integer delay;
  private Collapse collapse;
  private Boolean omit;

  public Command() {
  }

  public Command(
      final String action, final Boolean transition, final String target, final String execute,
      final String render, final String focus, final String confirmation, final Integer delay,
      final Collapse collapse, final Boolean omit) {
    this.action = action;
    this.transition = transition;
    this.target = target;
    setExecute(execute);
    setRender(render);
    this.focus = focus;
    this.confirmation = confirmation;
    this.delay = delay;
    this.collapse = collapse;
    this.omit = omit;
  }

  public Command(final FacesContext facesContext, final AbstractUICommand command) {
    this(
        null,
        command.isTransition(),
        command.getTarget(),
        null,
        null,
        null,
        getConfirmation(command),
        null,
        AjaxClientBehaviorRenderer.createCollapsible(facesContext, command),
        command.isOmit());
  }

  public Command(final FacesContext facesContext, UIComponent facetComponent, final String focusId) {
    if (facetComponent instanceof UIForm && facetComponent.getChildCount() == 1) {
      LOG.warn("Please don't use a form, but a command with immediate=true instead.");
      facetComponent = facetComponent.getChildren().get(0);
    }
    this.action = facetComponent.getClientId(facesContext);
    // transition == true is the default
    if (!ComponentUtils.getBooleanAttribute(facetComponent, Attributes.transition)) {
      this.transition = Boolean.FALSE;
    }
    final String target = ComponentUtils.getStringAttribute(facetComponent, Attributes.target);
    if (target != null) {
      this.target = target;
    }
    if (focusId != null) {
      this.focus = focusId;
    }

    final int delay = ComponentUtils.getIntAttribute(facetComponent, Attributes.delay);
    if (delay > 0) {
      this.delay = delay;
    }
    // omit == false is the default
    if (ComponentUtils.getBooleanAttribute(facetComponent, Attributes.omit)) {
      this.omit = Boolean.TRUE;
    }
  }

  private static String getConfirmation(final AbstractUICommand command) {
    final String confirmation = command.getConfirmation();
    if (confirmation != null) {
      return confirmation;
    }
    final UIComponent facet = ComponentUtils.getFacet(command, Facets.confirmation);
    if (facet instanceof ValueHolder) {
      final ValueHolder valueHolder = (ValueHolder) facet;
      return "" + valueHolder.getValue();
    } else if (facet != null) {
      LOG.warn("The content of a confirmation facet must be a ValueHolder. Use e. g. <tc:out>.");
    }
    return null;
  }

  public String getAction() {
    return action;
  }

  public void setAction(final String action) {
    this.action = action;
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

  /**
   * @deprecated use getExecute() getRender() instead
   */
  @Deprecated
  public String getPartially() {
    return partially;
  }

  /**
   * @deprecated use setExecute() setRender() instead
   */
  @Deprecated
  public void setPartially(final String partially) {
    if (StringUtils.isNotBlank(partially)) {
      this.partially = partially;
    }
  }

  public String getExecute() {
    return execute;
  }

  public void setExecute(String execute) {
    if (StringUtils.isNotBlank(execute)) {
      this.execute = execute;
    }
  }

  public String getRender() {
    return render;
  }

  public void setRender(String render) {
    if (StringUtils.isNotBlank(render)) {
      this.render = render;
    }
  }

  public String getFocus() {
    return focus;
  }

  public void setFocus(final String focus) {
    this.focus = focus;
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

  public void setCollapse(Collapse collapse) {
    this.collapse = collapse;
  }

  public Boolean getOmit() {
    return omit;
  }

  public void setOmit(final Boolean omit) {
    this.omit = omit;
  }
}

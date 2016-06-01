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
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

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
  private String url;
  private String partially;
  private String execute;
  private String render;
  private String focus;
  private String confirmation;
  private Integer delay;
  private Popup popup;
  private Boolean omit;

  public Command() {
  }

  public Command(
      final String action, final Boolean transition, final String target, final String url, final String execute,
      final String render, final String focus, final String confirmation, final Integer delay, final Popup popup,
      final Boolean omit) {
    this.action = action;
    this.transition = transition;
    this.target = target;
    this.url = url;
    setExecute(execute);
    setRender(render);
    this.focus = focus;
    this.confirmation = confirmation;
    this.delay = delay;
    this.popup = popup;
    this.omit = omit;
  }

  /**
   * @deprecated use with "execute" and "render" instead
   */
  @Deprecated
  public Command(
      final String action, final Boolean transition, final String target, final String url, final String partially,
      final String focus, final String confirmation, final Integer delay, final Popup popup, final Boolean omit) {
    this.action = action;
    this.transition = transition;
    this.target = target;
    this.url = url;
    setPartially(partially);
    this.focus = focus;
    this.confirmation = confirmation;
    this.delay = delay;
    this.popup = popup;
    this.omit = omit;
  }

  public Command(final FacesContext facesContext, final AbstractUICommand command, String[] ids) {
    this(
        null,
        command.isTransition(),
        command.getTarget(),
        RenderUtils.generateUrl(facesContext, command),
        ComponentUtils.evaluateClientIds(facesContext, command, ids),
        null,
        getConfirmation(command),
        null,
        Popup.createPopup(command),
        command.isOmit());
  }

  public Command(final FacesContext facesContext, final AbstractUICommand command) {
    this(
        null,
        command.isTransition(),
        command.getTarget(),
        RenderUtils.generateUrl(facesContext, command),
        ComponentUtils.evaluateClientIds(facesContext, command, command.getRenderedPartially()),
        null,
        getConfirmation(command),
        null,
        Popup.createPopup(command),
        command.isOmit());
  }

  public Command(final FacesContext facesContext, UIComponent facetComponent, final String focusId) {
    if (facetComponent instanceof UIForm && facetComponent.getChildCount() == 1) {
      Deprecation.LOG.warn("Please don't use a form, but a command with immediate=true instead.");
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
    if (facetComponent instanceof AbstractUICommand
        && ((AbstractUICommand) facetComponent).getRenderedPartially().length > 0) {
      setPartially(ComponentUtils.evaluateClientIds(
          facesContext, facetComponent, ((UICommand) facetComponent).getRenderedPartially()));
    } else {
      if (focusId != null) {
        this.focus = focusId;
      }
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
    final ValueHolder facet = (ValueHolder) ComponentUtils.getFacet(command, Facets.confirmation);
    return facet != null ? "" + facet.getValue() : null;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(final String url) {
    this.url = url;
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

  public Popup getPopup() {
    return popup;
  }

  public void setPopup(final Popup popup) {
    this.popup = popup;
  }

  public Boolean getOmit() {
    return omit;
  }

  public void setOmit(final Boolean omit) {
    this.omit = omit;
  }
}

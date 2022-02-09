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
  private String[] partially;
  private String focus;
  private String confirmation;
  private Integer delay;
  private Popup popup;
  private Boolean omit;
  /**
   * @deprecated Script will not work when CSP is activated
   */
  @Deprecated
  private String script;

  public Command() {
  }

  public Command(
      final String action, final Boolean transition, final String target, final String url, final String[] partially,
      final String focus, final String confirmation, final Integer delay, final Popup popup, final Boolean omit) {
    this.action = action;
    this.transition = transition;
    this.target = target;
    this.url = url;
    this.partially = partially;
    this.focus = focus;
    this.confirmation = confirmation;
    this.delay = delay;
    this.popup = popup;
    this.omit = omit;
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
    if (command.getOnclick() != null) {
      script = command.getOnclick();
    }
  }

  public Command(final FacesContext facesContext, UIComponent facetComponent, final String focusId) {
    final UIComponent facet;
    if (facetComponent instanceof UIForm && facetComponent.getChildCount() == 1) {
      Deprecation.LOG.warn("Please don't use a form, but a command with immediate=true instead.");
      facet = facetComponent.getChildren().get(0);
    } else {
      facet = facetComponent;
    }

    this.action = facet.getClientId(facesContext);
    // transition == true is the default
    if (!ComponentUtils.getBooleanAttribute(facet, Attributes.TRANSITION)) {
      this.transition = Boolean.FALSE;
    }
    final String targetAttribute = ComponentUtils.getStringAttribute(facet, Attributes.TARGET);
    if (targetAttribute != null) {
      this.target = targetAttribute;
    }
    if (facet instanceof AbstractUICommand
        && ((AbstractUICommand) facet).getRenderedPartially().length > 0) {
      this.partially = ComponentUtils.evaluateClientIds(
          facesContext, facet, ((UICommand) facet).getRenderedPartially());
    } else {
      String facetAction = (String) facet.getAttributes().get(Attributes.ONCLICK);
      if (facetAction != null) {
        // Replace @autoId
        facetAction = StringUtils.replace(facetAction, "@autoId", facet.getClientId(facesContext));
        // XXX this case is deprecated.
        // not allowed with Content Security Policy (CSP)
        this.script = facetAction;
      }
      if (focusId != null) {
        this.focus = focusId;
      }
    }

    final int delayAttribute = ComponentUtils.getIntAttribute(facet, Attributes.DELAY);
    if (delayAttribute > 0) {
      this.delay = delayAttribute;
    }
    // omit == false is the default
    if (ComponentUtils.getBooleanAttribute(facet, Attributes.OMIT)) {
      this.omit = Boolean.TRUE;
    }
  }

  private static String getConfirmation(final AbstractUICommand command) {
    final ValueHolder facet = (ValueHolder) command.getFacet(Facets.CONFIRMATION);
    return facet != null && ((UIComponent) facet).isRendered() ? "" + facet.getValue() : null;
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

  public String[] getPartially() {
    return partially;
  }

  public void setPartially(final String[] partially) {
    this.partially = partially;
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

  /**
   * @deprecated Script will not work when CSP is activated
   */
  @Deprecated
  public String getScript() {
    return script;
  }

  /**
   * @deprecated Script will not work when CSP is activated
   */
  @Deprecated
  public void setScript(final String script) {
    this.script = script;
  }

}

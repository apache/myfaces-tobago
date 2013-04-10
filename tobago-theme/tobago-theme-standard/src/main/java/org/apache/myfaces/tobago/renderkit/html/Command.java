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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;

/**
 * @since 2.0.0
 */
// XXX work in progress
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
  /**
   * @deprecated Script will not work when CSP is activated
   */
  @Deprecated
  private String script;

  public Command() {
  }

  public Command(
      String action, Boolean transition, String target, String url, String[] partially, String focus,
      String confirmation, Integer delay, Popup popup) {
    this.action = action;
    this.transition = transition;
    this.target = target;
    this.url = url;
    this.partially = partially;
    this.focus = focus;
    this.confirmation = confirmation;
    this.delay = delay;
    this.popup = popup;
  }

   public Command(FacesContext facesContext, AbstractUICommandBase command) {
    this(
        null,
        command.isTransition(),
        command.getTarget(),
        RenderUtils.generateUrl(facesContext, command),
        HtmlRendererUtils.getComponentIdsAsList(facesContext, command, command.getRenderedPartially()),
        null,
        getConfirmation(command),
        null,
        Popup.createPopup(command));
    if (command.getOnclick() != null) {
      script = command.getOnclick();
    }
  }

  public Command(FacesContext facesContext, UIComponent facetComponent, String focusId) {
    if (facetComponent instanceof UIForm && facetComponent.getChildCount() == 1) {
      Deprecation.LOG.warn("Please don't use a form, but a command with immediate=true instead.");
      facetComponent = facetComponent.getChildren().get(0);
    }
    this.action = facetComponent.getClientId(facesContext);
    // transition == true is the default
    if (!ComponentUtils.getBooleanAttribute(facetComponent, Attributes.TRANSITION)) {
      this.transition = Boolean.FALSE;
    }
    String target = ComponentUtils.getStringAttribute(facetComponent, Attributes.TARGET);
    if (target != null) {
      this.target = target;
    }
    if (facetComponent instanceof AbstractUICommand
        && ((AbstractUICommand) facetComponent).getRenderedPartially().length > 0) {
      this.partially = HtmlRendererUtils.getComponentIdsAsList(
          facesContext, facetComponent, ((UICommand) facetComponent).getRenderedPartially());
    } else {
      String facetAction = (String) facetComponent.getAttributes().get(Attributes.ONCLICK);
      if (facetAction != null) {
        // Replace @autoId
        facetAction = StringUtils.replace(facetAction, "@autoId", facetComponent.getClientId(facesContext));
        // XXX this case is deprecated.
        // not allowed with Content Security Policy (CSP)
        this.script = facetAction;
      }
      if (focusId != null) {
        this.focus = focusId;
      }
    }

    int delay = ComponentUtils.getIntAttribute(facetComponent, Attributes.DELAY);
    if (delay > 0) {
      this.delay = delay;
    }
  }

  private static String getConfirmation(AbstractUICommandBase command) {
    final ValueHolder facet = (ValueHolder) command.getFacet(Facets.CONFIRMATION);
    return facet != null ? "" + facet.getValue() : null;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
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
   * @deprecated Script will not work when CSP is activated
   */
  public String getScript() {
    return script;
  }

  /**
   * @deprecated Script will not work when CSP is activated
   */
  @Deprecated
  public void setScript(String script) {
    this.script = script;
  }

}

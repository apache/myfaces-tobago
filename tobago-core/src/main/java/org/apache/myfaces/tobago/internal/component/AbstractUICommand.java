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

import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.SupportsAccessKey;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.event.CollapsibleActionListener;
import org.apache.myfaces.tobago.internal.util.AuthorizationHelper;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.MethodExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractUICommand extends UICommand
    implements SupportsAccessKey, OnComponentPopulated, Visual, ClientBehaviorHolder {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUICommand.class);

  // todo generate
  private static final Collection<String> EVENT_NAMES = Arrays.asList("click", "change");

  enum PropertyKeys {
    disabled,
  }

  // todo: transient
  private Boolean parentOfCommands;

  @Override
  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {

    final List<AbstractUIOperation> list = ComponentUtils.findDescendantList(this, AbstractUIOperation.class);
    for (AbstractUIOperation operation : list) {
      addActionListener(new CollapsibleActionListener(operation.getFor()));
    }
  }

  @Override
  public void processDecodes(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException();
    }

    // Skip processing if our rendered flag is false
    if (!isRendered()) {
      return;
    }

    // Process this component itself
    try {
      decode(context);
    } catch (final RuntimeException e) {
      context.renderResponse();
      throw e;
    }

    final Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      final UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(context);
    }
  }

  @Override
  public void queueEvent(final FacesEvent facesEvent) {
    // fix for TOBAGO-262
    super.queueEvent(facesEvent);
    if (this == facesEvent.getSource()) {
      if (isImmediate()) {
        facesEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
      } else {
        facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
      }
    }
  }

  public boolean isParentOfCommands() {
    if (parentOfCommands == null) {
      parentOfCommands = false;
      for (UIComponent child : getChildren()) {
        if (child instanceof UICommand || child instanceof UIInput) {
          parentOfCommands = true;
          break;
        }
      }
    }
    return parentOfCommands;
  }

  /**
   Flag indicating that this element is disabled.
   <br>Default: <code>false</code>
   */
  public boolean isDisabled() {

    final FacesContext facesContext = getFacesContext();
    // todo: get from configuration tobago-config.xml
    if (true) {
      final AuthorizationHelper authorizationHelper = AuthorizationHelper.getInstance(facesContext);
      final MethodExpression actionExpression = getActionExpression();
      if (actionExpression != null) {
        final boolean authorized =
            authorizationHelper.isAuthorized(facesContext, actionExpression.getExpressionString());
        if (!authorized) {
          return true;
        }
      }
    }

    Boolean bool = (Boolean) getStateHelper().eval(PropertyKeys.disabled);
    if (bool != null) {
      return bool;
    }
    return false;
  }

  public void setDisabled(boolean disabled) {
    getStateHelper().put(PropertyKeys.disabled, disabled);
  }

  // todo generate
  @Override
  public String getDefaultEventName() {
    return "click";
  }

  // todo generate
  @Override
  public Collection<String> getEventNames() {
    return EVENT_NAMES;
  }

  @Override
  public abstract String getLabel();

  public abstract boolean isJsfResource();

  public abstract String getResource();

  public abstract String getLink();

  public abstract String getTarget();

  public abstract boolean isTransition();

  public abstract boolean isOmit();

  public abstract String getTip();

//  public abstract Integer getTabIndex();
}

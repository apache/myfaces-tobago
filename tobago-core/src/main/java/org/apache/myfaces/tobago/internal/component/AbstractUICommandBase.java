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

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.event.CollapsibleActionListener;
import org.apache.myfaces.tobago.internal.util.AuthorizationHelper;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.MethodExpression;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PhaseId;
import javax.faces.event.PostAddToViewEvent;
import java.util.Iterator;
import java.util.List;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractUICommandBase extends UICommand
    implements ComponentSystemEventListener {

  @Override
  public void processEvent(ComponentSystemEvent event) {
    super.processEvent(event);

    if (event instanceof PostAddToViewEvent) {
      final List<AbstractUIOperation> list = ComponentUtils.findDescendantList(this, AbstractUIOperation.class);
      for (AbstractUIOperation operation : list) {
        addActionListener(new CollapsibleActionListener(operation.getFor()));
      }
    }
  }

  @Override
  public void processDecodes(final FacesContext context) {
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

  @Override
  public boolean isRendered() {
    return super.isRendered() && isAllowed();
  }

  /**
   Flag indicating that this element is disabled.
   <br>Default: <code>false</code>
   */
  public boolean isDisabled() {

    if (!isAllowed()) {
      return true;
    }

    Boolean bool = (Boolean) getStateHelper().eval(AbstractUICommand.PropertyKeys.disabled);
    if (bool != null) {
      return bool;
    }
    return false;
  }

  private boolean isAllowed() {
    final FacesContext facesContext = getFacesContext();
    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);
    if (tobagoConfig.isCheckSecurityAnnotations()) {
      final AuthorizationHelper authorizationHelper = AuthorizationHelper.getInstance(facesContext);
      final MethodExpression actionExpression = getActionExpression();
      if (actionExpression != null) {
        final boolean authorized =
            authorizationHelper.isAuthorized(facesContext, actionExpression.getExpressionString());
        if (!authorized) {
          return false;
        }
      }
    }
    return true;
  }

  public void setDisabled(boolean disabled) {
    getStateHelper().put(AbstractUICommand.PropertyKeys.disabled, disabled);
  }

  public abstract String getTarget();

  public abstract boolean isTransition();

  public abstract boolean isOmit();

  public abstract void setOmit(boolean omit);

  public abstract String getConfirmation();

  public abstract String getLink();
}

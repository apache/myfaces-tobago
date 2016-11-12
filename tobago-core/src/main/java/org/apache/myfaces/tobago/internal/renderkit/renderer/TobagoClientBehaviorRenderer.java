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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.internal.behavior.EventBehavior;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIOperation;
import org.apache.myfaces.tobago.internal.renderkit.Collapse;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import java.util.Collection;
import java.util.List;

public class TobagoClientBehaviorRenderer extends javax.faces.render.ClientBehaviorRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoClientBehaviorRenderer.class);

  public static final String COMMAND_MAP = TobagoClientBehaviorRenderer.class.getName() + ".CommandMap";

  @Override
  public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {

    final FacesContext facesContext = behaviorContext.getFacesContext();
    final UIComponent uiComponent = behaviorContext.getComponent();
    final ClientBehaviors eventName = ClientBehaviors.valueOf(behaviorContext.getEventName());

    //// TBD: is this nice? May be implemented with a JSF behaviour?
    final Collapse collapse = createCollapsible(facesContext, uiComponent);

    String executeIds = null;
    String renderIds = null;
    Boolean transition = null;
    String target = null;
    String actionId = null;
    boolean omit = false;
    if (behavior instanceof AjaxBehavior) {
      AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;
      final Collection<String> execute = ajaxBehavior.getExecute();
      final Collection<String> render = ajaxBehavior.getRender();
      final String clientId = uiComponent.getClientId(facesContext);

      executeIds
          = ComponentUtils.evaluateClientIds(facesContext, uiComponent, execute.toArray(new String[execute.size()]));
      if (executeIds != null) {
        executeIds = executeIds + " " + clientId;
      } else {
        executeIds = clientId;
      }
      if (uiComponent instanceof AbstractUICommand) { // <f:ajax> inside of a command
        AbstractUICommand command = (AbstractUICommand) uiComponent;
        transition = command.isTransition();
        target = command.getTarget();
        omit = command.isOmit() || StringUtils.isNotBlank(RenderUtils.generateUrl(facesContext, command));
      }
      renderIds =
          ComponentUtils.evaluateClientIds(facesContext, uiComponent, render.toArray(new String[render.size()]));
      actionId = clientId;
    } else if (behavior instanceof EventBehavior) { // <tc:event>
      AbstractUIEvent event = findEvent(uiComponent, eventName);
      transition = event.isTransition();
      target = event.getTarget();
      actionId = event.getClientId(facesContext);
      omit = event.isOmit() || StringUtils.isNotBlank(RenderUtils.generateUrl(facesContext, event));
    } else {
      LOG.warn("Unknown behavior '{}'!", behavior.getClass().getName());
    }

    final Command command = new Command(
        actionId,
        transition,
        target,
        executeIds,
        renderIds,
        null,
        null, // getConfirmation(command), // todo
        null,
        collapse,
        omit);

    final CommandMap map = new CommandMap();
    map.addCommand(eventName, command);
    facesContext.getAttributes().put(COMMAND_MAP, map);

    // XXX the return value is a string, but we should use a CommandMap
    return COMMAND_MAP;
  }

  private AbstractUIEvent findEvent(final UIComponent component, final ClientBehaviors eventName) {
    for (UIComponent child : component.getChildren()) {
      if (child instanceof AbstractUIEvent) {
        AbstractUIEvent event = (AbstractUIEvent) child;
        if (eventName == event.getEvent()) {
          return event;
        }
      }
    }
    return null;
  }

  @Override
  public void decode(FacesContext context, UIComponent component, ClientBehavior behavior) {
    AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;
    if (ajaxBehavior.isDisabled() || !component.isRendered()) {
      return;
    }

    dispatchBehaviorEvent(component, ajaxBehavior);
  }

  private void dispatchBehaviorEvent(UIComponent component, AjaxBehavior ajaxBehavior) {

    AjaxBehaviorEvent event = new AjaxBehaviorEvent(component, ajaxBehavior);
    boolean isImmediate = isImmediate(ajaxBehavior, component);
    event.setPhaseId(isImmediate ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION);
    component.queueEvent(event);
  }

  private boolean isImmediate(AjaxBehavior ajaxBehavior, UIComponent component) {
    if (ajaxBehavior.isImmediateSet()) {
      return ajaxBehavior.isImmediate();
    }
    if (component instanceof EditableValueHolder) {
      return ((EditableValueHolder) component).isImmediate();
    }
    if (component instanceof ActionSource) {
      return ((ActionSource) component).isImmediate();
    }
    return false;
  }

  /**
   * @deprecated TBD
   */
  public static Collapse createCollapsible(FacesContext facesContext, UIComponent component) {
    //// TBD: is this nice? May be implemented with a JSF behaviour?
    //// BEGIN

    // XXX too complicated
    final List<AbstractUIOperation> operations =
        ComponentUtils.findDescendantList(component, AbstractUIOperation.class);
    Collapse collapse = null;
    if (operations.size() > 0) {
      final AbstractUIOperation operation = operations.get(0);
      final String forId = ComponentUtils.evaluateClientId(facesContext, component, operation.getFor());
      collapse = new Collapse(Collapse.Action.valueOf(operation.getName()), forId);
    }

    //// TBD: is this nice?
    //// END
    return collapse;
  }

}

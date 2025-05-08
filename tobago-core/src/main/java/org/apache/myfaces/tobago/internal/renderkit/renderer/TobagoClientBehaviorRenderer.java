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
import org.apache.myfaces.tobago.component.SupportFieldId;
import org.apache.myfaces.tobago.internal.behavior.EventBehavior;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUIOperation;
import org.apache.myfaces.tobago.internal.renderkit.Collapse;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.component.ActionSource;
import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.behavior.AjaxBehavior;
import jakarta.faces.component.behavior.ClientBehavior;
import jakarta.faces.component.behavior.ClientBehaviorContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.PhaseId;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TobagoClientBehaviorRenderer extends jakarta.faces.render.ClientBehaviorRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * In standard JSF this method returns a JavaScript string. Because of CSP, Tobago doesn't render JavaScript
   * into the HTML content. It transports the return value a bit hacky by {@link CommandMap#storeCommandMap}.
   *
   * @return "dummy" string or null, if nothing to do.
   */
  @Override
  public String getScript(final ClientBehaviorContext behaviorContext, final ClientBehavior behavior) {

    final FacesContext facesContext = behaviorContext.getFacesContext();
    final UIComponent uiComponent = behaviorContext.getComponent();
    final ClientBehaviors eventName = ClientBehaviors.getEnum(behaviorContext.getEventName());

    //// TBD: is this nice? May be implemented with a JSF behavior?
    Collapse collapse = createCollapsible(facesContext, uiComponent);

    String executeIds = null;
    String renderIds = null;
    Boolean transition = null;
    String target = null;
    String clientId = null;
    String fieldId = null;
    boolean omit = false;
    boolean resetValues = false;
    Integer delay = null;
    Boolean stopPropagation = null;
    String customEventName = null;
    boolean immediate = false;

    final String confirmation = ComponentUtils.getConfirmation(uiComponent);
    if (behavior instanceof AjaxBehavior) {
      final AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;
      if (ajaxBehavior.isDisabled()) {
        return null;
      }
      resetValues = ajaxBehavior.isResetValues();
      String delayStr = ajaxBehavior.getDelay();
      if (delayStr != null) {
        if (!"none".equalsIgnoreCase(delayStr)) {
          try {
            delay = Integer.parseInt(delayStr);
          } catch (NumberFormatException e) {
            LOG.warn("Wrong delay attribute format '{}' of AjaxBehavior '{}' clientId '{}'!",
                delayStr, eventName, clientId);
          }
        }
      }
      final Collection<String> execute = ajaxBehavior.getExecute();
      final Collection<String> render = ajaxBehavior.getRender();
      clientId = uiComponent.getClientId(facesContext);
      if (uiComponent instanceof SupportFieldId) {
        fieldId = ((SupportFieldId) uiComponent).getFieldId(facesContext);
      }

      executeIds = ComponentUtils.evaluateClientIds(facesContext, uiComponent, execute.toArray(new String[0]));
      if (executeIds == null) {
        executeIds = clientId;
      } else if (!executeIds.contains(clientId)) {
        executeIds = executeIds + " " + clientId;
      }
      if (uiComponent instanceof AbstractUICommand) { // <f:ajax> inside of a command
        final AbstractUICommand command = (AbstractUICommand) uiComponent;
        transition = command.isTransition();
        target = command.getTarget();
        omit = command.isOmit() || StringUtils.isNotBlank(RenderUtils.generateUrl(facesContext, command));
      }
      renderIds = ComponentUtils.evaluateClientIds(facesContext, uiComponent, render.toArray(new String[0]));
      immediate = ajaxBehavior.isImmediate();
    } else if (behavior instanceof EventBehavior) { // <tc:event>
      final EventBehavior eventBehavior = (EventBehavior) behavior;
      final AbstractUIEvent event = RenderUtils.getAbstractUIEvent(uiComponent, eventBehavior);

      if (event != null) {
        if (!event.isRendered() || event.isDisabled()) {
          return null;
        } else {
          transition = event.isTransition();
          target = event.getTarget();
          clientId = event.getClientId(facesContext);
          if (collapse == null) {
            collapse = createCollapsible(facesContext, event);
          }
          omit = event.isOmit() || StringUtils.isNotBlank(RenderUtils.generateUrl(facesContext, event));
          stopPropagation = event.getStopPropagation();
          customEventName = event.getCustomEventName();
          immediate = event.isImmediate();
        }
      }
    } else {
      LOG.warn("Unknown behavior '{}'!", behavior.getClass().getName());
    }

    final Command command = new Command(
        clientId,
        fieldId,
        transition,
        target,
        executeIds,
        renderIds,
        confirmation,
        delay,
        collapse,
        omit,
        stopPropagation,
        customEventName,
        immediate);

    if (resetValues) {
      command.setResetValues(true);
    }

    final CommandMap map = new CommandMap();
    map.addCommand(eventName, command);
    CommandMap.storeCommandMap(facesContext, map);

    // XXX the return value is a string, but we should use a CommandMap
    return "dummy";
  }

  @Override
  public void decode(
      final FacesContext facesContext, final UIComponent component,
      final ClientBehavior clientBehavior) {
    if (clientBehavior instanceof AjaxBehavior) {
      AjaxBehavior ajaxBehavior = (AjaxBehavior) clientBehavior;
      if (!component.isRendered() || ajaxBehavior.isDisabled()) {
        return;
      }

      dispatchBehaviorEvent(component, ajaxBehavior);
    } else if (clientBehavior instanceof EventBehavior) {
      final EventBehavior eventBehavior = (EventBehavior) clientBehavior;
      final AbstractUIEvent abstractUIEvent = RenderUtils.getAbstractUIEvent(component, eventBehavior);

      if (!component.isRendered() || abstractUIEvent == null
          || !abstractUIEvent.isRendered() || abstractUIEvent.isDisabled()) {
        return;
      }

      for (List<ClientBehavior> children : abstractUIEvent.getClientBehaviors().values()) {
        for (ClientBehavior child : children) {
          decode(facesContext, component, child);
        }
      }
      dispatchBehaviorEvent(component, eventBehavior);
    }
  }

  private void dispatchBehaviorEvent(final UIComponent component, final ClientBehavior clientBehavior) {
    final AjaxBehaviorEvent event = new AjaxBehaviorEvent(component, clientBehavior);
    final boolean isImmediate = isImmediate(clientBehavior, component);
    event.setPhaseId(isImmediate ? PhaseId.APPLY_REQUEST_VALUES : PhaseId.INVOKE_APPLICATION);
    component.queueEvent(event);
  }

  private boolean isImmediate(final ClientBehavior clientBehavior, final UIComponent component) {
    if (clientBehavior instanceof AjaxBehavior) {
      AjaxBehavior ajaxBehavior = (AjaxBehavior) clientBehavior;
      if (ajaxBehavior.isImmediateSet()) {
        return ajaxBehavior.isImmediate();
      }
    } else if (clientBehavior instanceof EventBehavior) {
      EventBehavior eventBehavior = (EventBehavior) clientBehavior;
      if (eventBehavior.isImmediateSet()) {
        return eventBehavior.isImmediate();
      }
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
   * @deprecated TBD: find a better way to implement this.
   */
  @Deprecated(since = "3.0.0")
  public static Collapse createCollapsible(final FacesContext facesContext, final UIComponent component) {
    //// TBD: is this nice? May be implemented with a JSF behavior?
    //// BEGIN

    // XXX too complicated
    final List<AbstractUIOperation> operations = new ArrayList<>();
    for (final UIComponent child : component.getChildren()) {
      if (AbstractUIOperation.class.isAssignableFrom(child.getClass())) {
        operations.add((AbstractUIOperation) child);
      }
    }

    Collapse collapse = null;
    if (operations.size() > 0) {
      final AbstractUIOperation operation = operations.get(0);
      final ValueExpression valueExpression = operation.getValueExpression("for");
      final String value;
      if (valueExpression != null) {
        value = (String) valueExpression.getValue(facesContext.getELContext());
      } else {
        value = operation.getFor();
      }
      final String forId = ComponentUtils.evaluateClientId(facesContext, component, value);
      collapse = new Collapse(Collapse.Operation.valueOf(operation.getName()), forId);
    }

    //// TBD: is this nice?
    //// END
    return collapse;
  }

}

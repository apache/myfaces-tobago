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

import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIOperation;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.render.ClientBehaviorRenderer;
import java.util.Collection;
import java.util.List;

public class AjaxClientBehaviorRenderer extends ClientBehaviorRenderer {

  @Override
  public String getScript(ClientBehaviorContext behaviorContext, ClientBehavior behavior) {

    final AjaxBehavior ajaxBehavior = (AjaxBehavior) behavior;
    final FacesContext facesContext = behaviorContext.getFacesContext();
    final Collection<String> execute = ajaxBehavior.getExecute();
    final Collection<String> render = ajaxBehavior.getRender();
    final UIComponent uiComponent = behaviorContext.getComponent();

    //// TBD: is this nice? May be implemented with a JSF behaviour?
    final Collapse collapse = createCollapsible(facesContext, uiComponent);

    final UIComponent component;
    if (uiComponent instanceof AbstractUICommand) {
      component = uiComponent;
    } else {
      final UIComponent facetComponent = uiComponent.getFacet(behaviorContext.getEventName());
      if (facetComponent instanceof AbstractUICommand) {
        component = facetComponent;
      } else {
        component = uiComponent;
      }
    }

    boolean omit = component instanceof AbstractUICommand &&
        (((AbstractUICommand) component).isOmit()
            // if it is a link, the default submit must not be called.
            || StringUtils.isNotBlank(RenderUtils.generateUrl(facesContext, (AbstractUICommand) component)));

    final String clientId = component.getClientId(facesContext);
    String executeIds =
        ComponentUtils.evaluateClientIds(facesContext, component, execute.toArray(new String[execute.size()]));
    if (executeIds != null) {
      executeIds = executeIds + " " + clientId;
    } else {
      executeIds = clientId;
    }
    final Command command = new Command(
        clientId,
        (component instanceof AbstractUICommand) ? ((AbstractUICommand) component).isTransition() : null,
        (component instanceof AbstractUICommand) ? ((AbstractUICommand) component).getTarget() : null,
        executeIds,
        ComponentUtils.evaluateClientIds(facesContext, component, render.toArray(new String[render.size()])),
        null,
        null, // getConfirmation(command), // todo
        null,
        collapse,
        omit);

    final CommandMap map = new CommandMap();
    map.addCommand(behaviorContext.getEventName(), command);
    return JsonUtils.encode(map);

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

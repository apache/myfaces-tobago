package org.apache.myfaces.tobago.renderkit;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_CONFIRMATION;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public abstract class CommandRendererBase extends RendererBase {

  private static final Log LOG = LogFactory.getLog(CommandRendererBase.class);

  public void decode(FacesContext facesContext, UIComponent component) {
//    boolean active = false;
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }
    String actionId = ComponentUtil.findPage(component).getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
    }
    String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("clientId = '" + clientId + "'");
    }
    if (actionId != null && actionId.equals(clientId)) {

      if (LOG.isDebugEnabled()) {
        LOG.debug("queueEvent");
      }
//      active = true;
      component.queueEvent(new ActionEvent(component));
//      ((UICommand) component).fireActionEvent(facesContext);

//      UIForm form = ComponentUtil.findForm(component);
//      if (form != null) {
//        form.setSubmitted(true);
//        if (LOG.isDebugEnabled()) {
//          LOG.debug("setting Form Active: " + form.getClientId(facesContext));
//        }
//      }
    }
//    if (component instanceof UICommand) {
//      ((UICommand)component).setActive(active);
//    }
  }

  public static String appendConfirmationScript(String onclick,
      UIComponent component, FacesContext facesContext) {
    ValueHolder confirmation
        = (ValueHolder) component.getFacet(FACET_CONFIRMATION);
    if (confirmation != null) {
      if (onclick != null) {
        onclick = "confirm('" + confirmation.getValue() + "') && " + onclick;
      } else {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Facet '" + FACET_CONFIRMATION + "' is not supported for "
              + "this type of button. id = '"
              + component.getClientId(facesContext) + "'");
        }
      }
    }
    return onclick;
  }

}

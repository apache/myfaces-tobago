/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
  * Created 14.04.2003 at 15:10:58.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import static org.apache.myfaces.tobago.TobagoConstants.*;

public abstract class CommandRendererBase extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CommandRendererBase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

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

// ///////////////////////////////////////////// bean getter + setter

}

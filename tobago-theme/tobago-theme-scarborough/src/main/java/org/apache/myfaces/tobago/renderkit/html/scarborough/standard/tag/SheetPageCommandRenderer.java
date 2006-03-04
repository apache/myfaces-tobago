package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;
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
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.event.PageAction;
import org.apache.myfaces.tobago.event.PageActionEvent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;


public class SheetPageCommandRenderer extends LinkRenderer {

  private final Log LOG = LogFactory.getLog(SheetPageCommandRenderer.class);

  public static final String PAGE_RENDERER_TYPE = "SheetPageCommand";

  public void decode(FacesContext facesContext, UIComponent component) {
    String actionId = ComponentUtil.findPage(component).getActionId();
    String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
      LOG.debug("clientId = '" + clientId + "'");
    }
    if (actionId != null && actionId.equals(clientId)) {

      PageAction action;
      try {
        action = PageAction.valueOf(component.getId());
      } catch (Exception e) {
        LOG.error("Illegal value for PageAction :" + component.getId());
        return;
      }
      PageActionEvent event
          = new PageActionEvent((UIData) component.getParent(), action);

      switch(action) {
        case ToPage:
        case ToRow:
          Map map = facesContext.getExternalContext().getRequestParameterMap();
          Object value = map.get(clientId + SUBCOMPONENT_SEP + "value");
          try {
            event.setValue(Integer.parseInt((String) value));
          } catch (Exception e) {
            LOG.error("Can't parse value for action " + action.name() + ": " + value);
          }
          break;
        default:
          // nothing more to do
      }

      component.queueEvent(event);
    }
  }


}

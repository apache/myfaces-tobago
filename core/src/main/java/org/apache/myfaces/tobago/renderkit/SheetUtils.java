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

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.event.PageAction;
import org.apache.myfaces.tobago.event.PageActionUtil;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.component.ComponentUtil;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.Map;

/*
 * Date: 24.04.2006
 * Time: 22:02:49
 */

//TODO move this back in the decode or SheetCommandRenderer
public class SheetUtils {
  private static final Log LOG = LogFactory.getLog(SheetUtils.class);

  public static void decode(FacesContext facesContext, UIComponent component) {
    String actionId = ComponentUtil.findPage(facesContext, component).getActionId();
    String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
      LOG.debug("clientId = '" + clientId + "'");
    }
    if (actionId != null && actionId.equals(clientId)) {

      PageAction action;
      try {
        action = PageActionUtil.parse(component.getId());
        if (action == null) {
          LOG.error("Illegal value for PageAction :" + component.getId());
          return;
        }
      } catch (Exception e) {
        LOG.error("Illegal value for PageAction :" + component.getId());
        return;
      }
      PageActionEvent event = new PageActionEvent((UIData) component.getParent(), action);

      switch (action) {
        case TO_PAGE:
        case TO_ROW:
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

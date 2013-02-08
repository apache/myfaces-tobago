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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.event.PageAction;
import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

public class SheetPageCommandRenderer extends LinkRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(SheetPageCommandRenderer.class);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    String actionId = FacesContextUtils.getActionId(facesContext);
    String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
      LOG.debug("clientId = '" + clientId + "'");
    }
    if (actionId != null && actionId.equals(clientId)) {

      PageAction action;
      try {
        action = PageAction.parse(component.getId());
      } catch (Exception e) {
        LOG.error("Illegal value for PageAction :" + component.getId());
        return;
      }
      PageActionEvent event = new PageActionEvent(component.getParent(), action);

      switch (action) {
        case TO_PAGE:
        case TO_ROW:
          Map map = facesContext.getExternalContext().getRequestParameterMap();
          Object value = map.get(clientId + ComponentUtils.SUB_SEPARATOR + "value");
          try {
            event.setValue(Integer.parseInt((String) value));
          } catch (NumberFormatException e) {
            LOG.error("Can't parse integer value for action " + action.name() + ": " + value);
          }
          break;
        default:
          // nothing more to do
      }
      component.queueEvent(event);
    }
  }
}

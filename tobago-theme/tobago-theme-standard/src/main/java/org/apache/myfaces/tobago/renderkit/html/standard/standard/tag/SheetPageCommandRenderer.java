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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
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
  public void decode(final FacesContext facesContext, final UIComponent component) {
    final String actionId = FacesContextUtils.getActionId(facesContext);
    final String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("javax.faces.source");
    final String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
      LOG.debug("sourceId = '" + sourceId + "'");
      LOG.debug("clientId = '" + clientId + "'");
    }

    // XXX todo: remove actionId
    if (actionId != null && actionId.equals(clientId) || sourceId != null && sourceId.equals(clientId)) {

      final String id = sourceId != null ? sourceId : actionId;

      final PageAction action = (PageAction) ComponentUtils.getAttribute(component, Attributes.pageAction);
      final PageActionEvent event = new PageActionEvent(component.getParent(), action);

      switch (action) {
        case TO_PAGE:
        case TO_ROW:
          Integer target = (Integer) ComponentUtils.getAttribute(component, Attributes.pagingTarget);
          if (target == null) {
            final Map map = facesContext.getExternalContext().getRequestParameterMap();
            final Object value = map.get(id + ComponentUtils.SUB_SEPARATOR + "value");
            try {
              target = Integer.parseInt((String) value);
            } catch (final NumberFormatException e) {
              LOG.error("Can't parse integer value for action " + action.name() + ": " + value);
              break;
            }
          }
          event.setValue(target);
          break;
        default:
          // nothing more to do
      }
      component.queueEvent(event);
    }
  }
}

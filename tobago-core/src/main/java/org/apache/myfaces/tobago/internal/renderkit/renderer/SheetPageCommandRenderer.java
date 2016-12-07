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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.event.SheetAction;
import org.apache.myfaces.tobago.event.PageActionEvent;
import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.Map;

public class SheetPageCommandRenderer extends LinkRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(SheetPageCommandRenderer.class);

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    final String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("javax.faces.source");
    final String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("sourceId = '" + sourceId + "'");
      LOG.debug("clientId = '" + clientId + "'");
    }

    if (clientId.equals(sourceId)) {

      final SheetAction action = (SheetAction) ComponentUtils.getAttribute(component, Attributes.sheetAction);
      ActionEvent event = null;

      switch (action) {
        case first:
        case prev:
        case next:
        case last:
          event = new PageActionEvent(component.getParent(), action);
          break;
        case toPage:
        case toRow:
          event = new PageActionEvent(component.getParent(), action);
          Integer target = (Integer) ComponentUtils.getAttribute(component, Attributes.pagingTarget);
          if (target == null) {
            final Map map = facesContext.getExternalContext().getRequestParameterMap();
            final Object value = map.get(clientId);
            try {
              target = Integer.parseInt((String) value);
            } catch (final NumberFormatException e) {
              LOG.error("Can't parse integer value for action " + action.name() + ": " + value);
              break;
            }
          }
          ((PageActionEvent) event).setValue(target);
          break;
        case sort:
          final UIColumn column = (UIColumn) component.getParent();
          final UIData data = (UIData) column.getParent();
          event = new SortActionEvent(data, column);
          break;
        default:
          LOG.error("Unknown action '{}' found!", action);
      }
      component.queueEvent(event);
    }
  }
}

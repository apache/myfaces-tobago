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

package org.apache.myfaces.tobago.event;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.event.AbortProcessingException;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import java.io.Serializable;

/*
 * Date: Dec 23, 2006
 * Time: 10:59:53 AM
 */
// TODO implement StateHolder
public class PopupActionListener implements ActionListener, Serializable {

  private static final Log LOG = LogFactory.getLog(PopupActionListener.class);

  private String popupId;

  private ValueBinding popupIdBinding;

  private boolean facet;


  public PopupActionListener() {
  }

  public PopupActionListener(String popupId) {

    if (UIComponentTag.isValueReference(popupId)) {
      popupIdBinding = ComponentUtil.createValueBinding(popupId);
    } else {
      this.popupId = popupId;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Add ActionListener: " + popupId);
    }
  }

  public PopupActionListener(UIPopup popup) {
    facet = true;
  }

  public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIPopup popup = null;
    if (facet) {
      UIComponent component = actionEvent.getComponent().getFacet(TobagoConstants.FACET_POPUP);
      if (component instanceof UIPopup) {
        popup = (UIPopup) component;
      }
      if (popup == null) {
        LOG.error("Found no popup facet in component "
              + actionEvent.getComponent().getClientId(facesContext));
      }
    } else {
      String id;
      if (popupIdBinding != null) {
        id = (String) popupIdBinding.getValue(facesContext);
      } else {
        id = popupId;
      }
      popup = (UIPopup) ComponentUtil.findComponent(actionEvent.getComponent(), id);
      if (popup == null) {
        LOG.error("Found no popup for \""
          + (popupIdBinding != null ? popupIdBinding.getExpressionString() + "\" := \"" : "")
          + id + "\"! Search base componentId : "
              + actionEvent.getComponent().getClientId(facesContext));
      }
    }
    if (popup != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("activated "
            + actionEvent.getComponent().getClientId(facesContext));
      }
      popup.setActivated(true);
    } 
  }

}

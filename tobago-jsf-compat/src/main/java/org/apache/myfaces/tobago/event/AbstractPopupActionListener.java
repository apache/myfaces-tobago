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

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPopupActionListener implements ActionListener {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractPopupActionListener.class);

  public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
    UIComponent popup = getPopup(actionEvent);
    if (popup != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("activated "
            + actionEvent.getComponent().getClientId(FacesContext.getCurrentInstance()));
      }
      // XXX this is called via reflection, because the class AbstractUIPopup is not available here.
      try {
        BeanUtils.setProperty(popup, "activated", true);
      } catch (IllegalAccessException e) {
        LOG.error("", e);
      } catch (InvocationTargetException e) {
        LOG.error("", e);
      }
    }
  }

  protected abstract UIComponent getPopup(ActionEvent actionEvent);
}

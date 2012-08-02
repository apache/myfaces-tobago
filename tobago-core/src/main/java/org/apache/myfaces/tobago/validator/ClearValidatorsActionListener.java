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

package org.apache.myfaces.tobago.validator;

/*
 * Created 22.10.2003 16:57:20.
 * $Id$
 */

import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import java.util.StringTokenizer;

public class ClearValidatorsActionListener implements ActionListener {

  private static final Logger LOG = LoggerFactory.getLogger(ClearValidatorsActionListener.class);

  public PhaseId getPhaseId() {
    return PhaseId.APPLY_REQUEST_VALUES;
  }

  public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionEvent = '" + actionEvent + "'");
    }
    UIComponent source = actionEvent.getComponent();
    String clearValidatorsFieldIds
        = (String) ComponentUtils.findParameter(source, "clearValidatorsFieldIds");

    if (LOG.isDebugEnabled()) {
      LOG.debug("clearValidatorsFieldIds = '" + clearValidatorsFieldIds + "'");
    }

    // FIXME: finding mechanism??? JSF ???

    for (StringTokenizer tokenizer
        = new StringTokenizer(clearValidatorsFieldIds, ",");
         tokenizer.hasMoreTokens();) {
      String clearValidatorsFieldId = tokenizer.nextToken();

      UIComponent component = source.findComponent(clearValidatorsFieldId);
      if (LOG.isDebugEnabled()) {
        LOG.debug("component = '" + component + "'");
      }

      if (component == null) { // not found locally
        if (LOG.isDebugEnabled()) {
          LOG.debug("Component not found locally, asking the tree.");
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        component = facesContext.getViewRoot().findComponent(clearValidatorsFieldId);
      }

      if (component == null) { // not found 
        LOG.warn("Component not found.");
      } else {
//        component.clearValidators();
        LOG.error("NO LONGER AVAILABLE: component.clearValidators();");
      }
    }
  }

}

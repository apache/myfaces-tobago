package org.apache.myfaces.tobago.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;

public class DebugNavigationHandler extends NavigationHandler {

  private static final Logger LOG = LoggerFactory.getLogger(DebugNavigationHandler.class);

  private NavigationHandler navigationHandler;

  public DebugNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Handle Navigation context: " + facesContext + " fromAction: '"
          + fromAction + "' outcome: '" + outcome + "'");
    }

    if (outcome != null && AjaxUtils.isAjaxRequest(facesContext)) {
        LOG.warn("An AJAX-Request should not have an outcome set: outcome='" + outcome + "'");
    }

    navigationHandler.handleNavigation(facesContext, fromAction, outcome);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Handled Navigation context: " + facesContext + " fromAction: '"
          + fromAction + "' outcome: '" + outcome + "'");
    }

  }
}

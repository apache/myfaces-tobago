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

package org.apache.myfaces.tobago.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;

import java.lang.invoke.MethodHandles;

public class DebugNavigationHandler extends NavigationHandler {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private NavigationHandler navigationHandler;

  public DebugNavigationHandler(final NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  @Override
  public void handleNavigation(final FacesContext facesContext, final String fromAction, final String outcome) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Handle Navigation context: " + facesContext + " fromAction: '"
          + fromAction + "' outcome: '" + outcome + "'");
    }

    // TBD: is this correct?
    if (outcome != null && facesContext.getPartialViewContext().isAjaxRequest()) {
      LOG.warn("An AJAX-Request should not have an outcome set: outcome='" + outcome + "'");
    }

    navigationHandler.handleNavigation(facesContext, fromAction, outcome);

    if (LOG.isDebugEnabled()) {
      LOG.debug("Handled Navigation context: " + facesContext + " fromAction: '"
          + fromAction + "' outcome: '" + outcome + "'");
    }

  }
}

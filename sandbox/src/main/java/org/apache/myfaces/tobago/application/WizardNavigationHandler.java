package org.apache.myfaces.tobago.application;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.model.Wizard;
import org.apache.myfaces.tobago.util.DebugNavigationHandler;

import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class WizardNavigationHandler extends NavigationHandler {

  @SuppressWarnings("UnusedDeclaration")
  private static final Log LOG = LogFactory.getLog(DebugNavigationHandler.class);

  private NavigationHandler base;

  public WizardNavigationHandler(NavigationHandler base) {
    this.base = base;
  }

  public void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {

    // todo: you may find a better indicator
    Wizard wizard = (Wizard) facesContext.getViewRoot().getAttributes().get(Wizard.class.getName());
    if (wizard != null) {

      String newViewId = wizard.getViewId();
      ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
      UIViewRoot viewRoot = viewHandler.createView(facesContext, newViewId);
      facesContext.setViewRoot(viewRoot);
      facesContext.renderResponse();

    } else {
      base.handleNavigation(facesContext, fromAction, outcome);
    }
  }
}

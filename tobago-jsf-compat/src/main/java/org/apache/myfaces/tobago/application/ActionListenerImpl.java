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

package org.apache.myfaces.tobago.application;

/*
 * Created 22.11.2004 18:33:44.
 * $Id$
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.FacesException;

public class ActionListenerImpl implements ActionListener {

  private static final Logger LOG = LoggerFactory.getLogger(ActionListenerImpl.class);

  private ActionListener base;

  private String errorOutcome = "error";

  public ActionListenerImpl(ActionListener base) {
    this.base = base;
  }

  public void processAction(ActionEvent event) throws AbortProcessingException {
    try {
      base.processAction(event);
    } catch (Throwable e) {
      if (e instanceof FacesException) {
        Throwable fe = e;
        while (fe != null) {
          if (fe instanceof AbortProcessingException) {
            throw (FacesException) e;
          }
          fe = fe.getCause();
        }
      }
      LOG.error("Processing failed. Forwarding to error page. errorOutcome=" + errorOutcome, e.getCause());
      FacesContext facesContext = FacesContext.getCurrentInstance();
      if (e.getCause() != null) {
         FacesMessage facesMessage = new FacesMessage(e.getCause().toString());
         facesContext.addMessage(null, facesMessage);
      }
      UIComponent source = event.getComponent();
      ActionSource actionSource = (ActionSource) source;
      Application application = facesContext.getApplication();
      MethodBinding binding = actionSource.getAction();
      // Retrieve the NavigationHandler instance..
      NavigationHandler navHandler = application.getNavigationHandler();
      // Invoke nav handling..
      String navBinding = (null != binding) ? binding.getExpressionString() : null;
      navHandler.handleNavigation(facesContext, navBinding, errorOutcome);
      // Trigger a switch to Render Response if needed
      facesContext.renderResponse();
    }
  }

  public String getErrorOutcome() {
    return errorOutcome;
  }

  public void setErrorOutcome(String errorOutcome) {
    this.errorOutcome = errorOutcome;
  }
}

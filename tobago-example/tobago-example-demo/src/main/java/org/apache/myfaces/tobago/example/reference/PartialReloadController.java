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

package org.apache.myfaces.tobago.example.reference;

import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.example.demo.NavigationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Date;

public class PartialReloadController {

  private static final Logger LOG = LoggerFactory.getLogger(PartialReloadController.class);

  private String navigateAction;

  @Inject
  private NavigationState navigationState;

  public Date getCurrentDate() {
    return new Date();
  }

  public String reload() {
    return logAndNavigate(null);
  }

  public String error() {
    throw new RuntimeException("Test Exception");
  }
  
  public String waitAndReload3() {
    return waitAndReload(3000);
  }

  public String waitAndReload7() {
    return waitAndReload(7000);
  }

  private String waitAndReload(final long delay) {
    synchronized (this) {
      try {
        wait(delay);
      } catch (final InterruptedException e) {
        //
      }
    }
    return logAndNavigate(null);
  }

  public String navigateAction() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();

    // in case of both the select control is not processed during lifecycle
    // we need to get the value from the request params
//    navigateAction = (String) facesContext.getExternalContext().getRequestParameterMap().get("page:navSelect");

    AjaxUtils.removeAjaxComponent(facesContext, "page:navTest");

    if (navigationState == null) {
      final ELContext elContext = facesContext.getELContext();
      final ValueExpression expression = facesContext.getApplication().getExpressionFactory()
          .createValueExpression(elContext, "#{navigationState}", NavigationState.class);
      navigationState = (NavigationState) expression.getValue(elContext);
    }

    LOG.info("navigateAction = \"" + navigateAction + "\"");
    if (navigateAction == null) {
      return logAndNavigate(null);
    } else if ("left".equals(navigateAction)) {
      AjaxUtils.addAjaxComponent(facesContext, "page:left");
      navigateAction = null;
      return logAndNavigate(null);
    } else if ("right".equals(navigateAction)) {
      AjaxUtils.addAjaxComponent(facesContext, "page:right");
      navigateAction = null;
      return logAndNavigate(null);
    } else if ("both".equals(navigateAction)) {
      AjaxUtils.addAjaxComponent(facesContext, "page:left");
      AjaxUtils.addAjaxComponent(facesContext, "page:right");
      navigateAction = null;
      return logAndNavigate(null);
    } else if ("parent".equals(navigateAction)) {
      navigateAction = null;
      AjaxUtils.addAjaxComponent(facesContext, "page:parent");
      return logAndNavigate(null);
    } else if ("prev".equals(navigateAction)) {
      navigateAction = null;
      return logAndNavigate(navigationState.gotoPrevious());
    } else if ("next".equals(navigateAction)) {
      navigateAction = null;
      return logAndNavigate(navigationState.gotoNext());
    }
    return logAndNavigate(null);
  }

  private String logAndNavigate(final String navValue) {
    LOG.info("Return navigate value: " + navValue + "");
    return navValue;
  }


  public String getNavigateAction() {
    return navigateAction;
  }

  public void setNavigateAction(final String navigateAction) {
    this.navigateAction = navigateAction;
  }
}

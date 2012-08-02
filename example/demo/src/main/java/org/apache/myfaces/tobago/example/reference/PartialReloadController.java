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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.tobago.example.demo.Navigation;

import javax.faces.context.FacesContext;
import javax.faces.el.VariableResolver;
import java.util.Date;

public class PartialReloadController {

  private static final Log LOG = LogFactory.getLog(PartialReloadController.class);

  private String navigateAction;

  public Date getCurrentDate() {
    return new Date();
  }

  public String leftAction() {
    return logAndNavigate(null);
  }

  public String rightAction() {
    return logAndNavigate(null);
  }

  public String navigateAction() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    VariableResolver resolver = facesContext.getApplication().getVariableResolver();
    Navigation navigation = (Navigation) resolver.resolveVariable(facesContext, "navigation");
    LOG.info("navigateAction = \"" + navigateAction + "\"");
    if (navigateAction == null) {
      return logAndNavigate(null);
    } else if ("both".equals(navigateAction)) {
      navigateAction = null;
      return logAndNavigate(null);
    } else if ("prev".equals(navigateAction)) {
      navigateAction = null;
      return logAndNavigate(navigation.gotoPrevious());
    } else if ("next".equals(navigateAction)) {
      navigateAction = null;
      return logAndNavigate(navigation.gotoNext());
    }
    return logAndNavigate(null);
  }

  private String logAndNavigate(String navValue) {
    LOG.info("Return navigate value: " + navValue + "");
    /*try {
      //((HttpServletResponse) FacesContext.getCurrentInstance()
      //.getExternalContext().getResponse()).sendRedirect("/tobago-example-demo/index.jsp");
      //FacesContext.getCurrentInstance().responseComplete();
      AjaxUtils.redirect(FacesContext.getCurrentInstance(), "/tobago-example-demo/index.jsp");

    } catch (IOException e) {
      LOG.error("", e);
    }*/
    return navValue;
  }


  public String getNavigateAction() {
    return navigateAction;
  }

  public void setNavigateAction(String navigateAction) {
    this.navigateAction = navigateAction;
  }
}

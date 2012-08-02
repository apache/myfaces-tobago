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

package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Iterator;

public class UIForm extends javax.faces.component.UIForm {

  private static final Log LOG = LogFactory.getLog(UIForm.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Form";
  public static final String SUBMITTED_MARKER = COMPONENT_TYPE + ".InSubmitted";

  public void processDecodes(FacesContext facesContext) {

    // Process this component first
    // to know the active actionId
    // for the following childrend
    decode(facesContext);

    Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(facesContext);
    }
  }

  public void setSubmitted(boolean b) {
    super.setSubmitted(b);

    // set submitted for all subforms
    for (UIForm subForm : ComponentUtil.findSubForms(this)) {
      subForm.setSubmitted(b);
    }
  }

  public void processValidators(FacesContext facesContext) {
    // if we're not the submitted form, only process subforms.
    if (LOG.isDebugEnabled()) {
      LOG.debug("processValidators for form: " + getClientId(facesContext));
    }
    if (!isSubmitted()) {
      for (UIForm subForm : ComponentUtil.findSubForms(this)) {
        subForm.processValidators(facesContext);
      }
    } else {
      // Process all facets and children of this component
      Iterator kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        UIComponent kid = (UIComponent) kids.next();
        kid.processValidators(facesContext);
      }
    }
  }

  public void processUpdates(FacesContext facesContext) {
    // if we're not the submitted form, only process subforms.
    if (LOG.isDebugEnabled()) {
      LOG.debug("processUpdates for form: " + getClientId(facesContext));
    }
    if (!isSubmitted()) {
      for (UIForm subForm : ComponentUtil.findSubForms(this)) {
        subForm.processUpdates(facesContext);
      }
    } else {
      // Process all facets and children of this component
      Iterator kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        UIComponent kid = (UIComponent) kids.next();
        kid.processUpdates(facesContext);
      }
    }
  }
}

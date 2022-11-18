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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.util.AjaxUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIForm;
import jakarta.faces.context.FacesContext;

import java.lang.invoke.MethodHandles;
import java.util.Iterator;

/**
 * Base class for form and page.
 */
public abstract class AbstractUIFormBase extends UIForm implements Visual {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void processDecodes(final FacesContext facesContext) {

    // Process this component first
    // to know the active actionId
    // for the following children
    decode(facesContext);

    final Iterator<UIComponent> kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      final UIComponent kid = kids.next();
      kid.processDecodes(facesContext);
    }
  }

  @Override
  public void setSubmitted(final boolean b) {
    super.setSubmitted(b);

    // set submitted for all subforms
    for (final AbstractUIForm subForm : ComponentUtils.findSubForms(this)) {
      subForm.setSubmitted(b);
    }
  }

  @Override
  public void processValidators(final FacesContext facesContext) {
    // if we're not the submitted form, only process subforms.
    if (LOG.isDebugEnabled()) {
      LOG.debug("processValidators for form: {}", getClientId(facesContext));
    }
    if (isSubmitted() || AjaxUtils.isAjaxRequest(facesContext)) {
      // Process all facets and children of this component
      final Iterator<UIComponent> kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        final UIComponent kid = kids.next();
        kid.processValidators(facesContext);
      }
    } else {
      for (final AbstractUIForm subForm : ComponentUtils.findSubForms(this)) {
        subForm.processValidators(facesContext);
      }
    }
  }

  @Override
  public void processUpdates(final FacesContext facesContext) {
    // if we're not the submitted form, only process subforms.
    if (LOG.isDebugEnabled()) {
      LOG.debug("processUpdates for form: {}", getClientId(facesContext));
    }
    if (isSubmitted() || AjaxUtils.isAjaxRequest(facesContext)) {
      // Process all facets and children of this component
      final Iterator<UIComponent> kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        final UIComponent kid = kids.next();
        kid.processUpdates(facesContext);
      }
    } else {
      for (final AbstractUIForm subForm : ComponentUtils.findSubForms(this)) {
        subForm.processUpdates(facesContext);
      }
    }
  }
}

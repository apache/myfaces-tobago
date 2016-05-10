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

import org.apache.myfaces.tobago.component.Form;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.FacesVersion;
import org.apache.myfaces.tobago.util.TobagoCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.Iterator;

public abstract class AbstractUIForm extends UIForm implements Form {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIForm.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Form";
  public static final String SUBMITTED_MARKER = COMPONENT_TYPE + ".InSubmitted";

  @Override
  public void processDecodes(final FacesContext facesContext) {

    // Process this component first
    // to know the active actionId
    // for the following children
    decode(facesContext);

    final Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      final UIComponent kid = (UIComponent) kids.next();
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
    if (!isSubmitted()) {
      for (final AbstractUIForm subForm : ComponentUtils.findSubForms(this)) {
        subForm.processValidators(facesContext);
      }
    } else {
      // Process all facets and children of this component
      final Iterator kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        final UIComponent kid = (UIComponent) kids.next();
        kid.processValidators(facesContext);
      }
    }
  }

  @Override
  public void processUpdates(final FacesContext facesContext) {
    // if we're not the submitted form, only process subforms.
    if (LOG.isDebugEnabled()) {
      LOG.debug("processUpdates for form: {}", getClientId(facesContext));
    }
    if (!isSubmitted()) {
      for (final AbstractUIForm subForm : ComponentUtils.findSubForms(this)) {
        subForm.processUpdates(facesContext);
      }
    } else {
      // Process all facets and children of this component
      final Iterator kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        final UIComponent kid = (UIComponent) kids.next();
        kid.processUpdates(facesContext);
      }
    }
  }

  @Override
  public void queueEvent(FacesEvent event) {

    Class<?> wrapper = null;

    try {
      if (FacesVersion.isMyfaces()) {
        // XXX hack for MyFaces
        wrapper = Class.forName("javax.faces.component.UIData$FacesEventWrapper");
      } else if (FacesVersion.isMojarra()) {
        // XXX hack for Mojarra
        wrapper = Class.forName("javax.faces.component.WrapperEvent");
      } else {
        LOG.warn("Can't identify JSF implementation. Events in sheet may not work.");
      }
    } catch (ClassNotFoundException e) {
      LOG.error("Can't find specific wrapper class.", e);
    }

    if (event instanceof AjaxBehaviorEvent || (wrapper != null && wrapper.equals(event.getClass()))) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("event={}", event);
      }
      setSubmitted(true);
    }
    super.queueEvent(event);
  }

  @Override
  public boolean invokeOnComponent(final FacesContext context, final String clientId, final ContextCallback callback)
      throws FacesException {
    // TODO is this needed?
    if (callback instanceof TobagoCallback) {
      if (PhaseId.APPLY_REQUEST_VALUES.equals(((TobagoCallback) callback).getPhaseId())) {
        decode(context);
      }
    }
    context.getExternalContext().getRequestMap().put(AbstractUIForm.SUBMITTED_MARKER, isSubmitted());

    return super.invokeOnComponent(context, clientId, callback);
  }
}

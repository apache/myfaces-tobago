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
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public abstract class AbstractUIPage extends AbstractUIFormBase implements Visual, ClientBehaviorHolder {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIPage.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Page";

  public static final String FORM_ACCEPT_CHARSET = "utf-8";

  private String formId;

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  public String getFormId(final FacesContext facesContext) {
    if (formId == null) {
      formId = getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "form";
    }
    return formId;
  }

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    super.encodeBegin(facesContext);
    final UIComponent layoutManager = LayoutUtils.getLayoutManager(this);
    if (layoutManager != null) {
      layoutManager.encodeBegin(facesContext);
    }
  }

  @Override
  public void encodeChildren(final FacesContext facesContext) throws IOException {
    final UIComponent layoutManager = LayoutUtils.getLayoutManager(this);
    if (layoutManager != null) {
      layoutManager.encodeChildren(facesContext);
    } else {
      super.encodeChildren(facesContext);
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {
    final UIComponent layoutManager = LayoutUtils.getLayoutManager(this);
    if (layoutManager != null) {
      layoutManager.encodeEnd(facesContext);
    }
    super.encodeEnd(facesContext);
    if (LOG.isTraceEnabled()) {
      LOG.trace(DebugUtils.toString(this.getParent(), 0));
    }
  }

  @Override
  public void processDecodes(final FacesContext context) {

    decode(context);

    markSubmittedForm(context);

    // invoke processDecodes() on children
    for (final Iterator kids = getFacetsAndChildren(); kids.hasNext();) {
      final UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(context);
    }
  }

  public void markSubmittedForm(final FacesContext facesContext) {
    // find the form of the action command and set submitted to it and all
    // children

    final UIViewRoot viewRoot = facesContext.getViewRoot();

    // reset old submitted state
    setSubmitted(false);

    String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("javax.faces.source");
    UIComponent command = null;
    if (sourceId != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("sourceId = '" + sourceId + "'");
      }
      command = viewRoot.findComponent(sourceId);
    } else {
      LOG.warn("No sourceId found!");
    }

    // TODO: remove this if block if proven this never happens anymore
    if (command == null
        && sourceId != null && sourceId.matches(".*:\\d+:.*")) {
      // If currentActionId component was inside a sheet the id contains the
      // rowIndex and is therefore not found here.
      // We do not need the row here because we want just to find the
      // related form, so removing the rowIndex will help here.
      sourceId = sourceId.replaceAll(":\\d+:", ":");
      try {
        command = viewRoot.findComponent(sourceId);
        //LOG.info("command = \"" + command + "\"", new Exception());
      } catch (final Exception e) {
        // ignore
      }
    }

    if (LOG.isTraceEnabled()) {
      LOG.trace(sourceId);
      LOG.trace("command:{}", command);
      LOG.trace(DebugUtils.toString(viewRoot, 0));
    }

    if (command != null) {
      final AbstractUIFormBase form = ComponentUtils.findForm(command);
      form.setSubmitted(true);

      if (LOG.isTraceEnabled()) {
        LOG.trace("form:{}", form);
        LOG.trace(form.getClientId(facesContext));
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Illegal actionId! Render response...");
      }
      facesContext.renderResponse();
    }
  }
}

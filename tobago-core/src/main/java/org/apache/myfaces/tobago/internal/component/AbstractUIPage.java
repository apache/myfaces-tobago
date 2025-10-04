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

import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.component.behavior.ClientBehaviorContext;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import jakarta.faces.component.visit.VisitCallback;
import jakarta.faces.component.visit.VisitContext;
import jakarta.faces.component.visit.VisitResult;
import jakarta.faces.context.FacesContext;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.PageTagDeclaration}
 */
public abstract class AbstractUIPage extends AbstractUIFormBase implements ClientBehaviorHolder {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Deprecated(since = "4.4.0", forRemoval = true)
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Page";

  public static final Charset FORM_ACCEPT_CHARSET = StandardCharsets.UTF_8;

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
  public void processDecodes(final FacesContext context) {

    decode(context);

    markSubmittedForm(context);

    // invoke processDecodes() on children
    for (final Iterator<UIComponent> kids = getFacetsAndChildren(); kids.hasNext(); ) {
      final UIComponent kid = kids.next();
      kid.processDecodes(context);
    }
  }

  public void markSubmittedForm(final FacesContext facesContext) {
    // find the form of the action command and set submitted to it and all
    // children

    final UIViewRoot viewRoot = facesContext.getViewRoot();

    // reset old submitted state
    setSubmitted(false);

    String sourceId = facesContext.getExternalContext().getRequestParameterMap()
        .get(ClientBehaviorContext.BEHAVIOR_SOURCE_PARAM_NAME);
    UIComponent command = null;
    if (sourceId != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("sourceId = '" + sourceId + "'");
      }
      FindCommandVisitor visitor = new FindCommandVisitor();
      viewRoot.visitTree(VisitContext
              .createVisitContext(facesContext, Set.of(sourceId), ComponentUtils.SET_SKIP_UNRENDERED),
          visitor);
      command = visitor.getCommand();
    } else {
      LOG.warn("No sourceId found!");
    }

    if (LOG.isTraceEnabled()) {
      LOG.trace(sourceId);
      LOG.trace("command:{}", command);
      LOG.trace(DebugUtils.toString(viewRoot, 0));
    }

    if (command != null) {
      final AbstractUIFormBase form = ComponentUtils.findForm(command);
      if (form != null) {
        form.setSubmitted(true);
      } else {
        LOG.warn("No form found - this actually can not happen, because there is a form on each page!");
      }

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

  public abstract String getLabel();

  public abstract String getFocusId();

  private static class FindCommandVisitor implements VisitCallback {
    private UIComponent command;

    @Override
    public VisitResult visit(VisitContext context, UIComponent target) {
      command = target;
      return VisitResult.COMPLETE;
    }

    public UIComponent getCommand() {
      return command;
    }
  }

}

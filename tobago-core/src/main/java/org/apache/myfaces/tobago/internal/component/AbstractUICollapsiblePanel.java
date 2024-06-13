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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.model.CollapseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Base class for collapsible panels.
 */
public abstract class AbstractUICollapsiblePanel extends AbstractUIPanelBase {

  private transient Boolean submittedCollapsed;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void processDecodes(final FacesContext facesContext) {
    if (isNormalLifecycle()) {
      super.processDecodes(facesContext);
    } else {
      decode(facesContext);
      final UIComponent bar = getFacet(Facets.BAR); // XXX is from Box or Section
      if (bar != null) {
        bar.processDecodes(facesContext);
      }
      final UIComponent label = getFacet(Facets.LABEL); // XXX is from Box or Section
      if (label != null) {
        label.processDecodes(facesContext);
      }
    }
  }

  @Override
  public void processValidators(final FacesContext facesContext) {
    if (isNormalLifecycle()) {
      super.processValidators(facesContext);
    } else {
      final UIComponent bar = getFacet(Facets.BAR); // XXX is from Box or Section
      if (bar != null) {
        bar.processValidators(facesContext);
      }
      final UIComponent label = getFacet(Facets.LABEL); // XXX is from Box or Section
      if (label != null) {
        label.processValidators(facesContext);
      }
    }
  }

  @Override
  public void processUpdates(final FacesContext facesContext) {
    if (isNormalLifecycle()) {
      super.processUpdates(facesContext);
    } else {
      final UIComponent bar = getFacet(Facets.BAR); // XXX is from Box or Section
      if (bar != null) {
        bar.processUpdates(facesContext);
      }
      final UIComponent label = getFacet(Facets.LABEL); // XXX is from Box or Section
      if (label != null) {
        label.processUpdates(facesContext);
      }
    }
  }

  public boolean isNormalLifecycle() {
    return getCollapsedMode() == CollapseMode.hidden || !isCollapsed();
  }

  public abstract boolean isCollapsed();

  public abstract void setCollapsed(boolean collapsed);

  public abstract CollapseMode getCollapsedMode();

  public Boolean getSubmittedCollapsed() {
    return submittedCollapsed;
  }

  public void setSubmittedCollapsed(final Boolean submittedCollapsed) {
    this.submittedCollapsed = submittedCollapsed;
  }

  public void processState() {
    if (submittedCollapsed != null) {
      final ValueExpression valueExpression = getValueExpression(Attributes.collapsed.name());
      if (valueExpression != null) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        if (!valueExpression.isReadOnly(elContext)) {
          valueExpression.setValue(elContext, submittedCollapsed);
        } else {
          final boolean developmentMode = facesContext.isProjectStage(ProjectStage.Development);
          String msg
              = "Component clientId={} ValueExpression of collapsed attribute is readonly. Can not process state.";
          if (developmentMode) {
            LOG.warn(msg, getClientId(facesContext));
          } else {
            LOG.debug(msg, getClientId(facesContext));
          }
        }
      } else {
        setCollapsed(submittedCollapsed);
      }
      submittedCollapsed = null;
    }
  }
}

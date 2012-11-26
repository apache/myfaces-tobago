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
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.DeprecatedDimension;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.Position;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractUIPopup extends AbstractUIPanelBase
    implements OnComponentCreated, OnComponentPopulated, NamingContainer,
    DeprecatedDimension, Position, LayoutContainer {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIPopup.class);

  private static final String Z_INDEX = AbstractUIPopup.class.getName() + ".Z_INDEX";

  private boolean activated;

  public void onComponentCreated(FacesContext facesContext, UIComponent parent) {
    Integer zIndex = (Integer) facesContext.getExternalContext().getRequestMap().get(Z_INDEX);
    if (zIndex == null) {
      zIndex = 1;
    } else {
      zIndex++;
    }
    setZIndex(zIndex);
    facesContext.getExternalContext().getRequestMap().put(Z_INDEX, zIndex);
  }

  public void onComponentPopulated(FacesContext facesContext, UIComponent parent) {
    if (getLayoutManager() == null) {
      final AbstractUIGridLayout layoutManager = (AbstractUIGridLayout) CreateComponentUtils.createAndInitLayout(
          facesContext, ComponentTypes.GRID_LAYOUT, RendererTypes.GRID_LAYOUT, parent);
      setLayoutManager(layoutManager);
    }
  }

  public List<LayoutComponent> getComponents() {
    return LayoutUtils.findLayoutChildren(this);
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  @Override
  public void processDecodes(FacesContext facesContext) {
    if (isSubmitted()) {
      for (Iterator it = getFacetsAndChildren(); it.hasNext();) {
        UIComponent childOrFacet = (UIComponent) it.next();
        childOrFacet.processDecodes(facesContext);
      }
      try {
        decode(facesContext);
      } catch (RuntimeException e) {
        facesContext.renderResponse();
        throw e;
      }
      if (facesContext.getRenderResponse()) {
        setActivated(true);
      }
    }
  }

  @Override
  public boolean isRendered() {
    final ValueExpression expression = getValueExpression("rendered");
    if (expression != null) {
      FacesContext context = FacesContext.getCurrentInstance();
      return (Boolean) expression.getValue(context.getELContext());
    } else {
      return isActivated() || isRedisplay();
    }
  }

  private boolean isSubmitted() {
    String action = FacesContextUtils.getActionId(getFacesContext());
    return action != null && action.startsWith(getClientId(getFacesContext()) + SEPARATOR_CHAR);
  }

  private boolean isRedisplay() {
    if (isSubmitted()) {
      String action = FacesContextUtils.getActionId(getFacesContext());
      if (action != null) {
        UIComponent command = getFacesContext().getViewRoot().findComponent(SEPARATOR_CHAR + action);
        if (command != null && command instanceof UICommand) {
          return !(command.getAttributes().get(Attributes.POPUP_CLOSE) != null);
        }
      }
    }
    return false;
  }

  private boolean isActivated() {
    return activated;
  }


  @Override
  public void processValidators(FacesContext context) {
    if (isSubmitted()) {
      for (Iterator it = getFacetsAndChildren(); it.hasNext();) {
        UIComponent childOrFacet = (UIComponent) it.next();
        childOrFacet.processValidators(context);
      }
      //TODO: check if validation has failed and reset rendered if needed
      if (context.getRenderResponse()) {
        setActivated(true);
      }
    }
  }

  @Override
  public void processUpdates(FacesContext context) {
    if (isSubmitted()) {
      for (Iterator it = getFacetsAndChildren(); it.hasNext();) {
        UIComponent childOrFacet = (UIComponent) it.next();
        childOrFacet.processUpdates(context);
      }
    }
  }

  @Override
  public Object saveState(FacesContext context) {
    Object[] saveState = new Object[2];
    saveState[0] = super.saveState(context);
    saveState[1] = activated;
    return saveState;
  }

  @Override
  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    activated = (Boolean) values[1];
  }

  @Override
  public void encodeEnd(FacesContext context) throws IOException {
    super.encodeEnd(context);
    activated = false;
  }

  public LayoutManager getLayoutManager() {
    return (LayoutManager) getFacet(Facets.LAYOUT);
  }

  public void setLayoutManager(LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (AbstractUILayoutBase) layoutManager);
  }

  public boolean isLayoutChildren() {
    return isRendered();
  }
  
  public abstract Measure getWidth();

  public abstract void setWidth(Measure width);

  public abstract Measure getHeight();

  public abstract void setHeight(Measure height);

  public abstract Measure getTop();

  public abstract void setTop(Measure top);

  public abstract Measure getLeft();

  public abstract void setLeft(Measure left);

  public abstract void setZIndex(Integer zIndex);
}

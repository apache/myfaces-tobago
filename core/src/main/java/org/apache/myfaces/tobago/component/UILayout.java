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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import org.apache.myfaces.tobago.renderkit.LayoutRenderer;
import org.apache.myfaces.tobago.util.LayoutUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import java.io.IOException;

public abstract class UILayout extends UIComponentBase {

  private static final Log LOG = LogFactory.getLog(UILayout.class);

  public void layoutBegin(FacesContext facesContext, UIComponent component) {
    // prepare component to render
    prepareDimension(facesContext, component);

  }


  public static void prepareDimension(FacesContext facesContext, UIComponent component) {
//    LOG.info("prepareDimension for " + component.getClientId(facesContext) + " is " + component.getRendererType());
    setInnerWidth(facesContext, component);
    setInnerHeight(facesContext, component);
  }

  private static void setInnerWidth(FacesContext facesContext, UIComponent component) {
    Integer layoutWidth = LayoutUtil.getLayoutWidth(component);
    if (layoutWidth != null) {
      int space = layoutWidth.intValue();
      int innerSpace = LayoutUtil.getInnerSpace(facesContext, component, space, true);
      component.getAttributes().put(ATTR_INNER_WIDTH, Integer.valueOf(innerSpace));
    }
  }

  private static void setInnerHeight(FacesContext facesContext, UIComponent component) {
    Integer layoutHeight = LayoutUtil.getLayoutHeight(component);
    if (layoutHeight != null) {
      int space = layoutHeight.intValue();
      int innerSpace = LayoutUtil.getInnerSpace(facesContext, component, space, false);
      component.getAttributes().put(ATTR_INNER_HEIGHT, Integer.valueOf(innerSpace));
    }
  }


  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component) throws IOException {
    ((LayoutRenderer) getRenderer(facesContext)).encodeChildrenOfComponent(facesContext, component);
  }


  public static UILayout getLayout(UIComponent component) {
    UILayout layout = (UILayout) component.getFacet(FACET_LAYOUT);
    if (layout == null) {
      if (component instanceof LayoutProvider) {
        layout = ((LayoutProvider) component).provideLayout();
      } else {
        layout = UIDefaultLayout.getInstance();
      }
    }
    return layout;
  }
}

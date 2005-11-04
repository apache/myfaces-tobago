/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
  * Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class PanelRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(PanelRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    // wenn hoehe gesetzt dann diese,
    // sonst wenn layout vorhanden dieses fragen:
    //       -> aus rowLayout berechnen
    // sonst Warnung ausgebenn und addition der children's fixedHeight

    int height =
        ComponentUtil.getIntAttribute(component, ATTR_HEIGHT, -1);

    if (height == -1) {
      height = getFixedHeightForPanel(component, facesContext);
    }
    return height;
  }

  public static int getFixedHeightForPanel(UIComponent component, FacesContext facesContext) {
    int height = -1;
    // first ask layoutManager
    UIComponent layout = component.getFacet("layout");
    if (layout != null) {
      RendererBase renderer = ComponentUtil.getRenderer(facesContext, layout);
      height = renderer.getFixedHeight(facesContext, component);
    }
    if (height < 0) {

      if (component.getChildren().size() == 0) {
        height = 0;
      }
      else {

        if (LOG.isDebugEnabled()) {
          LOG.debug("Can't calculate fixedHeight! "
              + "using estimation by contained components. for "
              + component.getClientId(facesContext) + " = "
              + component.getClass().getName() + " "
              + component.getRendererType());
        }

        height = 0;
        for (Iterator iterator = component.getChildren().iterator(); iterator.hasNext();) {
          UIComponent child = (UIComponent) iterator.next();
          RendererBase renderer = ComponentUtil.getRenderer(facesContext, child);
          if (renderer == null
              && child instanceof UINamingContainer
              && child.getChildren().size() > 0) {
            // this is a subview component ??
            renderer = ComponentUtil.getRenderer(facesContext, (UIComponent) child.getChildren().get(0));
          }
          if (renderer != null) {
            int h = renderer.getFixedHeight(facesContext, child);
            if (h > 0) {
              height += h;
            }
          }
        }
      }
    }
    return height;
  }

  public void encodeChildrenTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIPanel component = (UIPanel) uiComponent ;
    for (Iterator i = component.getChildren().iterator(); i.hasNext(); ) {
      UIComponent child = (UIComponent) i.next();
      RenderUtil.encode(facesContext, child);
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

  }

}


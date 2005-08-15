/*
 * Copyright 2002-2005 atanion GmbH.
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
 * Created 09.03.2004 12:26:39.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UILayout;
import org.apache.myfaces.tobago.util.LayoutUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public abstract class BoxRendererBase extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(BoxRendererBase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }


  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    // wenn hoehe gesetzt dann diese,
    // sonst wenn layout vorhanden dieses fragen:
    //       -> aus rowLayout berechnen
    // sonst Warnung ausgebenn und addition der children's fixedHeight

    int height =
        ComponentUtil.getIntAttribute(component, TobagoConstants.ATTR_HEIGHT, -1);
    if (height != -1) {
      return height;
    }

    // ask layoutManager
    UIComponent layout = UILayout.getLayout(component);
    if (layout != null) {
      RendererBase renderer = ComponentUtil.getRenderer(facesContext, layout);
      height = renderer.getFixedHeight(facesContext, component);
      if (height > -1) {
        return height;
      }
    }

    if (LOG.isInfoEnabled()) {
      LOG.info("Can't calculate fixedHeight! "
          + "using estimation by contained components. ");
    }

    height = LayoutUtil.calculateFixedHeightForChildren(facesContext, component);
    height += getHeaderHeight(facesContext, component);
    height += getPaddingHeight(facesContext, component);
    return height;
  }

// ///////////////////////////////////////////// bean getter + setter

}

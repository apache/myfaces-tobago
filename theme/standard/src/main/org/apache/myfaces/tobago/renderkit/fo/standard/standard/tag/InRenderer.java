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
package org.apache.myfaces.tobago.renderkit.fo.standard.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created: Dec 1, 2004 7:42:47 PM
 * User: bommel
 * $Id$
 */
public class InRenderer extends InputRendererBase {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(InRenderer.class);

// ----------------------------------------------------------- business methods

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** end      " + component);
    }
    try {
      encodeEndTobago(facesContext, component);
    } catch (IOException e) {
      throw e;
    } catch (RuntimeException e) {
      LOG.error("catched " + e + " :" + e.getMessage(), e);
      throw e;
    } catch (Throwable e) {
      LOG.error("catched Throwable :", e);
      throw new RuntimeException(e);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("*   end      " + component);
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
        UIComponent component) throws IOException {
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);

    ResponseWriter writer = facesContext.getResponseWriter();
    Layout layout = Layout.getLayout(component.getParent());
    //Layout in = layout.createWithMargin(0,0,0,0);
    //in.setParent(layout);

    if (label != null) {
      if (!Layout.isInLayout(component)) {
        FoUtils.startBlockContainer(writer, component);   FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT,layout.getWidth()/2, layout.getX(), layout.getY() );
      }
      RenderUtil.encode(facesContext, label);
      if (!Layout.isInLayout(component)) {
        FoUtils.endBlockContainer(writer);
      }
    }

    //in.addMargin(200, 0, 200, 0);
    String text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }
    if (!Layout.isInLayout(component)) {
      FoUtils.startBlockContainer(writer, component); FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT, layout.getWidth()/2, layout.getX()+layout.getWidth()/2, layout.getY());
    }
    FoUtils.writeTextBlockAlignLeft(writer, component, "TextBox");
    if (!Layout.isInLayout(component)) {
      FoUtils.endBlockContainer(writer);
    }
    if (!Layout.isInLayout(component)) {
      layout.addMargin(200, 0, 0, 0);
    }
    }

  public boolean getRendersChildren() {
    return false;
  }
}


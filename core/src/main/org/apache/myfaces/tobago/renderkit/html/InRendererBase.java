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
  * Created 15.04.2003 at 09:26:24.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit.html;

import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public abstract class InRendererBase extends InputRendererBase {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(InRendererBase.class);

// ----------------------------------------------------------- business methods


  public boolean getRendersChildren() {
    return true;
  }

  protected abstract void renderMain(FacesContext facesContext, UIInput input,
      TobagoResponseWriter writer) throws IOException;

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException {
    super.encodeEndTobago(facesContext, component);
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();


    renderMain(facesContext, (UIInput) component, writer);
  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;
//    if (component.getFacet(TobagoConstants.FACET_LABEL) != null) {
//      int labelWidht = LayoutUtil.getLabelWidth(component);
//      space += labelWidht != 0 ? labelWidht : getLabelWidth(facesContext, component);
//      space += getConfiguredValue(facesContext, component, "labelSpace");
//    }
//    if (component.getFacet("picker") != null) {
//      int pickerWidth = getConfiguredValue(facesContext, component, "pickerWidth");
//      space += pickerWidth;
//    }
    return space;
  }
}


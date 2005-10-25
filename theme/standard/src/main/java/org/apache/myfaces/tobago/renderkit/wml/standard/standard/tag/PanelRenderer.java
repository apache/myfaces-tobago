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
  * Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit.wml.standard.standard.tag;

import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class PanelRenderer extends RendererBase {

  public boolean getRendersChildren() {
    return true;
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

    //UIPanel component = (UIPanel) uiComponent ;
    //BodyContentHandler bodyContentHandler = (BodyContentHandler)
    //    component.getAttributes().get(ATTR_BODY_CONTENT);

    //if (bodyContentHandler != null) {
    //  ResponseWriter writer = facesContext.getResponseWriter();
    //  writer.write(bodyContentHandler.getBodyContent());
    //}
  }

}


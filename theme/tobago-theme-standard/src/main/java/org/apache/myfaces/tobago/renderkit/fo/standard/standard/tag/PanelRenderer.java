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
package org.apache.myfaces.tobago.renderkit.fo.standard.standard.tag;

//import static org.apache.myfaces.tobago.TobagoConstants.ATTR_BODY_CONTENT;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Created: Nov 30, 2004 6:04:53 PM
 * User: bommel
 * $Id$
 */
public class PanelRenderer extends FoRendererBase {

  public boolean getRendersChildren() {
    return true;
  }
  public void encodeBegin(FacesContext facesContext,
       UIComponent uiComponent) throws IOException {
    Layout.putLayout(uiComponent, Layout.getLayout(uiComponent.getParent()));
    super.encodeBegin(facesContext, uiComponent);
  }

  public void encodeEndTobago(FacesContext facesContext,
       UIComponent uiComponent) throws IOException {

  }
}

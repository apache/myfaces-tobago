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
 * Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_BODY_CONTENT;
import org.apache.myfaces.tobago.component.BodyContentHandler;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class FormRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    UIForm form = (UIForm) component;
    UIPage page = ComponentUtil.findPage(form);
    String actionId = page.getActionId();
    String clientId = form.getClientId(facesContext);
    if (actionId.startsWith(clientId)) {
      form.setSubmitted(true);
    }
    super.decode(facesContext, form);
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    ResponseWriter writer = facesContext.getResponseWriter();

    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(ATTR_BODY_CONTENT);
    if (bodyContentHandler != null) {
      writer.write(bodyContentHandler.getBodyContent());
    }

  }
  
// ///////////////////////////////////////////// bean getter + setter

}


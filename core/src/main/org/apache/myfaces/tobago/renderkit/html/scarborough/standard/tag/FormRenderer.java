/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
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
        component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);
    if (bodyContentHandler != null) {
      writer.write(bodyContentHandler.getBodyContent());
    }

  }
  
// ///////////////////////////////////////////// bean getter + setter

}


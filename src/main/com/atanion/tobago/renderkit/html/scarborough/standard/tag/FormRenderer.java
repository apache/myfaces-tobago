/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.BodyContentHandler;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RendererBase;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class FormRenderer extends RendererBase implements DirectRenderer {

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

  public void encodeDirectEnd(FacesContext facesContext,
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


package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.RendererBase;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 29, 2004 7:02:04 PM
 * User: bommel
 * $Id$
 */
public class OutRenderer extends RendererBase {

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    String text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }
    Layout layout = Layout.getLayout(component.getParent());
    //layout.addMargin(200, 0, 0, 0);
    ResponseWriter writer = facesContext.getResponseWriter();
    FoUtils.writeTextBlockAlignLeft(writer, component, text);

  }

}

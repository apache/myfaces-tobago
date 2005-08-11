package org.apache.myfaces.tobago.renderkit.fo.scarborough.standard.tag;

import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import java.io.IOException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 1, 2004 7:49:55 PM
 * User: bommel
 * $Id$
 */
public class LabelRenderer extends FoRendererBase {

  public void encodeEndTobago(
      FacesContext facesContext, UIComponent component) throws IOException {

    UIOutput output = (UIOutput) component;


    ResponseWriter writer = facesContext.getResponseWriter();
    FoUtils.writeTextBlockAlignLeft(writer, component, "Label");
    if (output.getValue() != null) {
      //writer.writeText("Label", null);
    }
    //writer.endElement("fo:block");


  }

}

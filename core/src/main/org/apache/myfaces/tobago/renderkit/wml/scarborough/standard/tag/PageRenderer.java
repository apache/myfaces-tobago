/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package org.apache.myfaces.tobago.renderkit.wml.scarborough.standard.tag;

import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.StringWriter;

public class PageRenderer extends PageRendererBase {

  private static final String DOCTYPE =
      "<?xml version='1.0'?>\n" +
      "<!DOCTYPE wml PUBLIC '-//WAPFORUM//DTD WML 1.1//EN'\n" +
      " 'http://www.wapforum.org/DTD/wml_1.1.xml'>";

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException {

    UIPage page = (UIPage) component;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    // replace responseWriter and render page content
    StringWriter content = new StringWriter();
    ResponseWriter contentWriter = new TobagoResponseWriter(
        content, writer.getContentType(), writer.getCharacterEncoding());
    facesContext.setResponseWriter(contentWriter);

    RenderUtil.encodeChildren(facesContext, page);

    // reset responseWriter and render page
    facesContext.setResponseWriter(writer);

    writer.write(DOCTYPE);
    writer.write('\n');

    writer.startElement("wml", page);
    writer.startElement("card", page);

    writer.write(content.toString());

    writer.endElement("card");
    writer.endElement("wml");
  }
}


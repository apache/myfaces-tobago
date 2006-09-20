package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Sep 19, 2006
 * Time: 11:13:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class SeparatorRenderer extends RendererBase {
  private static final Log LOG = LogFactory.getLog(SeparatorRenderer.class);


  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    writer.startElement("hr", component);
    writer.endElement("hr");

  }
}

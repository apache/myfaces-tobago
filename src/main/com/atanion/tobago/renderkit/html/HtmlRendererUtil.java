package com.atanion.tobago.renderkit.html;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jan 11, 2005
 * Time: 4:59:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class HtmlRendererUtil {

  private static final Log LOG = LogFactory.getLog(HtmlRendererUtil.class);

  public static void renderFocusId(FacesContext facesContext, UIComponent component)
      throws IOException {

    if (ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_FOCUS)) {
      UIPage page = ComponentUtil.findPage(component);
      String id = component.getClientId(facesContext);
      if (page.getFocusId() != null && ! page.getFocusId().equals(id)) {
        LOG.warn(
          "page focusId = \"" + page.getFocusId() + "\" ignoring new value \""
          + id + "\"");
      }
      else {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("\n<!--\n");
        writer.write("focusId = '" + id + "';");
        writer.write("\n-->\n");
        writer.endElement("script");
      }
    }
  }
}

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
/*
  * Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_POPUP_RESET;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class PopupRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(PopupRenderer.class);

  public static final String CONTENT_ID_POSTFIX = SUBCOMPONENT_SEP + "content";

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent uiComponent) throws IOException {

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    UIPopup component = (UIPopup) uiComponent ;
    final String clientId = component.getClientId(facesContext);
    final String contentDivId = clientId + CONTENT_ID_POSTFIX;
    final String left = component.getLeft();
    final String top = component.getTop();

    final StringBuffer contentStyle = new StringBuffer();
    if (component.getWidth() != null) {
      contentStyle.append("width: " );
      contentStyle.append(component.getWidth());
      contentStyle.append("; ");
    }
    if (component.getHeight() != null) {
      contentStyle.append("height: " );
      contentStyle.append(component.getHeight());
      contentStyle.append("; ");
    }
    contentStyle.append("left: " );
    contentStyle.append(left != null ? left : "100");
    contentStyle.append("; ");
    contentStyle.append("top: " );
    contentStyle.append(top != null ? top : "50");
    contentStyle.append("; ");

    writer.startElement("img", component);
    writer.writeIdAttribute(clientId);
    writer.writeComponentClass();
    writer.writeAttribute("onclick", "tobagoPopupBlink('" + clientId + "')", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif"), null);
    writer.writeAttribute("galleryimg", "no", null);
    writer.endElement("img");
    if (ClientProperties.getInstance(facesContext).getUserAgent().isMsie()) {
      writer.startElement("iframe", component);
      writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + "iframe");
      writer.writeClassAttribute("tobago-popup-iframe");
      writer.writeAttribute("style", contentStyle.toString(), null);
      writer.endElement("iframe");
    }
    writer.startElement("div", component);
    writer.writeIdAttribute(contentDivId);
    writer.writeClassAttribute("tobago-popup-content");


    writer.writeAttribute("style", contentStyle.toString(), null);
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    UIPopup component = (UIPopup) uiComponent ;
    final String clientId = component.getClientId(facesContext);

    writer.endElement("div");

    String setupScript = "tobagoSetupPopup('" + clientId + "', '"
        + component.getLeft() + "', '" + component.getTop() + "');";
    HtmlRendererUtil.writeJavascript(writer, setupScript);

    if (ComponentUtil.getBooleanAttribute(component, ATTR_POPUP_RESET)) {
      component.setRendered(false);
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}


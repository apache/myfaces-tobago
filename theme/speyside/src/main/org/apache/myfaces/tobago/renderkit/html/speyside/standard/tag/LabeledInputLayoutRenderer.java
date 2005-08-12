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
package org.apache.myfaces.tobago.renderkit.html.speyside.standard.tag;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

/**
 * User: weber
 * Date: Feb 22, 2005
 * Time: 3:05:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class LabeledInputLayoutRenderer extends
    org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.LabeledInputLayoutRenderer {

  private static final Log LOG = LogFactory.getLog(LabeledInputLayoutRenderer.class);


  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component)
      throws IOException {

    UIInput input = (UIInput) component;
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();

    boolean inline = ComponentUtil.getBooleanAttribute(component, ATTR_INLINE);
    String image = ResourceManagerUtil.getImage(facesContext, "image/1x1.gif");
    UIComponent label = input.getFacet(FACET_LABEL);
    UIComponent picker = input.getFacet(FACET_PICKER);

    debugLayout(input, label, picker);

    ClientProperties client
        = ClientProperties.getInstance(FacesContext.getCurrentInstance());

    if (!inline) {
      writer.startElement("table", input);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.writeAttribute("title", null, ATTR_TIP);
      writer.startElement("tr", null);
      if (label != null) {
        writer.startElement("td", null);
        writer.writeClassAttribute("tobago-label-td");
        writer.writeAttribute("valign", "top", null);
        writer.writeText("", null); // to ensure that the start-tag is closed!
        RenderUtil.encode(facesContext, label);
        writer.endElement("td");
        writer.startElement("td", null);
        writer.startElement("img", null);
        writer.writeAttribute("src", image, null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("height", "1", null);
        writer.writeAttribute("width", "5", null);
        writer.endElement("img");
        writer.endElement("td");
      }
      writer.startElement("td", null);
      writer.writeAttribute("valign", "top", null);
      writer.writeAttribute("rowspan", "2", null);
      writer.writeText("", null); // to ensure that the start-tag is closed!
      renderComponent(facesContext, input);
      if (picker != null) {
        writer.endElement("td");
        writer.startElement("td", null);
        writer.writeAttribute("valign", "top", null);
        writer.writeAttribute("rowspan", "2", null);
        writer.writeAttribute("style", "padding-left: 5px;", null);

        renderPicker(facesContext, input, picker);
      }
      writer.endElement("td");
      writer.endElement("tr");
      if (client.getTheme().getName() == "sap") { // fixme: "sap"
        writer.startElement("tr", null);
        if (label != null) {
          writer.startElement("td", null);
          writer.writeClassAttribute("tobago-label-td-underline-label");
          writer.startElement("img", null);
          writer.writeAttribute("src", image, null);
          writer.writeAttribute("border", "0", null);
          writer.writeAttribute("height", "1", null);
          writer.endElement("img");
          writer.endElement("td");
          writer.startElement("td", null);
          writer.writeClassAttribute("tobago-label-td-underline-spacer");
          writer.startElement("img", null);
          writer.writeAttribute("src", image, null);
          writer.writeAttribute("border", "0", null);
          writer.writeAttribute("height", "1", null);
          writer.endElement("img");
          writer.endElement("td");
        }
        writer.endElement("tr");
      }
      writer.endElement("table");
    } else {
      renderComponent(facesContext, input);
      renderPicker(facesContext, input, picker);
    }
    HtmlRendererUtil.renderFocusId(facesContext, component);

  }

  private void debugLayout(UIInput input, UIComponent label, UIComponent picker) {
    LOG.debug("\n\n###########################################################################");
    LOG.debug("INPUT");
    debugAttributes(input);
    if (label != null) {
      LOG.debug("------------------------");
      LOG.debug("LABEL");
      debugAttributes(label);
    }
    if (picker!= null) {
      LOG.debug("------------------------");
      LOG.debug("PICKER");
      debugAttributes(picker);
    }
    LOG.debug("###########################################################################\n");
  }

  private void debugAttributes(UIComponent component) {
    Map attributes = component.getAttributes();
    for (Object o : attributes.keySet()) {
      LOG.debug("  " + o + "='" + attributes.get(o) + "'");
    }
  }
}

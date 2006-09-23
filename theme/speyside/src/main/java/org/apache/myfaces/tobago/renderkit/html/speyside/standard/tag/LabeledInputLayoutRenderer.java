package org.apache.myfaces.tobago.renderkit.html.speyside.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PICKER;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

/**
 * User: weber
 * Date: Feb 22, 2005
 * Time: 3:05:58 PM
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
    String image = ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");
    UIComponent label = input.getFacet(FACET_LABEL);
    UIComponent picker = input.getFacet(FACET_PICKER);

    debugLayout(input, label, picker);

    ClientProperties client
        = ClientProperties.getInstance(FacesContext.getCurrentInstance());

    if (!inline) {
      writer.startElement(HtmlConstants.TABLE, input);
      writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
      writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
      writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
      writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
      writer.startElement(HtmlConstants.TR, null);
      if (label != null) {
        writer.startElement(HtmlConstants.TD, null);
        writer.writeClassAttribute("tobago-label-td");
        writer.writeAttribute(HtmlAttributes.VALIGN, "top", null);
        writer.writeText("", null); // to ensure that the start-tag is closed!
        RenderUtil.encode(facesContext, label);
        writer.endElement(HtmlConstants.TD);
        writer.startElement(HtmlConstants.TD, null);
        writer.startElement(HtmlConstants.IMG, null);
        writer.writeAttribute(HtmlAttributes.SRC, image, null);
        writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
        writer.writeAttribute(HtmlAttributes.HEIGHT, "1", null);
        writer.writeAttribute(HtmlAttributes.WIDTH, "5", null);
        writer.endElement(HtmlConstants.IMG);
        writer.endElement(HtmlConstants.TD);
      }
      writer.startElement(HtmlConstants.TD, null);
      writer.writeAttribute(HtmlAttributes.VALIGN, "top", null);
      writer.writeAttribute(HtmlAttributes.ROWSPAN, "2", null);
      writer.writeText("", null); // to ensure that the start-tag is closed!
      renderComponent(facesContext, input);
      if (picker != null) {
        writer.endElement(HtmlConstants.TD);
        writer.startElement(HtmlConstants.TD, null);
        writer.writeAttribute(HtmlAttributes.VALIGN, "top", null);
        writer.writeAttribute(HtmlAttributes.ROWSPAN, "2", null);
        writer.writeAttribute(HtmlAttributes.STYLE, "padding-left: 5px;", null);

        renderPicker(facesContext, input, picker);
      }
      writer.endElement(HtmlConstants.TD);
      writer.endElement(HtmlConstants.TR);
      if ("sap".equals(client.getTheme().getName())) { // FIXME: "sap"
        writer.startElement(HtmlConstants.TR, null);
        if (label != null) {
          writer.startElement(HtmlConstants.TD, null);
          writer.writeClassAttribute("tobago-label-td-underline-label");
          writer.startElement(HtmlConstants.IMG, null);
          writer.writeAttribute(HtmlAttributes.SRC, image, null);
          writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
          writer.writeAttribute(HtmlAttributes.HEIGHT, "1", null);
          writer.endElement(HtmlConstants.IMG);
          writer.endElement(HtmlConstants.TD);
          writer.startElement(HtmlConstants.TD, null);
          writer.writeClassAttribute("tobago-label-td-underline-spacer");
          writer.startElement(HtmlConstants.IMG, null);
          writer.writeAttribute(HtmlAttributes.SRC, image, null);
          writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
          writer.writeAttribute(HtmlAttributes.HEIGHT, "1", null);
          writer.endElement(HtmlConstants.IMG);
          writer.endElement(HtmlConstants.TD);
        }
        writer.endElement(HtmlConstants.TR);
      }
      writer.endElement(HtmlConstants.TABLE);
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

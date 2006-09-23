package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PICKER;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_PICKER_POPUP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.renderkit.LabeledLayoutRender;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * User: weber
 * Date: Feb 22, 2005
 * Time: 3:05:58 PM
 */
public class LabeledInputLayoutRenderer extends DefaultLayoutRenderer implements LabeledLayoutRender{

  private static final Log LOG = LogFactory.getLog(LabeledInputLayoutRenderer.class);

  public String getDefaultLayoutOrder() {
    return "LCP";
  }

  public void layoutWidth(FacesContext facesContext, UIComponent component) {
    super.layoutWidth(facesContext, component.getParent());
  }

  public void layoutHeight(FacesContext facesContext, UIComponent component) {
    super.layoutHeight(facesContext, component.getParent());
  }

  public void prepareRender(FacesContext facesContext, UIComponent component) {
    super.prepareRender(facesContext, component);
  }



  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component)
      throws IOException {
    UIComponent label = component.getFacet(FACET_LABEL);
    UIComponent picker = component.getFacet(FACET_PICKER);
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();

    if (label != null || picker != null) {
      writer.startElement(HtmlConstants.TABLE, component);
      writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
      writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
      writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
      writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
      writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
      writer.startElement(HtmlConstants.TR, null);
      writer.startElement(HtmlConstants.TD, null);
      writer.writeText("", null);
    }
    if (label != null) {
      RenderUtil.encode(facesContext, label);

      writer.endElement(HtmlConstants.TD);
      writer.startElement(HtmlConstants.TD, null);
    }

    renderComponent(facesContext, component);


    if (picker != null) {
      writer.endElement(HtmlConstants.TD);
      writer.startElement(HtmlConstants.TD, null);
      writer.writeAttribute(HtmlAttributes.STYLE, "padding-left: 5px;", null);
      renderPicker(facesContext, component, picker);
    }

    if (label != null || picker != null) {
      writer.endElement(HtmlConstants.TD);
      writer.endElement(HtmlConstants.TR);
      writer.endElement(HtmlConstants.TABLE);
    }
    HtmlRendererUtil.renderFocusId(facesContext, component);

  }

  protected void renderComponent(FacesContext facesContext, UIComponent component)
      throws IOException {
    RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
    renderer.encodeBegin(facesContext, component);
    renderer.encodeChildren(facesContext, component);
    renderer.encodeEnd(facesContext, component);


  }

  protected void renderPicker(FacesContext facesContext, UIComponent input,
                              UIComponent picker) throws IOException {
    if (picker != null) {
      ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
//      String url
//          = viewHandler.getActionURL(facesContext, ResourceManagerUtil.getJsp(facesContext, "datePicker.jsp"))
//          + "?tobago.date.inputId="
//          + input.getClientId(facesContext);
//      String command = "calendarWindow('" + url + "');";
//      picker.getAttributes().put(ATTR_ACTION_STRING, command);
      RenderUtil.encode(facesContext, picker);

      UIPopup popup = (UIPopup) picker.getFacet(FACET_PICKER_POPUP);
      if (popup != null) {
        UIPage page = ComponentUtil.findPage(input);
        page.getPopups().add(popup);
      }
    }
  }

}

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
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.renderkit.LabeledLayoutRender;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
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
      writer.startElement("table", component);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.writeAttribute("title", null, ATTR_TIP);
      writer.startElement("tr", null);
      writer.startElement("td", null);
      writer.writeText("", null);
    }
    if (label != null) {
      RenderUtil.encode(facesContext, label);

      writer.endElement("td");
      writer.startElement("td", null);
    }

    renderComponent(facesContext, component);


    if (picker != null) {
      writer.endElement("td");
      writer.startElement("td", null);
      writer.writeAttribute("style", "padding-left: 5px;", null);
      renderPicker(facesContext, component, picker);
    }

    if (label != null || picker != null) {
      writer.endElement("td");
      writer.endElement("tr");
      writer.endElement("table");
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

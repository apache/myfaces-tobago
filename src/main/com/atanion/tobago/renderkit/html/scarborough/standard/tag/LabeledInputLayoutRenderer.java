package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.renderkit.LayoutRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.application.ViewHandler;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Feb 22, 2005
 * Time: 3:05:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class LabeledInputLayoutRenderer extends LayoutRenderer {

  private static final Log LOG = LogFactory.getLog(LabeledInputLayoutRenderer.class);

  public void layoutWidth(FacesContext facesContext, UIComponent component) {
    HtmlRendererUtil.layoutWidth(facesContext, component.getParent());
  }

  public void layoutHeight(FacesContext facesContext, UIComponent component) {
    HtmlRendererUtil.layoutHeight(facesContext, component.getParent());
  }


  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component)
      throws IOException {
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
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
      HtmlRendererUtil.encodeHtml(facesContext, label);

      writer.endElement("td");
      writer.startElement("td", null);
    }

    renderComponent(facesContext, (UIInput) component);


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
      String url
          = viewHandler.getActionURL(facesContext, ResourceManagerUtil.getJsp(facesContext, "datePicker.jsp"))
          + "?tobago.date.inputId="
          + input.getClientId(facesContext);
      String command = "calendarWindow('" + url + "');";
      picker.getAttributes().put(ATTR_ACTION_STRING, command);
      HtmlRendererUtil.createCssClass(facesContext, picker);
      RenderUtil.encode(facesContext, picker);
    }
  }
}

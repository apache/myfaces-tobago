package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.LabeledLayoutRender;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.application.ViewHandler;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * User: weber
 * Date: Feb 22, 2005
 * Time: 3:05:58 PM
 * To change this template use File | Settings | File Templates.
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

      UIComponent popup = picker.getFacet(FACET_PICKER_POPUP);
      if (popup != null) {
        final UIPage page = ComponentUtil.findPage(input);

        List popups = (List) page.getAttributes().get(ATTR_POPUP_LIST);
        if (popups == null) {
          popups = new ArrayList();
          page.getAttributes().put(ATTR_POPUP_LIST, popups);
        }
        popups.add(popup);
      }
    }
  }

}

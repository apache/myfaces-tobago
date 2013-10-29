package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.internal.component.AbstractUISplitLayout;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SplitLayoutRenderer extends GridLayoutRenderer {
  
  private static final Logger LOG = LoggerFactory.getLogger(SplitLayoutRenderer.class);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    String clientId = component.getClientId();
    Map<String,String> parameterMap = facesContext.getExternalContext().getRequestParameterMap();
    String position = parameterMap.get(clientId + "_spLP");
    ((AbstractUISplitLayout) component).updateLayout(Integer.parseInt(position));
  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
    LayoutContainer container = (LayoutContainer) ((AbstractUISplitLayout) component).getParent();
    if (!((LayoutContainer) container).isLayoutChildren()) {
      return;
    } else {
      List<LayoutComponent> components = container.getComponents();
      if (components.size() != 2) {
        LOG.warn("Illegal component count in splitLayout: {}", components.size());
      }
      RenderUtils.encode(facesContext, (UIComponent) components.get(0));
      RenderUtils.encode(facesContext, (UIComponent) components.get(1));
      encodeHandle(facesContext, (AbstractUISplitLayout) component);
    }
  }

  protected void encodeHandle(FacesContext facesContext, AbstractUISplitLayout layout) throws IOException {
    String id = layout.getClientId(facesContext);
    
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.SPAN, layout);
    writer.writeIdAttribute(id + "_spLH");
    writer.writeAttribute("data-tobago-split-layout", layout.getOrientation().toLowerCase(), true);
    Style style = calculateHandleStyle(layout);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(layout, layout.getOrientation().toLowerCase()));

    int position;
    if (AbstractUISplitLayout.HORIZONTAL.equals(layout.getOrientation())) {
      position = style.getLeft().getPixel();
    } else {
      position = style.getTop().getPixel();
    }
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeIdAttribute(id + "_spLP");
    writer.writeNameAttribute(id + "_spLP");
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeAttribute(HtmlAttributes.VALUE, Integer.toString(position), false);
    writer.endElement(HtmlElements.INPUT);


    writer.endElement(HtmlElements.SPAN);

  }

  private Style calculateHandleStyle(AbstractUISplitLayout layout) {
    LayoutContainer container = (LayoutContainer) ((AbstractUISplitLayout) layout).getParent();
    LayoutComponent secondComponent = container.getComponents().get(1);
    Style style = new Style();
    if (AbstractUISplitLayout.HORIZONTAL.equals(layout.getOrientation())) {
      style.setWidth(Measure.valueOf(5));
      style.setHeight(container.getCurrentHeight());
      style.setLeft(Measure.valueOf(secondComponent.getLeft().subtract(5)));
      style.setTop(Measure.valueOf(0));
    } else {
      style.setWidth(container.getCurrentWidth());
      style.setHeight(Measure.valueOf(5));
      style.setLeft(Measure.valueOf(0));
      style.setTop(Measure.valueOf(Measure.valueOf(secondComponent.getTop().subtract(5))));
    }
    style.setDisplay(Display.BLOCK);
    style.setPosition(Position.ABSOLUTE);
    return style;
  }
}

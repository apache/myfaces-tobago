package org.apache.myfaces.tobago.renderkit.html.sandbox.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class InputNumberSliderRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(InputNumberSliderRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    String id = component.getClientId(facesContext);
    String currentValue = getCurrentValue(facesContext, component);
    boolean readonly = ComponentUtil.getBooleanAttribute(component, ATTR_READONLY);
    boolean disabled = ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED);
    Integer min = ComponentUtil.getIntAttribute(component, "min");
    Integer max = ComponentUtil.getIntAttribute(component, "max");
    LOG.info("### min = " + min);
    LOG.info("### max = " + max);
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    HtmlStyleMap style = (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE);
    int width = -1;
    int sliderWidth = 100; // fixme
    int inputWidth = 50; // fixme;
    if (style != null) {
      width = style.getInt("width");
    }
    if (width >= 0) {
      sliderWidth = width * 2 / 3;
      inputWidth = width * 1 / 3;
    }

    writer.startElement(HtmlConstants.TABLE, component);
    writer.writeIdAttribute(id);
    writer.writeComponentClass();
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    //writer.writeAttribute("border","1",false);

    writer.startElement(HtmlConstants.TR);
    writer.startElement(HtmlConstants.TD);
    writer.writeClassAttribute("tobago-inputNumberSlider-min-default");
    writer.writeAttribute(HtmlAttributes.STYLE,
        HtmlRendererUtil.toStyleString("width", sliderWidth * 1 / 2), null);
    writer.startElement(HtmlConstants.SPAN);
    writer.writeClassAttribute("tobago-inputNumberSlider-min-default");
    writer.writeText(min, null);
    writer.endElement(HtmlConstants.SPAN);

    writer.endElement(HtmlConstants.TD);
    writer.startElement(HtmlConstants.TD);
    writer.writeClassAttribute("tobago-inputNumberSlider-max-default");
    writer.writeAttribute(HtmlAttributes.STYLE,
        HtmlRendererUtil.toStyleString("width", sliderWidth * 1 / 2), null);
    writer.startElement(HtmlConstants.SPAN);
    writer.writeClassAttribute("tobago-inputNumberSlider-max-default");
    writer.writeText(max, null);
    writer.endElement(HtmlConstants.SPAN);
    writer.endElement(HtmlConstants.TD);

    // the input field starts here
    writer.startElement(HtmlConstants.TD);
    writer.writeAttribute("rowspan", "2", false);
    writer.writeClassAttribute("tobago-inputNumberSlider-input-default");

    writer.startElement(HtmlConstants.INPUT);
    writer.writeClassAttribute("tobago-in-default");
    writer.writeAttribute(HtmlAttributes.STYLE,
        HtmlRendererUtil.toStyleString("width", inputWidth), null);        
    String inputIdAndName = getIdForInputField(facesContext, component);
    writer.writeNameAttribute(inputIdAndName);
    writer.writeIdAttribute(inputIdAndName);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, null);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    //writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    writer.endElement(HtmlConstants.INPUT);
    writer.endElement(HtmlConstants.TD);

    writer.endElement(HtmlConstants.TR);
    writer.startElement(HtmlConstants.TR);
    writer.startElement(HtmlConstants.TD);
    writer.writeClassAttribute("tobago-inputNumberSlider-slider-default");
    writer.writeAttribute("colspan", "2", false);
    //writer.writeAttribute("width", "200", false);
    writer.writeAttribute("height", "5", false);
    //writer.writeAttribute("bgcolor", "yellow", false);

    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);

    //HtmlRendererUtil.renderFocusId(facesContext, component);
  }

  public void decode(FacesContext context, UIComponent component) {
    UIInput uiInput;
    if (component instanceof UIInput && !ComponentUtil.isOutputOnly(component)) {
      uiInput = (UIInput) component;
    } else {
      return;
    }
    String inputId = getIdForInputField(context, component);
    Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(inputId)) {
      String newValue = (String) requestParameterMap.get(inputId);
      uiInput.setSubmittedValue(newValue);
    }
  }

  private String getIdForInputField(FacesContext context,
      UIComponent component) {
    String id = component.getClientId(context);
    return id + TobagoConstants.SUBCOMPONENT_SEP + "input";
  }


}

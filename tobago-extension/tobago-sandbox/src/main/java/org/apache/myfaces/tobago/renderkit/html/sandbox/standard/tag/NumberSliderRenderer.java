package org.apache.myfaces.tobago.renderkit.html.sandbox.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.component.AbstractUINumberSlider;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class NumberSliderRenderer extends LayoutComponentRendererBase {

  private static final String SLIDER_WIDTH_PERCENT = "sliderWidthPercent";

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
/* todo: use e. g. jQuery
    if (facesContext instanceof TobagoFacesContext) {
      final String[] scripts = new String[]{"script/scriptaculous.js"};
      ((TobagoFacesContext) facesContext).getScriptFiles().add(scripts[0]);
    }
*/
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    AbstractUINumberSlider slider = (AbstractUINumberSlider) component;
    
    String id = slider.getClientId(facesContext);
    String currentValue = getCurrentValue(facesContext, slider);
    boolean readonly = slider.isReadonly();
    boolean disabled = slider.isDisabled();
    Integer min = ComponentUtils.getIntAttribute(slider, "min");
    Integer max = ComponentUtils.getIntAttribute(slider, "max");
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    Style style = new Style(facesContext, slider);
    int width = -1;
    int sliderWidthPerc 
        = getResourceManager().getThemeMeasure(facesContext, slider, SLIDER_WIDTH_PERCENT).getPixel();
      if (sliderWidthPerc <= 25) {
        sliderWidthPerc = 25;
      }
      if (sliderWidthPerc >= 75) {
        sliderWidthPerc = 75;
      }
    int sliderWidth = 100; // fixme
    int inputWidth = 50; // fixme;
    if (style.getWidth() != null && style.getWidth().getPixel() >= 0) {
      sliderWidth = (width * sliderWidthPerc) / 100;
      inputWidth = (width * (100 - sliderWidthPerc)) / 100;
    }

    writer.startElement(HtmlElements.TABLE, slider);
    writer.writeIdAttribute(id);
    writer.writeClassAttribute(Classes.create(slider));
    writer.writeStyleAttribute(style);
    //writer.writeAttribute(HtmlAttributes.BORDER,"1",false);

    writer.startElement(HtmlElements.TR, null);
    writer.startElement(HtmlElements.TD, null);
    writer.writeClassAttribute(Classes.create(slider, "min"));

    Style widthStyle = new Style();
    widthStyle.setWidth(Measure.valueOf(sliderWidth / 2));
    writer.writeStyleAttribute(widthStyle);
    writer.startElement(HtmlElements.SPAN, null);
    writer.writeClassAttribute(Classes.create(slider, "min"));
    writer.write(Integer.toString(min));
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.TD);
    writer.startElement(HtmlElements.TD, null);
    writer.writeClassAttribute(Classes.create(slider, "max"));
    writer.writeStyleAttribute(widthStyle);
    writer.startElement(HtmlElements.SPAN, null);
    writer.writeClassAttribute(Classes.create(slider, "max"));
    writer.write(Integer.toString(max));
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.TD);

    // the input field starts here
    writer.startElement(HtmlElements.TD, null);
    writer.writeAttribute(HtmlAttributes.ROWSPAN, "2", false);
    writer.writeClassAttribute(Classes.create(slider, "td"));

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeClassAttribute(Classes.create(slider, "input"));
    widthStyle.setWidth(Measure.valueOf(inputWidth));
    writer.writeStyleAttribute(widthStyle);
    String inputIdAndName = getIdForInputField(facesContext, slider);
    writer.writeNameAttribute(inputIdAndName);
    writer.writeIdAttribute(inputIdAndName);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, false);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    //writer.writeAttribute(HtmlAttributes.STYLE, null, STYLE);
    writer.endElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.TD);

    writer.endElement(HtmlElements.TR);
    writer.startElement(HtmlElements.TR, null);
    writer.startElement(HtmlElements.TD, null);
    writer.writeAttribute(HtmlAttributes.COLSPAN, 2);

    //track
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(slider, "slider"));
    writer.writeIdAttribute(getIdForSliderTrack(facesContext, slider));

    // handle
    writer.startElement(HtmlElements.DIV, null);
    writer.writeIdAttribute(getIdForSliderHandle(facesContext, slider));
    Style handleStyle = new Style();
    handleStyle.setPosition(Position.RELATIVE);
    handleStyle.setTop(Measure.valueOf(-6));
    handleStyle.setWidth(Measure.valueOf(12));
    handleStyle.setHeight(Measure.valueOf(6));
    writer.writeStyleAttribute(handleStyle); // todo: why not do that via the class?
    writer.startElement(HtmlElements.IMG, null);
    writer.writeAttribute(HtmlAttributes.SRC, getAbsoluteImagePath(facesContext, "image/sliderTriangle.gif"), true);
    writer.endElement(HtmlElements.IMG);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.TD);
    writer.endElement(HtmlElements.TR);
    writer.endElement(HtmlElements.TABLE);

    writeSliderJavaScript(facesContext, slider, writer);
    //HtmlRendererUtils.renderFocusId(facesContext, slider);
  }

  public void decode(FacesContext context, UIComponent component) {
    UIInput uiInput;
    if (component instanceof UIInput && !ComponentUtils.isOutputOnly(component)) {
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

  private String getAbsoluteImagePath(FacesContext facesContext, String relativeImagePath) {
    return facesContext.getExternalContext().getRequestContextPath()
        + ResourceManagerUtils.getImageWithPath(facesContext, relativeImagePath);
  }

  private String getIdForInputField(FacesContext context, UIComponent component) {
    String id = component.getClientId(context);
    return id + ComponentUtils.SUB_SEPARATOR + "input";
  }

  private String getIdForSliderTrack(FacesContext context, UIComponent component) {
    String id = component.getClientId(context);
    return id + ComponentUtils.SUB_SEPARATOR + "track";
  }

  private String getIdForSliderHandle(FacesContext context, UIComponent component) {
    String id = component.getClientId(context);
    return id + ComponentUtils.SUB_SEPARATOR + "handle";
  }

  private void writeSliderJavaScript(FacesContext context, UIComponent component, TobagoResponseWriter writer)
      throws IOException {
    String trackId = getIdForSliderTrack(context, component);
    String handleId = getIdForSliderHandle(context, component);
    String inputId = getIdForInputField(context, component);
    String jsId = component.getClientId(context).replace(":", "_");
    Integer min = ComponentUtils.getIntAttribute(component, "min");
    Integer max = ComponentUtils.getIntAttribute(component, "max");
    String script = "    var slider_" + jsId + " = new Control.Slider('" + handleId + "', '" + trackId + "', {\n"
        + "        sliderValue:$('" + inputId + "').value,\n"
        + "        range : $R(" + min + ", " + max + "),\n"
        + "        values: $R(" + min + ", " + max + ").toArray(),\n"
        + "        onSlide:function(v) {\n"
        + "            $('" + inputId + "').value = v;\n"
        + "        },\n"
        + "        onChange:function(v) {\n"
        + "            $('" + inputId + "').value = v;\n"
        + "        }\n"
        + "    });\n"
        + "\n"
        + "    Event.observe('value', 'change', function() {\n"
        + "        slider_" + jsId + ".setValue($('" + inputId + "').value);\n"
        + "    });\n";
    writer.writeJavascript(script);
  }

}

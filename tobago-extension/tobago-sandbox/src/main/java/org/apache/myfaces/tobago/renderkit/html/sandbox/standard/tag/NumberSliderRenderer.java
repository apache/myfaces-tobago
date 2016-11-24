/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.renderkit.html.sandbox.standard.tag;

import org.apache.myfaces.tobago.internal.component.AbstractUINumberSlider;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Position;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class NumberSliderRenderer extends RendererBase {

  private static final String SLIDER_WIDTH_PERCENT = "sliderWidthPercent";

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUINumberSlider slider = (AbstractUINumberSlider) component;
    
    final String id = slider.getClientId(facesContext);
    final String currentValue = getCurrentValue(facesContext, slider);
    final boolean readonly = slider.isReadonly();
    final boolean disabled = slider.isDisabled();
    final Integer min = slider.getMin();
    final Integer max = slider.getMax();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

//    final Style style = slider.getStyle();
//    final int width = -1;
//    int sliderWidthPerc
//        = getResourceManager().getThemeMeasure(facesContext, slider, SLIDER_WIDTH_PERCENT).getPixel();
//      if (sliderWidthPerc <= 25) {
//        sliderWidthPerc = 25;
//      }
//      if (sliderWidthPerc >= 75) {
//        sliderWidthPerc = 75;
//      }
//    int sliderWidth = 100; // fixme
//    int inputWidth = 50; // fixme;
//    if (style.getWidth() != null && style.getWidth().getPixel() >= 0) {
//      sliderWidth = (width * sliderWidthPerc) / 100;
//      inputWidth = (width * (100 - sliderWidthPerc)) / 100;
//    }

    writer.startElement(HtmlElements.TABLE);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PRESENTATION.toString(), false);
    writer.writeIdAttribute(id);
    writer.writeClassAttribute(Classes.create(slider), slider.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, slider);
    writer.writeStyleAttribute(slider.getStyle());
    //writer.writeAttribute(HtmlAttributes.border,"1",false);

    writer.startElement(HtmlElements.TR);
    writer.startElement(HtmlElements.TD);
    writer.writeClassAttribute(Classes.create(slider, "min"));

    final Style widthStyle = new Style();
//    widthStyle.setWidth(Measure.valueOf(sliderWidth / 2));
    writer.writeStyleAttribute(widthStyle);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(Classes.create(slider, "min"));
    writer.write(Integer.toString(min));
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.TD);
    writer.startElement(HtmlElements.TD);
    writer.writeClassAttribute(Classes.create(slider, "max"));
    writer.writeStyleAttribute(widthStyle);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(Classes.create(slider, "max"));
    writer.write(Integer.toString(max));
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.TD);

    // the input field starts here
    writer.startElement(HtmlElements.TD);
    writer.writeAttribute(HtmlAttributes.ROWSPAN, "2", false);
    writer.writeClassAttribute(Classes.create(slider, "td"));

    writer.startElement(HtmlElements.INPUT);
    writer.writeClassAttribute(Classes.create(slider, "input"));
//    widthStyle.setWidth(Measure.valueOf(inputWidth));
    writer.writeStyleAttribute(widthStyle);
    final String inputIdAndName = getIdForInputField(facesContext, slider);
    writer.writeNameAttribute(inputIdAndName);
    writer.writeIdAttribute(inputIdAndName);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, false);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.endElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.TD);

    writer.endElement(HtmlElements.TR);
    writer.startElement(HtmlElements.TR);
    writer.startElement(HtmlElements.TD);
    writer.writeAttribute(HtmlAttributes.COLSPAN, 2);

    //track
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(Classes.create(slider, "slider"));
    writer.writeIdAttribute(getIdForSliderTrack(facesContext, slider));

    // handle
    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(getIdForSliderHandle(facesContext, slider));
    final Style handleStyle = new Style();
    handleStyle.setPosition(Position.relative);
    handleStyle.setTop(Measure.valueOf(-6));
    handleStyle.setWidth(Measure.valueOf(12));
    handleStyle.setHeight(Measure.valueOf(6));
    writer.writeStyleAttribute(handleStyle); // todo: why not do that via the class?
    writer.startElement(HtmlElements.IMG);
    writer.writeAttribute(HtmlAttributes.SRC, getAbsoluteImagePath(facesContext, "image/sliderTriangle"), true);
    writer.endElement(HtmlElements.IMG);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.TD);
    writer.endElement(HtmlElements.TR);
    writer.endElement(HtmlElements.TABLE);

//    writeSliderJavaScript(facesContext, slider, writer);
    //HtmlRendererUtils.renderFocusId(facesContext, slider);
  }

  @Override
  public void decode(final FacesContext context, final UIComponent component) {
    final UIInput uiInput;
    if (component instanceof UIInput && !ComponentUtils.isOutputOnly(component)) {
      uiInput = (UIInput) component;
    } else {
      return;
    }
    final String inputId = getIdForInputField(context, component);
    final Map requestParameterMap = context.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(inputId)) {
      final String newValue = (String) requestParameterMap.get(inputId);
      uiInput.setSubmittedValue(newValue);
    }
  }

  private String getAbsoluteImagePath(final FacesContext facesContext, final String relativeImagePath) {
    return facesContext.getExternalContext().getRequestContextPath()/*
        + ResourceManagerUtils.getImage(facesContext, relativeImagePath)*/;
  }

  private String getIdForInputField(final FacesContext context, final UIComponent component) {
    final String id = component.getClientId(context);
    return id + ComponentUtils.SUB_SEPARATOR + "input";
  }

  private String getIdForSliderTrack(final FacesContext context, final UIComponent component) {
    final String id = component.getClientId(context);
    return id + ComponentUtils.SUB_SEPARATOR + "track";
  }

  private String getIdForSliderHandle(final FacesContext context, final UIComponent component) {
    final String id = component.getClientId(context);
    return id + ComponentUtils.SUB_SEPARATOR + "handle";
  }
}

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

import org.apache.myfaces.tobago.TobagoConstants;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class InputNumberSliderRenderer extends LayoutableRendererBase {

  private static final String SLIDER_WIDTH_PERCENT = "sliderWidthPercent";

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    final String[] scripts = new String[]{"script/scriptaculous.js"};
    ComponentUtil.addScripts(component, scripts);

    String id = component.getClientId(facesContext);
    String currentValue = getCurrentValue(facesContext, component);
    boolean readonly = ComponentUtil.getBooleanAttribute(component, ATTR_READONLY);
    boolean disabled = ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED);
    Integer min = ComponentUtil.getIntAttribute(component, "min");
    Integer max = ComponentUtil.getIntAttribute(component, "max");
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);


    HtmlStyleMap style = (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE);
    int width = -1;
    int sliderWidthPerc = 33;
    if (ThemeConfig.hasValue(facesContext, component, SLIDER_WIDTH_PERCENT)) {
      sliderWidthPerc = ThemeConfig.getValue(facesContext, component, SLIDER_WIDTH_PERCENT);
      if (sliderWidthPerc <= 25) {
        sliderWidthPerc = 25;
      }
      if (sliderWidthPerc >= 75) {
        sliderWidthPerc = 75;
      }
    }
    int sliderWidth = 100; // fixme
    int inputWidth = 50; // fixme;
    if (style != null && style.containsKey("width")) {
      width = style.getInt("width");
    }
    if (width >= 0) {
      sliderWidth = (width * sliderWidthPerc) / 100;
      inputWidth = (width * (100 - sliderWidthPerc)) / 100;
    }

    writer.startElement(HtmlConstants.TABLE, component);
    writer.writeIdAttribute(id);
    writer.writeClassAttribute();
    writer.writeStyleAttribute();
    //writer.writeAttribute("border","1",false);

    StyleClasses styleClasses = new StyleClasses();
    styleClasses.addAspectClass("inputNumberSlider", "min", StyleClasses.Aspect.DEFAULT);

    writer.startElement(HtmlConstants.TR, null);
    writer.startElement(HtmlConstants.TD, null);
    writer.writeClassAttribute(styleClasses);

    HtmlStyleMap widthStyle = new HtmlStyleMap();
    widthStyle.put("width", sliderWidth / 2);
    writer.writeStyleAttribute(widthStyle);
    writer.startElement(HtmlConstants.SPAN, null);
    writer.writeClassAttribute(styleClasses);
    writer.write(Integer.toString(min));
    writer.endElement(HtmlConstants.SPAN);

    styleClasses = new StyleClasses();
    styleClasses.addAspectClass("inputNumberSlider", "max", StyleClasses.Aspect.DEFAULT);

    writer.endElement(HtmlConstants.TD);
    writer.startElement(HtmlConstants.TD, null);
    writer.writeClassAttribute(styleClasses);
    writer.writeStyleAttribute(widthStyle);
    writer.startElement(HtmlConstants.SPAN, null);
    writer.writeClassAttribute(styleClasses);
    writer.write(Integer.toString(max));
    writer.endElement(HtmlConstants.SPAN);
    writer.endElement(HtmlConstants.TD);

    // the input field starts here
    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute("rowspan", "2", false);
    writer.writeClassAttribute("tobago-inputNumberSlider-input-default");

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeClassAttribute("tobago-in-default");
    widthStyle.put("width", inputWidth);
    writer.writeStyleAttribute(widthStyle);
    String inputIdAndName = getIdForInputField(facesContext, component);
    writer.writeNameAttribute(inputIdAndName);
    writer.writeIdAttribute(inputIdAndName);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, false);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    //writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
    writer.endElement(HtmlConstants.INPUT);
    writer.endElement(HtmlConstants.TD);

    writer.endElement(HtmlConstants.TR);
    writer.startElement(HtmlConstants.TR, null);
    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute("colspan", 2);

    //track
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeClassAttribute("tobago-inputNumberSlider-slider-default");
    writer.writeIdAttribute(getIdForSliderTrack(facesContext, component));

    // handle
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(getIdForSliderHandle(facesContext, component));
    writer.writeStyleAttribute("position:relative; top:-6px; width:12px; height:6px");
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeAttribute(HtmlAttributes.SRC, getAbsoluteImagePath(facesContext, "image/sliderTriangle.gif"), true);
    writer.endElement(HtmlConstants.IMG);
    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.DIV);
    writer.endElement(HtmlConstants.TD);
    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);

    writeSliderJavaScript(facesContext, component, writer);
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

  private String getAbsoluteImagePath(FacesContext facesContext, String relativeImagePath) {
    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
    return contextPath + resourceManager.getImage(viewRoot, relativeImagePath);
  }

  private String getIdForInputField(FacesContext context,
      UIComponent component) {
    String id = component.getClientId(context);
    return id + TobagoConstants.SUBCOMPONENT_SEP + "input";
  }

  private String getIdForSliderTrack(FacesContext context,
      UIComponent component) {
    String id = component.getClientId(context);
    return id + TobagoConstants.SUBCOMPONENT_SEP + "track";
  }

  private String getIdForSliderHandle(FacesContext context,
      UIComponent component) {
    String id = component.getClientId(context);
    return id + TobagoConstants.SUBCOMPONENT_SEP + "handle";
  }

  private void writeSliderJavaScript(FacesContext context, UIComponent component,
      TobagoResponseWriter writer) throws IOException {
    String trackId = getIdForSliderTrack(context, component);
    String handleId = getIdForSliderHandle(context, component);
    String inputId = getIdForInputField(context, component);
    String jsId = component.getClientId(context).replace(':', '_');
    Integer min = ComponentUtil.getIntAttribute(component, "min");
    Integer max = ComponentUtil.getIntAttribute(component, "max");
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

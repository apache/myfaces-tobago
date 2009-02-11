package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.AbstractUIGridConstraints;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UITextarea;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.LengthValidator;
import java.io.IOException;

public class TextAreaRenderer extends InputRendererBase {

  private static final Log LOG = LogFactory.getLog(TextAreaRenderer.class);

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UITextarea)) {
      LOG.error("Wrong type: Need " + UITextarea.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    UITextarea input = (UITextarea) component;
    String title = HtmlRendererUtil.getTitleFromTipAndMessages(facesContext, component);

    String clientId = input.getClientId(facesContext);
    String onchange = HtmlUtils.generateOnchange(input, facesContext);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.TEXTAREA, input);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.ROWS, null, Attributes.ROWS);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.writeAttribute(HtmlAttributes.READONLY,
        ComponentUtil.getBooleanAttribute(input, Attributes.READONLY));
    writer.writeAttribute(HtmlAttributes.DISABLED,
        ComponentUtil.getBooleanAttribute(input, Attributes.DISABLED));
    Integer tabIndex = input.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    HtmlRendererUtil.renderDojoDndItem(component, writer, true);

// LAYOUT Begin
    HtmlStyleMap style = (HtmlStyleMap) input.getAttributes().get(Attributes.STYLE);
    Measure width = ((AbstractUIGridConstraints) input.getConstraints()).getWidth();
    if (width != null) {
      style.put("width", width.toString());
    }
    Measure height = ((AbstractUIGridConstraints) input.getConstraints()).getHeight();
    if (height !=null) {
      style.put("height", height.toString());
    }
// LAYOUT End

    writer.writeStyleAttribute();
    writer.writeClassAttribute();
    if (onchange != null) {
      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
    }
    String currentValue = RenderUtil.currentValue(input);
    if (currentValue != null) {
      // this is because browsers eat the first CR+LF of <textarea>
      if (currentValue.startsWith("\r\n")) {
        currentValue = "\r\n" + currentValue;
      } else if (currentValue.startsWith("\n")) {
        currentValue = "\n" + currentValue;
      } else if (currentValue.startsWith("\r")) {
        currentValue = "\r" + currentValue;
      }
      writer.writeText(currentValue, null);
    }
    writer.endElement(HtmlConstants.TEXTAREA);

    HtmlRendererUtil.checkForCommandFacet(input, facesContext, writer);
    int maxLength = -1;
    for (Validator validator : input.getValidators()) {
      if (validator instanceof LengthValidator) {
        LengthValidator lengthValidator = (LengthValidator) validator;
        maxLength = lengthValidator.getMaximum();
      }
    }
    boolean required = ComponentUtil.getBooleanAttribute(input, Attributes.REQUIRED);
    if (required || maxLength > -1) {
      String rendererName = HtmlRendererUtil.getRendererName(facesContext, input);
      final String[] cmds = {
          "new Tobago.In(\"" + input.getClientId(facesContext) + "\", true ,\""
                  + StyleClasses.PREFIX + rendererName + "\" " + (maxLength > -1? "," + maxLength: "")  + "  );"
      };
      HtmlRendererUtil.writeScriptLoader(facesContext, null, cmds);
    }

    // focus
    HtmlRendererUtil.renderFocusId(facesContext, component);
  }
}


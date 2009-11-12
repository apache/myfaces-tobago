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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.AbstractUIPage;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UIInputBase;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InRenderer extends InputRendererBase implements AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(InRenderer.class);

  private static final String[] SCRIPTS = new String[] {"script/inputSuggest.js"};

  private static final String[] STYLES = new String[]{"style/dojo.css"};

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      if (component instanceof UIInput && ((UIInput) component).getSuggestMethod() != null) {
        ((TobagoFacesContext) facesContext).getScriptFiles().addAll(Arrays.asList(SCRIPTS));
        ((TobagoFacesContext) facesContext).getStyleFiles().addAll(Arrays.asList(STYLES));
      }
    }
  }
  
  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);
    String clientId = component.getClientId(facesContext);
    AbstractUIPage page = ComponentUtils.findPage(component);
    if (clientId.equals(page.getActionId())) {
      // this is a inputSuggest request -> render response
      facesContext.renderResponse();
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UIInputBase)) {
      LOG.error("Wrong type: Need " + UIInputBase.class.getName() + ", but was " + component.getClass().getName());
      return;
    }
    UIInputBase input = (UIInputBase) component;

    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, input);

    String currentValue = getCurrentValue(facesContext, input);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + currentValue + "'");
    }
    String type = ComponentUtils.getBooleanAttribute(input,
        Attributes.PASSWORD) ? HtmlInputTypes.PASSWORD : HtmlInputTypes.TEXT;

    // Todo: check for valid binding
    boolean renderAjaxSuggest = false;
    if (input instanceof UIInput) {
     renderAjaxSuggest = ((UIInput) input).getSuggestMethod() != null;
    }
    String id = input.getClientId(facesContext);
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.INPUT, input);
    writer.writeAttribute(HtmlAttributes.TYPE, type, false);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    }
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    int maxLength = 0;
    for (Validator validator : input.getValidators()) {
      if (validator instanceof LengthValidator) {
        LengthValidator lengthValidator = (LengthValidator) validator;
        maxLength = lengthValidator.getMaximum();
      }
    }
    if (maxLength > 0) {
      writer.writeAttribute(HtmlAttributes.MAXLENGTH, maxLength);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, ComponentUtils.getBooleanAttribute(input, Attributes.READONLY));
    writer.writeAttribute(HtmlAttributes.DISABLED, ComponentUtils.getBooleanAttribute(input, Attributes.DISABLED));
    Integer tabIndex = input.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    Style style = new Style(facesContext, input);
    writer.writeStyleAttribute(style);

    applyExtraStyle(facesContext, input, currentValue);
    HtmlRendererUtils.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute();
    /*if (component instanceof UIInputBase) {
      String onchange = HtmlUtils.generateOnchange((UIInputBase) component, facesContext);
      if (onchange != null) {
        // TODO: create and use utility method to write attributes without quoting
    //      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
      }
    } */
    writer.endElement(HtmlConstants.INPUT);

    HtmlRendererUtils.checkForCommandFacet(input, facesContext, writer);

    boolean required = ComponentUtils.getBooleanAttribute(input, Attributes.REQUIRED);
    String rendererName = HtmlRendererUtils.getRendererName(facesContext, input);
    if (required && !renderAjaxSuggest) {
      final String[] cmds = {
          "new Tobago.In(\"" + id + "\", true ,\"" + StyleClasses.PREFIX + rendererName + "\"  );"
      };

      HtmlRendererUtils.writeScriptLoader(facesContext, null, cmds);
    }

    // focus
    HtmlRendererUtils.renderFocusId(facesContext, input);

    // input suggest
    if (renderAjaxSuggest) {
   


      final String[] cmds = {
          "new Tobago.AutocompleterAjax(",
          "    '" + id + "',",
          "    '" + required + "',",
          "    '" + StyleClasses.PREFIX + rendererName + "',",
          "    { });"
      };

      HtmlRendererUtils.writeStyleLoader(facesContext, STYLES);
      HtmlRendererUtils.writeScriptLoader(facesContext, SCRIPTS, cmds);
    }

  }

  protected void applyExtraStyle(FacesContext facesContext, UIInputBase input, String currentValue) {
    if (currentValue != null && currentValue.length() > 0
        && ComponentUtils.getBooleanAttribute(input, Attributes.REQUIRED)) {
      StyleClasses styleClasses = StyleClasses.ensureStyleClasses(input);
      String rendererName = HtmlRendererUtils.getRendererName(facesContext, input);
      styleClasses.removeAspectClass(rendererName, StyleClasses.Aspect.REQUIRED);
    }
  }

  public void encodeAjax(FacesContext context, UIComponent component) throws IOException {
    if (!(component instanceof UIInputBase)) {
      LOG.error("Wrong type: Need " + UIInputBase.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    AjaxUtils.checkParamValidity(context, component, UIInput.class);

    UIInputBase input = (UIInputBase) component;

    MethodBinding mb;
    Object o = null;
    if (input instanceof UIInput) {
      o = ((UIInput) input).getSuggestMethod();
    }
    if (o instanceof MethodBinding) {
      mb = (MethodBinding) o;
    } else {
      // should never occur
      return;
    }

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(context);
    int maxSuggestedCount = 25; //input.getMaxSuggestedItems()!=null
//        ? input.getMaxSuggestedItems().intValue()
//        : DEFAULT_MAX_SUGGESTED_ITEMS;

    List suggesteds
        = (List) mb.invoke(context, new Object[]{(String) input.getSubmittedValue()});
    writer.startJavascript();
    writer.write("return  {items: [");

    int suggestedCount = 0;
    for (Iterator i = suggesteds.iterator(); i.hasNext(); suggestedCount++) {
      if (suggestedCount > maxSuggestedCount) {
        break;
      }
      if (suggestedCount > 0) {
        writer.write(", ");
      }
      writer.write("{label: \"");
      writer.write(AjaxUtils.encodeJavascriptString(i.next().toString()));
      writer.write("\"}");
    }
    writer.write("]};");
    writer.endJavascript();
  }
}

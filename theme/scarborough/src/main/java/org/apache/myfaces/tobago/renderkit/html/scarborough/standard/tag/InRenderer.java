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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_PASSWORD;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_REQUIRED;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import static org.apache.myfaces.tobago.ajax.api.AjaxResponse.CODE_ERROR;
import static org.apache.myfaces.tobago.ajax.api.AjaxResponse.CODE_SUCCESS;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class InRenderer extends InputRendererBase implements AjaxRenderer {
  private static final Log LOG = LogFactory.getLog(InRenderer.class);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);
    String clientId = component.getClientId(facesContext);
    UIPage page = ComponentUtil.findPage(component);
    if (clientId.equals(page.getActionId())) {
      // this is a inputSuggest request -> render response
      facesContext.renderResponse();
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UIInput)) {
      LOG.error("Wrong type: Need " + UIInput.class.getName() + ", but was " + component.getClass().getName());
      return;
    }
    UIInput input = (UIInput) component;

    String title = HtmlRendererUtil.getTitleFromTipAndMessages(facesContext, input);

    String currentValue = getCurrentValue(facesContext, input);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + currentValue + "'");
    }
    String type = ComponentUtil.getBooleanAttribute(input,
        ATTR_PASSWORD) ? "password" : "text";

    // Todo: check for valid binding
    boolean renderAjaxSuggest = input.getSuggestMethod() != null;
    String id = input.getClientId(facesContext);
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
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
    writer.writeAttribute(HtmlAttributes.READONLY, ComponentUtil.getBooleanAttribute(input, ATTR_READONLY));
    writer.writeAttribute(HtmlAttributes.DISABLED, ComponentUtil.getBooleanAttribute(input, ATTR_DISABLED));
    Integer tabIndex = input.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    writer.writeStyleAttribute();

    if (currentValue != null && currentValue.length() > 0
        && ComponentUtil.getBooleanAttribute(input, ATTR_REQUIRED)) {
      StyleClasses styleClasses = StyleClasses.ensureStyleClasses(input);
      String rendererName = HtmlRendererUtil.getRendererName(facesContext, input);
      styleClasses.removeAspectClass(rendererName, StyleClasses.Aspect.REQUIRED);
    }
    HtmlRendererUtil.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute();
    /*if (component instanceof UIInput) {
      String onchange = HtmlUtils.generateOnchange((UIInput) component, facesContext);
      if (onchange != null) {
        // TODO: create and use utility method to write attributes without quoting
    //      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
      }
    } */
    writer.endElement(HtmlConstants.INPUT);

    checkForCommandFacet(input, facesContext, writer);

    boolean required = ComponentUtil.getBooleanAttribute(input, ATTR_REQUIRED);
    String rendererName = HtmlRendererUtil.getRendererName(facesContext, input);
    if (required && !renderAjaxSuggest) {
      final String[] cmds = {
          "new Tobago.In(\"" + id + "\", true ,\"" + StyleClasses.PREFIX + rendererName + "\"  );"
      };

      HtmlRendererUtil.writeScriptLoader(facesContext, null, cmds);
    }

    // focus
    HtmlRendererUtil.renderFocusId(facesContext, input);

    // input suggest
    if (renderAjaxSuggest) {
   
      final String[] scripts = new String[]{
          "script/inputSuggest.js"
      };

      final String[] styles = new String[]{
          "style/dojo.css"
      };

      final UIPage page = ComponentUtil.findPage(facesContext, input);
      for (String file : scripts) {
        page.getScriptFiles().add(file);
      }
      for (String file : styles) {
        page.getStyleFiles().add(file);
      }

      final String[] cmds = {
          "new Tobago.AutocompleterAjax(",
          "    '" + id + "',",
          "    '" + required + "',",
          "    '" + StyleClasses.PREFIX + rendererName + "',",
          "    { });"
      };

      HtmlRendererUtil.writeStyleLoader(facesContext, styles);
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, cmds);
    }

  }

  public int encodeAjax(FacesContext context, UIComponent component) throws IOException {
    if (!(component instanceof UIInput)) {
      LOG.error("Wrong type: Need " + UIInput.class.getName() + ", but was " + component.getClass().getName());
      return CODE_ERROR;
    }

    AjaxUtils.checkParamValidity(context, component, UIInput.class);

    UIInput input = (UIInput) component;

    MethodBinding mb;
    Object o = input.getSuggestMethod();
    if (o instanceof MethodBinding) {
      mb = (MethodBinding) o;
    } else {
      // should never occur
      return CODE_ERROR;
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(context);
    int maxSuggestedCount = 25; //input.getMaxSuggestedItems()!=null
//        ? input.getMaxSuggestedItems().intValue()
//        : DEFAULT_MAX_SUGGESTED_ITEMS;

    List suggesteds
        = (List) mb.invoke(context, new Object[]{(String) input.getSubmittedValue()});

    StringBuilder sb = new StringBuilder();
    sb.append("return  {items: [");

    int suggestedCount = 0;
    for (Iterator i = suggesteds.iterator(); i.hasNext(); suggestedCount++) {
      if (suggestedCount > maxSuggestedCount) {
        break;
      }
      if (suggestedCount > 0) {
        sb.append(", ");
      }
      sb.append("{label: \"");
      sb.append(AjaxUtils.encodeJavascriptString(i.next().toString()));
      sb.append("\"}");
    }
    sb.append("]};");
    writer.writeJavascript(sb.toString());
    return CODE_SUCCESS;
  }
}

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
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
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
  public void encodeEnd(FacesContext facesContext, UIComponent component)
        throws IOException {

    String title = HtmlRendererUtil.getTitleFromTipAndMessages(facesContext, component);

    String currentValue = getCurrentValue(facesContext, component);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + currentValue + "'");
    }
    String type = ComponentUtil.getBooleanAttribute(component,
        ATTR_PASSWORD) ? "password" : "text";

    // Todo: check for valid binding
    boolean renderAjaxSuggest = false;
    if (component instanceof org.apache.myfaces.tobago.component.UIInput) {
      renderAjaxSuggest =
          ((org.apache.myfaces.tobago.component.UIInput) component).getSuggestMethod() != null;
    }

    String id = component.getClientId(facesContext);
    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    writer.startElement(HtmlConstants.INPUT, component);
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
    if (component instanceof EditableValueHolder) {
      EditableValueHolder editableValueHolder = (EditableValueHolder) component;
      for (Validator validator : editableValueHolder.getValidators()) {
        if (validator instanceof LengthValidator) {
          LengthValidator lengthValidator = (LengthValidator) validator;
          maxLength = lengthValidator.getMaximum();
        }
      }
    }
    if (maxLength > 0) {
      writer.writeAttribute(HtmlAttributes.MAXLENGTH, maxLength);
    }
    writer.writeAttribute(HtmlAttributes.READONLY,
        ComponentUtil.getBooleanAttribute(component, ATTR_READONLY));
    writer.writeAttribute(HtmlAttributes.DISABLED,
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeStyleAttribute();
    
    if (currentValue != null && currentValue.length() > 0
        && ComponentUtil.getBooleanAttribute(component, ATTR_REQUIRED)) {
      StyleClasses styleClasses = StyleClasses.ensureStyleClasses(component);
      String rendererName = HtmlRendererUtil.getRendererName(facesContext, component);
      styleClasses.removeAspectClass(rendererName, StyleClasses.Aspect.REQUIRED);
    }
    writer.writeClassAttribute();
    if (renderAjaxSuggest) {
      writer.writeAttribute(HtmlAttributes.AUTOCOMPLETE, "off", false);
    }
    if (component instanceof UIInput) {
      String onchange = HtmlUtils.generateOnchange((UIInput) component, facesContext);
      if (onchange != null) {
        // TODO: create and use utility method to write attributes without quoting
  //      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
      }
    }
    writer.endElement(HtmlConstants.INPUT);

    checkForCommandFacet(component, facesContext, writer);

    if (ComponentUtil.getBooleanAttribute(component, ATTR_REQUIRED)) {
      String rendererName = HtmlRendererUtil.getRendererName(facesContext, component);
      final String[] cmds = {
         "new Tobago.In(\"" + id + "\", true ,\"" + StyleClasses.PREFIX + rendererName + "\"  );"
      };

      HtmlRendererUtil.writeScriptLoader(facesContext, null, cmds);
    }

    // focus
    HtmlRendererUtil.renderFocusId(facesContext, component);

    // input suggest
    if (renderAjaxSuggest) {

      String popupId = id + SUBCOMPONENT_SEP + "ajaxPopup";

      final UIPage page = ComponentUtil.findPage(facesContext, component);
      page.getScriptFiles().add("script/effects.js");
      page.getScriptFiles().add("script/dragdrop.js");
      page.getScriptFiles().add("script/controls.js");
      page.getScriptFiles().add("script/inputSuggest.js");

      writer.startElement(HtmlConstants.DIV, null);
      writer.writeClassAttribute("tobago-in-suggest-popup");
      writer.writeStyleAttribute("display: none;");
      writer.writeIdAttribute(popupId);
      writer.endElement(HtmlConstants.DIV);

      final String[] scripts = new String[]{
          "script/effects.js",
          "script/dragdrop.js",
          "script/controls.js",
          "script/inputSuggest.js"
      };

      final String[] cmds = {
          "new Tobago.Autocompleter(",
          "    '" + id + "',",
          "    '" + page.getClientId(facesContext) + "',",
          "    { method:       'post',",
          "      asynchronous: true,",
          "      parameters: ''",
          "    });"
      };

      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, cmds);
    }

  }

  public void encodeAjax(FacesContext context, UIComponent uiComponent) throws IOException {
    AjaxUtils.checkParamValidity(context, uiComponent,
        org.apache.myfaces.tobago.component.UIInput.class);


    org.apache.myfaces.tobago.component.UIInput input =
        (org.apache.myfaces.tobago.component.UIInput) uiComponent;

    MethodBinding mb;
    Object o = input.getSuggestMethod();
    if (o instanceof MethodBinding) {
      mb = (MethodBinding) o;
    } else {
      // should never occur
      return;
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(context);
    int maxSuggestedCount = 25; //input.getMaxSuggestedItems()!=null
//        ? input.getMaxSuggestedItems().intValue()
//        : DEFAULT_MAX_SUGGESTED_ITEMS;

    List suggesteds = (List) mb.invoke(context, new Object[]{
        AjaxPhaseListener.getValueForComponent(context, uiComponent)});

    writer.startElement(HtmlConstants.UL, null);
    int suggestedCount = 0;
    for (Iterator i = suggesteds.iterator(); i.hasNext(); suggestedCount++) {
      if (suggestedCount > maxSuggestedCount) {
        break;
      }
      writer.startElement(HtmlConstants.LI, null);
      writer.writeText(i.next(), null);
      writer.endElement(HtmlConstants.LI);
    }
    writer.endElement(HtmlConstants.UL);
    context.responseComplete();
  }
}


package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.InRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class InRenderer extends InRendererBase implements AjaxRenderer {
  private static final Log LOG = LogFactory.getLog(InRenderer.class);

  public void encodeEndTobago(FacesContext facesContext,
        UIComponent component)
        throws IOException {
    Iterator messages = facesContext.getMessages(
        component.getClientId(facesContext));
    StringBuffer stringBuffer = new StringBuffer();
    while (messages.hasNext()) {
      FacesMessage message = (FacesMessage) messages.next();
      stringBuffer.append(message.getDetail());
    }

    String title = null;
    if (stringBuffer.length() > 0) {
      title = stringBuffer.toString();
    }

    title = HtmlRendererUtil.addTip(
            title, (String) component.getAttributes().get(ATTR_TIP));

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
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();
    writer.startElement("input", component);
    writer.writeAttribute("type", type, null);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
    if (currentValue != null) {
      writer.writeAttribute("value", currentValue, null);
    }
    if (title != null) {
      writer.writeAttribute("title", title, null);
    }
    writer.writeAttribute("readonly",
        ComponentUtil.getBooleanAttribute(component, ATTR_READONLY));
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeComponentClass();
    if (renderAjaxSuggest) {
      writer.writeAttribute("autocomplete", "off", false);
    }
    if (component instanceof UIInput) {
      String onchange = HtmlUtils.generateOnchange((UIInput) component, facesContext);
      if (onchange != null) {
        // TODO: create and use utility method to write attributes without quoting
  //      writer.writeAttribute("onchange", onchange, null);
      }
    }
    writer.endElement("input");

    // input suggest
    if (renderAjaxSuggest) {

      String popupId = id + SUBCOMPONENT_SEP + "ajaxPopup";

      final UIPage page = ComponentUtil.findPage(component);
      page.getScriptFiles().add("script/effects.js");
      page.getScriptFiles().add("script/dragdrop.js");
      page.getScriptFiles().add("script/controls.js");
      page.getScriptFiles().add("script/inputSuggest.js");

      writer.startElement("div");
      writer.writeClassAttribute("tobago-in-suggest-popup");
      writer.writeAttribute("style", "display: none;", null);
      writer.writeIdAttribute(popupId);
      writer.endElement("div");

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

    TobagoResponseWriter writer
        = (TobagoResponseWriter) context.getResponseWriter();
    int maxSuggestedCount = 25; //input.getMaxSuggestedItems()!=null
//        ? input.getMaxSuggestedItems().intValue()
//        : DEFAULT_MAX_SUGGESTED_ITEMS;

    List suggesteds = (List) mb.invoke(context, new Object[]{
        AjaxPhaseListener.getValueForComponent(context, uiComponent)});

    writer.startElement("ul", null);
    int suggestedCount = 0;
    for (Iterator i = suggesteds.iterator(); i.hasNext(); suggestedCount++) {
      if (suggestedCount > maxSuggestedCount) {
        break;
      }
      writer.startElement("li", null);
      writer.writeText(i.next(), null);
      writer.endElement("li");
    }
    writer.endElement("ul");
    context.responseComplete();
  }

}


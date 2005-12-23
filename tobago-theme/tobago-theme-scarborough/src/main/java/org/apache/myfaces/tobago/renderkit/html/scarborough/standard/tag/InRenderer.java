/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.InRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.el.MethodBinding;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class InRenderer extends InRendererBase implements AjaxRenderer {
  private static final Log LOG = LogFactory.getLog(InRenderer.class);

// ----------------------------------------------------------- business methods

  protected void renderMain(FacesContext facesContext, UIInput input,
      TobagoResponseWriter writer) throws IOException {
    Iterator messages = facesContext.getMessages(
        input.getClientId(facesContext));
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
            title, (String) input.getAttributes().get(ATTR_TIP));

    String currentValue = getCurrentValue(facesContext, input);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + currentValue + "'");
    }
    String type = ComponentUtil.getBooleanAttribute(input,
        ATTR_PASSWORD) ? "password" : "text";

    // Todo: check for valid binding
    boolean renderAjaxSuggest
        = input.getAttributes().get("suggestMethod") != null;

    String onchange = HtmlUtils.generateOnchange(input, facesContext);

    String id = input.getClientId(facesContext);

    writer.startElement("input", input);
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
        ComponentUtil.getBooleanAttribute(input, ATTR_READONLY));
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(input, ATTR_DISABLED));
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeComponentClass();
    if (renderAjaxSuggest) {
      writer.writeAttribute("autocomplete", "off", false);
    }
    if (onchange != null) {
      // TODO: create and use utility method to write attributes without quoting
//      writer.writeAttribute("onchange", onchange, null);
    }
    writer.endElement("input");

    if (input.getConverter() != null) {
      Converter converter = input.getConverter();
      if (converter instanceof DateTimeConverter) {
        String pattern
            = ((DateTimeConverter) converter).getPattern();
        if (pattern != null) {
          writer.startElement("input", input);
          writer.writeAttribute("type", "hidden", null);
          writer.writeIdAttribute(id + ":converterPattern");
          writer.writeAttribute("value", pattern, null);
          writer.endElement("input");
        }
      }
    }

    // input suggest
    if (renderAjaxSuggest) {

      String popupId = id + SUBCOMPONENT_SEP + "ajaxPopup";
      String viewId = facesContext.getViewRoot().getViewId();
      ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
      String actionURL = viewHandler.getActionURL(facesContext, viewId);

//      final UIPage page = ComponentUtil.findPage(input);
//      page.getScriptFiles().add("script/effects.js");
//      page.getScriptFiles().add("script/dragdrop.js");
//      page.getScriptFiles().add("script/controls.js");
//      page.getScriptFiles().add("script/inputSuggest.js");

      writer.startElement("div");
//      writer.writeClassAttribute("ajaxPopup");
      writer.writeClassAttribute("tobago-in-suggest-popup");
      writer.writeIdAttribute(popupId);
      writer.endElement("div");

      final String[] scripts = new String[]{
          "script/effects.js",
          "script/dragdrop.js",
          "script/controls.js",
          "script/inputSuggest.js"
      };
      String function = "return entry";
      if (facesContext.getApplication().getStateManager().isSavingStateInClient(facesContext)) {
        function +=
            "+'&jsf_tree_64='+encodeURIComponent($('jsf_tree_64').value)" +
            "+'&jsf_state_64='+encodeURIComponent($('jsf_state_64').value)" +
            "+'&jsf_viewid='+encodeURIComponent($('jsf_viewid').value)";
      }

      final String[] cmds = {
          "new Ajax.MyFacesAutocompleter(",
          "    '" + id + "',",
          "    '" + popupId + "',",
          "    '" + AjaxUtils.createUrl(facesContext, id) + "',",
          "    { method:       'post',",
          "      asynchronous: true,",
          "      parameters: '',",
          "      callback: function(element,entry) {" + function + ";}",
          "    });"
      };

      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, cmds);

//      HtmlRendererUtil.startJavascript(writer);
//
//      writer.writeText("{\n", null);
//      writer.writeText("  var suggestDiv = document.getElementById('" + popupId + "');\n", null);
//      writer.writeText("  var suggestParent = suggestDiv.parentNode;\n", null);
//      writer.writeText("  var bodyNode = document.getElementById('" + page.getClientId(facesContext) + "');\n", null);
//      writer.writeText("  suggestParent.removeChild(suggestDiv);\n", null);
//      writer.writeText("  bodyNode.appendChild(suggestDiv);\n", null);
//      writer.writeText("}\n", null);
//
//      HtmlRendererUtil.endJavascript(writer);

    }

  }

  public void encodeAjax(FacesContext context, UIComponent uiComponent) throws IOException
  {
    AjaxUtils.checkParamValidity(context, uiComponent, UIInput.class);


    UIInput input = (UIInput) uiComponent;

    MethodBinding mb;
    Object o = input.getAttributes().get("suggestMethod");
    if (o instanceof MethodBinding) {
      mb = (MethodBinding) o;
    } else {
      // should never occur
      return;
    }


    int maxSuggestedCount = 25 ;//input.getMaxSuggestedItems()!=null
//        ? input.getMaxSuggestedItems().intValue()
//        : DEFAULT_MAX_SUGGESTED_ITEMS;

    List suggesteds = (List) mb.invoke(context,new Object[]{
        AjaxPhaseListener.getValueForComponent(context, uiComponent)});


    StringBuffer buf = new StringBuffer();
    buf.append("<ul>");

    int suggestedCount=0;
    for (Iterator i = suggesteds.iterator() ; i.hasNext() ; suggestedCount++)
    {
      if( suggestedCount > maxSuggestedCount )
        break;

      buf.append("<li>");
      buf.append(i.next().toString());
      buf.append("</li>");
    }
    buf.append("</ul>");

    context.getResponseWriter().write(buf.toString());
  }

}


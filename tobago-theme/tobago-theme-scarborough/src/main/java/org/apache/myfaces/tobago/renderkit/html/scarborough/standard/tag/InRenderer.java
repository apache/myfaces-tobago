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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.model.AutoSuggestExtensionItem;
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.AutoSuggestItems;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InRenderer extends InputRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(InRenderer.class);

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
    if (!(component instanceof AbstractUIInput)) {
      LOG.error("Wrong type: Need " + AbstractUIInput.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    String clientId = component.getClientId(facesContext);
    AbstractUIPage page = ComponentUtils.findPage(component);
    if (clientId.equals(page.getActionId())) {
      // this is a inputSuggest
      encodeAjax(facesContext, component);
    } else {

      AbstractUIInput input = (AbstractUIInput) component;

      String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, input);

      String currentValue = getCurrentValue(facesContext, input);
      if (LOG.isDebugEnabled()) {
        LOG.debug("currentValue = '" + currentValue + "'");
      }
      String type = ComponentUtils.getBooleanAttribute(input,
          Attributes.PASSWORD) ? HtmlInputTypes.PASSWORD : HtmlInputTypes.TEXT;

      // Todo: check for valid binding
      boolean renderAjaxSuggest = false;
      if (input instanceof UIIn) {
        renderAjaxSuggest = ((UIIn) input).getSuggestMethod() != null;
      }
      String id = input.getClientId(facesContext);
      TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
      writer.startElement(HtmlElements.INPUT, input);
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

      HtmlRendererUtils.renderDojoDndItem(component, writer, true);
      writer.writeClassAttribute(Classes.create(input));
      /*if (component instanceof AbstractUIInput) {
       String onchange = HtmlUtils.generateOnchange((AbstractUIInput) component, facesContext);
       if (onchange != null) {
         // TODO: create and use utility method to write attributes without quoting
     //      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
       }
     } */
      writer.endElement(HtmlElements.INPUT);

      HtmlRendererUtils.checkForCommandFacet(input, facesContext, writer);

      boolean required = ComponentUtils.getBooleanAttribute(input, Attributes.REQUIRED);
      final String requiredClass = Classes.requiredWorkaround(input);
      if (required && !renderAjaxSuggest) {
        final String[] cmds = {
            "new Tobago.In(\"" + id + "\", true ,\"" + requiredClass + "\"  );"
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
            "    " + required + ",",
            "    '" + requiredClass + "',",
            "    { });"
        };

        HtmlRendererUtils.writeScriptLoader(facesContext, null, cmds);
      }
    }
  }

  private void encodeAjax(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UIIn)) {
      LOG.error("Wrong type: Need " + UIIn.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    final UIIn input = (UIIn) component;
    final MethodBinding methodBinding = input.getSuggestMethod();
    final AutoSuggestItems items
            = createAutoSuggestItems(methodBinding.invoke(facesContext, new Object[]{input}));
    final List<AutoSuggestItem> suggestItems = items.getItems();
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startJavascript();

    writer.write("Tobago.ajaxComponents['");
    writer.write(component.getClientId(facesContext));
    writer.write("'].suggestions = {items: [");

    for (int i = 0; i < suggestItems.size() && i < items.getMaxSuggestedCount(); i++) {
      AutoSuggestItem suggestItem = suggestItems.get(i);
      if (i > 0) {
        writer.write(", ");
      }
      writer.write("{label: \"");
      writer.write(AjaxInternalUtils.encodeJavaScriptString(suggestItem.getLabel()));
      writer.write("\", value: \"");
      writer.write(AjaxInternalUtils.encodeJavaScriptString(suggestItem.getValue()));
      writer.write("\"");
      if (suggestItem.getExtensionItems() != null) {
        writer.write(", values: [");
        for (int j = 0; j < suggestItem.getExtensionItems().size(); j++) {
          AutoSuggestExtensionItem item = suggestItem.getExtensionItems().get(j);
          if (j > 0) {
            writer.write(", ");
          }
          writer.write("{id: \"");
          writer.write(item.getId());
          writer.write("\", value: \"");
          writer.write(AjaxInternalUtils.encodeJavaScriptString(item.getValue()));
          writer.write("\"}");
        }
        writer.write("]");
      }
      if (suggestItem.getNextFocusId() != null) {
        writer.write(", nextFocusId: \"");
        writer.write(AjaxInternalUtils.encodeJavaScriptString(suggestItem.getNextFocusId()));
        writer.write("\"");
      }

      writer.write("}");

    }

    writer.write("]");
    if (items.getNextFocusId() != null) {
      writer.write(", nextFocusId: \"");
      writer.write(items.getNextFocusId());
      writer.write("\"");
    }

    if (suggestItems.size() > items.getMaxSuggestedCount()) {
      writer.write(", moreElements: \"");
      writer.write(
          ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "tobago.in.inputSuggest.moreElements"));
      writer.write("\"");
    }

    writer.write("};");
    writer.endJavascript();
  }

  private AutoSuggestItems createAutoSuggestItems(Object object) {
    if (object instanceof AutoSuggestItems) {
      return (AutoSuggestItems) object;
    }
    AutoSuggestItems autoSuggestItems = new AutoSuggestItems();
    if (object instanceof List && !((List) object).isEmpty()) {
      if (((List) object).get(0) instanceof AutoSuggestItem) {
        //noinspection unchecked
        autoSuggestItems.setItems((List<AutoSuggestItem>) object);
      } else if (((List) object).get(0) instanceof String) {
        List<AutoSuggestItem> items = new ArrayList<AutoSuggestItem>(((List) object).size());
        for (int i = 0; i < ((List) object).size(); i++) {
          AutoSuggestItem item = new AutoSuggestItem();
          item.setLabel((String) ((List) object).get(i));
          item.setValue((String) ((List) object).get(i));
          items.add(item);
        }
        autoSuggestItems.setItems(items);
      } else {
        throw new IllegalArgumentException("Cant create AutoSuggestItems from " + object);
      }
    } else {
      autoSuggestItems.setItems(Collections.<AutoSuggestItem>emptyList());
    }
    return autoSuggestItems;
  }
}

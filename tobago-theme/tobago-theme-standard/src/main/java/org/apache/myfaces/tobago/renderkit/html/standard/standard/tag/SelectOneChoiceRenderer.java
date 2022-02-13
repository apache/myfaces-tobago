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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.component.UISelectOneChoice;
import org.apache.myfaces.tobago.internal.component.AbstractUISuggest;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.SelectOneRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.Select2Options;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;

public class SelectOneChoiceRenderer extends SelectOneRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectOneChoiceRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    if (Select2Options.of((UISelectOneChoice) component).hasAnyOption()) {
      SelectManyBoxRenderer.addSelect2LanguageJs(facesContext);
    }
  }

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    if (!(component instanceof UISelectOneChoice)) {
      LOG.error("Wrong type: Need " + UISelectOneChoice.class.getName()
          + ", but was " + component.getClass().getName());
      return;
    }

    final UISelectOneChoice select = (UISelectOneChoice) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    AbstractUISuggest suggest = (AbstractUISuggest) select.getSuggest();

    final String id = select.getClientId(facesContext);
    final Iterable<SelectItem> items = SelectItemUtils.getItemIterator(facesContext, select);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    final boolean disabled = !(suggest != null || select.isAllowCustom() || items.iterator().hasNext())
        || select.isDisabled()
        || select.isReadonly();
    final Style style = new Style(facesContext, select);
    final Select2Options select2Options = Select2Options.of(select);
    final boolean renderAsSelect2 = select2Options.hasAnyOption();
    if (suggest != null) {
      select2Options.setMinimumInputLength(suggest.getMinimumCharacters());
    }

    if (renderAsSelect2) {
      String json = select2Options.toJson();
      LOG.trace("Select2 json = \"{}\"", json);
      ComponentUtils.putDataAttribute(select, "tobago-select2", json);

      writer.startElement(HtmlElements.SPAN, select);
      writer.writeStyleAttribute(style);
      style.setTop(Measure.ZERO);
      style.setLeft(Measure.ZERO);
    }


    writer.startElement(HtmlElements.SELECT, select);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    final Integer tabIndex = select.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(select));
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    final String onchange = HtmlUtils.generateOnchange(select, facesContext);
    if (onchange != null) {
      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, true);
    }
    if (suggest != null) {
      writer.writeAttribute(DataAttributes.SUGGEST_ID, suggest.getClientId(facesContext), false);
    }

    HtmlRendererUtils.renderCommandFacet(select, facesContext, writer);
    HtmlRendererUtils.renderFocus(id, select.isFocus(), ComponentUtils.isError(select), facesContext, writer);
    if (renderAsSelect2 && select.getPlaceholder() != null && select.getPlaceholder().length() > 0) {
     writer.startElement(HtmlElements.OPTION, null);
     writer.endElement(HtmlElements.OPTION);
    }

    HtmlRendererUtils.renderSelectItems(select, items, select.getValue(), (String) select.getSubmittedValue(), writer,
        facesContext);

    writer.endElement(HtmlElements.SELECT);
    if (renderAsSelect2) {
      writer.endElement(HtmlElements.SPAN);
    }

    super.encodeEnd(facesContext, select);
  }
}

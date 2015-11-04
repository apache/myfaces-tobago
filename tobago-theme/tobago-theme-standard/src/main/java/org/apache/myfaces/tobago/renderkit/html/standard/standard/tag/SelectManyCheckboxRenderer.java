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

import org.apache.myfaces.tobago.component.UISelectManyCheckbox;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;

public class SelectManyCheckboxRenderer extends SelectManyRendererBase {

  public void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UISelectManyCheckbox select = (UISelectManyCheckbox) component;
    super.prepareRender(facesContext, select);
    if (select.isInline()) {
      ComponentUtils.addCurrentMarkup(select, Markup.INLINE);
    }
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UISelectManyCheckbox select = (UISelectManyCheckbox) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String id = select.getClientId(facesContext);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    final boolean disabled = select.isDisabled();
    final boolean readonly = select.isReadonly();
    final boolean required = select.isRequired();

    writer.startElement(HtmlElements.OL);
    writer.writeIdAttribute(id);
    writer.writeStyleAttribute(select.getStyle());
    writer.writeClassAttribute(Classes.create(select), select.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    boolean first = true;
    final Object[] values = select.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(select);
    int i = 0;
    for (final SelectItem item : SelectItemUtils.getItemIterator(facesContext, select)) {
      final boolean itemDisabled = item.isDisabled() || disabled;
      final String itemId = id + ComponentUtils.SUB_SEPARATOR + i++;
      writer.startElement(HtmlElements.LI);
      if (itemDisabled) {
        writer.writeClassAttribute(BootstrapClass.CHECKBOX, BootstrapClass.DISABLED);
      } else {
        writer.writeClassAttribute(BootstrapClass.CHECKBOX);
      }
      writer.startElement(HtmlElements.LABEL);
      writer.startElement(HtmlElements.INPUT);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
      final String formattedValue = RenderUtils.getFormattedValue(facesContext, select, item.getValue());
      boolean checked;
      if (submittedValues == null) {
        checked = RenderUtils.contains(values, item.getValue());
      } else {
        checked = RenderUtils.contains(submittedValues, formattedValue);
      }
      writer.writeAttribute(HtmlAttributes.CHECKED, checked);
      writer.writeNameAttribute(id);
      writer.writeIdAttribute(itemId);
      writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
      writer.writeAttribute(HtmlAttributes.DISABLED, itemDisabled);
      writer.writeAttribute(HtmlAttributes.READONLY, readonly);
      writer.writeAttribute(HtmlAttributes.REQUIRED, required);
      if (first) {
        HtmlRendererUtils.renderFocus(id, select.isFocus(), ComponentUtils.isError(select), facesContext, writer);
        first = false;
      }
      final Integer tabIndex = select.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
      HtmlRendererUtils.renderCommandFacet(select, itemId, facesContext, writer);
      writer.endElement(HtmlElements.INPUT);

      final String label = item.getLabel();
      if (label != null) {
        writer.writeText(label);
      }

      writer.endElement(HtmlElements.LABEL);
      writer.endElement(HtmlElements.LI);
    }
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.OL);
  }
}

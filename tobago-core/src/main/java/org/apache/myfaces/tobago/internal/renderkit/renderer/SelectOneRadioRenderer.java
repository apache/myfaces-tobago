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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneRadio;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectReference;
import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.ObjectUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;

public class SelectOneRadioRenderer extends SelectOneRendererBase {

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUISelectOneRadio select = (AbstractUISelectOneRadio) component;
    final AbstractUISelectReference reference = select.getRenderRangeReference();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String id = select.getClientId(facesContext);
    final String referenceId = reference != null ? reference.getClientId(facesContext) : id;
    final Iterable<SelectItem> items = SelectItemUtils.getItemIterator(facesContext, select);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    final boolean disabled = select.isDisabled();
    final boolean readonly = select.isReadonly();
    final boolean required = select.isRequired();
    final boolean inline = select.isInline();
    final Markup markup = select.getMarkup();

    writer.startElement(HtmlElements.DIV);
    if (select.isLabelLayoutSkip()) {
      writer.writeIdAttribute(referenceId);
      writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(markup), false);
    }

    writer.writeClassAttribute(
        TobagoClass.SELECT_ONE_RADIO,
        TobagoClass.SELECT_ONE_RADIO.createMarkup(markup),
        inline ? TobagoClass.SELECT_ONE_RADIO__INLINE : null,
        select.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    boolean first = true;
    final Object value = select.getValue();
    final String submittedValue = (String) select.getSubmittedValue();
    int i = 0;
    final int[] renderRange = getRenderRangeList(select, reference);
    for (final SelectItem item : items) {
      if (renderRange == null || ArrayUtils.contains(renderRange, i)) {
        final boolean itemDisabled = item.isDisabled() || disabled;
        final String itemId = id + ComponentUtils.SUB_SEPARATOR + i;
        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(
            BootstrapClass.CUSTOM_CONTROL,
            BootstrapClass.CUSTOM_RADIO,
            inline ? BootstrapClass.CUSTOM_CONTROL_INLINE : null);

        writer.startElement(HtmlElements.INPUT);
        writer.writeClassAttribute(BootstrapClass.CUSTOM_CONTROL_INPUT);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RADIO);
        final String formattedValue = ComponentUtils.getFormattedValue(facesContext, select, item.getValue());
        final boolean checked;
        if (submittedValue == null) {
          checked = ObjectUtils.equals(item.getValue(), value);
        } else {
          checked = ObjectUtils.equals(formattedValue, submittedValue);
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
        writer.writeAttribute(HtmlAttributes.TABINDEX, select.getTabIndex());
        writer.writeCommandMapAttribute(JsonUtils.encode(RenderUtils.getBehaviorCommands(facesContext, select)));
        writer.endElement(HtmlElements.INPUT);

        writer.startElement(HtmlElements.LABEL);
        writer.writeClassAttribute(
            BootstrapClass.CUSTOM_CONTROL_LABEL,
            getCssItems(facesContext, select));
        writer.writeAttribute(HtmlAttributes.FOR, itemId, false);
        if (item instanceof org.apache.myfaces.tobago.model.SelectItem) {
          final org.apache.myfaces.tobago.model.SelectItem tobagoItem =
              (org.apache.myfaces.tobago.model.SelectItem) item;
          final String image = tobagoItem.getImage();
          if (image != null) {
            writer.startElement(HtmlElements.IMG);
            writer.writeAttribute(HtmlAttributes.SRC, image, true);
            writer.writeAttribute(HtmlAttributes.ALT, "", false);
            writer.endElement(HtmlElements.IMG);
          }
        }

        final String label = item.getLabel();
        if (label != null) {
          writer.writeText(label);
        }

        writer.endElement(HtmlElements.LABEL);
        writer.endElement(HtmlElements.DIV);
      }
      i++;
    }
  }

  private int[] getRenderRangeList(AbstractUISelectOneRadio select, AbstractUISelectReference reference) {
    final int[] indices
        = StringUtils.getIndices(reference != null ? reference.getRenderRange() : select.getRenderRange());
    return indices.length > 0 ? indices : null;
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  protected CssItem[] getCssItems(final FacesContext facesContext, final AbstractUISelectOneRadio select) {
    return null;
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final UIComponent component) {
    return component.getClientId(facesContext);
  }
}

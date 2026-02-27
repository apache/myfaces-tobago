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
import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyCheckbox;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectReference;
import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;

import java.io.IOException;

public class SelectManyCheckboxRenderer<T extends AbstractUISelectManyCheckbox> extends SelectManyRendererBase<T> {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_MANY_CHECKBOX;
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final AbstractUISelectReference reference = component.getRenderRangeReference();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String id = component.getClientId(facesContext);
//    final String referenceId = reference != null ? reference.getClientId(facesContext) : id;
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    final boolean disabled = component.isDisabled();
    final boolean readonly = component.isReadonly();
    final boolean required = component.isRequired();
    final boolean inline = component.isInline();
    final Markup markup = component.getMarkup();
    final boolean isInsideCommand = isInside(facesContext, HtmlElements.COMMAND);

    writer.startElement(getTag(facesContext));
    writer.writeClassAttribute(
        inline ? BootstrapClass.FORM_CHECK_INLINE : null,
        component.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    boolean first = true;
    final Object[] values = component.getSelectedValues();
    final String[] submittedValues = getSubmittedValues(component);
    int i = 0;
    final int[] renderRange = getRenderRangeList(component, reference);
    for (final SelectItem item : SelectItemUtils.getItemIterator(facesContext, component)) {
      if (renderRange == null || ArrayUtils.contains(renderRange, i)) {
        final String formattedValue = getFormattedValue(facesContext, component, item.getValue());
        final boolean checked;
        if (submittedValues == null) {
          checked = ArrayUtils.contains(values, item.getValue());
        } else {
          checked = ArrayUtils.contains(submittedValues, formattedValue);
        }
        if (item.isNoSelectionOption() && required && values != null && values.length > 0 && !checked) {
          // skip the noSelectionOption if there is another value selected and required
          continue;
        }
        final boolean itemDisabled = item.isDisabled() || disabled;
        final String itemId = id + ComponentUtils.SUB_SEPARATOR + i;
        writer.startElement(HtmlElements.DIV);
        writer.writeClassAttribute(
            BootstrapClass.FORM_CHECK,
            inline ? BootstrapClass.FORM_CHECK_INLINE : null,
            isInsideCommand ? BootstrapClass.DROPDOWN_ITEM : null);
        writer.startElement(HtmlElements.INPUT);
        writer.writeClassAttribute(
            BootstrapClass.FORM_CHECK_INPUT,
            BootstrapClass.validationColor(ComponentUtils.getMaximumSeverity(component)));
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.CHECKBOX);
        writer.writeAttribute(HtmlAttributes.CHECKED, checked);
        writer.writeNameAttribute(id);
        writer.writeIdAttribute(itemId);
        writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
        writer.writeAttribute(HtmlAttributes.DISABLED, itemDisabled);
        writer.writeAttribute(HtmlAttributes.READONLY, readonly);
        writer.writeAttribute(HtmlAttributes.REQUIRED, required);
        if (first) {
          renderFocus(id, component.isFocus(), component.isError(), facesContext, writer);
          first = false;
        }
        writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());
        writer.endElement(HtmlElements.INPUT);

        writer.startElement(HtmlElements.LABEL);
        writer.writeClassAttribute(BootstrapClass.FORM_CHECK_LABEL);
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
          if (item.isEscape()) {
            writer.writeText(label);
          } else {
            writer.write(label);
          }
        }
        writer.endElement(HtmlElements.LABEL);
        writer.endElement(HtmlElements.DIV);
      }
      i++;
    }
  }

  private int[] getRenderRangeList(AbstractUISelectManyCheckbox select, AbstractUISelectReference reference) {
    final int[] indices
        = StringUtils.getIndices(reference != null ? reference.getRenderRange() : select.getRenderRange());
    return indices.length > 0 ? indices : null;
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.endElement(getTag(facesContext));

    encodeBehavior(writer, facesContext, component);
  }

  private HtmlElements getTag(final FacesContext facesContext) {
    if (isInside(facesContext, HtmlElements.COMMAND)) {
      return HtmlElements.TOBAGO_SELECT_MANY_CHECKBOX;
    } else {
      return HtmlElements.DIV;
    }
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getClientId(facesContext);
  }
}

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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIFormBase;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneRadio;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectReference;
import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.ObjectUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class SelectOneRadioRenderer<T extends AbstractUISelectOneRadio> extends SelectOneRendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    if (isOutputOnly(component)) {
      return;
    }
    final String group = component.getGroup();
    if (StringUtils.isBlank(group)) {
      super.decodeInternal(facesContext, component);
    } else {
      final String decodingId = getDecodingId(facesContext, component);
      final Object newValue = facesContext.getExternalContext().getRequestParameterMap().get(decodingId);
      LOG.debug("decode: key='{}' value='{}'", decodingId, newValue);
      if (component.getValueExpression(Attributes.value.name()) != null) {
        LOG.debug("setSubmittedValue");
        component.setSubmittedValue(newValue);
      } else {
        LOG.debug("resetValue");
        component.resetValue();
      }

      decodeClientBehaviors(facesContext, component);
    }
  }

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_ONE_RADIO;
  }

  @Override
  protected String getDecodingId(final FacesContext facesContext, final T component) {
    final String group = component.getGroup();
    final String name;
    if (StringUtils.isNotBlank(group)) {
      final AbstractUIFormBase form = ComponentUtils.findForm(component);
      if (form != null) {
        name = form.getClientId(facesContext) + facesContext.getNamingContainerSeparatorChar() + group;
      } else {
        LOG.warn("Can't find form for grouping id.");
        name = group;
      }
    } else {
      name = component.getClientId(facesContext);
    }
    return name;
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final AbstractUISelectReference reference = component.getRenderRangeReference();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String id = component.getClientId(facesContext);
//    final String referenceId = reference != null ? reference.getClientId(facesContext) : id;
    final Iterable<SelectItem> items = SelectItemUtils.getItemIterator(facesContext, component);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    final boolean disabled = component.isDisabled();
    final boolean readonly = component.isReadonly();
    final boolean required = component.isRequired();
    final boolean inline = component.isInline();
    final Markup markup = component.getMarkup();
    final boolean isInsideCommand = isInside(facesContext, HtmlElements.COMMAND);
    final String name = getDecodingId(facesContext, component);

    writer.startElement(getTag(facesContext));
    writer.writeClassAttribute(
        inline ? BootstrapClass.FORM_CHECK_INLINE : null,
        component.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    boolean first = true;
    final Object value = component.getValue();
    final String submittedValue = (String) component.getSubmittedValue();
    int i = 0;
    final int[] renderRange = getRenderRangeList(component, reference);
    for (final SelectItem item : items) {
      if (renderRange == null || ArrayUtils.contains(renderRange, i)) {
        final String formattedValue = getFormattedValue(facesContext, component, item.getValue());
        final boolean checked;
        if (submittedValue == null) {
          checked = ObjectUtils.equals(item.getValue(), value);
        } else {
          checked = ObjectUtils.equals(formattedValue, submittedValue);
        }
        if (item.isNoSelectionOption() && component.isRequired() && value != null && !checked) {
          // skip the noSelectionOption if there is a value available
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
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RADIO);
        writer.writeAttribute(HtmlAttributes.CHECKED, checked);
        writer.writeNameAttribute(name);
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

  private int[] getRenderRangeList(AbstractUISelectOneRadio select, AbstractUISelectReference reference) {
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
      return HtmlElements.TOBAGO_SELECT_ONE_RADIO;
    } else {
      return HtmlElements.DIV;
    }
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return null; // there is not a single input element in the type=radio case.
  }
}

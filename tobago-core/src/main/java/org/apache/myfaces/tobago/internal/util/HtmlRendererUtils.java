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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Locale;
import java.util.Map;

public final class HtmlRendererUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private HtmlRendererUtils() {
    // to prevent instantiation
  }

  public static String getRendererName(final FacesContext facesContext, final UIComponent component) {
    final String rendererType = component.getRendererType();
    return rendererType.substring(0, 1).toLowerCase(Locale.ENGLISH) + rendererType.substring(1);
  }

  public static void writeLabelWithAccessKey(final TobagoResponseWriter writer, final LabelWithAccessKey label)
      throws IOException {
    final int pos = label.getPos();
    final String text = label.getLabel();
    if (text != null) {
      if (pos == -1) {
        writer.writeText(text);
      } else {
        writer.writeText(text.substring(0, pos));
        writer.startElement(HtmlElements.U);
        writer.writeText(Character.toString(text.charAt(pos)));
        writer.endElement(HtmlElements.U);
        writer.writeText(text.substring(pos + 1));
      }
    }
  }

  public static void encodeIconOrImage(final TobagoResponseWriter writer, final String image) throws IOException {
    if (image != null && !image.isEmpty()) {
      if (Icons.matches(image)) {
        writer.startElement(HtmlElements.I);
        writer.writeClassAttribute(Icons.custom(image));
        writer.endElement(HtmlElements.I);
      } else {
        writer.startElement(HtmlElements.IMG);
        writer.writeAttribute(HtmlAttributes.SRC, image, true);
        writer.writeAttribute(HtmlAttributes.ALT, "", false);
        writer.endElement(HtmlElements.IMG);
      }
    }
  }

  public static String getTitleFromTipAndMessages(final FacesContext facesContext, final UIComponent component) {
    final String messages = ComponentUtils.getFacesMessageAsString(facesContext, component);
    return HtmlRendererUtils.addTip(messages, ComponentUtils.getAttribute(component, Attributes.tip));
  }

  public static String addTip(final String title, final Object tip) {
    String result = title;
    if (tip != null) {
      if (result != null && result.length() > 0) {
        result += " :: ";
      } else {
        result = "";
      }
      result += tip;
    }
    return result;
  }

  /**
   * @deprecated since 5.0.0. Please use {@link RendererBase#renderSelectItems}
   */
  @Deprecated
  public static void renderSelectItems(
      final UIInput component, final TobagoClass optionClass,
      final Iterable<SelectItem> items, final Object[] values, final String[] submittedValues,
      final TobagoResponseWriter writer, final FacesContext facesContext) throws IOException {
    renderSelectItems(component, optionClass, items, values, submittedValues, null, writer, facesContext);
  }

  /**
   * @deprecated since 5.0.0. Please use {@link RendererBase#renderSelectItems}
   */
  @Deprecated
  public static void renderSelectItems(
      final UIInput component, final TobagoClass optionClass,
      final Iterable<SelectItem> items, final Object value, final String submittedValue,
      final TobagoResponseWriter writer, final FacesContext facesContext) throws IOException {
    renderSelectItems(component, optionClass, items, value != null ? new Object[]{value} : null,
        submittedValue != null ? new String[]{submittedValue} : null, null, writer, facesContext);
  }

  /**
   * @deprecated since 5.0.0. Please use {@link RendererBase#renderSelectItems}
   */
  @Deprecated
  public static void renderSelectItems(
      final UIInput component, final TobagoClass optionClass,
      final Iterable<SelectItem> items, final Object[] values, final String[] submittedValues,
      final Boolean onlySelected, final TobagoResponseWriter writer, final FacesContext facesContext)
      throws IOException {
    new RendererBase<UIComponent>() {
      public void fake(
          final UIInput component, final TobagoClass optionClass,
          final Iterable<SelectItem> items, final Object[] values, final String[] submittedValues,
          final Boolean onlySelected, final TobagoResponseWriter writer, final FacesContext facesContext)
          throws IOException {
        renderSelectItems(component, optionClass, items, values, submittedValues, onlySelected, writer, facesContext);
      }
    }.fake(component, optionClass, items, values, submittedValues, onlySelected, writer, facesContext);
  }

  public static void writeDataAttributes(
      final FacesContext context, final TobagoResponseWriter writer, final UIComponent component)
      throws IOException {

    final Map<Object, Object> dataAttributes = ComponentUtils.getDataAttributes(component);
    if (dataAttributes == null) {
      return;
    }

    final ELContext elContext = context.getELContext();

    for (final Map.Entry<Object, Object> entry : dataAttributes.entrySet()) {
      final Object mapKey = entry.getKey();
      final String name = mapKey instanceof ValueExpression
          ? ((ValueExpression) mapKey).getValue(elContext).toString() : mapKey.toString();
      final Object mapValue = entry.getValue();
      final String value = mapValue instanceof ValueExpression
          ? ((ValueExpression) mapValue).getValue(elContext).toString() : mapValue.toString();
      writer.writeAttribute(DataAttributes.dynamic(name), value, true);
    }
  }
}

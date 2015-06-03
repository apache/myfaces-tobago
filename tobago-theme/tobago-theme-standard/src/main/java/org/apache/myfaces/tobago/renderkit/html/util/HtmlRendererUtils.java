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

package org.apache.myfaces.tobago.renderkit.html.util;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIColumnEvent;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterWrapper;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.FacetUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public final class HtmlRendererUtils {

  private static final Logger LOG = LoggerFactory.getLogger(HtmlRendererUtils.class);
  private static final String ERROR_FOCUS_KEY = HtmlRendererUtils.class.getName() + ".ErrorFocusId";
  private static final String FOCUS_KEY = HtmlRendererUtils.class.getName() + ".FocusId";

  private HtmlRendererUtils() {
    // to prevent instantiation
  }

  public static void renderFocus(
      final String clientId, final boolean focus, final boolean error, final FacesContext facesContext,
      final TobagoResponseWriter writer) throws IOException {
    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
    if (!requestMap.containsKey(FOCUS_KEY)
        && (clientId.equals(FacesContextUtils.getFocusId(facesContext)) || focus || error)) {
      requestMap.put(FOCUS_KEY, Boolean.TRUE);
      writer.writeAttribute(HtmlAttributes.AUTOFOCUS, true);
    }
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
        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute("tobago-x-accessKey");
        writer.writeText(Character.toString(text.charAt(pos)));
        writer.endElement(HtmlElements.SPAN);
        writer.writeText(text.substring(pos + 1));
      }
    }
  }

  public static TobagoResponseWriter getTobagoResponseWriter(final FacesContext facesContext) {

    final ResponseWriter writer = facesContext.getResponseWriter();
    if (writer instanceof TobagoResponseWriter) {
      return (TobagoResponseWriter) writer;
    } else {
      return new TobagoResponseWriterWrapper(writer);
    }
  }

  public static String getTitleFromTipAndMessages(final FacesContext facesContext, final UIComponent component) {
    final String messages = ComponentUtils.getFacesMessageAsString(facesContext, component);
    return HtmlRendererUtils.addTip(messages, component.getAttributes().get(Attributes.TIP));
  }

  public static String addTip(String title, final Object tip) {
    if (tip != null) {
      if (title != null && title.length() > 0) {
        title += " :: ";
      } else {
        title = "";
      }
      title += tip;
    }
    return title;
  }

  public static void renderSelectItems(
      final UIInput component, final Iterable<SelectItem> items, final Object[] values,
      final TobagoResponseWriter writer, final FacesContext facesContext) throws IOException {
    renderSelectItems(component, items, values, null, writer, facesContext);
  }

  public static void renderSelectItems(
      final UIInput component, final Iterable<SelectItem> items, final Object[] values, final Boolean onlySelected,
      final TobagoResponseWriter writer, final FacesContext facesContext) throws IOException {

    if (LOG.isDebugEnabled()) {
      LOG.debug("value = '" + Arrays.toString(values) + "'");
    }
    for (final SelectItem item : items) {
      if (item instanceof SelectItemGroup) {
        writer.startElement(HtmlElements.OPTGROUP, null);
        writer.writeAttribute(HtmlAttributes.LABEL, item.getLabel(), true);
        if (item.isDisabled()) {
          writer.writeAttribute(HtmlAttributes.DISABLED, true);
        }
        final SelectItem[] selectItems = ((SelectItemGroup) item).getSelectItems();
        renderSelectItems(component, Arrays.asList(selectItems), values, onlySelected, writer, facesContext);
        writer.endElement(HtmlElements.OPTGROUP);
      } else {

        Object itemValue = item.getValue();
        // when using selectItem tag with a literal value: use the converted value
        if (itemValue instanceof String && values != null && values.length > 0 && !(values[0] instanceof String)) {
          itemValue = ComponentUtils.getConvertedValue(facesContext, component, (String) itemValue);
        }
        final boolean contains = RenderUtils.contains(values, itemValue);
        if (onlySelected != null) {
          if (onlySelected) {
            if (!contains) {
              continue;
            }
          } else {
            if (contains) {
              continue;
            }
          }
        }
        writer.startElement(HtmlElements.OPTION, null);
        final String formattedValue = RenderUtils.getFormattedValue(facesContext, component, itemValue);
        writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
        if (item instanceof org.apache.myfaces.tobago.model.SelectItem) {
          final String image = ((org.apache.myfaces.tobago.model.SelectItem) item).getImage();
          if (image != null) {
            final Style style = new Style();
            style.setBackgroundImage("url('"
                + ResourceManagerUtils.getImageOrDisabledImageWithPath(facesContext, image, item.isDisabled())
                + "')");
            writer.writeStyleAttribute(style);
          }
        }
        Markup markup = item instanceof SupportsMarkup ? ((SupportsMarkup) item).getMarkup() : Markup.NULL;
        if (onlySelected == null && contains) {
          writer.writeAttribute(HtmlAttributes.SELECTED, true);
          markup = Markup.SELECTED.add(markup);
        }
        if (item.isDisabled()) {
          writer.writeAttribute(HtmlAttributes.DISABLED, true);
          markup = Markup.DISABLED.add(markup);
        }
        writer.writeClassAttribute(Classes.create(component, "option", markup));

        writer.writeText(item.getLabel());
        writer.endElement(HtmlElements.OPTION);
      }
    }
  }

  public static void renderCommandFacet(
      final UIComponent component, final FacesContext facesContext, final TobagoResponseWriter writer)
      throws IOException {
    renderCommandFacet(component, component.getClientId(facesContext), facesContext, writer);
  }

  public static void renderCommandFacet(
      final UIComponent component, final String id, final FacesContext facesContext, final TobagoResponseWriter writer)
      throws IOException {
    if (ComponentUtils.getBooleanAttribute(component, Attributes.READONLY)
        || ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      return;
    }
    CommandMap commandMap = null;
    final Map<String, UIComponent> facets = component.getFacets();
    for (final Map.Entry<String, UIComponent> entry : facets.entrySet()) {
      final UIComponent facetComponent = entry.getValue();
      if (facetComponent.isRendered()
          && (facetComponent instanceof AbstractUICommand || facetComponent instanceof UIForm)) {
        if (commandMap == null) {
          commandMap = new CommandMap();
        }
        final String key = entry.getKey();
        commandMap.addCommand(key, new Command(facesContext, entry.getValue(), id));
      }
    }
    if (commandMap != null) {
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(commandMap), true);
    }
  }

  public static boolean renderSheetCommands(
      final UISheet sheet, final FacesContext facesContext, final TobagoResponseWriter writer) throws IOException {
    CommandMap commandMap = null;
    for (final UIComponent child : sheet.getChildren()) {
      if (child instanceof UIColumnEvent) {
        final UIColumnEvent columnEvent = (UIColumnEvent) child;
        if (columnEvent.isRendered()) {
          final UIComponent selectionChild = child.getChildren().get(0);
          if (selectionChild != null && selectionChild instanceof AbstractUICommand && selectionChild.isRendered()) {
            final UICommand action = (UICommand) selectionChild;
            if (commandMap == null) {
              commandMap = new CommandMap();
            }
            commandMap.addCommand(columnEvent.getEvent(), new Command(facesContext, action, null));
          }
        }
      }
    }
    if (commandMap != null) {
      writer.writeAttribute(DataAttributes.ROW_ACTION, JsonUtils.encode(commandMap), true);
      return true;
    }
    return false;
  }

  public static void encodeContextMenu(
      final FacesContext facesContext, final TobagoResponseWriter writer, final UIComponent parent)
      throws IOException {
    final UIComponent contextMenu = FacetUtils.getContextMenu(parent);
    if (contextMenu != null) {
      writer.startElement(HtmlElements.OL, contextMenu);
      writer.writeClassAttribute("tobago-menuBar tobago-menu-contextMenu");
      RenderUtils.encode(facesContext, contextMenu);
      writer.endElement(HtmlElements.OL);
    }
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
      writer.writeAttribute("data-" + name, value, true);
    }
  }
}

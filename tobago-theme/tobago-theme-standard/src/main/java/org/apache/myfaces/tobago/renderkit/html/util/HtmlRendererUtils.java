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
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.component.UIColumnEvent;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
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
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class HtmlRendererUtils {

  private static final Logger LOG = LoggerFactory.getLogger(HtmlRendererUtils.class);
  private static final String ERROR_FOCUS_KEY = HtmlRendererUtils.class.getName() + ".ErrorFocusId";
  private static final String FOCUS_KEY = HtmlRendererUtils.class.getName() + ".FocusId";

  private HtmlRendererUtils() {
    // to prevent instantiation
  }

  private static boolean renderErrorFocusId(final FacesContext facesContext, final UIInput input) throws IOException {
    if (ComponentUtils.isError(input)) {
      if (!FacesContext.getCurrentInstance().getExternalContext().getRequestMap().containsKey(ERROR_FOCUS_KEY)) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(ERROR_FOCUS_KEY, Boolean.TRUE);
        final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
        final String id = input.getClientId(facesContext);
        writer.writeJavascript("Tobago.errorFocusId = '" + id + "';");
        return true;
      } else {
        return true;
      }
    }
    return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().containsKey(ERROR_FOCUS_KEY);
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

  /** @deprecated since 2.0.0, because of CSP */
  public static void renderFocusId(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    if (component instanceof UIInput) {
      renderFocusId(facesContext, (UIInput) component);
    }
  }

  /** @deprecated since 2.0.0, because of CSP */
  public static void renderFocusId(final FacesContext facesContext, final UIInput component)
      throws IOException {
    if (renderErrorFocusId(facesContext, component)) {
      return;
    }
    if (ComponentUtils.getBooleanAttribute(component, Attributes.FOCUS)) {
      final UIPage page = (UIPage) ComponentUtils.findPage(facesContext, component);
      final String id = component.getClientId(facesContext);
      if (!StringUtils.isBlank(page.getFocusId()) && !page.getFocusId().equals(id)) {
        LOG.warn("page focusId='" + page.getFocusId() + "' ignoring new value '" + id + "'");
      } else {
        final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
        writer.writeJavascript("Tobago.focusId='" + id + "';");
      }
    }
  }

  /**
   * @deprecated Since Tobago 2.0.0
   */
  @Deprecated
  public static void createCssClass(final FacesContext facesContext, final UIComponent component) {
    final String rendererName = getRendererName(facesContext, component);
    Deprecation.LOG.error("Can't render style class for renderer " + rendererName);
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

  /** @deprecated since 1.5.7 and 2.0.0 */
  @Deprecated
  public static void setDefaultTransition(final FacesContext facesContext, final boolean transition)
      throws IOException {
    writeScriptLoader(facesContext, null, new String[]{"Tobago.transition = " + transition + ";"});
  }

  /** @deprecated since 2.0.0 */
  @Deprecated
  public static void addClickAcceleratorKey(
      final FacesContext facesContext, final String clientId, final char key)
      throws IOException {
    //addClickAcceleratorKey(facesContext, clientId, key, null);
  }

  /** @deprecated since 2.0.0 */
  @Deprecated
  public static void addClickAcceleratorKey(
      final FacesContext facesContext, final String clientId, final char key, final String modifier)
      throws IOException {
    //String str
    //    = createOnclickAcceleratorKeyJsStatement(clientId, key, modifier);
    //writeScriptLoader(facesContext, null, new String[]{str});
  }

  /** @deprecated since 2.0.0 */
  @Deprecated
  public static void addAcceleratorKey(
      final FacesContext facesContext, final String func, final char key) throws IOException {
    //addAcceleratorKey(facesContext, func, key, null);
  }

  /** @deprecated since 2.0.0 */
  @Deprecated
  public static void addAcceleratorKey(
      final FacesContext facesContext, final String func, final char key, final String modifier)
      throws IOException {
    final String str = createAcceleratorKeyJsStatement(func, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{str});
  }

  /** @deprecated since 2.0.0 */
  @Deprecated
  public static String createOnclickAcceleratorKeyJsStatement(
      final String clientId, final char key, final String modifier) {
    final String func = "Tobago.clickOnElement('" + clientId + "');";
    return createAcceleratorKeyJsStatement(func, key, modifier);
  }

  /** @deprecated since 2.0.0 */
  @Deprecated
  public static String createAcceleratorKeyJsStatement(
      final String func, final char key, final String modifier) {
    final StringBuilder buffer = new StringBuilder("new Tobago.AcceleratorKey(function() {");
    buffer.append(func);
    if (!func.endsWith(";")) {
      buffer.append(';');
    }
    buffer.append("}, \"");
    buffer.append(key);
    if (modifier != null) {
      buffer.append("\", \"");
      buffer.append(modifier);
    }
    buffer.append("\");");
    return buffer.toString();
  }

  /**
   * @deprecated since 2.0.0. Please use setter.
   */
  @Deprecated
  public static void removeStyleAttribute(final UIComponent component, final String name) {
    Deprecation.LOG.error("HtmlRendererUtils.removeStyleAttribute() no longer supported. Use setter.");
  }

  /** @deprecated since 2.0.0 */
  @Deprecated
  public static void createHeaderAndBodyStyles(final FacesContext facesContext, final UIComponent component) {
    Deprecation.LOG.error("HtmlRendererUtils.createHeaderAndBodyStyles() no longer supported");
  }

  /** @deprecated since 2.0.0 */
  @Deprecated
  public static void createHeaderAndBodyStyles(
      final FacesContext facesContext, final UIComponent component, final boolean width) {
    Deprecation.LOG.error("HtmlRendererUtils.createHeaderAndBodyStyles() no longer supported");
  }

  public static String createSrc(final String src, final String ext) {
    final int dot = src.lastIndexOf('.');
    if (dot == -1) {
      LOG.warn("Image src without extension: '" + src + "'");
      return src;
    } else {
      return src.substring(0, dot) + ext + src.substring(dot);
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

  /**
   * @deprecated Since Tobago 2.0.0. Because of CSP.
   */
  @Deprecated
  public static void writeScriptLoader(final FacesContext facesContext, final String script)
      throws IOException {
    writeScriptLoader(facesContext, new String[]{script}, null);
  }

  /**
   * @deprecated Since Tobago 2.0.0. Because of CSP.
   */
  @Deprecated
  public static void writeScriptLoader(
      final FacesContext facesContext, final String[] scripts, final String[] afterLoadCmds)
      throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    if (scripts != null) {
      LOG.error("Scripts argument for writeScriptLoader not supported anymore!");
    }
    String allScripts = "[]";
    if (scripts != null) {
      allScripts = ResourceManagerUtils.getScriptsAsJSArray(facesContext, scripts);
    }
    final boolean ajax = FacesContextUtils.isAjax(facesContext);
    writer.startJavascript();
    // XXX fix me if scripts != null
    if (ajax || scripts != null) {
      writer.write("new Tobago.ScriptLoader(");
      if (!ajax) {
        writer.write("\n    ");
      }
      writer.write(allScripts);

      if (afterLoadCmds != null && afterLoadCmds.length > 0) {
        writer.write(", ");
        if (!ajax) {
          writer.write("\n");
        }
        boolean first = true;
        for (final String afterLoadCmd : afterLoadCmds) {
          final String[] splittedStrings = StringUtils.split(afterLoadCmd, '\n'); // split on <CR> to have nicer JS
          for (final String splitted : splittedStrings) {
            writer.write(first ? "          " : "        + ");
            writer.write("\"");
            String cmd = StringUtils.replace(splitted, "\\", "\\\\");
            cmd = StringUtils.replace(cmd, "\"", "\\\"");
            writer.write(cmd);
            writer.write("\"");
            if (!ajax) {
              writer.write("\n");
            }
            first = false;
          }
        }
      }
      writer.write(");");
    } else {
    for (final String afterLoadCmd : afterLoadCmds) {
      writer.write(afterLoadCmd);
    }
    }
    writer.endJavascript();
  }

  /**
   * @deprecated Since Tobago 2.0.0. Because of CSP.
   */
  @Deprecated
  public static void writeStyleLoader(
      final FacesContext facesContext, final String[] styles) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startJavascript();
    writer.write("Tobago.ensureStyleFiles(");
    writer.write(ResourceManagerUtils.getStylesAsJSArray(facesContext, styles));
    writer.write(");");
    writer.endJavascript();
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

  public static void renderSelectItems(final UIInput component, final Iterable<SelectItem> items, final Object[] values,
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

  public static String getComponentIds(
      final FacesContext context, final UIComponent component, final String[] componentId) {
    final StringBuilder sb = new StringBuilder();
    for (final String id : componentId) {
      if (!StringUtils.isBlank(id)) {
        if (sb.length() > 0) {
          sb.append(",");
        }
        final String clientId = getComponentId(context, component, id);
        if (clientId != null) {
          sb.append(clientId);
        }
      }
    }
    return sb.toString();
  }

  public static String[] getComponentIdsAsList(
      final FacesContext context, final UIComponent component, final String[] componentId) {
    final List<String> result = new ArrayList<String>(componentId.length);
    for (final String id : componentId) {
      if (!StringUtils.isBlank(id)) {
        final String clientId = getComponentId(context, component, id);
        if (clientId != null) {
          result.add(clientId);
        }
      }
    }
    return (String[]) result.toArray(new String[result.size()]);
  }

  public static String getComponentId(
      final FacesContext context, final UIComponent component, final String componentId) {
    final UIComponent partiallyComponent = ComponentUtils.findComponent(component, componentId);
    if (partiallyComponent != null) {
      final String clientId = partiallyComponent.getClientId(context);
      if (partiallyComponent instanceof UISheet) {
        final int rowIndex = ((UISheet) partiallyComponent).getRowIndex();
        if (rowIndex >= 0 && clientId.endsWith(Integer.toString(rowIndex))) {
          return clientId.substring(0, clientId.lastIndexOf(UINamingContainer.getSeparatorChar(context)));
        }
      }
      return clientId;
    }
    LOG.error("No component found for id='" + componentId + "', " +
        "search base component is '" + component.getClientId(context) + "'");
    return null;
  }

  public static String getJavascriptString(final String str) {
    if (str != null) {
      return "\"" + str + "\"";
    }
    return null;
  }

  public static String getRenderedPartiallyJavascriptArray(final FacesContext facesContext, final UICommand command) {
    if (command == null) {
      return null;
    }
    return getRenderedPartiallyJavascriptArray(facesContext, command, command);
  }

  public static String getRenderedPartiallyJavascriptArray(
      final FacesContext facesContext, final UIComponent searchBase,
      final SupportsRenderedPartially supportsRenderedPartially) {
    final String[] list = supportsRenderedPartially.getRenderedPartially();
    if (list == null || list.length == 0) {
      return null;
    }
    final StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("[");
    for (int i = 0; i < list.length; i++) {
      if (i != 0) {
        strBuilder.append(",");
      }
      strBuilder.append("\"");
      strBuilder.append(HtmlRendererUtils.getComponentId(facesContext, searchBase, list[i]));
      strBuilder.append("\"");
    }
    strBuilder.append("]");
    return strBuilder.toString();
  }

  public static String getJavascriptArray(final String[] list) {
    final StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("[");
    for (int i = 0; i < list.length; i++) {
      if (i != 0) {
        strBuilder.append(",");
      }
      strBuilder.append("\"");
      strBuilder.append(list[i]);
      strBuilder.append("\"");
    }
    strBuilder.append("]");
    return strBuilder.toString();
  }

  /**
   * will be removed in later versions
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void renderDojoDndSource(final FacesContext context, final UIComponent component)
      throws IOException {
    final Object objDojoType = component.getAttributes().get("dojoType");
    if (null != objDojoType && (objDojoType.equals("dojo.dnd.Source") || objDojoType.equals("dojo.dnd.Target"))) {
      FacesContextUtils.addOnloadScript(context, createDojoDndType(component,
          component.getClientId(context), String.valueOf(objDojoType)));
    }
  }

  /**
   * will be removed in later versions
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void renderDojoDndItem(
      final UIComponent component, final TobagoResponseWriter writer, final boolean addStyle)
      throws IOException {
    final Object objDndType = component.getAttributes().get("dndType");
    if (objDndType != null) {
      writer.writeAttribute("dndType", String.valueOf(objDndType), false);
    }
    final Object objDndData = component.getAttributes().get("dndData");
    if (objDndData != null) {
      writer.writeAttribute("dndData", String.valueOf(objDndData), false);
    }
  }

  /**
   * will be removed in later versions
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static String createDojoDndType(final UIComponent component, final String clientId, final String dojoType) {
    final StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("new ").append(dojoType).append("('").append(clientId).append("'");
    final StringBuilder parameter = new StringBuilder();

    final Object objHorizontal = component.getAttributes().get("horizontal");
    if (objHorizontal != null) {
      parameter.append("horizontal: ").append(String.valueOf(objHorizontal)).append(",");
    }
    final Object objCopyOnly = component.getAttributes().get("copyOnly");
    if (objCopyOnly != null) {
      parameter.append("copyOnly: ").append(String.valueOf(objCopyOnly)).append(",");
    }
    final Object objSkipForm = component.getAttributes().get("skipForm");
    if (objSkipForm != null) {
      parameter.append("skipForm: ").append(String.valueOf(objSkipForm)).append(",");
    }
    final Object objWithHandles = component.getAttributes().get("withHandles");
    if (objWithHandles != null) {
      parameter.append("withHandles: ").append(String.valueOf(objWithHandles)).append(",");
    }
    final Object objAccept = component.getAttributes().get("accept");
    if (objAccept != null) {
      String accept = null;
      if (objAccept instanceof String[]) {
        final String[] allowed = (String[]) objAccept;
        if (allowed.length > 1) {
          // TODO replace this
          accept = "'" + allowed[0] + "'";
          for (int i = 1; i < allowed.length; i++) {
            accept += ",'" + allowed[i] + "'";
          }
        }
      } else {
        accept = (String) objAccept;
      }
      parameter.append("accept: [").append(accept).append("],");
    }
    final Object objSingular = component.getAttributes().get("singular");
    if (objSingular != null) {
      parameter.append("singular: ").append(String.valueOf(objSingular)).append(",");
    }
    final Object objCreator = component.getAttributes().get("creator");
    if (objCreator != null) {
      parameter.append("creator: ").append(String.valueOf(objCreator)).append(",");
    }
    if (parameter.length() > 0) {
      parameter.deleteCharAt(parameter.lastIndexOf(","));
      strBuilder.append(",{").append(parameter).append("}");
    }
    strBuilder.append(");");
    return strBuilder.toString();
  }

  public static void renderCommandFacet(final UIComponent component, final FacesContext facesContext,
      final TobagoResponseWriter writer) throws IOException {
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

  public static boolean renderSheetCommands(final UISheet sheet, final FacesContext facesContext,
                                         final TobagoResponseWriter writer) throws IOException {
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


  public static void checkForCommandFacet(
      final UIComponent component, final FacesContext facesContext, final TobagoResponseWriter writer)
      throws IOException {
    checkForCommandFacet(component, Arrays.asList(component.getClientId(facesContext)), facesContext, writer);
  }

  public static void checkForCommandFacet(
      final UIComponent component, final List<String> clientIds, final FacesContext facesContext,
      final TobagoResponseWriter writer) throws IOException {
    if (ComponentUtils.getBooleanAttribute(component, Attributes.READONLY)
        || ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      return;
    }
    final Map<String, UIComponent> facets = component.getFacets();
    for (final Map.Entry<String, UIComponent> entry : facets.entrySet()) {
      if (entry.getValue() instanceof UICommand) {
        addCommandFacet(clientIds, entry, facesContext, writer);
      }
    }
  }

  private static void addCommandFacet(
      final List<String> clientIds, final Map.Entry<String, UIComponent> facetEntry,
      final FacesContext facesContext, final TobagoResponseWriter writer)
      throws IOException {
    for (final String clientId : clientIds) {
      writeScriptForClientId(clientId, facetEntry, facesContext, writer);
    }
  }

  /**
   * @deprecated Since Tobago 2.0.0. Because of CSP.
   */
  @Deprecated
  private static void writeScriptForClientId(
      final String clientId, final Map.Entry<String, UIComponent> facetEntry,
      final FacesContext facesContext, final TobagoResponseWriter writer) throws IOException {
    if (facetEntry.getValue() instanceof UICommand
        && ((UICommand) facetEntry.getValue()).getRenderedPartially().length > 0) {
      writer.startJavascript();
      writer.write("var element = Tobago.element(\"");
      writer.write(clientId);
      writer.write("\");\n");
      writer.write("if (element) {\n");
      writer.write("   Tobago.addEventListener(element, \"");
      writer.write(facetEntry.getKey());
      writer.write("\", function(){Tobago.reloadComponent(this, '");
      writer.write(HtmlRendererUtils.getComponentIds(facesContext, facetEntry.getValue(),
              ((UICommand) facetEntry.getValue()).getRenderedPartially()));
      writer.write("','");
      writer.write(facetEntry.getValue().getClientId(facesContext)); 
      writer.write("', {})});\n");
      writer.write("};");
      writer.endJavascript();
    } else {
      final UIComponent facetComponent = facetEntry.getValue();

      writer.startJavascript();
      writer.write("var element = Tobago.element(\"");
      writer.write(clientId + "\");\n");
      writer.write("if (element) {\n");
      writer.write("   Tobago.addEventListener(element, \"");
      writer.write(facetEntry.getKey());
      writer.write("\", function(){");
      String facetAction = (String) facetComponent.getAttributes().get(Attributes.ONCLICK);
      if (facetAction != null) {
         // Replace @autoId
        facetAction = StringUtils.replace(facetAction, "@autoId", facetComponent.getClientId(facesContext));
        writer.write(facetAction);
      } else {
        writer.write(createSubmitAction(
            facetComponent.getClientId(facesContext),
            ComponentUtils.getBooleanAttribute(facetComponent, Attributes.TRANSITION),
            null,
            clientId));
      }
      writer.write("});\n};");
      writer.endJavascript();
    }
  }

  /**
   * @deprecated since 2.0.0. JavaScript should not be rendered in the page. See CSP.
   */
  @Deprecated
  public static String createSubmitAction(
      final String clientId, final boolean transition, final String target, final String focus) {
    final StringBuilder builder = new StringBuilder();
    builder.append("Tobago.submitAction(this,'");
    builder.append(clientId);
    builder.append("',{");
    if (!transition) { // transition == true is the default
      builder.append("transition:false");
      if (target != null || focus != null) {
        builder.append(',');
      }
    }
    if (target != null) {
      builder.append("target:'");
      builder.append(target);
      builder.append('\'');
      if (focus != null) {
        builder.append(',');
      }
    }
    if (focus != null) {
      builder.append("focus:'");
      builder.append(focus);
      builder.append('\'');
    }
    builder.append("});");
    return builder.toString();
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

  public static void addAcceleratorKey(
      final FacesContext facesContext, final UIComponent component, final Character accessKey) {
    final String clientId = component.getClientId(facesContext);
    final String jsStatement = createOnclickAcceleratorKeyJsStatement(clientId, accessKey, null);
    FacesContextUtils.addMenuAcceleratorScript(facesContext, jsStatement);
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

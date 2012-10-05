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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
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
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterWrapper;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.FacetUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
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
        TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
        String id = input.getClientId(facesContext);        
        writer.writeJavascript("Tobago.errorFocusId = '" + id + "';");
        return true;
      } else {
        return true;
      }
    }
    return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().containsKey(ERROR_FOCUS_KEY);
  }

  public static void renderFocus(String clientId, boolean focus, boolean error, FacesContext facesContext,
      TobagoResponseWriter writer) throws IOException {
    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
    if (!requestMap.containsKey(FOCUS_KEY)
        && (clientId.equals(FacesContextUtils.getFocusId(facesContext)) || focus || error)) {
      requestMap.put(FOCUS_KEY, Boolean.TRUE);
      writer.writeAttribute(HtmlAttributes.AUTOFOCUS, true);
    }
  }

  public static void renderFocusId(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    if (component instanceof UIInput) {
      renderFocusId(facesContext, (UIInput) component);
    }
  }

  public static void renderFocusId(final FacesContext facesContext, final UIInput component)
      throws IOException {
    if (renderErrorFocusId(facesContext, component)) {
      return;
    }
    if (ComponentUtils.getBooleanAttribute(component, Attributes.FOCUS)) {
      UIPage page = (UIPage) ComponentUtils.findPage(facesContext, component);
      String id = component.getClientId(facesContext);
      if (!StringUtils.isBlank(page.getFocusId()) && !page.getFocusId().equals(id)) {
        LOG.warn("page focusId = \"" + page.getFocusId() + "\" ignoring new value \""
            + id + "\"");
      } else {
        TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
        writer.writeJavascript("Tobago.focusId = '" + id + "';");
      }
    }
  }

  public static void createCssClass(FacesContext facesContext, UIComponent component) {
    String rendererName = getRendererName(facesContext, component);
    if (rendererName != null) {
      StyleClasses classes = StyleClasses.ensureStyleClasses(component);
      classes.updateClassAttributeAndMarkup(component, rendererName);
    }
  }

  public static String getRendererName(FacesContext facesContext, UIComponent component) {
    String rendererType = component.getRendererType();
    return rendererType.substring(0, 1).toLowerCase(Locale.ENGLISH) + rendererType.substring(1);
  }

  public static void writeLabelWithAccessKey(TobagoResponseWriter writer, LabelWithAccessKey label)
      throws IOException {
    int pos = label.getPos();
    String text = label.getText();
    if (pos == -1) {
      writer.writeText(text);
    } else {
      writer.writeText(text.substring(0, pos));
      writer.startElement(HtmlElements.U, null);
      writer.writeText(Character.toString(text.charAt(pos)));
      writer.endElement(HtmlElements.U);
      writer.writeText(text.substring(pos + 1));
    }
  }

  /** @deprecated since 1.5.7 and 1.6.0 */
  @Deprecated
  public static void setDefaultTransition(FacesContext facesContext, boolean transition)
      throws IOException {
    writeScriptLoader(facesContext, null, new String[]{"Tobago.transition = " + transition + ";"});
  }

  public static void addClickAcceleratorKey(
      FacesContext facesContext, String clientId, char key)
      throws IOException {
    //addClickAcceleratorKey(facesContext, clientId, key, null);
  }

  public static void addClickAcceleratorKey(
      FacesContext facesContext, String clientId, char key, String modifier)
      throws IOException {
    //String str
    //    = createOnclickAcceleratorKeyJsStatement(clientId, key, modifier);
    //writeScriptLoader(facesContext, null, new String[]{str});
  }

  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key) throws IOException {
    //addAcceleratorKey(facesContext, func, key, null);
  }

  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key, String modifier)
      throws IOException {
    String str = createAcceleratorKeyJsStatement(func, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{str});
  }

  public static String createOnclickAcceleratorKeyJsStatement(
      String clientId, char key, String modifier) {
    String func = "Tobago.clickOnElement('" + clientId + "');";
    return createAcceleratorKeyJsStatement(func, key, modifier);
  }

  public static String createAcceleratorKeyJsStatement(
      String func, char key, String modifier) {
    StringBuilder buffer = new StringBuilder("new Tobago.AcceleratorKey(function() {");
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
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void removeStyleAttribute(UIComponent component, String name) {
    Deprecation.LOG.error("HtmlRendererUtils.removeStyleAttribute() no longer supported. Use setter.");
  }

  @Deprecated
  public static void createHeaderAndBodyStyles(FacesContext facesContext, UIComponent component) {
    Deprecation.LOG.error("HtmlRendererUtils.createHeaderAndBodyStyles() no longer supported");
  }

  @Deprecated
  public static void createHeaderAndBodyStyles(FacesContext facesContext, UIComponent component, boolean width) {
    Deprecation.LOG.error("HtmlRendererUtils.createHeaderAndBodyStyles() no longer supported");
  }

  public static String createSrc(String src, String ext) {
    int dot = src.lastIndexOf('.');
    if (dot == -1) {
      LOG.warn("Image src without extension: '" + src + "'");
      return src;
    } else {
      return src.substring(0, dot) + ext + src.substring(dot);
    }
  }

  public static TobagoResponseWriter getTobagoResponseWriter(FacesContext facesContext) {

    ResponseWriter writer = facesContext.getResponseWriter();
    if (writer instanceof TobagoResponseWriter) {
      return (TobagoResponseWriter) writer;
    } else {
      return new TobagoResponseWriterWrapper(writer);
    }
  }

  public static void writeScriptLoader(FacesContext facesContext, String script)
      throws IOException {
    writeScriptLoader(facesContext, new String[]{script}, null);
  }

  public static void writeScriptLoader(FacesContext facesContext, String[] scripts, String[] afterLoadCmds)
      throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    if (scripts != null) {
      LOG.error("Scripts argument for writeScriptLoader not supported anymore!");
    }
    String allScripts = "[]";
    if (scripts != null) {
      allScripts = ResourceManagerUtils.getScriptsAsJSArray(facesContext, scripts);
    }
    boolean ajax = FacesContextUtils.isAjax(facesContext);
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
        for (String afterLoadCmd : afterLoadCmds) {
          String[] splittedStrings = StringUtils.split(afterLoadCmd, '\n'); // split on <CR> to have nicer JS
          for (String splitted : splittedStrings) {
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
    for (String afterLoadCmd : afterLoadCmds) {
      writer.write(afterLoadCmd);
    }
    }
    writer.endJavascript();
  }

  public static void writeStyleLoader(
      FacesContext facesContext, String[] styles) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startJavascript();
    writer.write("Tobago.ensureStyleFiles(");
    writer.write(ResourceManagerUtils.getStylesAsJSArray(facesContext, styles));
    writer.write(");");
    writer.endJavascript();
  }

  public static String getTitleFromTipAndMessages(FacesContext facesContext, UIComponent component) {
    String messages = ComponentUtils.getFacesMessageAsString(facesContext, component);
    return HtmlRendererUtils.addTip(messages, component.getAttributes().get(Attributes.TIP));
  }

  public static String addTip(String title, Object tip) {
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

  public static void renderSelectItems(UIInput component, List<SelectItem> items, Object[] values,
      TobagoResponseWriter writer, FacesContext facesContext) throws IOException {
    renderSelectItems(component, items, values, null, writer, facesContext);

  }

  public static void renderSelectItems(UIInput component, List<SelectItem> items, Object[] values, Boolean onlySelected,
      TobagoResponseWriter writer, FacesContext facesContext) throws IOException {

    if (LOG.isDebugEnabled()) {
      LOG.debug("value = '" + Arrays.toString(values) + "'");
    }
    for (SelectItem item : items) {
      if (item instanceof SelectItemGroup) {
        writer.startElement(HtmlElements.OPTGROUP, null);
        writer.writeAttribute(HtmlAttributes.LABEL, item.getLabel(), true);
        if (item.isDisabled()) {
          writer.writeAttribute(HtmlAttributes.DISABLED, true);
        }
        SelectItem[] selectItems = ((SelectItemGroup) item).getSelectItems();
        renderSelectItems(component, Arrays.asList(selectItems), values, onlySelected, writer, facesContext);
        writer.endElement(HtmlElements.OPTGROUP);
      } else {

        Object itemValue = item.getValue();
        // when using selectItem tag with a literal value: use the converted value
        if (itemValue instanceof String && values != null && values.length > 0 && !(values[0] instanceof String)) {
          itemValue = ComponentUtils.getConvertedValue(facesContext, component, (String) itemValue);
        }
        boolean contains = RenderUtils.contains(values, itemValue);
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
        String formattedValue = RenderUtils.getFormattedValue(facesContext, component, itemValue);
        writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
        if (item instanceof org.apache.myfaces.tobago.model.SelectItem) {
          String image = ((org.apache.myfaces.tobago.model.SelectItem) item).getImage();
          if (image != null) {
            String imagePath = ResourceManagerUtils.getImageWithPath(facesContext, image);
            writer.writeStyleAttribute("background-image: url('" + imagePath + "')");
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

  public static String getComponentIds(FacesContext context, UIComponent component, String[] componentId) {
    StringBuilder sb = new StringBuilder();
    for (String id : componentId) {
      if (!StringUtils.isBlank(id)) {
        if (sb.length() > 0) {
          sb.append(",");
        }
        String clientId = getComponentId(context, component, id);
        if (clientId != null) {
          sb.append(clientId);
        }
      }
    }
    return sb.toString();
  }

  public static String[] getComponentIdsAsList(
      FacesContext context, UIComponent component, String[] componentId) {
    List<String> result = new ArrayList<String>(componentId.length);
    for (String id : componentId) {
      if (!StringUtils.isBlank(id)) {
        String clientId = getComponentId(context, component, id);
        if (clientId != null) {
          result.add(clientId);
        }
      }
    }
    return (String[]) result.toArray(new String[result.size()]);
  }

  public static String getComponentId(FacesContext context, UIComponent component, String componentId) {
    UIComponent partiallyComponent = ComponentUtils.findComponent(component, componentId);
    if (partiallyComponent != null) {
      String clientId = partiallyComponent.getClientId(context);
      if (partiallyComponent instanceof UISheet) {
        int rowIndex = ((UISheet) partiallyComponent).getRowIndex();
        if (rowIndex >= 0 && clientId.endsWith(Integer.toString(rowIndex))) {
          return clientId.substring(0, clientId.lastIndexOf(NamingContainer.SEPARATOR_CHAR));
        }
      }
      return clientId;
    }
    LOG.error("No Component found for id " + componentId + " search base component " + component.getClientId(context));
    return null;
  }

  /**
   * @deprecated since Tobago 1.5.0.
   */
  @Deprecated
  public static String toStyleString(String key, Integer value) {
    StringBuilder buf = new StringBuilder();
    buf.append(key);
    buf.append(":");
    buf.append(value);
    buf.append("px; ");
    return buf.toString();
  }

  /**
   * @deprecated since Tobago 1.5.0.
   */
  @Deprecated
  public static String toStyleString(String key, String value) {
    StringBuilder buf = new StringBuilder();
    buf.append(key);
    buf.append(":");
    buf.append(value);
    buf.append("; ");
    return buf.toString();
  }

  /**
   * @deprecated since Tobago 1.5.0. Please use getTitleFromTipAndMessages and write it out.
   */
  @Deprecated
  public static void renderTip(UIComponent component, TobagoResponseWriter writer) throws IOException {
    Object objTip = component.getAttributes().get(Attributes.TIP);
    if (objTip != null) {
      String tip = String.valueOf(objTip);
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }
  }

  /**
   * @deprecated since Tobago 1.5.0. Please use getTitleFromTipAndMessages and write it out.
   */
  public static void renderImageTip(UIComponent component, TobagoResponseWriter writer) throws IOException {
    Object objTip = component.getAttributes().get(Attributes.TIP);
    if (objTip != null) {
      String tip = String.valueOf(objTip);
      writer.writeAttribute(HtmlAttributes.ALT, tip, true);
    } else {
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
    }
  }

  public static String getJavascriptString(String str) {
    if (str != null) {
      return "\"" + str + "\"";
    }
    return null;
  }

  public static String getRenderedPartiallyJavascriptArray(FacesContext facesContext, UICommand command) {
    if (command == null) {
      return null;
    }
    return getRenderedPartiallyJavascriptArray(facesContext, command, command);
  }

  public static String getRenderedPartiallyJavascriptArray(FacesContext facesContext, UIComponent searchBase,
      SupportsRenderedPartially supportsRenderedPartially) {
    String[] list = supportsRenderedPartially.getRenderedPartially();
    if (list == null || list.length == 0) {
      return null;
    }
    StringBuilder strBuilder = new StringBuilder();
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

  public static String getJavascriptArray(String[] list) {
    StringBuilder strBuilder = new StringBuilder();
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

  public static void renderDojoDndSource(FacesContext context, UIComponent component)
      throws IOException {
    Object objDojoType = component.getAttributes().get("dojoType");
    if (null != objDojoType && (objDojoType.equals("dojo.dnd.Source") || objDojoType.equals("dojo.dnd.Target"))) {
      FacesContextUtils.addOnloadScript(context, createDojoDndType(component,
          component.getClientId(context), String.valueOf(objDojoType)));
    }
  }

  public static void renderDojoDndItem(UIComponent component, TobagoResponseWriter writer, boolean addStyle)
      throws IOException {
    Object objDndType = component.getAttributes().get("dndType");
    if (objDndType != null) {
      writer.writeAttribute("dndType", String.valueOf(objDndType), false);
    }
    Object objDndData = component.getAttributes().get("dndData");
    if (objDndData != null) {
      writer.writeAttribute("dndData", String.valueOf(objDndData), false);
    }
    if (addStyle && (null != objDndType || null != objDndData)) {
      StyleClasses styles = StyleClasses.ensureStyleClasses(component);
      styles.addFullQualifiedClass("dojoDndItem");
    }
  }

  private static String createDojoDndType(UIComponent component, String clientId, String dojoType) {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("new ").append(dojoType).append("('").append(clientId).append("'");
    StringBuilder parameter = new StringBuilder();

    Object objHorizontal = component.getAttributes().get("horizontal");
    if (objHorizontal != null) {
      parameter.append("horizontal: ").append(String.valueOf(objHorizontal)).append(",");
    }
    Object objCopyOnly = component.getAttributes().get("copyOnly");
    if (objCopyOnly != null) {
      parameter.append("copyOnly: ").append(String.valueOf(objCopyOnly)).append(",");
    }
    Object objSkipForm = component.getAttributes().get("skipForm");
    if (objSkipForm != null) {
      parameter.append("skipForm: ").append(String.valueOf(objSkipForm)).append(",");
    }
    Object objWithHandles = component.getAttributes().get("withHandles");
    if (objWithHandles != null) {
      parameter.append("withHandles: ").append(String.valueOf(objWithHandles)).append(",");
    }
    Object objAccept = component.getAttributes().get("accept");
    if (objAccept != null) {
      String accept = null;
      if (objAccept instanceof String[]) {
        String[] allowed = (String[]) objAccept;
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
    Object objSingular = component.getAttributes().get("singular");
    if (objSingular != null) {
      parameter.append("singular: ").append(String.valueOf(objSingular)).append(",");
    }
    Object objCreator = component.getAttributes().get("creator");
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


  public static void renderCommandFacet(UIComponent component, FacesContext facesContext,
      TobagoResponseWriter writer) throws IOException {
    renderCommandFacet(component, component.getClientId(facesContext), facesContext, writer);
  }

  public static void renderCommandFacet(
      UIComponent component, String id, FacesContext facesContext, TobagoResponseWriter writer) throws IOException {
    if (ComponentUtils.getBooleanAttribute(component, Attributes.READONLY)
        || ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      return;
    }
    CommandMap commandMap = null;
    Map<String, UIComponent> facets = component.getFacets();
    for (Map.Entry<String, UIComponent> entry : facets.entrySet()) {
      UIComponent facetComponent = entry.getValue();
      if (facetComponent.isRendered()
          && (facetComponent instanceof AbstractUICommand || facetComponent instanceof UIForm)) {
        if (commandMap == null) {
          commandMap = new CommandMap();
        }
        String key = entry.getKey();
        if (Facets.RESIZE_ACTION.equals(key)) {
          key = Facets.RESIZE;
        }
        commandMap.addCommand(key, new Command(facesContext, entry.getValue(), id));
      }
    }
    if (commandMap != null) {
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(commandMap), true);
    }
  }

  public static boolean renderSheetCommands(UISheet sheet, FacesContext facesContext,
                                         TobagoResponseWriter writer) throws IOException {
    CommandMap commandMap = null;
    for (UIComponent child : (List<UIComponent>) sheet.getChildren()) {
      if (child instanceof UIColumnEvent) {
        UIColumnEvent columnEvent = (UIColumnEvent) child;
        if (columnEvent.isRendered()) {
          UIComponent selectionChild = (UIComponent) child.getChildren().get(0);
          if (selectionChild != null && selectionChild instanceof AbstractUICommand && selectionChild.isRendered()) {
            UICommand action = (UICommand) selectionChild;
            if (commandMap == null) {
              commandMap = new CommandMap();
            }
            commandMap.addCommand(columnEvent.getEvent(), new Command(facesContext, action, null));
          }
        }
      }
    }
    if (commandMap != null) {
      writer.writeAttribute(DataAttributes.ROWACTION, JsonUtils.encode(commandMap), true);
      return true;
    }
    return false;
  }


  public static void checkForCommandFacet(UIComponent component, FacesContext facesContext, TobagoResponseWriter writer)
      throws IOException {
    checkForCommandFacet(component, Arrays.asList(component.getClientId(facesContext)), facesContext, writer);
  }

  public static void checkForCommandFacet(UIComponent component, List<String> clientIds, FacesContext facesContext,
      TobagoResponseWriter writer) throws IOException {
    if (ComponentUtils.getBooleanAttribute(component, Attributes.READONLY)
        || ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      return;
    }
    Map<String, UIComponent> facets = component.getFacets();
    for (Map.Entry<String, UIComponent> entry : facets.entrySet()) {
      if (entry.getValue() instanceof UICommand) {
        addCommandFacet(clientIds, entry, facesContext, writer);
      }
    }
  }

  private static void addCommandFacet(
      List<String> clientIds, Map.Entry<String, UIComponent> facetEntry,
      FacesContext facesContext, TobagoResponseWriter writer)
      throws IOException {
    for (String clientId : clientIds) {
      writeScriptForClientId(clientId, facetEntry, facesContext, writer);
    }
  }

  /**
   * @deprecated Since Tobago 1.6.0. Because of CSP.
   */
  @Deprecated
  private static void writeScriptForClientId(
      String clientId, Map.Entry<String, UIComponent> facetEntry,
      FacesContext facesContext, TobagoResponseWriter writer) throws IOException {
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
      UIComponent facetComponent = facetEntry.getValue();

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
   * @deprecated since 1.6.0. JavaScript should not be rendered in the page. See CSP.
   */
  @Deprecated
  public static String createSubmitAction(String clientId, boolean transition, String target, String focus) {
    StringBuilder builder = new StringBuilder();
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

  /**
   * @deprecated since Tobago 1.5.0. Please use {@link org.apache.myfaces.tobago.renderkit.css.Classes}.
   */
  @Deprecated
  public static void removeStyleClasses(UIComponent cell) {
    Object obj = cell.getAttributes().get(Attributes.STYLE_CLASS);
    if (obj != null && obj instanceof StyleClasses && cell.getRendererType() != null) {
      StyleClasses styleClasses = (StyleClasses) obj;
      if (!styleClasses.isEmpty()) {
        String rendererName = cell.getRendererType().substring(0, 1).toLowerCase(Locale.ENGLISH)
            + cell.getRendererType().substring(1);
        styleClasses.removeTobagoClasses(rendererName);
      }
      if (styleClasses.isEmpty()) {
        cell.getAttributes().remove(Attributes.STYLE_CLASS);
      }
    }
  }

  public static void encodeContextMenu(FacesContext facesContext, TobagoResponseWriter writer, UIComponent parent)
      throws IOException {
    final UIComponent contextMenu = FacetUtils.getContextMenu(parent);
    if (contextMenu != null) {
      writer.startElement(HtmlElements.OL, contextMenu);
      writer.writeClassAttribute("tobago-menuBar tobago-menu-contextMenu");
      RenderUtils.encode(facesContext, contextMenu);
      writer.endElement(HtmlElements.OL);
    }
  }

    public static void addAcceleratorKey(FacesContext facesContext, UIComponent component, Character accessKey) {
      String clientId = component.getClientId(facesContext);
      String jsStatement = createOnclickAcceleratorKeyJsStatement(clientId, accessKey, null);
      FacesContextUtils.addMenuAcceleratorScript(facesContext, jsStatement);
    }

  public static void writeDataAttributes(
      FacesContext context, TobagoResponseWriter writer, UIComponent component)
      throws IOException {

    final Map<Object, Object> dataAttributes = ComponentUtils.getDataAttributes(component);
    if (dataAttributes == null) {
      return;
    }

    final ELContext elContext = context.getELContext();

    for (Map.Entry<Object, Object> entry : dataAttributes.entrySet()) {
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

package org.apache.myfaces.tobago.renderkit.html.util;

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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseJsonWriterImpl;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterWrapper;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @deprecated Please use HtmlRendererUtils
 */
@Deprecated
public final class HtmlRendererUtil {

  private static final Logger LOG = LoggerFactory.getLogger(HtmlRendererUtil.class);
  private static final String ERROR_FOCUS_KEY = HtmlRendererUtil.class.getName() + ".ErrorFocusId";

  private HtmlRendererUtil() {
    // to prevent instantiation
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  private static boolean renderErrorFocusId(final FacesContext facesContext, final UIInput input) throws IOException {
    if (ComponentUtils.isError(input)) {
      if (!FacesContext.getCurrentInstance().getExternalContext().getRequestMap().containsKey(ERROR_FOCUS_KEY)) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(ERROR_FOCUS_KEY, Boolean.TRUE);
        TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
        String id = input.getClientId(facesContext);        
        writer.writeJavascript("Tobago.errorFocusId = '" + id + "';");
        return true;
      } else {
        return true;
      }
    }
    return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().containsKey(ERROR_FOCUS_KEY);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void renderFocusId(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    if (component instanceof UIInput) {
      renderFocusId(facesContext, (UIInput) component);
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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
        TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
        writer.writeJavascript("Tobago.focusId = '" + id + "';");
      }
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void createCssClass(FacesContext facesContext, UIComponent component) {
    String rendererName = getRendererName(facesContext, component);
    if (rendererName != null) {
      StyleClasses classes = StyleClasses.ensureStyleClasses(component);
      classes.updateClassAttributeAndMarkup(component, rendererName);
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getRendererName(FacesContext facesContext, UIComponent component) {
    return HtmlRendererUtils.getRendererName(facesContext, component);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void writeLabelWithAccessKey(TobagoResponseWriter writer, LabelWithAccessKey label)
      throws IOException {
    int pos = label.getPos();
    String text = label.getText();
    if (pos == -1) {
      writer.writeText(text);
    } else {
      writer.writeText(text.substring(0, pos));
      writer.startElement(HtmlConstants.U, null);
      writer.writeText(Character.toString(text.charAt(pos)));
      writer.endElement(HtmlConstants.U);
      writer.writeText(text.substring(pos + 1));
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void setDefaultTransition(FacesContext facesContext, boolean transition)
      throws IOException {
    writeScriptLoader(facesContext, null, new String[]{"Tobago.transition = " + transition + ";"});
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void addClickAcceleratorKey(
      FacesContext facesContext, String clientId, char key)
      throws IOException {
    addClickAcceleratorKey(facesContext, clientId, key, null);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void addClickAcceleratorKey(
      FacesContext facesContext, String clientId, char key, String modifier)
      throws IOException {
    String str
        = createOnclickAcceleratorKeyJsStatement(clientId, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{str});
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key) throws IOException {
    addAcceleratorKey(facesContext, func, key, null);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key, String modifier)
      throws IOException {
    String str = createAcceleratorKeyJsStatement(func, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{str});
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String createOnclickAcceleratorKeyJsStatement(
      String clientId, char key, String modifier) {
    String func = "Tobago.clickOnElement('" + clientId + "');";
    return createAcceleratorKeyJsStatement(func, key, modifier);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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
  public static void replaceStyleAttribute(UIComponent component, String styleAttribute, String value) {
    Deprecation.LOG.error("HtmlRendererUtils.replaceStyleAttribute() no longer supported. Use setter.");
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void replaceStyleAttribute(UIComponent component, String attribute,
      String styleAttribute, String value) {
    Deprecation.LOG.error("HtmlRendererUtils.replaceStyleAttribute() no longer supported. Use setter.");
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void replaceStyleAttribute(UIComponent component, String styleAttribute, int value) {
    Deprecation.LOG.error("HtmlRendererUtils.replaceStyleAttribute() no longer supported. Use setter.");
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void replaceStyleAttribute(UIComponent component, String attribute,
      String styleAttribute, int value) {
    Deprecation.LOG.error("HtmlRendererUtils.replaceStyleAttribute() no longer supported. Use setter.");

  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  private static Style ensureStyleAttributeMap(UIComponent component) {
    return ensureStyleAttributeMap(component, Attributes.STYLE);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  private static Style ensureStyleAttributeMap(UIComponent component, String attribute) {
    final Map attributes = component.getAttributes();
    Style style = (Style) attributes.get(attribute);
    if (style == null) {
      style = new Style();
      attributes.put(attribute, style);
    }
    return style;
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String replaceStyleAttribute(String style, String name,
      String value) {
    style = removeStyleAttribute(style != null ? style : "", name);
    return style + " " + name + ": " + value + ";";
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String removeStyleAttribute(String style, String name) {
    if (style == null) {
      return null;
    }
    String pattern = name + "\\s*?:[^;]*?;";
    return style.replaceAll(pattern, "").trim();
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void removeStyleAttribute(UIComponent component, String name) {
    Deprecation.LOG.error("HtmlRendererUtils.removeStyleAttribute() no longer supported. Use setter.");
  }

  /**
   * @deprecated Please use StyleClasses.ensureStyleClasses(component).add(clazz);
   */
  @Deprecated
  public static void addCssClass(UIComponent component, String clazz) {
    StyleClasses.ensureStyleClasses(component).addFullQualifiedClass(clazz);
  }

  @Deprecated
  public static void createHeaderAndBodyStyles(FacesContext facesContext, UIComponent component) {
    Deprecation.LOG.error("HtmlRendererUtils.createHeaderAndBodyStyles() no longer supported");
  }

  @Deprecated
  public static void createHeaderAndBodyStyles(FacesContext facesContext, UIComponent component, boolean width) {
    Deprecation.LOG.error("HtmlRendererUtils.createHeaderAndBodyStyles() no longer supported");
  }

  /**
   * @deprecated Please use StyleClasses.ensureStyleClasses(component).updateClassAttribute(renderer, component);
   */
  @Deprecated
  public static void updateClassAttribute(String cssClass, String rendererName, UIComponent component) {
    throw new UnsupportedOperationException(
        "Please use StyleClasses.ensureStyleClasses(component).updateClassAttribute(renderer, component)");
  }

  /**
   * @deprecated Please use StyleClasses.addMarkupClass()
   */
  @Deprecated
  public static void addMarkupClass(UIComponent component, String rendererName,
      String subComponent, StringBuilder tobagoClass) {
    throw new UnsupportedOperationException("Please use StyleClasses.addMarkupClass()");
  }

  /**
   * @deprecated Please use StyleClasses.addMarkupClass()
   */
  @Deprecated
  public static void addMarkupClass(UIComponent component, String rendererName, StyleClasses classes) {
    classes.addMarkupClass(component, rendererName);
  }

  /**
   * @deprecated
   */
  @Deprecated
  public static void addImageSources(FacesContext facesContext, TobagoResponseWriter writer, String src, String id)
      throws IOException {
    Deprecation.LOG.error("using deprecated API");
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String createSrc(String src, String ext) {
    int dot = src.lastIndexOf('.');
    if (dot == -1) {
      LOG.warn("Image src without extension: '" + src + "'");
      return src;
    } else {
      return src.substring(0, dot) + ext + src.substring(dot);
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static TobagoResponseWriter getTobagoResponseWriter(FacesContext facesContext) {

    ResponseWriter writer = facesContext.getResponseWriter();
    if (writer instanceof TobagoResponseWriter) {
      return (TobagoResponseWriter) writer;
    } else {
      return new TobagoResponseWriterWrapper(writer);
    }
  }

  /**
   * @deprecated use TobagoResponseWriter.writeJavascript()
   */
  @Deprecated
  public static void writeJavascript(ResponseWriter writer, String script) throws IOException {
    startJavascript(writer);
    writer.write(script);
    endJavascript(writer);
  }

  /**
   * @deprecated use TobagoResponseWriter.writeJavascript()
   */
  @Deprecated
  public static void startJavascript(ResponseWriter writer) throws IOException {
    writer.startElement(HtmlConstants.SCRIPT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", null);
    writer.write("\n<!--\n");
  }

  /**
   * @deprecated use TobagoResponseWriter.writeJavascript()
   */
  @Deprecated
  public static void endJavascript(ResponseWriter writer) throws IOException {
    writer.write("\n// -->\n");
    writer.endElement(HtmlConstants.SCRIPT);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void writeScriptLoader(FacesContext facesContext, String script)
      throws IOException {
    writeScriptLoader(facesContext, new String[]{script}, null);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void writeScriptLoader(FacesContext facesContext, String[] scripts, String[] afterLoadCmds)
      throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    String allScripts = "[]";
    if (scripts != null) {
      allScripts = ResourceManagerUtils.getScriptsAsJSArray(facesContext, scripts);
    }
    boolean ajax = false;
    if (facesContext instanceof TobagoFacesContext) {
      ajax = ((TobagoFacesContext) facesContext).isAjax();
    }
    writer.startJavascript();
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

    writer.endJavascript();
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void writeStyleLoader(
      FacesContext facesContext, String[] styles) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    writer.startJavascript();
    writer.write("Tobago.ensureStyleFiles(");
    writer.write(ResourceManagerUtils.getStylesAsJSArray(facesContext, styles));
    writer.write(");");
    writer.endJavascript();
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getTitleFromTipAndMessages(FacesContext facesContext, UIComponent component) {
    String messages = ComponentUtils.getFacesMessageAsString(facesContext, component);
    return HtmlRendererUtil.addTip(messages, component.getAttributes().get(Attributes.TIP));
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void renderSelectItems(
      UIInput component, List<SelectItem> items, Object[] values,
      TobagoResponseWriter writer, FacesContext facesContext) throws IOException {
    HtmlRendererUtils.renderSelectItems(component, items, values, writer, facesContext);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getComponentId(FacesContext context, UIComponent component, String componentId) {
    UIComponent partiallyComponent = ComponentUtils.findComponent(component, componentId);
    if (partiallyComponent != null) {
      String clientId = partiallyComponent.getClientId(context);
      if (partiallyComponent instanceof UIData) {
        int rowIndex = ((UIData) partiallyComponent).getRowIndex();
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
   * @deprecated Please use HtmlRendererUtils
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
   * @deprecated Please use HtmlRendererUtils
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
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void renderTip(UIComponent component, TobagoResponseWriter writer) throws IOException {
    Object objTip = component.getAttributes().get(Attributes.TIP);
    if (objTip != null) {
      String tip = String.valueOf(objTip);
      if (writer instanceof TobagoResponseJsonWriterImpl) {
        tip = AjaxInternalUtils.encodeJavaScriptString(tip);
      }
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void renderImageTip(UIComponent component, TobagoResponseWriter writer) throws IOException {
    Object objTip = component.getAttributes().get(Attributes.TIP);
    if (objTip != null) {
      String tip = String.valueOf(objTip);
      if (writer instanceof TobagoResponseJsonWriterImpl) {
        tip = AjaxInternalUtils.encodeJavaScriptString(tip);
      }
      writer.writeAttribute(HtmlAttributes.ALT, tip, true);
    } else {
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getJavascriptString(String str) {
    if (str != null) {
      return "\"" + str + "\"";
    }
    return null;
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getRenderedPartiallyJavascriptArray(FacesContext facesContext, UICommand command) {
    if (command == null) {
      return null;
    }
    String[] list = command.getRenderedPartially();
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("[");
    for (int i = 0; i < list.length; i++) {
      if (i != 0) {
        strBuilder.append(",");
      }
      strBuilder.append("\"");
      strBuilder.append(HtmlRendererUtil.getComponentId(facesContext, command, list[i]));
      strBuilder.append("\"");
    }
    strBuilder.append("]");
    return strBuilder.toString();
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void renderDojoDndSource(FacesContext context, UIComponent component)
      throws IOException {
    Object objDojoType = component.getAttributes().get("dojoType");
    if (null != objDojoType && (objDojoType.equals("dojo.dnd.Source") || objDojoType.equals("dojo.dnd.Target"))) {
      if (context instanceof TobagoFacesContext) {
        ((TobagoFacesContext) context).getOnloadScripts().add(createDojoDndType(component,
            component.getClientId(context), String.valueOf(objDojoType)));
      }
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void checkForCommandFacet(UIComponent component, FacesContext facesContext, TobagoResponseWriter writer)
      throws IOException {
    checkForCommandFacet(component, Arrays.asList(component.getClientId(facesContext)), facesContext, writer);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  private static void addCommandFacet(List<String> clientIds, Map.Entry<String, UIComponent> facetEntry,
                               FacesContext facesContext, TobagoResponseWriter writer) throws
      IOException {
    for (String clientId : clientIds) {
      writeScriptForClientId(clientId, facetEntry, facesContext, writer);
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  private static void writeScriptForClientId(String clientId, Map.Entry<String, UIComponent> facetEntry,
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
      writer.write(HtmlRendererUtil.getComponentIds(facesContext, facetEntry.getValue(),
              ((UICommand) facetEntry.getValue()).getRenderedPartially()));
      writer.write("','");
      writer.write(facetEntry.getValue().getClientId(facesContext)); 
      writer.write("', {})});\n");
      writer.write("}");
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
        writer.write(facetAction);
      } else {
        writer.write("Tobago.submitAction(this, '");
        writer.write(facetComponent.getClientId(facesContext));
        writer.write("', ");
        writer.write(Boolean.toString(ComponentUtils.getBooleanAttribute(facetComponent, Attributes.TRANSITION)));
        writer.write(", null, '");
        writer.write(clientId);
        writer.write("')");
      }
      writer.write("});\n}");
      writer.endJavascript();
    }
  }

  /**
   * @deprecated Please use HtmlRendererUtils
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
}

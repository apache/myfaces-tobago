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

package org.apache.myfaces.tobago.renderkit.html;

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.webapp.TobagoResponseWriterWrapper;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import java.io.IOException;
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
        final TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
        final String id = input.getClientId(facesContext);
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
    HtmlRendererUtils.renderFocusId(facesContext, component);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void createCssClass(final FacesContext facesContext, final UIComponent component) {
    HtmlRendererUtils.createCssClass(facesContext, component);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getRendererName(final FacesContext facesContext, final UIComponent component) {
    return HtmlRendererUtils.getRendererName(facesContext, component);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void writeLabelWithAccessKey(final TobagoResponseWriter writer, final LabelWithAccessKey label)
      throws IOException {
    HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void setDefaultTransition(final FacesContext facesContext, final boolean transition)
      throws IOException {
    writeScriptLoader(facesContext, null, new String[]{"Tobago.transition = " + transition + ";"});
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void addClickAcceleratorKey(
      final FacesContext facesContext, final String clientId, final char key)
      throws IOException {
    addClickAcceleratorKey(facesContext, clientId, key, null);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void addClickAcceleratorKey(
      final FacesContext facesContext, final String clientId, final char key, final String modifier)
      throws IOException {
    final String str
        = createOnclickAcceleratorKeyJsStatement(clientId, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{str});
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void addAcceleratorKey(
      final FacesContext facesContext, final String func, final char key) throws IOException {
    addAcceleratorKey(facesContext, func, key, null);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void addAcceleratorKey(
      final FacesContext facesContext, final String func, final char key, final String modifier)
      throws IOException {
    final String str = createAcceleratorKeyJsStatement(func, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{str});
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String createOnclickAcceleratorKeyJsStatement(
      final String clientId, final char key, final String modifier) {
    final String func = "Tobago.clickOnElement('" + clientId + "');";
    return createAcceleratorKeyJsStatement(func, key, modifier);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String createAcceleratorKeyJsStatement(
      final String func, final char key, final String modifier) {
    return HtmlRendererUtils.createAcceleratorKeyJsStatement(func, key, modifier);
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void replaceStyleAttribute(
      final UIComponent component, final String styleAttribute, final String value) {
    Deprecation.LOG.error("HtmlRendererUtils.replaceStyleAttribute() no longer supported. Use setter.");
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void replaceStyleAttribute(final UIComponent component, final String attribute,
      final String styleAttribute, final String value) {
    Deprecation.LOG.error("HtmlRendererUtils.replaceStyleAttribute() no longer supported. Use setter.");
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void replaceStyleAttribute(final UIComponent component, final String styleAttribute, final int value) {
    Deprecation.LOG.error("HtmlRendererUtils.replaceStyleAttribute() no longer supported. Use setter.");
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void replaceStyleAttribute(final UIComponent component, final String attribute,
      final String styleAttribute, final int value) {
    Deprecation.LOG.error("HtmlRendererUtils.replaceStyleAttribute() no longer supported. Use setter.");

  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  private static Style ensureStyleAttributeMap(final UIComponent component) {
    return ensureStyleAttributeMap(component, Attributes.STYLE);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  private static Style ensureStyleAttributeMap(final UIComponent component, final String attribute) {
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
  public static String replaceStyleAttribute(String styleParameter, final String name,
      final String value) {
    String style = styleParameter;
    style = removeStyleAttribute(style != null ? style : "", name);
    return style + " " + name + ": " + value + ";";
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String removeStyleAttribute(final String style, final String name) {
    if (style == null) {
      return null;
    }
    final String pattern = name + "\\s*?:[^;]*?;";
    return style.replaceAll(pattern, "").trim();
  }

  /**
   * @deprecated Please use setter;
   */
  @Deprecated
  public static void removeStyleAttribute(final UIComponent component, final String name) {
    Deprecation.LOG.error("HtmlRendererUtils.removeStyleAttribute() no longer supported. Use setter.");
  }

  /**
   * @deprecated Please use StyleClasses.ensureStyleClasses(component).add(clazz);
   */
  @Deprecated
  public static void addCssClass(final UIComponent component, final String clazz) {
    StyleClasses.ensureStyleClasses(component).addFullQualifiedClass(clazz);
  }

  /**
   * @deprecated xxx
   */
  @Deprecated
  public static void createHeaderAndBodyStyles(final FacesContext facesContext, final UIComponent component) {
    Deprecation.LOG.error("HtmlRendererUtils.createHeaderAndBodyStyles() no longer supported");
  }

  /**
   * @deprecated xxx
   */
  @Deprecated
  public static void createHeaderAndBodyStyles(
      final FacesContext facesContext, final UIComponent component, final boolean width) {
    Deprecation.LOG.error("HtmlRendererUtils.createHeaderAndBodyStyles() no longer supported");
  }

  /**
   * @deprecated Please use StyleClasses.ensureStyleClasses(component).updateClassAttribute(renderer, component);
   */
  @Deprecated
  public static void updateClassAttribute(
      final String cssClass, final String rendererName, final UIComponent component) {
    throw new UnsupportedOperationException(
        "Please use StyleClasses.ensureStyleClasses(component).updateClassAttribute(renderer, component)");
  }

  /**
   * @deprecated Please use StyleClasses.addMarkupClass()
   */
  @Deprecated
  public static void addMarkupClass(final UIComponent component, final String rendererName,
      final String subComponent, final StringBuilder tobagoClass) {
    throw new UnsupportedOperationException("Please use StyleClasses.addMarkupClass()");
  }

  /**
   * @deprecated Please use StyleClasses.addMarkupClass()
   */
  @Deprecated
  public static void addMarkupClass(
      final UIComponent component, final String rendererName, final StyleClasses classes) {
    classes.addMarkupClass(component, rendererName);
  }

  /**
   * @deprecated xxx
   */
  @Deprecated
  public static void addImageSources(
      final FacesContext facesContext, final TobagoResponseWriter writer, final String src, final String id)
      throws IOException {
    Deprecation.LOG.error("using deprecated API");
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String createSrc(final String src, final String ext) {
    final int dot = src.lastIndexOf('.');
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
  public static TobagoResponseWriter getTobagoResponseWriter(final FacesContext facesContext) {

    final ResponseWriter writer = facesContext.getResponseWriter();
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
  public static void writeJavascript(final ResponseWriter writer, final String script) throws IOException {
    startJavascript(writer);
    writer.write(script);
    endJavascript(writer);
  }

  /**
   * @deprecated use TobagoResponseWriter.writeJavascript()
   */
  @Deprecated
  public static void startJavascript(final ResponseWriter writer) throws IOException {
    writer.startElement(HtmlElements.SCRIPT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", null);
    writer.write("\n<!--\n");
  }

  /**
   * @deprecated use TobagoResponseWriter.writeJavascript()
   */
  @Deprecated
  public static void endJavascript(final ResponseWriter writer) throws IOException {
    writer.write("\n// -->\n");
    writer.endElement(HtmlElements.SCRIPT);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void writeScriptLoader(final FacesContext facesContext, final String script)
      throws IOException {
    writeScriptLoader(facesContext, new String[]{script}, null);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void writeScriptLoader(
      final FacesContext facesContext, final String[] scripts, final String[] afterLoadCmds)
      throws IOException {
    HtmlRendererUtils.writeScriptLoader(facesContext, scripts, afterLoadCmds);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void writeStyleLoader(
      final FacesContext facesContext, final String[] styles) throws IOException {
    HtmlRendererUtils.writeStyleLoader(facesContext, styles);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getTitleFromTipAndMessages(final FacesContext facesContext, final UIComponent component) {
    final String messages = ComponentUtils.getFacesMessageAsString(facesContext, component);
    return HtmlRendererUtil.addTip(messages, component.getAttributes().get(Attributes.TIP));
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String addTip(String titleParameter, final Object tip) {
    String title = titleParameter;
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
      final UIInput component, final List<SelectItem> items, final Object[] values,
      final TobagoResponseWriter writer, final FacesContext facesContext) throws IOException {
    HtmlRendererUtils.renderSelectItems(component, items, values, (String[]) null, writer, facesContext);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
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

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getComponentId(
      final FacesContext context, final UIComponent component, final String componentId) {
    final UIComponent partiallyComponent = ComponentUtils.findComponent(component, componentId);
    if (partiallyComponent != null) {
      final String clientId = partiallyComponent.getClientId(context);
      if (partiallyComponent instanceof UIData) {
        final int rowIndex = ((UIData) partiallyComponent).getRowIndex();
        if (rowIndex >= 0 && clientId.endsWith(Integer.toString(rowIndex))) {
          return clientId.substring(0, clientId.lastIndexOf(UINamingContainer.getSeparatorChar(context)));
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
  public static String toStyleString(final String key, final Integer value) {
    final StringBuilder buf = new StringBuilder();
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
  public static String toStyleString(final String key, final String value) {
    final StringBuilder buf = new StringBuilder();
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
  public static void renderTip(final UIComponent component, final TobagoResponseWriter writer) throws IOException {
    HtmlRendererUtils.renderTip(component, writer);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void renderImageTip(final UIComponent component, final TobagoResponseWriter writer) throws IOException {
    HtmlRendererUtils.renderImageTip(component, writer);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getJavascriptString(final String str) {
    if (str != null) {
      return "\"" + str + "\"";
    }
    return null;
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static String getRenderedPartiallyJavascriptArray(final FacesContext facesContext, final UICommand command) {
    if (command == null) {
      return null;
    }
    final String[] list = command.getRenderedPartially();
    final StringBuilder strBuilder = new StringBuilder();
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
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void renderDojoDndSource(final FacesContext context, final UIComponent component)
      throws IOException {
    HtmlRendererUtils.renderDojoDndSource(context, component);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void renderDojoDndItem(
      final UIComponent component, final TobagoResponseWriter writer, final boolean addStyle)
      throws IOException {
    HtmlRendererUtils.renderDojoDndItem(component, writer, addStyle);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  private static String createDojoDndType(final UIComponent component, final String clientId, final String dojoType) {
    return HtmlRendererUtils.createDojoDndType(component, clientId, dojoType);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void checkForCommandFacet(
      final UIComponent component, final FacesContext facesContext, final TobagoResponseWriter writer)
      throws IOException {
    HtmlRendererUtils.checkForCommandFacet(component, facesContext, writer);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void checkForCommandFacet(
      final UIComponent component, final List<String> clientIds, final FacesContext facesContext,
      final TobagoResponseWriter writer) throws IOException {
    HtmlRendererUtils.checkForCommandFacet(component, clientIds, facesContext, writer);
  }

  /**
   * @deprecated Please use HtmlRendererUtils
   */
  @Deprecated
  public static void removeStyleClasses(final UIComponent cell) {
    final Object obj = cell.getAttributes().get(Attributes.STYLE_CLASS);
    if (obj != null && obj instanceof StyleClasses && cell.getRendererType() != null) {
      final StyleClasses styleClasses = (StyleClasses) obj;
      if (!styleClasses.isEmpty()) {
        final String rendererName = cell.getRendererType().substring(0, 1).toLowerCase(Locale.ENGLISH)
            + cell.getRendererType().substring(1);
        styleClasses.removeTobagoClasses(rendererName);
      }
      if (styleClasses.isEmpty()) {
        cell.getAttributes().remove(Attributes.STYLE_CLASS);
      }
    }
  }
}

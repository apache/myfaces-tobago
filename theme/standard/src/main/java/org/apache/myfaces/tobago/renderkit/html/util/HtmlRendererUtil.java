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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOCUS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_BODY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_HEADER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_OUT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ONCLICK;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.PageFacesContextWrapper;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutInformationProvider;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBaseWrapper;
import org.apache.myfaces.tobago.renderkit.LayoutableRenderer;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriterWrapper;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/*
 * User: weber
 * Date: Jan 11, 2005
 * Time: 4:59:36 PM
 */
public final class HtmlRendererUtil {

  private static final Log LOG = LogFactory.getLog(HtmlRendererUtil.class);

  private HtmlRendererUtil() {
    // to prevent instantiation
  }

  public static void renderFocusId(FacesContext facesContext, UIComponent component)
      throws IOException {

    if (ComponentUtil.getBooleanAttribute(component, ATTR_FOCUS)) {
      UIPage page = (UIPage) ComponentUtil.findPage(facesContext, component);
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

  public static void prepareRender(FacesContext facesContext, UIComponent component) {
    // xxx find a better way for this question: isTobago or isLayoutable something like that.
    LayoutableRenderer layoutRenderer = ComponentUtil.getRenderer(facesContext, component);
    if (layoutRenderer != null && !(layoutRenderer instanceof RendererBaseWrapper)) {
      createCssClass(facesContext, component);
      layoutWidth(facesContext, component);
      layoutHeight(facesContext, component);
    }
  }

  public static HtmlStyleMap prepareInnerStyle(UIComponent component) {
    HtmlStyleMap htmlStyleMap = new HtmlStyleMap();
    Integer innerSpaceInteger = (Integer)
        component.getAttributes().get(ATTR_INNER_WIDTH);
    if (innerSpaceInteger != null && innerSpaceInteger != -1) {
      htmlStyleMap.put("width", innerSpaceInteger);
    }
    innerSpaceInteger = (Integer)
        component.getAttributes().get(ATTR_INNER_HEIGHT);
    if (innerSpaceInteger != null && innerSpaceInteger != -1) {
      htmlStyleMap.put("height", innerSpaceInteger);
    }
    return htmlStyleMap;
  }


  public static void createCssClass(FacesContext facesContext, UIComponent component) {
    String rendererName = getRendererName(facesContext, component);
    if (rendererName != null) {
      StyleClasses classes = StyleClasses.ensureStyleClasses(component);
      classes.updateClassAttributeAndMarkup(component, rendererName);
    }
  }

  public static String getRendererName(FacesContext facesContext, UIComponent component) {
    final String rendererType = component.getRendererType();
    //final String family = component.getFamily();
    if (rendererType != null//&& !"facelets".equals(family)
        ) {
      LayoutableRenderer layoutableRendererBase = ComponentUtil.getRenderer(facesContext, component);
      if (layoutableRendererBase != null) {
        return layoutableRendererBase.getRendererName(rendererType);
      }
    }
    return null;
  }

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

  public static void setDefaultTransition(FacesContext facesContext, boolean transition)
      throws IOException {
    writeScriptLoader(facesContext, null, new String[]{"Tobago.transition = " + transition + ";"});
  }

  public static void addClickAcceleratorKey(
      FacesContext facesContext, String clientId, char key)
      throws IOException {
    addClickAcceleratorKey(facesContext, clientId, key, null);
  }

  public static void addClickAcceleratorKey(
      FacesContext facesContext, String clientId, char key, String modifier)
      throws IOException {
    String str
        = createOnclickAcceleratorKeyJsStatement(clientId, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{str});
  }

  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key) throws IOException {
    addAcceleratorKey(facesContext, func, key, null);
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

  public static String getLayoutSpaceStyle(UIComponent component) {
    StringBuilder sb = new StringBuilder();
    Integer space = LayoutUtil.getLayoutSpace(component, ATTR_LAYOUT_WIDTH, ATTR_LAYOUT_WIDTH);
    if (space != null) {
      sb.append(" width: ");
      sb.append(space);
      sb.append("px;");
    }
    space = LayoutUtil.getLayoutSpace(component, ATTR_LAYOUT_HEIGHT, ATTR_LAYOUT_HEIGHT);
    if (space != null) {
      sb.append(" height: ");
      sb.append(space);
      sb.append("px;");
    }
    return sb.toString();
  }

  public static Integer getStyleAttributeIntValue(HtmlStyleMap style, String name) {
    if (style == null) {
      return null;
    }
    return style.getInt(name);
  }

  public static String getStyleAttributeValue(String style, String name) {
    if (style == null) {
      return null;
    }
    String value = null;
    StringTokenizer st = new StringTokenizer(style, ";");
    while (st.hasMoreTokens()) {
      String attribute = st.nextToken().trim();
      if (attribute.startsWith(name)) {
        value = attribute.substring(attribute.indexOf(':') + 1).trim();
      }
    }
    return value;
  }


  public static void replaceStyleAttribute(UIComponent component, String styleAttribute, String value) {
    HtmlStyleMap style = ensureStyleAttributeMap(component);
    style.put(styleAttribute, value);
  }

  public static void replaceStyleAttribute(UIComponent component, String attribute,
      String styleAttribute, String value) {
    HtmlStyleMap style = ensureStyleAttributeMap(component, attribute);
    style.put(styleAttribute, value);
  }

  public static void replaceStyleAttribute(UIComponent component, String styleAttribute, int value) {
    HtmlStyleMap style = ensureStyleAttributeMap(component);
    style.put(styleAttribute, value);
  }

  public static void replaceStyleAttribute(UIComponent component, String attribute,
      String styleAttribute, int value) {
    HtmlStyleMap style = ensureStyleAttributeMap(component, attribute);
    style.put(styleAttribute, value);

  }

  private static HtmlStyleMap ensureStyleAttributeMap(UIComponent component) {
    return ensureStyleAttributeMap(component, ATTR_STYLE);
  }

  private static HtmlStyleMap ensureStyleAttributeMap(UIComponent component, String attribute) {
    final Map attributes = component.getAttributes();
    HtmlStyleMap style = (HtmlStyleMap) attributes.get(attribute);
    if (style == null) {
      style = new HtmlStyleMap();
      attributes.put(attribute, style);
    }
    return style;
  }

  public static String replaceStyleAttribute(String style, String name,
      String value) {
    style = removeStyleAttribute(style != null ? style : "", name);
    return style + " " + name + ": " + value + ";";
  }

  public static String removeStyleAttribute(String style, String name) {
    if (style == null) {
      return null;
    }
    String pattern = name + "\\s*?:[^;]*?;";
    return style.replaceAll(pattern, "").trim();
  }

  public static void removeStyleAttribute(UIComponent component, String name) {
    ensureStyleAttributeMap(component).remove(name);
  }

  /**
   * @deprecated Please use StyleClasses.ensureStyleClasses(component).add(clazz);
   */
  @Deprecated
  public static void addCssClass(UIComponent component, String clazz) {
    StyleClasses.ensureStyleClasses(component).addFullQualifiedClass(clazz);
  }

  public static void layoutWidth(FacesContext facesContext, UIComponent component) {
    layoutSpace(facesContext, component, true);
  }

  public static void layoutHeight(FacesContext facesContext, UIComponent component) {
    layoutSpace(facesContext, component, false);
  }

  public static void layoutSpace(FacesContext facesContext, UIComponent component,
      boolean width) {

    // prepare html 'style' attribute
    Integer layoutSpace;
    String layoutAttribute;
    String styleAttribute;
    if (width) {
      layoutSpace = LayoutUtil.getLayoutWidth(component);
      layoutAttribute = ATTR_LAYOUT_WIDTH;
      styleAttribute = HtmlAttributes.WIDTH;
    } else {
      layoutSpace = LayoutUtil.getLayoutHeight(component);
      layoutAttribute = ATTR_LAYOUT_HEIGHT;
      styleAttribute = HtmlAttributes.HEIGHT;
    }
    int space = -1;
    if (layoutSpace != null) {
      space = layoutSpace.intValue();
    }
    if (space == -1 && (!RENDERER_TYPE_OUT.equals(component.getRendererType()))) {
      UIComponent parent = component.getParent();
      space = LayoutUtil.getInnerSpace(facesContext, parent, width);
      if (space > 0 && !ComponentUtil.isFacetOf(component, parent)) {
        component.getAttributes().put(layoutAttribute, Integer.valueOf(space));
        if (width) {
          component.getAttributes().remove(ATTR_INNER_WIDTH);
        } else {
          component.getAttributes().remove(ATTR_INNER_HEIGHT);
        }
      }
    }
    if (space > 0) {
      LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, component);
      if (layoutSpace != null
          || !ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
        int styleSpace = space;
        if (renderer != null) {
          if (width) {
            styleSpace -= renderer.getComponentExtraWidth(facesContext, component);
          } else {
            styleSpace -= renderer.getComponentExtraHeight(facesContext, component);
          }
        }

        replaceStyleAttribute(component, styleAttribute, styleSpace);

      }
      UIComponent layout = component.getFacet(FACET_LAYOUT);
      if (layout != null) {
        int layoutSpace2 = LayoutUtil.getInnerSpace(facesContext, component,
            width);
        if (layoutSpace2 > 0) {
          layout.getAttributes().put(layoutAttribute, Integer.valueOf(layoutSpace2));
        }
      }
    }
  }

  public static void createHeaderAndBodyStyles(FacesContext facesContext, UIComponent component) {
    createHeaderAndBodyStyles(facesContext, component, true);
    createHeaderAndBodyStyles(facesContext, component, false);
  }

  public static void createHeaderAndBodyStyles(FacesContext facesContext, UIComponent component, boolean width) {
    LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, component);
    HtmlStyleMap style = (HtmlStyleMap) component.getAttributes().get(ATTR_STYLE);
    Integer styleSpace = null;
    try {
      styleSpace = style.getInt(width ? "width" : "height");
    } catch (Exception e) {
      /* ignore */
    }
    if (styleSpace != null) {
      int bodySpace = 0;
      int headerSpace = 0;
      if (!width) {
        if (renderer != null) {
          headerSpace = renderer.getHeaderHeight(facesContext, component);
        }
        bodySpace = styleSpace - headerSpace;
      }
      HtmlStyleMap headerStyle = ensureStyleAttributeMap(component, ATTR_STYLE_HEADER);
      HtmlStyleMap bodyStyle = ensureStyleAttributeMap(component, ATTR_STYLE_BODY);
      if (width) {
        headerStyle.put("width", styleSpace);
        bodyStyle.put("width", styleSpace);
      } else {
        headerStyle.put("height", headerSpace);
        bodyStyle.put("height", bodySpace);
      }
    }
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

  public static void addImageSources(FacesContext facesContext, TobagoResponseWriter writer, String src, String id)
      throws IOException {
    writer.startJavascript();
    writer.write("new Tobago.Image('");
    writer.write(id);
    writer.write("','");
    String img = ResourceManagerUtil.getImageWithPath(facesContext, src, false);
    writer.write(img!=null?img:"");
    writer.write("','");
    String disabled = ResourceManagerUtil.getImageWithPath(facesContext, createSrc(src, "Disabled"), true);
    writer.write(disabled!=null?disabled:"");
    writer.write("','");
    String hover = ResourceManagerUtil.getImageWithPath(facesContext, createSrc(src, "Hover"), true);
    writer.write(hover!=null?hover:"");
    writer.write("');");
    writer.endJavascript();
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

  public static void writeScriptLoader(FacesContext facesContext, String script)
      throws IOException {
    writeScriptLoader(facesContext, new String[]{script}, null);
  }

  public static void writeScriptLoader(FacesContext facesContext, String[] scripts, String[] afterLoadCmds)
      throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    String allScripts = "[]";
    if (scripts != null) {
      allScripts = ResourceManagerUtil.getScriptsAsJSArray(facesContext, scripts);
    }

    StringBuilder script = new StringBuilder(128);
    script.append("new Tobago.ScriptLoader(\n    ");
    script.append(allScripts);

    if (afterLoadCmds != null && afterLoadCmds.length > 0) {
      script.append(", \n");
      boolean first = true;
      for (String afterLoadCmd : afterLoadCmds) {
        String[] splittedStrings = StringUtils.split(afterLoadCmd, '\n'); // split on <CR> to have nicer JS
        for (String splitted : splittedStrings) {
          String cmd = StringUtils.replace(splitted, "\\", "\\\\");
          cmd = StringUtils.replace(cmd, "\"", "\\\"");
          script.append(first ? "          " : "        + ");
          script.append("\"");
          script.append(cmd);
          script.append("\"\n");
          first = false;
        }
      }
    }
    script.append(");");

    writer.writeJavascript(script.toString());
  }

  public static void writeStyleLoader(
      FacesContext facesContext, String[] styles) throws IOException {
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    StringBuilder builder = new StringBuilder(64);
    builder.append("Tobago.ensureStyleFiles(\n    ");
    builder.append(ResourceManagerUtil.getStylesAsJSArray(facesContext, styles));
    builder.append(");");
    writer.writeJavascript(builder.toString());
  }

  public static String getTitleFromTipAndMessages(FacesContext facesContext, UIComponent component) {
    String messages = ComponentUtil.getFacesMessageAsString(facesContext, component);
    return HtmlRendererUtil.addTip(messages, component.getAttributes().get(ATTR_TIP));
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

    if (LOG.isDebugEnabled()) {
      LOG.debug("value = '" + Arrays.toString(values) + "'");
    }
    for (SelectItem item : items) {
      if (item instanceof SelectItemGroup) {
        writer.startElement(HtmlConstants.OPTGROUP, null);
        writer.writeAttribute(HtmlAttributes.LABEL, item.getLabel(), true);
        if (item.isDisabled()) {
          writer.writeAttribute(HtmlAttributes.DISABLED, true);
        }
        SelectItem[] selectItems = ((SelectItemGroup) item).getSelectItems();
        renderSelectItems(component, Arrays.asList(selectItems), values, writer, facesContext);
        writer.endElement(HtmlConstants.OPTGROUP);
      } else {
        writer.startElement(HtmlConstants.OPTION, null);
        final Object itemValue = item.getValue();
        String formattedValue = RenderUtil.getFormattedValue(facesContext, component, itemValue);
        writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, true);
        if (item instanceof org.apache.myfaces.tobago.model.SelectItem) {
          String image = ((org.apache.myfaces.tobago.model.SelectItem) item).getImage();
          if (image != null) {
            String imagePath = ResourceManagerUtil.getImageWithPath(facesContext, image);
            writer.writeStyleAttribute("background-image=url('" + imagePath + "')");
          }
        }
        if (item instanceof SupportsMarkup) {
          StyleClasses optionStyle = new StyleClasses();
          optionStyle.addMarkupClass((SupportsMarkup) item, getRendererName(facesContext, component), "option");
          if (!optionStyle.isEmpty()) {
            writer.writeClassAttribute(optionStyle);
          }
        }
        if (RenderUtil.contains(values, item.getValue())) {
          writer.writeAttribute(HtmlAttributes.SELECTED, true);
        }
        if (item.isDisabled()) {
          writer.writeAttribute(HtmlAttributes.DISABLED, true);
        }
        writer.writeText(item.getLabel());
        writer.endElement(HtmlConstants.OPTION);
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

  public static String getComponentId(FacesContext context, UIComponent component, String componentId) {
    UIComponent partiallyComponent = ComponentUtil.findComponent(component, componentId);
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
    LOG.error("No Component found for id " + componentId);
    return null;
  }

  public static String toStyleString(String key, Integer value) {
    StringBuilder buf = new StringBuilder();
    buf.append(key);
    buf.append(":");
    buf.append(value);
    buf.append("px; ");
    return buf.toString();
  }

  public static String toStyleString(String key, String value) {
    StringBuilder buf = new StringBuilder();
    buf.append(key);
    buf.append(":");
    buf.append(value);
    buf.append("; ");
    return buf.toString();
  }

  public static void renderTip(UIComponent component, TobagoResponseWriter writer) throws IOException {
    Object objTip = component.getAttributes().get(ATTR_TIP);
    if (objTip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, String.valueOf(objTip), true);
    }
  }

  public static void renderImageTip(UIComponent component, TobagoResponseWriter writer) throws IOException {
    Object objTip = component.getAttributes().get(ATTR_TIP);
    if (objTip != null) {
      writer.writeAttribute(HtmlAttributes.ALT, String.valueOf(objTip), true);
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
      if (context instanceof PageFacesContextWrapper) {
        ((PageFacesContextWrapper) context).getOnloadScripts().add(createDojoDndType(component,
            component.getClientId(context), String.valueOf(objDojoType)));
      }
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

  public static void checkForCommandFacet(UIComponent component, FacesContext facesContext, TobagoResponseWriter writer)
      throws IOException {
    checkForCommandFacet(component, Arrays.asList(component.getClientId(facesContext)), facesContext, writer);
  }

  public static void checkForCommandFacet(UIComponent component, List<String> clientIds, FacesContext facesContext,
                                      TobagoResponseWriter writer) throws IOException {
    if (ComponentUtil.getBooleanAttribute(component, ATTR_READONLY)
        || ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      return;
    }
    Map<String, UIComponent> facets = component.getFacets();
    for (Map.Entry<String, UIComponent> entry : facets.entrySet()) {
      if (entry.getValue() instanceof UICommand) {
        addCommandFacet(clientIds, entry, facesContext, writer);
      }
    }
  }

  private static void addCommandFacet(List<String> clientIds, Map.Entry<String, UIComponent> facetEntry,
                               FacesContext facesContext, TobagoResponseWriter writer) throws
      IOException {
    for (String clientId : clientIds) {
      writeScriptForClientId(clientId, facetEntry, facesContext, writer);
    }
  }

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
      writer.write("\", function(){Tobago.reloadComponent('");
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
      String facetAction = (String) facetComponent.getAttributes().get(ATTR_ONCLICK);
      if (facetAction != null) {
        writer.write(facetAction);
      } else {
        writer.write("Tobago.submitAction('");
        writer.write(facetComponent.getClientId(facesContext));
        writer.write("')");
      }
      writer.write("});\n}");
      writer.endJavascript();
    }
  }
}

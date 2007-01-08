package org.apache.myfaces.tobago.renderkit.html;

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
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.event.PopupActionListener;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOCUS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MARKUP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_BODY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_HEADER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_INNER;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_OUT;
import static org.apache.myfaces.tobago.TobagoConstants.TOBAGO_CSS_CLASS_PREFIX;
import static org.apache.myfaces.tobago.TobagoConstants.TOBAGO_CSS_CLASS_SUFFIX_DEFAULT;
import static org.apache.myfaces.tobago.TobagoConstants.TOBAGO_CSS_CLASS_SUFFIX_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.TOBAGO_CSS_CLASS_SUFFIX_ERROR;
import static org.apache.myfaces.tobago.TobagoConstants.TOBAGO_CSS_CLASS_SUFFIX_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.TOBAGO_CSS_CLASS_SUFFIX_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.TOBAGO_CSS_CLASS_SUFFIX_REQUIRED;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_POPUP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
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
      UIPage page = ComponentUtil.findPage(component);
      String id = component.getClientId(facesContext);
      if (page.getFocusId() != null && !page.getFocusId().equals(id)) {
        LOG.warn("page focusId = \"" + page.getFocusId() + "\" ignoring new value \""
            + id + "\"");
      } else {
        ResponseWriter writer = facesContext.getResponseWriter();
        startJavascript(writer);
        writer.write("Tobago.focusId = '" + id + "';");
        endJavascript(writer);
      }
    }
  }

  public static void prepareRender(FacesContext facesContext, UIComponent component) {
    createCssClass(facesContext, component);
    layoutWidth(facesContext, component);
    layoutHeight(facesContext, component);
  }

  public static void prepareInnerStyle(UIComponent component) {
    String innerStyle = "";
    Integer innerSpaceInteger = (Integer)
        component.getAttributes().get(ATTR_INNER_WIDTH);
    if (innerSpaceInteger != null && innerSpaceInteger != -1) {
      innerStyle = "width: " + innerSpaceInteger + "px;";
    }
    innerSpaceInteger = (Integer)
        component.getAttributes().get(ATTR_INNER_HEIGHT);
    if (innerSpaceInteger != null && innerSpaceInteger != -1) {
      innerStyle += " height: " + innerSpaceInteger + "px;";
    }
    component.getAttributes().put(ATTR_STYLE_INNER, innerStyle);
  }


  public static void createCssClass(FacesContext facesContext, UIComponent component) {
    final String rendererType = component.getRendererType();
    if (rendererType != null) {
      String rendererName = ComponentUtil.getRenderer(facesContext, component).getRendererName(rendererType);
      createClassAttribute(component, rendererName);
    }

  }

  public static String getRendererName(FacesContext facesContext, UIComponent component) {
    final String rendererType = component.getRendererType();
    if (rendererType != null) {
      return ComponentUtil.getRenderer(facesContext, component).getRendererName(rendererType);
    }
    return null;
  }

  public static void writeLabelWithAccessKey(ResponseWriter writer,
      LabelWithAccessKey label)
      throws IOException {
    int pos = label.getPos();
    String text = label.getText();
    if (pos == -1) {
      writer.writeText(text, null);
    } else {
      writer.writeText(text.substring(0, pos), null);
      writer.write("<u>");
      writer.writeText(text.charAt(pos), null);
      writer.write("</u>");
      writer.writeText(text.substring(pos + 1), null);
    }
  }

  public static void addClickAcceleratorKey(
      FacesContext facesContext, String clientId, char key)
      throws IOException {
    addClickAcceleratorKey(facesContext, clientId, key, null);
  }

  public static void addClickAcceleratorKey(
      FacesContext facesContext, String clientId, char key, String modifier)
      throws IOException {
    StringBuilder buffer
        = createOnclickAcceleratorKeyJsStatement(clientId, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{buffer.toString()});
  }

  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key) throws IOException {
    addAcceleratorKey(facesContext, func, key, null);
  }

  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key, String modifier)
      throws IOException {
    StringBuilder buffer = createAcceleratorKeyJsStatement(func, key, modifier);
    writeScriptLoader(facesContext, null, new String[]{buffer.toString()});
  }

  public static StringBuilder createOnclickAcceleratorKeyJsStatement(
      String clientId, char key, String modifier) {
    String func = "Tobago.clickOnElement('" + clientId + "');";
    return createAcceleratorKeyJsStatement(func, key, modifier);
  }

  public static StringBuilder createAcceleratorKeyJsStatement(
      String func, char key, String modifier) {
    StringBuilder buffer = new StringBuilder();
    buffer.append("new Tobago.AcceleratorKey(function() {");
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
    return buffer;
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

  public static void addCssClass(UIComponent component, String newClass) {
    final Map attributes = component.getAttributes();
    String cssClass = (String) attributes.get(ATTR_STYLE_CLASS);
    if (cssClass == null) {
      attributes.put(ATTR_STYLE_CLASS, newClass);
    } else if (cssClass.indexOf(newClass + " ") == -1
        || !cssClass.equals(newClass) || !cssClass.endsWith(newClass)) {
      cssClass += " " + newClass;
      attributes.put(ATTR_STYLE_CLASS, cssClass);
    }
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
        component.getAttributes().put(layoutAttribute, new Integer(space));
        if (width) {
          component.getAttributes().remove(ATTR_INNER_WIDTH);
        } else {
          component.getAttributes().remove(ATTR_INNER_HEIGHT);
        }
      }
    }
    if (space > 0) {
      RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
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
          layout.getAttributes().put(layoutAttribute, new Integer(layoutSpace2));
        }
      }
    }
  }

  public static void createHeaderAndBodyStyles(FacesContext facesContext, UIComponent component) {
    createHeaderAndBodyStyles(facesContext, component, true);
    createHeaderAndBodyStyles(facesContext, component, false);
  }

  public static void createHeaderAndBodyStyles(FacesContext facesContext, UIComponent component, boolean width) {
    RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
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

  private static void createClassAttribute(UIComponent component, String name) {
    if (LOG.isDebugEnabled()) {
      Object styleClassO = component.getAttributes().get(ATTR_STYLE_CLASS);
      if (styleClassO != null) {
        LOG.debug("styleClassO = '" + styleClassO.getClass().getName() + "'");
      }
    }
    String styleClass
        = (String) component.getAttributes().get(ATTR_STYLE_CLASS);
    styleClass = updateClassAttribute(styleClass, name, component);
    component.getAttributes().put(ATTR_STYLE_CLASS, styleClass);
  }

  static String removeTobagoClasses(String s, String rendererName) {
    int length = s.length();
    if (length == 0) {
      return s;
    }
    StringBuilder newS = new StringBuilder(length);
    String toFind = TOBAGO_CSS_CLASS_PREFIX + rendererName;
    int lastSpace = 0;
    for (int i = 0; i < length; i++) {
      char c = s.charAt(i);
      if (c == ' ' || i == length - 1) {
        String part = s.substring(lastSpace == 0 ? 0 : lastSpace + 1, i + 1);
        if (!part.startsWith(toFind)) {
          newS.append(part);
        }
        lastSpace = i;
      }
    }
    return newS.toString();
  }

  public static String updateClassAttribute(String cssClass, String rendererName, UIComponent component) {
    if (cssClass != null) {
      // first remove old tobago-<rendererName>-<type> classes from class-attribute
      cssClass = removeTobagoClasses(cssClass, rendererName).trim();
    } else {
      cssClass = "";
    }
    StringBuilder prefix = new StringBuilder(TOBAGO_CSS_CLASS_PREFIX).append(rendererName);
    StringBuilder tobagoClass = new StringBuilder(64).append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_DEFAULT);
    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_DISABLED);
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_READONLY)) {
      tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_READONLY);
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
      tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_INLINE);
    }
    if (component instanceof UIInput) {
      UIInput input = (UIInput) component;
      if (ComponentUtil.isError(input)) {
        tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_ERROR);
      }
      if (input.isRequired()) {
        tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_REQUIRED);
      }
    }

    addMarkupClass(component, rendererName, tobagoClass);
    return tobagoClass.append(cssClass).toString();
  }

  public static void addMarkupClass(UIComponent component, String rendererName,
      String subComponent, StringBuilder tobagoClass) {

    if (component instanceof SupportsMarkup) {
      String markup = ComponentUtil.getStringAttribute(component, ATTR_MARKUP);
      if (StringUtils.isNotEmpty(markup)) {
        Theme theme = ClientProperties.getInstance(FacesContext.getCurrentInstance().getViewRoot()).getTheme();
        if (theme.getRenderersConfig().isMarkupSupported(rendererName, markup)) {
          tobagoClass.append(TOBAGO_CSS_CLASS_PREFIX).append(rendererName).append("-").append(subComponent)
              .append("-markup-").append(markup).append(" ");
        } else {
          LOG.warn("Unknown markup='" + markup + "'");
        }
      }
    }
  }

  public static void addMarkupClass(UIComponent component, String rendererName, StringBuilder tobagoClass) {

    if (component instanceof SupportsMarkup) {
      String markup = ComponentUtil.getStringAttribute(component, ATTR_MARKUP);
      if (StringUtils.isNotEmpty(markup)) {
        Theme theme = ClientProperties.getInstance(FacesContext.getCurrentInstance().getViewRoot()).getTheme();
        if (theme.getRenderersConfig().isMarkupSupported(rendererName, markup)) {
          tobagoClass.append(TOBAGO_CSS_CLASS_PREFIX).append(rendererName)
              .append("-markup-").append(markup).append(" ");
        } else {
          LOG.warn("Unknown markup='" + markup + "'");
        }
      }
    }
  }

  public static void addImageSources(
      FacesContext facesContext, ResponseWriter writer, String src, String id)
      throws IOException {
    StringBuilder buffer = new StringBuilder();
    buffer.append("new Tobago.Image('");
    buffer.append(id);
    buffer.append("','");
    buffer.append(ResourceManagerUtil.getImageWithPath(
        facesContext, src, false));
    buffer.append("','");
    buffer.append(ResourceManagerUtil.getImageWithPath(
        facesContext, createSrc(src, "Disabled"), true));
    buffer.append("','");
    buffer.append(ResourceManagerUtil.getImageWithPath(
        facesContext, createSrc(src, "Hover"), true));
    buffer.append("');");
    writeJavascript(writer, buffer.toString());
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

  public static void writeJavascript(ResponseWriter writer, String script)
      throws IOException {
    startJavascript(writer);
    writer.writeText(script, null);
    endJavascript(writer);
  }

  public static void startJavascript(ResponseWriter writer) throws IOException {
    writer.startElement(HtmlConstants.SCRIPT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", null);
    writer.writeText("\n<!--\n", null);
  }

  public static void endJavascript(ResponseWriter writer) throws IOException {
    writer.writeText("\n// -->\n", null);
    writer.endElement(HtmlConstants.SCRIPT);
  }

  public static void writeScriptLoader(FacesContext facesContext, String script)
      throws IOException {
    writeScriptLoader(facesContext, new String[]{script}, null);
  }

  public static void writeScriptLoader(
      FacesContext facesContext, String[] scripts, String[] afterLoadCmds)
      throws IOException {
    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();
    startJavascript(writer);

    String allScripts = "[]";
    if (scripts != null) {
      allScripts = ResourceManagerUtil.getScriptsAsJSArray(facesContext, scripts);
    }

    writer.writeText("new Tobago.ScriptLoader(\n    ", null);
    writer.writeText(allScripts, null);
    if (afterLoadCmds != null && afterLoadCmds.length > 0) {
      writer.writeText(", \n", null);
      for (int i = 0; i < afterLoadCmds.length; i++) {
        String cmd = StringUtils.replace(afterLoadCmds[i], "\\", "\\\\");
        cmd = StringUtils.replace(cmd, "\"", "\\\"");
        writer.writeText(i == 0 ? "          " : "        + ", null);
        writer.writeText("\"" + cmd + "\"\n", null);
      }
    }
    writer.writeText(");", null);

    endJavascript(writer);
  }

  public static void writeStyleLoader(
      FacesContext facesContext, String[] styles) throws IOException {
    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();
    startJavascript(writer);

    String allStyles
        = ResourceManagerUtil.getStylesAsJSArray(facesContext, styles);

    writer.writeText("Tobago.ensureStyleFiles(\n    ", null);
    writer.writeText(allStyles, null);
    writer.writeText(");", null);

    endJavascript(writer);
  }


  public static String addTip(String title, String tip) {
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
      LOG.debug("value = '" + values + "'");
    }
    for (SelectItem item : items) {
      if (item instanceof SelectItemGroup) {
        writer.startElement(HtmlConstants.OPTGROUP, null);
        writer.writeAttribute(HtmlAttributes.LABEL, item.getLabel(), null);
        SelectItem[] selectItems = ((SelectItemGroup) item).getSelectItems();
        renderSelectItems(component, Arrays.asList(selectItems), values, writer, facesContext);
        writer.endElement(HtmlConstants.OPTGROUP);
      } else {
        writer.startElement(HtmlConstants.OPTION, null);
        final Object itemValue = item.getValue();
        String formattedValue
            = RenderUtil.getFormattedValue(facesContext, component, itemValue);
        writer.writeAttribute(HtmlAttributes.VALUE, formattedValue, null);
        if (RenderUtil.contains(values, item.getValue())) {
          writer.writeAttribute(HtmlAttributes.SELECTED, HtmlAttributes.SELECTED, null);
        }
        writer.writeText(item.getLabel(), null);
        writer.endElement(HtmlConstants.OPTION);
      }
    }
  }

  public static String createOnClick(FacesContext facesContext, UIComponent component) {

    // TODO move this
    UIPopup popup = (UIPopup) component.getFacet(FACET_POPUP);
    if (popup != null && component instanceof UICommand) {
      UICommand command = (UICommand) component;
      if (!ComponentUtil.containsPopupActionListener(command)) {
        command.addActionListener(new PopupActionListener(popup));
      }
    }

    //String type = (String) component.getAttributes().get(ATTR_TYPE);
    //String command = (String) component.getAttributes().get(ATTR_ACTION_STRING);
    String clientId = component.getClientId(facesContext);
    boolean defaultCommand = ComponentUtil.getBooleanAttribute(component,
        TobagoConstants.ATTR_DEFAULT_COMMAND);
    boolean preserveOnclick = true;
    String onclick;
    if (component.getAttributes().get(TobagoConstants.ATTR_ACTION_LINK) != null) {
      onclick = "Tobago.navigateToUrl('"
          + HtmlUtils.generateUrl(facesContext,
          (String) component.getAttributes().get(TobagoConstants.ATTR_ACTION_LINK)) + "');";
      // FIXME !!
      //} else if (COMMAND_TYPE_RESET.equals(type)) {
      //  onclick = null;
    } else if (component.getAttributes().get(TobagoConstants.ATTR_ACTION_ONCLICK) != null) {
      onclick = prepareOnClick(facesContext, component);

    } else if (component instanceof UICommand
        && ((UICommand) component).getRenderedPartially().length > 0) {


      String[] componentId = ((UICommand) component).getRenderedPartially();


      if (componentId != null && componentId.length == 1) {


        if (component.getFacet(FACET_POPUP) !=null) {
          onclick = "Tobago.openPopupWithAction('" + getComponentId(facesContext, component, componentId[0]) + "', '"
              + clientId + "')";
        } else {
          onclick = "Tobago.reloadComponent('" + getComponentId(facesContext, component, componentId[0]) + "','"
              + clientId + "', {});";
        }
      } else {
        onclick = "";
        LOG.error("more than one parially rendered component is currently not supported " + componentId);
      }

    } else if (defaultCommand) {
      ComponentUtil.findPage(component).setDefaultActionId(clientId);
//      onclick = "Tobago.setAction('" + clientId + "');";
      onclick = null;
    } else {
      onclick = "Tobago.submitAction('" + clientId + "');";
      preserveOnclick = false;
    }

    if (component.getAttributes().get(TobagoConstants.ATTR_POPUP_CLOSE) != null
        && ComponentUtil.isInPopup(component)) {
      String value = (String) component.getAttributes().get(TobagoConstants.ATTR_POPUP_CLOSE);
      if (value.equals("immediate")) {
        onclick = (preserveOnclick ? onclick : "") + "Tobago.closePopup(this);";
      } else if (value.equals("afterSubmit")
          && component instanceof UICommand
          && ((UICommand) component).getRenderedPartially().length > 0) {
        onclick += "Tobago.closePopup(this);";
      }

    }
    return onclick;
  }

  public static String getComponentId(FacesContext context, UIComponent component, String componentId) {
    if (componentId.startsWith(":")) {
      return componentId.substring(1);
    } else {
      UIComponent partiallyComponent = component.findComponent(componentId);
      if (partiallyComponent != null) {
        return partiallyComponent.getClientId(context);
      }
    }
    return null;
  }

  public static String prepareOnClick(FacesContext facesContext, UIComponent component) {
    String onclick;
    onclick = (String) component.getAttributes().get(TobagoConstants.ATTR_ACTION_ONCLICK);
    if (onclick.contains("@autoId")) {
      onclick = onclick.replace("@autoId", component.getClientId(facesContext));
    }
    return onclick;
  }

  public static String appendConfirmationScript(String onclick,
      UIComponent component, FacesContext facesContext) {
    ValueHolder confirmation
        = (ValueHolder) component.getFacet(TobagoConstants.FACET_CONFIRMATION);
    if (confirmation != null) {
      if (onclick != null) {
        onclick = "confirm('" + confirmation.getValue() + "') && " + onclick;
      } else {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Facet '" + TobagoConstants.FACET_CONFIRMATION + "' is not supported for "
              + "this type of button. id = '"
              + component.getClientId(facesContext) + "'");
        }
      }
    }
    return onclick;
  }

  public static String getEmptyHref(FacesContext facesContext) {
    ClientProperties clientProperties = ClientProperties.getInstance(facesContext);
    return clientProperties.getUserAgent().isMsie() ? "#" : "javascript:;";
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
}

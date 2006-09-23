package org.apache.myfaces.tobago.renderkit.html;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

/**
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
    StringBuffer buffer
        = createOnclickAcceleratorKeyJsStatement(clientId, key, modifier);
    writeScriptLoader(facesContext, null, new String[] {buffer.toString()});
  }

  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key) throws IOException {
    addAcceleratorKey(facesContext, func, key, null);
  }

  public static void addAcceleratorKey(
      FacesContext facesContext, String func, char key, String modifier)
      throws IOException {
    StringBuffer buffer = createAcceleratorKeyJsStatement(func, key, modifier);
    writeScriptLoader(facesContext, null, new String[] {buffer.toString()});
  }

  public static StringBuffer createOnclickAcceleratorKeyJsStatement(
      String clientId, char key, String modifier) {
    String func = "Tobago.clickOnElement('" + clientId + "');";
    return createAcceleratorKeyJsStatement(func, key, modifier);
  }

  public static StringBuffer createAcceleratorKeyJsStatement(
      String func, char key, String modifier) {
    StringBuffer buffer = new StringBuffer();
    buffer.append("new Tobago.AcceleratorKey(function() {");
    buffer.append(func);
    if (!func.endsWith(";")) {
      buffer.append(';');
    }
    buffer.append("}, \"");
    buffer.append(key);
    if (modifier !=  null) {
      buffer.append("\", \"");
      buffer.append(modifier);
    }
    buffer.append("\");");
    return buffer;
  }

  public static String getLayoutSpaceStyle(UIComponent component) {
    StringBuffer sb = new StringBuffer();
    Integer space = LayoutUtil.getLayoutSpace(component, ATTR_LAYOUT_WIDTH,
        ATTR_LAYOUT_WIDTH);
    if (space != null) {
      sb.append(" width: ");
      sb.append(space);
      sb.append("px;");
    }
    space = LayoutUtil.getLayoutSpace(component, ATTR_LAYOUT_HEIGHT,
        ATTR_LAYOUT_HEIGHT);
    if (space != null) {
      sb.append(" height: ");
      sb.append(space);
      sb.append("px;");
    }
    return sb.toString();
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
    final Map attributes = component.getAttributes();
    String style = (String) attributes.get(ATTR_STYLE);
    style = replaceStyleAttribute(style, styleAttribute, value);
    attributes.put(ATTR_STYLE, style);
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

        replaceStyleAttribute(component, styleAttribute, styleSpace + "px");

      }
      UIComponent layout = component.getFacet("layout");
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
    String style = (String) component.getAttributes().get(ATTR_STYLE);
    int styleSpace = -1;
    try {
      styleSpace = Integer.parseInt(getStyleAttributeValue(style, width ? "width" : "height").replaceAll("\\D", ""));
    } catch (Exception e) {
      /* ignore */
    }
    if (styleSpace != -1) {
      int bodySpace = 0;
      int headerSpace = 0;
      if (!width) {
        if (renderer != null) {
          headerSpace = renderer.getHeaderHeight(facesContext, component);
        }
        bodySpace = styleSpace - headerSpace;
      }

      String headerStyle;
      String bodyStyle;
      if (width) {
        headerStyle = "width: " + styleSpace + "px;";
        bodyStyle = "width: " + styleSpace + "px;";
      } else {
        headerStyle =
            (String)
            component.getAttributes().get(ATTR_STYLE_HEADER);
        if (headerStyle == null) {
          LOG.warn("headerStyle attribute == null, set to empty String");
          headerStyle = "";
        }
        headerStyle
            = headerStyle.replaceAll("height:\\s\\d+px;", "").trim();
        headerStyle += " height: " + headerSpace + "px;";
        bodyStyle
            = (String) component.getAttributes().get(ATTR_STYLE_BODY);
        if (bodyStyle == null) {
          LOG.warn("bodyStyle attribute == null, set to empty String");
          bodyStyle = "";
        }
        bodyStyle = bodyStyle.replaceAll("height:\\s\\d+px;", "").trim();
        bodyStyle += " height: " + bodySpace + "px;";
      }
      component.getAttributes().put(ATTR_STYLE_HEADER, headerStyle);
      component.getAttributes().put(ATTR_STYLE_BODY, bodyStyle);
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

  private static String removeTobagoClasses(String s, String rendererName) {
    int length = s.length();
    if (length == 0) {
      return s;
    }
    StringBuffer newS = new StringBuffer(length);
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
    StringBuffer prefix = new StringBuffer(TOBAGO_CSS_CLASS_PREFIX).append(rendererName);
    StringBuffer tobagoClass = new StringBuffer(64).append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_DEFAULT);
    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_DISABLED);
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_READONLY)) {
      tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_READONLY);
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
      tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_INLINE);
    }
    if (ComponentUtil.isError(component)) {
      tobagoClass.append(prefix).append(TOBAGO_CSS_CLASS_SUFFIX_ERROR);
    }
    String markup = ComponentUtil.getStringAttribute(component, ATTR_MARKUP);
    if (StringUtils.isNotEmpty(markup)) {
      if (markup.equals("strong") || markup.equals("deleted")) {
        tobagoClass.append(prefix).append("-markup-").append(markup).append(" ");
      } else {
        LOG.warn("Unknown markup='" + markup + "'");
      }
    }

    return tobagoClass.append(cssClass).toString();
  }

  public static void addImageSources(
      FacesContext facesContext, ResponseWriter writer, String src, String id)
      throws IOException {
    StringBuffer buffer = new StringBuffer();
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
    writeScriptLoader(facesContext, new String[] {script}, null);
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

  public static void main(String[] args) {
    System.out.println(removeTobagoClasses("bla bla bla tobago-test-inline bla bla", "test"));
    System.out.println(removeTobagoClasses("tobago-test-inline blablubber bla", "test"));
    System.out.println(removeTobagoClasses("bla bla bla tobago-2test-inline bla tobago-test-blubber bla blubb",
        "test"));
    System.out.println(removeTobagoClasses("bla bla bla tobago-testXXX", "test"));
    System.out.println(removeTobagoClasses("tobago-test", "test"));
    System.out.println(removeTobagoClasses(" x x ", "test"));
    System.out.println(removeTobagoClasses("", "test"));
    System.out.println(removeTobagoClasses("hallo", "test"));
  }

}

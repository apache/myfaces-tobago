package com.atanion.tobago.renderkit.html;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.lang.StringUtils;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.renderkit.LabelWithAccessKey;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.util.LayoutUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jan 11, 2005
 * Time: 4:59:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class HtmlRendererUtil {

  private static final Log LOG = LogFactory.getLog(HtmlRendererUtil.class);

  public static void renderFocusId(FacesContext facesContext, UIComponent component)
      throws IOException {

    if (ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_FOCUS)) {
      UIPage page = ComponentUtil.findPage(component);
      String id = component.getClientId(facesContext);
      if (page.getFocusId() != null && ! page.getFocusId().equals(id)) {
        LOG.warn(
          "page focusId = \"" + page.getFocusId() + "\" ignoring new value \""
          + id + "\"");
      }
      else {
        ResponseWriter writer = facesContext.getResponseWriter();
        startJavascript(writer);
        writer.write("focusId = '" + id + "';");
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
        component.getAttributes().get(TobagoConstants.ATTR_INNER_WIDTH);
    if (innerSpaceInteger != null && innerSpaceInteger.intValue() != -1) {
      innerStyle = "width: " + innerSpaceInteger + "px;";
    }
    innerSpaceInteger = (Integer)
        component.getAttributes().get(TobagoConstants.ATTR_INNER_HEIGHT);
    if (innerSpaceInteger != null && innerSpaceInteger.intValue() != -1) {
      innerStyle += " height: " + innerSpaceInteger + "px;";
    }
    component.getAttributes().put(TobagoConstants.ATTR_STYLE_INNER, innerStyle);
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
      writer.writeText(new Character(text.charAt(pos)), null);
      writer.write("</u>");
      writer.writeText(text.substring(pos + 1), null);
    }
  }

  public static String getLayoutSpaceStyle(UIComponent component) {
    StringBuffer sb = new StringBuffer();
    Integer  space = LayoutUtil.getLayoutSpace(component, TobagoConstants.ATTR_LAYOUT_WIDTH,
        TobagoConstants.ATTR_LAYOUT_WIDTH);
    if (space != null) {
      sb.append(" width: ");
      sb.append(space);
      sb.append("px;");
    }
    space = LayoutUtil.getLayoutSpace(component, TobagoConstants.ATTR_LAYOUT_HEIGHT,
        TobagoConstants.ATTR_LAYOUT_HEIGHT);
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
    String style = (String) attributes.get(TobagoConstants.ATTR_STYLE);
    style = replaceStyleAttribute(style, styleAttribute, value);
    attributes.put(TobagoConstants.ATTR_STYLE, style);
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
    String cssClass = (String) attributes.get(TobagoConstants.ATTR_STYLE_CLASS);
    if (cssClass == null) {
      attributes.put(TobagoConstants.ATTR_STYLE_CLASS, newClass);
    } else if (cssClass.indexOf(newClass + " ") == -1
        || !cssClass.equals(newClass) || !cssClass.endsWith(newClass)) {
      attributes.put(TobagoConstants.ATTR_STYLE_CLASS, cssClass += " " + newClass);
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
      layoutAttribute = TobagoConstants.ATTR_LAYOUT_WIDTH;
      styleAttribute = "width";
    } else {
      layoutSpace = LayoutUtil.getLayoutHeight(component);
      layoutAttribute = TobagoConstants.ATTR_LAYOUT_HEIGHT;
      styleAttribute = "height";
    }
    int space = -1;
    if (layoutSpace != null) {
      space = layoutSpace.intValue();
    }
    if (space == -1 && (!TobagoConstants.RENDERER_TYPE_OUT.equals(component.getRendererType()))) {
      UIComponent parent = component.getParent();
      space = LayoutUtil.getInnerSpace(facesContext, parent, width);
      if (space > 0 && !ComponentUtil.isFacetOf(component, parent)) {
        component.getAttributes().put(layoutAttribute, new Integer(space));
        if (width) {
          component.getAttributes().remove(TobagoConstants.ATTR_INNER_WIDTH);
        } else {
          component.getAttributes().remove(TobagoConstants.ATTR_INNER_HEIGHT);
        }
      }
    }
    if (space > 0) {
      RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
      if (layoutSpace != null
          || !ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_INLINE)) {
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
    String style = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE);
    int styleSpace = -1;
    try {
      styleSpace = Integer.parseInt(getStyleAttributeValue(style, width ? "width" : "height").replaceAll("\\D", ""));
    } catch (Exception e) { /* ignore */}
    if (styleSpace != -1) {
      int bodySpace = 0;
      int headerSpace = 0;
      if (! width) {
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
            component.getAttributes().get(TobagoConstants.ATTR_STYLE_HEADER);
        if (headerStyle == null) {
          LOG.warn("headerStyle attribute == null, set to empty String");
          headerStyle = "";
        }
        headerStyle
            = headerStyle.replaceAll("height:\\s\\d+px;", "").trim();
        headerStyle += " height: " + headerSpace + "px;";
            bodyStyle
            = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE_BODY);
        if (bodyStyle == null) {
          LOG.warn("bodyStyle attribute == null, set to empty String");
          bodyStyle = "";
        }
        bodyStyle = bodyStyle.replaceAll("height:\\s\\d+px;", "").trim();
        bodyStyle += " height: " + bodySpace + "px;";
      }
      component.getAttributes().put(TobagoConstants.ATTR_STYLE_HEADER, headerStyle);
      component.getAttributes().put(TobagoConstants.ATTR_STYLE_BODY, bodyStyle);
    }
  }

  public static void createClassAttribute(UIComponent component, String name) {
    String rendererType = component.getRendererType();
    if (rendererType != null) {
      Object styleClassO = component.getAttributes().get(TobagoConstants.ATTR_STYLE_CLASS);
      if (styleClassO != null && LOG.isDebugEnabled()) {
        LOG.debug("styleClassO = '" + styleClassO.getClass().getName() + "'");
      }
      String styleClass
          = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE_CLASS);
      styleClass = updateClassAttribute(styleClass, name, component);
      component.getAttributes().put(TobagoConstants.ATTR_STYLE_CLASS, styleClass);
    }
  }

  public static String updateClassAttribute(
      String cssClass, String rendererName,
      UIComponent component) {
    if (cssClass != null) {
      // first remove old tobago-<rendererName>-<type> classes from class-attribute
      cssClass = cssClass.replaceAll(
          "tobago-" + rendererName
          + "-(default|disabled|readonly|inline|error)", "").trim();
      // remove old tobago-<rendererName>-markup-<type> classes from class-attribute
      cssClass = cssClass.replaceAll(
          "tobago-" + rendererName + "-markup-(strong|deleted)", "").trim();
    } else {
      cssClass = "";
    }
    String tobagoClass = "tobago-" + rendererName + "-default ";
    if (ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_DISABLED)) {
      tobagoClass += "tobago-" + rendererName + "-disabled ";
    }
    if (ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_READONLY)) {
      tobagoClass += "tobago-" + rendererName + "-readonly ";
    }
    if (ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_INLINE)) {
      tobagoClass += "tobago-" + rendererName + "-inline ";
    }
    if (ComponentUtil.isError(component)) {
      tobagoClass += "tobago-" + rendererName + "-error ";
    }
    String markup = ComponentUtil.getStringAttribute(component, TobagoConstants.ATTR_MARKUP);
    if (StringUtils.isNotEmpty(markup)) {
      if (markup.equals("strong") || markup.equals("deleted")) {
        tobagoClass += "tobago-" + rendererName + "-markup-" + markup + " ";
      } else {
        LOG.warn("Unknown markup='" + markup + "'");
      }
    }

    return tobagoClass + cssClass;
  }

  public static void writeJavascript(ResponseWriter writer, String script)
      throws IOException {
    startJavascript(writer);
    writer.writeText(script, null);
    endJavascript(writer);
  }

  public static void startJavascript(ResponseWriter writer) throws IOException {
    writer.startElement("script", null);
    writer.writeAttribute("type", "text/javascript", null);
    writer.writeText("\n<!--\n", null);
  }

  public static void endJavascript(ResponseWriter writer) throws IOException {
    writer.writeText("\n// -->\n", null);
    writer.endElement("script");
  }


  public static String addTip(String title, String tip) {
    if (tip != null) {
      if (title != null && title.length() > 0) {
        title += " :: ";
      }
      else {
        title = "";
      }
      title += tip;
    }
    return title;
  }
}

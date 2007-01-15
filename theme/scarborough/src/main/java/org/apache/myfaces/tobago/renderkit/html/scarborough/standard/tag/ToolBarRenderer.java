package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

/*
  * Created 28.04.2003 at 15:29:36.
  * $Id$
  */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MENU_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MENU_POPUP_TYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_MENUPOPUP;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_ITEMS;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_TOOL_BAR;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_BOX;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_MENUBAR;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UISelectOneCommand;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ToolBarRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(ToolBarRenderer.class);


  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    UIPanel toolbar = (UIPanel) uiComponent;

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();
    boolean suppressContainer = ComponentUtil.getBooleanAttribute(toolbar,
        ATTR_SUPPPRESS_TOOLBAR_CONTAINER);

    if (!suppressContainer) {
      setToolBarHeight(facesContext, uiComponent);

      writer.startElement(HtmlConstants.DIV, toolbar);
      writer.writeIdAttribute(toolbar.getClientId(facesContext));
      writer.writeComponentClass();
      writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
      writer.startElement(HtmlConstants.DIV, toolbar);
      boolean right = false;
      if (toolbar instanceof UIToolBar) {
        right = UIToolBar.ORIENTATION_RIGHT.equals(((UIToolBar) toolbar).getOrientation());
      }
      writer.writeClassAttribute("tobago-toolbar-div-inner" + (right ? " tobago-toolbar-orientation-right" : ""));

    }

    boolean boxFacet = isBoxFacet(toolbar);

    List children = toolbar.getChildren();

    int index = 0;
    for (Iterator iter = children.iterator(); iter.hasNext();) {
      UIComponent component = (UIComponent) iter.next();
      if (component instanceof UICommand) {
        boolean addExtraClass = boxFacet ? !iter.hasNext() : index++ == 0;
        renderToolbarCommand(facesContext, (UICommand) component, writer,
            boxFacet, addExtraClass);
      } else {
        LOG.error("Illegal UIComponent class in toolbar :"
            + component.getClass().getName());
      }
    }

    if (!suppressContainer) {
      writer.endElement(HtmlConstants.DIV);
      writer.endElement(HtmlConstants.DIV);
    }
  }

// ----------------------------------------------------------- business methods

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public boolean getRendersChildren() {
    return true;
  }

  private boolean isBoxFacet(UIComponent component) {
    return (RENDERER_TYPE_BOX.equals(component.getParent().getRendererType())
        && component.getParent().getFacet(FACET_TOOL_BAR) == component);
  }

  private void renderToolbarCommand(FacesContext facesContext,
      final UICommand command, TobagoResponseWriter writer, boolean boxFacet,
      boolean addExtraHoverClass)
      throws IOException {
    if (command instanceof UISelectBooleanCommand) {
      renderSelectBoolean(facesContext, command, writer, boxFacet, addExtraHoverClass);
    } else if (command instanceof UISelectOneCommand) {
      renderSelectOne(facesContext, command, writer, boxFacet, addExtraHoverClass);
    } else {
      if (command.getFacet(FACET_ITEMS) != null) {
        UIComponent facet = command.getFacet(FACET_ITEMS);
        if (facet instanceof UISelectBoolean) {
          renderSelectBoolean(facesContext, command, writer, boxFacet, addExtraHoverClass);
        } else if (facet instanceof UISelectOne) {
          renderSelectOne(facesContext, command, writer, boxFacet, addExtraHoverClass);
        }
      } else {
        String onClick = createOnClick(facesContext, command);
        renderToolbarButton(facesContext, command, writer, boxFacet, addExtraHoverClass, false, onClick);
      }
    }
  }

  private void renderSelectOne(FacesContext facesContext, UICommand command,
      TobagoResponseWriter writer, boolean boxFacet, boolean addExtraHoverClass)
      throws IOException {

    String onClick = createOnClick(facesContext, command);
    onClick = HtmlRendererUtil.appendConfirmationScript(onClick, command, facesContext);

    List<SelectItem> items; 

    UIMenuSelectOne radio = (UIMenuSelectOne) command.getFacet(FACET_ITEMS);
    if (radio == null) {
      items = ComponentUtil.getSelectItems(command);
      radio = ComponentUtil.createUIMenuSelectOneFacet(facesContext, command);
      radio.setId(facesContext.getViewRoot().createUniqueId());
    } else {
      items = ComponentUtil.getSelectItems(radio);
    }


    if (radio != null) {
      Object value = radio.getValue();

      boolean markFirst = !ComponentUtil.hasSelectedValue(items, value);
      String radioId = radio.getClientId(facesContext);
      String onClickPrefix = "menuSetRadioValue('" + radioId + "', '";
      String onClickPostfix = onClick != null ? "') ; " + onClick : "";
      for (SelectItem item : items) {
        final String labelText = item.getLabel();
        if (labelText != null) {
          command.getAttributes().put(ATTR_LABEL, labelText);
        } else {
          LOG.warn("Menu item has label=null. UICommand.getClientId()="
              + command.getClientId(facesContext));
        }

        String image = null;
        if (item instanceof org.apache.myfaces.tobago.model.SelectItem) {
          image = ((org.apache.myfaces.tobago.model.SelectItem) item).getImage();
        } else if (LOG.isDebugEnabled()) {
          LOG.debug("select item is not " + org.apache.myfaces.tobago.model.SelectItem.class.getName());
        }
        if (image == null) {
          image = "image/1x1.gif";
        }
        command.getAttributes().put(ATTR_IMAGE, image);

        if (item.getDescription() != null) {
          command.getAttributes().put(ATTR_TIP, item.getDescription());
        }


        String formattedValue
            = RenderUtil.getFormattedValue(facesContext, radio, item.getValue());
        onClick = onClickPrefix + formattedValue + onClickPostfix;
        final boolean checked;
        if (item.getValue().equals(value) || markFirst) {
          checked = true;
          markFirst = false;
          HtmlRendererUtil.startJavascript(writer);
          writer.write("    " + onClickPrefix + formattedValue + "');");
          HtmlRendererUtil.endJavascript(writer);
        } else {
          checked = false;
        }

        renderToolbarButton(facesContext, command, writer, boxFacet, addExtraHoverClass, checked, onClick);

      }
    }

  }

  private void renderSelectBoolean(FacesContext facesContext, UICommand command,
      TobagoResponseWriter writer, boolean boxFacet, boolean addExtraHoverClass)
      throws IOException {

    UIComponent checkbox = command.getFacet(FACET_ITEMS);
    if (checkbox == null) {
      checkbox = ComponentUtil.createUISelectBooleanFacet(facesContext, command);
      checkbox.setId(facesContext.getViewRoot().createUniqueId());
    }

    final boolean checked = ComponentUtil.getBooleanAttribute(checkbox, ATTR_VALUE);

    String onClick = createOnClick(facesContext, command);

    String clientId = checkbox.getClientId(facesContext);
    onClick = RenderUtil.addMenuCheckToggle(clientId, onClick);
    if (checked) {
      HtmlRendererUtil.startJavascript(writer);
      writer.write("    menuCheckToggle('" + clientId + "');\n");
      HtmlRendererUtil.endJavascript(writer);
    }

    renderToolbarButton(facesContext, command, writer, boxFacet, addExtraHoverClass, checked, onClick);
  }

  private void renderToolbarButton(FacesContext facesContext,
                                   final UICommand command, TobagoResponseWriter writer, boolean boxFacet,
                                   boolean addExtraHoverClass, boolean selected, String onClick)
      throws IOException {
    if (!command.isRendered()) {
      return;
    }

    final String clientId = command.getClientId(facesContext);
    final boolean disabled
        = ComponentUtil.getBooleanAttribute(command, ATTR_DISABLED);
    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    final UIComponent popupMenu = command.getFacet(FACET_MENUPOPUP);

    Map parentAttributes = command.getParent().getAttributes();
    String labelPosition = (String) parentAttributes.get(ATTR_LABEL_POSITION);
    String iconSize = (String) parentAttributes.get(ATTR_ICON_SIZE);

    onClick = HtmlRendererUtil.appendConfirmationScript(onClick, command,
        facesContext);

    String divClasses = "tobago-toolbar-button"
        + " tobago-toolbar-button-"  + (boxFacet ? "box-facet-" : "")
        + (selected ? "selected-" : "") + (disabled ? "disabled" : "enabled")
        + (boxFacet ? " tobago-toolbar-button-box-facet" : "");

    String tableClasses = "tobago-toolbar-button-table"
        + (boxFacet ? " tobago-toolbar-button-table-box-facet" : "")
        + " tobago-toolbar-button-table-" + (boxFacet ? "box-facet-" : "")
        + (selected ? "selected-" : "") + (disabled ? "disabled" : "enabled");


    String iconName = (String) command.getAttributes().get(ATTR_IMAGE);
    String image = getImage(facesContext, iconName, iconSize, disabled, selected);
    String graphicId = clientId + SUBCOMPONENT_SEP + "icon";

    String extraHoverClass = "";
    if (addExtraHoverClass) {
      if (!boxFacet) {
        extraHoverClass = " tobago-toolBar-button-hover-first";
      } else {
        extraHoverClass = " tobago-box-toolBar-button-hover-last";
      }
    }
    final String args = "this, 'tobago-toolBar-button-hover"
        + (boxFacet ? " tobago-toolBar-button-box-facet-hover" : "")
        + extraHoverClass + "', '" + graphicId + "'";
    final String mouseOverScript = "Tobago.toolbarMousesover(" + args + ");";
    final String mouseOutScript = "Tobago.toolbarMousesout(" + args + ");";

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeClassAttribute(divClasses);
    if (!disabled) {
      writer.writeAttribute(HtmlAttributes.ONMOUSEOVER, mouseOverScript, null);
      writer.writeAttribute(HtmlAttributes.ONMOUSEOUT, mouseOutScript, null);
      writer.writeAttribute(HtmlAttributes.ONCLICK, onClick, null);
    }
    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
    writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
    writer.writeClassAttribute(tableClasses);
    writer.startElement(HtmlConstants.TR, null);

    boolean anchorOnLabel =
        label.getText() != null && !ToolBarTag.LABEL_OFF.equals(labelPosition);

    if (!ToolBarTag.ICON_OFF.equals(iconSize)) {
      HtmlRendererUtil.addImageSources(facesContext, writer,
          iconName != null ? iconName : "image/1x1.gif", graphicId);

      writer.startElement(HtmlConstants.TD, command);
      writer.writeAttribute(HtmlAttributes.ALIGN, "center", null);
      writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);

      boolean render1pxImage
          = (iconName == null && (!ToolBarTag.LABEL_BOTTOM.equals(
              labelPosition)
          && label.getText() != null));


      if (((!ToolBarTag.LABEL_OFF.equals(labelPosition)
            && label.getText() != null)
           || popupMenu != null)
          && !render1pxImage) {
        writer.writeAttribute(HtmlAttributes.STYLE, "padding-right: 3px;", null);
        // TODO: make this '3px' configurable
      }

      String className = "tobago-image-default tobago-toolBar-button-image"
          + " tobago-toolBar-button-image-" + iconSize;

      if (!anchorOnLabel) {
        renderAnchorBegin(facesContext, writer, command, label, disabled);
      }
      writer.startElement(HtmlConstants.IMG, command);
      writer.writeIdAttribute(graphicId);
      writer.writeAttribute(HtmlAttributes.SRC, image, null);
      writer.writeAttribute(HtmlAttributes.ALT, "", null);
      writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
      writer.writeAttribute(HtmlAttributes.BORDER, "0", null);
      writer.writeClassAttribute(className);
      if (render1pxImage) {
        writer.writeAttribute(HtmlAttributes.STYLE, "width: 1px;", null);
      }

      writer.endElement(HtmlConstants.IMG);
      if (!anchorOnLabel) {
        writer.endElement(HtmlConstants.A);
      }
      writer.endElement(HtmlConstants.TD);
    }

    boolean popupOn2 = ToolBarTag.LABEL_BOTTOM.equals(labelPosition)
        && !ToolBarTag.ICON_OFF.equals(iconSize);
    if (popupOn2) {
      if (popupMenu != null) {
        renderPopupTd(facesContext, writer, command, popupMenu,
            true);
      }
      writer.endElement(HtmlConstants.TR);
      writer.startElement(HtmlConstants.TR, null);
    }

    if (!ToolBarTag.LABEL_OFF.equals(labelPosition)) {
      writer.startElement(HtmlConstants.TD, null);
      writer.writeClassAttribute("tobago-toolbar-label-td");
      writer.writeAttribute(HtmlAttributes.ALIGN, "center", null);
      if (popupMenu != null) {
        writer.writeAttribute(HtmlAttributes.STYLE, "padding-right: 3px;", null);
        // TODO: make this '3px' configurable
      }
      if (label.getText() != null) {
        renderAnchorBegin(facesContext, writer, command, label, disabled);
        HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
        writer.endElement(HtmlConstants.A);
      }
      writer.endElement(HtmlConstants.TD);
    }
    if (!popupOn2 && popupMenu != null) {
      renderPopupTd(facesContext, writer, command, popupMenu,
          false);
    }

    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);
    writer.endElement(HtmlConstants.DIV);
  }

  private String createOnClick(FacesContext facesContext,
      UIComponent component) {
    if (component.getFacet(FACET_MENUPOPUP) != null
        && ((UICommand) component).getAction() == null
        && ((UICommand) component).getActionListener() == null
        && ((UICommand) component).getActionListeners().length == 0) {
      String searchId = component.getClientId(facesContext)
          + MenuBarRenderer.SEARCH_ID_POSTFIX;
      return "tobagoButtonOpenMenu(this, '" + searchId + "')";
    } else {
      return HtmlRendererUtil.createOnClick(facesContext, component);
    }
  }

  private String getImage(FacesContext facesContext, String name,
                          String iconSize, boolean disabled, boolean selected) {
    if (name == null) {
      return ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");
    }
    int pos = name.lastIndexOf('.');
    if (pos == -1) {
      pos = name.length(); // avoid exception if no '.' in name
    }
    String key = name.substring(0, pos);
    String ext = name.substring(pos);

    String size = "";
    if (ToolBarTag.ICON_SMALL.equals(iconSize)) {
      size = "16";
    } else if (ToolBarTag.ICON_BIG.equals(iconSize)) {
      size = "32";
    }
    String image = null;
    ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    UIViewRoot viewRoot = facesContext.getViewRoot();
    if (disabled && selected) {
      image = resourceManager.getImage(
          viewRoot, key + "SelectedDisabled" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(
                viewRoot, key + "SelectedDisabled" + ext, true);
      }
    }
    if (image == null && disabled) {
      image = resourceManager.getImage(
          viewRoot, key + "Disabled" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(
            viewRoot, key + "Disabled" + ext, true);
      }
    }
    if (image == null && selected) {
      image = resourceManager.getImage(
          viewRoot, key + "Selected" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(
            viewRoot, key + "Selected" + ext, true);
      }
    }
    if (image == null) {
      image
          = resourceManager.getImage(viewRoot, key + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(viewRoot, key + ext, true);
      }
    }
    String contextPath = facesContext.getExternalContext().getRequestContextPath();
//    LOG.info("getImage for " + name + ", " + iconSize + ", " + disabled + ", " + selected + " = " + image);
    return contextPath + image;
  }

  private void renderAnchorBegin(FacesContext facesContext,
      TobagoResponseWriter writer, final UICommand command,
      final LabelWithAccessKey label, final boolean disabled)
      throws IOException {
    writer.startElement(HtmlConstants.A, command);
    writer.writeClassAttribute("tobago-toolBar-button-link" + (disabled ? "tobago-toolBar-button-link-disabled" : ""));
    writer.writeAttribute(HtmlAttributes.TITLE, null, ATTR_TIP);
    if (!disabled) {
      writer.writeAttribute(HtmlAttributes.HREF, "#", null);
      writer.writeAttribute(HtmlAttributes.ONFOCUS, "Tobago.toolbarFocus(this, event)", null);
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
                && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("dublicated accessKey : " + label.getAccessKey());
        }
        String id = command.getClientId(facesContext) + SUBCOMPONENT_SEP + "link";
        writer.writeIdAttribute(id);
      HtmlRendererUtil.addClickAcceleratorKey(
          facesContext, id, label.getAccessKey());
      }
    }
  }

  private void renderPopupTd(FacesContext facesContext,
      TobagoResponseWriter writer, UIComponent command, UIComponent popupMenu,
      boolean labelBottom)
      throws IOException {
    writer.startElement(HtmlConstants.TD, null);
    if (labelBottom) {
      writer.writeAttribute(HtmlAttributes.ROWSPAN, "2", null);
    }

    if (popupMenu != null) {
      String backgroundImage = ResourceManagerUtil.getImageWithPath(facesContext,
          "image/1x1.gif");
      writer.startElement(HtmlConstants.DIV, null);
      writer.writeIdAttribute(
          command.getClientId(facesContext) + SUBCOMPONENT_SEP + "popup");
      writer.writeClassAttribute("tobago-toolBar-button-menu");
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, backgroundImage, null);
      writer.writeClassAttribute("tobago-toolBar-button-menu-background-image");
      writer.endElement(HtmlConstants.IMG);
      writer.endElement(HtmlConstants.DIV);
      popupMenu.getAttributes().put(ATTR_MENU_POPUP, Boolean.TRUE);
      popupMenu.getAttributes().put(ATTR_MENU_POPUP_TYPE, "ToolBarButton");
      popupMenu.setRendererType(RENDERER_TYPE_MENUBAR);
      popupMenu.getAttributes().remove(ATTR_LABEL);
      popupMenu.getAttributes().put(ATTR_IMAGE, "image/toolbarButtonMenu.gif");
      RenderUtil.encode(facesContext, popupMenu);
    }

    writer.endElement(HtmlConstants.TD);
  }

  private void setToolBarHeight(FacesContext facesContext,
      UIComponent component) {
    final int height = getFixedHeight(facesContext, component);
    HtmlRendererUtil.replaceStyleAttribute(component, "height", height);
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    final Map attributes = component.getAttributes();
    final String labelPosition = (String) attributes.get(ATTR_LABEL_POSITION);
    final String iconSize = (String) attributes.get(ATTR_ICON_SIZE);

    final String key = iconSize + "_" + labelPosition + "_Height";
    return getConfiguredValue(facesContext, component, key);
  }
}

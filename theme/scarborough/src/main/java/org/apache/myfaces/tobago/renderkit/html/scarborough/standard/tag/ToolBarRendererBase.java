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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MENU_POPUP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MENU_POPUP_TYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_ITEMS;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_MENUPOPUP;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_MENUBAR;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UISelectOneCommand;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class ToolBarRendererBase extends LayoutableRendererBase {

  protected String getLabelPosition(UIComponent component) {
    return (String) component.getAttributes().get(ATTR_LABEL_POSITION);
  }

  protected String getIconSize(UIComponent component) {
    return (String) component.getAttributes().get(ATTR_ICON_SIZE);
  }

  public void encodeEnd(FacesContext context, UIComponent uiComponent) throws IOException {
    UIPanel toolbar = (UIPanel) uiComponent;

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(context);
    List children = toolbar.getChildren();

    boolean first = true;
    for (Iterator iter = children.iterator(); iter.hasNext();) {
      UIComponent component = (UIComponent) iter.next();
      if (component instanceof UICommand) {
        boolean last = !iter.hasNext();
        renderToolbarCommand(context, (UICommand) component, writer, first, last);
        first = false;
      } else {
        LOG.error("Illegal UIComponent class in toolbar :" + component.getClass().getName());
      }
    }
  }

  private void renderToolbarCommand(FacesContext facesContext,
      final UICommand command, TobagoResponseWriter writer, boolean first, boolean last)
      throws IOException {
    if (command instanceof UISelectBooleanCommand) {
      renderSelectBoolean(facesContext, command, writer, first, last);
    } else if (command instanceof UISelectOneCommand) {
      renderSelectOne(facesContext, command, writer, first, last);
    } else {
      if (command.getFacet(FACET_ITEMS) != null) {
        UIComponent facet = command.getFacet(FACET_ITEMS);
        if (facet instanceof UISelectBoolean) {
          renderSelectBoolean(facesContext, command, writer, first, last);
        } else if (facet instanceof UISelectOne) {
          renderSelectOne(facesContext, command, writer, first, last);
        }
      } else {
        String onClick = createOnClick(facesContext, command);
        renderToolbarButton(facesContext, command, writer, first, last, false, onClick);
      }
    }
  }

  private void renderSelectOne(FacesContext facesContext, UICommand command,
      TobagoResponseWriter writer, boolean first, boolean last)
      throws IOException {

    String onclick = createOnClick(facesContext, command);

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
      String onClickPostfix = onclick != null ? "') ; " + onclick : "";
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
        onclick = onClickPrefix + formattedValue + onClickPostfix;
        final boolean checked;
        if (item.getValue().equals(value) || markFirst) {
          checked = true;
          markFirst = false;
          writer.writeJavascript("    " + onClickPrefix + formattedValue + "');");
        } else {
          checked = false;
        }

        renderToolbarButton(facesContext, command, writer, first, last, checked, onclick);

      }
    }

  }

  private void renderSelectBoolean(FacesContext facesContext, UICommand command,
      TobagoResponseWriter writer, boolean first, boolean last)
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
      writer.writeJavascript("    menuCheckToggle('" + clientId + "');\n");
    }

    renderToolbarButton(facesContext, command, writer, first, last, checked, onClick);
  }

  private void renderToolbarButton(
      FacesContext facesContext, final UICommand command, TobagoResponseWriter writer,
      boolean first, boolean last, boolean selected, String onClick)
      throws IOException {
    if (!command.isRendered()) {
      return;
    }

    final String clientId = command.getClientId(facesContext);
    final boolean disabled = ComponentUtil.getBooleanAttribute(command, ATTR_DISABLED);
    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    final UIComponent popupMenu = command.getFacet(FACET_MENUPOPUP);

    String labelPosition = getLabelPosition(command.getParent());
    String iconSize = getIconSize(command.getParent());


    String iconName = (String) command.getAttributes().get(ATTR_IMAGE);
    String image = getImage(facesContext, iconName, iconSize, disabled, selected);
    String graphicId = clientId + SUBCOMPONENT_SEP + "icon";

    final String hover = getHoverClasses(first, last);
    final String mouseOverScript = "Tobago.toolbarMousesover(this, '" + hover + "', '" + graphicId + "');";
    final String mouseOutScript = "Tobago.toolbarMousesout(this, '" + hover + "', '" + graphicId + "');";

    writer.startElement(HtmlConstants.DIV, null);
    writer.writeIdAttribute(command.getClientId(facesContext));
    String divClasses = getDivClasses(selected, disabled);

    writer.writeClassAttribute(divClasses);
    if (!disabled) {
      writer.writeAttribute(HtmlAttributes.ONMOUSEOVER, mouseOverScript, null);
      writer.writeAttribute(HtmlAttributes.ONMOUSEOUT, mouseOutScript, null);
      writer.writeAttribute(HtmlAttributes.ONCLICK, onClick, null);
    }
    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 0);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    writer.writeAttribute(HtmlAttributes.BORDER, 0);
    String tableClasses = getTableClasses(selected, disabled);
    writer.writeClassAttribute(tableClasses);
    writer.startElement(HtmlConstants.TR, null);

    boolean anchorOnLabel = label.getText() != null && !ToolBarTag.LABEL_OFF.equals(labelPosition);

    if (!ToolBarTag.ICON_OFF.equals(iconSize)) {
      HtmlRendererUtil.addImageSources(facesContext, writer,
          iconName != null ? iconName : "image/1x1.gif", graphicId);

      writer.startElement(HtmlConstants.TD, command);
      writer.writeAttribute(HtmlAttributes.ALIGN, "center", false);
      HtmlRendererUtil.renderTip(command, writer);

      boolean render1pxImage = (iconName == null
          && (!ToolBarTag.LABEL_BOTTOM.equals(labelPosition)
          && label.getText() != null));

      if (((!ToolBarTag.LABEL_OFF.equals(labelPosition)
          && label.getText() != null)
          || popupMenu != null)
          && !render1pxImage) {
        writer.writeStyleAttribute("padding-right: 3px;");
        // TODO: make this '3px' configurable
      }

      String className = getIconClass(iconSize);

      if (!anchorOnLabel) {
        renderAnchorBegin(facesContext, writer, command, label, disabled);
      }
      writer.startElement(HtmlConstants.IMG, command);
      writer.writeIdAttribute(graphicId);
      writer.writeAttribute(HtmlAttributes.SRC, image, false);
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      HtmlRendererUtil.renderTip(command, writer);
      writer.writeAttribute(HtmlAttributes.BORDER, 0);
      writer.writeClassAttribute(className);
      if (render1pxImage) {
        writer.writeStyleAttribute("width: 1px;");
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
      writer.writeAttribute(HtmlAttributes.ALIGN, "center", false);
      if (popupMenu != null) {
        writer.writeAttribute(HtmlAttributes.STYLE, "padding-right: 3px;", false);
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

  protected String getIconClass(String iconSize) {
    return "tobago-image-default tobago-toolBar-button-image tobago-toolBar-button-image-" + iconSize;
  }

  protected abstract String getHoverClasses(boolean first, boolean last);

  protected abstract String getTableClasses(boolean selected, boolean disabled);

  protected abstract String getDivClasses(boolean selected, boolean disabled);

  private String createOnClick(FacesContext facesContext, UIComponent component) {
    if (component.getFacet(FACET_MENUPOPUP) != null
        && ((UICommand) component).getAction() == null
        && ((UICommand) component).getActionListener() == null
        && ((UICommand) component).getActionListeners().length == 0) {
      String searchId = component.getClientId(facesContext)
          + MenuBarRenderer.SEARCH_ID_POSTFIX;
      return "tobagoButtonOpenMenu(this, '" + searchId + "')";
    } else {
      CommandRendererHelper helper
          = new CommandRendererHelper(facesContext, (UICommand) component);
      return helper.getOnclick();
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

  private void renderAnchorBegin(
      FacesContext facesContext, TobagoResponseWriter writer, final UICommand command,
      final LabelWithAccessKey label, final boolean disabled)
      throws IOException {
    writer.startElement(HtmlConstants.A, command);
    // TODO use StyleClasses
    writer.writeClassAttribute(getAnchorClass(disabled));
    HtmlRendererUtil.renderTip(command, writer);
    if (!disabled) {
      writer.writeAttribute(HtmlAttributes.HREF, "#", false);
      writer.writeAttribute(HtmlAttributes.ONFOCUS, "Tobago.toolbarFocus(this, event)", false);
      String id = command.getClientId(facesContext) + SUBCOMPONENT_SEP + "link";
      writer.writeIdAttribute(id);
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("dublicated accessKey : " + label.getAccessKey());
        }
        HtmlRendererUtil.addClickAcceleratorKey(
            facesContext, id, label.getAccessKey());
      }
    }
  }

  protected String getAnchorClass(boolean disabled) {
    return "tobago-toolBar-button-link" + (disabled ? " tobago-toolBar-button-link-disabled" : "");
  }

  private void renderPopupTd(FacesContext facesContext,
      TobagoResponseWriter writer, UIComponent command, UIComponent popupMenu,
      boolean labelBottom)
      throws IOException {
    writer.startElement(HtmlConstants.TD, null);
    if (labelBottom) {
      writer.writeAttribute(HtmlAttributes.ROWSPAN, 2);
    }

    if (popupMenu != null) {
      String backgroundImage = ResourceManagerUtil.getImageWithPath(facesContext,
          "image/1x1.gif");
      writer.startElement(HtmlConstants.DIV, null);
      writer.writeIdAttribute(
          command.getClientId(facesContext) + SUBCOMPONENT_SEP + "popup");
      writer.writeClassAttribute("tobago-toolBar-button-menu");
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, backgroundImage, false);
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

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    final Map attributes = component.getAttributes();
    final String labelPosition = getLabelPosition(component);
    final String iconSize = getIconSize(component);
    final String key = iconSize + "_" + labelPosition + "_Height";
    return getConfiguredValue(facesContext, component, key);
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public boolean getRendersChildren() {
    return true;
  }

}

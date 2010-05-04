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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.CreateComponentUtils;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIMenu;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UISelectOneCommand;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.UICommandBase;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public abstract class ToolBarRendererBase extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ToolBarRendererBase.class);

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    HtmlRendererUtils.renderDojoDndSource(facesContext, component);
  }

  protected String getLabelPosition(UIComponent component) {
    return (String) component.getAttributes().get(Attributes.LABEL_POSITION);
  }

  protected String getIconSize(UIComponent component) {
    return (String) component.getAttributes().get(Attributes.ICON_SIZE);
  }

  @Override
  public void encodeEnd(FacesContext context, UIComponent uiComponent) throws IOException {
    UIPanel toolbar = (UIPanel) uiComponent;

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(context);
    List children = toolbar.getChildren();

    boolean first = true;
    for (Iterator iter = children.iterator(); iter.hasNext();) {
      UIComponent component = (UIComponent) iter.next();
      if (component instanceof UICommandBase) {
        boolean last = !iter.hasNext();
        renderToolbarCommand(context, (UICommandBase) component, writer, first, last);
        first = false;
      } else {
        LOG.error("Illegal UIComponent class in toolbar (not UICommandBase):" + component.getClass().getName());
      }
    }
  }

  private void renderToolbarCommand(FacesContext facesContext,
      final UICommandBase command, TobagoResponseWriter writer, boolean first, boolean last)
      throws IOException {
    if (command instanceof UISelectBooleanCommand) {
      renderSelectBoolean(facesContext, command, writer, first, last);
    } else if (command instanceof UISelectOneCommand) {
      renderSelectOne(facesContext, command, writer, first, last);
    } else {
      if (command.getFacet(Facets.RADIO) != null) {
        renderSelectOne(facesContext, command, writer, first, last);
      } else if (command.getFacet(Facets.CHECKBOX) != null) {
        renderSelectBoolean(facesContext, command, writer, first, last);
      } else {
        String onClick = createOnClick(facesContext, command);
        renderToolbarButton(facesContext, command, writer, first, last, false, onClick);
      }
    }
  }

  private void renderSelectOne(FacesContext facesContext, UICommandBase command,
      TobagoResponseWriter writer, boolean first, boolean last)
      throws IOException {

    String onclick = createOnClick(facesContext, command);

    List<SelectItem> items;

    UIMenuSelectOne radio = (UIMenuSelectOne) command.getFacet(Facets.RADIO);
    if (radio == null) {
      items = RenderUtils.getSelectItems(command);
      radio = CreateComponentUtils.createUIMenuSelectOneFacet(facesContext, command);
      radio.setId(facesContext.getViewRoot().createUniqueId());
    } else {
      items = RenderUtils.getSelectItems(radio);
    }


    if (radio != null) {
      Object value = radio.getValue();

      boolean markFirst = !ComponentUtils.hasSelectedValue(items, value);
      String radioId = radio.getClientId(facesContext);
      String onClickPrefix = "menuSetRadioValue('" + radioId + "', '";
      String onClickPostfix = onclick != null ? "') ; " + onclick : "";
      for (SelectItem item : items) {
        final String labelText = item.getLabel();
        if (labelText != null) {
          command.getAttributes().put(Attributes.LABEL, labelText);
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
        command.getAttributes().put(Attributes.IMAGE, image);

        if (item.getDescription() != null) {
          command.getAttributes().put(Attributes.TIP, item.getDescription());
        }


        String formattedValue
            = RenderUtils.getFormattedValue(facesContext, radio, item.getValue());
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

  private void renderSelectBoolean(FacesContext facesContext, UICommandBase command,
      TobagoResponseWriter writer, boolean first, boolean last)
      throws IOException {

    UIComponent checkbox = command.getFacet(Facets.CHECKBOX);
    if (checkbox == null) {
      checkbox = CreateComponentUtils.createUISelectBooleanFacetWithId(facesContext, command);
    }

    final boolean checked = ComponentUtils.getBooleanAttribute(checkbox, Attributes.VALUE);

    String onClick = createOnClick(facesContext, command);

    String clientId = checkbox.getClientId(facesContext);
    onClick = RenderUtils.addMenuCheckToggle(clientId, onClick);
    if (checked) {
      writer.writeJavascript("    menuCheckToggle('" + clientId + "');\n");
    }

    renderToolbarButton(facesContext, command, writer, first, last, checked, onClick);
  }

  private void renderToolbarButton(
      FacesContext facesContext, UICommandBase command, TobagoResponseWriter writer,
      boolean first, boolean last, boolean selected, String onClick)
      throws IOException {
    if (!command.isRendered()) {
      return;
    }

    final String clientId = command.getClientId(facesContext);
    final boolean disabled = ComponentUtils.getBooleanAttribute(command, Attributes.DISABLED);
    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    final UIComponent popupMenu = command.getFacet(Facets.MENUPOPUP);

    String labelPosition = getLabelPosition(command.getParent());
    String iconSize = getIconSize(command.getParent());


    String iconName = (String) command.getAttributes().get(Attributes.IMAGE);
    String image = getImage(facesContext, iconName, iconSize, disabled, selected);
    String graphicId = clientId + ComponentUtils.SUB_SEPARATOR + "icon";

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

    boolean anchorOnLabel = label.getText() != null && !UIToolBar.LABEL_OFF.equals(labelPosition);

    if (!UIToolBar.ICON_OFF.equals(iconSize)) {
      HtmlRendererUtils.addImageSources(facesContext, writer,
          iconName != null ? iconName : "image/1x1.gif", graphicId);

      writer.startElement(HtmlConstants.TD, command);
      writer.writeAttribute(HtmlAttributes.ALIGN, "center", false);
      HtmlRendererUtils.renderTip(command, writer);

      boolean render1pxImage = (iconName == null
          && (!UIToolBar.LABEL_BOTTOM.equals(labelPosition)
          && label.getText() != null));

      if (((!UIToolBar.LABEL_OFF.equals(labelPosition)
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
      HtmlRendererUtils.renderTip(command, writer);
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

    boolean popupOn2 = UIToolBar.LABEL_BOTTOM.equals(labelPosition)
        && !UIToolBar.ICON_OFF.equals(iconSize);
    if (popupOn2) {
      if (popupMenu != null) {
        renderPopupTd(facesContext, writer, command, popupMenu, true);
      }
      writer.endElement(HtmlConstants.TR);
      writer.startElement(HtmlConstants.TR, null);
    }

    if (!UIToolBar.LABEL_OFF.equals(labelPosition)) {
      writer.startElement(HtmlConstants.TD, null);
      writer.writeClassAttribute("tobago-toolBar-label-td");
      writer.writeAttribute(HtmlAttributes.ALIGN, "center", false);
      if (popupMenu != null) {
        writer.writeAttribute(HtmlAttributes.STYLE, "padding-right: 3px;", false);
        // TODO: make this '3px' configurable
      }
      if (label.getText() != null) {
        renderAnchorBegin(facesContext, writer, command, label, disabled);
        HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
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
    return "tobago-image tobago-toolBar-button-image tobago-toolBar-button-image-" + iconSize;
  }

  protected abstract String getHoverClasses(boolean first, boolean last);

  protected abstract String getTableClasses(boolean selected, boolean disabled);

  protected abstract String getDivClasses(boolean selected, boolean disabled);

  private String createOnClick(FacesContext facesContext, UICommandBase command) {
    if (command.getFacet(Facets.MENUPOPUP) != null
        && command.getAction() == null
        && command.getActionListener() == null
        && command.getActionListeners().length == 0) {
      String searchId = command.getClientId(facesContext) + MenuBarRenderer.SEARCH_ID_POSTFIX;
      return "tobagoButtonOpenMenu(this, '" + searchId + "')";
    } else {
      CommandRendererHelper helper = new CommandRendererHelper(facesContext, command);
      return helper.getOnclick();
    }
  }

  private String getImage(FacesContext facesContext, String name,
      String iconSize, boolean disabled, boolean selected) {
    if (name == null) {
      return ResourceManagerUtils.getImageWithPath(facesContext, "image/1x1.gif");
    }
    int pos = name.lastIndexOf('.');
    if (pos == -1) {
      pos = name.length(); // avoid exception if no '.' in name
    }
    String key = name.substring(0, pos);
    String ext = name.substring(pos);

    String size = "";
    if (UIToolBar.ICON_SMALL.equals(iconSize)) {
      size = "16";
    } else if (UIToolBar.ICON_BIG.equals(iconSize)) {
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
      FacesContext facesContext, TobagoResponseWriter writer, UICommandBase command,
      final LabelWithAccessKey label, final boolean disabled)
      throws IOException {
    writer.startElement(HtmlConstants.A, command);
    // TODO use StyleClasses
    writer.writeClassAttribute(getAnchorClass(disabled));
    HtmlRendererUtils.renderTip(command, writer);
    if (!disabled) {
      writer.writeAttribute(HtmlAttributes.HREF, "#", false);
      writer.writeAttribute(HtmlAttributes.ONFOCUS, "Tobago.toolbarFocus(this, event)", false);
      String id = command.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "link";
      writer.writeIdAttribute(id);
      if (label.getAccessKey() != null) {
        if (LOG.isInfoEnabled()
            && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
          LOG.info("dublicated accessKey : " + label.getAccessKey());
        }
        HtmlRendererUtils.addClickAcceleratorKey(
            facesContext, id, label.getAccessKey());
      }
    }
  }

  protected String getAnchorClass(boolean disabled) {
    return "tobago-toolBar-button-link" + (disabled ? " tobago-toolBar-button-link-disabled" : "");
  }

  private void renderPopupTd(
      FacesContext facesContext, TobagoResponseWriter writer, UIComponent command, UIComponent popupMenu,
      boolean labelBottom) throws IOException {
    writer.startElement(HtmlConstants.TD, null);
    if (labelBottom) {
      writer.writeAttribute(HtmlAttributes.ROWSPAN, 2);
    }

    if (popupMenu != null) {
      String backgroundImage = ResourceManagerUtils.getImageWithPath(facesContext, "image/1x1.gif");
      writer.startElement(HtmlConstants.DIV, null);
      writer.writeIdAttribute(command.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "popup");
      writer.writeClassAttribute("tobago-toolBar-button-menu");
      writer.startElement(HtmlConstants.IMG, null);
      writer.writeAttribute(HtmlAttributes.SRC, backgroundImage, false);
      writer.writeClassAttribute("tobago-toolBar-button-menu-background-image");
      writer.endElement(HtmlConstants.IMG);
      writer.endElement(HtmlConstants.DIV);
      if (popupMenu instanceof UIMenu)  {
        ((UIMenu) popupMenu).setLabel(null);
      } else {
        popupMenu.getAttributes().remove(Attributes.LABEL);
      }
      String image = ResourceManagerUtils.getImageWithPath(facesContext, "image/toolbarButtonMenu.gif");
      popupMenu.getAttributes().put(Attributes.IMAGE, image);
      popupMenu.getAttributes().put(Attributes.LABEL, "\u00a0\u00a0"); // non breaking space
      writer.startElement(HtmlConstants.OL, popupMenu);
      writer.writeClassAttribute("tobago-menuBar");
      writer.writeStyleAttribute("position:relative;");  // FIXME: use a different style class
      RenderUtils.encode(facesContext, popupMenu);
      writer.endElement(HtmlConstants.OL);
    }

    writer.endElement(HtmlConstants.TD);
  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

}

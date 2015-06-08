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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.SelectBooleanCommand;
import org.apache.myfaces.tobago.component.SelectOneCommand;
import org.apache.myfaces.tobago.component.SupportsAccessKey;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.component.UIToolBarSeparator;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.component.AbstractUIMenu;
import org.apache.myfaces.tobago.internal.util.ObjectUtils;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.util.FacetUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.List;

public abstract class ToolBarRendererBase extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ToolBarRendererBase.class);

  @Override
  public void prepareRender(
      final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    final UIToolBar toolBar = (UIToolBar) component;

    if ("big".equals(getIconSize(toolBar))) {
      ComponentUtils.addCurrentMarkup(toolBar, Markup.BIG);
    }
    if ("right".equals(getLabelPosition(toolBar))) {
      ComponentUtils.addCurrentMarkup(toolBar, Markup.RIGHT);
    }
  }

  protected String getLabelPosition(final UIComponent component) {
    return (String) component.getAttributes().get(Attributes.LABEL_POSITION);
  }

  // XXX remove it, after removing subclasses
  protected String getIconSize(final UIComponent component) {
    return (String) component.getAttributes().get(Attributes.ICON_SIZE);
  }

  protected boolean isRightAligned(final UIToolBar toolBar) {
    return UIToolBar.ORIENTATION_RIGHT.equals(toolBar.getOrientation());
  }

  @Override
  public void encodeEnd(final FacesContext context, final UIComponent component) throws IOException {
    final UIToolBar toolBar = (UIToolBar) component;

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(context);

    for (final UIComponent command : toolBar.getChildren()) {
      if (command instanceof AbstractUICommand) {
        renderToolbarCommand(context, toolBar, (AbstractUICommand) command, writer);
      } else if (command instanceof UIToolBarSeparator) {
        renderSeparator(context, toolBar, (UIToolBarSeparator) command, writer);
      } else {
        LOG.error("Illegal UIComponent class in toolbar (not a AbstractUICommand):" + command.getClass().getName());
      }
    }
  }

  private void renderToolbarCommand(
      final FacesContext facesContext, final UIToolBar toolBar, final AbstractUICommand command,
      final TobagoResponseWriter writer) throws IOException {
    if (command instanceof SelectBooleanCommand) {
      renderSelectBoolean(facesContext, toolBar, command, writer);
    } else if (command instanceof SelectOneCommand) {
      renderSelectOne(facesContext, toolBar, command, writer);
    } else {
      if (command.getFacet(Facets.RADIO) != null) {
        renderSelectOne(facesContext, toolBar, command, writer);
      } else if (command.getFacet(Facets.CHECKBOX) != null) {
        renderSelectBoolean(facesContext, toolBar, command, writer);
      } else {
        final CommandMap map = new CommandMap(new Command(facesContext, command));
        renderToolbarButton(
            facesContext, toolBar, command, writer, false, map, null);
      }
    }
  }

  // todo: remove component creation in renderer, for JSF 2.0
  // todo: One solution is to make <tx:toolBarSelectOne> instead of <tc:toolBarSelectOne>
  private void renderSelectOne(
      final FacesContext facesContext, final UIToolBar toolBar, final AbstractUICommand command,
      final TobagoResponseWriter writer) throws IOException {

    final List<SelectItem> items;

    UIMenuSelectOne radio = (UIMenuSelectOne) command.getFacet(Facets.RADIO);
    if (radio == null) {
      items = SelectItemUtils.getItemList(facesContext, command);
      radio = CreateComponentUtils.createUIMenuSelectOneFacet(facesContext, command);
      radio.setId(facesContext.getViewRoot().createUniqueId());
    } else {
      items = SelectItemUtils.getItemList(facesContext, radio);
    }

    if (radio != null) {
      writer.startElement(HtmlElements.SPAN, radio);
      writer.writeClassAttribute(Classes.create(toolBar, "selectOne"));
      final Object value = radio.getValue();

      String currentValue = "";
      boolean markFirst = !hasSelectedValue(items, value);
      final String radioId = radio.getClientId(facesContext);
      for (final SelectItem item : items) {
        final String labelText = item.getLabel();
        if (labelText != null) {
          command.getAttributes().put(Attributes.LABEL, labelText);
        } else {
          LOG.warn("Menu item has label=null. UICommand.getClientId()=" + command.getClientId(facesContext));
        }

        String image = null;
        if (item instanceof org.apache.myfaces.tobago.model.SelectItem) {
          image = ((org.apache.myfaces.tobago.model.SelectItem) item).getImage();
        }
        if (image == null) {
          image = "image/1x1";
        }
        command.getAttributes().put(Attributes.IMAGE, image);

        if (item.getDescription() != null) {
          command.getAttributes().put(Attributes.TIP, item.getDescription());
        }

        final String formattedValue = RenderUtils.getFormattedValue(facesContext, radio, item.getValue());
        final boolean checked;
        if (ObjectUtils.equals(item.getValue(), value) || markFirst) {
          checked = true;
          markFirst = false;
          currentValue = formattedValue;
        } else {
          checked = false;
        }

        final CommandMap map = new CommandMap(new Command());
        renderToolbarButton(
            facesContext, toolBar, command, writer, checked, map, formattedValue);
      }

      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.writeNameAttribute(radioId);
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
      writer.endElement(HtmlElements.INPUT);
      writer.endElement(HtmlElements.SPAN);
    }
  }

  // todo: remove component creation in renderer, for JSF 2.0
  // todo: One solution is to make <tx:toolBarCheck> instead of <tc:toolBarCheck>
  // may be renamed to toolBarSelectBoolean?
  private void renderSelectBoolean(
      final FacesContext facesContext, final UIToolBar toolBar, final AbstractUICommand command,
      final TobagoResponseWriter writer) throws IOException {

    UIComponent checkbox = command.getFacet(Facets.CHECKBOX);
    if (checkbox == null) {
      checkbox = CreateComponentUtils.createUISelectBooleanFacetWithId(facesContext, command);
    }

    final boolean checked = ComponentUtils.getBooleanAttribute(checkbox, Attributes.VALUE);
    final String clientId = checkbox.getClientId(facesContext);

    writer.startElement(HtmlElements.SPAN, checkbox);
    writer.writeClassAttribute(Classes.create(toolBar, "selectBoolean"));
    final CommandMap map = new CommandMap(new Command());
    renderToolbarButton(facesContext, toolBar, command, writer, checked, map, null);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, Boolean.toString(checked), false);
    writer.endElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.SPAN);
  }

  private void renderToolbarButton(
      final FacesContext facesContext, final UIToolBar toolBar, final AbstractUICommand command,
      final TobagoResponseWriter writer,
      final boolean selected, final CommandMap map, final String value)
      throws IOException {
    if (!command.isRendered()) {
      return;
    }

    final boolean disabled = ComponentUtils.getBooleanAttribute(command, Attributes.DISABLED);
    final LabelWithAccessKey label = command instanceof SupportsAccessKey
        ? new LabelWithAccessKey((SupportsAccessKey) command)
        : new LabelWithAccessKey(command.getLabel());
    final AbstractUIMenu dropDownMenu = FacetUtils.getDropDownMenu(command);
//    final ResourceManager resources = getResourceManager();

    final String labelPosition = getLabelPosition(command.getParent());
    final String iconSize = getIconSize(command.getParent());
    String iconName = (String) command.getAttributes().get(Attributes.IMAGE);
    if (iconName == null) {
      iconName = "image/blank";
    }
    final String image = getImage(facesContext, iconName, iconSize, disabled, selected);

    final boolean showIcon = !UIToolBar.ICON_OFF.equals(iconSize);
    final boolean iconBig = UIToolBar.ICON_BIG.equals(iconSize);

    final boolean showLabelBottom = UIToolBar.LABEL_BOTTOM.equals(labelPosition);
    final boolean showLabelRight = UIToolBar.LABEL_RIGHT.equals(labelPosition);
    final boolean showLabel = showLabelBottom || showLabelRight;
    final boolean showDropDownMenu = dropDownMenu != null && dropDownMenu.isRendered();
    // two separate buttons for the command and the sub menu
    final boolean separateButtons = hasAnyCommand(command) && showDropDownMenu;

    // start rendering
    writer.startElement(HtmlElements.SPAN, command);
    Markup itemMarkup = Markup.NULL;
    if (selected) {
      itemMarkup = itemMarkup.add(Markup.SELECTED);
    }
    if (disabled) {
      itemMarkup = itemMarkup.add(Markup.DISABLED);
    }
    writer.writeClassAttribute(Classes.create(toolBar, "item", itemMarkup));
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, command);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    writer.startElement(HtmlElements.SPAN, command);
    if (separateButtons || !showDropDownMenu) {
      writer.writeClassAttribute(Classes.create(toolBar, "button", selected ? Markup.SELECTED : Markup.NULL));
    } else {
      writer.writeClassAttribute(Classes.create(toolBar, "menu"));
    }
    if (!toolBar.isTransient()) {
      writer.writeIdAttribute(command.getClientId(facesContext));
    }
    if (map != null) {
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(map), true);
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, command);
    if (value != null) {
      writer.writeAttribute(DataAttributes.VALUE, value, true);
    }

    // render icon
    if (showIcon) {
      writer.startElement(HtmlElements.IMG, command);
      if (iconBig) {
        writer.writeClassAttribute(Classes.create(toolBar, "image32"));
      } else {
        writer.writeClassAttribute(Classes.create(toolBar, "image16"));
      }
      writer.writeAttribute(HtmlAttributes.SRC, image, false);
      writer.writeAttribute(HtmlAttributes.ALT, label.getLabel(), true);
      writer.endElement(HtmlElements.IMG);
    }
    // render label
    if (showLabel) {
      writer.startElement(HtmlElements.SPAN, command);
      if (showLabelRight) {
        writer.writeClassAttribute(Classes.create(toolBar, "labelHorizontal"));
      } else {
        writer.writeClassAttribute(Classes.create(toolBar, "labelVertical"));
      }
      if (label.getLabel() != null) {
        HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      } else {
        writer.writeText(" "); // this is a non-breaking-space
      }
      writer.endElement(HtmlElements.SPAN);
    }

    if (separateButtons) {
      writer.endElement(HtmlElements.SPAN);

      writer.startElement(HtmlElements.SPAN, command);
      writer.writeClassAttribute(Classes.create(toolBar, "menu"));
      // todo: span has not type: use data-tobago-type here (TOBAGO-1004)
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.BUTTON, false);
    }

    // render sub menu popup button
    if (showDropDownMenu) {
      writer.startElement(HtmlElements.IMG, command);
      final boolean dropDownDisabled
          = ComponentUtils.getBooleanAttribute(dropDownMenu, Attributes.DISABLED) || disabled;
      final String menuImage
          = ResourceManagerUtils.getImageOrDisabledImage(facesContext, "image/toolbarButtonMenu", dropDownDisabled);
      writer.writeAttribute(HtmlAttributes.SRC, menuImage, false);
      writer.endElement(HtmlElements.IMG);
      renderDropDownMenu(facesContext, writer, dropDownMenu);
    }
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.SPAN);
  }

  private void renderSeparator(
      final FacesContext facesContext, final UIToolBar toolBar, final UIToolBarSeparator separator,
      final TobagoResponseWriter writer)
      throws IOException {
    if (!separator.isRendered()) {
      return;
    }

    writer.startElement(HtmlElements.SPAN, separator);
    writer.writeClassAttribute(Classes.create(toolBar, "item", Markup.DISABLED));

    writer.startElement(HtmlElements.SPAN, separator);
    writer.writeClassAttribute(Classes.create(toolBar, "separator"));
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.SPAN);
  }

  private boolean hasAnyCommand(final AbstractUICommand command) {
    return !hasNoCommand(command);
  }

  private boolean hasNoCommand(final AbstractUICommand command) {
    return command.getActionExpression() == null
        && command.getActionListeners().length == 0
        && command.getLink() == null;
  }

  private String getImage(
      final FacesContext facesContext, final String name, final String iconSize, final boolean disabled,
      final boolean selected) {
    final int dot = ResourceManagerUtils.indexOfExtension(name);
    final int pos = dot == -1 ? name.length() : dot; // avoid exception if no '.' in name
    final String key = name.substring(0, pos);
    final String ext = name.substring(pos);

    String size = "";
    if (UIToolBar.ICON_SMALL.equals(iconSize)) {
      size = "16";
    } else if (UIToolBar.ICON_BIG.equals(iconSize)) {
      size = "32";
    }
    String image = null;

    if (disabled && selected) {
      if (dot != -1) {
        image = ResourceManagerUtils.getImageWithPath(facesContext, key + "SelectedDisabled" + size + ext, true);
      } else {
        image = ResourceManagerUtils.getImage(facesContext, key + "SelectedDisabled" + size, true);
      }
      if (image == null) {
        if (dot != -1) {
          image = ResourceManagerUtils.getImageWithPath(facesContext, key + "SelectedDisabled" + ext, true);
        } else {
          image = ResourceManagerUtils.getImage(facesContext, key + "SelectedDisabled", true);
        }
      }
    }
    if (image == null && disabled) {
      if (dot != -1) {
        image = ResourceManagerUtils.getImageWithPath(facesContext, key + "Disabled" + size + ext, true);
      } else {
        image = ResourceManagerUtils.getImage(facesContext, key + "Disabled" + size, true);
      }
      if (image == null) {
        if (dot != -1) {
          image = ResourceManagerUtils.getImageWithPath(facesContext, key + "Disabled" + ext, true);
        } else {
          image = ResourceManagerUtils.getImage(facesContext, key + "Disabled", true);
        }
      }
    }
    if (image == null && selected) {
      if (dot != -1) {
        image = ResourceManagerUtils.getImageWithPath(facesContext, key + "Selected" + size + ext, true);
      } else {
        image = ResourceManagerUtils.getImage(facesContext, key + "Selected" + size, true);
      }
      if (image == null) {
        if (dot != -1) {
          image = ResourceManagerUtils.getImageWithPath(facesContext, key + "Selected" + ext, true);
        } else {
          image = ResourceManagerUtils.getImage(facesContext, key + "Selected", true);
        }
      }
    }
    if (image == null) {
      if (dot != -1) {
        image = ResourceManagerUtils.getImageWithPath(facesContext, key + size + ext, true);
      } else {
        image = ResourceManagerUtils.getImage(facesContext, key + size, true);
      }
      if (image == null) {
        if (dot != -1) {
          image = ResourceManagerUtils.getImageWithPath(facesContext, key + ext, true);
        } else {
          image = ResourceManagerUtils.getImage(facesContext, key, true);
        }
      }
    }

    return image;
  }

  public static void renderDropDownMenu(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUIMenu dropDownMenu)
      throws IOException {
    writer.startElement(HtmlElements.OL, dropDownMenu);
    // XXX fix naming conventions for CSS classes
    writer.writeClassAttribute("tobago-menuBar tobago-menu-dropDownMenu");
    RenderUtils.encode(facesContext, dropDownMenu);
    writer.endElement(HtmlElements.OL);
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component)
      throws IOException {
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  private boolean hasSelectedValue(final Iterable<SelectItem> items, final Object value) {
    for (final SelectItem item : items) {
      if (ObjectUtils.equals(item.getValue(), value)) {
        return true;
      }
    }
    return false;
  }

}

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
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.component.UIToolBarSeparator;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIMenu;
import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.internal.util.ObjectUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
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

  protected String getLabelPosition(final UIComponent component) {
    return (String) component.getAttributes().get(Attributes.LABEL_POSITION);
  }

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

    Measure width = Measure.valueOf(-1);
    for (final UIComponent command : toolBar.getChildren()) {
      if (command instanceof AbstractUICommandBase) {
        width = renderToolbarCommand(context, toolBar, (AbstractUICommandBase) command, writer, width);
      } else if (command instanceof UIToolBarSeparator) {
        width = renderSeparator(context, toolBar, (UIToolBarSeparator) command, writer, width);
      } else {
        LOG.error("Illegal UIComponent class in toolbar (not a AbstractUICommandBase):" + command.getClass().getName());
      }
    }
  }

  private Measure renderToolbarCommand(
      final FacesContext facesContext, final UIToolBar toolBar, final AbstractUICommandBase command,
      final TobagoResponseWriter writer, final Measure width) throws IOException {
    if (command instanceof SelectBooleanCommand) {
      return renderSelectBoolean(facesContext, toolBar, command, writer, width);
    } else if (command instanceof SelectOneCommand) {
      return renderSelectOne(facesContext, toolBar, command, writer, width);
    } else {
      if (command.getFacet(Facets.RADIO) != null) {
        return renderSelectOne(facesContext, toolBar, command, writer, width);
      } else if (command.getFacet(Facets.CHECKBOX) != null) {
        return renderSelectBoolean(facesContext, toolBar, command, writer, width);
      } else {
        final CommandMap map = new CommandMap(new Command(facesContext, command));
        return renderToolbarButton(
            facesContext, toolBar, command, writer, false, width, map, null);
      }
    }
  }

  // todo: remove component creation in renderer, for JSF 2.0
  // todo: One solution is to make <tx:toolBarSelectOne> instead of <tc:toolBarSelectOne>
  private Measure renderSelectOne(
      final FacesContext facesContext, final UIToolBar toolBar, final AbstractUICommandBase command,
      final TobagoResponseWriter writer, Measure width) throws IOException {

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
      writer.writeClassAttribute(Classes.createWorkaround("toolBar", "selectOne", null));
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
          image = "image/1x1.gif";
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
        width = renderToolbarButton(
            facesContext, toolBar, command, writer, checked, width, map, formattedValue);
      }

      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.writeNameAttribute(radioId);
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
      writer.endElement(HtmlElements.INPUT);
      writer.endElement(HtmlElements.SPAN);
    }
    return width;
  }

  // todo: remove component creation in renderer, for JSF 2.0
  // todo: One solution is to make <tx:toolBarCheck> instead of <tc:toolBarCheck>
  // may be renamed to toolBarSelectBoolean?
  private Measure renderSelectBoolean(
      final FacesContext facesContext, final UIToolBar toolBar, final AbstractUICommandBase command,
      final TobagoResponseWriter writer, Measure width) throws IOException {

    UIComponent checkbox = command.getFacet(Facets.CHECKBOX);
    if (checkbox == null) {
      checkbox = CreateComponentUtils.createUISelectBooleanFacetWithId(facesContext, command);
    }

    final boolean checked = ComponentUtils.getBooleanAttribute(checkbox, Attributes.VALUE);
    final String clientId = checkbox.getClientId(facesContext);

    writer.startElement(HtmlElements.SPAN, checkbox);
    writer.writeClassAttribute(Classes.createWorkaround("toolBar", "selectBoolean", null));
    final CommandMap map = new CommandMap(new Command());
    width = renderToolbarButton(facesContext, toolBar, command, writer, checked, width, map, null);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.VALUE, Boolean.toString(checked), false);
    writer.endElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.SPAN);

    return width;
  }

  private Measure renderToolbarButton(
      final FacesContext facesContext, final UIToolBar toolBar, final AbstractUICommandBase command,
      final TobagoResponseWriter writer,
      final boolean selected, final Measure width, final CommandMap map, final String value)
      throws IOException {
    if (!command.isRendered()) {
      return width;
    }

    //final String clientId = command.getClientId(facesContext);
    final boolean disabled = ComponentUtils.getBooleanAttribute(command, Attributes.DISABLED);
    final LabelWithAccessKey label = new LabelWithAccessKey(command);
    final AbstractUIMenu dropDownMenu = FacetUtils.getDropDownMenu(command);
    final ResourceManager resources = getResourceManager();

    final String labelPosition = getLabelPosition(command.getParent());
    final String iconSize = getIconSize(command.getParent());
    final String iconName = (String) command.getAttributes().get(Attributes.IMAGE);
    final boolean lackImage = iconName == null;
    final String image = lackImage ? null : getImage(facesContext, iconName, iconSize, disabled, selected);

    final boolean showIcon = !UIToolBar.ICON_OFF.equals(iconSize);
    final boolean iconBig = UIToolBar.ICON_BIG.equals(iconSize);

    final boolean showLabelBottom = UIToolBar.LABEL_BOTTOM.equals(labelPosition);
    final boolean showLabelRight = UIToolBar.LABEL_RIGHT.equals(labelPosition);
    final boolean showLabel = showLabelBottom || showLabelRight;
    final boolean showDropDownMenu = dropDownMenu != null && dropDownMenu.isRendered();
    // two separate buttons for the command and the sub menu
    final boolean separateButtons = hasAnyCommand(command) && showDropDownMenu;

    final Measure paddingTop = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-top");
    final Measure paddingMiddle = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-middle");
    final Measure paddingBottom = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-bottom");
    final Measure paddingLeft = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-left");
    final Measure paddingCenter = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-center");
    final Measure paddingRight = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-right");
    final Measure iconBigHeight = resources.getThemeMeasure(facesContext, toolBar, "custom.icon-big-height");
    final Measure iconSmallHeight = resources.getThemeMeasure(facesContext, toolBar, "custom.icon-small-height");
    final Measure iconBigWidth = resources.getThemeMeasure(facesContext, toolBar, "custom.icon-big-width");
    final Measure iconSmallWidth = resources.getThemeMeasure(facesContext, toolBar, "custom.icon-small-width");

    // label style
    final Style labelStyle;
    if (showLabel) {
      labelStyle = new Style();
      labelStyle.setLeft(paddingLeft);
      labelStyle.setTop(paddingTop);
      labelStyle.setWidth(RenderUtils.calculateStringWidth(facesContext, toolBar, label.getText()));
      labelStyle.setHeight(resources.getThemeMeasure(facesContext, toolBar, "custom.label-height"));
    } else {
      labelStyle = null;
    }

    // button style
    final Style buttonStyle = new Style();
    buttonStyle.setLeft(Measure.ZERO);
    buttonStyle.setTop(Measure.ZERO);
    buttonStyle.setWidth(paddingLeft.add(paddingRight));
    buttonStyle.setHeight(paddingBottom.add(paddingTop));

    // icon style
    final Style iconStyle;

    if (showIcon) {
      iconStyle = new Style();
      iconStyle.setLeft(paddingLeft);
      iconStyle.setTop(paddingTop);
      iconStyle.setHeight(iconBig ? iconBigHeight : iconSmallHeight);
      if (lackImage && showLabelRight && StringUtils.isNotBlank(label.getText())) {
        iconStyle.setWidth(Measure.valueOf(1));
      } else {
        iconStyle.setWidth(iconBig ? iconBigWidth : iconSmallWidth);
      }
      if (showLabelBottom) {
        labelStyle.setTop(labelStyle.getTop().add(iconStyle.getHeight()).add(paddingMiddle));
        if (labelStyle.getWidth().lessThan(iconStyle.getWidth())) {
          // label smaller than icon
          labelStyle.setLeft(labelStyle.getLeft().add(iconStyle.getWidth().subtract(labelStyle.getWidth()).divide(2)));
          buttonStyle.setWidth(buttonStyle.getWidth().add(iconStyle.getWidth()));
        } else {
          // label bigger than icon
          iconStyle.setLeft(iconStyle.getLeft().add(labelStyle.getWidth().subtract(iconStyle.getWidth()).divide(2)));
          buttonStyle.setWidth(buttonStyle.getWidth().add(labelStyle.getWidth()));
        }
        buttonStyle.setHeight(
            buttonStyle.getHeight().add(iconStyle.getHeight()).add(paddingMiddle).add(labelStyle.getHeight()));
      } else if (showLabelRight) {
        labelStyle.setTop(labelStyle.getTop().add(iconStyle.getHeight().subtract(labelStyle.getHeight()).divide(2)));
        labelStyle.setLeft(labelStyle.getLeft().add(iconStyle.getWidth()).add(paddingCenter));
        buttonStyle.setWidth(
            buttonStyle.getWidth().add(iconStyle.getWidth()).add(paddingCenter).add(labelStyle.getWidth()));
        buttonStyle.setHeight(buttonStyle.getHeight().add(iconStyle.getHeight()));
      } else {
        buttonStyle.setWidth(buttonStyle.getWidth().add(iconStyle.getWidth()));
        buttonStyle.setHeight(buttonStyle.getHeight().add(iconStyle.getHeight()));
      }
    } else {
      iconStyle = null;
      if (showLabel) {
        // only label
        buttonStyle.setWidth(buttonStyle.getWidth().add(labelStyle.getWidth()));
        if (StringUtils.isBlank(label.getText())) {
          buttonStyle.setWidth(buttonStyle.getWidth().add(iconSmallWidth));
        }
        buttonStyle.setHeight(buttonStyle.getHeight().add(labelStyle.getHeight()));
      } else {
        // both off: use some reasonable defaults
        buttonStyle.setWidth(buttonStyle.getWidth().add(iconSmallWidth));
        buttonStyle.setHeight(buttonStyle.getHeight().add(iconSmallWidth));
      }
    }

    // opener style (for menu popup)
    final Style openerStyle = new Style();
    openerStyle.setWidth(resources.getThemeMeasure(facesContext, toolBar, "custom.opener-width"));
    openerStyle.setHeight(resources.getThemeMeasure(facesContext, toolBar, "custom.opener-height"));

    final Style menuStyle = new Style();
    menuStyle.setLeft(buttonStyle.getWidth());
    menuStyle.setTop(Measure.ZERO);
    menuStyle.setWidth(paddingLeft.add(openerStyle.getWidth()).add(paddingRight));
    menuStyle.setHeight(buttonStyle.getHeight());

    // opener style (for menu popup)
    openerStyle.setLeft(menuStyle.getWidth().subtract(openerStyle.getWidth()).divide(2));
    openerStyle.setTop(menuStyle.getHeight().subtract(openerStyle.getHeight()).divide(2));

    // item style
    final Style itemStyle = new Style();
    if (isRightAligned(toolBar)) { // overrides the default in the CSS file.
      itemStyle.setLeft(resources.getThemeMeasure(facesContext, toolBar, "css.border-right-width"));
    }
    itemStyle.setWidth(
        showDropDownMenu ? buttonStyle.getWidth().add(menuStyle.getWidth()) : buttonStyle.getWidth());
    itemStyle.setHeight(buttonStyle.getHeight());

    // XXX hack
    if (showDropDownMenu && lackImage && !showLabel) {
      itemStyle.setWidth(openerStyle.getWidth());
      buttonStyle.setWidth(openerStyle.getWidth());
    }

    // change values when only have one button
    if (showDropDownMenu && !separateButtons && (!lackImage || StringUtils.isNotBlank(label.getText()))) {
      openerStyle.setLeft(openerStyle.getLeft().add(buttonStyle.getWidth()));
      buttonStyle.setWidth(buttonStyle.getWidth().add(menuStyle.getWidth()));
    }
    
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
    HtmlRendererUtils.renderTip(command, writer);
    writer.writeStyleAttribute(itemStyle);

    writer.startElement(HtmlElements.SPAN, command);
    if (separateButtons || !showDropDownMenu) {
      writer.writeClassAttribute(Classes.create(toolBar, "button", selected ? Markup.SELECTED : Markup.NULL));
    } else {
      writer.writeClassAttribute(Classes.create(toolBar, "menu"));
    }
    writer.writeStyleAttribute(buttonStyle);
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
    if (showIcon && iconName != null) {
      writer.startElement(HtmlElements.IMG, command);
      writer.writeAttribute(HtmlAttributes.SRC, image, false);
      final String imageHover
          = ResourceManagerUtils.getImageWithPath(facesContext, HtmlRendererUtils.createSrc(iconName, "Hover"), true);
      if (imageHover != null) {
        writer.writeAttribute(DataAttributes.SRC_DEFAULT, image, false);
        writer.writeAttribute(DataAttributes.SRC_HOVER, imageHover, false);
      }
      writer.writeAttribute(HtmlAttributes.ALT, label.getText(), true);
      writer.writeStyleAttribute(iconStyle);
      writer.endElement(HtmlElements.IMG);
    }
    // render label
    if (showLabel) {
      writer.startElement(HtmlElements.SPAN, command);
      writer.writeClassAttribute(Classes.create(toolBar, "label"));
      writer.writeStyleAttribute(labelStyle);
      if (label.getText() != null) {
        HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
      }
      writer.endElement(HtmlElements.SPAN);
    }

    if (separateButtons) {
      writer.endElement(HtmlElements.SPAN);

      writer.startElement(HtmlElements.SPAN, command);
      writer.writeClassAttribute(Classes.create(toolBar, "menu"));
      writer.writeStyleAttribute(menuStyle);
      // todo: span has not type: use data-tobago-type here (TOBAGO-1004)
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.BUTTON, false);
    }

    // render sub menu popup button
    if (showDropDownMenu) {
      writer.startElement(HtmlElements.IMG, command);
      final boolean dropDownDisabled
          = ComponentUtils.getBooleanAttribute(dropDownMenu, Attributes.DISABLED) || disabled;
      final String menuImage = ResourceManagerUtils
          .getImageOrDisabledImageWithPath(facesContext, "image/toolbarButtonMenu.gif", dropDownDisabled);
      writer.writeAttribute(HtmlAttributes.SRC, menuImage, false);
      writer.writeStyleAttribute(openerStyle);
      writer.endElement(HtmlElements.IMG);
      renderDropDownMenu(facesContext, writer, dropDownMenu);
    }
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.SPAN);

    return width.add(itemStyle.getWidth()).add(2); // XXX
    // computation of the width of the toolBar will not be used in the moment.
  }

  private Measure renderSeparator(
      final FacesContext facesContext, final UIToolBar toolBar, final UIToolBarSeparator separator,
      final TobagoResponseWriter writer, final Measure width)
      throws IOException {
    if (!separator.isRendered()) {
      return width;
    }

    writer.startElement(HtmlElements.SPAN, separator);
    writer.writeClassAttribute(Classes.create(toolBar, "item", Markup.DISABLED));
    final Style itemStyle = new Style();
    itemStyle.setHeight(getItemHeight(facesContext, toolBar));
    itemStyle.setWidth(Measure.valueOf(10));
    writer.writeStyleAttribute(itemStyle);

    writer.startElement(HtmlElements.SPAN, separator);
    writer.writeClassAttribute(Classes.create(toolBar, "separator"));
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.SPAN);

    return width.add(itemStyle.getWidth()).add(2); // XXX
    // computation of the width of the toolBar will not be used in the moment.
  }

  protected Measure getItemHeight(final FacesContext facesContext, final Configurable toolBar) {
    final String iconSize = getIconSize((UIComponent) toolBar);
    final String labelPosition = getLabelPosition((UIComponent) toolBar);

    final boolean showIcon = !UIToolBar.ICON_OFF.equals(iconSize);
    final boolean iconBig = UIToolBar.ICON_BIG.equals(iconSize);
    final boolean iconSmall = UIToolBar.ICON_SMALL.equals(iconSize);
    final boolean showLabelBottom = UIToolBar.LABEL_BOTTOM.equals(labelPosition);
    final boolean showLabelRight = UIToolBar.LABEL_RIGHT.equals(labelPosition);
    final boolean showLabel = showLabelBottom || showLabelRight;

    final ResourceManager resources = getResourceManager();

    final Measure paddingTop = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-top");
    final Measure paddingMiddle = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-middle");
    final Measure paddingBottom = resources.getThemeMeasure(facesContext, toolBar, "custom.padding-bottom");
    final Measure iconHeight = iconBig
        ? resources.getThemeMeasure(facesContext, toolBar, "custom.icon-big-height")
        : resources.getThemeMeasure(facesContext, toolBar, "custom.icon-small-height");
    final Measure labelHeight = resources.getThemeMeasure(facesContext, toolBar, "custom.label-height");

    Measure result = paddingTop;
    if (showIcon) {
      result = result.add(iconHeight);
      if (showLabel && showLabelBottom) {
        result = result.add(paddingMiddle);
        result = result.add(labelHeight);
      }
    } else {
      if (showLabel) {
        result = result.add(labelHeight);
      } else {
        // both off: use some reasonable defaults
        result = result.add(16);
      }
    }
    result = result.add(paddingBottom);
    return result;
  }

  private boolean hasAnyCommand(final AbstractUICommandBase command) {
    return !hasNoCommand(command);
  }

  private boolean hasNoCommand(final AbstractUICommandBase command) {
    return command.getAction() == null
        && command.getActionListener() == null
        && command.getActionListeners().length == 0
        && command.getLink() == null
        && command.getAttributes().get(Attributes.ONCLICK) == null;
  }

  private String getImage(
      final FacesContext facesContext, final String name, final String iconSize, final boolean disabled,
      final boolean selected) {
    int pos = name.lastIndexOf('.');
    if (pos == -1) {
      pos = name.length(); // avoid exception if no '.' in name
    }
    final String key = name.substring(0, pos);
    final String ext = name.substring(pos);

    String size = "";
    if (UIToolBar.ICON_SMALL.equals(iconSize)) {
      size = "16";
    } else if (UIToolBar.ICON_BIG.equals(iconSize)) {
      size = "32";
    }
    String image = null;
    final ResourceManager resourceManager = ResourceManagerFactory.getResourceManager(facesContext);
    if (disabled && selected) {
      image = resourceManager.getImage(facesContext, key + "SelectedDisabled" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(facesContext, key + "SelectedDisabled" + ext, true);
      }
    }
    if (image == null && disabled) {
      image = resourceManager.getImage(facesContext, key + "Disabled" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(facesContext, key + "Disabled" + ext, true);
      }
    }
    if (image == null && selected) {
      image = resourceManager.getImage(facesContext, key + "Selected" + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(facesContext, key + "Selected" + ext, true);
      }
    }
    if (image == null) {
      image = resourceManager.getImage(facesContext, key + size + ext, true);
      if (image == null) {
        image = resourceManager.getImage(facesContext, key + ext, true);
      }
    }

    return facesContext.getExternalContext().getRequestContextPath() + image;
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

package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasWidth;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasLabelPosition;
import com.atanion.tobago.taglib.decl.HasIconSize;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;

/*
  * <![CDATA[
  *   Copyright (c) 2004 Atanion GmbH, Germany
  *   All rights reserved. Created 29.07.2003 at 15:09:53.
  *   $Id$
  *
  *       Renders a toolbar.<p>
  *       Allowed subcomponents are subtypes of UICommand i.e.
  *       <code>'button'</code> and <code>'link'</code> tags.
  *       These are rendered by ToolbarRenderer, so the result has
  *       no difference.<p>
  *       To add an dropdown menu to a button add a facet <code>'menupopup'</code>
  *       containing a
  *       <a href="../tobago/menu.html"><code>&lt;tobago:menu></code></a>
  *       tag to the button. Label's and Image's on those menu tag's are ignored
  *       and replaced by the renderer.
  *       <pre>
  *      &lt;tobago:button commandName="alert('test 0')" type="script"
  *          label="Alert 0" >
  *        &lt;f:facet name="menupopup">
  *          &lt;tobago:menu>
  *            &lt;tobago:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
  *            &lt;tobago:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
  *            &lt;tobago:menuItem action="alert('test 3')" type="script" label="Alert 3"/>
  *          &lt;/tobago:menu>
  *        &lt;/f:facet>
  *      &lt;/tobago:button>
  *      </pre>
  *
  *    ]]>
  */

@Tag(name="toolBar")
public class ToolBarTag extends PanelTag
    implements HasId, HasWidth, IsRendered, HasBinding, HasLabelPosition,
               HasIconSize {

  public static final String LABEL_BOTTOM = "bottom";
  public static final String LABEL_RIGHT = "right";
  public static final String LABEL_OFF = "off";

  public static final String ICON_SMALL = "small";
  public static final String ICON_BIG = "big";
  public static final String ICON_OFF = "off";

  private String labelPosition = LABEL_BOTTOM;
  private String iconSize = ICON_SMALL;


  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(
        component, ATTR_LABEL_POSITION, labelPosition, getIterationHelper());
    ComponentUtil.setStringProperty(
        component, ATTR_ICON_SIZE, iconSize, getIterationHelper());
  }

  public void release() {
    super.release();
    labelPosition = LABEL_BOTTOM;
    iconSize = ICON_SMALL;
  }

  public void setLabelPosition(String labelPosition) {
    this.labelPosition = labelPosition;
  }

  public void setIconSize(String iconSize) {
    this.iconSize = iconSize;
  }

}

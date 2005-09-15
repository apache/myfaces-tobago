/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasWidth;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import static org.apache.myfaces.tobago.TobagoConstants.*;

/**
 *   Created 29.07.2003 at 15:09:53.
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
 *      <tobago:button commandName="alert('test 0')" type="script"
 *          label="Alert 0" >
 *        <f:facet name="menupopup">
 *          <tobago:menu>
 *            <tobago:menuItem action="alert('test 1')" type="script" label="Alert 1"/>
 *            <tobago:menuItem action="alert('test 2')" type="script" label="Alert 2"/>
 *            <tobago:menuItem action="alert('test 3')" type="script" label="Alert 3"/>
 *          </tobago:menu>
 *        </f:facet>
 *      </tobago:button>
 *      </pre>
 *
 */

@Tag(name="toolBar")
@BodyContentDescription(anyTagOf="(<t:toolBarCommand>|<t:toolBarSelectBoolean>|<t:toolBarSelectOne>)* " )
public class ToolBarTag extends PanelTag
    implements HasIdBindingAndRendered, HasWidth {

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


  /**
   * Position of the button label, possible values are: right, bottom, off.
   * If toolbar is facet of box: bottom is changed to right!
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue="bottom")
  public void setLabelPosition(String labelPosition) {
    this.labelPosition = labelPosition;
  }


  /**
   * Size of button images, possible values are: small, big, off.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue="small")
  public void setIconSize(String iconSize) {
    this.iconSize = iconSize;
  }

}

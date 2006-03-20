package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.UIToolBar;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 11.02.2006
 * Time: 14:07:05
 * To change this template use File | Settings | File Templates.
 */

/**
 * <p/>
 * Renders a toolbar.<p>
 * Allowed subcomponents are subtypes of UICommand i.e.
 * <code>'button'</code> and <code>'link'</code> tags.
 * These are rendered by ToolbarRenderer, so the result has
 * no difference.<p>
 * To add an dropdown menu to a button add a facet <code>'menupopup'</code>
 * containing a
 * <a href="../tobago/menu.html"><code>&lt;tobago:menu></code></a>
 * tag to the button. Label's and Image's on those menu tag's are ignored
 * and replaced by the renderer.
 * <pre>
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
 */

@Tag(name = "toolBar")
@BodyContentDescription(anyTagOf = "(<tc:toolBarCommand>|<tc:toolBarSelectBoolean>|<tc:toolBarSelectOne>)* ")
@UIComponentTag(UIComponent = "javax.faces.component.UIPanel")
public interface ToolBarTagDeclaration extends PanelTagDeclaration {
  /**
   * Position of the button label, possible values are: right, bottom, off.
   * If toolbar is facet of box: bottom is changed to right!
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = UIToolBar.LABEL_BOTTOM,
      allowedValues = {UIToolBar.LABEL_BOTTOM, UIToolBar.LABEL_RIGHT, UIToolBar.LABEL_OFF})
  void setLabelPosition(String labelPosition);

  /**
   * Size of button images, possible values are: small, big, off.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = UIToolBar.ICON_SMALL,
      allowedValues = {UIToolBar.ICON_SMALL, UIToolBar.ICON_BIG, UIToolBar.ICON_OFF})
  void setIconSize(String iconSize);
}

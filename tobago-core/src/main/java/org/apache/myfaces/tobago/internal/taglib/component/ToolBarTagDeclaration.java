package org.apache.myfaces.tobago.internal.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIToolBar;

/*
 * Date: 11.02.2006
 * Time: 14:07:05
 */

/**
 * <p/>
 * Renders a toolbar.<p />
 * Allowed subcomponents are subtypes of UICommand i.e.
 * <code>'button'</code> and <code>'link'</code> tags.
 * These are rendered by ToolbarRenderer, so the result has
 * no difference.<p />
 * To add an dropdown menu to a button add a facet <code>'menupopup'</code>
 * containing a
 * <a href="menu.html"><code>&lt;tc:menu></code></a>
 * tag to the button. Label's and Image's on those menu tag's are ignored
 * and replaced by the renderer.
 * <pre>
 *      &lt;tc:button onclick="alert('test 0')"
 *          label="Alert 0" &gt;
 *        &lt;f:facet name="menupopup"&gt;
 *          &lt;tc:menu&gt;
 *            &lt;tc:menuCommand onclick="alert('test 1')" label="Alert 1"/&gt;
 *            &lt;tc:menuCommand onclick="alert('test 2')" label="Alert 2"/&gt;
 *            &lt;tc:menuCommand onclick="alert('test 3')" label="Alert 3"/&gt;
 *          &lt;/tc:menu&gt;
 *        &lt;/f:facet&gt;
 *      &lt;/tc:button&gt;
 *      </pre>
 */

@Tag(name = "toolBar")
@BodyContentDescription(anyTagOf = "(<tc:toolBarCommand>|<tc:toolBarSelectBoolean>|<tc:toolBarSelectOne>)* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIToolBar",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIToolBar",
    rendererType = RendererTypes.TOOL_BAR,
    allowedChildComponenents = {
        "org.apache.myfaces.tobago.SelectOneCommand",
        "org.apache.myfaces.tobago.SelectBooleanCommand",
        "org.apache.myfaces.tobago.Command"})
public interface ToolBarTagDeclaration extends PanelTagDeclaration {
  /**
   * Position of the button label, possible values are: right, bottom, off.
   * If toolbar is facet of box: bottom is changed to right!
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = AbstractUIToolBar.LABEL_BOTTOM,
      allowedValues = {AbstractUIToolBar.LABEL_BOTTOM, AbstractUIToolBar.LABEL_RIGHT, AbstractUIToolBar.LABEL_OFF})
  void setLabelPosition(String labelPosition);

  /**
   * Size of button images, possible values are: small, big, off.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = AbstractUIToolBar.ICON_SMALL,
      allowedValues = {AbstractUIToolBar.ICON_SMALL, AbstractUIToolBar.ICON_BIG, AbstractUIToolBar.ICON_OFF})
  void setIconSize(String iconSize);

  /**
   * Orientation of toolbar
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = AbstractUIToolBar.ORIENTATION_LEFT,
      allowedValues = {AbstractUIToolBar.ORIENTATION_LEFT, AbstractUIToolBar.ORIENTATION_RIGHT})
  void setOrientation(String orientation);
}

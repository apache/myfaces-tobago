package org.apache.myfaces.tobago.taglib.component;

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
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.taglib.decl.HasDeprecatedDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;

/**
 * Renders a tab panel.
 */
@Tag(name = "tabGroup")
@BodyContentDescription(anyTagOf = "(<tc:tab>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITabGroup",
    rendererType = "TabGroup")

public interface TabGroupTagDeclaration extends TobagoTagDeclaration, HasIdBindingAndRendered, HasDeprecatedDimension,
    IsImmediateCommand {
  /**
   * Deprecated! Use 'switchType' instead.
   * Flag indicating that tab switching is done by server request.
   *
   * @deprecated
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "false")
  @Deprecated
  void setServerside(String serverside);

  /**
   * Flag indicating that the tab navigation bar is rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "true")
  void setShowNavigationBar(String showNavigationBar);

  /**
   * Indicating how tab switching should be done.
   * <p/>
   * Possible values are:
   * <dl>
   *   <dt>client</dt>
   *   <dd>Tab switching is done on client, no server Request.</dd>
   *   <dt>reloadPage</dt>
   *   <dd>Tab switching is done by server request. Full page is reloaded.</dd>
   *   <dt>reloadTab</dt>
   *   <dd>Tab switching is done by server request. Only the Tab is reloaded.</dd>
   * </dl>
   * @param switchType Sets the switching type.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.String",
      allowedValues =
          {UITabGroup.SWITCH_TYPE_CLIENT, UITabGroup.SWITCH_TYPE_RELOAD_PAGE, UITabGroup.SWITCH_TYPE_RELOAD_TAB},
      defaultValue = UITabGroup.SWITCH_TYPE_CLIENT)
  void setSwitchType(String switchType);

  /**
   * <strong>ValueBindingExpression</strong> pointing to a Integer to save the
   * component's selected Tab.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer")
  void setSelectedIndex(String selectedIndex);

  /**
   * <strong>ValueBindingExpression</strong> pointing to a Integer to save the
   * component's selected Tab.
   * @deprecated Please use "selectedIndex" instead.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer")
  @Deprecated()
  void setState(String state);

  @TagAttribute
  @UIComponentTagAttribute(
      type = "javax.faces.el.MethodBinding",
      expression = DynamicExpression.METHOD_BINDING)
  void setTabChangeListener(String listener);
}

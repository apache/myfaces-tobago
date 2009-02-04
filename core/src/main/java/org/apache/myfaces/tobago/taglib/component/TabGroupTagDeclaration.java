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
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.taglib.decl.HasDeprecatedDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;

/*
 * Date: 08.04.2006
 * Time: 14:53:06
 */

/**
 * Renders a tabpanel.
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

   * Flag indicating that the Tab Navigationbar is rendered.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "true")
  @Deprecated
  void setShowNavigationBar(String serverside);


  /**
   * Flag indicating how tab switching should be done.
   * <p/>
   * Possible values are:
   * "client"     : Tab switching id done on client, no server Request. This is default.
   * "reloadPage" : Tab switching id done by server request. Full page is reloaded.
   * "reloadTab"  : Tab switching id done by server request. Only the Tab is reloaded.
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

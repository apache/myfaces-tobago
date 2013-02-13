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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUITabGroup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasAction;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasActionListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRenderedPartially;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutComponent;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsGridLayoutContainer;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsImmediateCommand;

import javax.faces.component.UIPanel;

/**
 * Renders a tab group which contains tab panels.
 */
@Tag(name = "tabGroup")
@BodyContentDescription(anyTagOf = "(<tc:tab>* ")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITabGroup",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUITabGroup",
    uiComponentFacesClass = "javax.faces.component.UIPanel",
    componentFamily = UIPanel.COMPONENT_FAMILY,
    rendererType = RendererTypes.TAB_GROUP,
    interfaces = "javax.faces.component.ActionSource2",
    allowedChildComponenents = "org.apache.myfaces.tobago.Tab")

public interface TabGroupTagDeclaration
    extends HasIdBindingAndRendered, IsImmediateCommand, HasAction, HasActionListener, HasMarkup, HasCurrentMarkup,
    IsGridLayoutComponent, IsGridLayoutContainer, HasRenderedPartially {

  /**
   * Flag indicating that the tab navigation bar is rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
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
      allowedValues = {AbstractUITabGroup.SWITCH_TYPE_CLIENT, AbstractUITabGroup.SWITCH_TYPE_RELOAD_PAGE,
              AbstractUITabGroup.SWITCH_TYPE_RELOAD_TAB},
      defaultValue = AbstractUITabGroup.SWITCH_TYPE_CLIENT)
  void setSwitchType(String switchType);

  /**
   *
   * <strong>ValueBindingExpression</strong> pointing to a Integer to save the
   * component's selected Tab.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  void setSelectedIndex(String selectedIndex);

  /**
   * For internal use. TODO: Check if this long needed
   */
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  void setRenderedIndex(String renderedIndex);

  @TagAttribute  
  @UIComponentTagAttribute(
      type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "org.apache.myfaces.tobago.event.TabChangeEvent")
  void setTabChangeListener(String listener);
}

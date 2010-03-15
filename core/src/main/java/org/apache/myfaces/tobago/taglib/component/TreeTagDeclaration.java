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
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasTreeNodeValue;
import org.apache.myfaces.tobago.taglib.decl.IsGridLayoutComponent;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

/**
 * A tree with classical look. 
 * Usually used with icons and junction lines to open folder, etc.
 */
@Tag(name = "tree")
@BodyContentDescription(anyTagOf = "<tc:treeNode>|<tc:treeData>")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UITree",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUITree",
    rendererType = RendererTypes.TREE,
    allowedChildComponenents = {
        "org.apache.myfaces.tobago.TreeNode",
        "org.apache.myfaces.tobago.TreeData"
        })
public interface TreeTagDeclaration
    extends HasIdBindingAndRendered, HasTreeNodeValue, IsRequired, IsGridLayoutComponent, HasMarkup {

  /**
   * Flag indicating whether or not this component should be render selectable items.
   * Possible values are:
   * <ul>
   * <li><strong>multi</strong> : a multi section tree is rendered</li>
   * <li><strong>single</strong> : a single section tree is rendered</li>
   * <li><strong>multiLeafOnly</strong> : a multi section tree is rendered,
   * only Leaf's are selectable</li>
   * <li><strong>singleLeafOnly</strong> : a single section tree is rendered,
   * only Leaf's are selectable</li>
   * </ul>
   * For any other value or if this attribute is omitted the items are not selectable.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "off",
      allowedValues = {"multi", "single", "multiLeafOnly", "singleLeafOnly", "off"})
  void setSelectable(String selectable);

  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowRootJunction(String showRootJunction);

  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowIcons(String showIcons);

  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowJunctions(String showJunctions);

  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowRoot(String showRoot);
  
  /**
   *
   * <strong>ValueBindingExpression</strong> pointing to a object to save the
   * component's state.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Object", expression = DynamicExpression.VALUE_BINDING_REQUIRED)
  void setState(String state);
}

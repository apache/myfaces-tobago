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

package org.apache.myfaces.tobago.internal.taglib.extension;

import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * Renders a menu item like a check box.
 * <pre>
 * &lt;tx:menuCheckbox/&gt;</pre>
 * is the short form of
 * <pre>
 * &lt;tc:menuCommand&gt;
 *   &lt;f:facet name="checkbox"&gt;
 *     &lt;tc:selectBooleanCheckbox/&gt;
 *   &lt;/f:facet&gt;
 * &lt;/tc:menuCommand&gt;</pre>
 *
 * @deprecated since Tobago 3.0. The tx-library is deprecated, please use the tc-library.
 */
@Tag(name = "menuCheckbox")
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.component.MenuCheckboxTag",
    componentType = "org.apache.myfaces.tobago.MenuCommand",
    rendererType = "MenuCommand",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.MenuCheckboxExtensionHandler")
public interface MenuCheckboxExtensionTag {

  /**
   * Action to invoke when clicked.
   * This must be a MethodExpression or a String representing the application action to invoke when
   * this component is activated by the user.
   * The MethodExpression must evaluate to a public method that takes no parameters,
   * and returns a String (the logical outcome) which is passed to the
   * NavigationHandler for this application.
   * The String is directly passed to the Navigationhandler.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {}, expression = DynamicExpression.METHOD_EXPRESSION,
      methodReturnType = "java.lang.Object")
  void setAction(final javax.el.MethodExpression action);

  /**
   * MethodExpression representing an action listener method that will be
   * notified when this component is activated by the user.
   * The expression must evaluate to a public method that takes an ActionEvent
   * parameter, with a return type of void.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {}, expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.event.ActionEvent")
  void setActionListener(final javax.el.MethodExpression actionListener);

  /**
   * Script to be invoked when clicked
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setOnclick(final javax.el.ValueExpression onclick);

  /**
   * Link to an arbitrary URL
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setLink(final javax.el.ValueExpression link);

  /**
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  void setBinding(final javax.el.ValueExpression binding);

  /**
   * Flag indicating whether or not this component should be rendered
   * (during Render Response Phase), or processed on any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setRendered(final javax.el.ValueExpression rendered);

  /**
   * Flag indicating that this element is disabled.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setDisabled(final javax.el.ValueExpression disabled);

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setValue(final javax.el.ValueExpression value);

  /**
   * Text value to display as label.
   * If text contains an underscore the next character is used as accesskey.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setLabel(final javax.el.ValueExpression label);

  /**
   * Flag indicating that, if this component is activated by the user,
   * notifications should be delivered to interested listeners and actions
   * immediately (that is, during Apply Request Values phase) rather than
   * waiting until Invoke Application phase.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setImmediate(final javax.el.ValueExpression immediate);

  /**
   * Specify, if the command calls an JSF-Action.
   * Useful to switch off the Double-Submit-Check and Waiting-Behavior.
   *
   * @param transition Indicates the transition.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setTransition(final javax.el.ValueExpression transition);

  /**
   * Indicate the partially rendered Components in a case of a submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.String[]")
  void setRenderedPartially(final javax.el.ValueExpression renderedPartially);

  /**
   * The component identifier for the input field component inside of the container.
   * This value must be unique within the closest parent component that is a naming container.
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  void setFieldId(final String fieldId);

  /**
   * The component identifier for this component.
   * This value must be unique within the closest parent component that is a naming container.
   * For tx components the id will be set to the container (e. g. the panel).
   * To set the id of the input field, you have to use the attribute "fieldId".
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  void setId(final String id);
}

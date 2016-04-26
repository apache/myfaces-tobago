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

import javax.el.MethodExpression;
import javax.el.ValueExpression;

/**
 * Renders a text input field with a label.
 * <br>
 * Short syntax of:
 * <pre>
 * &lt;tc:panel&gt;
 *   &lt;f:facet name="layout"&gt;
 *     &lt;tc:gridLayout columns="auto;*"/&gt;
 *   &lt;/f:facet&gt;
 *   &lt;tc:label value="#{label}" for="@auto"/&gt;
 *   &lt;tc:in value="#{value}"&gt;
 *     ...
 *   &lt;/tc:in&gt;
 * &lt;/tc:panel&gt;
 * </pre>
 *
 * @deprecated since Tobago 3.0. The tx-library is deprecated, please use the tc-library.
 */
@Deprecated
@Tag(name = "in")
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.InTag",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.InExtensionHandler")
public interface InExtensionTag {

  /**
   * Indicate markup of this component.
   * Possible value is 'none'. But this can be overridden in the theme.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none", type = "java.lang.String[]")
  void setMarkup(final ValueExpression markup);

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Object")
  void setValue(final ValueExpression value);

  /**
   * MethodExpression representing a value change listener method
   * that will be notified when a new value has been set for this input component.
   * The expression must evaluate to a public method that takes a ValueChangeEvent
   * parameter, with a return type of void.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.event.ValueChangeEvent")
  void setValueChangeListener(final MethodExpression valueChangeListener);

  /**
   * Text value to display as label.
   * If text contains an underscore the next character is used as accesskey.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setLabel(final ValueExpression label);

  /**
   * Flag indicating this component should receive the focus.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setFocus(final ValueExpression focus);

  /**
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  void setBinding(final ValueExpression binding);

  /**
   * Flag indicating whether or not this component should be rendered
   * (during Render Response Phase), or processed on any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setRendered(final ValueExpression rendered);

  /**
   * An expression that specifies the Converter for this component.
   * If the value binding expression is a String,
   * the String is used as an ID to look up a Converter.
   * If the value binding expression is a Converter,
   * uses that instance as the converter.
   * The value can either be a static value (ID case only)
   * or an EL expression.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.convert.Converter",
      expression = DynamicExpression.VALUE_EXPRESSION)
  void setConverter(final ValueExpression converter);

  /**
   * Clientside script function to add to this component's onchange handler.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setOnchange(final ValueExpression onchange);

  /**
   * MethodBinding which generates a list of suggested input values based on
   * the currently entered text, which could be retrieved via getSubmittedValue() on the UIIn.
   * The expression has to evaluate to a public method which has a javax.faces.component.UIInput parameter
   * and returns a List&lt;String&gt;(deprecated), a List&lt;org.apache.myfaces.tobago.model.AutoSuggestItem&gt;
   * or a org.apache.myfaces.tobago.model.AutoSuggestItems.
   *
   * @deprecated since 2.0.0, please use tc:suggest
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.component.UIInput")
  void setSuggestMethod(final MethodExpression suggestMethod);

  /**
   * Minimum number of chars to type before the list will be requested.
   *
   * @since 1.5.9
   * @deprecated since 2.0.0
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "1")
  void setSuggestMinChars(final ValueExpression suggestMinChars);

  /**
   * Time in milli seconds before the list will be requested.
   *
   * @since 1.5.9
   * @deprecated since 2.0.0
   */
  @Deprecated
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "300")
  void setSuggestDelay(final ValueExpression suggestDelay);

  /**
   * A method binding EL expression,
   * accepting FacesContext, UIComponent,
   * and Object parameters, and returning void, that validates
   * the component's local value.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_EXPRESSION,
      methodSignature = {"javax.faces.context.FacesContext", "javax.faces.component.UIComponent", "java.lang.Object"})
  void setValidator(final MethodExpression validator);

  /**
   * Flag indicating whether or not this component should be rendered as
   * password field , so you will not see the typed charakters.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setPassword(final ValueExpression password);

  /**
   * Flag indicating that this component will prohibit changes by the user.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setReadonly(final ValueExpression readonly);

  /**
   * Flag indicating that this element is disabled.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setDisabled(final ValueExpression disabled);

  /**
   * Flag indicating that a value is required.
   * If the value is an empty string a
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setRequired(final ValueExpression required);

  /**
   * Text value to display as tooltip.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setTip(final ValueExpression tip);

  /**
   * Displays a short text in the input field, that describes the meaning of this field.
   * This is part of HTML 5, the theme should emulate the behaviour, when the browser doesn't support it.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setPlaceholder(final ValueExpression placeholder);

  /**
   * The width for the label component. Default: 'auto'.
   * This value is used in the gridLayouts columns attribute.
   * See gridLayout tag for valid values.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setLabelWidth(final ValueExpression labelWidth);

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer")
  void setTabIndex(final ValueExpression tabIndex);

  /**
   * An expression that specifies the validator message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setValidatorMessage(final ValueExpression validatorMessage);

  /**
   * An expression that specifies the converter message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setConverterMessage(final ValueExpression converterMessage);

  /**
   * An expression that specifies the required message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setRequiredMessage(final ValueExpression requiredMessage);

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

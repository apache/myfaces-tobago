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
import org.apache.myfaces.tobago.compat.FacesUtilsEL;
import org.apache.myfaces.tobago.internal.taglib.SelectBooleanCheckboxTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;

/**
 * Renders a checkbox.
 */
@Tag(name = "selectBooleanCheckbox")
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.SelectBooleanCheckboxTag",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.SelectBooleanCheckboxExtensionHandler")
public class SelectBooleanCheckboxExtensionTag extends TobagoExtensionBodyTagSupport {

  private ValueExpression value;
  private MethodExpression valueChangeListener;
  private ValueExpression disabled;
  private ValueExpression readonly;
  private ValueExpression onchange;
  private ValueExpression label;
  private ValueExpression itemLabel;
  private ValueExpression accessKey;
  private ValueExpression rendered;
  private ValueExpression binding;
  private ValueExpression tip;
  private ValueExpression converter;
  private MethodExpression validator;
  private ValueExpression labelWidth;
  private ValueExpression markup;
  private ValueExpression tabIndex;
  private ValueExpression required;
  private ValueExpression focus;
  private ValueExpression validatorMessage;
  private ValueExpression converterMessage;
  private ValueExpression requiredMessage;
  private String fieldId;

  private LabelExtensionTag labelTag;
  private SelectBooleanCheckboxTag selectBooleanCheckboxTag;

  @Override
  public int doStartTag() throws JspException {

    labelTag = new LabelExtensionTag();
    labelTag.setPageContext(pageContext);
    if (id != null) {
      labelTag.setId(id);
    }
    if (label != null) {
      labelTag.setValue(label);
    }
    if (accessKey != null) {
      labelTag.setAccessKey(accessKey);
    }
    if (tip != null) {
      labelTag.setTip(tip);
    }
    if (rendered != null) {
      labelTag.setRendered(rendered);
    }
    if (labelWidth != null) {
      labelTag.setColumns(createStringValueExpression(labelWidth.getExpressionString() + ";*"));
    }
    if (markup != null) {
      labelTag.setMarkup(markup);
    }
    labelTag.setParent(getParent());
    labelTag.setJspId(nextJspId());
    labelTag.doStartTag();

    selectBooleanCheckboxTag = new SelectBooleanCheckboxTag();
    selectBooleanCheckboxTag.setPageContext(pageContext);
    if (value != null) {
      selectBooleanCheckboxTag.setValue(value);
    }
    if (valueChangeListener != null) {
      selectBooleanCheckboxTag.setValueChangeListener(valueChangeListener);
    }
    if (binding != null) {
      selectBooleanCheckboxTag.setBinding(binding);
    }
    if (onchange != null) {
      selectBooleanCheckboxTag.setOnchange(onchange);
    }
    if (validator != null) {
      selectBooleanCheckboxTag.setValidator(validator);
    }
    if (converter != null) {
      selectBooleanCheckboxTag.setConverter(converter);
    }
    if (disabled != null) {
      selectBooleanCheckboxTag.setDisabled(disabled);
    }
    if (fieldId != null) {
      selectBooleanCheckboxTag.setId(fieldId);
    }
    if (readonly != null) {
      selectBooleanCheckboxTag.setReadonly(readonly);
    }
    if (focus != null) {
      selectBooleanCheckboxTag.setFocus(focus);
    }
    if (required != null) {
      selectBooleanCheckboxTag.setRequired(required);
    }
    if (label != null) {
      selectBooleanCheckboxTag.setLabel(label);
    }
    if (itemLabel != null) {
      selectBooleanCheckboxTag.setItemLabel(itemLabel);
    } else {
      selectBooleanCheckboxTag.setItemLabel(FacesUtilsEL.createValueExpression(""));
    }
    if (markup != null) {
      selectBooleanCheckboxTag.setMarkup(markup);
    }
    if (tabIndex != null) {
      selectBooleanCheckboxTag.setTabIndex(tabIndex);
    }
    if (validatorMessage != null) {
      selectBooleanCheckboxTag.setValidatorMessage(validatorMessage);
    }
    if (converterMessage != null) {
      selectBooleanCheckboxTag.setConverterMessage(converterMessage);
    }
    if (requiredMessage != null) {
      selectBooleanCheckboxTag.setRequiredMessage(requiredMessage);
    }
    selectBooleanCheckboxTag.setParent(labelTag);
    selectBooleanCheckboxTag.setJspId(nextJspId());
    selectBooleanCheckboxTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    selectBooleanCheckboxTag.doEndTag();
    labelTag.doEndTag();
    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    binding = null;
    onchange = null;
    disabled = null;
    label = null;
    itemLabel = null;
    accessKey = null;
    labelWidth = null;
    readonly = null;
    rendered = null;
    converter = null;
    validator = null;
    tip = null;
    value = null;
    valueChangeListener = null;
    markup = null;
    tabIndex = null;
    focus = null;
    required = null;
    selectBooleanCheckboxTag = null;
    labelTag = null;
    validatorMessage = null;
    converterMessage = null;
    requiredMessage = null;
    fieldId = null;
  }

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean")
  public void setValue(final ValueExpression value) {
    this.value = value;
  }

  /**
   * MethodBinding representing a value change listener method that will be notified when a new value has been set for
   * this input component. The expression must evaluate to a public method that takes a ValueChangeEvent parameter, with
   * a return type of void.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.event.ValueChangeEvent")
  public void setValueChangeListener(final MethodExpression valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  /**
   * Flag indicating that this element is disabled.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setDisabled(final ValueExpression disabled) {
    this.disabled = disabled;
  }

  /**
   * Flag indicating that this component will prohibit changes by the user.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setReadonly(final ValueExpression readonly) {
    this.readonly = readonly;
  }

  /**
   * Clientside script function to add to this component's onchange handler.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setOnchange(final ValueExpression onchange) {
    this.onchange = onchange;
  }

  /**
   * Text value to display as label. If text contains an underscore the next character is used as accesskey.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabel(final ValueExpression label) {
    this.label = label;
  }

  /**
   * The accessKey of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Character")
  public void setAccessKey(final javax.el.ValueExpression accessKey) {
    this.accessKey = accessKey;
  }

  /**
   * Label to be displayed to the user for this option. This label will displayed beneath the component like the label
   * of other check box components.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setItemLabel(final ValueExpression itemLabel) {
    this.itemLabel = itemLabel;
  }

  /**
   * A method binding EL expression, accepting FacesContext, UIComponent, and Object parameters, and returning void,
   * that validates the component's local value.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_EXPRESSION,
      methodSignature = {"javax.faces.context.FacesContext", "javax.faces.component.UIComponent", "java.lang.Object"})
  public void setValidator(final MethodExpression validator) {
    this.validator = validator;
  }

  /**
   * An expression that specifies the Converter for this component. If the value binding expression is a String, the
   * String is used as an ID to look up a Converter. If the value binding expression is a Converter, uses that instance
   * as the converter. The value can either be a static value (ID case only) or an EL expression.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.convert.Converter",
      expression = DynamicExpression.VALUE_EXPRESSION)
  public void setConverter(final ValueExpression converter) {
    this.converter = converter;
  }

  /**
   * Flag indicating whether or not this component should be rendered (during Render Response Phase), or processed on
   * any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  public void setRendered(final ValueExpression rendered) {
    this.rendered = rendered;
  }

  /**
   * The value binding expression linking this component to a property in a backing bean.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  public void setBinding(final ValueExpression binding) {
    this.binding = binding;
  }

  /**
   * Text value to display as tooltip.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setTip(final ValueExpression tip) {
    this.tip = tip;
  }

  /**
   * The width for the label component. Default: 'auto'. This value is used in the gridLayouts columns attribute. See
   * gridLayout tag for valid values.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabelWidth(final ValueExpression labelWidth) {
    this.labelWidth = labelWidth;
  }

  /**
   * Indicate markup of this component. Possible value is 'none'. But this can be overridden in the theme.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none", type = "java.lang.String[]")
  public void setMarkup(final ValueExpression markup) {
    this.markup = markup;
  }

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer")
  public void setTabIndex(final ValueExpression tabIndex) {
    this.tabIndex = tabIndex;
  }

  /**
   * Flag indicating this component should receive the focus.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setFocus(final ValueExpression focus) {
    this.focus = focus;
  }

  /**
   * Flag indicating that a value is required. If the value is an empty string a ValidationError occurs and a Error
   * Message is rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setRequired(final ValueExpression required) {
    this.required = required;
  }

  /**
   * An expression that specifies the validator message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setValidatorMessage(final ValueExpression validatorMessage) {
    this.validatorMessage = validatorMessage;
  }

  /**
   * An expression that specifies the converter message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setConverterMessage(final ValueExpression converterMessage) {
    this.converterMessage = converterMessage;
  }

  /**
   * An expression that specifies the required message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setRequiredMessage(final ValueExpression requiredMessage) {
    this.requiredMessage = requiredMessage;
  }

  /**
   * The component identifier for the input field component inside of the container. This value must be unique within
   * the closest parent component that is a naming container.
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setFieldId(final String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * The component identifier for this component. This value must be unique within the closest parent component that is
   * a naming container. For tx components the id will be set to the container (e. g. the panel). To set the id of the
   * input field, you have to use the attribute "fieldId".
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setId(final String id) {
    super.setId(id);
  }
}

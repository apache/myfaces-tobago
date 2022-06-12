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
import org.apache.myfaces.tobago.internal.taglib.SelectManyBoxTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;

/**
 * Renders a multi selection option listbox with a label.
 */
@Tag(name = "selectManyBox")
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.SelectManyBoxTag",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.SelectManyBoxExtensionHandler")
public class SelectManyBoxExtensionTag extends TobagoExtensionBodyTagSupport {

  private ValueExpression accessKey;
  private ValueExpression allowClear;
  private ValueExpression allowCustom;
  private ValueExpression binding;
  private ValueExpression converter;
  private ValueExpression converterMessage;
  private ValueExpression disabled;
  private ValueExpression focus;
  private ValueExpression hideDropdown;
  private ValueExpression label;
  private ValueExpression labelWidth;
  private ValueExpression markup;
  private ValueExpression  matcher;
  private ValueExpression  maximumInputLength;
  private ValueExpression  maximumSelectionLength;
  private ValueExpression  minimumInputLength;
  private ValueExpression onchange;
  private ValueExpression placeholder;
  private ValueExpression readonly;
  private ValueExpression rendered;
  private ValueExpression required;
  private ValueExpression requiredMessage;
  private ValueExpression tabIndex;
  private ValueExpression tip;
  private ValueExpression tokenizer;
  private ValueExpression tokenSeparators;
  private MethodExpression validator;
  private ValueExpression validatorMessage;
  private ValueExpression value;
  private MethodExpression valueChangeListener;
  private String fieldId;

  private LabelExtensionTag labelTag;
  private SelectManyBoxTag selectManyBoxTag;

  @Override
  public int doStartTag() throws JspException {

    labelTag = new LabelExtensionTag();
    labelTag.setPageContext(pageContext);
    labelTag.setRows("*");
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

    selectManyBoxTag = new SelectManyBoxTag();
    selectManyBoxTag.setPageContext(pageContext);
    if (allowClear != null) {
      selectManyBoxTag.setAllowClear(allowClear);
    }
    if (allowCustom != null) {
      selectManyBoxTag.setAllowCustom(allowCustom);
    }
    if (binding != null) {
      selectManyBoxTag.setBinding(binding);
    }
    if (converter != null) {
      selectManyBoxTag.setConverter(converter);
    }
    if (converterMessage != null) {
      selectManyBoxTag.setConverterMessage(converterMessage);
    }
    if (disabled != null) {
      selectManyBoxTag.setDisabled(disabled);
    }
    if (fieldId != null) {
      selectManyBoxTag.setId(fieldId);
    }
    if (focus != null) {
      selectManyBoxTag.setFocus(focus);
    }
    if (hideDropdown != null) {
      selectManyBoxTag.setHideDropdown(hideDropdown);
    }
    if (label != null) {
      selectManyBoxTag.setLabel(label);
    }
    if (markup != null) {
      selectManyBoxTag.setMarkup(markup);
    }
    if (matcher != null) {
      selectManyBoxTag.setMatcher(matcher);
    }
    if (maximumInputLength != null) {
      selectManyBoxTag.setMaximumInputLength(maximumInputLength);
    }
    if (maximumSelectionLength != null) {
      selectManyBoxTag.setMaximumSelectionLength(maximumSelectionLength);
    }
    if (minimumInputLength != null) {
      selectManyBoxTag.setMinimumInputLength(minimumInputLength);
    }
    if (onchange != null) {
      selectManyBoxTag.setOnchange(onchange);
    }
    if (placeholder != null) {
      selectManyBoxTag.setPlaceholder(placeholder);
    }
    if (readonly != null) {
      selectManyBoxTag.setReadonly(readonly);
    }
    if (required != null) {
      selectManyBoxTag.setRequired(required);
    }
    if (requiredMessage != null) {
      selectManyBoxTag.setRequiredMessage(requiredMessage);
    }
    if (tabIndex != null) {
      selectManyBoxTag.setTabIndex(tabIndex);
    }
    if (tokenizer != null) {
      selectManyBoxTag.setTokenizer(tokenizer);
    }
    if (tokenSeparators != null) {
      if (!tokenSeparators.isLiteralText()) {
        selectManyBoxTag.setTokenSeparators(tokenSeparators);
      } else {
        selectManyBoxTag.setTokenSeparators(tokenSeparators);
      }
    }
    if (validator != null) {
      selectManyBoxTag.setValidator(validator);
    }
    if (validatorMessage != null) {
      selectManyBoxTag.setValidatorMessage(validatorMessage);
    }
    if (value != null) {
      selectManyBoxTag.setValue(value);
    }
    if (valueChangeListener != null) {
      selectManyBoxTag.setValueChangeListener(valueChangeListener);
    }

    selectManyBoxTag.setParent(labelTag);
    selectManyBoxTag.setJspId(nextJspId());
    selectManyBoxTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    selectManyBoxTag.doEndTag();
    labelTag.doEndTag();
    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    accessKey = null;
    allowClear = null;
    allowCustom = null;
    binding = null;
    converter = null;
    converterMessage = null;
    disabled = null;
    fieldId = null;
    focus = null;
    hideDropdown = null;
    label = null;
    labelTag = null;
    labelWidth = null;
    markup = null;
    matcher = null;
    maximumInputLength = null;
    maximumSelectionLength = null;
    minimumInputLength = null;
    onchange = null;
    placeholder = null;
    readonly = null;
    rendered = null;
    required = null;
    requiredMessage = null;
    selectManyBoxTag = null;
    tabIndex = null;
    tip = null;
    tokenizer = null;
    tokenSeparators = null;
    validator = null;
    validatorMessage = null;
    valueChangeListener = null;
    value = null;

  }

  /**
   * The accessKey of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Character")
  public void setAccessKey(final ValueExpression accessKey) {
    this.accessKey = accessKey;
  }

  /**
   * Flag indicating that this select provides support for clearable selections.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false", generate = false)
  public void setAllowClear(ValueExpression allowClear) {
    this.allowClear = allowClear;
  }

  /**
   * Flag indicating that this select enables free text responses.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false", generate = false)
  public void setAllowCustom(ValueExpression allowCustom) {
    this.allowCustom = allowCustom;
  }

  /**
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  public void setBinding(final ValueExpression binding) {
    this.binding = binding;
  }

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
  public void setConverter(final ValueExpression converter) {
    this.converter = converter;
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
   * Flag indicating that this element is disabled.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setDisabled(final ValueExpression disabled) {
    this.disabled = disabled;
  }

  /**
   * The component identifier for the input field component inside of the container.
   * This value must be unique within the closest parent component that is a naming container.
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setFieldId(final String fieldId) {
    this.fieldId = fieldId;
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
   * Hide the dropdown, this is only useful with allowCustom=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false", generate = false)
  public void setHideDropdown(ValueExpression hideDropdown) {
    this.hideDropdown = hideDropdown;
  }

  /**
   * The component identifier for this component.
   * This value must be unique within the closest parent component that is a naming container.
   * For tx components the id will be set to the container (e. g. the panel).
   * To set the id of the input field, you have to use the attribute "fieldId".
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setId(final String id) {
    super.setId(id);
  }

  /**
   * Text value to display as label.
   * If text contains an underscore the next character is used as accesskey.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabel(final ValueExpression label) {
    this.label = label;
  }

  /**
   * The width for the label component. Default: 'auto'.
   * This value is used in the gridLayouts columns attribute.
   * See gridLayout tag for valid values.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabelWidth(final ValueExpression labelWidth) {
    this.labelWidth = labelWidth;
  }

  /**
   * Indicate markup of this component.
   * Possible value is 'none'. But this can be overridden in the theme.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none", type = "java.lang.String[]")
  public void setMarkup(final ValueExpression markup) {
    this.markup = markup;
  }

  /**
   * Javascript callback to handle custom search matching
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(generate = false)
  public void setMatcher(ValueExpression matcher) {
    this.matcher = matcher;
  }

  /**
   * Maximum number of characters that may be provided for a search term.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "int", defaultValue = "0", generate = false)
  public void setMaximumInputLength(ValueExpression maximumInputLength) {
    this.maximumInputLength = maximumInputLength;
  }

  /**
   * The maximum number of items that may be selected in a multi-select control.
   * If the value of this option is less than 1, the number of selected items will not be limited.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "int", defaultValue = "0", generate = false)
  public void setMaximumSelectionLength(ValueExpression maximumSelectionLength) {
    this.maximumSelectionLength = maximumSelectionLength;
  }

  /**
   * Minimum number of characters required to start a search.
   *
   * This is a select2 feature and will force select2=true
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "int", defaultValue = "0", generate = false)
  public void setMinimumInputLength(ValueExpression minimumInputLength) {
    this.minimumInputLength = minimumInputLength;
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
   * Displays a short text in the input field, that describes the meaning of this field.
   * This is part of HTML 5, the theme should emulate the behaviour, when the browser doesn't support it.
   * <p/>
   * The text will not be displayed, when the input field is readonly or disabled.
   * @param placeholder The text to display
   */
  @TagAttribute
  @UIComponentTagAttribute
  public void setPlaceholder(ValueExpression placeholder) {
    this.placeholder = placeholder;
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
   * Flag indicating whether or not this component should be rendered
   * (during Render Response Phase), or processed on any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  public void setRendered(final ValueExpression rendered) {
    this.rendered = rendered;
  }

  /**
   * Flag indicating that a value is required.
   * If the value is an empty string a
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setRequired(final ValueExpression required) {
    this.required = required;
  }

  /**
   * An expression that specifies the required message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setRequiredMessage(final ValueExpression requiredMessage) {
    this.requiredMessage = requiredMessage;
  }

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer")
  public void setTabIndex(final ValueExpression tabIndex) {
    this.tabIndex = tabIndex;
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
   * A javascript callback that handles automatic tokenization of free-text entry.
   */
  @TagAttribute()
  @UIComponentTagAttribute(generate = false)
  public void setTokenizer(ValueExpression tokenizer) {
    this.tokenizer = tokenizer;
  }

  /**
   * The list of characters that should be used as token separators.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "java.lang.String[]", generate = false)
  public void setTokenSeparators(ValueExpression tokenSeparators) {
    this.tokenSeparators = tokenSeparators;
  }

  /**
   * A method binding EL expression,
   * accepting FacesContext, UIComponent,
   * and Object parameters, and returning void, that validates
   * the component's local value.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_EXPRESSION,
      methodSignature = { "javax.faces.context.FacesContext", "javax.faces.component.UIComponent", "java.lang.Object" })
  public void setValidator(final MethodExpression validator) {
    this.validator = validator;
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
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Object[]", "java.util.List"})
  public void setValue(final ValueExpression value) {
    this.value = value;
  }

  /**
   * MethodBinding representing a value change listener method
   * that will be notified when a new value has been set for this input component.
   * The expression must evaluate to a public method that takes a ValueChangeEvent
   * parameter, with a return type of void.
   *
   * @param valueChangeListener
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.event.ValueChangeEvent")
  public void setValueChangeListener(final MethodExpression valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }
}

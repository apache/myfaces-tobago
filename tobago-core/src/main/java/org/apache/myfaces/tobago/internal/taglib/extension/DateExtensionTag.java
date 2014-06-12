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
import org.apache.myfaces.tobago.internal.taglib.DatePickerTag;
import org.apache.myfaces.tobago.internal.taglib.DateTag;
import org.apache.myfaces.tobago.internal.taglib.FormTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;

/**
 * Renders a date input field with a date picker and a label.
 * <br />
 * Short syntax of:
 * <p/>
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="auto;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *   &lt;tc:date value="#{value}">
 *     ...
 *   &lt;/tc:in>
 * &lt;/tc:panel>
 * </pre>
 */
@Tag(name = "date")
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.DateTag",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.DateExtensionHandler")
public class DateExtensionTag extends TobagoExtensionBodyTagSupport {

  private static final long serialVersionUID = 1L;

  private ValueExpression binding;
  private ValueExpression converter;
  private MethodExpression validator;
  private ValueExpression disabled;
  private ValueExpression focus;
  private ValueExpression label;
  private ValueExpression accessKey;
  private ValueExpression readonly;
  private ValueExpression rendered;
  private ValueExpression required;
  private ValueExpression tip;
  private ValueExpression placeholder;
  private ValueExpression value;
  private MethodExpression valueChangeListener;
  private ValueExpression onchange;
  private ValueExpression tabIndex;
  private ValueExpression markup;
  private ValueExpression validatorMessage;
  private ValueExpression converterMessage;
  private ValueExpression requiredMessage;
  private String fieldId;
  private String pickerId;
  private String formId;

  private ValueExpression labelWidth;
  private LabelExtensionTag labelTag;
  private DateTag dateTag;

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
    if (labelWidth != null) {
      labelTag.setColumns(createStringValueExpression(labelWidth.getExpressionString() + ";*;auto"));
    } else {
      labelTag.setColumns(createStringValueExpression("auto;*;auto"));
    }
    if (tip != null) {
      labelTag.setTip(tip);
    }
    if (rendered != null) {
      labelTag.setRendered(rendered);
    }
    if (markup != null) {
      labelTag.setMarkup(markup);
    }
    labelTag.setParent(getParent());
    labelTag.setJspId(nextJspId());
    labelTag.doStartTag();

    dateTag = new DateTag();
    dateTag.setPageContext(pageContext);
    if (value != null) {
      dateTag.setValue(value);
    }
    if (valueChangeListener != null) {
      dateTag.setValueChangeListener(valueChangeListener);
    }
    if (placeholder != null) {
      dateTag.setPlaceholder(placeholder);
    }
    if (binding != null) {
      dateTag.setBinding(binding);
    }
    if (converter != null) {
      dateTag.setConverter(converter);
    }
    if (validator != null) {
      dateTag.setValidator(validator);
    }
    if (disabled != null) {
      dateTag.setDisabled(disabled);
    }
    if (onchange != null) {
      dateTag.setOnchange(onchange);
    }
    if (focus != null) {
      dateTag.setFocus(focus);
    }
    if (fieldId != null) {
      dateTag.setId(fieldId);
    }
    if (label != null) {
      dateTag.setLabel(label);
    }
    if (readonly != null) {
      dateTag.setReadonly(readonly);
    }
    if (required != null) {
      dateTag.setRequired(required);
    }
    if (markup != null) {
      dateTag.setMarkup(markup);
    }
    if (tabIndex != null) {
      dateTag.setTabIndex(tabIndex);
    }
    if (validatorMessage != null) {
      dateTag.setValidatorMessage(validatorMessage);
    }
    if (converterMessage != null) {
      dateTag.setConverterMessage(converterMessage);
    }
    if (requiredMessage != null) {
      dateTag.setRequiredMessage(requiredMessage);
    }

    dateTag.setParent(labelTag);
    dateTag.setJspId(nextJspId());
    dateTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    dateTag.doEndTag();
    final FormTag formTag = new FormTag();
    formTag.setPageContext(pageContext);
    formTag.setParent(labelTag);
    if (formId != null) {
      formTag.setId(formId);
    }
    formTag.setJspId(nextJspId());
    formTag.doStartTag();

    final DatePickerTag datePicker = new DatePickerTag();
    datePicker.setPageContext(pageContext);
    datePicker.setFor("@auto");
    if (tabIndex != null) {
      datePicker.setTabIndex(tabIndex);
    }
    if (markup != null) {
      datePicker.setMarkup(markup);
    }
    datePicker.setParent(formTag);
    if (pickerId != null) {
      datePicker.setId(pickerId);
    }
    datePicker.setJspId(nextJspId());
    datePicker.doStartTag();
    datePicker.doEndTag();
    formTag.doEndTag();

    labelTag.doEndTag();
    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    binding = null;
    converter = null;
    validator = null;
    disabled = null;
    labelWidth = null;
    focus = null;
    label = null;
    accessKey = null;
    readonly = null;
    rendered = null;
    required = null;
    tip = null;
    placeholder = null;
    value = null;
    valueChangeListener = null;
    onchange = null;
    markup = null;
    tabIndex = null;
    labelTag = null;
    dateTag = null;
    validatorMessage = null;
    converterMessage = null;
    requiredMessage = null;
    fieldId = null;
    pickerId = null;
    formId = null;
  }

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setValue(final ValueExpression value) {
    this.value = value;
  }

  /**
   * MethodBinding representing a value change listener method
   * that will be notified when a new value has been set for this input component.
   * The expression must evaluate to a public method that takes a ValueChangeEvent
   * parameter, with a return type of void.
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
   * Text value to display as label.
   * If text contains an underscore the next character is used as accesskey.
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
   * Client side script function to add to this component's onchange handler.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setOnchange(final ValueExpression onchange) {
    this.onchange = onchange;
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
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  public void setBinding(final ValueExpression binding) {
    this.binding = binding;
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
   * Flag indicating that this component will prohibit changes by the user.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setReadonly(final ValueExpression readonly) {
    this.readonly = readonly;
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
   * Text value to display as tooltip.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setTip(final ValueExpression tip) {
    this.tip = tip;
  }

  /**
   * Displays a short text in the input field, that describes the meaning of this field.
   * This is part of HTML 5, the theme should emulate the behaviour, when the browser doesn't support it.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setPlaceholder(final ValueExpression placeholder) {
    this.placeholder = placeholder;
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
  @UIComponentTagAttribute(type = "java.lang.String[]", defaultValue = "none")
  public void setMarkup(final ValueExpression markup) {
    this.markup = markup;
  }

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer")
  public void setTabIndex(final ValueExpression tabIndex) {
    this.tabIndex = tabIndex;
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
   * The component identifier for the input field component inside of the container.
   * This value must be unique within the closest parent component that is a naming container.
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setFieldId(final String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * The component identifier for the automatically created picker component inside of the container.
   * This value must be unique within the closest parent component that is a naming container.
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setPickerId(final String pickerId) {
    this.pickerId = pickerId;
  }

  /**
   * The component identifier for the automatically created form component inside of the container.
   * This value must be unique within the closest parent component that is a naming container.
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setFormId(final String formId) {
    this.formId = formId;
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
}

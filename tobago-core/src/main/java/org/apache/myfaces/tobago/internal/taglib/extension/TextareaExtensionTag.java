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
import org.apache.myfaces.tobago.internal.taglib.TextareaTag;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;

/**
 * Renders a multi line text input control with a label. <br /> Short syntax of:
 * <p/>
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="auto;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *   &lt;tc:textarea value="#{value}">
 *     ...
 *   &lt;/tc:in>
 * &lt;/tc:panel>
 * </pre>
 */

@Tag(name = "textarea")
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.TextareaTag",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.TextareaExtensionHandler")
public class TextareaExtensionTag extends TobagoExtensionBodyTagSupport {

  private ValueExpression binding;
  private ValueExpression converter;
  private ValueExpression disabled;
  private ValueExpression focus;
  private ValueExpression label;
  private ValueExpression accessKey;
  private ValueExpression readonly;
  private ValueExpression rendered;
  private ValueExpression required;
  private ValueExpression tip;
  private ValueExpression value;
  private MethodExpression valueChangeListener;
  private MethodExpression validator;
  private ValueExpression onchange;
  private ValueExpression markup;
  private ValueExpression labelWidth;
  private ValueExpression tabIndex;
  private ValueExpression validatorMessage;
  private ValueExpression converterMessage;
  private ValueExpression requiredMessage;
  private String fieldId;

  private LabelExtensionTag labelTag;
  private TextareaTag textareaTag;

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

    textareaTag = new TextareaTag();
    textareaTag.setPageContext(pageContext);
    if (value != null) {
      textareaTag.setValue(value);
    }
    if (valueChangeListener != null) {
      textareaTag.setValueChangeListener(valueChangeListener);
    }
    if (binding != null) {
      textareaTag.setBinding(binding);
    }
    if (converter != null) {
      textareaTag.setConverter(converter);
    }
    if (validator != null) {
      textareaTag.setValidator(validator);
    }
    if (onchange != null) {
      textareaTag.setOnchange(onchange);
    }
    if (disabled != null) {
      textareaTag.setDisabled(disabled);
    }
    if (focus != null) {
      textareaTag.setFocus(focus);
    }
    if (fieldId != null) {
      textareaTag.setId(fieldId);
    }
    if (label != null) {
      textareaTag.setLabel(label);
    }
    if (readonly != null) {
      textareaTag.setReadonly(readonly);
    }
    if (required != null) {
      textareaTag.setRequired(required);
    }
    if (markup != null) {
      textareaTag.setMarkup(markup);
    }
    if (tabIndex != null) {
      textareaTag.setTabIndex(tabIndex);
    }
    if (validatorMessage != null) {
      textareaTag.setValidatorMessage(validatorMessage);
    }
    if (converterMessage != null) {
      textareaTag.setConverterMessage(converterMessage);
    }
    if (requiredMessage != null) {
      textareaTag.setRequiredMessage(requiredMessage);
    }
    textareaTag.setParent(labelTag);
    textareaTag.setJspId(nextJspId());
    textareaTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    textareaTag.doEndTag();
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
    value = null;
    onchange = null;
    markup = null;
    valueChangeListener = null;
    tabIndex = null;
    textareaTag = null;
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
  @UIComponentTagAttribute(type = "java.lang.Object")
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
   * Flag indicating this component should receive the focus.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setFocus(final ValueExpression focus) {
    this.focus = focus;
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
   * Flag indicating whether or not this component should be rendered (during Render Response Phase), or processed on
   * any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  public void setRendered(final ValueExpression rendered) {
    this.rendered = rendered;
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
   * Clientside script function to add to this component's onchange handler.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setOnchange(final ValueExpression onchange) {
    this.onchange = onchange;
  }

  /**
   * Indicate markup of this component. Possible value is 'none'. But this can be overridden in the theme.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none", type = "java.lang.String[]")
  public void setMarkup(final ValueExpression markup) {
    this.markup = markup;
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
   * Flag indicating that a value is required. If the value is an empty string a ValidationError occurs and a Error
   * Message is rendered.
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
   * The width for the label component. Default: 'auto'. This value is used in the gridLayouts columns attribute. See
   * gridLayout tag for valid values.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabelWidth(final ValueExpression labelWidth) {
    this.labelWidth = labelWidth;
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

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
import org.apache.myfaces.tobago.internal.taglib.FileTag;

import javax.servlet.jsp.JspException;

/**
 * Renders a file input field with a label.
 * <p/>
 * Short syntax of:
 * <p/>
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="auto;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *   &lt;tc:file value="#{value}">
 *     ...
 *   &lt;/tc:in>
 * &lt;/tc:panel>
 * </pre>
 */

@Tag(name = "file")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.FileTag")
public class FileExtensionTag extends TobagoExtensionBodyTagSupport {

  private javax.el.ValueExpression binding;
  private javax.el.ValueExpression label;
  private javax.el.ValueExpression value;
  private javax.el.MethodExpression valueChangeListener;
  private javax.el.MethodExpression validator;
  private javax.el.ValueExpression disabled;
  private javax.el.ValueExpression rendered;
  private javax.el.ValueExpression tip;
  private javax.el.ValueExpression onchange;
  private javax.el.ValueExpression labelWidth;
  private javax.el.ValueExpression required;
  private javax.el.ValueExpression tabIndex;
  private javax.el.ValueExpression focus;
  private javax.el.ValueExpression validatorMessage;
  private javax.el.ValueExpression converterMessage;
  private javax.el.ValueExpression requiredMessage;
  private String fieldId;

  private LabelExtensionTag labelTag;
  private FileTag fileTag;

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
    if (tip != null) {
      labelTag.setTip(tip);
    }
    if (rendered != null) {
      labelTag.setRendered(rendered);
    }
    if (labelWidth != null) {
      labelTag.setLabelWidth(createStringValueExpression(labelWidth.getExpressionString() + ";*"));
    }
    labelTag.setParent(getParent());
    labelTag.setJspId(nextJspId());
    labelTag.doStartTag();

    fileTag = new FileTag();
    fileTag.setPageContext(pageContext);
    if (value != null) {
      fileTag.setValue(value);
    }
    if (valueChangeListener != null) {
      fileTag.setValueChangeListener(valueChangeListener);
    }
    if (binding != null) {
      fileTag.setBinding(binding);
    }
    if (validator != null) {
      fileTag.setValidator(validator);
    }
    if (disabled != null) {
      fileTag.setDisabled(disabled);
    }
    if (fieldId != null) {
      fileTag.setId(fieldId);
    }
    if (label != null) {
      fileTag.setLabel(label);
    }
    if (onchange != null) {
      fileTag.setOnchange(onchange);
    }
    if (required != null) {
      fileTag.setRequired(required);
    }
    if (tabIndex != null) {
      fileTag.setTabIndex(tabIndex);
    }
    if (focus != null) {
      //fileTag.set
    }
    if (validatorMessage != null) {
      fileTag.setValidatorMessage(validatorMessage);
    }
    if (converterMessage != null) {
      fileTag.setConverterMessage(converterMessage);
    }
    if (requiredMessage != null) {
      fileTag.setRequiredMessage(requiredMessage);
    }
    fileTag.setParent(labelTag);
    fileTag.setJspId(nextJspId());
    fileTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    fileTag.doEndTag();
    labelTag.doEndTag();
    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    binding = null;
    validator = null;
    disabled = null;
    label = null;
    labelWidth = null;
    tip = null;
    onchange = null;
    value = null;
    rendered = null;
    valueChangeListener = null;
    required = null;
    tabIndex = null;
    fileTag = null;
    labelTag = null;
    focus = null;
    validatorMessage = null;
    converterMessage = null;
    requiredMessage = null;
    fieldId = null;
  }

  /**
   * Text value to display as label.
   * If text contains an underscore the next character is used as accesskey.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabel(javax.el.ValueExpression label) {
    this.label = label;
  }

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setValue(javax.el.ValueExpression value) {
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
          expression = DynamicExpression.METHOD_BINDING_REQUIRED,
          methodSignature = "javax.faces.event.ValueChangeEvent")
  public void setValueChangeListener(javax.el.MethodExpression valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  /**
   * Clientside script function to add to this component's onchange handler.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setOnchange(javax.el.ValueExpression onchange) {
    this.onchange = onchange;
  }

  /**
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  public void setBinding(javax.el.ValueExpression binding) {
    this.binding = binding;
  }

  /**
   * Flag indicating whether or not this component should be rendered
   * (during Render Response Phase), or processed on any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  public void setRendered(javax.el.ValueExpression rendered) {
    this.rendered = rendered;
  }

  /**
   * A method binding EL expression,
   * accepting FacesContext, UIComponent,
   * and Object parameters, and returning void, that validates
   * the component's local value.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_BINDING,
      methodSignature = { "javax.faces.context.FacesContext", "javax.faces.component.UIComponent", "java.lang.Object" })
  public void setValidator(javax.el.MethodExpression validator) {
    this.validator = validator;
  }

  /**
   * Flag indicating that this element is disabled.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setDisabled(javax.el.ValueExpression disabled) {
    this.disabled = disabled;
  }

  /**
   * Text value to display as tooltip.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setTip(javax.el.ValueExpression tip) {
    this.tip = tip;
  }
   /**
   * The width for the label component. Default: 'auto'.
   * This value is used in the gridLayouts columns attribute.
   * See gridLayout tag for valid values.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabelWidth(javax.el.ValueExpression labelWidth) {
    this.labelWidth = labelWidth;
  }

  /**
   * Flag indicating that a value is required.
   * If the value is an empty string a
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setRequired(javax.el.ValueExpression required) {
    this.required = required;
  }

  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer")
  public void setTabIndex(javax.el.ValueExpression tabIndex) {
    this.tabIndex = tabIndex;
  }

  /**
   * Flag indicating this component should receive the focus.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setFocus(javax.el.ValueExpression focus) {
    this.focus = focus;
  }

  /**
   * An expression that specifies the validator message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setValidatorMessage(javax.el.ValueExpression validatorMessage) {
    this.validatorMessage = validatorMessage;
  }

  /**
   * An expression that specifies the converter message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setConverterMessage(javax.el.ValueExpression converterMessage) {
    this.converterMessage = converterMessage;
  }

  /**
   * An expression that specifies the required message
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setRequiredMessage(javax.el.ValueExpression requiredMessage) {
    this.requiredMessage = requiredMessage;
  }

  /**
   * The component identifier for the input field component inside of the container.
   * This value must be unique within the closest parent component that is a naming container.
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * The component identifier for this component.
   * This value must be unique within the closest parent component that is a naming container.
   * For tx components the id will be set to the container (e. g. the panel).
   * To set the id of the input field, you have to use the attribute "fieldId".
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setId(String id) {
    super.setId(id);
  }
}

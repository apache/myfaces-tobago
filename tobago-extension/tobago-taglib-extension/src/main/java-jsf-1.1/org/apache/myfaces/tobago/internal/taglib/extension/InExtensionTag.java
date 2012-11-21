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

import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.internal.taglib.InTag;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFieldId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelWidth;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasOnchange;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasSuggestMethod;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsPassword;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;

import javax.el.ValueExpression;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a text input field with a label.
 * <br />
 * Short syntax of:
 * <p/>
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="auto;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *   &lt;tc:in value="#{value}">
 *     ...
 *   &lt;/tc:in>
 * &lt;/tc:panel>
 * </pre>
 */

@Tag(name = "in")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.InTag")
public class InExtensionTag extends BodyTagSupport
    implements HasValue, HasValueChangeListener, HasValidator, HasIdBindingAndRendered,
    HasConverter, IsReadonly, IsDisabled, HasOnchange, HasMarkup, IsRequired,
    HasValidatorMessage, HasRequiredMessage, HasConverterMessage,
    HasTip, HasLabel, HasLabelWidth, IsPassword, IsFocus, HasSuggestMethod, HasTabIndex, HasFieldId {

  private String binding;
  private String converter;
  private String validator;
  private String disabled;
  private String focus;
  private String label;
  private String password;
  private String readonly;
  private String rendered;
  private String required;
  private String tip;
  private String value;
  private String valueChangeListener;
  private String onchange;
  private String suggestMethod;
  private String suggestMinChars;
  private String suggestDelay;
  private String markup;
  private String labelWidth;
  private String tabIndex;
  private String validatorMessage;
  private String converterMessage;
  private String requiredMessage;
  private String fieldId;

  private LabelExtensionTag labelTag;
  private InTag inTag;

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
      labelTag.setColumns(labelWidth + ";*");
    }
    if (markup != null) {
      labelTag.setMarkup(markup);
    }
    labelTag.setParent(getParent());
    labelTag.doStartTag();

    inTag = new InTag();
    inTag.setPageContext(pageContext);
    if (value != null) {
      inTag.setValue(value);
    }
    if (valueChangeListener != null) {
      inTag.setValueChangeListener(valueChangeListener);
    }
    if (binding != null) {
      inTag.setBinding(binding);
    }
    if (converter != null) {
      inTag.setConverter(converter);
    }
    if (validator != null) {
      inTag.setValidator(validator);
    }
    if (onchange != null) {
      inTag.setOnchange(onchange);
    }
    if (suggestMethod != null) {
      inTag.setSuggestMethod(suggestMethod);
    }
    if (suggestMinChars != null) {
      inTag.setSuggestMinChars(suggestMinChars);
    }
    if (suggestDelay != null) {
      inTag.setSuggestDelay(suggestDelay);
    }
    if (disabled != null) {
      inTag.setDisabled(disabled);
    }
    if (focus != null) {
      inTag.setFocus(focus);
    }
    if (fieldId != null) {
      inTag.setId(fieldId);
    }
    if (label != null) {
      inTag.setLabel(label);
    }
    if (password != null) {
      inTag.setPassword(password);
    }
    if (readonly != null) {
      inTag.setReadonly(readonly);
    }
    if (required != null) {
      inTag.setRequired(required);
    }
    if (markup != null) {
      inTag.setMarkup(markup);
    }
    if (tabIndex != null) {
      inTag.setTabIndex(tabIndex);
    }
    if (validatorMessage != null) {
      inTag.setValidatorMessage(validatorMessage);
    }
    if (converterMessage != null) {
      inTag.setConverterMessage(converterMessage);
    }
    if (requiredMessage != null) {
      inTag.setRequiredMessage(requiredMessage);
    }
    inTag.setParent(labelTag);
    inTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    inTag.doEndTag();
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
    password = null;
    readonly = null;
    rendered = null;
    required = null;
    tip = null;
    value = null;
    valueChangeListener = null;
    onchange = null;
    suggestMethod = null;
    suggestMinChars = null;
    suggestDelay = null;
    markup = null;
    tabIndex = null;
    inTag = null;
    labelTag = null;
    validatorMessage = null;
    converterMessage = null;
    requiredMessage = null;
    fieldId = null;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setValueChangeListener(String valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setFocus(String focus) {
    this.focus = focus;
  }

  public void setBinding(String binding) {
    this.binding = binding;
  }

  public void setRendered(String rendered) {
    this.rendered = rendered;
  }

  public void setConverter(String converter) {
    this.converter = converter;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public void setSuggestMethod(String suggestMethod) {
    this.suggestMethod = suggestMethod;
  }

  public void setSuggestMinChars(String suggestMinChars) {
    this.suggestMinChars = suggestMinChars;
  }

  public void setSuggestDelay(String suggestDelay) {
    this.suggestDelay = suggestDelay;
  }

  public void setValidator(String validator) {
    this.validator = validator;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setReadonly(String readonly) {
    this.readonly = readonly;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public void setRequired(String required) {
    this.required = required;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public void setLabelWidth(String labelWidth) {
    this.labelWidth = labelWidth;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }

  public void setValidatorMessage(String validatorMessage) {
    this.validatorMessage = validatorMessage;
  }

  public void setConverterMessage(String converterMessage) {
    this.converterMessage = converterMessage;
  }

  public void setRequiredMessage(String requiredMessage) {
    this.requiredMessage = requiredMessage;
  }

  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }
}

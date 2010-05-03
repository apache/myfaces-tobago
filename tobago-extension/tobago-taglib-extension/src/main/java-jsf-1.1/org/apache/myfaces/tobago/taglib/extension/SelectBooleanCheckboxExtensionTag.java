package org.apache.myfaces.tobago.taglib.extension;

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

import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.internal.taglib.SelectBooleanCheckboxTag;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.HasConverterMessage;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasLabelWidth;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasOnchange;
import org.apache.myfaces.tobago.taglib.decl.HasRequiredMessage;
import org.apache.myfaces.tobago.taglib.decl.HasTabIndex;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.HasValidatorMessage;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsFocus;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a checkbox.
 */
@Tag(name = "selectBooleanCheckbox")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.SelectBooleanCheckboxTag")
public class SelectBooleanCheckboxExtensionTag extends BodyTagSupport implements
    HasValidator, HasOnchange, HasValueChangeListener, HasIdBindingAndRendered, HasLabel,
    HasValidatorMessage, HasRequiredMessage, HasConverterMessage,
    HasBooleanValue, HasLabelWidth, IsDisabled, HasTip, IsReadonly, HasMarkup, HasTabIndex, IsRequired,
    IsFocus {

  private String value;
  private String valueChangeListener;
  private String disabled;
  private String readonly;
  private String onchange;
  private String label;
  private String itemLabel;
  private String rendered;
  private String binding;
  private String tip;
  private String converter;
  private String validator;
  private String labelWidth;
  private String markup;
  private String tabIndex;
  private String required;
  private String focus;
  private String validatorMessage;
  private String converterMessage;
  private String requiredMessage;

  private LabelExtensionTag labelTag;
  private SelectBooleanCheckboxTag selectBooleanCheckboxTag;

  @Override
  public int doStartTag() throws JspException {

    labelTag = new LabelExtensionTag();
    labelTag.setPageContext(pageContext);
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
    if (id != null) {
      selectBooleanCheckboxTag.setId(id);
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
    if (itemLabel != null) {
      selectBooleanCheckboxTag.setLabel(itemLabel);
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
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setValueChangeListener(String valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public void setReadonly(String readonly) {
    this.readonly = readonly;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setItemLabel(String itemLabel) {
    this.itemLabel = itemLabel;
  }

  public void setValidator(String validator) {
    this.validator = validator;
  }

  public void setConverter(String converter) {
    this.converter = converter;
  }

  public void setRendered(String rendered) {
    this.rendered = rendered;
  }

  public void setBinding(String binding) {
    this.binding = binding;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public void setLabelWidth(String labelWidth) {
    this.labelWidth = labelWidth;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }

  public void setFocus(String focus) {
    this.focus = focus;
  }

  public void setRequired(String required) {
    this.required = required;
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
}

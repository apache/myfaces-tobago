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
import org.apache.myfaces.tobago.internal.taglib.SelectOneRadioTag;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFieldId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelWidth;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasOnchange;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRenderRange;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsInline;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Render a set of radiobuttons.
 *
 * @since 1.0.13
 */

@Tag(name = "selectOneRadio")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.SelectOneRadioTag")
public class SelectOneRadioExtensionTag extends BodyTagSupport
    implements HasId, HasValue, HasValueChangeListener, IsDisabled,
    IsReadonly, HasOnchange, IsInline, HasLabel, HasLabelWidth, IsRequired,
    HasValidatorMessage, HasRequiredMessage, HasConverterMessage, HasMarkup,
    IsRendered, HasBinding, HasTip, HasValidator, HasConverter, HasRenderRange, HasTabIndex, HasFieldId {

  private String required;
  private String value;
  private String valueChangeListener;
  private String disabled;
  private String readonly;
  private String onchange;
  private String inline;
  private String label;
  private String rendered;
  private String binding;
  private String tip;
  private String validator;
  private String converter;
  private String labelWidth;
  private String tabIndex;
  private String focus;
  private String renderRange;
  private String validatorMessage;
  private String converterMessage;
  private String requiredMessage;
  private String markup;
  private String fieldId;

  private LabelExtensionTag labelTag;
  private SelectOneRadioTag selectOneRadioTag;

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

    selectOneRadioTag = new SelectOneRadioTag();
    selectOneRadioTag.setPageContext(pageContext);
    if (value != null) {
      selectOneRadioTag.setValue(value);
    }
    if (valueChangeListener != null) {
      selectOneRadioTag.setValueChangeListener(valueChangeListener);
    }
    if (validator != null) {
      selectOneRadioTag.setValidator(validator);
    }
    if (converter != null) {
      selectOneRadioTag.setConverter(converter);
    }
    if (binding != null) {
      selectOneRadioTag.setBinding(binding);
    }
    if (onchange != null) {
      selectOneRadioTag.setOnchange(onchange);
    }
    if (disabled != null) {
      selectOneRadioTag.setDisabled(disabled);
    }
    if (inline != null) {
      selectOneRadioTag.setInline(inline);
    }
    if (focus != null) {
      selectOneRadioTag.setFocus(focus);
    }
    if (fieldId != null) {
      selectOneRadioTag.setId(fieldId);
    }
    if (label != null) {
      selectOneRadioTag.setLabel(label);
    }
    if (readonly != null) {
      selectOneRadioTag.setReadonly(readonly);
    }
    if (required != null) {
      selectOneRadioTag.setRequired(required);
    }
    if (tabIndex != null) {
      selectOneRadioTag.setTabIndex(tabIndex);
    }
    if (renderRange != null) {
      selectOneRadioTag.setRenderRange(renderRange);
    }
    if (validatorMessage != null) {
      selectOneRadioTag.setValidatorMessage(validatorMessage);
    }
    if (converterMessage != null) {
      selectOneRadioTag.setConverterMessage(converterMessage);
    }
    if (requiredMessage != null) {
      selectOneRadioTag.setRequiredMessage(requiredMessage);
    }
    if (markup != null) {
      selectOneRadioTag.setMarkup(markup);
    }

    selectOneRadioTag.setParent(labelTag);
    selectOneRadioTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    selectOneRadioTag.doEndTag();
    labelTag.doEndTag();
    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    binding = null;
    onchange = null;
    disabled = null;
    inline = null;
    label = null;
    labelWidth = null;
    converter = null;
    validator = null;
    readonly = null;
    rendered = null;
    required = null;
    tip = null;
    value = null;
    valueChangeListener = null;
    tabIndex = null;
    selectOneRadioTag = null;
    labelTag = null;
    focus = null;
    renderRange = null;
    validatorMessage = null;
    converterMessage = null;
    requiredMessage = null;
    markup = null;
    fieldId = null;
  }

  public void setRequired(String required) {
    this.required = required;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setValueChangeListener(String valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  public void setValidator(String validator) {
    this.validator = validator;
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

  public void setConverter(String converter) {
    this.converter = converter;
  }

  public void setInline(String inline) {
    this.inline = inline;
  }

  public void setLabel(String label) {
    this.label = label;
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

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }

  public void setFocus(String focus) {
    this.focus = focus;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
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

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }
}

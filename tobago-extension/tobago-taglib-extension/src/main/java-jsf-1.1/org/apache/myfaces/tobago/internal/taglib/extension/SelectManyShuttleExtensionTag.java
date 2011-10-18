package org.apache.myfaces.tobago.internal.taglib.extension;

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
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFieldId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelWidth;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasOnchange;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRequiredMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTabIndex;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidator;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValidatorMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValueChangeListener;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsDisabled;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsFocus;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsReadonly;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a multi selection option shuttle with a label.
 */
@Tag(name = "selectManyShuttle")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.SelectManyShuttleTag")
public class SelectManyShuttleExtensionTag extends BodyTagSupport
    implements HasId, HasValue, HasValueChangeListener, IsDisabled,
    HasLabel, HasLabelWidth, IsRendered, HasBinding, HasTip, HasConverter, HasValidator, HasOnchange,
    HasValidatorMessage, HasRequiredMessage, HasConverterMessage,
    IsReadonly, HasMarkup, IsFocus, IsRequired, HasTabIndex, HasFieldId {

  private String required;
  private String value;
  private String valueChangeListener;
  private String disabled;
  private String readonly;
  private String onchange;
  private String label;
  private String unselectedLabel;
  private String selectedLabel;
  private String rendered;
  private String binding;
  private String tip;
  private String converter;
  private String validator;
  private String labelWidth;
  private String markup;
  private String tabIndex;
  private String focus;
  private String renderRange;
  private String validatorMessage;
  private String converterMessage;
  private String requiredMessage;
  private String fieldId;

  private LabelExtensionTag labelTag;
  private SelectManyShuttleTag selectManyShuttleTag;

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

    selectManyShuttleTag = new SelectManyShuttleTag();
    selectManyShuttleTag.setPageContext(pageContext);
    if (value != null) {
      selectManyShuttleTag.setValue(value);
    }
    if (valueChangeListener != null) {
      selectManyShuttleTag.setValueChangeListener(valueChangeListener);
    }
    if (binding != null) {
      selectManyShuttleTag.setBinding(binding);
    }
    if (onchange != null) {
      selectManyShuttleTag.setOnchange(onchange);
    }
    if (validator != null) {
      selectManyShuttleTag.setValidator(validator);
    }
    if (converter != null) {
      selectManyShuttleTag.setConverter(converter);
    }
    if (disabled != null) {
      selectManyShuttleTag.setDisabled(disabled);
    }
    if (focus != null) {
      selectManyShuttleTag.setFocus(focus);
    }
    if (fieldId != null) {
      selectManyShuttleTag.setId(fieldId);
    }
    if (label != null) {
      selectManyShuttleTag.setLabel(label);
    }
    if (unselectedLabel != null) {
      selectManyShuttleTag.setUnselectedLabel(unselectedLabel);
    }
    if (selectedLabel != null) {
      selectManyShuttleTag.setSelectedLabel(selectedLabel);
    }
    if (readonly != null) {
      selectManyShuttleTag.setReadonly(readonly);
    }
    if (required != null) {
      selectManyShuttleTag.setRequired(required);
    }
    if (markup != null) {
      selectManyShuttleTag.setMarkup(markup);
    }
    if (renderRange != null) {
      selectManyShuttleTag.setRenderRange(renderRange);
    }
    if (tabIndex != null) {
      selectManyShuttleTag.setTabIndex(tabIndex);
    }
    if (validatorMessage != null) {
      selectManyShuttleTag.setValidatorMessage(validatorMessage);
    }
    if (converterMessage != null) {
      selectManyShuttleTag.setConverterMessage(converterMessage);
    }
    if (requiredMessage != null) {
      selectManyShuttleTag.setRequiredMessage(requiredMessage);
    }

    selectManyShuttleTag.setParent(labelTag);
    selectManyShuttleTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    selectManyShuttleTag.doEndTag();
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
    unselectedLabel = null;
    selectedLabel = null;
    labelWidth = null;
    readonly = null;
    rendered = null;
    converter = null;
    validator = null;
    required = null;
    tip = null;
    value = null;
    valueChangeListener = null;
    markup = null;
    tabIndex = null;
    selectManyShuttleTag = null;
    labelTag = null;
    focus = null;
    renderRange = null;
    validatorMessage = null;
    converterMessage = null;
    requiredMessage = null;
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

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public void setReadonly(String readonly) {
    this.readonly = readonly;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public void setInline(String inline) {
    this.inline = inline;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * A localized user presentable label for the left select box.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setUnselectedLabel(String unselectedLabel) {
    this.unselectedLabel = unselectedLabel;
  }
  /**
   * A localized user presentable label for the right select box.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setSelectedLabel(String selectedLabel) {
    this.selectedLabel = selectedLabel;
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

  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

}

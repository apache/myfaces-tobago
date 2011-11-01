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
import org.apache.myfaces.tobago.internal.taglib.FileTag;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasConverterMessage;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFieldId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelWidth;
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
import org.apache.myfaces.tobago.internal.taglib.declaration.IsRequired;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

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
public class FileExtensionTag extends BodyTagSupport
    implements HasValidator, HasOnchange, HasValue, HasValueChangeListener,
    HasValidatorMessage, HasConverterMessage, HasRequiredMessage, HasTabIndex, IsFocus,
    HasIdBindingAndRendered, IsDisabled,
    HasTip, HasLabel, HasLabelWidth, IsRequired, HasFieldId {

  private String binding;
  private String label;
  private String value;
  private String valueChangeListener;
  private String validator;
  private String disabled;
  private String rendered;
  private String tip;
  private String onchange;
  private String labelWidth;
  private String required;
  private String tabIndex;
  private String focus;
  private String validatorMessage;
  private String converterMessage;
  private String requiredMessage;
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
      labelTag.setColumns(labelWidth + ";*");
    }
    labelTag.setParent(getParent());
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
    if (validatorMessage != null) {
      fileTag.setValidatorMessage(validatorMessage);
    }
    if (converterMessage != null) {
      fileTag.setConverterMessage(converterMessage);
    }
    if (requiredMessage != null) {
      fileTag.setRequiredMessage(requiredMessage);
    }

    if (focus != null) {
      //fileTag.set
    }
    fileTag.setParent(labelTag);
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

  public void setLabel(String label) {
    this.label = label;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setValueChangeListener(String valueChangeListener) {
    this.valueChangeListener = valueChangeListener;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public void setBinding(String binding) {
    this.binding = binding;
  }

  public void setRendered(String rendered) {
    this.rendered = rendered;
  }

  public void setValidator(String validator) {
    this.validator = validator;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public void setLabelWidth(String labelWidth) {
    this.labelWidth = labelWidth;
  }

  public void setRequired(String required) {
    this.required = required;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }

  public String getFocus() {
    return focus;
  }

  public void setFocus(String focus) {
    this.focus = focus;
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

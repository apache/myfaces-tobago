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

package org.apache.myfaces.tobago.taglib.extension;

import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.taglib.component.TextAreaTag;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasLabelWidth;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.decl.HasOnchange;
import org.apache.myfaces.tobago.taglib.decl.HasTabIndex;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsFocus;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a multiline text input control with a label.
 * <br />
 * Short syntax of:
 * <p/>
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="fixed;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *   &lt;tc:textarea value="#{value}">
 *     ...
 *   &lt;/tc:in>
 * &lt;/tc:panel>
 * </pre>
 */

@Tag(name = "textarea")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.component.TextAreaTag")
public class TextAreaExtensionTag extends BodyTagSupport
    implements HasValue, HasValueChangeListener, HasIdBindingAndRendered,
    HasConverter, HasValidator, IsReadonly, IsDisabled, HasMarkup, IsRequired,
    HasTip, HasLabel, HasLabelWidth, IsFocus, HasOnchange, HasTabIndex {

  private String binding;
  private String converter;
  private String disabled;
  private String focus;
  private String label;
  private String readonly;
  private String rendered;
  private String required;
  private String tip;
  private String value;
  private String valueChangeListener;
  private String validator;
  private String onchange;
  private String markup;
  private String labelWidth;
  private String tabIndex;

  private LabelExtensionTag labelTag;
  private TextAreaTag textAreaTag;

  @Override
  public int doStartTag() throws JspException {

    labelTag = new LabelExtensionTag();
    labelTag.setPageContext(pageContext);
    labelTag.setRows("*");
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

    textAreaTag = new TextAreaTag();
    textAreaTag.setPageContext(pageContext);
    if (value != null) {
      textAreaTag.setValue(value);
    }
    if (valueChangeListener != null) {
      textAreaTag.setValueChangeListener(valueChangeListener);
    }
    if (binding != null) {
      textAreaTag.setBinding(binding);
    }
    if (converter != null) {
      textAreaTag.setConverter(converter);
    }
    if (validator != null) {
      textAreaTag.setValidator(validator);
    }
    if (onchange != null) {
      textAreaTag.setOnchange(onchange);
    }
    if (disabled != null) {
      textAreaTag.setDisabled(disabled);
    }
    if (focus != null) {
      textAreaTag.setFocus(focus);
    }
    if (id != null) {
      textAreaTag.setId(id);
    }
    if (readonly != null) {
      textAreaTag.setReadonly(readonly);
    }
    if (required != null) {
      textAreaTag.setRequired(required);
    }
    if (markup != null) {
      textAreaTag.setMarkup(markup);
    }
    if (tabIndex != null) {
      textAreaTag.setTabIndex(tabIndex);
    }
    textAreaTag.setParent(labelTag);
    textAreaTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    textAreaTag.doEndTag();
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
    readonly = null;
    rendered = null;
    required = null;
    tip = null;
    value = null;
    onchange = null;
    markup = null;
    valueChangeListener = null;
    tabIndex = null;
    textAreaTag = null;
    labelTag = null;
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

  public void setValidator(String validator) {
    this.validator = validator;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
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
}

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
import org.apache.myfaces.tobago.taglib.component.TimeTag;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasLabelWidth;
import org.apache.myfaces.tobago.taglib.decl.HasOnchange;
import org.apache.myfaces.tobago.taglib.decl.HasTabIndex;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsFocus;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a time input field with a label.
 * <br />
 * Short syntax of:
 * <p/>
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="fixed;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *   &lt;tc:time value="#{value}">
 *     ...
 *   &lt;/tc:in>
 * &lt;/tc:panel>
 * </pre>
 */
@Tag(name = "time")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.component.TimeTag")
public class TimeExtensionTag extends BodyTagSupport
    implements HasValue, HasValueChangeListener, HasValidator, HasIdBindingAndRendered,
    HasConverter, IsReadonly, IsDisabled, HasOnchange, IsRequired, HasTip,
    HasLabel, HasLabelWidth, IsFocus, IsInline, HasTabIndex {

  private String binding;
  private String converter;
  private String validator;
  private String disabled;
  private String focus;
  private String label;
  private String readonly;
  private String rendered;
  private String required;
  private String tip;
  private String value;
  private String valueChangeListener;
  private String inline;
  private String onchange;
  private String labelWidth;
  private String tabIndex;

  private LabelExtensionTag labelTag;
  private TimeTag timeTag;

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
    labelTag.setParent(getParent());
    labelTag.doStartTag();

    timeTag = new TimeTag();
    timeTag.setPageContext(pageContext);
    if (value != null) {
      timeTag.setValue(value);
    }
    if (valueChangeListener != null) {
      timeTag.setValueChangeListener(valueChangeListener);
    }
    if (binding != null) {
      timeTag.setBinding(binding);
    }
    if (converter != null) {
      timeTag.setConverter(converter);
    }
    if (validator != null) {
      timeTag.setValidator(validator);
    }
    if (onchange != null) {
      timeTag.setOnchange(onchange);
    }
    if (disabled != null) {
      timeTag.setDisabled(disabled);
    }
    if (focus != null) {
      timeTag.setFocus(focus);
    }
    if (id != null) {
      timeTag.setId(id);
    }
    if (inline != null) {
      timeTag.setInline(inline);
    }
    if (readonly != null) {
      timeTag.setReadonly(readonly);
    }
    if (required != null) {
      timeTag.setRequired(required);
    }
    if (tabIndex != null) {
      timeTag.setTabIndex(tabIndex);
    }
    timeTag.setParent(labelTag);
    timeTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    timeTag.doEndTag();
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
    inline = null;
    readonly = null;
    rendered = null;
    required = null;
    tip = null;
    value = null;
    onchange = null;
    valueChangeListener = null;
    tabIndex = null;
    timeTag = null;
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

  public void setInline(String inline) {
    this.inline = inline;
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

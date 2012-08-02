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
import org.apache.myfaces.tobago.taglib.component.DatePickerTag;
import org.apache.myfaces.tobago.taglib.component.DateTag;
import org.apache.myfaces.tobago.taglib.component.FormTag;
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
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/*
 * Date: 19.12.2005
 * Time: 20:13:26
 */
/**
 * Renders a date input field with a date picker and a label.
 * <br />
 * Short syntax of:
 * <p/>
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="fixed;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *   &lt;tc:date value="#{value}">
 *     ...
 *   &lt;/tc:in>
 * &lt;/tc:panel>
 * </pre>
 */
@Tag(name = "date")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.component.DateTag")
public class DateExtensionTag extends BodyTagSupport
    implements HasValue, HasValueChangeListener, HasValidator, HasIdBindingAndRendered,
    HasConverter, IsReadonly, IsDisabled, HasOnchange, IsRequired, HasTip,
    HasLabel, HasMarkup, HasLabelWidth, IsFocus, IsInline, HasTabIndex {

  private static final long serialVersionUID = 2044784791513107420L;

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
  private String tabIndex;
  private String markup;

  private String labelWidth;
  private LabelExtensionTag labelTag;
  private DateTag dateTag;

  @Override
  public int doStartTag() throws JspException {

    labelTag = new LabelExtensionTag();
    labelTag.setPageContext(pageContext);
    if (label != null) {
      labelTag.setValue(label);
    }
    if (labelWidth != null) {
      labelTag.setColumns(labelWidth + ";*;fixed");
    } else {
      labelTag.setColumns("fixed;*;fixed");
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
    labelTag.doStartTag();

    dateTag = new DateTag();
    dateTag.setPageContext(pageContext);
    if (value != null) {
      dateTag.setValue(value);
    }
    if (valueChangeListener != null) {
      dateTag.setValueChangeListener(valueChangeListener);
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
    if (id != null) {
      dateTag.setId(id);
    }
    if (inline != null) {
      dateTag.setInline(inline);
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
    dateTag.setParent(labelTag);
    dateTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    dateTag.doEndTag();
    FormTag formTag = new FormTag();
    formTag.setPageContext(pageContext);
    formTag.setParent(labelTag);
    formTag.doStartTag();

    DatePickerTag datePicker = new DatePickerTag();
    datePicker.setPageContext(pageContext);
    datePicker.setFor("@auto");
    if (tabIndex != null) {
      datePicker.setTabIndex(tabIndex);
    }
    datePicker.setParent(formTag);
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
    inline = null;
    readonly = null;
    rendered = null;
    required = null;
    tip = null;
    value = null;
    valueChangeListener = null;
    onchange = null;
    markup = null;
    tabIndex = null;
    labelTag = null;
    dateTag = null;
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

  public void setOnchange(String onchange) {
    this.onchange = onchange;
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

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public void setTabIndex(String tabIndex) {
    this.tabIndex = tabIndex;
  }
}

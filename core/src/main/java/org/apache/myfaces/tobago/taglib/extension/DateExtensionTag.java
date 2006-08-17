package org.apache.myfaces.tobago.taglib.extension;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.taglib.component.DateTag;
import org.apache.myfaces.tobago.taglib.component.DatePickerTag;
import org.apache.myfaces.tobago.taglib.component.FormTag;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsFocus;
import org.apache.myfaces.tobago.taglib.decl.IsInline;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;
import org.apache.myfaces.tobago.taglib.decl.HasOnchange;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 19.12.2005
 * Time: 20:13:26
 */
/**
 * Renders a date input field with a date picker and a label.
 */
@Tag(name = "date")
public class DateExtensionTag extends BodyTagSupport
    implements HasValue, HasValueChangeListener, HasValidator, HasIdBindingAndRendered,
    HasConverter, IsReadonly, IsDisabled, HasOnchange,
    IsRequired, HasTip, HasLabel,  IsFocus, IsInline {

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


  private LabelExtensionTag labelTag;
  private DateTag dateTag;

  @Override
  public int doStartTag() throws JspException {

    labelTag = new LabelExtensionTag();
    labelTag.setPageContext(pageContext);
    if (label != null) {
      labelTag.setValue(label);
    }
    labelTag.setColumns("fixed;*;15px");
    if (tip != null) {
      labelTag.setTip(tip);
    }
    if (rendered != null) {
      labelTag.setRendered(rendered);
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
}

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.taglib.extension;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.taglib.component.InTag;
import org.apache.myfaces.tobago.taglib.decl.HasConverter;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsFocus;
import org.apache.myfaces.tobago.taglib.decl.IsPassword;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.IsRequired;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
@Tag(name="in")
public class InExtensionTag extends BodyTagSupport
    implements HasValue, HasValidator, HasIdBindingAndRendered,
    HasConverter, IsReadonly, IsDisabled,
    IsRequired, HasTip, HasLabel, IsPassword, IsFocus {

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

  private LabelExtensionTag labelTag;
  private InTag inTag;

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
    labelTag.setParent(getParent());
    labelTag.doStartTag();

    inTag = new InTag();
    inTag.setPageContext(pageContext);
    if (value != null) {
      inTag.setValue(value);
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
    if (disabled != null) {
      inTag.setDisabled(disabled);
    }
    if (focus != null) {
      inTag.setFocus(focus);
    }
    if (id != null) {
      inTag.setId(id);
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
    focus = null;
    label = null;
    password = null;
    readonly = null;
    rendered = null;
    required = null;
    tip = null;
    value = null;
  }

  public void setValue(String value) {
    this.value = value;
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
}

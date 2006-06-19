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
import org.apache.myfaces.tobago.taglib.component.FileTag;
import org.apache.myfaces.tobago.taglib.component.InputTagDeclaration;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a file input field with a label.
 */

@Tag(name = "file")
public class FileExtensionTag extends BodyTagSupport
    implements InputTagDeclaration, HasIdBindingAndRendered, IsDisabled,
    HasTip, HasLabel {

  private String binding;
  private String label;
  private String value;
  private String valueChangeListener;
  private String validator;
  private String disabled;
  private String rendered;
  private String tip;
  private String onchange;

  private LabelExtensionTag labelTag;
  private FileTag fileTag;

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
    if (id != null) {
      fileTag.setId(id);
    }
    if (onchange != null) {
      fileTag.setOnchange(onchange);
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
    tip = null;
    onchange = null;
    value = null;
    valueChangeListener = null;
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
}

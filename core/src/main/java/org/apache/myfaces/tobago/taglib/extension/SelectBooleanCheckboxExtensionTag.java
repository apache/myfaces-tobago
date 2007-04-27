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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasOnchange;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.HasValueChangeListener;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValidator;
import org.apache.myfaces.tobago.taglib.decl.IsReadonly;
import org.apache.myfaces.tobago.taglib.decl.HasLabelWidth;
import org.apache.myfaces.tobago.taglib.decl.HasMarkup;
import org.apache.myfaces.tobago.taglib.component.SelectBooleanCheckboxTag;
import org.apache.myfaces.tobago.taglib.component.TobagoTagDeclaration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Oct 7, 2006
 * Time: 9:13:21 AM
 */
/**
 * Renders a checkbox.
 */
@Tag(name = "selectBooleanCheckbox")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.component.SelectBooleanCheckboxTag")
public class SelectBooleanCheckboxExtensionTag extends BodyTagSupport implements TobagoTagDeclaration,
    HasValidator, HasOnchange, HasValueChangeListener, HasIdBindingAndRendered, HasLabel,
    HasBooleanValue, HasLabelWidth, IsDisabled, HasTip, IsReadonly, HasMarkup {

  private String value;
  private String valueChangeListener;
  private String disabled;
  private String readonly;
  private String onchange;
  private String label;
  private String rendered;
  private String binding;
  private String tip;
  private String converter;
  private String validator;
  private String labelWidth;
  private String markup;

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
    /* TODO accessKey
    if (labelWithAccessKey != null) {
      label.setLabelWithAccessKey(labelWithAccessKey);
    }
    if (accessKey !=null) {
      label.setAccessKey(accessKey);
    } */
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

    //if (required != null) {
    //  selectBooleanCheckboxTag.setRequired(required);
    //}

    if (markup != null) {
      selectBooleanCheckboxTag.setMarkup(markup);
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
    labelWidth = null;
    readonly = null;
    rendered = null;
    converter = null;
    validator = null;
    tip = null;
    value = null;
    valueChangeListener = null;
    markup = null;
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

}

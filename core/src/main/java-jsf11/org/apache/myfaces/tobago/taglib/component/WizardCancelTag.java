package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.internal.taglib.ButtonTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@Tag(name = "wizardCancel")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.component.WizardCancelTag")
public class WizardCancelTag extends BodyTagSupport {

  private String wizard;
  private String action;
  private String label;

  @Override
  public int doStartTag() throws JspException {

    int result = super.doStartTag();

    ButtonTag button = new ButtonTag();
    button.setPageContext(pageContext);
    button.setParent(getParent());
    button.setLabel(StringUtils.isEmpty(label) ? "Cancel Wizard" : label); // todo: i18n
    button.setAction(action);
    button.setActionListener("#{" + wizard + ".cancel}");
    button.setDisabled(Boolean.toString(action == null));
//    button.setDisabled(controller.replace("}", ".finishAvailable}").replace("#{", "#{!"));
    button.setImmediate(Boolean.TRUE.toString());
    button.doStartTag();
    button.doEndTag();

    return result;
  }

  @Override
  public void release() {
    super.release();
    wizard = null;
    action = null;
    label = null;
  }

  @TagAttribute(required = true)
  @UIComponentTagAttribute
  public void setWizard(String wizard) {
    this.wizard = wizard;
  }

  @TagAttribute
  @UIComponentTagAttribute
  public void setAction(String action) {
    this.action = action;
  }

  @TagAttribute
  @UIComponentTagAttribute
  public void setLabel(String label) {
    this.label = label;
  }
}

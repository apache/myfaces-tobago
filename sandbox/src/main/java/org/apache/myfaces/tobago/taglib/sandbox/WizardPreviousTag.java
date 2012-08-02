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

package org.apache.myfaces.tobago.taglib.sandbox;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.component.ButtonTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@Tag(name = "wizardPrevious")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.sandbox.WizardPreviousTag")
public class WizardPreviousTag extends BodyTagSupport {

  private String wizard;
  private String label;

  @Override
  public int doStartTag() throws JspException {

    int result = super.doStartTag();

    ButtonTag button = new ButtonTag();
    button.setPageContext(pageContext);
    button.setParent(getParent());
    button.setLabel(StringUtils.isEmpty(label) ? "Previous" : label); // todo: i18n
    button.setAction("#{" + wizard + ".previous}");
    button.setDisabled("#{!" + wizard + ".previousAvailable}");
    button.doStartTag();
    button.doEndTag();

    return result;
  }

  @Override
  public void release() {
    super.release();
    wizard = null;
    label = null;
  }

  @TagAttribute(required = true)
  @UIComponentTagAttribute
  public void setWizard(String wizard) {
    this.wizard = wizard;
  }

  @TagAttribute
  @UIComponentTagAttribute
  public void setLabel(String label) {
    this.label = label;
  }
}

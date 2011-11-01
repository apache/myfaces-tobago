package org.apache.myfaces.tobago.internal.taglib.component;

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
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.taglib.AttributeTag;
import org.apache.myfaces.tobago.internal.taglib.ButtonTag;
import org.apache.myfaces.tobago.internal.taglib.GridLayoutTag;
import org.apache.myfaces.tobago.internal.taglib.OutTag;
import org.apache.myfaces.tobago.internal.taglib.PanelTag;
import org.apache.myfaces.tobago.model.Wizard;
import org.apache.myfaces.tobago.model.WizardStep;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.List;

@Tag(name = "wizardTrain")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.component.WizardTrainTag")
public class WizardTrainTag extends BodyTagSupport {

  private ValueExpression controller;
  private ValueExpression wizard;

  @Override
  public int doStartTag() throws JspException {

    int result = super.doStartTag();

    FacesContext facesContext = FacesContext.getCurrentInstance();
    Wizard wizardObject = (Wizard) controller.getValue(facesContext.getELContext());
    List<WizardStep> course = wizardObject.getCourse();

    PanelTag panel = new PanelTag();
    panel.setPageContext(pageContext);
    panel.setParent(getParent());
    panel.doStartTag();

    FacetTag facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setName(Facets.LAYOUT);
    facetTag.setParent(panel);
    facetTag.doStartTag();

    GridLayoutTag gridLayoutTag = new GridLayoutTag();
    gridLayoutTag.setPageContext(pageContext);
    StringBuilder columns = new StringBuilder();
    for (WizardStep info : course) {
      columns.append("auto;");
    }
    gridLayoutTag.setColumns(createStringValueExpression(columns + "*"));
    gridLayoutTag.setParent(facetTag);
    gridLayoutTag.doStartTag();
    gridLayoutTag.doEndTag();

    facetTag.doEndTag();

    for (WizardStep info : course) {
      ButtonTag button = new ButtonTag();
      button.setPageContext(pageContext);
      button.setParent(panel);
//todo      button.setAction(info.getOutcome());
//todo      button.setActionListener("#{" + wizard + ".gotoStep}");
//todo      button.setLabel(info.getTitle());
      button.doStartTag();

      AttributeTag step = new AttributeTag();
      step.setPageContext(pageContext);
      step.setParent(button);
//todo      step.setName("step");
//todo      step.setValue("" + info.getIndex());
      step.doStartTag();
      step.doEndTag();

      button.doEndTag();
    }
    OutTag spacer = new OutTag();
    spacer.setPageContext(pageContext);
    spacer.setParent(panel);
//todo    spacer.setValue("#{" + wizard + ".index}");
    spacer.doStartTag();
    spacer.doEndTag();

    panel.doEndTag();

    return result;
  }

  protected ValueExpression createStringValueExpression(String expression) {
    return FacesContext.getCurrentInstance().getApplication().getExpressionFactory().
        createValueExpression(FacesContext.getCurrentInstance().getELContext(), expression, String.class);
  }

  @Override
  public void release() {
    super.release();
    controller = null;
    wizard = null;
  }

  @TagAttribute(required = true)
  @UIComponentTagAttribute
  public void setWizard(ValueExpression wizard) {
    this.wizard = wizard;
  }

  @TagAttribute(required = true)
  @UIComponentTagAttribute
  public void setController(ValueExpression controller) {
    this.controller = controller;
  }
}

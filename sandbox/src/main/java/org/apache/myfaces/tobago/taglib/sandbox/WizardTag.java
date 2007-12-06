package org.apache.myfaces.tobago.taglib.sandbox;

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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.model.AbstractWizardController;
import org.apache.myfaces.tobago.model.Wizard;
import org.apache.myfaces.tobago.taglib.component.AttributeTag;
import org.apache.myfaces.tobago.taglib.component.ButtonTag;
import org.apache.myfaces.tobago.taglib.component.CellTag;
import org.apache.myfaces.tobago.taglib.component.GridLayoutTag;
import org.apache.myfaces.tobago.taglib.component.OutTag;
import org.apache.myfaces.tobago.taglib.component.PanelTag;
import org.apache.myfaces.tobago.util.VariableResolverUtil;

import javax.faces.context.FacesContext;
import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.List;

@Tag(name = "wizard")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.sandbox.WizardTag")
public class WizardTag extends BodyTagSupport {

  private static final Log LOG = LogFactory.getLog(WizardTag.class);

  private String controller;

  private String next;
  private String outcome;
  private String title;

  private PanelTag panelTag;

  @Override
  public int doStartTag() throws JspException {

    panelTag = new PanelTag();
    panelTag.setPageContext(pageContext);
    panelTag.setParent(getParent());
/* todo
    if (rendered != null) {
      panelTag.setRendered(rendered);
    }
*/
    panelTag.doStartTag();

    FacetTag facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setName(FACET_LAYOUT);
    facetTag.setParent(panelTag);
    facetTag.doStartTag();

    GridLayoutTag gridLayoutTag = new GridLayoutTag();
    gridLayoutTag.setPageContext(pageContext);
    gridLayoutTag.setRows("fixed;*;fixed");
    gridLayoutTag.setParent(facetTag);
    gridLayoutTag.doStartTag();
    gridLayoutTag.doEndTag();

    facetTag.doEndTag();

    processTrain();

    return super.doStartTag();
  }

  private void processTrain() throws JspException {

    List<AbstractWizardController.Info> course = null;
    try {
      Object bean = VariableResolverUtil.resolveVariable(FacesContext.getCurrentInstance(), "controller");
      Wizard wizard = (Wizard) PropertyUtils.getProperty(bean, "wizard");
      wizard.registerOutcome(outcome, title);
      course = wizard.getCourse();
    } catch (Exception e) {
      LOG.error("", e);
    }

    PanelTag p = new PanelTag();
    p.setPageContext(pageContext);
    p.setParent(panelTag);
    p.doStartTag();

    FacetTag facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setName(FACET_LAYOUT);
    facetTag.setParent(panelTag);
    facetTag.doStartTag();

    GridLayoutTag gridLayoutTag = new GridLayoutTag();
    gridLayoutTag.setPageContext(pageContext);
//    gridLayoutTag.setColumns("*");
    StringBuilder columns = new StringBuilder();
    for (AbstractWizardController.Info info : course) {
      columns.append("fixed;");
    }
    gridLayoutTag.setColumns(columns + "*");
    gridLayoutTag.setParent(facetTag);
    gridLayoutTag.doStartTag();
    gridLayoutTag.doEndTag();

    facetTag.doEndTag();

    for (AbstractWizardController.Info info : course) {
      ButtonTag button = new ButtonTag();
      button.setPageContext(pageContext);
      button.setParent(p);
      button.setAction(info.getOutcome());
      button.setActionListener(controller.replace("}", ".gotoStep}"));
      button.setLabel(info.getTitle());
      button.doStartTag();

      AttributeTag step = new AttributeTag();
      step.setPageContext(pageContext);
      step.setParent(button);
      step.setName("step");
      step.setValue("" + info.getIndex());
      step.doStartTag();
      step.doEndTag();

      button.doEndTag();
    }
    OutTag spacer = new OutTag();
    spacer.setPageContext(pageContext);
    spacer.setParent(p);
    spacer.setValue(controller.replace("}", ".index}"));
    spacer.doStartTag();
    spacer.doEndTag();

    p.doEndTag();
  }

  @Override
  public int doEndTag() throws JspException {

    PanelTag p = new PanelTag();
    p.setPageContext(pageContext);
    p.setParent(panelTag);
    p.doStartTag();

    FacetTag facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setName(FACET_LAYOUT);
    facetTag.setParent(panelTag);
    facetTag.doStartTag();

    GridLayoutTag gridLayoutTag = new GridLayoutTag();
    gridLayoutTag.setPageContext(pageContext);
    gridLayoutTag.setColumns("*;fixed;fixed;fixed");
    gridLayoutTag.setParent(facetTag);
    gridLayoutTag.doStartTag();
    gridLayoutTag.doEndTag();

    facetTag.doEndTag();

    cell(panelTag);

/*
    WizardControllerTag controllerTag = new WizardControllerTag();
    controllerTag.setPageContext(pageContext);
    controllerTag.setParent(panelTag);
    controllerTag.setController(controller);
    controllerTag.doStartTag();
    controllerTag.doEndTag();
*/

    ButtonTag previousTag = new ButtonTag();
    previousTag.setPageContext(pageContext);
    previousTag.setParent(panelTag);
    previousTag.setLabel("Previous");
    previousTag.setAction(controller.replace("}", ".previous}"));
    previousTag.setDisabled(controller.replace("}", ".previousAvailable}").replace("#{", "#{!"));
    previousTag.doStartTag();
    previousTag.doEndTag();

    ButtonTag nextTag = new ButtonTag();
    nextTag.setPageContext(pageContext);
    nextTag.setParent(panelTag);
    nextTag.setLabel("Next");
    nextTag.setAction(next);
    nextTag.setActionListener(controller.replace("}", ".next}"));
    nextTag.setDisabled(controller.replace("}", ".nextAvailable}").replace("#{", "#{!"));
    nextTag.doStartTag();
    nextTag.doEndTag();

    ButtonTag finish = new ButtonTag();
    finish.setPageContext(pageContext);
    finish.setParent(panelTag);
    finish.setLabel("Finish");
    finish.setAction(controller.replace("}", ".finish}"));
    finish.setDisabled(controller.replace("}", ".finishAvailable}").replace("#{", "#{!"));
    finish.doStartTag();
    finish.doEndTag();

    p.doEndTag();

    panelTag.doEndTag();

    return super.doEndTag();
  }

  private void cell(javax.servlet.jsp.tagext.Tag tag) throws JspException {
    CellTag spacer = new CellTag();
    spacer.setPageContext(pageContext);
    spacer.setParent(tag);
    spacer.doStartTag();
    spacer.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    controller = null;
    next = null;
    outcome = null;
    title = null;
    panelTag = null;
  }

  @TagAttribute(required = true, type = Wizard.class)
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.model.Wizard")
  public void setController(String controller) {
    this.controller = controller;
  }

  @TagAttribute
  @UIComponentTagAttribute
  public void setNext(String next) {
    this.next = next;
  }

  @TagAttribute
  @UIComponentTagAttribute
  public void setOutcome(String outcome) {
    this.outcome = outcome;
  }

  @TagAttribute
  @UIComponentTagAttribute
  public void setTitle(String title) {
    this.title = title;
  }

}

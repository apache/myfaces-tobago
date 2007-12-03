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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.model.Wizard;
import org.apache.myfaces.tobago.taglib.component.ButtonTag;
import org.apache.myfaces.tobago.taglib.component.CellTag;
import org.apache.myfaces.tobago.taglib.component.GridLayoutTag;
import org.apache.myfaces.tobago.taglib.component.PanelTag;

import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@Tag(name = "wizard")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.sandbox.WizardTag")
public class WizardTag extends BodyTagSupport {

  private static final Log LOG = LogFactory.getLog(WizardTag.class);

  private String controller;

  private String next;
  private String previous;

  private PanelTag panelTag;

  private CellTag cellTag;

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
//    gridLayoutTag.setColumns("*;fixed;fixed;fixed;fixed");
    gridLayoutTag.setColumns("*;fixed;fixed;fixed");
    gridLayoutTag.setRows("*;fixed");
    gridLayoutTag.setParent(facetTag);
    gridLayoutTag.doStartTag();
    gridLayoutTag.doEndTag();

    facetTag.doEndTag();

    cellTag = new CellTag();
    cellTag.setPageContext(pageContext);
    cellTag.setParent(panelTag);
    cellTag.setSpanX("5");
    cellTag.doStartTag();

/*
    Object bean = VariableResolverUtil.resolveVariable(FacesContext.getCurrentInstance(), "controller");
    int index = 0;
    try {
      Wizard wizard = (Wizard) PropertyUtils.getProperty(bean, "wizard");
      index = wizard.getIndex();
    } catch (Exception e) {
      LOG.error("", e);
    }

    if (index < 2) {
      IncludeTag content = new IncludeTag();
      content.setPageContext(pageContext);
      content.setParent(cellTag);
      content.setValue("snip-" + index + ".jsp");
      content.doStartTag();
      content.doEndTag();
    } else {
      PanelTag content = new PanelTag();
      content.setPageContext(pageContext);
      content.setParent(cellTag);
      content.setBinding(controller.replace("}", ".currentComponent}"));
      content.doStartTag();
      content.doEndTag();
    }
*/
    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {

    cellTag.doEndTag();

    CellTag spacer = new CellTag();
    spacer.setPageContext(pageContext);
    spacer.setParent(panelTag);
    spacer.doStartTag();
    spacer.doEndTag();

/*
    WizardControllerTag controllerTag = new WizardControllerTag();
    controllerTag.setPageContext(pageContext);
    controllerTag.setParent(panelTag);
    controllerTag.setController(controller);
    controllerTag.doStartTag();
    controllerTag.doEndTag();
*/

    // XXX remove start? using process train instead
/*
    ButtonTag start = new ButtonTag();
    start.setPageContext(pageContext);
    start.setParent(panelTag);
    start.setLabel("Start");
    start.setAction(controller.replace("}", ".start}"));
    start.setDisabled(controller.replace("}", ".startAvailable}").replace("#{", "#{!"));
    start.doStartTag();
    start.doEndTag();
*/

    ButtonTag previousTag = new ButtonTag();
    previousTag.setPageContext(pageContext);
    previousTag.setParent(panelTag);
    previousTag.setLabel("Previous");
//    previous.setAction(controller.replace("}", ".previous}"));
    if (previous != null) {
      previousTag.setAction(previous);
      previousTag.setActionListener(controller.replace("}", ".previous}"));
    } else {
      previousTag.setAction(controller.replace("}", ".previous}"));
    }
    previousTag.setDisabled(controller.replace("}", ".previousAvailable}").replace("#{", "#{!"));
    previousTag.doStartTag();
    previousTag.doEndTag();

    ButtonTag nextTag = new ButtonTag();
    nextTag.setPageContext(pageContext);
    nextTag.setParent(panelTag);
    nextTag.setLabel("Next");
//    nextTag.setAction(controller.replace("}", ".next}"));
    if (next != null) {
      nextTag.setAction(next);
      nextTag.setActionListener(controller.replace("}", ".next}"));
    } else {
      nextTag.setAction(controller.replace("}", ".next}"));
    }
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

    panelTag.doEndTag();

    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    controller = null;
    next = null;
    previous = null;
    panelTag = null;
    cellTag = null;
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
  public void setPreviousNext(String previous) {
    this.previous = previous;
  }
}

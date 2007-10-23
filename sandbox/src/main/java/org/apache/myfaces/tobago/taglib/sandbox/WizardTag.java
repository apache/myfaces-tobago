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
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIWizard;
import org.apache.myfaces.tobago.model.Wizard;
import org.apache.myfaces.tobago.taglib.component.ButtonTag;
import org.apache.myfaces.tobago.taglib.component.CellTag;
import org.apache.myfaces.tobago.taglib.component.GridLayoutTag;
import org.apache.myfaces.tobago.taglib.component.IncludeTag;
import org.apache.myfaces.tobago.taglib.component.PanelTag;
import org.apache.myfaces.tobago.taglib.component.TobagoTag;
import org.apache.myfaces.tobago.util.VariableResolverUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;

public class WizardTag extends TobagoTag implements WizardTagDeclaration {

  private static final Log LOG = LogFactory.getLog(WizardTag.class);

  private String controller;

  private PanelTag panelTag;

  @Override
  public String getComponentType() {
    return UIWizard.COMPONENT_TYPE;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    // xxx
    //ComponentUtil.setStringProperty(component, ATTR_CONTROLLER, controller);
    ComponentUtil.setStringProperty(component, "controller", controller);
  }

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
    gridLayoutTag.setColumns("*;fixed;fixed;fixed;fixed");
    gridLayoutTag.setRows("*;fixed");
    gridLayoutTag.setParent(facetTag);
    gridLayoutTag.doStartTag();
    gridLayoutTag.doEndTag();

    facetTag.doEndTag();

    {
      CellTag cell = new CellTag();
      cell.setPageContext(pageContext);
      cell.setParent(panelTag);
      cell.setSpanX("5");
      cell.doStartTag();

      {
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
          content.setParent(cell);
          content.setValue("snip-" + index + ".jsp");
          content.doStartTag();
          content.doEndTag();
        } else {
          PanelTag content = new PanelTag();
          content.setPageContext(pageContext);
          content.setParent(cell);
          content.setBinding(controller.replace("}", ".currentComponent}"));
          content.doStartTag();
          content.doEndTag();
        }
      }

      cell.doEndTag();
    }

    {
      CellTag spacer = new CellTag();
      spacer.setPageContext(pageContext);
      spacer.setParent(panelTag);
      spacer.doStartTag();
      spacer.doEndTag();
    }

    {
      ButtonTag start = new ButtonTag();
      start.setPageContext(pageContext);
      start.setParent(panelTag);
      start.setLabel("Start");
      start.setAction(controller.replace("}", ".start}"));
      start.setDisabled(controller.replace("}", ".startAvailable}").replace("#{", "#{!"));
      start.doStartTag();
      start.doEndTag();
    }

    {
      ButtonTag previous = new ButtonTag();
      previous.setPageContext(pageContext);
      previous.setParent(panelTag);
      previous.setLabel("Previous");
      previous.setAction(controller.replace("}", ".previous}"));
      previous.setDisabled(controller.replace("}", ".previousAvailable}").replace("#{", "#{!"));
      previous.doStartTag();
      previous.doEndTag();
    }

    {
      ButtonTag next = new ButtonTag();
      next.setPageContext(pageContext);
      next.setParent(panelTag);
      next.setLabel("Next");
      next.setAction(controller.replace("}", ".next}"));
      next.setDisabled(controller.replace("}", ".nextAvailable}").replace("#{", "#{!"));
      next.doStartTag();
      next.doEndTag();
    }

    {
      ButtonTag finish = new ButtonTag();
      finish.setPageContext(pageContext);
      finish.setParent(panelTag);
      finish.setLabel("Finish");
      finish.setAction(controller.replace("}", ".finish}"));
      finish.setDisabled(controller.replace("}", ".finishAvailable}").replace("#{", "#{!"));
      finish.doStartTag();
      finish.doEndTag();
    }

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    panelTag.doEndTag();
    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    controller = null;
    panelTag = null;
  }

  public String getController() {
    return controller;
  }

  public void setController(String controller) {
    this.controller = controller;
  }
}
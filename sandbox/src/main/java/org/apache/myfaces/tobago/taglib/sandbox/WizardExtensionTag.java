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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.component.GridLayoutTag;
import org.apache.myfaces.tobago.taglib.component.PanelTag;

import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@Tag(name = "tx_wizard")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.sandbox.WizardExtensionTag")
public class WizardExtensionTag extends BodyTagSupport {

  private static final Log LOG = LogFactory.getLog(WizardExtensionTag.class);

  private String controller;

  private String outcome;
  private String title;
  private String allowJumpForward;

  private WizardTag controllerTag;
  private PanelTag panelTag;

  @Override
  public int doStartTag() throws JspException {

    controllerTag = new WizardTag();
    controllerTag.setPageContext(pageContext);
    controllerTag.setParent(getParent());
    controllerTag.setController(controller);

    controllerTag.setOutcome(outcome);
    controllerTag.setTitle(title);
    controllerTag.setAllowJumpForward(allowJumpForward);

    // todo: change "w" to ? or make it configurable or settable over this tag? Whould this be helpful?
    controllerTag.setVar("w");

    controllerTag.doStartTag();

    panelTag = new PanelTag();
    panelTag.setPageContext(pageContext);
    panelTag.setParent(controllerTag);
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

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {

    panelTag.doEndTag();

    controllerTag.doEndTag();

    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    controller = null;
    outcome = null;
    title = null;
    panelTag = null;
  }

  @TagAttribute(required = true)
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.model.Wizard")
  public void setController(String controller) {
    this.controller = controller;
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

  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Boolean")
  public void setAllowJumpForward(String allowJumpForward) {
    this.allowJumpForward = allowJumpForward;
  }
}

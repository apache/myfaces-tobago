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
import org.apache.myfaces.tobago.taglib.component.GridLayoutTag;
import org.apache.myfaces.tobago.taglib.component.LabelTag;
import org.apache.myfaces.tobago.taglib.component.PanelTag;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;

import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@Tag(name = "label")
public class LabelExtensionTag extends BodyTagSupport
    implements HasValue, HasTip {

  private String value;
  private String tip;
  private String rendered;
  private String columns = "fixed;*";

  private PanelTag panelTag;

  @Override
  public int doStartTag() throws JspException {

    panelTag = new PanelTag();
    panelTag.setPageContext(pageContext);
    panelTag.setParent(getParent());
    if (rendered != null) {
      panelTag.setRendered(rendered);
    }
    panelTag.doStartTag();

    FacetTag facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setName(FACET_LAYOUT);
    facetTag.setParent(panelTag);
    facetTag.doStartTag();

    GridLayoutTag gridLayoutTag = new GridLayoutTag();
    gridLayoutTag.setPageContext(pageContext);
    gridLayoutTag.setColumns(columns);
    gridLayoutTag.setParent(facetTag);
    gridLayoutTag.doStartTag();
    gridLayoutTag.doEndTag();

    facetTag.doEndTag();

    LabelTag labelTag = new LabelTag();
    labelTag.setPageContext(pageContext);
    if (value != null) {
      labelTag.setValue(value);
    }
    if (tip != null) {
      labelTag.setTip(tip);
    }
    labelTag.setFor("@auto");
    labelTag.setParent(panelTag);
    labelTag.doStartTag();
    labelTag.doEndTag();

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
    value = null;
    tip = null;
    rendered = null;
    columns = "fixed;*";
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public void setRendered(String rendered) {
    this.rendered = rendered;
  }

  void setColumns(String columns) {
    this.columns = columns;
  }
}

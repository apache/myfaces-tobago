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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.taglib.component.GridLayoutTag;
import org.apache.myfaces.tobago.taglib.component.LabelTag;
import org.apache.myfaces.tobago.taglib.component.PanelTag;
import org.apache.myfaces.tobago.taglib.decl.HasTip;
import org.apache.myfaces.tobago.taglib.decl.HasValue;
import org.apache.myfaces.tobago.util.LayoutUtil;

import javax.faces.webapp.FacetTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@Tag(name = "label")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.taglib.component.LabelTag")
public class LabelExtensionTag extends BodyTagSupport
    implements HasValue, HasTip {

  private static final Log LOG = LogFactory.getLog(LabelExtensionTag.class);

  public static final String DEFAULT_COLUMNS = "fixed;*";

  private String value;
  private String tip;
  private String rendered;
  private String columns = DEFAULT_COLUMNS;
  private String rows = "fixed";

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
    gridLayoutTag.setRows(rows);
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
    columns = DEFAULT_COLUMNS;
    rows = "fixed";
    panelTag = null;
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
    if (!(UIComponentTag.isValueReference(columns) || LayoutUtil.checkTokens(columns))) {
      LOG.warn("Illegal value for columns = \"" + columns + "\" replacing with default: \"" + DEFAULT_COLUMNS + "\"");
      this.columns = DEFAULT_COLUMNS;
    } else {
      this.columns = columns;
    }
  }

  void setRows(String rows) {
    this.rows = rows;
  }
}

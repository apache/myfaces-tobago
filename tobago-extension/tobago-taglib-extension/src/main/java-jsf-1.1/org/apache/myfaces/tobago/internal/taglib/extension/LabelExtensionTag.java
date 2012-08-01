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

package org.apache.myfaces.tobago.internal.taglib.extension;

import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.internal.taglib.GridLayoutTag;
import org.apache.myfaces.tobago.internal.taglib.LabelTag;
import org.apache.myfaces.tobago.internal.taglib.PanelTag;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabelWidth;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasTip;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.webapp.FacetTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a label to any component.
 * <br />
 * Short syntax of:
 * <br />
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="auto;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *     ...
 * &lt;/tc:panel>
 * </pre>
 * This is the universal version of the special versions: &lt;tx:in>, etc.
 * In other words:
 * <pre>
 * &lt;tx:label>
 *   &lt;tc:in/>
 * &lt;/tx:label>
 * </pre>
 * does the same like
 * <pre>
 *   &lt;tx:in/>
 * </pre>
 */

@Tag(name = "label")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.LabelTag")
public class LabelExtensionTag extends BodyTagSupport
    implements HasValue, HasLabelWidth, HasTip {

  private static final Logger LOG = LoggerFactory.getLogger(LabelExtensionTag.class);

  public static final String DEFAULT_COLUMNS = "auto;*";

  private String value;
  private String tip;
  private String rendered;
  private String columns = DEFAULT_COLUMNS;
  private String rows = "auto";
  private String labelWidth;
  private String markup;

  private ExtensionPanelTag panelTag;

  @Override
  public int doStartTag() throws JspException {

    panelTag = new ExtensionPanelTag();
    panelTag.setPageContext(pageContext);
    panelTag.setParent(getParent());
    if (rendered != null) {
      panelTag.setRendered(rendered);
    }
    if (tip != null) {
      panelTag.setTip(tip);
    }
    if (id != null) {
      panelTag.setId(id);
    }
    panelTag.doStartTag();

    FacetTag facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setName(Facets.LAYOUT);
    facetTag.setParent(panelTag);
    facetTag.doStartTag();

    if (labelWidth != null) {
      setColumns(labelWidth + ";*");
    }

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
    if (markup != null) {
      labelTag.setMarkup(markup);
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
    rows = "auto";
    panelTag = null;
    labelWidth = null;
    markup = null;
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
    if (!(UIComponentTag.isValueReference(columns) || LayoutUtils.checkTokens(columns))) {
      LOG.warn("Illegal value for columns = \"" + columns + "\" replacing with default: \"" + DEFAULT_COLUMNS + "\"");
      this.columns = DEFAULT_COLUMNS;
    } else {
      this.columns = columns;
    }
  }

  void setRows(String rows) {
    this.rows = rows;
  }

  public void setLabelWidth(String labelWidth) {
    this.labelWidth = labelWidth;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }
}

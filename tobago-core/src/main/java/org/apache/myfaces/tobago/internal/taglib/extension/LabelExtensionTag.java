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
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.internal.taglib.GridLayoutTag;
import org.apache.myfaces.tobago.internal.taglib.LabelTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;

/**
 * Renders a label to any component. <br /> Short syntax of: <br />
 * <pre>
 * &lt;tc:panel>
 *   &lt;f:facet name="layout">
 *     &lt;tc:gridLayout columns="auto;*"/>
 *   &lt;/f:facet>
 *   &lt;tc:label value="#{label}" for="@auto"/>
 *     ...
 * &lt;/tc:panel>
 * </pre>
 * This is the universal version of the special versions: &lt;tx:in>, etc. In other words:
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
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.LabelTag",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.LabelExtensionHandler")
public class LabelExtensionTag extends TobagoExtensionBodyTagSupport {

  private static final Logger LOG = LoggerFactory.getLogger(LabelExtensionTag.class);

  public static final String DEFAULT_COLUMNS = "auto;*";

  private javax.el.ValueExpression value;
  private javax.el.ValueExpression accessKey;
  private javax.el.ValueExpression tip;
  private javax.el.ValueExpression rendered;
  private javax.el.ValueExpression columns;
  private String rows = "auto";
  private javax.el.ValueExpression labelWidth;
  private javax.el.ValueExpression markup;

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
    panelTag.setJspId(nextJspId());
    panelTag.doStartTag();

    final FacetTag facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setName(Facets.LAYOUT);
    facetTag.setParent(panelTag);
    facetTag.doStartTag();

    if (columns == null) {
      if (labelWidth != null) {
        setColumns(createStringValueExpression(labelWidth.getExpressionString() + ";*"));
      } else {
        setColumns(createStringValueExpression(DEFAULT_COLUMNS));
      }
    }
    final GridLayoutTag gridLayoutTag = new GridLayoutTag();
    gridLayoutTag.setPageContext(pageContext);
    gridLayoutTag.setColumns(columns);
    final javax.el.ValueExpression ve = createStringValueExpression(rows);
    gridLayoutTag.setRows(ve);
    gridLayoutTag.setParent(facetTag);
    gridLayoutTag.setJspId(nextJspId());
    gridLayoutTag.doStartTag();
    gridLayoutTag.doEndTag();

    facetTag.doEndTag();

    final LabelTag labelTag = new LabelTag();
    labelTag.setPageContext(pageContext);
    if (value != null) {
      labelTag.setValue(value);
    }
    if (accessKey != null) {
      labelTag.setAccessKey(accessKey);
    }
    if (markup != null) {
      labelTag.setMarkup(markup);
    }
    labelTag.setFor("@auto");
    labelTag.setParent(panelTag);
    labelTag.setJspId(nextJspId());
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
    accessKey = null;
    tip = null;
    rendered = null;
    columns = null;
    rows = "auto";
    panelTag = null;
    labelWidth = null;
    markup = null;
  }

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setValue(final javax.el.ValueExpression value) {
    this.value = value;
  }

  /**
   * The accessKey of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Character")
  public void setAccessKey(final javax.el.ValueExpression accessKey) {
    this.accessKey = accessKey;
  }

  /**
   * Text value to display as tooltip.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setTip(final javax.el.ValueExpression tip) {
    this.tip = tip;
  }

  /**
   * Flag indicating whether or not this component should be rendered (during Render Response Phase), or processed on
   * any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  public void setRendered(final javax.el.ValueExpression rendered) {
    this.rendered = rendered;
  }

  void setColumns(final javax.el.ValueExpression columns) {
    if (!(!columns.isLiteralText() || LayoutUtils.checkTokens(columns.getExpressionString()))) {
      LOG.warn("Illegal value for columns = \"" + columns.getExpressionString()
          + "\" replacing with default: \"" + DEFAULT_COLUMNS + "\"");
      this.columns = createStringValueExpression(DEFAULT_COLUMNS);
    } else {
      this.columns = columns;
    }
  }

  void setRows(final String rows) {
    this.rows = rows;
  }

  /**
   * The width for the label component. Default: 'auto'. This value is used in the gridLayouts columns attribute. See
   * gridLayout tag for valid values.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabelWidth(final ValueExpression labelWidth) {
    this.labelWidth = labelWidth;
  }

  /**
   * Indicate markup of this component. Possible value is 'none'. But this can be overridden in the theme.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none", type = "java.lang.String[]")
  public void setMarkup(final javax.el.ValueExpression markup) {
    this.markup = markup;
  }
}

package org.apache.myfaces.tobago.internal.taglib.extension;

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
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.taglib.LabelTag;
import org.apache.myfaces.tobago.internal.taglib.SeparatorTag;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasLabel;

import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Renders a separator.
 * <br />
 * Short syntax of:
 * <p/>
 * <pre>
 * &lt;tc:separator>
 *   &lt;f:facet name="label">
 *     &lt;tc:label value="label"/>
 *   &lt;/f:facet>
 * &lt;/tc:separator>
 * </pre>
 */

@Tag(name = "separator")
@ExtensionTag(baseClassName = "org.apache.myfaces.tobago.internal.taglib.SeparatorTag")
public class SeparatorExtensionTag extends BodyTagSupport implements HasIdBindingAndRendered, HasLabel {
  private String binding;
  private String rendered;
  private String label;

  private SeparatorTag separatorTag;
  private FacetTag facetTag;
  private LabelTag labelTag;

  @Override
  public int doStartTag() throws JspException {
    separatorTag = new SeparatorTag();
    separatorTag.setPageContext(pageContext);
    separatorTag.setParent(getParent());
    if (binding != null) {
      separatorTag.setBinding(binding);
    }
    if (rendered != null) {
      separatorTag.setRendered(rendered);
    }
    facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setParent(separatorTag);
    facetTag.setName(Facets.LABEL);

    facetTag.doStartTag();
    labelTag = new LabelTag();
    labelTag.setPageContext(pageContext);
    labelTag.setParent(facetTag);
    if (label != null) {
      labelTag.setValue(label);
    }
    labelTag.doStartTag();
    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {
    labelTag.doEndTag();
    facetTag.doEndTag();
    separatorTag.doEndTag();
    return super.doEndTag();
  }

  @Override
  public void release() {
    super.release();
    binding = null;
    rendered = null;
    label = null;
    separatorTag = null;
    facetTag = null;
    labelTag = null;
  }

  public void setBinding(String binding) throws JspException {
    this.binding = binding;
  }

  public void setRendered(String rendered) {
    this.rendered = rendered;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}

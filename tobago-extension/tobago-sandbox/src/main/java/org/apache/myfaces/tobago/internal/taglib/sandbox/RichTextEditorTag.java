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

package org.apache.myfaces.tobago.internal.taglib.sandbox;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.taglib.TobagoTag;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

/**
 * TODO: under construction
 */
public abstract class RichTextEditorTag extends TobagoTag
    implements RichTextEditorTagDeclaration {

  private ValueExpression statePreview;


  public int doEndTag() throws JspException {
    // TODO: own layout for editor?
    final int result = super.doEndTag();
    getComponentInstance().getFacets().remove(Facets.LAYOUT);
    return result;
  }

  protected void setProperties(final UIComponent component) {

    super.setProperties(component);

    if (statePreview != null) {
      if (!statePreview.isLiteralText()) {
        component.setValueExpression(Attributes.STATE_PREVIEW, statePreview);
      } else {
        component.getAttributes().put(Attributes.STATE_PREVIEW, statePreview.getExpressionString());
      }
    }
  }

  public void release() {
    super.release();
    statePreview = null;
  }

  public ValueExpression getStatePreview() {
    return statePreview;
  }

  public void setStatePreview(final ValueExpression statePreview) {
    this.statePreview = statePreview;
  }
}

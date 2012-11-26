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
import org.apache.myfaces.tobago.component.UIExtensionPanel;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.internal.taglib.TobagoELTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


public final class ExtensionPanelTag extends TobagoELTag {
  private static final Logger LOG = LoggerFactory.getLogger(ExtensionPanelTag.class);
  private javax.el.ValueExpression  markup;
  private javax.el.ValueExpression  tip;

  @Override
  public String getComponentType() {
    return UIExtensionPanel.COMPONENT_TYPE;
  }
  @Override
  public String getRendererType() {
    return "Panel";
  }

  @Override
  protected void setProperties(final UIComponent uiComponent) {
    super.setProperties(uiComponent);
    final UIPanel component = (UIPanel) uiComponent;
    final FacesContext context = FacesContext.getCurrentInstance();
    final Application application = context.getApplication();
    if (markup != null) {
      if (!markup.isLiteralText()) {
        component.setValueExpression("markup", markup);
      } else {
        component.setMarkup(org.apache.myfaces.tobago.context.Markup.valueOf(markup.getExpressionString()));
      }
    }
    if (tip != null) {
      component.setValueExpression("tip", tip);
    }

  }

  public javax.el.ValueExpression getMarkup() {
    return markup;
  }

  public void setMarkup(final javax.el.ValueExpression markup) {
    this.markup = markup;
  }

  public javax.el.ValueExpression getTip() {
    return tip;
  }

  public void setTip(final javax.el.ValueExpression tip) {
    this.tip = tip;
  }

  @Override
  public void release() {
    super.release();
    markup = null;
    tip = null;
  }
}

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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUISelectBoolean;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanToggle;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;

import jakarta.faces.context.FacesContext;

public class SelectBooleanToggleRenderer<T extends AbstractUISelectBooleanToggle>
    extends SelectBooleanRendererBase<T> {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_SELECT_BOOLEAN_TOGGLE;
  }

  protected CssItem[] getOuterCssItems(final FacesContext facesContext, final AbstractUISelectBoolean select) {
    final boolean insideCommand = isInside(facesContext, HtmlElements.COMMAND);
    final boolean colFromLabel = !select.isLabelLayoutSkip() && !insideCommand;
    return new CssItem[]{
        colFromLabel ? BootstrapClass.COL_FORM_LABEL : null,
        insideCommand ? BootstrapClass.DROPDOWN_ITEM : null,
        BootstrapClass.FORM_SWITCH
    };
  }

}

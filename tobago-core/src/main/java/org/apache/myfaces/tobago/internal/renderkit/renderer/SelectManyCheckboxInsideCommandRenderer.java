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

import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyCheckbox;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SelectManyCheckboxInsideCommandRenderer extends SelectManyCheckboxRenderer {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    encodeBeginField(facesContext, component);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    encodeEndField(facesContext, component);
  }

  @Override
  protected HtmlElements getOuterHtmlTag() {
    return HtmlElements.TOBAGO_SELECT_MANY_CHECKBOX;
  }

  @Override
  protected CssItem[] getCssItems(final FacesContext facesContext, final AbstractUISelectManyCheckbox select) {
    return new CssItem[]{BootstrapClass.DROPDOWN_ITEM};
  }
}

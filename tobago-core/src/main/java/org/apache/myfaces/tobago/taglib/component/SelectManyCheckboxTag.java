/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created: Sep 9, 2002 4:28:29 PM
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDER_RANGE;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

/**
 * Render a group of checkboxes.
 */

public class SelectManyCheckboxTag extends SelectManyTag implements SelectManyCheckboxTagDeclaration {

  private String renderRange;

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange);
  }

  public void release() {
    super.release();
    renderRange = null;
  }

  public String getRenderRange() {
    return renderRange;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}


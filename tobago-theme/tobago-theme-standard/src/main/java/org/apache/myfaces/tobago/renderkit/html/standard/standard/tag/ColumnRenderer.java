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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.UIColumn;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;

public class ColumnRenderer extends RendererBase {

  /**
   * Differ between simple content and complex content.
   * Decide if the content of a cell needs usually the whole possible space or
   * is the character of the content like flowing text.
   * In the second case, the style usually sets a padding.<br/>
   * Pure is needed for &lt;tc:panel>,  &lt;tc:in>, etc.<br/>
   * Pure is not needed for  &lt;tc:out> and &lt;tc:link>
   */
  private boolean isPure(final UIColumn column) {
    for (final UIComponent child : column.getChildren()) {
      if (!(child instanceof UIOut) && !(child instanceof UILink)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }
}

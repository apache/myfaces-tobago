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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBorder;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasColumnLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMargin;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMargins;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRowLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasSpacing;

/**
 * Renders a GridLayout.
 * <pre>
 * columns/rows ::= LAYOUT
 * LAYOUT       ::= TOKEN [";" TOKEN]+
 * TOKEN        ::= AUTO | PIXEL | PROPORTIONAL
 * AUTO         ::= "auto" | "fixed"
 * PIXEL        ::= NUMBER "px"
 * PROPORTIONAL ::= [NUMBER] "*"
 * </pre>
 * <p/>
 * <table border="1">
 * <tr>
 * <th>Parent</th>
 * <th>Child</th>
 * <th>Okay?</th>
 * <th>Remarks</th>
 * </tr>
 * <tr>
 * <td>AUTO</td>
 * <td>any combination of AUTO or PIXEL but no PROPORTIONAL</td>
 * <td>okay</td>
 * <td>-</td>
 * </tr>
 * <tr>
 * <td>AUTO</td>
 * <td>any combination with at least one PROPORTIONAL</td>
 * <td>wrong</td>
 * <td>LayoutManager cannot compute the auto value.</td>
 * </tr>
 * <tr>
 * <td>PIXEL</td>
 * <td>any combination of AUTO or PIXEL but no PROPORTIONAL</td>
 * <td>potentially wrong</td>
 * <td>The values depend on each other, the programmer has to keep consistency manually.</td>
 * </tr>
 * <tr>
 * <td>PIXEL</td>
 * <td>any combination with at least one PROPORTIONAL</td>
 * <td>okay</td>
 * <td>-</td>
 * </tr>
 * <tr>
 * <td>PROPORTIONAL</td>
 * <td>any combination of AUTO or PIXEL but no PROPORTIONAL</td>
 * <td>potentially wrong</td>
 * <td>No automatic matching:<ul><li>too little space: scroll bar</li>
 * <li>too much space: elements will be spread.</li></ul></td>
 * </tr>
 * <tr>
 * <td>PROPORTIONAL</td>
 * <td>any combination with at least one PROPORTIONAL</td>
 * <td>okay</td>
 * <td>-</td>
 * </tr>
 * </table>
 */
@Tag(name = "gridLayout")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIGridLayout",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout",
    uiComponentFacesClass = "javax.faces.component.UIComponentBase",
    componentFamily = AbstractUIGridLayout.COMPONENT_FAMILY,
    rendererType = RendererTypes.GRID_LAYOUT,
    allowedChildComponenents = "NONE", isLayout = true)
public interface GridLayoutTagDeclaration extends HasId, HasBorder, HasSpacing, HasMargin,
    HasMargins, HasColumnLayout, HasRowLayout, HasBinding, HasMarkup, HasCurrentMarkup {

  /**
   * This attribute is a hint for the layout manager. Should not be used in most cases.
   *
   * @param columnOverflow Does the component need a horizontal scrollbar?
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean")
  void setColumnOverflow(String columnOverflow);

  /**
   * This attribute is a hint for the layout manager. Should not be used in most cases.
   *
   * @param rowOverflow Does the component need a vertical scrollbar?
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean")
  void setRowOverflow(String rowOverflow);

  /**
   * This attribute advises the layout manager, to not use space that comes from non rendered components.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean")
  void setRigid(String rigid);

}

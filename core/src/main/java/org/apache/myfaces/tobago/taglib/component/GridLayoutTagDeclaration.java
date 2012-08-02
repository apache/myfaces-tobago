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

package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasBorder;
import org.apache.myfaces.tobago.taglib.decl.HasCellspacing;
import org.apache.myfaces.tobago.taglib.decl.HasColumnLayout;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasMargin;
import org.apache.myfaces.tobago.taglib.decl.HasMargins;
import org.apache.myfaces.tobago.taglib.decl.HasRowLayout;

/*
 * Date: 14.03.2006
 * Time: 16:58:13
 */

/**
 * Renders a GridLayout.
 * <pre>
 * columns/rows ::= LAYOUT
 * LAYOUT       ::= TOKEN [";" TOKEN]+
 * TOKEN        ::= FIXED | PIXEL | PROPORTIONAL
 * FIXED        ::= "fixed"
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
 * <td>FIXED</td>
 * <td>any combination of FIXED or PIXEL but no PROPORTIONAL</td>
 * <td>okay</td>
 * <td>-</td>
 * </tr>
 * <tr>
 * <td>FIXED</td>
 * <td>any combination with at least one PROPORTIONAL</td>
 * <td>wrong</td>
 * <td>LayoutManager cannot compute the fixed value.</td>
 * </tr>
 * <tr>
 * <td>PIXEL</td>
 * <td>any combination of FIXED or PIXEL but no PROPORTIONAL</td>
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
 * <td>any combination of FIXED or PIXEL but no PROPORTIONAL</td>
 * <td>potentially wrong</td>
 * <td>No automatical matching:<ul><li>too little space: scrollbar</li>
 * <li>too much space: elements will be spreaded.</li></ul></td>
 * </tr>
 * <tr>
 * <td>PROPORTIONAL</td>
 * <td>any combination with at least one PROPORTIONAL</td>
 * <td>okay</td>
 * <td>-</td>
 * </tr>
 * </table>
 */
@Tag(name = "gridLayout", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIGridLayout",
    rendererType = "GridLayout")
public interface GridLayoutTagDeclaration extends TobagoTagDeclaration, HasId, HasBorder, HasCellspacing, HasMargin,
    HasMargins, HasColumnLayout, HasRowLayout, HasBinding {
}

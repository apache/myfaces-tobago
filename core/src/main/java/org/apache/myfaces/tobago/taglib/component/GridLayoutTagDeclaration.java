package org.apache.myfaces.tobago.taglib.component;

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
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 14.03.2006
 * Time: 16:58:13
 */

/**
 *
 * <code>
 * columns/rows ::= LAYOUT
 * LAYOUT       ::= TOKEN [";" TOKEN]+
 * TOKEN        ::= FIXED | PIXEL | PROPORTIONAL
 * FIXED        ::= "fixed"
 * PIXEL        ::= NUMBER "px"
 * PROPORTIONAL ::= [NUMBER] "*"
 * </code>

 <table border="1">
   <tr>
     <th>Parent</th>
     <th>Child</th>
     <th>Okay?</th>
     <th>Remarks</th>
   </tr>
   <tr>
     <td rowspan="2">FIXED</td>
     <td>any combination of FIXED or PIXEL but no PROPORTIONAL</td>
     <td>okay</td>
     <td/>
   </tr>
   <tr>
     <!--<td>FIXED</td>-->
     <td>any combination with at least one PROPORTIONAL 
     <td>wrong</td>
     <td>LayoutManager can't compute the fixed value.</td>
   </tr>
   <tr>
     <td rowspan="2">PIXEL</td>
     <td>any combination of FIXED or PIXEL but no PROPORTIONAL</td>
     <td>potentially wrong</td>
     <td>The values depends on each other, the programmer has to keep consitency manually.</td>
   </tr>
   <tr>
     <!--<td>PIXEL</td>-->
     <td>any combination with at least one PROPORTIONAL 
     <td>okay</td>
     <td/>
   </tr>
   <tr>
     <td rowspan="2">PROPORTIONAL</td>
     <td>any combination of FIXED or PIXEL but no PROPORTIONAL</td>
     <td>potentially wrong</td>
     <td>No automatical matching:<br/>  
       a) to less space: scrollbar<br/>  
       b) to much space: elements will be speaded.</td>
   </tr>
   <tr>
     <!--<td>PROPORTIONAL</td>-->
     <td>any combination with at least one PROPORTIONAL 
     </td>
     <td>okay</td>
     <td/>
   </tr>
 </table>

 */

@Tag(name = "gridLayout", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIGridLayout",
    rendererType = "GridLayout")
public interface GridLayoutTagDeclaration extends TobagoTagDeclaration, HasId, HasBorder, HasCellspacing, HasMargin,
    HasMargins, HasColumnLayout, HasRowLayout, HasBinding {
}

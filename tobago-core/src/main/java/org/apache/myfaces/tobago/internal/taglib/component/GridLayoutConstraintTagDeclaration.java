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

import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;

import jakarta.el.ValueExpression;

/**
 * Set grid layout specific contraints to the parent component.
 */
@Tag(name = "gridLayoutConstraint")
@SimpleTag(faceletHandler = "org.apache.myfaces.tobago.facelets.GridLayoutConstraintHandler")
public interface GridLayoutConstraintTagDeclaration {

  /**
   * The number of horizontal cells this component should use.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setColumnSpan(ValueExpression columnSpan);

  /**
   * The number of vertical cells this component should use.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setRowSpan(ValueExpression rowSpan);

  /**
   * The horizontal position in the grid of this component. Please use gridColumn and gridRow both or none.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setGridColumn(ValueExpression gridColumn);

  /**
   * The vertical position in the grid of this component. Please use gridColumn and gridRow both or none.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setGridRow(ValueExpression gridRow);
}

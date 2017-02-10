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

import javax.el.ValueExpression;

/**
 * Set a offset for the parent UIComponent withing a segment layout.
 */
@Tag(name = "segmentLayoutConstraint")
@SimpleTag(faceletHandler = "org.apache.myfaces.tobago.facelets.SegmentLayoutConstraintHandler")
public interface SegmentLayoutConstraintTagDeclaration {

  /**
   * The number of columns this component moves to the right for extra small devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetExtraSmall(final ValueExpression offsetExtraSmall);

  /**
   * The number of columns this component moves to the right for small devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetSmall(final ValueExpression offsetSmall);

  /**
   * The number of columns this component moves to the right for medium devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetMedium(final ValueExpression offsetMedium);

  /**
   * The number of columns this component moves to the right for large devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetLarge(final ValueExpression offsetLarge);

  /**
   * Overwrite the default layout for extra small devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setExtraSmall(final ValueExpression extraSmall);

  /**
   * Overwrite the default layout for small devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setSmall(final ValueExpression small);

  /**
   * Overwrite the default layout for medium devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setMedium(final ValueExpression medium);

  /**
   * Overwrite the default layout for large devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setLarge(final ValueExpression large);
}

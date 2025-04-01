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
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.layout.Margin;

import jakarta.el.ValueExpression;

/**
 * Set a offset for the parent UIComponent within a segment layout.
 */
@Tag(name = "segmentLayoutConstraint")
@SimpleTag(faceletHandler = "org.apache.myfaces.tobago.facelets.SegmentLayoutConstraintHandler")
public interface SegmentLayoutConstraintTagDeclaration {

  /**
   * Overwrite the default layout for extra small devices.
   * Possible values are: segment values between 1 and 12, 'auto' and '*'.
   */
  @TagAttribute
  void setExtraSmall(ValueExpression extraSmall);

  /**
   * Overwrite the default layout for small devices.
   * Possible values are: segment values between 1 and 12, 'auto' and '*'.
   */
  @TagAttribute
  void setSmall(ValueExpression small);

  /**
   * Overwrite the default layout for medium devices.
   * Possible values are: segment values between 1 and 12, 'auto' and '*'.
   */
  @TagAttribute
  void setMedium(ValueExpression medium);

  /**
   * Overwrite the default layout for large devices.
   * Possible values are: segment values between 1 and 12, 'auto' and '*'.
   */
  @TagAttribute
  void setLarge(ValueExpression large);

  /**
   * Overwrite the default layout for extra large devices.
   * Possible values are: segment values between 1 and 12, 'auto' and '*'.
   */
  @TagAttribute
  void setExtraLarge(ValueExpression extraLarge);

  /**
   * Overwrite the default margin for extra small devices.
   */
  @TagAttribute
  @UIComponentTagAttribute(allowedValues = {Margin.NONE, Margin.LEFT, Margin.RIGHT, Margin.BOTH})
  void setMarginExtraSmall(ValueExpression overwriteMarginExtraSmall);

  /**
   * Overwrite the default margin for small devices.
   */
  @TagAttribute
  @UIComponentTagAttribute(allowedValues = {Margin.NONE, Margin.LEFT, Margin.RIGHT, Margin.BOTH})
  void setMarginSmall(ValueExpression overwriteMarginSmall);

  /**
   * Overwrite the default margin for medium devices.
   */
  @TagAttribute
  @UIComponentTagAttribute(allowedValues = {Margin.NONE, Margin.LEFT, Margin.RIGHT, Margin.BOTH})
  void setMarginMedium(ValueExpression overwriteMarginMedium);

  /**
   * Overwrite the default margin for large devices.
   */
  @TagAttribute
  @UIComponentTagAttribute(allowedValues = {Margin.NONE, Margin.LEFT, Margin.RIGHT, Margin.BOTH})
  void setMarginLarge(ValueExpression overwriteMarginLarge);

  /**
   * Overwrite the default margin for extra large devices.
   */
  @TagAttribute
  @UIComponentTagAttribute(allowedValues = {Margin.NONE, Margin.LEFT, Margin.RIGHT, Margin.BOTH})
  void setMarginExtraLarge(ValueExpression overwriteMarginExtraLarge);

  /**
   * The number of columns this component moves to the right for extra small devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetExtraSmall(ValueExpression offsetExtraSmall);

  /**
   * The number of columns this component moves to the right for small devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetSmall(ValueExpression offsetSmall);

  /**
   * The number of columns this component moves to the right for medium devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetMedium(ValueExpression offsetMedium);

  /**
   * The number of columns this component moves to the right for large devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetLarge(ValueExpression offsetLarge);

  /**
   * The number of columns this component moves to the right for extra large devices.
   */
  @TagAttribute(type = "java.lang.Integer")
  void setOffsetExtraLarge(ValueExpression offsetLarge);
}

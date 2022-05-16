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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.layout.MeasureList;
import org.apache.myfaces.tobago.layout.SegmentJustify;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.SegmentLayoutTagDeclaration}
 *
 * @since 3.0.0
 */
public abstract class AbstractUISegmentLayout extends AbstractUILayoutBase {

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.SegmentLayout";

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder(getClass().getSimpleName());
    if (getExtraSmall() != null) {
      builder.append("\n        extraSmall=");
      builder.append(getExtraSmall());
    }
    if (getSmall() != null) {
      builder.append("\n        small=");
      builder.append(getSmall());
    }
    if (getMedium() != null) {
      builder.append("\n        medium=");
      builder.append(getMedium());
    }
    if (getLarge() != null) {
      builder.append("\n        large=");
      builder.append(getLarge());
    }
    if (getExtraLarge() != null) {
      builder.append("\n        extraLarge=");
      builder.append(getExtraLarge());
    }
    if (getExtraExtraLarge() != null) {
      builder.append("\n        extraExtraLarge=");
      builder.append(getExtraLarge());
    }
    return builder.toString();
  }

  public abstract MeasureList getExtraSmall();

  public abstract MeasureList getSmall();

  public abstract MeasureList getMedium();

  public abstract MeasureList getLarge();

  public abstract MeasureList getExtraLarge();

  public abstract MeasureList getExtraExtraLarge();

  public abstract String getMarginExtraSmall();

  public abstract String getMarginSmall();

  public abstract String getMarginMedium();

  public abstract String getMarginLarge();

  public abstract String getMarginExtraLarge();

  public abstract String getMarginExtraExtraLarge();

  public abstract SegmentJustify getJustify();
}

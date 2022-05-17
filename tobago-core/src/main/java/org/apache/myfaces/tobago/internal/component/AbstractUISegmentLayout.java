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

import org.apache.myfaces.tobago.component.SupportsSegmentLayout;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.SegmentLayoutTagDeclaration}
 *
 * @since 3.0.0
 */
public abstract class AbstractUISegmentLayout extends AbstractUILayoutBase implements SupportsSegmentLayout {

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
    if (getExtra2Large() != null) {
      builder.append("\n        extra2Large=");
      builder.append(getExtraLarge());
    }
    return builder.toString();
  }
}

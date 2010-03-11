package org.apache.myfaces.tobago.internal.layout;

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

import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;


public class Interval {

  private final Measure minimum;

  private final Measure preferred;

  private final Measure maximum;

  private final Measure current;

  public Interval(LayoutComponent component, Orientation orientation) {
    this(
        orientation == Orientation.HORIZONTAL ? component.getMinimumWidth() : component.getMinimumHeight(),
        orientation == Orientation.HORIZONTAL ? component.getPreferredWidth() : component.getPreferredHeight(),
        orientation == Orientation.HORIZONTAL ? component.getMaximumWidth() : component.getMaximumHeight(),
        orientation == Orientation.HORIZONTAL ? component.getCurrentWidth() : component.getCurrentHeight());
  }

  public Interval(Measure minimum, Measure preferred, Measure maximum, Measure current) {
    this.minimum = minimum;
    this.preferred = preferred;
    this.maximum = maximum;
    this.current = current;
  }

  public Measure getMinimum() {
    return minimum;
  }

  public Measure getPreferred() {
    return preferred;
  }

  public Measure getMaximum() {
    return maximum;
  }

  public Measure getCurrent() {
    return current;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    builder.append(minimum);
    builder.append("<=");
    builder.append(preferred);
    builder.append("<=");
    builder.append(maximum);
    builder.append(",");
    builder.append(current);
    builder.append("]");
    return builder.toString();
  }
}

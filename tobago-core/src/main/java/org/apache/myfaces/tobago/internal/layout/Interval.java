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

package org.apache.myfaces.tobago.internal.layout;

import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Interval {

  private static final Logger LOG = LoggerFactory.getLogger(Interval.class);

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
    if (LOG.isWarnEnabled()) {
      if (minimum != null && minimum.greaterThan(preferred)) {
        LOG.warn("Minimum '{}' is not smaller or equals preferred '{}'.", minimum, preferred);
      }
      if (preferred != null && preferred.greaterThan(maximum)) {
        LOG.warn("Preferred '{}' is not smaller or equals maximum '{}'.", preferred, maximum);
      }
    }
    this.minimum = minimum;
    this.preferred = preferred;
    this.maximum = maximum;
    this.current = current;
  }

  // XXX what about rounding??? may use multiply instead of divide
  public Interval(Interval interval, int divider) {
    this.minimum = interval.minimum != null
        ? interval.minimum.divide(divider) : null; // XXX may add one for rounding up.
    this.preferred = interval.preferred != null ? interval.preferred.divide(divider) : null;
    this.maximum = interval.maximum != null ? interval.maximum.divide(divider) : null;
    this.current = interval.current != null ? interval.current.divide(divider) : null;
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

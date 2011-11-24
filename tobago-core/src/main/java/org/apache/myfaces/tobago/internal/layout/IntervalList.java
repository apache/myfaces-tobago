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

import org.apache.myfaces.tobago.layout.Measure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class IntervalList extends ArrayList<Interval> {

  private static final Logger LOG = LoggerFactory.getLogger(IntervalList.class);

  private Measure minimum;
  private Measure current;

  public void evaluate() {
    List<Measure> minimumList = collectMinimum();
    List<Measure> maximumList = collectMaximum();
    minimum = Measure.max(minimumList);
    Measure maximum = Measure.min(maximumList);
    if (minimum.greaterThan(maximum)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Layout: Found a minimum constraint " + minimum
            + " which is greater than a maximum constraint " + maximum + "!");
      }
      current = minimum;
    } else {
      List<Measure> preferred = findPreferredInInterval(minimum, maximum);
      if (!preferred.isEmpty()) {
        current = Measure.max(preferred);
      } else {
        current = minimum;
      }
    }
  }

  private List<Measure> collectMinimum() {
    List<Measure> result = new ArrayList<Measure>();
    for (Interval interval : this) {
      if (interval.getMinimum() != null) {
        result.add(interval.getMinimum());
      }
      if (interval.getCurrent() != null) {
        result.add(interval.getCurrent());
      }
    }
    return result;
  }

  private List<Measure> collectMaximum() {
    List<Measure> result = new ArrayList<Measure>();
    for (Interval interval : this) {
      if (interval.getMaximum() != null) {
        result.add(interval.getMaximum());
      }
    }
    return result;
  }

  private List<Measure> findPreferredInInterval(Measure min, Measure max) {
    List<Measure> result = new ArrayList<Measure>();
    for (Interval interval : this) {
      Measure preferred = interval.getPreferred();
      if (preferred != null && preferred.greaterOrEqualThan(min) && preferred.lessOrEqualThan(max)) {
        result.add(preferred);
      }
    }
    return result;
  }

  public Measure getMinimum() {
    return minimum;
  }

  public Measure getCurrent() {
    return current;
  }
}

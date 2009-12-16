package org.apache.myfaces.tobago.layout;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class IntervalList extends ArrayList<Interval> {

  private static final Log LOG = LogFactory.getLog(IntervalList.class);

  public Measure computeAuto() {
    Measure auto;
    List<Measure> fixedList = collectFixed();
    List<Measure> minimumList = collectMinimum();
    List<Measure> maximumList = collectMaximum();
    if (!fixedList.isEmpty()) {
      auto = max(max(minimumList), max(fixedList));
    } else {
      Measure maximumOfMinimumList = max(minimumList);
      Measure minimumOfMaximumList = min(maximumList);
      if (maximumOfMinimumList.getPixel() > minimumOfMaximumList.getPixel()) {
        LOG.warn("!");
        auto = maximumOfMinimumList;
      } else {
        List preferredInInterval = findPreferredInInterval(maximumOfMinimumList, minimumOfMaximumList);
        if (!preferredInInterval.isEmpty()) {
          auto = max(preferredInInterval);
        } else {
          auto = maximumOfMinimumList;
        }
      }
    }

    return auto;
  }

  private List<Measure> collectFixed() {
    List<Measure> result = new ArrayList<Measure>();
    for (Interval interval : this) {
      if (interval.getFixed() != null) {
        result.add(interval.getFixed());
      }
    }
    return result;
  }

  private List<Measure> collectMinimum() {
    List<Measure> result = new ArrayList<Measure>();
    for (Interval interval : this) {
      if (interval.getMinimum() != null) {
        result.add(interval.getMinimum());
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

  private Measure max(List<Measure> list) {
    Measure max = Measure.ZERO;
    for (Measure measure : list) {
      if (measure.getPixel() > max.getPixel()) {
        max = measure;
      }
    }
    return max;
  }

  private Measure min(List<Measure> list) {
    Measure min = new PixelMeasure(Integer.MAX_VALUE);
    for (Measure measure : list) {
      if (measure.getPixel() < min.getPixel()) {
        min = measure;
      }
    }
    return min;
  }

  private Measure max(Measure m1, Measure m2) {
    if (m1.getPixel() > m2.getPixel()) {
      return m1;
    } else {
      return m2;
    }
  }

  private List<Measure> findPreferredInInterval(Measure min, Measure max) {
    List<Measure> result = new ArrayList<Measure>();
    for (Interval interval : this) {
      Measure preferred = interval.getPreferred();
      if (preferred != null && preferred.getPixel() >= min.getPixel() && preferred.getPixel() <= max.getPixel()) {
        result.add(preferred);
      }
    }
    return result;
  }

}

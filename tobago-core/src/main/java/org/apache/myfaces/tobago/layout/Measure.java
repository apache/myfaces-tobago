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

package org.apache.myfaces.tobago.layout;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public abstract class Measure implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(Measure.class);

  public static final Measure ZERO = valueOf(0);
  public static final Measure MAX = valueOf(Integer.MAX_VALUE);

  // todo: refactor and consolidate with LayoutToken
  
  public static Measure valueOf(Measure value) {
    if (value == null) {
      return ZERO;
    }
    return value;
  }

  public static Measure valueOf(int value) {
    return PixelMeasure.pixelValueOf(value);
  }

  public static Measure valueOf(Integer value) {
    if (value == null) {
      return ZERO;
    }
    return valueOf(value.intValue());
  }

  public static Measure valueOf(Number value) {
    if (value == null) {
      return ZERO;
    }
    return valueOf(value.intValue());
  }

  public static Measure valueOf(String value) {
    if (StringUtils.isEmpty(value)) {
      return ZERO;
    }
    try {
      if (value.endsWith("px")) {
        return Measure.valueOf(Integer.parseInt(value.substring(0, value.length() - 2)));
      }
      return Measure.valueOf(Integer.parseInt(value));

    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Can't parse to any measure: '" + value + "'", e);
    }
  }

  public static Measure valueOf(Object object) {
    if (object instanceof Measure) {
      return valueOf((Measure) object);
    }
    if (object instanceof Number) {
      return valueOf((Number) object);
    }
    if (object instanceof String) {
      return valueOf((String) object);
    }
    if (object == null) {
      return ZERO;
    }
    return valueOf(object.toString());
  }

  /**
   * @deprecated since 1.5.0, please use valueOf()
   */
  @Deprecated
  public static Measure parse(String value) {
    return valueOf(value);
  }

  public abstract Measure add(Measure m);

  public abstract Measure add(int m);

  public abstract Measure multiply(int times);

  public abstract Measure divide(int times);

  /**
   * @deprecated since 1.5.0, please use subtractNotNegative
   */
  @Deprecated
  public Measure substractNotNegative(Measure m) {
    return subtractNotNegative(m);
  }

  public abstract Measure subtractNotNegative(Measure m);

  public abstract Measure subtract(Measure m);

  public abstract Measure subtract(int m);

  public boolean greaterThan(Measure measure) {
    return measure != null && getPixel() > measure.getPixel();
  }

  public boolean greaterOrEqualThan(Measure measure) {
    return measure != null && getPixel() >= measure.getPixel();
  }

  public boolean lessThan(Measure measure) {
    return measure != null && getPixel() < measure.getPixel();
  }

  public boolean lessOrEqualThan(Measure measure) {
    return measure != null && getPixel() <= measure.getPixel();
  }

  public abstract int getPixel();

  /**
   * Returns the maximum. If all parameters are null, than the result is {@value #ZERO}.
   */
  public static Measure max(List<Measure> list) {
    Measure max = ZERO;
    for (Measure measure : list) {
      if (max.lessThan(measure)) {
        max = measure;
      }
    }
    return max;
  }

  /**
   * Returns the minimum. If all parameters are null, than the result is {@value #MAX}.
   */
  public static Measure min(List<Measure> list) {
    Measure min = MAX;
    for (Measure measure : list) {
      if (min.greaterThan(measure)) {
        min = measure;
      }
    }
    return min;
  }

  /**
   * Returns the maximum. If all parameters are null, than the result is {@value #ZERO}.
   */
  public static Measure max(Measure m1, Measure m2) {
    if (m1 != null) {
      return m1.lessThan(m2) ? m2 : m1;
    } else {
      return m2 != null ? m2 : ZERO;
    }
  }

  /**
   * Returns the minimum. If all parameters are null, than the result is {@value #MAX}.
   */
  public static Measure min(Measure m1, Measure m2) {
    if (m1 != null) {
      return m1.greaterThan(m2) ? m2 : m1;
    } else {
      return m2 != null ? m2 : MAX;
    }
  }
}

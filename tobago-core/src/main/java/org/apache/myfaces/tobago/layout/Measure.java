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

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.Locale;

/**
 * In PDLs the class {@link org.apache.myfaces.tobago.layout.MeasureEditor} will convert the string literals.
 */
public final class Measure implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final Measure ZERO = valueOf(0);
  public static final Measure AUTO = valueOf("auto");
  public static final Measure FRACTION1 = valueOf("1fr");

  private final float value;
  private final Unit unit;

  private Measure() {
    this.value = 0;
    this.unit = Unit.AUTO;
  }

  public Measure(final int i, final Unit unit) {
    this.value = i;
    this.unit = unit;
  }

  public Measure(final double d, final Unit unit) {
    this.value = (float) d;
    this.unit = unit;
  }

  public Measure(final String string, final Unit unit) {
    this.value = Float.parseFloat(string);
    this.unit = unit;
  }

  public static Measure valueOf(final Measure measure) {
    if (measure == null) {
      return ZERO;
    }
    return measure;
  }

  public static Measure valueOf(final int value) {
    return new Measure(value, Unit.PX);
  }

  public static Measure valueOf(final Integer i) {
    if (i == null) {
      return ZERO;
    }
    return valueOf(i.intValue());
  }

  public static Measure valueOf(final Number n) {
    if (n == null) {
      return ZERO;
    }
    return valueOf(n.doubleValue());
  }

  public static Measure valueOf(final String s) {
    return valueOf(s, Unit.PX);
  }

  public static Measure valueOf(final String s, final Unit defaultUnit) {
    try {
      if (StringUtils.isEmpty(s)) {
        return null;
      }
      final int length = s.length();
      if (s.endsWith("%")) {
        return new Measure(s.substring(0, length - 1), Unit.PERCENT);
      }
      if (s.endsWith("*")) {
        if (s.length() == 1) {
          return new Measure(1, Unit.FR);
        } else {
          return new Measure(s.substring(0, length - 1), Unit.FR);
        }
      }
      if (s.equals("auto")) {
        return new Measure();
      }
      for (int i = 4; i >= 2; i--) {
        final int pos = length - i;
        if (length >= i && Character.isLetter(s.charAt(pos))) {
          return new Measure(s.substring(0, pos), Unit.valueOf(s.substring(pos).toUpperCase(Locale.ROOT)));
        }
      }
      return new Measure(s, defaultUnit);

    } catch (final RuntimeException e) {
      LOG.warn("Can't parse to any measure: '" + s + "'");
    }
    return null;
  }

  public static Measure valueOf(final Object object) {
    if (object == null) {
      return null;
    }
    if (object instanceof Measure) {
      return valueOf((Measure) object);
    }
    if (object instanceof Number) {
      return valueOf((Number) object);
    }
    if (object instanceof String) {
      return valueOf((String) object);
    }
    return valueOf(object.toString());
  }

  public String serialize() {
    final StringBuilder builder = new StringBuilder();
    if (unit != Unit.AUTO) {
      builder.append(value);
    }
    final int length = builder.length();
    if (length >= 3 && builder.charAt(length - 1) == '0' && builder.charAt(length - 2) == '.') {
      builder.deleteCharAt(length - 1);
      builder.deleteCharAt(length - 2);
    }
    builder.append(unit.getValue());
    return builder.toString();
  }

  public float getValue() {
    return value;
  }

  public Unit getUnit() {
    return unit;
  }

  public String toString() {
    return serialize();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Measure measure = (Measure) o;

    if (Float.compare(measure.value, value) != 0) {
      return false;
    }
    return unit == measure.unit;
  }

  @Override
  public int hashCode() {
    int result = value != +0.0f ? Float.floatToIntBits(value) : 0;
    result = 31 * result + unit.hashCode();
    return result;
  }

  public enum Unit {

    EM,
    PX,
    EX,
    PT,
    CM,
    MM,
    IN,
    PC,
    CH, // Relative to width of the "0" (zero)
    REM, // Relative to font-size of the root element
    VW, // Relative to 1% of the width of the viewport*
    VH, // Relative to 1% of the height of the viewport*
    VMIN, // Relative to 1% of viewport's* smaller dimension
    VMAX, // Relative to 1% of viewport's* larger dimension
    PERCENT,
    FR, // Fraction - same as * in classic Tobago
    SEG, // Number of used columns in segment layout (Tobago specific)
    AUTO; // Marker for CSS 'auto', not a regular measure

    private final String value;

    Unit() {
      value = name().equals("PERCENT") ? "%" : name().toLowerCase(Locale.ROOT);
    }

    public String getValue() {
      return value;
    }
  }
}

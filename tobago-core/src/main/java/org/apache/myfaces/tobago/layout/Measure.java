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

import java.io.Serializable;

/**
 * In JSPs the class {@link org.apache.myfaces.tobago.layout.MeasureEditor} will convert the string literals.
 */
public final class Measure implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(Measure.class);

  public static final Measure ZERO = valueOf(0);

  // todo: refactor and consolidate with LayoutToken

  private final Double d;
  private final Integer i;
  private final Unit unit;

  public Measure(int i, Unit unit) {
    this.d = null;
    this.i = i;
    this.unit = unit;
  }

  public Measure(double d, Unit unit) {
    this.d = d;
    this.i = null;
    this.unit = unit;
  }

  public Measure(String string, Unit unit) {
    if (string.contains(".")) {
      this.d = Double.parseDouble(string);
      this.i = null;
    } else {
      this.d = null;
      this.i = Integer.parseInt(string);
    }
    this.unit = unit;
  }

  public static Measure valueOf(final Measure value) {
    if (value == null) {
      return ZERO;
    }
    return value;
  }

  public static Measure valueOf(final int value) {
    return new Measure(value, Unit.PX);
  }

  public static Measure valueOf(final Integer value) {
    if (value == null) {
      return ZERO;
    }
    return valueOf(value.intValue());
  }

  public static Measure valueOf(final Number value) {
    if (value == null) {
      return ZERO;
    }
    return valueOf(value.doubleValue());
  }

  public static Measure valueOf(final String value) {
    try {
      if (StringUtils.isEmpty(value)) {
        return null;
      }
      final int length = value.length();
      if (value.endsWith("%")) {
        return new Measure(value.substring(0, length - 1), Unit.PERCENT);
      }
      if (length >= 2 && Character.isLetter(value.charAt(length - 2))) {
        return new Measure(value.substring(0, length - 2), Unit.valueOf(value.substring(length - 2).toUpperCase()));
      }
      return new Measure(value, Unit.PX);

    } catch (final RuntimeException e) {
      LOG.warn("Can't parse to any measure: '" + value + "'");
    }
    return null;
  }

  public static Measure valueOf(final Object object) {
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

  public String serialize() {
    return "" + (i != null ? i : "") + (d != null ? d : "") + unit.getValue();
  }

  public String toString() {
    return serialize();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Measure measure = (Measure) o;

    if (d != null ? !d.equals(measure.d) : measure.d != null) {
      return false;
    }
    if (i != null ? !i.equals(measure.i) : measure.i != null) {
      return false;
    }
    return unit == measure.unit;

  }

  @Override
  public int hashCode() {
    int result = d != null ? d.hashCode() : 0;
    result = 31 * result + (i != null ? i.hashCode() : 0);
    result = 31 * result + unit.hashCode();
    return result;
  }

  private enum Unit {

    EM,
    PX,
    EX,
    PT,
    CM,
    MM,
    IN,
    PC,
    PERCENT;

    private final String value;

    Unit() {
      value = name().equals("PERCENT") ? "%" : name().toLowerCase();
    }

    String getValue() {
      return value;
    }
  }
}

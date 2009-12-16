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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

public abstract class Measure implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Log LOG = LogFactory.getLog(Measure.class);

  // todo: refactor and consolidate with LayoutToken

  public static Measure parse(Object object) {
    if (object == null) {
      return PixelMeasure.ZERO; // fixme: may return a "default measure", or is Pixel the default?
    }
    if (object instanceof Measure) {
      return (Measure) object;
    }
    if (object instanceof Number) {
      return new PixelMeasure(((Number) object).intValue());
    }
    String value = object instanceof String ? (String) object : object.toString();
    
    if (StringUtils.isEmpty(value)) {
      return PixelMeasure.ZERO; // fixme: may return a "default measure", or is Pixel the default?
    }
    if (value.toLowerCase().matches("\\d+px")) {// XXX no regexp here: user LayoutTokens.parse !!!
      return new PixelMeasure(Integer.parseInt(value.substring(0, value.length() - 2)));
    }
    if (value.matches("\\d+")) {// XXX no regexp here: user LayoutTokens.parse !!!
      LOG.warn("Measure parser found value without unit. Assuming px for value='" + value + "'.");
      return new PixelMeasure(Integer.parseInt(value));
    }
    throw new IllegalArgumentException("Can't parse to any measure: '" + value + "'");
  }

  public abstract Measure add(Measure m);

  public abstract Measure add(int m);

  public abstract Measure multiply(int times);

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

  public boolean lessThan(Measure measure) {
    return measure != null && getPixel() < measure.getPixel();
  }

  public abstract int getPixel();

}

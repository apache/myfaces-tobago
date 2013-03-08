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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PixelMeasure extends Measure {

  private static final Logger LOG = LoggerFactory.getLogger(PixelMeasure.class);

  static final PixelMeasure[] PIXEL_CACHE;
  static final int PIXEL_CACHE_MAX = 4000;
  
  static {
    PIXEL_CACHE = new PixelMeasure[PIXEL_CACHE_MAX + 1];
    for (int i = 0; i < PIXEL_CACHE.length; i++) {
      PIXEL_CACHE[i] = new PixelMeasure(i);
    }
  }
  
  private final int pixel;

  private PixelMeasure(int pixel) {
    this.pixel = pixel;
  }

  static PixelMeasure pixelValueOf(int value) {
    if (value >= 0 && value <= PixelMeasure.PIXEL_CACHE_MAX) {
      return PixelMeasure.PIXEL_CACHE[value];
    }
    return new PixelMeasure(value);
  }
  
  public Measure add(Measure m) {
    if (m == null) {
      return this;
    } else {
      return pixelValueOf(pixel + m.getPixel());
    }
  }

  public Measure add(int m) {
    return pixelValueOf(pixel + m);
  }

  public Measure multiply(int times) {
    return pixelValueOf(pixel * times);
  }

  public Measure divide(int times) {
    return pixelValueOf(pixel / times);
  }

  public Measure subtractNotNegative(Measure m) {
    if (m == null) {
      return this;
    } else if (m.getPixel() > pixel) {
      LOG.warn("Not enough space! value=" + pixel);
      return ZERO;
    } else {
      return pixelValueOf(pixel - m.getPixel());
    }
  }

  public Measure subtract(Measure m) {
    if (m == null) {
      return this;
    } else {
      return pixelValueOf(pixel - m.getPixel());
    }
  }

  public Measure subtract(int m) {
      return pixelValueOf(pixel - m);
  }

  public int getPixel() {
    return pixel;
  }

  @Override
  public String toString() {
    return serialize();
  }

  @Override
  public String serialize() {
    return pixel + "px";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PixelMeasure that = (PixelMeasure) o;

    if (pixel != that.pixel) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return pixel;
  }
}

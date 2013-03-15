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


public class PixelLayoutToken extends LayoutToken {

  static final String SUFFIX = "px";

  private final PixelMeasure pixel;

  public PixelLayoutToken(int pixel) {
    // here we cannot use this(PixelMeasure.pixelValueOf(pixel)), because of class initialization problems
    this((PixelMeasure)Measure.valueOf(pixel));
  }

  public PixelLayoutToken(PixelMeasure pixel) {
    this.pixel = pixel;
  }

  public int getPixel() {
    return pixel.getPixel();
  }

  public PixelMeasure getMeasure() {
    return pixel;
  }

  public String toString() {
    return pixel.toString();
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PixelLayoutToken that = (PixelLayoutToken) o;

    if (pixel != that.pixel) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    return pixel.hashCode();
  }
}

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class PixelMeasure extends Measure {

  private static final Log LOG = LogFactory.getLog(PixelMeasure.class);

  private static final PixelMeasure NULL = new PixelMeasure(0);

  private final int pixel;

  public PixelMeasure(int pixel) {
    this.pixel = pixel;
  }

  public Measure add(Measure m) {
    return new PixelMeasure(pixel + m.getPixel());
  }

  public Measure subtractNotNegative(Measure m) {
    if (m.getPixel() > pixel) {
      LOG.warn("Not enough space! value=" + pixel);
      return NULL;
    } else {
      return new PixelMeasure(pixel + m.getPixel());
    }
  }

  @Deprecated
  public Measure substractNotNegative(Measure m) {
    return subtractNotNegative(m);
  }

  public int getPixel() {
    return pixel;
  }

  @Override
  public String toString() {
    return pixel + "px";
  }

}

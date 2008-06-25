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

/*
 * Date: 23.01.2008 20:21:08
 */
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

  public Measure substractNotNegative(Measure m) {
    if (m.getPixel() > pixel) {
      LOG.warn("Not enough space! value=" + pixel);
      return NULL;
    } else {
      return new PixelMeasure(pixel + m.getPixel());
    }
  }

  public int getPixel() {
    return pixel;
  }

  @Override
  public String toString() {
    return pixel + "px";
  }

}

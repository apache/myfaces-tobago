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
import org.apache.myfaces.tobago.util.LayoutUtil;

/*
 * User: lofwyr
 * Date: 23.01.2008 20:12:30
 */
public abstract class Measure {

  // todo: refactor and consolidate with LayoutToken

  public static Measure parse(String value) {
    if (StringUtils.isEmpty(value)) {
      return new PixelMeasure(0); // fixme: may return a "default measure", or is Pixel the default?
    }
    // XXX improve code sharing with LayoutTokens.parse
    if (LayoutUtil.isNumberAndSuffix(value.toLowerCase(), "px")) {
      return new PixelMeasure(Integer.parseInt(LayoutUtil.removeSuffix(value, "px")));
    }
    throw new IllegalArgumentException("Can't parse to any measure: '" + value + "'");
  }

  public abstract Measure add(Measure m);

  @Deprecated
  public abstract Measure substractNotNegative(Measure m);

  public abstract Measure subtractNotNegative(Measure m);

  public abstract int getPixel();
}

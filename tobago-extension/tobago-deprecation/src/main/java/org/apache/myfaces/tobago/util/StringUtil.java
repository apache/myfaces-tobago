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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.internal.util.Deprecation;

import java.util.List;

/**
 * @deprecated Please use StringUtils from tobago internal package, or commons-lang
 * @see org.apache.myfaces.tobago.internal.util.StringUtils
 * @see org.apache.commons.lang.StringUtils
 */
@Deprecated
public class StringUtil {

  private StringUtil() {
  }

  @Deprecated
  public static String firstToUpperCase(String string) {
    if (Deprecation.LOG.isWarnEnabled()) {
      Deprecation.LOG.warn("use commons-lang please");
    }
    return StringUtils.firstToUpperCase(string);
  }

  @Deprecated
  public static List<Integer> parseIntegerList(String integerList)
      throws NumberFormatException {
    if (Deprecation.LOG.isWarnEnabled()) {
      Deprecation.LOG.warn("use StringUtils please");
    }
   return StringUtils.parseIntegerList(integerList);
  }

  @Deprecated
  public static <T> String toString(List<T> list) {
    if (Deprecation.LOG.isWarnEnabled()) {
      Deprecation.LOG.warn("use StringUtils please");
    }
    return StringUtils.toString(list);
  }

  @Deprecated
  public static String escapeAccessKeyIndicator(String label) {
    if (Deprecation.LOG.isWarnEnabled()) {
      Deprecation.LOG.warn(label);
    }
    return StringUtils.escapeAccessKeyIndicator(label);
  }

}

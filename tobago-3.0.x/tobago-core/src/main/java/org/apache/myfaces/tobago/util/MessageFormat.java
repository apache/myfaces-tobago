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

import javax.faces.context.FacesContext;

/**
 * Helper class for the EL functions <code>tc:format1()</code> ... <code>tc:format9()</code>
 */
public class MessageFormat {

  public static String format(String pattern, Object param0) {
    return createMessageFormat(pattern).format(new Object[] {param0});
  }

  public static String format(String pattern, Object param0, Object param1) {
    return createMessageFormat(pattern).format(new Object[] {param0, param1});
  }

  public static String format(String pattern, Object param0, Object param1, Object param2) {
    return createMessageFormat(pattern).format(new Object[] {param0, param1, param2});
  }

  public static String format(String pattern, Object param0, Object param1, Object param2, Object param3) {
    return createMessageFormat(pattern).format(new Object[] {param0, param1, param2, param3});
  }

  public static String format(String pattern, Object param0, Object param1, Object param2, Object param3,
                               Object param4) {
    return createMessageFormat(pattern).format(new Object[] {param0, param1, param2, param3, param4});
  }

  public static String format(String pattern, Object param0, Object param1, Object param2, Object param3,
                               Object param4, Object param5) {
    return createMessageFormat(pattern).format(new Object[] {param0, param1, param2, param3, param4, param5});
  }

  public static String format(String pattern, Object param0, Object param1, Object param2, Object param3,
                               Object param4, Object param5, Object param6) {
    return createMessageFormat(pattern).format(new Object[] {param0, param1, param2, param3, param4, param5, param6});
  }

  public static String format(String pattern, Object param0, Object param1, Object param2, Object param3,
                               Object param4, Object param5, Object param6, Object param7) {
    return createMessageFormat(pattern).format(
        new Object[] {param0, param1, param2, param3, param4, param5, param6, param7});
  }

  public static String format(String pattern, Object param0, Object param1, Object param2, Object param3,
                               Object param4, Object param5, Object param6, Object param7, Object param8) {
    return createMessageFormat(pattern).format(
        new Object[] {param0, param1, param2, param3, param4, param5, param6, param7, param8});
  }

  private static java.text.MessageFormat createMessageFormat(String pattern) {
    return new java.text.MessageFormat(pattern, FacesContext.getCurrentInstance().getViewRoot().getLocale());
  }

}

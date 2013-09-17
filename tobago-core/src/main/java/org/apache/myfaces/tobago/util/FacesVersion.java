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

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

public enum FacesVersion {

  VERSION_11,
  VERSION_12,
  VERSION_20,
  VERSION_21,
  VERSION_22;

  private static FacesVersion currentVersion;
  private static boolean mojarra;
  private static boolean myfaces;

  static {
    try {
      currentVersion = VERSION_11;
      Application.class.getMethod("getExpressionFactory");
      currentVersion = VERSION_12;
      Application.class.getMethod("getDefaultValidatorInfo");
      currentVersion = VERSION_20;
      FacesContext.class.getMethod("isReleased");
      currentVersion = VERSION_21;
      Application.class.getMethod("getFlowHandler");
      currentVersion = VERSION_22;
    } catch (NoSuchMethodException e) {
      // ignore
    }

    mojarra = isAvailable("com.sun.faces.application.ApplicationImpl");
    myfaces = isAvailable("org.apache.myfaces.application.ApplicationImpl");

  }

  private static boolean isAvailable(String className) {
    try {
      try {
        Class.forName(className, false, Thread.currentThread().getContextClassLoader());
        return true;
      } catch (ClassNotFoundException e) {
        // ignore
        try {
          Class.forName(className, false, FacesVersion.class.getClassLoader());
          return true;
        } catch (ClassNotFoundException e1) {
          // ignore
        }
      }
    } catch (Exception e) {
      // ignore
    }
    return false;
  }

  /**
   * Does the JSF is version 1.2 or higher
   * @return Supports 1.2 or higher
   */
  public static boolean supports12() {
    return currentVersion == VERSION_12
        || currentVersion == VERSION_20
        || currentVersion == VERSION_21
        || currentVersion == VERSION_22;
  }

  /**
   * Does the JSF is version 2.0 or higher
   * @return Supports 2.0 or higher
   */
  public static boolean supports20() {
    return currentVersion == VERSION_20
        || currentVersion == VERSION_21
        || currentVersion == VERSION_22;
  }

  /**
   * Does the JSF is version 2.1 or higher
   * @return Supports 2.1 or higher
   */
  public static boolean supports21() {
    return currentVersion == VERSION_21
        || currentVersion == VERSION_22;
  }

  /**
   * Does the JSF is version 2.2 or higher
   * @return Supports 2.2 or higher
   */
  public static boolean supports22() {
    return currentVersion == VERSION_22;
  }

  public static boolean isMojarra() {
    return mojarra;
  }

  public static boolean isMyfaces() {
    return myfaces;
  }
}

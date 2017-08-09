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

package org.apache.myfaces.tobago.portlet;

import javax.portlet.PortletRequest;

/**
 * Static utility class for portlet-related operations.
 */
public final class PortletUtils {

  private static final boolean PORTLET_API_AVAILABLE;

  static {
    boolean result;
    try {
      result = PortletRequest.class != null;
    } catch (final NoClassDefFoundError e) {
      result = false;
    }
    PORTLET_API_AVAILABLE = result;
  }

  private PortletUtils() {
    // avoid instantiation
  }

  /**
   * The Portlet API is optional in the class path. We are only allowed to check instance of e. g. PortletRequest,
   * if the API exists.
   *
   * @return Are the Portlet classes available?
   */
  public static boolean isPortletApiAvailable() {
    return PORTLET_API_AVAILABLE;
  }

}

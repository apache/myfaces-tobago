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

package org.apache.myfaces.tobago.internal.util;

import jakarta.servlet.http.Part;

/**
 * Only needed for Servlet 3.0. Not needed for Servlet 3.1 or higher.
 * <p>
 * Basically taken from Apache Tomcat 8
 *
 * @deprecated since 5.0.0. Servlet 3.1 or higher is required.
 */
@Deprecated
public final class PartUtils {

  private PartUtils() {
    // to prevent instantiation
  }

  /**
   * This is a helper method, to get the original file name of the upload.
   * If you have at least Servlet 3.1, you wouldn't need this function.
   *
   * @since Tobago 3.0.0
   * @deprecated since 5.0.0. Servlet 3.1 or higher is required.
   */
  @Deprecated
  public static String getSubmittedFileName(final Part part) {
    return part.getSubmittedFileName();
  }
}

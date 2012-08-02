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

package org.apache.myfaces.tobago.context;

public enum Capability {

  /**
   * Support for the placeholder in HTML 5.
   */
  PLACEHOLDER,

  /**
   * Does the user agent support "application/xhtml+xml" as content type?
   */
  CONTENT_TYPE_XHTML,

  /**
   * In IE 8 and later, a compatibility mode can be activated in the user agent.
   * For Tobago pages this should not be activated. So this property can be used to
   * decide, if a warning should be rendered.
   */
  IE_COMPATIBILITY_MODE
}

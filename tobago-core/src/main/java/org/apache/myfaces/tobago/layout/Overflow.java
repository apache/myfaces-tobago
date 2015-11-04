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

public enum Overflow {

  auto,
  hidden,
  scroll;

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link Overflow#auto}
   */
  public static final Overflow AUTO = auto;

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link Overflow#hidden}
   */
  public static final Overflow HIDDEN = hidden;

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link Overflow#scroll}
   */
  public static final Overflow SCROLL = scroll;

  /**
   * Internal constant to use in annotations. Please use {@link Overflow#auto}
   */
  public static final String STRING_AUTO = "auto";

  /**
   * Internal constant to use in annotations. Please use {@link Overflow#hidden}
   */
  public static final String STRING_HIDDEN = "hidden";

  /**
   * Internal constant to use in annotations. Please use {@link Overflow#scroll}
   */
  public static final String STRING_SCROLL = "scroll";

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link Overflow:name}
   */
  public String getValue() {
    return name();
  }
}

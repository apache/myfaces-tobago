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

/**
 * Show Arrows, or not.
 *
 * @since 5.14.0, 6.6.0
 */
public enum Arrows {

  /**
   * Show arrows at this control.
   */
  show,

  /**
   * Hide arrows at this control.
   */
  hide,

  /**
   * Arrows will be shown automatically (currently the same as {@link #show}).
   */
  auto;

  public static final String SHOW = "show";
  public static final String HIDE = "hide";
  public static final String AUTO = "auto";

  /**
   * @param name Name of the Arrows.
   * @throws IllegalArgumentException When the name doesn't match any {@link Arrows}.
   */
  public static Arrows parse(final Object name) throws IllegalArgumentException {
    if (name == null) {
      return null;
    }
    if (name instanceof Arrows) {
      return (Arrows) name;
    }
    return valueOf(name.toString());
  }
}

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
 * Values for vertical alignments used with CSS.
 * <p>
 * Note: the enums are not capitalized, because of problems with {@link java.beans.PropertyEditor}.
 */
public enum VerticalAlign {
  top, bottom, middle;

  /**
   * Internal constant to use in annotations. Please use {@link VerticalAlign#top}
   */
  public static final String TOP = "top";

  /**
   * Internal constant to use in annotations. Please use {@link VerticalAlign#bottom}
   */
  public static final String BOTTOM = "bottom";

  /**
   * Internal constant to use in annotations. Please use {@link VerticalAlign#middle}
   */
  public static final String MIDDLE = "middle";
}

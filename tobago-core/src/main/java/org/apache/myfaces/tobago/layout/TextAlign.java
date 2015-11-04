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
 * Values for text alignments used with CSS.
 *
 * Note: the enums are not capitalized, because of problems with {@link java.beans.PropertyEditor}.
 */
public enum TextAlign {

  left,
  right,
  center,
  justify;

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link TextAlign#left}
   */
  public static final TextAlign LEFT = left;

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link TextAlign#right}
   */
  public static final TextAlign RIGHT = right;

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link TextAlign#center}
   */
  public static final TextAlign CENTER = center;

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link TextAlign#justify}
   */
  public static final TextAlign JUSTIFY = justify;

  /**
   * Internal constant to use in annotations. Please use {@link TextAlign#left}
   */
  public static final String STRING_LEFT = "left";

  /**
   * Internal constant to use in annotations. Please use {@link TextAlign#right}
   */
  public static final String STRING_RIGHT = "right";

  /**
   * Internal constant to use in annotations. Please use {@link TextAlign#center}
   */
  public static final String STRING_CENTER = "center";

  /**
   * Internal constant to use in annotations. Please use {@link TextAlign#justify}
   */
  public static final String STRING_JUSTIFY = "justify";

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link TextAlign:name}
   */
  public String getValue() {
    return name();
  }

  /**
   * @deprecated Since Tobago 3.0.0 Please use {@link TextAlign:valueOf}
   */
  @Deprecated
  public static TextAlign parse(final String string) {
    return valueOf(string);
  }

}

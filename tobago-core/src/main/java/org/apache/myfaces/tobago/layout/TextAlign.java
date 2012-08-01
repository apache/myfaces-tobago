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


import java.util.HashMap;
import java.util.Map;


public enum TextAlign {

  LEFT("left"),
  RIGHT("right"),
  CENTER("center"),
  JUSTIFY("justify");

  public static final String STRING_LEFT = "left";
  public static final String STRING_RIGHT = "right";
  public static final String STRING_CENTER = "center";
  public static final String STRING_JUSTIFY = "justify";

  private String value;

  TextAlign(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  private static final Map<String, TextAlign> MAPPING;

  static {
    MAPPING = new HashMap<String, TextAlign>();

    for (TextAlign textAlign : TextAlign.values()) {
      MAPPING.put(textAlign.getValue(), textAlign);
    }
  }

  public static TextAlign parse(String string) {
    if (string == null) {
      return null;
    }
    TextAlign value = MAPPING.get(string);
    if (value != null) {
      return value;
    } else {
      throw new IllegalArgumentException("Unknown value for TextAlign: '" + string + "'");
    }
  }

}

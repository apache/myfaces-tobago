package org.apache.myfaces.tobago.event;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.HashMap;
import java.util.Map;

public enum PageAction {

  /**
   * First page is requested
   */
  FIRST("First"),

  /**
   * Next page is requested
   */
  NEXT("Next"),

  /**
   * Previous page is requested
   */
  PREV("Prev"),

  /**
   * Last page is requested
   */
  LAST("Last"),

  /**
   * A specified row is requested
   */
  TO_ROW("ToRow"),

  /**
   * A specified page is requested
   */
  TO_PAGE("ToPage");

  private String token;

  PageAction(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
  
  private static final Map<String, PageAction> MAPPING;

  static {
    MAPPING = new HashMap<String, PageAction>();

    for (PageAction action : PageAction.values()) {
      MAPPING.put(action.getToken(), action);
    }
  }

  /**
   * 
   * @param name Name of the PageAction
   * @return The matching page action (can't be null).
   * @throws IllegalArgumentException When the name doesn't match any PageAction.
   */
  public static PageAction parse(String name) throws IllegalArgumentException {
    PageAction value = MAPPING.get(name);
    if (value != null) {
      return value;
    } else {
      throw new IllegalArgumentException("Unknown name for PageAction: '" + name + "'");
    }
  }
}

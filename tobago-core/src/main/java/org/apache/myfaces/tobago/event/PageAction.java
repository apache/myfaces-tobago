package org.apache.myfaces.tobago.event;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
  FIRST {
    public String getToken() {
      return "First";
    }
  },

  /**
   * Next page is requested
   */
  NEXT {
    public String getToken() {
      return "Next";
    }
  },

  /**
   * Previous page is requested
   */
  PREV {
    public String getToken() {
      return "Prev";
    }
  },

  /**
   * Last page is requested
   */
  LAST {
    public String getToken() {
      return "Last";
    }
  },

  /**
   * A specified row is requested
   */
  TO_ROW {
    public String getToken() {
      return "ToRow";
    }
  },

  /**
   * A specified page is requested
   */
  TO_PAGE {
    public String getToken() {
      return "ToPage";
    }
  };

  private static final Map<String, PageAction> MAPPING;

  static {
    MAPPING = new HashMap<String, PageAction>();

    for (PageAction action : values()) {
      MAPPING.put(action.getToken(), action);
    }
  }

  public static PageAction parse(String name) {
    PageAction value = MAPPING.get(name);
    if (value != null) {
      return value;
    } else {
      throw new IllegalArgumentException("Unknown name: " + name);
    }
  }

  public abstract String getToken();
}

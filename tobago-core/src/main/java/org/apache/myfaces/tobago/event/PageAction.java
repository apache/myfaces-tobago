package org.apache.myfaces.tobago.event;

import java.util.HashMap;
import java.util.Map;

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

public enum PageAction {

  FIRST {
    public String getToken() {
      return "First";
    }
  },
  NEXT {
    public String getToken() {
      return "Next";
    }
  },
  PREV {
    public String getToken() {
      return "Prev";
    }
  },
  LAST {
    public String getToken() {
      return "Last";
    }
  },
  TO_ROW {
    public String getToken() {
      return "ToRow";
    }
  },
  TO_PAGE {
    public String getToken() {
      return "ToPage";
    }
  };

  private static final Map<String, PageAction> mapping;
    
  static {
    mapping = new HashMap<String, PageAction>();

    for (PageAction action : values()) {
      mapping.put(action.getToken(), action);
    }
  }

  public static PageAction parse(String name) {
    PageAction value = mapping.get(name);
    if (value != null) {
      return value;
    } else {
      throw new IllegalArgumentException("Unknown name: " + name);
    }
  }

  public abstract String getToken();
}

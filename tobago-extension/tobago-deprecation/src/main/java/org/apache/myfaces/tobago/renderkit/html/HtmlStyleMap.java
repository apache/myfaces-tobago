package org.apache.myfaces.tobago.renderkit.html;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated
 * @see org.apache.myfaces.tobago.renderkit.css.Style
 */
@Deprecated
public class HtmlStyleMap extends HashMap<String, Object> {

  private static final Logger LOG = LoggerFactory.getLogger(HtmlStyleMap.class);
  private static final long serialVersionUID = 342607693971417143L;

  public Object put(String s, Object o) {
    if (o instanceof String && (s.equals("height") || s.equals("width"))) {
      String str = (String) o;
      if (str.endsWith("px")) {
        LOG.error("", new Exception());
        o = Integer.parseInt(str.substring(0, str.length() - 2));
      }
    }
    return super.put(s, o);
  }

  public Integer getInt(Object o) {
    Object obj = get(o);
    if (obj instanceof Integer) {
      return (Integer) obj;
    }
    if (obj == null) {
      return null;
    }
    LOG.error("", new Exception());
    return Integer.parseInt(obj.toString());
  }

  public String toString() {
    if (entrySet().isEmpty()) {
      return null;
    }
    StringBuilder buf = new StringBuilder();
    for (Map.Entry<String, Object> style : entrySet()) {
      buf.append(style.getKey());
      buf.append(":");
      buf.append(style.getValue());
      if (style.getValue() instanceof Integer && !"z-index".equals(style.getKey())) {
        buf.append("px; ");
      } else {
        buf.append("; ");
      }
    }
    return buf.toString();
  }
}

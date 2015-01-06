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

package org.apache.myfaces.tobago.internal.util;

import java.util.HashMap;
import java.util.Map;

public final class MimeTypeUtils {

  private static final Map<String, String> extensionTypeMap = new HashMap<String, String>();

  public static final String DEFAULT_MAPPING = ".gif:image/gif,.png:image/png,.jpg:image/jpeg,.js:text/javascript,"
      + ".css:text/css,.ico:image/vnd.microsoft.icon,.html:text/html,.htm:text/html,.map:application/json";

  private MimeTypeUtils() {
    // utils class
  }

  // init() is invoked by ResourceServlet.init(), this is at application startup.
  public static void init(String mimeTypeMapping) {
    if (mimeTypeMapping == null) {
      mimeTypeMapping = DEFAULT_MAPPING;
    }
    try {
      extensionTypeMap.clear();
      for (String typeMapping : mimeTypeMapping.split(",")) {
        int idx = typeMapping.indexOf(':');
        extensionTypeMap.put(typeMapping.substring(0, idx).trim(), typeMapping.substring(idx + 1).trim());
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid parameter 'mimeTypeMapping': \"" + mimeTypeMapping + "\"", e);
    }
  }

  public static String getMimeTypeForFile(final String file) {
    for (Map.Entry<String, String> entry : extensionTypeMap.entrySet()) {
      if (file.endsWith(entry.getKey())) {
        return entry.getValue();
      }
    }
    return null;
  }
}

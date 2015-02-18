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

import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.context.FacesContext;
import java.util.Map;

public final class MimeTypeUtils {

  private static Map<String, String> ADDITIONAL_MIME_TYPES = null;

  private MimeTypeUtils() {
    // utils class
  }

  // todo: maybe support more extensions
  public static String getMimeTypeForFile(final String file) {
    int length = file.length();
    if (file.charAt(length - 4) == '.') {
      if (file.charAt(length - 1) == 'g') {
        if (file.regionMatches(length - 3, "png", 0, 2)) {
          return "image/png";
        }
        if (file.regionMatches(length - 3, "jpg", 0, 2)) {
          return "image/jpeg";
        }
        if (file.endsWith("svg")) {
          return "image/svg+xml";
        }
      } else {
        if (file.endsWith("css")) {
          return "text/css";
        }
        if (file.endsWith("gif")) {
          return "image/gif";
        }
        if (file.endsWith("htm")) {
          return "text/html";
        }
        if (file.endsWith("ico")) {
          return "image/vnd.microsoft.icon";
        }
        if (file.endsWith("map")) {
          return "application/json";
        }
        if (file.endsWith("ttf")) {
          return "application/x-font-ttf";
        }
      }
    } else if (file.charAt(length - 3) == '.') {
      if (file.endsWith("js")) {
        return "text/javascript";
      }
    } else if (file.charAt(length - 5) == '.') {
      if (file.endsWith("woff")) {
        return "application/font-woff";
      }
      if (file.endsWith("html")) {
        return "text/html";
      }
    }

    if (ADDITIONAL_MIME_TYPES == null) {
      final TobagoConfig tobagoConfig = TobagoConfig.getInstance(FacesContext.getCurrentInstance());
      ADDITIONAL_MIME_TYPES = tobagoConfig.getMimeTypes();
    }

    final int index = file.lastIndexOf('.');
    if (index > -1) {
      String extension = file.substring(index + 1);
      return ADDITIONAL_MIME_TYPES.get(extension);
    }

    return null;
  }
}

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

import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * PRELIMINARY: MAY BE INTEGRATED IN THE {@link TobagoResponseWriter}
 * TBD
 */
public class StyleRenderUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private StyleRenderUtils() {
    // to prevent instantiation
  }

  /**
   * PRELIMINARY
   */
  public static void writeIdSelector(final TobagoResponseWriter writer, final String id) throws IOException {

    writer.writeText("#");

    final char[] chars = id.toCharArray();
    int last = 0;
    for (int i = 0; i < chars.length; i++) {
      final char c = chars[i];
      if (c == ':') {
        writer.writeText(chars, last, i - last);
        writer.writeText("\\:");
        last = i + 1;
      }
    }
    writer.writeText(chars, last, chars.length - last);
  }

  /**
   * PRELIMINARY
   */
  // not using writeText, because > must not be encoded!
  public static void writeSelector(final TobagoResponseWriter writer, final String selector) throws IOException {
    if (selector.contains("<")) {
      LOG.warn("Found invalid char < inside of style!");
      writer.write(selector.replaceAll("<", "&lt;"));
    } else {
      writer.write(selector);
    }
  }

  /**
   * PRELIMINARY
   */
  public static String encodeIdSelector(final String clientId) {
    return "#" + clientId.replaceAll(":", "\\\\:");
  }

}

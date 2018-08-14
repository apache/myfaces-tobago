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

import java.util.StringTokenizer;

/**
 * Basic helper type for the CSS3 Grid property.
 * Limitations: "auto" is not supported by IE 10/11.
 */
public class GridSpan {

  private Integer start;
  private Integer span;

  private GridSpan() {
  }

  public static GridSpan valueOf(final String string) {
    final GridSpan item = new GridSpan();
    final StringTokenizer tokenizer = new StringTokenizer(string, " /");
    if (tokenizer.hasMoreElements()) {
      item.start = Integer.parseInt(tokenizer.nextToken());
    }
    if (tokenizer.hasMoreElements()) {
      final String next = tokenizer.nextToken();
      if (next.equals("span")) {
        item.span = Integer.parseInt(tokenizer.nextToken());
      } else {
        item.span = Integer.parseInt(next) - item.start;
      }
    }
    return item;
  }

  public static GridSpan valueOf(final Integer start, final Integer span) {
    final GridSpan item = new GridSpan();
    item.start = start;
    item.span = span;
    return item;
  }

  public String encode() {
    if (start != null) {
      if (span != null && span != 1) {
        return start + "/span " + span;
      } else {
        return start.toString();
      }
    } else {
      if (span != null && span != 1) { // XXX "auto" not supported by MS IE
        return "auto/span " + span;
      } else {
        return "auto";
      }
    }
  }

  public Integer getStart() {
    return start;
  }

  public Integer getSpan() {
    return span;
  }
}

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

import org.apache.commons.lang.StringUtils;

/*
 * Date: Oct 30, 2006
 * Time: 10:26:27 PM
 */
public class ContentType {
  private String primaryType;
  private String subType;

  public ContentType(String contentType) {
    parse(contentType);
  }

  private void parse(String contentType) {
    // TODO parse Parameter
    String[] values = StringUtils.split(contentType, "/");
    if (values.length == 2) {
      primaryType = values[0];
      subType = values[1];
    } else {
      throw new IllegalArgumentException("ContentType '" + contentType + "' not recognized.");
    }
  }

  public String getPrimaryType() {
    return primaryType;
  }

  public String getSubType() {
    return subType;
  }

  public boolean match(ContentType contentType) {
    return primaryType.equalsIgnoreCase(contentType.getPrimaryType())
        && ("*".equals(subType) || subType.equalsIgnoreCase(contentType.getSubType()));
  }

  public String toString() {
    return primaryType + "/" + subType;
  }

  public static ContentType valueOf(String s) {
    return new ContentType(s);
  }
}

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

public class RelativeLayoutToken extends LayoutToken {

  static final String SUFFIX = "*";

  public static final String DEFAULT_TOKEN_STRING = "1*";
  public static final RelativeLayoutToken DEFAULT_INSTANCE = new RelativeLayoutToken(1);

  private int factor = 1;

  public RelativeLayoutToken(int factor) {
    this.factor = factor;
  }

  public int getFactor() {
    return factor;
  }

  public String toString() {
    return factor + SUFFIX;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RelativeLayoutToken that = (RelativeLayoutToken) o;

    if (factor != that.factor) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    return factor;
  }
}

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

/*
 * Date: May 2, 2007
 * Time: 4:29:28 PM
 */
public class PercentLayoutToken extends LayoutToken {

  static final String SUFFIX = "%";

  private int percent = 0;

  public PercentLayoutToken(int percent) {
    this.percent = percent;
  }

  public int getPercent() {
    return percent;
  }

  public String toString() {
    return percent + SUFFIX;
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PercentLayoutToken that = (PercentLayoutToken) o;

    if (percent != that.percent) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    return percent;
  }
}

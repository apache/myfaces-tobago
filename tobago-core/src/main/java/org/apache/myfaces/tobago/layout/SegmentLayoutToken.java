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

public class SegmentLayoutToken extends LayoutToken {

  private int columnSize = 1;

  public SegmentLayoutToken(final int columnSize) {
    if (columnSize < 1) {
      this.columnSize = 1;
    } else if (columnSize > 12) {
      this.columnSize = 12;
    } else {
      this.columnSize = columnSize;
    }
  }

  public int getColumnSize() {
    return columnSize;
  }

  public String toString() {
    return String.valueOf(columnSize);
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final SegmentLayoutToken that = (SegmentLayoutToken) o;

    if (columnSize != that.columnSize) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    return columnSize;
  }
}

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

/**
 * User: lofwyr
 * Date: 23.01.2008 20:18:45
 */
public class Position {

  private Measure left;
  private Measure top;

  public Position(Measure left, Measure top) {
    this.left = left;
    this.top = top;
  }

  public Position(String string) {
    int comma = string.indexOf(',');
    if (comma >= 0) { // found first comma
      left = Measure.parse(string.substring(0, comma));
      top = Measure.parse(string.substring(comma + 1));
    } else {
      throw new IllegalArgumentException("Can't parse to the position: '" + string + "'");
    }
  }

  public Measure getLeft() {
    return left;
  }

  public void setLeft(Measure left) {
    this.left = left;
  }

  public Measure getTop() {
    return top;
  }

  public void setTop(Measure top) {
    this.top = top;
  }

  @Override
  public String toString() {
    return new StringBuilder().append(left).append(',').append(top).toString();
  }
}

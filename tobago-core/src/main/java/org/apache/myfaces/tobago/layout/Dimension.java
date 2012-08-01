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
 * Date: 23.01.2008 20:18:45
 */
public class Dimension {

  private Measure width;
  private Measure height;

  public Dimension(Measure width, Measure height) {
    this.width = width;
    this.height = height;
  }

  public Dimension(String string) {
    int comma = string.indexOf(',');
    if (comma >= 0) { // found first comma
      width = Measure.parse(string.substring(0, comma));
      height = Measure.parse(string.substring(comma + 1));
    } else {
      throw new IllegalArgumentException("Can't parse to the dimension: '" + string + "'");
    }
  }

  public Measure getWidth() {
    return width;
  }

  public void setWidth(Measure width) {
    this.width = width;
  }

  public Measure getHeight() {
    return height;
  }

  public void setHeight(Measure height) {
    this.height = height;
  }

  @Override
  public String toString() {
    return new StringBuilder().append(width).append(',').append(height).toString();
  }
}

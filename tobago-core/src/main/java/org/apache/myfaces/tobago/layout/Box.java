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

public class Box {

  private Position position;
  private Dimension dimension;

  public Box(Position position, Dimension dimension) {
    this.position = position;
    this.dimension = dimension;
  }

  public Box(String string) {
    int comma = string.indexOf(',');
    if (comma >= 0) { // found first comma
      comma = string.indexOf(',', comma + 1);
      if (comma >= 0) { // found second comma
        position = new Position(string.substring(0, comma));
        dimension = new Dimension(string.substring(comma + 1));
        return;
      }
    }
    throw new IllegalArgumentException("Can't parse to a box: '" + string + "'");
  }

  /**
   * Convenience method to get left + width.
   */
  public Measure getRight() {
    return position.getLeft().add(dimension.getWidth());
  }

  /**
   * Convenience method to get top + height.
   */
  public Measure getBottom() {
    return position.getTop().add(dimension.getHeight());
  }

  public Measure getLeft() {
    return position.getLeft();
  }

  public void setLeft(Measure left) {
    position.setLeft(left);
  }

  public Measure getTop() {
    return position.getTop();
  }

  public void setTop(Measure top) {
    position.setTop(top);
  }

  public Measure getWidth() {
    return dimension.getWidth();
  }

  public void setWidth(Measure width) {
    dimension.setWidth(width);
  }

  public Measure getHeight() {
    return dimension.getHeight();
  }

  public void setHeight(Measure height) {
    dimension.setHeight(height);
  }

  @Override
  public String toString() {
    return new StringBuilder().append(position).append(',').append(dimension).toString();
  }
}

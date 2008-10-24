package org.apache.myfaces.tobago.layout.grid;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.layout.Component;
import org.apache.myfaces.tobago.layout.Constraints;
import org.apache.myfaces.tobago.layout.Dimension;
import org.apache.myfaces.tobago.layout.Measure;

/*
 * User: lofwyr
 * Date: 13.02.2008 20:47:28
 */
public class GridConstraints implements Constraints {

  private int columnSpan = 1;

  private int rowSpan = 1;

  private Dimension minimumSize; // TODO = new Dimension();

  private Dimension preferedSize; // TODO = new Dimension();

  private Dimension maximumSize; // TODO = new Dimension();

  private Measure width;

  private Measure height;

  /**
   * Convenience method to get the correct layout constraints from the component.
   * If the constraints object doesn't exists it will be created a default and set to the component.
   */
  public static GridConstraints getConstraints(Component component) {
    GridConstraints constraints
        = (GridConstraints) component.getConstraints();
    if (constraints == null) {
      constraints = new GridConstraints();
      component.setConstraints(constraints);
    }
    return constraints;
  }

  public int getColumnSpan() {
    return columnSpan;
  }

  public void setColumnSpan(int columnSpan) {
    this.columnSpan = columnSpan;
  }

  public int getRowSpan() {
    return rowSpan;
  }

  public void setRowSpan(int rowSpan) {
    this.rowSpan = rowSpan;
  }

  public Dimension getMinimumSize() {
    return minimumSize;
  }

  public void setMinimumSize(Dimension minimumSize) {
    this.minimumSize = minimumSize;
  }

  public Dimension getPreferedSize() {
    return preferedSize;
  }

  public void setPreferedSize(Dimension preferedSize) {
    this.preferedSize = preferedSize;
  }

  public Dimension getMaximumSize() {
    return maximumSize;
  }

  public void setMaximumSize(Dimension maximumSize) {
    this.maximumSize = maximumSize;
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
}

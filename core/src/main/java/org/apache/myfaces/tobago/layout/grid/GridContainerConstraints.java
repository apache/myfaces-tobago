package org.apache.myfaces.tobago.layout.grid;

import org.apache.myfaces.tobago.layout.ContainerConstraints;
import org.apache.myfaces.tobago.layout.Dimension;
import org.apache.myfaces.tobago.layout.LayoutContainer;

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

/**
 * User: lofwyr
 * Date: 14.02.2008
 */
public class GridContainerConstraints implements ContainerConstraints {

  public static final String NAME = "grid";

  private Dimension beginInset;// TODO = new Dimension();

  private Dimension endInset;// TODO = new Dimension();

  /**
   * Convenience method to get the correct layout constraints from the component.
   * If the constraints object doesn't exists it will be created a default and set to the component.
   */
  public static GridContainerConstraints getConstraints(LayoutContainer container) {
    GridContainerConstraints constraints
        = (GridContainerConstraints) container.getContainerConstraints(NAME);
    if (constraints == null) {
      constraints = new GridContainerConstraints();
      container.setContainerConstraints(NAME, constraints);
    }
    return constraints;
  }

  public Dimension getBeginInset() {
    return beginInset;
  }

  public void setBeginInset(Dimension beginInset) {
    this.beginInset = beginInset;
  }

  public Dimension getEndInset() {
    return endInset;
  }

  public void setEndInset(Dimension endInset) {
    this.endInset = endInset;
  }
}

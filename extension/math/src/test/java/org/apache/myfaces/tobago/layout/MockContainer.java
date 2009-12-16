package org.apache.myfaces.tobago.layout;

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

import java.util.ArrayList;
import java.util.List;

public class MockContainer extends MockComponent implements LayoutContainer {

  private LayoutManager layoutManager;

  private List<LayoutComponent> components;

  private Measure leftOffset = Measure.ZERO;
  private Measure topOffset = Measure.ZERO;

  private Measure rightOffset = Measure.ZERO;
  private Measure bottomOffset = Measure.ZERO;

  public MockContainer() {
    components = new ArrayList<LayoutComponent>();
  }

  public List<LayoutComponent> getComponents() {
    return components;
  }

  public void setLayoutManager(LayoutManager layoutManager) {
    this.layoutManager = layoutManager;
  }

  public LayoutManager getLayoutManager() {
    return layoutManager;
  }

  public Measure getLeftOffset() {
    return leftOffset;
  }

  public void setLeftOffset(Measure leftOffset) {
    this.leftOffset = leftOffset;
  }

  public Measure getTopOffset() {
    return topOffset;
  }

  public void setTopOffset(Measure topOffset) {
    this.topOffset = topOffset;
  }

  public Measure getRightOffset() {
    return rightOffset;
  }

  public void setRightOffset(Measure rightOffset) {
    this.rightOffset = rightOffset;
  }

  public Measure getBottomOffset() {
    return bottomOffset;
  }

  public void setBottomOffset(Measure bottomOffset) {
    this.bottomOffset = bottomOffset;
  }
}

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

public interface LayoutComponent {

  Integer getColumnSpan();
  void setColumnSpan(Integer columnSpan);

  Integer getRowSpan();
  void setRowSpan(Integer rowSpan);

  Measure getWidth();
  void setWidth(Measure width);

  Measure getHeight();
  void setHeight(Measure height);

  void setMinimumWidth(Measure minimumWidth);

  void setMinimumHeight(Measure minimumHeight);

  void setPreferredWidth(Measure preferredWidth);

  void setPreferredHeight(Measure preferredHeight);

  void setMaximumWidth(Measure maximumWidth);

  void setMaximumHeight(Measure maximumHeight);

  Measure getLeft();
  void setLeft(Measure left);

  Measure getTop();
  void setTop(Measure top);

  Integer getHorizontalIndex();
  void setHorizontalIndex(Integer horizontalIndex);

  Integer getVerticalIndex();
  void setVerticalIndex(Integer verticalIndex);

  Display getDisplay();
  void setDisplay(Display display);
}

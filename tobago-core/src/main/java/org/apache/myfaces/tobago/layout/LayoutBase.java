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

import org.apache.myfaces.tobago.config.Configurable;

/**
 * A LayoutBase is the technical base for LayoutComponent and LayoutContainer.
 */
public interface LayoutBase extends Configurable {

  Measure getWidth();

  void setWidth(Measure width);

  Measure getHeight();

  void setHeight(Measure height);

  Measure getCurrentWidth();

  void setCurrentWidth(Measure width);

  Measure getCurrentHeight();

  void setCurrentHeight(Measure height);

  Measure getMinimumWidth();

  void setMinimumWidth(Measure minimumWidth);

  Measure getMinimumHeight();

  void setMinimumHeight(Measure minimumHeight);

  void setPreferredWidth(Measure preferredWidth);

  Measure getPreferredWidth();

  void setPreferredHeight(Measure preferredHeight);

  Measure getPreferredHeight();

  Measure getMaximumWidth();

  void setMaximumWidth(Measure maximumWidth);

  Measure getMaximumHeight();

  void setMaximumHeight(Measure maximumHeight);

  Measure getLeft();

  void setLeft(Measure left);

  Measure getTop();

  void setTop(Measure top);

  Measure getMarginLeft();

  void setMarginLeft(Measure marginLeft);

  Measure getMarginRight();

  void setMarginRight(Measure marginRight);

  Measure getMarginTop();

  void setMarginTop(Measure marginTop);

  Measure getMarginBottom();

  void setMarginBottom(Measure marginBottom);


}

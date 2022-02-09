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

public interface LayoutBox extends Configurable {

  Measure getBorderLeft();

  void setBorderLeft(Measure borderLeft);

  Measure getBorderRight();

  void setBorderRight(Measure borderRight);

  Measure getBorderTop();

  void setBorderTop(Measure borderTop);

  Measure getBorderBottom();

  void setBorderBottom(Measure borderBottom);

  Measure getPaddingLeft();

  void setPaddingLeft(Measure paddingLeft);

  Measure getPaddingRight();

  void setPaddingRight(Measure paddingRight);

  Measure getPaddingTop();

  void setPaddingTop(Measure paddingTop);

  Measure getPaddingBottom();

  void setPaddingBottom(Measure paddingBottom);

}

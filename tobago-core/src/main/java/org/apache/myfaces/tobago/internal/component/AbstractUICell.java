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

package org.apache.myfaces.tobago.internal.component;

/**
 * @deprecated The Cell is deprecated since Tobago 1.5.0
 */
@Deprecated
public abstract class AbstractUICell extends AbstractUIPanel {

  public abstract Integer getColumnSpan();

  public abstract void setColumnSpan(Integer columnSpan);

  public abstract Integer getRowSpan();

  public abstract void setRowSpan(Integer rowSpan);

  public Integer getSpanX() {
    return getColumnSpan();
  }

  public void setSpanX(final Integer spanX) {
    setColumnSpan(spanX);
  }

  public Integer getSpanY() {
    return getRowSpan();
  }

  public void setSpanY(final Integer spanY) {
    setRowSpan(spanY);
  }
}

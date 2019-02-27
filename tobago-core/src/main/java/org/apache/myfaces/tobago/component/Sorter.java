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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.event.SortActionEvent;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.internal.util.SortingUtils;

import java.util.Comparator;

/**
 * @deprecated since 4.4.0. Please use {@link SortingUtils}
 */
@Deprecated
public class Sorter {

  private Comparator comparator;

  /**
   * @deprecated since 2.0.7, please use {@link #perform(org.apache.myfaces.tobago.internal.component.AbstractUISheet)}
   */
  @Deprecated
  public void perform(final SortActionEvent sortEvent) {
    SortingUtils.sort((AbstractUISheet) sortEvent.getComponent(), comparator);
  }

  /**
   * @deprecated since 4.4.0. Please use {@link SortingUtils#sort}
   */
  @Deprecated
  public void perform(final AbstractUISheet sheet) {
    SortingUtils.sort(sheet, comparator);
  }

  public Comparator getComparator() {
    return comparator;
  }

  public void setComparator(final Comparator comparator) {
    this.comparator = comparator;
  }
}

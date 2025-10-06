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

package org.apache.myfaces.tobago.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SortedColumnList implements Serializable {
  private final int max;
  private final boolean showNumbers;
  private final List<SortedColumn> list;
  private int toBeSortedLevel;

  public SortedColumnList(final int max) {
    this.max = Math.max(max, 1);
    this.showNumbers = max > 1;
    this.list = new ArrayList<>(max);
    this.toBeSortedLevel = 0;
  }

  public boolean isEmpty() {
    return list.size() == 0;
  }

  public SortedColumn getFirst() {
    return list.size() > 0 ? list.get(0) : null;
  }

  public void updateSortState(final String columnId) {
    if (getFirst() != null && Objects.equals(getFirst().getId(), columnId)) {
      getFirst().toggle();
      toBeSortedLevel = Math.max(1, toBeSortedLevel);
    } else {
      final int index = indexOf(columnId);
      if (index >= 0) {
        list.remove(index);
        toBeSortedLevel--;
      }
      list.add(0, new SortedColumn(columnId, true));
      toBeSortedLevel++;
      checkSize();
    }
  }

  /**
   * @deprecated Only for backward compatibility of deprecated functions!
   */
  @Deprecated
  public void add(final String columnId, final boolean ascending) {
    list.add(new SortedColumn(columnId, ascending));
    toBeSortedLevel++;
    checkSize();
  }

  public SortedColumn get(int index) {
    return list.get(index);
  }

  private void checkSize() {
    while (size() > max) {
      list.remove(size() - 1);
    }
    if (toBeSortedLevel > max) {
      toBeSortedLevel = max;
    }
  }

  public int size() {
    return list.size();
  }

  public void clear() {
    list.clear();
  }

  /**
   * Indicates that every column is sorted and a SortActionEvent is not executed in this request.
   */
  public void sorted() {
    toBeSortedLevel = 0;
  }

  public int indexOf(String id) {
    for (int i = 0; i < size(); i++) {
      SortedColumn sortedColumn = list.get(i);
      if (Objects.equals(sortedColumn.getId(), id)) {
        return i;
      }
    }
    return -1;
  }

  public boolean isShowNumbers() {
    return showNumbers;
  }

  /**
   * @return the number of levels (columns) which must be sorted this request
   */
  public int getToBeSortedLevel() {
    return toBeSortedLevel;
  }

  public void setToBeSortedLevel(int toBeSortedLevel) {
    this.toBeSortedLevel = toBeSortedLevel;
  }
}

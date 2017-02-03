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

import org.apache.myfaces.tobago.internal.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A list of positive integers with sum 12 (separated by a semicolon ";"). Is used for the 12 columns partitioning.
 */
public final class ColumnPartition implements Serializable {

  private static final Integer[] INTEGER_12 = new Integer[]{12};
  public static final ColumnPartition PARTITION_12 = new ColumnPartition(INTEGER_12);

  private Integer[] parts;

  public ColumnPartition() {
    this(new Integer[0]);
  }

  public ColumnPartition(Integer... parts) {
    setParts(parts);
  }

  public static ColumnPartition valueOf(String string) {
    final List<Integer> integers = StringUtils.parseIntegerList(string, ";");
    return new ColumnPartition(integers.toArray(new Integer[integers.size()]));
  }

  public static ColumnPartition valueOf(Object object) {
    if (object instanceof String) {
      return valueOf((String) object);
    } else if (object instanceof Integer[]) {
      return new ColumnPartition((Integer[]) object);
    } else if (object != null) {
      return valueOf(object.toString());
    } else {
      return PARTITION_12;
    }
  }

  private boolean checkSum(final Integer[] summands) {
    if (summands == null || summands.length == 0) {
      return false;
    }
    int sum = 0;
    for (int summand : summands) {
      if (summand < 1) {
        return false;
      }
      sum += summand;
      if (sum > 12) {
        return false;
      }
    }
    return sum == 12;
  }

  private Integer[] createParts(Integer[] summands) {
    return createParts(Arrays.asList(summands));
  }

  private Integer[] createParts(List<Integer> summands) {
    List<Integer> list = new ArrayList<Integer>();
    if (summands == null || summands.size() == 0) {
      return INTEGER_12;
    }
    int sum = 0;
    for (int summand : summands) {
      if (summand < 1) {
        summand = 1;
      }
      if (sum + summand > 12) {
        break;
      }
      sum += summand;
      list.add(summand);
    }
    if (sum < 12) {
      list.add(12 - sum);
    }
    return list.toArray(new Integer[list.size()]);
  }

  public Integer[] getParts() {
    return parts;
  }

  private void setParts(Integer[] parts) {
    if (checkSum(parts)) {
      this.parts = parts;
    } else {
      this.parts = createParts(parts);
    }
  }

  public int getSize() {
    return parts.length;
  }

  public int getPart(final int column) {
    return parts[column];
  }

  @Override
  public String toString() {
    return Arrays.toString(parts);
  }
}

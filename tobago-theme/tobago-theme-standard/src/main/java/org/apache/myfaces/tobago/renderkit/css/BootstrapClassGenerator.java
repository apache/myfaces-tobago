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

package org.apache.myfaces.tobago.renderkit.css;

import org.apache.myfaces.tobago.layout.ColumnPartition;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 3.0.0
 */
public class BootstrapClassGenerator {

  private static final BootstrapClass[] EXTRA_SMALL = new BootstrapClass[]{
      BootstrapClass.COL_XS_1, BootstrapClass.COL_XS_2, BootstrapClass.COL_XS_3, BootstrapClass.COL_XS_4,
      BootstrapClass.COL_XS_5, BootstrapClass.COL_XS_6, BootstrapClass.COL_XS_7, BootstrapClass.COL_XS_8,
      BootstrapClass.COL_XS_9, BootstrapClass.COL_XS_10, BootstrapClass.COL_XS_11, BootstrapClass.COL_XS_12,
  };
  private static final BootstrapClass[] SMALL = new BootstrapClass[]{
      BootstrapClass.COL_SM_1, BootstrapClass.COL_SM_2, BootstrapClass.COL_SM_3, BootstrapClass.COL_SM_4,
      BootstrapClass.COL_SM_5, BootstrapClass.COL_SM_6, BootstrapClass.COL_SM_7, BootstrapClass.COL_SM_8,
      BootstrapClass.COL_SM_9, BootstrapClass.COL_SM_10, BootstrapClass.COL_SM_11, BootstrapClass.COL_SM_12,
  };
  private static final BootstrapClass[] MEDIUM = new BootstrapClass[]{
      BootstrapClass.COL_MD_1, BootstrapClass.COL_MD_2, BootstrapClass.COL_MD_3, BootstrapClass.COL_MD_4,
      BootstrapClass.COL_MD_5, BootstrapClass.COL_MD_6, BootstrapClass.COL_MD_7, BootstrapClass.COL_MD_8,
      BootstrapClass.COL_MD_9, BootstrapClass.COL_MD_10, BootstrapClass.COL_MD_11, BootstrapClass.COL_MD_12,
  };
  private static final BootstrapClass[] LARGE = new BootstrapClass[]{
      BootstrapClass.COL_LG_1, BootstrapClass.COL_LG_2, BootstrapClass.COL_LG_3, BootstrapClass.COL_LG_4,
      BootstrapClass.COL_LG_5, BootstrapClass.COL_LG_6, BootstrapClass.COL_LG_7, BootstrapClass.COL_LG_8,
      BootstrapClass.COL_LG_9, BootstrapClass.COL_LG_10, BootstrapClass.COL_LG_11, BootstrapClass.COL_LG_12,
  };

  private ColumnPartition extraSmall;
  private ColumnPartition small;
  private ColumnPartition medium;
  private ColumnPartition large;

  private int index = 0;

  public BootstrapClassGenerator(
      final ColumnPartition extraSmall, final ColumnPartition small, final ColumnPartition medium,
      final ColumnPartition large) {
    this.extraSmall = extraSmall;
    this.small = small;
    this.medium = medium;
    this.large = large;
  }

  public void reset() {
    index = 0;
  }

  public void next() {
    index++;
  }

  public BootstrapClass[] generate() {
    ArrayList<BootstrapClass> result = new ArrayList<BootstrapClass>(4);
    generate(result, extraSmall, EXTRA_SMALL);
    generate(result, small, SMALL);
    generate(result, medium, MEDIUM);
    generate(result, large, LARGE);
    return result.toArray(new BootstrapClass[result.size()]);
  }

  private void generate(
      final List<BootstrapClass> result, final ColumnPartition partition, final BootstrapClass[] values) {
    if (partition != null) {
      result.add(values[partition.getPart(index % partition.getSize()) - 1]);
    }
  }
}

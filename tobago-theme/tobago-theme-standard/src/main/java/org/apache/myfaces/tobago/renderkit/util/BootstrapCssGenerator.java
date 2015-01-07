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

package org.apache.myfaces.tobago.renderkit.util;

import org.apache.myfaces.tobago.layout.ColumnPartition;
import org.apache.myfaces.tobago.renderkit.css.Css;

public class BootstrapCssGenerator {

  private ColumnPartition extraSmall;
  private ColumnPartition small;
  private ColumnPartition medium;
  private ColumnPartition large;

  private int index = 0;

  public BootstrapCssGenerator(
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

  public void generate(final Css css) {
    generate(css, extraSmall, "col-xs-");
    generate(css, small, "col-sm-");
    generate(css, medium, "col-md-");
    generate(css, large, "col-lg-");
  }

  private void generate(final Css css, final ColumnPartition partition, final String prefix) {
    if (partition != null) {
      css.add(prefix + partition.getPart(index % partition.getSize()));
    }
  }
}

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

package org.apache.myfaces.tobago.example.reference;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.BoundedRangeModel;

public class Progress {
  private DefaultBoundedRangeModel progress = new DefaultBoundedRangeModel(0, 0, 0, 100);
  private boolean update;

  public boolean isUpdate() {
    update = !update;
    if (update) {
      int value = progress.getValue();
      if (value < progress.getMaximum()) {
        value = value+10;
        progress.setValue(value);
      }
    }
    return update;
  }

  public void setUpdate(boolean update) {
    this.update = update;
  }

  public BoundedRangeModel getProgress() {
    return progress;
  }

  public String reset() {
    progress.setValue(0);
    return "reset";
  }
}

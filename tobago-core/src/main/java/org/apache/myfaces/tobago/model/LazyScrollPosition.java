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

import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;

@Preliminary
public class LazyScrollPosition implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private Integer[] data = new Integer[]{0, Integer.MIN_VALUE, 0};

  public String encode() {
    return JsonUtils.encode(data);
  }

  public void clear() {
    data[0] = 0;
    data[1] = Integer.MIN_VALUE;
    data[2] = 0;
  }

  public void update(final String value) {
    if (StringUtils.isBlank(value)) {
      clear();
    } else {
      data = JsonUtils.decodeIntegerArray(value).toArray(data);
    }
  }

  public Integer getFirstVisibleRow() {
    return data[0];
  }

  public void setFirstVisibleRow(final Integer firstVisibleRow) {
    this.data[0] = firstVisibleRow;
  }

  /**
   * @return scrollTop form first visible row
   */
  public Integer getTop() {
    return data[1];
  }

  public void setTop(final Integer top) {
    this.data[1] = top;
  }

  public Integer getLeft() {
    return data[2];
  }

  public void setLeft(final Integer left) {
    this.data[2] = left;
  }
}

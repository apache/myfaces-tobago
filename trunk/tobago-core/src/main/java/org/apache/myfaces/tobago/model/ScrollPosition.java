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

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class ScrollPosition implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(ScrollPosition.class);

  private Integer left;
  private Integer top;

  public String encode() {
    return (left != null ? left : "0") + ";" + (top != null ? top : "0");
  }

  public void clear() {
    top = null;
    left = null;
  }

  public void update(final String value) {
    if (StringUtils.isBlank(value)) {
      top = null;
      left = null;
    } else {
      final int sep = value.indexOf(";");
      if (sep == -1) {
        LOG.warn("Can't parse: '{}'", value);
        return;
      }
      left = Integer.parseInt(value.substring(0, sep));
      top = Integer.parseInt(value.substring(sep + 1));
    }
  }

  public Integer getLeft() {
    return left;
  }

  public void setLeft(Integer left) {
    this.left = left;
  }

  public Integer getTop() {
    return top;
  }

  public void setTop(Integer top) {
    this.top = top;
  }
}

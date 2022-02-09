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

/**
 * @deprecated since 1.5.0, please configure constraints for the page size with a tc:gridLayoutConstraints tag inside
 * the tc:page tag.
 */
@Deprecated
public class PageStateImpl implements PageState, Serializable {

  private int clientWidth;
  private int clientHeight;

  public int getClientWidth() {
    return clientWidth;
  }

  public void setClientWidth(final int clientWidth) {
    this.clientWidth = clientWidth;
  }

  public int getClientHeight() {
    return clientHeight;
  }

  public void setClientHeight(final int clientHeight) {
    this.clientHeight = clientHeight;
  }
}

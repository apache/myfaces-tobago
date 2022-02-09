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

package org.apache.myfaces.tobago.example.addressbook.web;

import org.apache.myfaces.tobago.model.PageStateImpl;

/**
 * @deprecated since 1.5.0, please configure constraints for the page size with a tc:gridLayoutConstraints tag
 * inside the tc:page tag.
 */
@Deprecated
public class Layout extends PageStateImpl {

  private int width;
  private int height;
  private int minimumWidth;
  private int minimumHeight;
  private int maximumWidth;
  private int maximumHeight;

  public int getWidth() {
    final int clientWidth = getClientWidth();
    if (clientWidth != 0) {
      if (clientWidth < minimumWidth) {
        return minimumWidth;
      } else if (clientWidth > maximumWidth) {
        return maximumWidth;
      }
      return clientWidth;
    }
    return width;
  }

  public void setWidth(final int width) {
    this.width = width;
  }

  public int getHeight() {
    final int clientHeight = getClientHeight();
    if (clientHeight != 0) {
      if (clientHeight < minimumHeight) {
        return minimumHeight;
      } else if (clientHeight > maximumHeight) {
        return maximumHeight;
      }
      return clientHeight;
    }
    return height;
  }

  public void setHeight(final int height) {
    this.height = height;
  }

  public int getMinimumWidth() {
    return minimumWidth;
  }

  public void setMinimumWidth(final int minimumWidth) {
    this.minimumWidth = minimumWidth;
  }

  public int getMinimumHeight() {
    return minimumHeight;
  }

  public void setMinimumHeight(final int minimumHeight) {
    this.minimumHeight = minimumHeight;
  }

  public int getMaximumWidth() {
    return maximumWidth;
  }

  public void setMaximumWidth(final int maximumWidth) {
    this.maximumWidth = maximumWidth;
  }

  public int getMaximumHeight() {
    return maximumHeight;
  }

  public void setMaximumHeight(final int maximumHeight) {
    this.maximumHeight = maximumHeight;
  }
}


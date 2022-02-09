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

/**
 * @deprecated since 1.5.0, please configure constraints for the page size with a tc:gridLayoutConstraints tag inside
 * the tc:page tag.
 */
@Deprecated
public interface PageState {

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  int getClientWidth();

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  void setClientWidth(int width);

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  int getClientHeight();

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  void setClientHeight(int height);
}

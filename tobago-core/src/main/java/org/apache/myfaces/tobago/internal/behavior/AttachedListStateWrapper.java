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

package org.apache.myfaces.tobago.internal.behavior;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

// todo: clean up (is a copy of MyFaces, but not all stuff is refactored)

/**
 * @since 3.0.0
 */
class AttachedListStateWrapper implements Serializable {

  @Serial
  private static final long serialVersionUID = -3958718149793179776L;

  private List<Object> wrappedStateList;

  AttachedListStateWrapper(final List<Object> wrappedStateList) {
    this.wrappedStateList = wrappedStateList;
  }

  List<Object> getWrappedStateList() {
    return wrappedStateList;
  }
}

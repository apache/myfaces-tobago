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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.SupportsAutoSpacing;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.sanitizer.SanitizeMode;

import jakarta.faces.component.UIOutput;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.OutTagDeclaration}
 */
public abstract class AbstractUIOut extends UIOutput implements SupportsLabelLayout, Visual, SupportsAutoSpacing {

  private transient boolean nextToRenderIsLabel;

  public abstract boolean isEscape();

  public abstract boolean isKeepLineBreaks();

  /**
   * @deprecated since 4.0.0, please use Visual#isPlain()
   */
  @Deprecated
  public abstract boolean isCreateSpan();

  /**
   * @deprecated since 4.3.0, please use Visual#isPlain()
   */
  @Deprecated
  public abstract boolean isCompact();

  public abstract SanitizeMode getSanitize();

  @Override
  public boolean isNextToRenderIsLabel() {
    return nextToRenderIsLabel;
  }

  @Override
  public void setNextToRenderIsLabel(final boolean nextToRenderIsLabel) {
    this.nextToRenderIsLabel = nextToRenderIsLabel;
  }
}

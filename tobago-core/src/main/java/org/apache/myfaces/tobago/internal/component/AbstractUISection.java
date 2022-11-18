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
import org.apache.myfaces.tobago.util.ComponentUtils;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.SectionTagDeclaration}
 */
public abstract class AbstractUISection extends AbstractUICollapsiblePanel implements SupportsAutoSpacing {

  public abstract String getLabel();

  public abstract Integer getLevel();

  public abstract void setLevel(Integer level);

  public abstract String getImage();

  @Override
  public void encodeBegin(final FacesContext context) throws IOException {
    final Integer level = getLevel();
    if (level == null) {
      final AbstractUISection section = ComponentUtils.findAncestor(getParent(), AbstractUISection.class);
      if (section != null) {
        setLevel(section.getLevel() + 1);
      } else {
        setLevel(1);
      }
    } else if (level < 1) {
      setLevel(1);
    }

    super.encodeBegin(context);
  }
}

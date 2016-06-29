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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public abstract class AbstractUISection extends AbstractUICollapsiblePanel {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUISection.class);

  private int level;

  public String getLabelToRender() {

    final UIComponent facet = ComponentUtils.getFacet(this, Facets.label);
    if (facet instanceof UIOutput) {
      return String.valueOf(((UIOutput) facet).getValue());
    } else if (facet != null) {
      LOG.warn("Wrong type: " + facet.getClass().getName());
    }

    return getLabel();
  }

  public abstract String getLabel();

  public abstract String getImage();

  @Override
  public void encodeBegin(FacesContext context) throws IOException {

    if (getLevel() == 0) {
      final AbstractUISection section = ComponentUtils.findAncestor(getParent(), AbstractUISection.class);
      if (section != null) {
        setLevel(section.getLevel() + 1);
      } else {
        setLevel(1);
      }
    }

    super.encodeBegin(context);
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }
}

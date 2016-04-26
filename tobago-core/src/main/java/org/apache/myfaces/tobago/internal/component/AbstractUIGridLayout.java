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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.internal.layout.OriginCell;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

public abstract class AbstractUIGridLayout extends AbstractUILayoutBase implements Visual {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIGridLayout.class);

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.GridLayout";

  private Grid grid;

  /**
   * Initialize the grid and remove the current width and height values from the component, recursively.
   */
  public void init() {

    final UIComponent parent = getParent();
    if (!parent.isRendered()) {
      return;
    }

    grid = new Grid(LayoutTokens.parse(getColumns()), LayoutTokens.parse(getRows()));

    final List<UIComponent> components = LayoutUtils.findLayoutChildren(parent);
    for (final UIComponent component : components) {
      final int columnSpan = ComponentUtils.getIntAttribute(component, Attributes.columnSpan, 1);
      final int rowSpan = ComponentUtils.getIntAttribute(component, Attributes.rowSpan, 1);
      grid.add(new OriginCell(component), columnSpan, rowSpan);
      if (LOG.isDebugEnabled()) {
        LOG.debug("\n" + grid);
      }
    }
  }

  public abstract String getRows();

  public abstract void setRows(String rows);

  public abstract String getColumns();

  public abstract void setColumns(String columns);

  public abstract boolean isRigid();

  public Grid getGrid() {
    if (grid == null) {
      init();
    }
    return grid;
  }

  @Override
  public boolean getRendersChildren() {
    return false;
  }

  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(getClass().getSimpleName()).append("#");
    builder.append(getClientId(FacesContext.getCurrentInstance()));
    builder.append("\n");
    if (grid != null) {
      builder.append(StringUtils.repeat("  ", 4));
      builder.append("horiz.: ");
      final LayoutTokens rows = grid.getRows();
      for (int i = 0; i < rows.getSize(); i++) {
        if (i != 0) {
          builder.append(StringUtils.repeat("  ", 4 + 4));
        }
        builder.append(rows.get(i));
        builder.append("\n");
      }
      builder.append(StringUtils.repeat("  ", 4));
      builder.append("verti.: ");
      final LayoutTokens columns = grid.getColumns();
      for (int i = 0; i < columns.getSize(); i++) {
        if (i != 0) {
          builder.append(StringUtils.repeat("  ", 4 + 4));
        }
        builder.append(columns.get(i));
        builder.append("\n");
      }
    }
    builder.setLength(builder.length() - 1);
    return builder.toString();
  }
}

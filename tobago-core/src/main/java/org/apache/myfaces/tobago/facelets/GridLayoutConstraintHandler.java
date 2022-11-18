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

package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.component.Attributes;

import jakarta.faces.component.UIComponent;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagHandler;

import java.io.IOException;
import java.util.Map;

/**
 * @since 4.0.0
 */
public class GridLayoutConstraintHandler extends TagHandler {

  private final TagAttribute columnSpan;
  private final TagAttribute rowSpan;
  private final TagAttribute gridColumn;
  private final TagAttribute gridRow;

  public GridLayoutConstraintHandler(final TagConfig config) {
    super(config);

    final TagAttribute oldColumn = getAttribute(Attributes.column.getName()); // deprecated
    final TagAttribute newColumn = getAttribute(Attributes.columnSpan.getName());
    columnSpan = newColumn != null ? newColumn : oldColumn;

    final TagAttribute oldRow = getAttribute(Attributes.row.getName()); // deprecated
    final TagAttribute newRow = getAttribute(Attributes.rowSpan.getName());
    rowSpan = newRow != null ? newRow : oldRow;

    gridColumn = getAttribute(Attributes.gridColumn.getName());

    gridRow = getAttribute(Attributes.gridRow.getName());
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException {
    final Map<String, Object> attributes = parent.getAttributes();

    if (columnSpan != null) {
      if (columnSpan.isLiteral()) {
        attributes.put(Attributes.columnSpan.getName(), Integer.valueOf(columnSpan.getValue()));
      } else {
        parent.setValueExpression(Attributes.columnSpan.getName(),
            columnSpan.getValueExpression(faceletContext, Integer.TYPE));
      }
    }

    if (rowSpan != null) {
      if (rowSpan.isLiteral()) {
        attributes.put(Attributes.rowSpan.getName(), Integer.valueOf(rowSpan.getValue()));
      } else {
        parent.setValueExpression(Attributes.rowSpan.getName(),
            rowSpan.getValueExpression(faceletContext, Integer.TYPE));
      }
    }

    if (gridColumn != null) {
      if (gridColumn.isLiteral()) {
        attributes.put(Attributes.gridColumn.getName(), Integer.valueOf(gridColumn.getValue()));
      } else {
        parent.setValueExpression(Attributes.gridColumn.getName(),
            gridColumn.getValueExpression(faceletContext, Integer.TYPE));
      }
    }

    if (gridRow != null) {
      if (gridRow.isLiteral()) {
        attributes.put(Attributes.gridRow.getName(), Integer.valueOf(gridRow.getValue()));
      } else {
        parent.setValueExpression(Attributes.gridRow.getName(),
            gridRow.getValueExpression(faceletContext, Integer.TYPE));
      }
    }
  }
}

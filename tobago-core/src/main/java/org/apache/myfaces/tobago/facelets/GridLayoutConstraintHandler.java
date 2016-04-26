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
import org.apache.myfaces.tobago.component.UIExtensionPanel;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;
import java.util.Map;

/**
 * @deprecated since 3.0.0
 */
@Deprecated
public class GridLayoutConstraintHandler extends TagHandler {

  private final TagAttribute columnSpan;
  private final TagAttribute rowSpan;

  public GridLayoutConstraintHandler(final TagConfig config) {
    super(config);
    columnSpan = getAttribute(Attributes.columnSpan.getName());
    rowSpan = getAttribute(Attributes.rowSpan.getName());
  }

  @Override
  public void apply(final FaceletContext faceletContext, UIComponent parent) throws IOException {
    final Map<String, Object> attributes = parent.getAttributes();
    if (parent.getParent() != null && parent.getParent() instanceof UIExtensionPanel) {
      parent = parent.getParent();
    } else if (attributes.get("tobago.panel") != null
        && attributes.get("tobago.panel") instanceof UIExtensionPanel) {
      parent = (UIComponent) attributes.get("tobago.panel");
    }

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
  }
}

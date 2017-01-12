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

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;
import java.util.Map;

/**
 * @since 3.0.0
 */
public class SpanHandler extends TagHandler {

  private final TagAttribute column;
  private final TagAttribute row;

  public SpanHandler(final TagConfig config) {
    super(config);
    column = getAttribute(Attributes.column.getName());
    row = getAttribute(Attributes.row.getName());
  }

  @Override
  public void apply(final FaceletContext faceletContext, UIComponent parent) throws IOException {
    final Map<String, Object> attributes = parent.getAttributes();

    if (column != null) {
      if (column.isLiteral()) {
        attributes.put(Attributes.column.getName(), Integer.valueOf(column.getValue()));
      } else {
        parent.setValueExpression(Attributes.column.getName(),
            column.getValueExpression(faceletContext, Integer.TYPE));
      }
    }

    if (row != null) {
      if (row.isLiteral()) {
        attributes.put(Attributes.row.getName(), Integer.valueOf(row.getValue()));
      } else {
        parent.setValueExpression(Attributes.row.getName(),
            row.getValueExpression(faceletContext, Integer.TYPE));
      }
    }
  }
}

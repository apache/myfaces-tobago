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
import org.apache.myfaces.tobago.util.ComponentUtils;

import jakarta.el.ELException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.view.facelets.ComponentHandler;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagException;
import jakarta.faces.view.facelets.TagHandler;

public final class DataAttributeHandler extends TagHandler {

  private final TagAttribute name;

  private final TagAttribute value;

  public DataAttributeHandler(final TagConfig config) {
    super(config);
    this.name = getRequiredAttribute(Attributes.name.getName());
    this.value = getRequiredAttribute(Attributes.value.getName());
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws ELException {
    if (parent == null) {
      throw new TagException(tag, "Parent UIComponent was null");
    }

    if (ComponentHandler.isNew(parent)) {

      final Object attributeName = name.isLiteral()
          ? (Object) name.getValue(faceletContext)
          : name.getValueExpression(faceletContext, Object.class);
      final Object attributeValue = value.isLiteral()
          ? (Object) value.getValue(faceletContext)
          : value.getValueExpression(faceletContext, Object.class);
      ComponentUtils.putDataAttribute(parent, attributeName, attributeValue);
    }
  }
}

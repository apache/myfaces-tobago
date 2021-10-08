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
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.context.Markup;

import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.MetaRule;
import jakarta.faces.view.facelets.Metadata;
import jakarta.faces.view.facelets.MetadataTarget;
import jakarta.faces.view.facelets.TagAttribute;

public class SupportsMarkupRule extends MetaRule {

  public static final SupportsMarkupRule INSTANCE = new SupportsMarkupRule();

  @Override
  public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(Visual.class)) {
      final Attributes a = Attributes.valueOfFailsafe(name);
      if (Attributes.markup == a) {
        return new SupportsMarkupMapper(attribute);
      }
    }
    return null;
  }

  static final class SupportsMarkupMapper extends Metadata {

    private final TagAttribute attribute;

    SupportsMarkupMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    @Override
    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      if (attribute.isLiteral()) {
        ((Visual) instance).setMarkup(Markup.valueOf(attribute.getValue()));
      } else {
        final ValueExpression expression = attribute.getValueExpression(ctx, Object.class);
        ((UIComponent) instance).setValueExpression(Attributes.markup.getName(), expression);
      }
    }
  }
}

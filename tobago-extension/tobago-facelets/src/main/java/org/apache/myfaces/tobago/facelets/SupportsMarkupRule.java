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
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.context.Markup;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

public class SupportsMarkupRule extends MetaRule {

  public static final SupportsMarkupRule INSTANCE = new SupportsMarkupRule();

  public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SupportsMarkup.class)) {
      if (Attributes.MARKUP.equals(name)) {
        return new SupportsMarkupMapper(attribute);
      }
    }
    return null;
  }

  static final class SupportsMarkupMapper extends Metadata {

    private final TagAttribute attribute;

    public SupportsMarkupMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      if (attribute.isLiteral()) {
        ((SupportsMarkup) instance).setMarkup(Markup.valueOf(attribute.getValue()));
      } else {
        ValueExpression expression = attribute.getValueExpression(ctx, Object.class);
        ((UIComponent) instance).setValueExpression(Attributes.MARKUP, expression);
      }
    }
  }
}

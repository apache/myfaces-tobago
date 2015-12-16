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
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

public class SupportsRenderedPartiallyRule extends MetaRule {

  public static final SupportsRenderedPartiallyRule INSTANCE = new SupportsRenderedPartiallyRule();

  public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SupportsRenderedPartially.class)) {
      Attributes a = Attributes.valueOf(name);
      if (Attributes.renderedPartially == a) {
        return new SupportsRenderedPartiallyMapper(attribute);
      }
    }
    return null;
  }

  static final class SupportsRenderedPartiallyMapper extends Metadata {

    private final TagAttribute attribute;

    public SupportsRenderedPartiallyMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      if (attribute.isLiteral()) {
        final String[] components = ComponentUtils.splitList(attribute.getValue());
        ((SupportsRenderedPartially) instance).setRenderedPartially(components);
      } else {
        final ValueExpression expression = attribute.getValueExpression(ctx, Object.class);
        ((UIComponent) instance).setValueExpression(Attributes.renderedPartially.getName(), expression);
      }
    }
  }
}

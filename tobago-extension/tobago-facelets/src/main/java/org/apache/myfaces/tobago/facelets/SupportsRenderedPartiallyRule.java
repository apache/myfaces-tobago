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

import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.ELAdaptor;
import com.sun.facelets.tag.MetaRule;
import com.sun.facelets.tag.Metadata;
import com.sun.facelets.tag.MetadataTarget;
import com.sun.facelets.tag.TagAttribute;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

public class SupportsRenderedPartiallyRule extends MetaRule {

  public static final SupportsRenderedPartiallyRule INSTANCE = new SupportsRenderedPartiallyRule();

  public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SupportsRenderedPartially.class)) {
      if (Attributes.RENDERED_PARTIALLY.equals(name)) {
        return new SupportsRenderedPartiallyMapper(attribute);
      }
    }
    return null;
  }

  static final class SupportsRenderedPartiallyMapper extends Metadata {

    private final TagAttribute attribute;

    public SupportsRenderedPartiallyMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      if (attribute.isLiteral()) {
        String[] components = ComponentUtils.splitList(attribute.getValue());
        ((SupportsRenderedPartially) instance).setRenderedPartially(components);
      } else {
        ValueExpression expression = attribute.getValueExpression(ctx, Object.class);
        ELAdaptor.setExpression((UIComponent) instance, Attributes.RENDERED_PARTIALLY, expression);
      }
    }
  }
}

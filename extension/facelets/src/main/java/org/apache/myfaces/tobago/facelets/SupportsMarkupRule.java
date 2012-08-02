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

import com.sun.facelets.tag.Metadata;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.MetadataTarget;
import com.sun.facelets.tag.MetaRule;
import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.ELAdaptor;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.el.ValueExpression;

/*
 * User: bommel
 * Date: Jan 20, 2007
 * Time: 5:54:23 PM
 */
public class SupportsMarkupRule extends MetaRule {
  public static final SupportsMarkupRule INSTANCE = new SupportsMarkupRule();

  public Metadata applyRule(String name, TagAttribute attribute,
      MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SupportsMarkup.class)) {
      if (TobagoConstants.ATTR_MARKUP.equals(name)) {
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
        ComponentUtil.setMarkup((UIComponent) instance, attribute.getValue());
      } else {
        ValueExpression expression = attribute.getValueExpression(ctx, Object.class);
        ELAdaptor.setExpression((UIComponent) instance, TobagoConstants.ATTR_MARKUP, expression);
      }
    }
  }
}

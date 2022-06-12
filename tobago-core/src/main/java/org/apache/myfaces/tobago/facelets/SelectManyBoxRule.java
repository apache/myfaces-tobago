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

import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyBox;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

public class SelectManyBoxRule extends MetaRule {

  public static final String TOKEN_SEPARATORS = "tokenSeparators";

  public static final SelectManyBoxRule INSTANCE = new SelectManyBoxRule();

  public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(AbstractUISelectManyBox.class)) {
      if (TOKEN_SEPARATORS.equals(name)) {
        return new SelectManyBoxRule.TokenSeparatorsMapper(attribute);
      }
    }
    return null;
  }

  static final class TokenSeparatorsMapper extends Metadata {

    private final TagAttribute attribute;

    TokenSeparatorsMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext faceletContext, Object instance) {
      if (attribute.isLiteral()) {
        final String[] tokenSeparators = AbstractUISelectManyBox.parseTokenSeparators(attribute.getValue());
        ((AbstractUISelectManyBox) instance).setTokenSeparators(tokenSeparators);
      } else {
        final ValueExpression expression = attribute.getValueExpression(faceletContext, Object.class);
        ((UIComponent) instance).setValueExpression(TOKEN_SEPARATORS, expression);
      }
    }
  }
}

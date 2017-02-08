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

import org.apache.myfaces.tobago.component.InputSuggest2;
import org.apache.myfaces.tobago.internal.component.AbstractUISuggest;
import org.apache.myfaces.tobago.model.SuggestFilter;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

public class SuggestMethodRule extends MetaRule {

  public static final Class[] SUGGEST_METHOD = new Class[]{javax.faces.component.UIInput.class};

  public static final SuggestMethodRule INSTANCE = new SuggestMethodRule();

  @Override
  public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget metadataTarget) {

    if (metadataTarget.isTargetInstanceOf(InputSuggest2.class)) {
      if ("suggestMethod".equals(name)) {
        return new SuggestMethodMapper(attribute);
      }
    }
    if (metadataTarget.isTargetInstanceOf(AbstractUISuggest.class)) {
      if (attribute.isLiteral()) {
        if ("filter".equals(name)) {
          return new SuggestFilterMapper(attribute);
        }
      }
    }
    return null;
  }

  static final class SuggestMethodMapper extends Metadata {
    private final TagAttribute attribute;

    SuggestMethodMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    @Override
    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      ((InputSuggest2) instance).setSuggestMethodExpression(
          attribute.getMethodExpression(ctx, null, SuggestMethodRule.SUGGEST_METHOD));
    }
  }

  static final class SuggestFilterMapper extends Metadata {
    private final TagAttribute attribute;

    SuggestFilterMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    @Override
    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      ((AbstractUISuggest) instance).setFilter(SuggestFilter.parse(attribute.getValue()));
    }
  }

}

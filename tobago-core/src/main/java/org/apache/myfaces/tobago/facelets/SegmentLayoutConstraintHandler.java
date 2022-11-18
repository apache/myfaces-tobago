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

import jakarta.faces.component.UIComponent;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagHandler;

import java.io.IOException;

public class SegmentLayoutConstraintHandler extends TagHandler {

  private final TagAttribute overwriteExtraSmall;
  private final TagAttribute overwriteSmall;
  private final TagAttribute overwriteMedium;
  private final TagAttribute overwriteLarge;
  private final TagAttribute overwriteExtraLarge;
  private final TagAttribute overwriteMarginExtraSmall;
  private final TagAttribute overwriteMarginSmall;
  private final TagAttribute overwriteMarginMedium;
  private final TagAttribute overwriteMarginLarge;
  private final TagAttribute overwriteMarginExtraLarge;
  private final TagAttribute offsetExtraSmall;
  private final TagAttribute offsetSmall;
  private final TagAttribute offsetMedium;
  private final TagAttribute offsetLarge;
  private final TagAttribute offsetExtraLarge;

  public SegmentLayoutConstraintHandler(final TagConfig config) {
    super(config);
    overwriteExtraSmall = getAttribute(Attributes.extraSmall.getName());
    overwriteSmall = getAttribute(Attributes.small.getName());
    overwriteMedium = getAttribute(Attributes.medium.getName());
    overwriteLarge = getAttribute(Attributes.large.getName());
    overwriteExtraLarge = getAttribute(Attributes.extraLarge.getName());
    overwriteMarginExtraSmall = getAttribute(Attributes.marginExtraSmall.getName());
    overwriteMarginSmall = getAttribute(Attributes.marginSmall.getName());
    overwriteMarginMedium = getAttribute(Attributes.marginMedium.getName());
    overwriteMarginLarge = getAttribute(Attributes.marginLarge.getName());
    overwriteMarginExtraLarge = getAttribute(Attributes.marginExtraLarge.getName());
    offsetExtraSmall = getAttribute(Attributes.offsetExtraSmall.getName());
    offsetSmall = getAttribute(Attributes.offsetSmall.getName());
    offsetMedium = getAttribute(Attributes.offsetMedium.getName());
    offsetLarge = getAttribute(Attributes.offsetLarge.getName());
    offsetExtraLarge = getAttribute(Attributes.offsetExtraLarge.getName());
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws IOException {
    apply(faceletContext, parent, overwriteExtraSmall, Attributes.overwriteExtraSmall, Integer.TYPE);
    apply(faceletContext, parent, overwriteSmall, Attributes.overwriteSmall, Integer.TYPE);
    apply(faceletContext, parent, overwriteMedium, Attributes.overwriteMedium, Integer.TYPE);
    apply(faceletContext, parent, overwriteLarge, Attributes.overwriteLarge, Integer.TYPE);
    apply(faceletContext, parent, overwriteExtraLarge, Attributes.overwriteExtraLarge, Integer.TYPE);

    apply(faceletContext, parent, overwriteMarginExtraSmall, Attributes.overwriteMarginExtraSmall, String.class);
    apply(faceletContext, parent, overwriteMarginSmall, Attributes.overwriteMarginSmall, String.class);
    apply(faceletContext, parent, overwriteMarginMedium, Attributes.overwriteMarginMedium, String.class);
    apply(faceletContext, parent, overwriteMarginLarge, Attributes.overwriteMarginLarge, String.class);
    apply(faceletContext, parent, overwriteMarginExtraLarge, Attributes.overwriteMarginExtraLarge, String.class);

    apply(faceletContext, parent, offsetExtraSmall, Attributes.offsetExtraSmall, Integer.TYPE);
    apply(faceletContext, parent, offsetSmall, Attributes.offsetSmall, Integer.TYPE);
    apply(faceletContext, parent, offsetMedium, Attributes.offsetMedium, Integer.TYPE);
    apply(faceletContext, parent, offsetLarge, Attributes.offsetLarge, Integer.TYPE);
    apply(faceletContext, parent, offsetExtraLarge, Attributes.offsetExtraLarge, Integer.TYPE);
  }

  private void apply(
      final FaceletContext faceletContext, final UIComponent parent, final TagAttribute tagAttribute,
      final Attributes attribute, final Class type) {
    if (tagAttribute != null) {
      if (tagAttribute.isLiteral()) {
        parent.getAttributes().put(attribute.getName(), tagAttribute.getValue());
      } else {
        parent.setValueExpression(attribute.getName(), tagAttribute.getValueExpression(faceletContext, type));
      }
    }
  }
}

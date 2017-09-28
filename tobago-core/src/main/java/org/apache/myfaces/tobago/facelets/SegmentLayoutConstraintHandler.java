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

public class SegmentLayoutConstraintHandler extends TagHandler {

  private final TagAttribute overwriteExtraSmall;
  private final TagAttribute overwriteSmall;
  private final TagAttribute overwriteMedium;
  private final TagAttribute overwriteLarge;
  private final TagAttribute overwriteExtraLarge;

  public SegmentLayoutConstraintHandler(TagConfig config) {
    super(config);
    overwriteExtraSmall = getAttribute(Attributes.extraSmall.getName());
    overwriteSmall = getAttribute(Attributes.small.getName());
    overwriteMedium = getAttribute(Attributes.medium.getName());
    overwriteLarge = getAttribute(Attributes.large.getName());
    overwriteExtraLarge = getAttribute(Attributes.extraLarge.getName());
  }

  @Override
  public void apply(FaceletContext faceletContext, UIComponent parent) throws IOException {
    final Map<String, Object> attributes = parent.getAttributes();

    if (overwriteExtraSmall != null) {
      if (overwriteExtraSmall.isLiteral()) {
        attributes.put(Attributes.overwriteExtraSmall.getName(), overwriteExtraSmall.getValue());
      } else {
        parent.setValueExpression(Attributes.overwriteExtraSmall.getName(),
            overwriteExtraSmall.getValueExpression(faceletContext, Integer.TYPE));
      }
    }

    if (overwriteSmall != null) {
      if (overwriteSmall.isLiteral()) {
        attributes.put(Attributes.overwriteSmall.getName(), overwriteSmall.getValue());
      } else {
        parent.setValueExpression(Attributes.overwriteSmall.getName(),
            overwriteSmall.getValueExpression(faceletContext, Integer.TYPE));
      }
    }

    if (overwriteMedium != null) {
      if (overwriteMedium.isLiteral()) {
        attributes.put(Attributes.overwriteMedium.getName(), overwriteMedium.getValue());
      } else {
        parent.setValueExpression(Attributes.overwriteMedium.getName(),
            overwriteMedium.getValueExpression(faceletContext, Integer.TYPE));
      }
    }

    if (overwriteLarge != null) {
      if (overwriteLarge.isLiteral()) {
        attributes.put(Attributes.overwriteLarge.getName(), overwriteLarge.getValue());
      } else {
        parent.setValueExpression(Attributes.overwriteLarge.getName(),
            overwriteLarge.getValueExpression(faceletContext, Integer.TYPE));
      }
    }

    if (overwriteExtraLarge != null) {
      if (overwriteExtraLarge.isLiteral()) {
        attributes.put(Attributes.overwriteExtraLarge.getName(), overwriteExtraLarge.getValue());
      } else {
        parent.setValueExpression(Attributes.overwriteExtraLarge.getName(),
            overwriteExtraLarge.getValueExpression(faceletContext, Integer.TYPE));
      }
    }
  }
}

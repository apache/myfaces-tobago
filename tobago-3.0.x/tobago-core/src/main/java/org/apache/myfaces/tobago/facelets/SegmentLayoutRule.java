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
import org.apache.myfaces.tobago.component.UISegmentLayout;
import org.apache.myfaces.tobago.layout.ColumnPartition;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

public class SegmentLayoutRule extends MetaRule {

  public static final SegmentLayoutRule INSTANCE = new SegmentLayoutRule();

  @Override
  public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(UISegmentLayout.class)) {
      if (attribute.isLiteral()) {
        Attributes a = Attributes.valueOfFailsafe(name);
        if (Attributes.extraSmall == a) {
          return new ExtraSmallMapper(attribute);
        }
        if (Attributes.small == a) {
          return new SmallMapper(attribute);
        }
        if (Attributes.medium == a) {
          return new MediumMapper(attribute);
        }
        if (Attributes.large == a) {
          return new LargeMapper(attribute);
        }
      }
    }
    return null;
  }

  static final class ExtraSmallMapper extends Metadata {
    private final TagAttribute attribute;

    ExtraSmallMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    @Override
    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      final UISegmentLayout gridLayout = (UISegmentLayout) instance;
      gridLayout.setExtraSmall(ColumnPartition.valueOf(attribute.getValue()));
    }
  }

  static final class SmallMapper extends Metadata {
    private final TagAttribute attribute;

    SmallMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    @Override
    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      final UISegmentLayout gridLayout = (UISegmentLayout) instance;
      gridLayout.setSmall(ColumnPartition.valueOf(attribute.getValue()));
    }
  }

  static final class MediumMapper extends Metadata {
    private final TagAttribute attribute;

    MediumMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    @Override
    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      final UISegmentLayout gridLayout = (UISegmentLayout) instance;
      gridLayout.setMedium(ColumnPartition.valueOf(attribute.getValue()));
    }
  }

  static final class LargeMapper extends Metadata {
    private final TagAttribute attribute;

    LargeMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    @Override
    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      final UISegmentLayout gridLayout = (UISegmentLayout) instance;
      gridLayout.setLarge(ColumnPartition.valueOf(attribute.getValue()));
    }
  }

}

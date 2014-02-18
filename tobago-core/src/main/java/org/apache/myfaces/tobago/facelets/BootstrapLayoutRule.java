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
import org.apache.myfaces.tobago.component.UIBootstrapLayout;
import org.apache.myfaces.tobago.layout.BootstrapPartition;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

public class BootstrapLayoutRule extends MetaRule {

  public static final BootstrapLayoutRule INSTANCE = new BootstrapLayoutRule();

  public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(UIBootstrapLayout.class)) {
      if (attribute.isLiteral()) {
        if (Attributes.EXTRA_SMALL.equals(name)) {
          return new ExtraSmallMapper(attribute);
        }
        if (Attributes.SMALL.equals(name)) {
          return new SmallMapper(attribute);
        }
        if (Attributes.MEDIUM.equals(name)) {
          return new MediumMapper(attribute);
        }
        if (Attributes.LARGE.equals(name)) {
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

    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      final UIBootstrapLayout gridLayout = (UIBootstrapLayout) instance;
      gridLayout.setExtraSmall(BootstrapPartition.valueOf(attribute.getValue()));
    }
  }

  static final class SmallMapper extends Metadata {
    private final TagAttribute attribute;

    SmallMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      final UIBootstrapLayout gridLayout = (UIBootstrapLayout) instance;
      gridLayout.setSmall(BootstrapPartition.valueOf(attribute.getValue()));
    }
  }

  static final class MediumMapper extends Metadata {
    private final TagAttribute attribute;

    MediumMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      final UIBootstrapLayout gridLayout = (UIBootstrapLayout) instance;
      gridLayout.setMedium(BootstrapPartition.valueOf(attribute.getValue()));
    }
  }

  static final class LargeMapper extends Metadata {
    private final TagAttribute attribute;

    LargeMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      final UIBootstrapLayout gridLayout = (UIBootstrapLayout) instance;
      gridLayout.setLarge(BootstrapPartition.valueOf(attribute.getValue()));
    }
  }

}

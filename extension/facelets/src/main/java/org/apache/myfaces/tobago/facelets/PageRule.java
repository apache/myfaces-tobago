package org.apache.myfaces.tobago.facelets;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.MetaRule;
import com.sun.facelets.tag.Metadata;
import com.sun.facelets.tag.MetadataTarget;
import com.sun.facelets.tag.TagAttribute;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.layout.Measure;

public class PageRule extends MetaRule {
  public static final PageRule INSTANCE = new PageRule();

  public Metadata applyRule(String name, TagAttribute attribute,
      MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(UIPage.class)) {
      if (attribute.isLiteral()) {
        if (Attributes.WIDTH.equals(name)) {
          return new PageWidthMapper(attribute);
        }
        if (Attributes.HEIGHT.equals(name)) {
          return new PageHeightMapper(attribute);
        }
      }
    }
    return null;
  }

  // TODO remove this
/*
  static final class PageDimensionExpression extends Metadata {
    private final String name;
    private final TagAttribute attr;
    private final Class type;

    PageDimensionExpression(String name, Class type, TagAttribute attr) {
      this.name = name;
      this.attr = attr;
      this.type = type;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      ((UIComponent) instance).setValueBinding(name, new LegacyValueBinding(attr.getValueExpression(ctx, type)));
    }
  }
*/
  static final class PageWidthMapper extends Metadata {
    private final TagAttribute attribute;

    PageWidthMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      UIPage page = (UIPage) instance;
      page.setWidth(Measure.parse(attribute.getValue()));
    }
  }

  static final class PageHeightMapper extends Metadata {
    private final TagAttribute attribute;

    PageHeightMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      UIPage page = (UIPage) instance;
      page.setHeight(Measure.parse(attribute.getValue()));
    }
  }
}

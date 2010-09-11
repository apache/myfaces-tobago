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
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.validator.FileItemValidator;

public class ContentTypeRule extends MetaRule {

  public static final ContentTypeRule INSTANCE = new ContentTypeRule();

  public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget metadataTarget) {
    if ("contentType".equals(name)) {
      return new ContentTypeRuleMapper(attribute);
    }
    return null;
  }

  static final class ContentTypeRuleMapper extends Metadata {

    private final TagAttribute attribute;

    public ContentTypeRuleMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      String[] components = StringUtils.split(attribute.getValue(), ", ");
      ((FileItemValidator) instance).setContentType(components);
    }
  }
}
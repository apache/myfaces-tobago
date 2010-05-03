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

import com.sun.facelets.tag.MetaRule;
import com.sun.facelets.tag.Metadata;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.MetadataTarget;
import com.sun.facelets.FaceletContext;
import com.sun.facelets.el.LegacyMethodBinding;
import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.event.TabChangeEvent;

/*
 * User: bommel
 * Date: 15.04.2006
 * Time: 14:24:54
 */
public class TabChangeSourceRule extends MetaRule {
  static final Class[] ACTION_LISTENER = new Class[]{TabChangeEvent.class};
  public static final TabChangeSourceRule INSTANCE = new TabChangeSourceRule();

  public Metadata applyRule(String name, TagAttribute attribute,
      MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(TabChangeSource.class)) {
      if ("tabChangeListener".equals(name)) {
        return new TabChangeListenerMapper(attribute);
      }
    }
    return null;
  }

  static final class TabChangeListenerMapper extends Metadata {

    private final TagAttribute attribute;

    public TabChangeListenerMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      ((TabChangeSource) instance)
          .setTabChangeListener(new LegacyMethodBinding(attribute
              .getMethodExpression(ctx, null,
              TabChangeSourceRule.ACTION_LISTENER)));
    }
  }

}

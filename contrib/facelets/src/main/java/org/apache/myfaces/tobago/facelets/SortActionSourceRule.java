package org.apache.myfaces.tobago.facelets;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import org.apache.myfaces.tobago.event.SortActionSource;
import org.apache.myfaces.tobago.event.SortActionEvent;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 15.04.2006
 * Time: 13:53:41
 */
public class SortActionSourceRule extends MetaRule {
  static final Class[] ACTION_LISTENER_SIG = new Class[]{SortActionEvent.class};
  public static final SortActionSourceRule INSTANCE = new SortActionSourceRule();

  public Metadata applyRule(String name, TagAttribute attribute,
      MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SortActionSource.class)) {
      if ("sortActionListener".equals(name)) {
        return new SortActionListenerMapper(attribute);
      }
    }
    return null;
  }

  static final class SortActionListenerMapper extends Metadata {

    private final TagAttribute attribute;

    public SortActionListenerMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      ((SortActionSource) instance)
          .setSortActionListener(new LegacyMethodBinding(attribute
              .getMethodExpression(ctx, null,
              SortActionSourceRule.ACTION_LISTENER_SIG)));
    }
  }

}

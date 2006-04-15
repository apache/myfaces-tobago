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
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.event.SheetStateChangeSource;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 15.04.2006
 * Time: 14:25:10
 * To change this template use File | Settings | File Templates.
 */
public class SheetStateChangeSourceRule extends MetaRule {
  public final static Class[] ACTION_LISTENER_SIG = new Class[]{SheetStateChangeEvent.class};
  public final static SheetStateChangeSourceRule INSTANCE = new SheetStateChangeSourceRule();

  public Metadata applyRule(String name, TagAttribute attribute,
      MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SheetStateChangeSource.class)) {
      if ("stateChangeListener".equals(name)) {
        return new SheetStateChangeListenerMapper(attribute);
      }
    }
    return null;
  }

  final static class SheetStateChangeListenerMapper extends Metadata {

    private final TagAttribute attribute;

    public SheetStateChangeListenerMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      ((SheetStateChangeSource) instance)
          .setStateChangeListener(new LegacyMethodBinding(attribute
              .getMethodExpression(ctx, null,
              SortActionSourceRule.ACTION_LISTENER_SIG)));
    }
  }
}

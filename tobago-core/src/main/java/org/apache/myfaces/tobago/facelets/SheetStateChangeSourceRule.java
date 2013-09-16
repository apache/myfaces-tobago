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

import org.apache.myfaces.tobago.event.SheetStateChangeSource;
import org.apache.myfaces.tobago.event.SheetStateChangeSource2;

import javax.faces.event.ActionEvent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;

public class SheetStateChangeSourceRule extends MetaRule {

  static final Class[] ACTION_LISTENER = new Class[]{ActionEvent.class};

  public static final SheetStateChangeSourceRule INSTANCE = new SheetStateChangeSourceRule();

  public Metadata applyRule(String name, TagAttribute attribute, MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SheetStateChangeSource.class)) {
      if ("stateChangeListener".equals(name)) {
        return new SheetStateChangeListenerMapper(attribute);
      }
    }
    return null;
  }

  static final class SheetStateChangeListenerMapper extends Metadata {

    private final TagAttribute attribute;

    public SheetStateChangeListenerMapper(TagAttribute attribute) {
      this.attribute = attribute;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
      ((SheetStateChangeSource2) instance).setStateChangeListenerExpression(
          attribute.getMethodExpression(ctx, null, SheetStateChangeSourceRule.ACTION_LISTENER));
    }
  }
}

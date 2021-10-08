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

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.MetaRule;
import jakarta.faces.view.facelets.Metadata;
import jakarta.faces.view.facelets.MetadataTarget;
import jakarta.faces.view.facelets.TagAttribute;

public class SheetStateChangeSourceRule extends MetaRule {

  static final Class[] ACTION_LISTENER = new Class[]{ActionEvent.class};

  public static final SheetStateChangeSourceRule INSTANCE = new SheetStateChangeSourceRule();

  @Override
  public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SheetStateChangeSource.class)) {
      if ("stateChangeListener".equals(name)) {
        return new SheetStateChangeListenerMapper(attribute);
      }
    }
    return null;
  }

  static final class SheetStateChangeListenerMapper extends Metadata {

    private final TagAttribute attribute;

    SheetStateChangeListenerMapper(final TagAttribute attribute) {
      this.attribute = attribute;
    }

    @Override
    public void applyMetadata(final FaceletContext ctx, final Object instance) {
      ((SheetStateChangeSource) instance).setStateChangeListenerExpression(
          attribute.getMethodExpression(ctx, null, SheetStateChangeSourceRule.ACTION_LISTENER));
    }
  }
}

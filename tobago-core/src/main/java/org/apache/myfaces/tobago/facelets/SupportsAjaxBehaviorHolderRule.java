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
import org.apache.myfaces.tobago.component.SupportsAjaxBehaviorHolder;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.Metadata;
import javax.faces.view.facelets.MetadataTarget;
import javax.faces.view.facelets.TagAttribute;
import java.util.List;
import java.util.Map;

public class SupportsAjaxBehaviorHolderRule extends MetaRule {

  public static final SupportsAjaxBehaviorHolderRule INSTANCE = new SupportsAjaxBehaviorHolderRule();

  @Override
  public Metadata applyRule(final String name, final TagAttribute attribute, final MetadataTarget metadataTarget) {
    if (metadataTarget.isTargetInstanceOf(SupportsAjaxBehaviorHolder.class)
        || metadataTarget.isTargetInstanceOf(SupportsRenderedPartially.class)) {
      Attributes a = Attributes.valueOfFailsafe(name);
      if (a != null) {
        switch (a) {
          case renderedPartially:
          case renderPartially:
          case executePartially:
            return new SupportsAjaxBehaviorHolderMapper(attribute, a);
          default:
        }
      }
    }
    return null;
  }

  static final class SupportsAjaxBehaviorHolderMapper extends Metadata {

    private final TagAttribute tagAttribute;
    private final Attributes tobagoAttribute;

    public SupportsAjaxBehaviorHolderMapper(final TagAttribute tagAttribute, Attributes tobagoAttribute) {
      this.tagAttribute = tagAttribute;
      this.tobagoAttribute = tobagoAttribute;
    }

    @Override
    public void applyMetadata(final FaceletContext faceletContext, final Object instance) {

      AjaxBehavior ajaxBehavior = null;
      if (instance instanceof SupportsAjaxBehaviorHolder) {
        final SupportsAjaxBehaviorHolder ajaxBehaviorHolder = (SupportsAjaxBehaviorHolder) instance;
        ajaxBehavior = findAjaxBehavior(ajaxBehaviorHolder, faceletContext);
      }

      switch (tobagoAttribute) {
        case renderedPartially:
          if (ajaxBehavior == null && instance instanceof SupportsRenderedPartially) {
            SupportsRenderedPartially supportsRenderedPartially = (SupportsRenderedPartially) instance;
            supportsRenderedPartially.setRenderedPartially(ComponentUtils.splitList(tagAttribute.getValue()));
            break;
          }
        case renderPartially:
          if (ajaxBehavior != null) {
            ajaxBehavior.setValueExpression("render", tagAttribute.getValueExpression(faceletContext, Object.class));
          }
          break;
        case executePartially:
          if (ajaxBehavior != null) {
            ajaxBehavior.setValueExpression("execute", tagAttribute.getValueExpression(faceletContext, Object.class));
          }
          break;
        default:
      }
    }

    private AjaxBehavior findAjaxBehavior(SupportsAjaxBehaviorHolder behaviorHolder, FaceletContext faceletContext) {
      Map<String, List<ClientBehavior>> clientBehaviors = behaviorHolder.getClientBehaviors();

      for (List<ClientBehavior> behaviors : clientBehaviors.values()) {
        if (behaviors != null && !behaviors.isEmpty()) {
          for (ClientBehavior cb : behaviors) {
            if (cb instanceof AjaxBehavior) {
              return (AjaxBehavior) cb;
            }
          }
        }
      }
      final AjaxBehavior ajaxBehavior
          = (AjaxBehavior) faceletContext.getFacesContext().getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
      behaviorHolder.addClientBehavior(behaviorHolder.getDefaultEventName(), ajaxBehavior);
      return ajaxBehavior;
    }
  }
}

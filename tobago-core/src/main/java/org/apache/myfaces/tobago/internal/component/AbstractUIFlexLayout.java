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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.layout.AlignItems;
import org.apache.myfaces.tobago.layout.JustifyContent;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MeasureList;
import org.apache.myfaces.tobago.util.ComponentUtils;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.event.ListenerFor;
import jakarta.faces.event.PostAddToViewEvent;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.FlexLayoutTagDeclaration}
 *
 * @since 3.0.0
 */
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractUIFlexLayout extends AbstractUILayoutBase {

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.FlexLayout";

  @Override
  public void processEvent(final ComponentSystemEvent event) {

    if (event instanceof PostAddToViewEvent) {
      final FacesContext facesContext = getFacesContext();

      final boolean horizontal = isHorizontal();

      MeasureList tokens = horizontal ? getColumns() : getRows();
      if (tokens == null) {
        tokens = new MeasureList();
        tokens.add(Measure.AUTO);
      }

      if (tokens.getSize() > 0) {
        int i = 0;

        for (final UIComponent child : getChildren()) {
          if (child instanceof Visual) {
            if (i >= tokens.getSize()) {
              i = 0;
            }
            final Measure token = tokens.get(i++);
            final Measure.Unit unit = token.getUnit();
            if (unit != Measure.Unit.AUTO) {
              AbstractUIStyle style = ComponentUtils.findChild(child, AbstractUIStyle.class);
              if (style == null) {
                style = (AbstractUIStyle) facesContext.getApplication().createComponent(
                    facesContext, Tags.style.componentType(), RendererTypes.Style.name());
                style.setTransient(true);
                child.getChildren().add(style);
              }
              if (unit == Measure.Unit.FR) {
                final float factor = token.getValue();
                style.setFlexGrow(factor);
                style.setFlexShrink(0);
                style.setFlexBasis(Measure.ZERO);
//                style.setFlexBasis(Measure.AUTO); // is unbalanced when mixing e.g. <p> with <textarea>
              } else {
                if (horizontal) {
                  style.setWidth(token);
                } else {
                  style.setHeight(token);
                }
              }
            }
          }
        }
      }
    }
  }

  public abstract MeasureList getColumns();

  public abstract MeasureList getRows();

  public abstract AlignItems getAlignItems();

  public abstract JustifyContent getJustifyContent();

  public boolean isHorizontal() {
    return getRows() == null;
  }
}

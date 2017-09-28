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
import org.apache.myfaces.tobago.component.UIStyle;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MeasureLayoutToken;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.FlexLayoutTagDeclaration}
 *
 * @since 3.0.0
 */
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractUIFlexLayout extends AbstractUILayoutBase implements Visual {

  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.FlexLayout";

  @Override
  public void processEvent(final ComponentSystemEvent event) {

    if (event instanceof PostAddToViewEvent) {
      final FacesContext facesContext = getFacesContext();

      final boolean horizontal = isHorizontal();

      String tokensString = getColumns();
      if (tokensString == null) {
        tokensString = getRows();
        if (tokensString == null) {
          tokensString = "auto"; // XXX magic string
        }
      }
      final LayoutTokens tokens = LayoutTokens.parse(tokensString);
      if (tokens.getSize() > 0) {
        int i = 0;

        for (UIComponent child : getChildren()) {
          if (child instanceof Visual) {
            final Visual visual = (Visual) child;
            if (i >= tokens.getSize()) {
              i = 0;
            }
            final LayoutToken token = tokens.get(i++);
            if (token instanceof MeasureLayoutToken) {
              final Measure measure = ((MeasureLayoutToken) token).getMeasure();
              // XXX only create if there is no child UIStyle
              UIStyle style = (UIStyle) facesContext.getApplication().createComponent(
                  facesContext, UIStyle.COMPONENT_TYPE, RendererTypes.Style.name());
              style.setTransient(true);
              if (horizontal) {
                style.setWidth(measure);
              } else {
                style.setHeight(measure);
              }
              ((UIComponent) visual).getChildren().add(style);
            } else if (token instanceof RelativeLayoutToken) {
              final int factor = ((RelativeLayoutToken) token).getFactor();
              // XXX only create if there is no child UIStyle
              UIStyle style = (UIStyle) facesContext.getApplication().createComponent(
                  facesContext, UIStyle.COMPONENT_TYPE, RendererTypes.Style.name());
              style.setTransient(true);
              style.setFlexGrow(factor);
              style.setFlexShrink(0);
              style.setFlexBasis(Measure.ZERO);
              ((UIComponent) visual).getChildren().add(style);
            }
          }
        }
      }
    }
  }

  public abstract java.lang.String getColumns();

  public abstract java.lang.String getRows();

  public boolean isHorizontal() {
    return getRows() == null;
  }
}

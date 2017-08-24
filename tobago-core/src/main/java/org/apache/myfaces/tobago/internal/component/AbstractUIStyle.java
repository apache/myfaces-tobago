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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Overflow;
import org.apache.myfaces.tobago.layout.Position;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.apache.myfaces.tobago.renderkit.css.CustomClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;

/**
 * {@link org.apache.myfaces.tobago.internal.taglib.component.StyleTagDeclaration}
 *
 * @since 4.0.0
 */
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public abstract class AbstractUIStyle extends UIComponentBase {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIStyle.class);

  @Override
  public void processEvent(final ComponentSystemEvent event) {

    final FacesContext facesContext = getFacesContext();

    if (event instanceof PostAddToViewEvent) {

      // attribute file
      if (StringUtils.isNotBlank(getFile())) {
        final UIViewRoot root = facesContext.getViewRoot();
        root.addComponentResource(facesContext, this);
      }

      // attribute customClass
      final ValueExpression valueExpression = getValueExpression(Attributes.customClass.getName());
      if (valueExpression != null) {
        final UIComponent parent = getParent();
        if (parent instanceof Visual) {
          parent.setValueExpression(Attributes.customClass.getName(), valueExpression);
        } else {
          LOG.warn("The parent '{}' of a style component doesn't support styling!", parent.getClientId(facesContext));
        }
      } else {
        final CustomClass customClass = getCustomClass();
        if (customClass != null) {
          final UIComponent parent = getParent();
          if (parent instanceof Visual) {
            ((Visual) parent).setCustomClass(customClass);
          } else {
            LOG.warn("The parent '{}' of a style component doesn't support styling!", parent.getClientId(facesContext));
          }
        }
      }
    }
  }

  public abstract Measure getPaddingRight();

  public abstract Measure getMinHeight();

  public abstract Measure getMarginRight();

  public abstract String getFile();

  public abstract Measure getPaddingBottom();

  public abstract Measure getTop();

  public abstract Measure getMaxHeight();

  public abstract Measure getPaddingTop();

  public abstract Measure getHeight();

  public abstract Measure getMaxWidth();

  public abstract TextAlign getTextAlign();

  public abstract Measure getBottom();

  public abstract Display getDisplay();

  public abstract Measure getMinWidth();

  public abstract Measure getRight();

  public abstract Measure getMarginLeft();

  public abstract Overflow getOverflowX();

  public abstract Overflow getOverflowY();

  public abstract Measure getLeft();

  public abstract Measure getWidth();

  public abstract CustomClass getCustomClass();

  public abstract Measure getMarginBottom();

  public abstract Position getPosition();

  public abstract Measure getPaddingLeft();

  public abstract Measure getMarginTop();

  public abstract String getBackgroundImage();

}

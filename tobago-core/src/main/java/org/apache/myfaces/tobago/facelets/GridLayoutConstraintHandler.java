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
import org.apache.myfaces.tobago.component.UIExtensionPanel;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;

public class GridLayoutConstraintHandler extends TagHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GridLayoutConstraintHandler.class);

  private final TagAttribute columnSpan;
  private final TagAttribute rowSpan;

  private final TagAttribute width;
  private final TagAttribute height;

  private final TagAttribute minimumWidth;
  private final TagAttribute minimumHeight;

  private final TagAttribute preferredWidth;
  private final TagAttribute preferredHeight;

  private final TagAttribute maximumWidth;
  private final TagAttribute maximumHeight;

  private final TagAttribute marginLeft;
  private final TagAttribute marginRight;
  private final TagAttribute marginTop;
  private final TagAttribute marginBottom;

  private final TagAttribute borderLeft;
  private final TagAttribute borderRight;
  private final TagAttribute borderTop;
  private final TagAttribute borderBottom;

  private final TagAttribute paddingLeft;
  private final TagAttribute paddingRight;
  private final TagAttribute paddingTop;
  private final TagAttribute paddingBottom;

  public GridLayoutConstraintHandler(final TagConfig config) {
    super(config);
    columnSpan = getAttribute(Attributes.COLUMN_SPAN);
    rowSpan = getAttribute(Attributes.ROW_SPAN);
    width = getAttribute(Attributes.WIDTH);
    height = getAttribute(Attributes.HEIGHT);
    minimumWidth = getAttribute(Attributes.MINIMUM_WIDTH);
    minimumHeight = getAttribute(Attributes.MINIMUM_HEIGHT);
    preferredWidth = getAttribute(Attributes.PREFERRED_WIDTH);
    preferredHeight = getAttribute(Attributes.PREFERRED_HEIGHT);
    maximumWidth = getAttribute(Attributes.MAXIMUM_WIDTH);
    maximumHeight = getAttribute(Attributes.MAXIMUM_HEIGHT);
    marginLeft = getAttribute(Attributes.MARGIN_LEFT);
    marginRight = getAttribute(Attributes.MARGIN_RIGHT);
    marginTop = getAttribute(Attributes.MARGIN_TOP);
    marginBottom = getAttribute(Attributes.MARGIN_BOTTOM);
    borderLeft = getAttribute(Attributes.BORDER_LEFT);
    borderRight = getAttribute(Attributes.BORDER_RIGHT);
    borderTop = getAttribute(Attributes.BORDER_TOP);
    borderBottom = getAttribute(Attributes.BORDER_BOTTOM);
    paddingLeft = getAttribute(Attributes.PADDING_LEFT);
    paddingRight = getAttribute(Attributes.PADDING_RIGHT);
    paddingTop = getAttribute(Attributes.PADDING_TOP);
    paddingBottom = getAttribute(Attributes.PADDING_BOTTOM);
  }

  public void apply(final FaceletContext faceletContext, UIComponent parent) throws IOException {
    if (parent.getParent() != null && parent.getParent() instanceof UIExtensionPanel) {
       parent = parent.getParent();
    } else if (parent.getAttributes().get("tobago.panel") != null
         && parent.getAttributes().get("tobago.panel") instanceof UIExtensionPanel) {
       parent = (UIComponent) parent.getAttributes().get("tobago.panel");
    }
    if (parent instanceof LayoutBase) {
      final LayoutBase component = (LayoutBase) parent;

      if (parent instanceof LayoutComponent && columnSpan != null) {
        if (columnSpan.isLiteral()) {
          ((LayoutComponent) component).setColumnSpan(Integer.valueOf(columnSpan.getValue()));
        } else {
          parent.setValueExpression(Attributes.COLUMN_SPAN,
              columnSpan.getValueExpression(faceletContext, Integer.TYPE));
        }
      }

      if (parent instanceof LayoutComponent && rowSpan != null) {
        if (rowSpan.isLiteral()) {
          ((LayoutComponent) component).setRowSpan(Integer.valueOf(rowSpan.getValue()));
        } else {
          parent.setValueExpression(Attributes.ROW_SPAN,
              rowSpan.getValueExpression(faceletContext, Integer.TYPE));
        }
      }

      if (width != null) {
        if (width.isLiteral()) {
          component.setWidth(Measure.valueOf(width.getValue()));
        } else {
          parent.setValueExpression(Attributes.WIDTH,
              width.getValueExpression(faceletContext, Object.class));
        }
      }

      if (height != null) {
        if (height.isLiteral()) {
          component.setHeight(Measure.valueOf(height.getValue()));
        } else {
          parent.setValueExpression(Attributes.HEIGHT,
              height.getValueExpression(faceletContext, Object.class));
        }
      }

      if (minimumWidth != null) {
        if (minimumWidth.isLiteral()) {
          component.setMinimumWidth(Measure.valueOf(minimumWidth.getValue()));
        } else {
          parent.setValueExpression(Attributes.MINIMUM_WIDTH,
              minimumWidth.getValueExpression(faceletContext, Object.class));
        }
      }

      if (minimumHeight != null) {
        if (minimumHeight.isLiteral()) {
          component.setMinimumHeight(Measure.valueOf(minimumHeight.getValue()));
        } else {
          parent.setValueExpression(Attributes.MINIMUM_HEIGHT,
              minimumHeight.getValueExpression(faceletContext, Object.class));
        }
      }

      if (preferredWidth != null) {
        if (preferredWidth.isLiteral()) {
          component.setPreferredWidth(Measure.valueOf(preferredWidth.getValue()));
        } else {
          parent.setValueExpression(Attributes.PREFERRED_WIDTH,
              preferredWidth.getValueExpression(faceletContext, Object.class));
        }
      }

      if (preferredHeight != null) {
        if (preferredHeight.isLiteral()) {
          component.setPreferredHeight(Measure.valueOf(preferredHeight.getValue()));
        } else {
          parent.setValueExpression(Attributes.PREFERRED_HEIGHT,
              preferredHeight.getValueExpression(faceletContext, Object.class));
        }
      }

      if (maximumWidth != null) {
        if (maximumWidth.isLiteral()) {
          component.setMaximumWidth(Measure.valueOf(maximumWidth.getValue()));
        } else {
          parent.setValueExpression(Attributes.MAXIMUM_WIDTH,
              maximumWidth.getValueExpression(faceletContext, Object.class));
        }
      }

      if (maximumHeight != null) {
        if (maximumHeight.isLiteral()) {
          component.setMaximumHeight(Measure.valueOf(maximumHeight.getValue()));
        } else {
          parent.setValueExpression(Attributes.MAXIMUM_HEIGHT,
              maximumHeight.getValueExpression(faceletContext, Object.class));
        }
      }

      if (marginLeft != null) {
        if (marginLeft.isLiteral()) {
          component.setMarginLeft(Measure.valueOf(marginLeft.getValue()));
        } else {
          parent.setValueExpression(Attributes.MARGIN_LEFT,
              marginLeft.getValueExpression(faceletContext, Object.class));
        }
      }

      if (marginRight != null) {
        if (marginRight.isLiteral()) {
          component.setMarginRight(Measure.valueOf(marginRight.getValue()));
        } else {
          parent.setValueExpression(Attributes.MARGIN_RIGHT,
              marginRight.getValueExpression(faceletContext, Object.class));
        }
      }

      if (marginTop != null) {
        if (marginTop.isLiteral()) {
          component.setMarginTop(Measure.valueOf(marginTop.getValue()));
        } else {
          parent.setValueExpression(Attributes.MARGIN_TOP,
              marginTop.getValueExpression(faceletContext, Object.class));
        }
      }

      if (marginBottom != null) {
        if (marginBottom.isLiteral()) {
          component.setMarginBottom(Measure.valueOf(marginBottom.getValue()));
        } else {
          parent.setValueExpression(Attributes.MARGIN_BOTTOM,
              marginBottom.getValueExpression(faceletContext, Object.class));
        }
      }

    } else {
      LOG.warn("Layout Constraints inside of a non LayoutBase component!");
    }

    if (parent instanceof LayoutContainer) {
      final LayoutContainer container = (LayoutContainer) parent;

      if (borderLeft != null) {
        if (borderLeft.isLiteral()) {
          container.setBorderLeft(Measure.valueOf(borderLeft.getValue()));
        } else {
          parent.setValueExpression(Attributes.BORDER_LEFT,
              borderLeft.getValueExpression(faceletContext, Object.class));
        }
      }

      if (borderRight != null) {
        if (borderRight.isLiteral()) {
          container.setBorderRight(Measure.valueOf(borderRight.getValue()));
        } else {
          parent.setValueExpression(Attributes.BORDER_RIGHT,
              borderRight.getValueExpression(faceletContext, Object.class));
        }
      }

      if (borderTop != null) {
        if (borderTop.isLiteral()) {
          container.setBorderTop(Measure.valueOf(borderTop.getValue()));
        } else {
          parent.setValueExpression(Attributes.BORDER_TOP,
              borderTop.getValueExpression(faceletContext, Object.class));
        }
      }

      if (borderBottom != null) {
        if (borderBottom.isLiteral()) {
          container.setBorderBottom(Measure.valueOf(borderBottom.getValue()));
        } else {
          parent.setValueExpression(Attributes.BORDER_BOTTOM,
              borderBottom.getValueExpression(faceletContext, Object.class));
        }
      }

      if (paddingLeft != null) {
        if (paddingLeft.isLiteral()) {
          container.setPaddingLeft(Measure.valueOf(paddingLeft.getValue()));
        } else {
          parent.setValueExpression(Attributes.PADDING_LEFT,
              paddingLeft.getValueExpression(faceletContext, Object.class));
        }
      }

      if (paddingRight != null) {
        if (paddingRight.isLiteral()) {
          container.setPaddingRight(Measure.valueOf(paddingRight.getValue()));
        } else {
          parent.setValueExpression(Attributes.PADDING_RIGHT,
              paddingRight.getValueExpression(faceletContext, Object.class));
        }
      }

      if (paddingTop != null) {
        if (paddingTop.isLiteral()) {
          container.setPaddingTop(Measure.valueOf(paddingTop.getValue()));
        } else {
          parent.setValueExpression(Attributes.PADDING_TOP,
              paddingTop.getValueExpression(faceletContext, Object.class));
        }
      }

      if (paddingBottom != null) {
        if (paddingBottom.isLiteral()) {
          container.setPaddingBottom(Measure.valueOf(paddingBottom.getValue()));
        } else {
          parent.setValueExpression(Attributes.PADDING_BOTTOM,
              paddingBottom.getValueExpression(faceletContext, Object.class));
        }
      }

    } else {
      if (LOG.isWarnEnabled()) {
        if (borderLeft != null) {
          LOG.warn("Ignoring border left, because the parent is not a LayoutContainer!");
        }
        if (borderRight != null) {
          LOG.warn("Ignoring border right, because the parent is not a LayoutContainer!");
        }
        if (borderTop != null) {
          LOG.warn("Ignoring border top, because the parent is not a LayoutContainer!");
        }
        if (borderBottom != null) {
          LOG.warn("Ignoring border bottom, because the parent is not a LayoutContainer!");
        }
        if (paddingLeft != null) {
          LOG.warn("Ignoring padding left, because the parent is not a LayoutContainer!");
        }
        if (paddingRight != null) {
          LOG.warn("Ignoring padding right, because the parent is not a LayoutContainer!");
        }
        if (paddingTop != null) {
          LOG.warn("Ignoring padding top, because the parent is not a LayoutContainer!");
        }
        if (paddingBottom != null) {
          LOG.warn("Ignoring padding bottom, because the parent is not a LayoutContainer!");
        }
      }
    }
  }
}

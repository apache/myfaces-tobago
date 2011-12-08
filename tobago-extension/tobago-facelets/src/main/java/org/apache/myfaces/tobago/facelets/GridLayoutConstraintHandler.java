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

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagHandler;
import org.apache.myfaces.tobago.component.UIExtensionPanel;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.Measure;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
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

  public GridLayoutConstraintHandler(TagConfig config) {
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

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws IOException, FacesException, ELException {
    if (parent.getParent() != null && parent.getParent() instanceof UIExtensionPanel) {
       parent = parent.getParent();
    } else if (parent.getAttributes().get("parent") != null
         && parent.getAttributes().get("parent") instanceof UIExtensionPanel) {
       parent = parent.getParent();
    }
    if (parent instanceof LayoutBase) {
      LayoutBase component = (LayoutBase) parent;

      if (parent instanceof LayoutComponent && columnSpan != null) {
        if (columnSpan.isLiteral()) {
          ((LayoutComponent) component).setColumnSpan(Integer.valueOf(columnSpan.getValue()));
        } else {
          parent.setValueBinding(Attributes.COLUMN_SPAN,
              faceletContext.getFacesContext().getApplication().createValueBinding(columnSpan.getValue()));
        }
      }

      if (parent instanceof LayoutComponent && rowSpan != null) {
        if (rowSpan.isLiteral()) {
          ((LayoutComponent) component).setRowSpan(Integer.valueOf(rowSpan.getValue()));
        } else {
          parent.setValueBinding(Attributes.ROW_SPAN,
              faceletContext.getFacesContext().getApplication().createValueBinding(rowSpan.getValue()));
        }
      }

      if (width != null) {
        if (width.isLiteral()) {
          component.setWidth(Measure.parse(width.getValue()));
        } else {
          parent.setValueBinding(Attributes.WIDTH,
              faceletContext.getFacesContext().getApplication().createValueBinding(width.getValue()));
        }
      }

      if (height != null) {
        if (height.isLiteral()) {
          component.setHeight(Measure.parse(height.getValue()));
        } else {
          parent.setValueBinding(Attributes.HEIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(height.getValue()));
        }
      }

      if (minimumWidth != null) {
        if (minimumWidth.isLiteral()) {
          component.setMinimumWidth(Measure.parse(minimumWidth.getValue()));
        } else {
          parent.setValueBinding(Attributes.MINIMUM_WIDTH,
              faceletContext.getFacesContext().getApplication().createValueBinding(minimumWidth.getValue()));
        }
      }

      if (minimumHeight != null) {
        if (minimumHeight.isLiteral()) {
          component.setMinimumHeight(Measure.parse(minimumHeight.getValue()));
        } else {
          parent.setValueBinding(Attributes.MINIMUM_HEIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(minimumHeight.getValue()));
        }
      }

      if (preferredWidth != null) {
        if (preferredWidth.isLiteral()) {
          component.setPreferredWidth(Measure.parse(preferredWidth.getValue()));
        } else {
          parent.setValueBinding(Attributes.PREFERRED_WIDTH,
              faceletContext.getFacesContext().getApplication().createValueBinding(preferredWidth.getValue()));
        }
      }

      if (preferredHeight != null) {
        if (preferredHeight.isLiteral()) {
          component.setPreferredHeight(Measure.parse(preferredHeight.getValue()));
        } else {
          parent.setValueBinding(Attributes.PREFERRED_HEIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(preferredHeight.getValue()));
        }
      }

      if (maximumWidth != null) {
        if (maximumWidth.isLiteral()) {
          component.setMaximumWidth(Measure.parse(maximumWidth.getValue()));
        } else {
          parent.setValueBinding(Attributes.MAXIMUM_WIDTH,
              faceletContext.getFacesContext().getApplication().createValueBinding(maximumWidth.getValue()));
        }
      }

      if (maximumHeight != null) {
        if (maximumHeight.isLiteral()) {
          component.setMaximumHeight(Measure.parse(maximumHeight.getValue()));
        } else {
          parent.setValueBinding(Attributes.MAXIMUM_HEIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(maximumHeight.getValue()));
        }
      }

      if (marginLeft != null) {
        if (marginLeft.isLiteral()) {
          component.setMarginLeft(Measure.parse(marginLeft.getValue()));
        } else {
          parent.setValueBinding(Attributes.MARGIN_LEFT,
              faceletContext.getFacesContext().getApplication().createValueBinding(marginLeft.getValue()));
        }
      }

      if (marginRight != null) {
        if (marginRight.isLiteral()) {
          component.setMarginRight(Measure.parse(marginRight.getValue()));
        } else {
          parent.setValueBinding(Attributes.MARGIN_RIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(marginRight.getValue()));
        }
      }

      if (marginTop != null) {
        if (marginTop.isLiteral()) {
          component.setMarginTop(Measure.parse(marginTop.getValue()));
        } else {
          parent.setValueBinding(Attributes.MARGIN_TOP,
              faceletContext.getFacesContext().getApplication().createValueBinding(marginTop.getValue()));
        }
      }

      if (marginBottom != null) {
        if (marginBottom.isLiteral()) {
          component.setMarginBottom(Measure.parse(marginBottom.getValue()));
        } else {
          parent.setValueBinding(Attributes.MARGIN_BOTTOM,
              faceletContext.getFacesContext().getApplication().createValueBinding(marginBottom.getValue()));
        }
      }

    } else {
      LOG.warn("Layout Constraints inside of a non LayoutBase component!");
    }

    if (parent instanceof LayoutContainer) {
      LayoutContainer container = (LayoutContainer) parent;

      if (borderLeft != null) {
        if (borderLeft.isLiteral()) {
          container.setBorderLeft(Measure.parse(borderLeft.getValue()));
        } else {
          parent.setValueBinding(Attributes.BORDER_LEFT,
              faceletContext.getFacesContext().getApplication().createValueBinding(borderLeft.getValue()));
        }
      }

      if (borderRight != null) {
        if (borderRight.isLiteral()) {
          container.setBorderRight(Measure.parse(borderRight.getValue()));
        } else {
          parent.setValueBinding(Attributes.BORDER_RIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(borderRight.getValue()));
        }
      }

      if (borderTop != null) {
        if (borderTop.isLiteral()) {
          container.setBorderTop(Measure.parse(borderTop.getValue()));
        } else {
          parent.setValueBinding(Attributes.BORDER_TOP,
              faceletContext.getFacesContext().getApplication().createValueBinding(borderTop.getValue()));
        }
      }

      if (borderBottom != null) {
        if (borderBottom.isLiteral()) {
          container.setBorderBottom(Measure.parse(borderBottom.getValue()));
        } else {
          parent.setValueBinding(Attributes.BORDER_BOTTOM,
              faceletContext.getFacesContext().getApplication().createValueBinding(borderBottom.getValue()));
        }
      }

      if (paddingLeft != null) {
        if (paddingLeft.isLiteral()) {
          container.setPaddingLeft(Measure.parse(paddingLeft.getValue()));
        } else {
          parent.setValueBinding(Attributes.PADDING_LEFT,
              faceletContext.getFacesContext().getApplication().createValueBinding(paddingLeft.getValue()));
        }
      }

      if (paddingRight != null) {
        if (paddingRight.isLiteral()) {
          container.setPaddingRight(Measure.parse(paddingRight.getValue()));
        } else {
          parent.setValueBinding(Attributes.PADDING_RIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(paddingRight.getValue()));
        }
      }

      if (paddingTop != null) {
        if (paddingTop.isLiteral()) {
          container.setPaddingTop(Measure.parse(paddingTop.getValue()));
        } else {
          parent.setValueBinding(Attributes.PADDING_TOP,
              faceletContext.getFacesContext().getApplication().createValueBinding(paddingTop.getValue()));
        }
      }

      if (paddingBottom != null) {
        if (paddingBottom.isLiteral()) {
          container.setPaddingBottom(Measure.parse(paddingBottom.getValue()));
        } else {
          parent.setValueBinding(Attributes.PADDING_BOTTOM,
              faceletContext.getFacesContext().getApplication().createValueBinding(paddingBottom.getValue()));
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

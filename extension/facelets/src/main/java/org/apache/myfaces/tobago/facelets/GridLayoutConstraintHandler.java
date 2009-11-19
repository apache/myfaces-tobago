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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.Measure;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class GridLayoutConstraintHandler extends TagHandler {

  private static final Log LOG = LogFactory.getLog(GridLayoutConstraintHandler.class);

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
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws IOException, FacesException, ELException {

    if (parent instanceof LayoutBase) {
      LayoutBase component = (LayoutBase) parent;

      if (parent instanceof LayoutComponent && columnSpan != null) {
        LOG.error(columnSpan.getValue());
        if (columnSpan.isLiteral()) {
          ((LayoutComponent) component).setColumnSpan(Integer.valueOf(columnSpan.getValue()));
        } else {
          parent.setValueBinding(Attributes.COLUMN_SPAN,
              faceletContext.getFacesContext().getApplication().createValueBinding(columnSpan.getValue()));
        }
      }

      if (parent instanceof LayoutComponent && rowSpan != null) {
        LOG.error(rowSpan.getValue());
        if (rowSpan.isLiteral()) {
          ((LayoutComponent) component).setRowSpan(Integer.valueOf(rowSpan.getValue()));
        } else {
          parent.setValueBinding(Attributes.ROW_SPAN,
              faceletContext.getFacesContext().getApplication().createValueBinding(rowSpan.getValue()));
        }
      }

      if (width != null) {
        LOG.error(width.getValue());
        if (width.isLiteral()) {
          component.setWidth(Measure.parse(width.getValue()));
        } else {
          parent.setValueBinding(Attributes.WIDTH,
              faceletContext.getFacesContext().getApplication().createValueBinding(width.getValue()));
        }
      }

      if (height != null) {
        LOG.error(height.getValue());
        if (height.isLiteral()) {
          component.setHeight(Measure.parse(height.getValue()));
        } else {
          parent.setValueBinding(Attributes.HEIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(height.getValue()));
        }
      }

      if (minimumWidth != null) {
        LOG.error(minimumWidth.getValue());
        if (minimumWidth.isLiteral()) {
          component.setMinimumWidth(Measure.parse(minimumWidth.getValue()));
        } else {
          parent.setValueBinding(Attributes.MINIMUM_WIDTH,
              faceletContext.getFacesContext().getApplication().createValueBinding(minimumWidth.getValue()));
        }
      }

      if (minimumHeight != null) {
        LOG.error(minimumHeight.getValue());
        if (minimumHeight.isLiteral()) {
          component.setMinimumHeight(Measure.parse(minimumHeight.getValue()));
        } else {
          parent.setValueBinding(Attributes.MINIMUM_HEIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(minimumHeight.getValue()));
        }
      }

      if (preferredWidth != null) {
        LOG.error(preferredWidth.getValue());
        if (preferredWidth.isLiteral()) {
          component.setPreferredWidth(Measure.parse(preferredWidth.getValue()));
        } else {
          parent.setValueBinding(Attributes.PREFERRED_WIDTH,
              faceletContext.getFacesContext().getApplication().createValueBinding(preferredWidth.getValue()));
        }
      }

      if (preferredHeight != null) {
        LOG.error(preferredHeight.getValue());
        if (preferredHeight.isLiteral()) {
          component.setPreferredHeight(Measure.parse(preferredHeight.getValue()));
        } else {
          parent.setValueBinding(Attributes.PREFERRED_HEIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(preferredHeight.getValue()));
        }
      }

      if (maximumWidth != null) {
        LOG.error(maximumWidth.getValue());
        if (maximumWidth.isLiteral()) {
          component.setMaximumWidth(Measure.parse(maximumWidth.getValue()));
        } else {
          parent.setValueBinding(Attributes.MAXIMUM_WIDTH,
              faceletContext.getFacesContext().getApplication().createValueBinding(maximumWidth.getValue()));
        }
      }

      if (maximumHeight != null) {
        LOG.error(maximumHeight.getValue());
        if (maximumHeight.isLiteral()) {
          component.setMaximumHeight(Measure.parse(maximumHeight.getValue()));
        } else {
          parent.setValueBinding(Attributes.MAXIMUM_HEIGHT,
              faceletContext.getFacesContext().getApplication().createValueBinding(maximumHeight.getValue()));
        }
      }
    } else {
      LOG.warn("");
    }
  }
}

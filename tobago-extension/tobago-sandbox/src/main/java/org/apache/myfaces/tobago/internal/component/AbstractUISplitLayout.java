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

import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

public abstract class AbstractUISplitLayout extends AbstractUIGridLayout {

  public static final String VERTICAL = Orientation.VERTICAL.name();
  public static final String HORIZONTAL = Orientation.HORIZONTAL.name();

  private String submittedLayout;

  public void updateLayout(final int position) {
    final LayoutContainer container = (LayoutContainer) getParent();
    final LayoutComponent firstComponent = container.getComponents().get(0);
    final LayoutComponent secondComponent = container.getComponents().get(1);
    final int oldPosition;

    final int currentSize1;
    final int currentSize2;
    if (HORIZONTAL.equals(getOrientation())) {
      oldPosition = secondComponent.getLeft().getPixel() - 5;
      currentSize1 = firstComponent.getCurrentWidth().getPixel();
      currentSize2 = secondComponent.getCurrentWidth().getPixel();
    } else {
      oldPosition = secondComponent.getTop().getPixel() - 5;
      currentSize1 = firstComponent.getCurrentHeight().getPixel();
      currentSize2 = secondComponent.getCurrentHeight().getPixel();
    }

    final int offset = position - oldPosition;
    final int newSize1 = currentSize1 + offset;
    final int newSize2 = currentSize2 - offset;

    final int ggt = gcd(newSize1, newSize2);
    submittedLayout = new StringBuilder()
        .append(Integer.toString(newSize1 / ggt)).append("*;")
        .append(Integer.toString(newSize2 / ggt)).append("*")
        .toString();
  }

  // TODO: MathUtils
  public static int gcd(int a, int b) {
    if (a < 0) {
      a = -a;
    }
    if (b < 0) {
      b = -b;
    }
    int t;
    while (b != 0) {
      t = a % b;
      a = b;
      b = t;
    }
    return a;
  }

  @Override
  public void processUpdates(final FacesContext facesContext) {
    updateModel(facesContext);
    super.processUpdates(facesContext);
  }

  private void updateModel(final FacesContext facesContext) {
    if (submittedLayout != null) {
      final ValueExpression expression = getValueExpression("layout");
      if (expression != null) {
        final ELContext elContext = facesContext.getELContext();
        expression.setValue(elContext, submittedLayout);
        submittedLayout = null;
      }
    }
  }

  public Measure getSpacing(final Orientation orientation) {
    return orientation == Orientation.HORIZONTAL ? getColumnSpacing() : getRowSpacing();
  }

@Override
  public void setColumns(final String columns) {
  }

  @Override
  public String getColumns() {
    return VERTICAL.equals(getOrientation()) ? "1*" : getLayout2();
  }

//  private String getLayout2() {
//    return getLayout().replace(";", ";5px;");
//  }

  @Override
  public void setRows(final String rows) {
  }

  @Override
  public String getRows() {
    return HORIZONTAL.equals(getOrientation()) ? "1*" : getLayout2();
  }

  private String getLayout2() {
    return submittedLayout != null ? submittedLayout : getLayout();
  }

  @Override
  public boolean isRowOverflow() {
    return false;
  }

  @Override
  public boolean isColumnOverflow() {
    return false;
  }

  public abstract String getLayout();

  public abstract String getOrientation();

  @Deprecated
  public abstract Measure getCellspacing();

  public abstract Measure getRowSpacing();

  public abstract Measure getColumnSpacing();

  public abstract Measure getMarginLeft();

  public abstract Measure getMarginTop();

  public abstract Measure getMarginRight();

  public abstract Measure getMarginBottom();

  public abstract boolean isRigid();
}

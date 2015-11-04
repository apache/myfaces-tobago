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

import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.List;

public abstract class AbstractUISplitLayout extends AbstractUIGridLayout {

  private String submittedLayout;

  public void updateLayout(final int position) {
    final List<UIComponent> components = LayoutUtils.findLayoutChildren(getParent());
    final Visual firstComponent = (Visual) components.get(0);
    final Visual secondComponent = (Visual) components.get(1);
    final int oldPosition;

    final int currentSize1;
    final int currentSize2;
/* XXX to be reimplemented: not using GridLayout, it might be better using flex?!
    if (getOrientation() == Orientation.horizontal) {
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
    submittedLayout = Integer.toString(newSize1 / ggt) + "*;" + Integer.toString(newSize2 / ggt) + "*";
*/
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

@Override
  public void setColumns(final String columns) {
  }

  @Override
  public String getColumns() {
    return getOrientation() == Orientation.vertical ? "1*" : getLayout2();
  }

//  private String getLayout2() {
//    return getLayout().replace(";", ";5px;");
//  }

  @Override
  public void setRows(final String rows) {
  }

  @Override
  public String getRows() {
    return getOrientation() == Orientation.horizontal ? "1*" : getLayout2();
  }

  private String getLayout2() {
    return submittedLayout != null ? submittedLayout : getLayout();
  }

  public abstract String getLayout();

  public abstract Orientation getOrientation();

  public abstract Measure getRowSpacing();

  public abstract Measure getColumnSpacing();

  public abstract boolean isRigid();
}

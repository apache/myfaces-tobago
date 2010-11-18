package org.apache.myfaces.tobago.internal.component;

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

import org.apache.myfaces.tobago.internal.layout.Interval;
import org.apache.myfaces.tobago.internal.layout.IntervalList;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUITabGroupLayout extends AbstractUILayoutBase implements LayoutManager {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIGridLayout.class);

  private boolean horizontalAuto;
  private boolean verticalAuto;

  public void init() {
    for (LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().init();
      }
    }
  }

  public void fixRelativeInsideAuto(Orientation orientation, boolean auto) {

    if (orientation == Orientation.HORIZONTAL) {
      horizontalAuto = auto;
    } else {
      verticalAuto = auto;
    }

    for (LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().fixRelativeInsideAuto(orientation, auto);
      }
    }
  }

  public void preProcessing(Orientation orientation) {

    // process auto tokens
    IntervalList intervals = new IntervalList();
    for (LayoutComponent component : getLayoutContainer().getComponents()) {

      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().preProcessing(orientation);
      }

      if (orientation == Orientation.HORIZONTAL && horizontalAuto 
          || orientation == Orientation.VERTICAL && verticalAuto) {
        intervals.add(new Interval(component, orientation));
      }
    }

    if (intervals.size() >= 1) {
      intervals.evaluate();
      Measure size = intervals.getCurrent();
      size = size.add(LayoutUtils.getBorderBegin(orientation, getLayoutContainer()));
      size = size.add(LayoutUtils.getBorderEnd(orientation, getLayoutContainer()));
      LayoutUtils.setCurrentSize(orientation, getLayoutContainer(), size);
    }
  }

  public void mainProcessing(Orientation orientation) {

    // find *
    if (orientation == Orientation.HORIZONTAL && !horizontalAuto 
        || orientation == Orientation.VERTICAL && !verticalAuto) {
      // find rest
      LayoutContainer container = getLayoutContainer();
      Measure available = LayoutUtils.getCurrentSize(orientation, container);
      if (available != null) {
        available = available.subtractNotNegative(LayoutUtils.getBorderBegin(orientation, container));
        available = available.subtractNotNegative(LayoutUtils.getBorderEnd(orientation, container));

        for (LayoutComponent component : getLayoutContainer().getComponents()) {

          component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css
          LayoutUtils.setCurrentSize(orientation, component, available);


          // call sub layout manager
          if (component instanceof LayoutContainer) {
            ((LayoutContainer) component).getLayoutManager().mainProcessing(orientation);
          }
        }
      } else {
        LOG.warn("No width/height set but needed for *!"); // todo: more information
      }
    }
  }

  public void postProcessing(Orientation orientation) {

    // set positions to all sub-layout-managers

    for (LayoutComponent component : getLayoutContainer().getComponents()) {

      component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css

      // compute the position of the cell
      Measure position = LayoutUtils.getBorderBegin(orientation, getLayoutContainer());
      if (orientation == Orientation.HORIZONTAL) {
        component.setLeft(position);
      } else {
        component.setTop(position);
      }

      // call sub layout manager
      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().postProcessing(orientation);
      }

      // todo: optimize: the AutoLayoutTokens with columnSpan=1 are already called
    }
  }

  private LayoutContainer getLayoutContainer() {
    // todo: check with instanceof and do something in the error case
    return ((LayoutContainer) getParent());
  }

  @Override
  public boolean getRendersChildren() {
    return false;
  }
}

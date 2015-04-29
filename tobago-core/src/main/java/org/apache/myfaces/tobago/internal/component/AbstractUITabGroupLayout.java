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

import org.apache.myfaces.tobago.layout.LayoutManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUITabGroupLayout extends AbstractUILayoutBase implements LayoutManager {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIGridLayout.class);

  private boolean horizontalAuto;
  private boolean verticalAuto;

/*
  public void init() {
    for (final LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer && component.isRendered()) {
        ((LayoutContainer) component).getLayoutManager().init();
      }
    }
  }

  public void fixRelativeInsideAuto(final Orientation orientation, final boolean auto) {

    if (orientation == Orientation.HORIZONTAL) {
      horizontalAuto = auto;
    } else {
      verticalAuto = auto;
    }

    for (final LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer && component.isRendered()) {
        ((LayoutContainer) component).getLayoutManager().fixRelativeInsideAuto(orientation, auto);
      }
    }
  }

  public void preProcessing(final Orientation orientation) {

    // process auto tokens
    final IntervalList intervals = new IntervalList();
    for (final LayoutComponent component : getLayoutContainer().getComponents()) {

      if (component instanceof LayoutContainer && component.isRendered()) {
        ((LayoutContainer) component).getLayoutManager().preProcessing(orientation);
      }

      if (orientation == Orientation.HORIZONTAL && horizontalAuto
          || orientation == Orientation.VERTICAL && verticalAuto) {
        intervals.add(new Interval(component, orientation));
      }
    }

    if (intervals.size() >= 1) {
      intervals.evaluate();
      final Measure size = intervals.getCurrent();
*/
/*
      size = size.add(LayoutUtils.getBorderBegin(orientation, getLayoutContainer()));
      size = size.add(LayoutUtils.getBorderEnd(orientation, getLayoutContainer()));
*//*

      LayoutUtils.setCurrentSize(orientation, getLayoutContainer(), size);
    }
  }

  public void mainProcessing(final Orientation orientation) {

    // find *
    if (orientation == Orientation.HORIZONTAL && !horizontalAuto
        || orientation == Orientation.VERTICAL && !verticalAuto) {
      // find rest
      final LayoutContainer container = getLayoutContainer();
      final Measure available = LayoutUtils.getCurrentSize(orientation, container);
      if (available != null) {
        for (final LayoutComponent component : getLayoutContainer().getComponents()) {
          LayoutUtils.setCurrentSize(orientation, component, available);
        }
      } else {
        LOG.warn("No width/height set but needed for *!"); // todo: more information
      }
    }

    // call sub layout manager
    for (final LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer && component.isRendered()) {
        ((LayoutContainer) component).getLayoutManager().mainProcessing(orientation);
      }
    }
  }

  public void postProcessing(final Orientation orientation) {

    // set positions to all sub-layout-managers
    for (final LayoutComponent component : getLayoutContainer().getComponents()) {
      // call sub layout manager
      if (component instanceof LayoutContainer && component.isRendered()) {
        ((LayoutContainer) component).getLayoutManager().postProcessing(orientation);
      }

      // todo: optimize: the AutoLayoutTokens with columnSpan=1 are already called
    }
  }
*/

  @Override
  public boolean getRendersChildren() {
    return false;
  }
}

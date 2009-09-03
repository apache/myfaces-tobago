package org.apache.myfaces.tobago.component;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.OnComponentCreated;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Interval;
import org.apache.myfaces.tobago.layout.IntervalList;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutUtils;
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public abstract class AbstractUITabGroupLayout extends UILayout implements LayoutManager, OnComponentCreated {

  private static final Log LOG = LogFactory.getLog(AbstractUIGridLayout.class);

  private boolean horizontalAuto;
  private boolean verticalAuto;

  public void onComponentCreated(FacesContext context, UIComponent component) {
  }

  public void init() {
    for (LayoutComponent component : getLayoutContainer().getComponents()) {
      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().init();
      }
    }
  }

  public void fixRelativeInsideAuto(boolean orientation, boolean auto) {

    if (orientation) {
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

  public void preProcessing(boolean orientation) {

    // process auto tokens
    int i = 0;
    IntervalList intervals = new IntervalList();
    for (LayoutComponent component : getLayoutContainer().getComponents()) {

      if (component instanceof LayoutContainer) {
        ((LayoutContainer) component).getLayoutManager().preProcessing(orientation);
      }

      if (orientation && horizontalAuto || !orientation && verticalAuto) {
        intervals.add(new Interval(component, orientation));
      }
    }

    if (intervals.size() >= 1) {
      Measure size = intervals.computeAuto();
      size = size.add(LayoutUtils.getBeginOffset(orientation, getLayoutContainer()));
      size = size.add(LayoutUtils.getEndOffset(orientation, getLayoutContainer()));
      LayoutUtils.setSize(orientation, getLayoutContainer(), size);
    }
  }

  public void mainProcessing(boolean orientation) {

    // find *
    {
      if (orientation && !horizontalAuto || !orientation && !verticalAuto) {
        // find rest
        LayoutContainer container = getLayoutContainer();
        Measure available = LayoutUtils.getSize(orientation, container);
        if (available != null) {
          available = available.substractNotNegative(LayoutUtils.getBeginOffset(orientation, container));
          available = available.substractNotNegative(LayoutUtils.getEndOffset(orientation, container));

          for (LayoutComponent component : getLayoutContainer().getComponents()) {

            component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css
            LayoutUtils.setSize(orientation, component, available);


            // call sub layout manager
            if (component instanceof LayoutContainer) {
              ((LayoutContainer) component).getLayoutManager().mainProcessing(orientation);
            }
          }
        } else {
          LOG.warn("No width/height set but needed for *!");// todo: more information
        }
      }
    }
  }

  public void postProcessing(boolean orientation) {

    // set positions to all sub-layout-managers

    for (LayoutComponent component : getLayoutContainer().getComponents()) {

      component.setDisplay(Display.BLOCK); // TODO: use CSS via classes and style.css

      // compute the position of the cell
      Measure position = LayoutUtils.getBeginOffset(orientation, getLayoutContainer());
      if (orientation) {
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

  public void collect(LayoutContext layoutContext, LayoutContainer container, int horizontalIndex, int verticalIndex) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void distribute(LayoutContext layoutContext, LayoutContainer container) {
    //To change body of implemented methods use File | Settings | File Templates.
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

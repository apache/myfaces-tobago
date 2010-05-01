package org.apache.myfaces.tobago.internal.layout;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.Orientation;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/*
An algorithm for laying out ...

- get UIPage
  - call compute-sizes
    - get the LayoutManager
      - go to the PX elements
        - call compute-sizes (recursively)
      - go to the Auto elements
        - call compute-sizes (recursively)
        - compute the max size of the elements and set it to the row/column
      - check given size: if not set: warn
      - calculate remainder = given size - all px sizes - all auto sizes
      - go to the * elements
        - partition remainder to this elements (3*;2*)
        - call compute-sizes (recursively)
  - call set-positions
      - compute and set positions of columns/rows
      - call set-positions for all elements (recursively)

todo: describe what happens, when there is a rendered=false (if all in one bank than collapse)
todo: describe what happens, when there are too less components (there a free space)
todo: describe what happens, when there are too much components (there a rows with * added)
 */
public class LayoutContext {

  private static final Logger LOG = LoggerFactory.getLogger(LayoutContext.class);

  private LayoutContainer container;

  public LayoutContext(LayoutContainer container) {
    this.container = container;
  }

  public void layout() {

    LayoutManager layoutManager = container.getLayoutManager();
    layoutManager.init();
    layoutManager.fixRelativeInsideAuto(Orientation.HORIZONTAL, false);
    layoutManager.fixRelativeInsideAuto(Orientation.VERTICAL, false);
    layoutManager.preProcessing(Orientation.HORIZONTAL);
    layoutManager.preProcessing(Orientation.VERTICAL);
    layoutManager.mainProcessing(Orientation.HORIZONTAL);
    layoutManager.mainProcessing(Orientation.VERTICAL);
    layoutManager.postProcessing(Orientation.HORIZONTAL);
    layoutManager.postProcessing(Orientation.VERTICAL);

    log();
  }

  private void log() {
    StringBuffer buffer = new StringBuffer("\n");
    log(buffer, (UIComponent) container, 0);
    LOG.info(buffer.toString());
  }

  private void log(StringBuffer buffer, UIComponent component, int depth) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    for (int i = 0; i < depth; i++) {
      buffer.append("  ");
    }
    buffer.append(component.getClass().getSimpleName());
    buffer.append("#");
    buffer.append(component.getClientId(facesContext));
    if (component instanceof LayoutBase) {
      buffer.append("(");
      buffer.append(((LayoutBase) component).getCurrentWidth());
      buffer.append(", ");
      buffer.append(((LayoutBase) component).getCurrentHeight());
      buffer.append(")");
    }
    if (component instanceof LayoutContainer) {
      LayoutManager layoutManager = ((LayoutContainer) component).getLayoutManager();
      if (layoutManager instanceof AbstractUIGridLayout) {
        buffer.append(" ");
        buffer.append(layoutManager.toString());
      }
    }
    buffer.append("\n");
    for (Object child : component.getChildren()) {
      log(buffer, (UIComponent) child, depth + 2);
    }
  }
}

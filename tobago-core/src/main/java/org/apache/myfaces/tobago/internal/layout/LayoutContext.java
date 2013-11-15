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

package org.apache.myfaces.tobago.internal.layout;

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.Orientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.text.DecimalFormat;

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

  public LayoutContext(final LayoutContainer container) {
    this.container = container;
  }

  public void layout() {

    long begin = 0;
    final boolean debug = LOG.isDebugEnabled();
    final boolean trace = LOG.isTraceEnabled();
    if (debug) {
      begin = System.nanoTime();
    }

    final LayoutManager layoutManager = container.getLayoutManager();
    layoutManager.init();
    if (trace) {
      log("after init", true);
    }
    layoutManager.fixRelativeInsideAuto(Orientation.VERTICAL, false);
    if (trace) {
      log("after fixRelativeInsideAuto vertical", true);
    }
    layoutManager.fixRelativeInsideAuto(Orientation.HORIZONTAL, false);
    if (trace) {
      log("after fixRelativeInsideAuto horizontal", true);
    }
    layoutManager.preProcessing(Orientation.VERTICAL);
    if (trace) {
      log("after preProcessing vertical", true);
    }
    layoutManager.preProcessing(Orientation.HORIZONTAL);
    if (trace) {
      log("after preProcessing horizontal", true);
    }
    layoutManager.mainProcessing(Orientation.VERTICAL);
    if (trace) {
      log("after mainProcessing vertical", true);
    }
    layoutManager.mainProcessing(Orientation.HORIZONTAL);
    if (trace) {
      log("after mainProcessing horizontal", true);
    }
    layoutManager.postProcessing(Orientation.VERTICAL);
    if (trace) {
      log("after postProcessing vertical", true);
    }
    layoutManager.postProcessing(Orientation.HORIZONTAL);
    if (trace) {
      log("after postProcessing horizontal", true);
    }

    if (debug) {
      LOG.debug("Laying out takes: {} ns", new DecimalFormat("#,##0").format(System.nanoTime() - begin));
      log("after layout", false);
    }
  }

  private void log(final String message, final boolean trace) {
    final StringBuffer buffer = new StringBuffer(message + "\n");
    log(buffer, (UIComponent) container, 0);
    if (trace) {
      LOG.trace(buffer.toString());
    } else {
      LOG.debug(buffer.toString());
    }
  }

  private void log(final StringBuffer buffer, final UIComponent component, final int depth) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    buffer.append(StringUtils.repeat("  ", depth));
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
      final LayoutManager layoutManager = ((LayoutContainer) component).getLayoutManager();
      if (layoutManager instanceof AbstractUIGridLayout) {
        buffer.append("\n");
        buffer.append(StringUtils.repeat("  ", depth + 4));
        buffer.append("layout: ");
        buffer.append(((AbstractUIGridLayout) layoutManager).toString(depth));
      }
    }
    buffer.append("\n");
    for (final UIComponent child : component.getChildren()) {
      log(buffer, child, depth + 2);
    }
  }
}

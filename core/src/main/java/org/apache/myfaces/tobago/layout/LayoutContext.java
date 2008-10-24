package org.apache.myfaces.tobago.layout;

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
import org.apache.myfaces.tobago.component.AbstractUIGridConstraints;
import org.apache.myfaces.tobago.layout.math.EquationManager;

public class LayoutContext {

  private static final Log LOG = LogFactory.getLog(LayoutContext.class);

  private EquationManager horizontal;
  private EquationManager vertical;
  private Container container;

  public LayoutContext(Container container) {
    this.horizontal = new EquationManager();
    this.vertical = new EquationManager();
    this.container = container;
  }

  public EquationManager getHorizontal() {
    return horizontal;
  }

  public EquationManager getVertical() {
    return vertical;
  }

  public void layout() {

    horizontal.setFixedLength(0, ((AbstractUIGridConstraints)container.getConstraints()).getWidth().getPixel());
    vertical.setFixedLength(0, ((AbstractUIGridConstraints)container.getConstraints()).getHeight().getPixel());

    horizontal.descend(0, 1);
    vertical.descend(0, 1);
    container.getLayoutManager().collect(this, container);
    horizontal.ascend();
    vertical.ascend();

    horizontal.solve();
    vertical.solve();

    horizontal.descend(0, 1);
    vertical.descend(0, 1);
    container.getLayoutManager().distribute(this, container);
    horizontal.ascend();
    vertical.ascend();
  }
}

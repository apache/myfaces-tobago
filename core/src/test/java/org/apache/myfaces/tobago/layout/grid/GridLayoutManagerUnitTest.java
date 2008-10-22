package org.apache.myfaces.tobago.layout.grid;

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

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.layout.Component;
import org.apache.myfaces.tobago.layout.ComponentImpl;
import org.apache.myfaces.tobago.layout.Container;
import org.apache.myfaces.tobago.layout.ContainerImpl;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.math.AssertUtils;
import org.apache.myfaces.tobago.layout.math.EquationManager;

import java.util.Arrays;

public class GridLayoutManagerUnitTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(GridLayoutManagerUnitTest.class);

  /**
   * <pre>
   * |                  800px               |
   * |    *    |    2*    |       500px     |
   * |         |          |    7*   |   3*  |
   * <p/>
   * +---------+----------+-----------------+  ----- ----- -----
   * |         |                            |
   * |         |                            |          *
   * |         |                            |
   * +---------+----------+--------+--------+  800px ----- -----
   * |         |          |        |        |                *
   * |         |          +--------+--------+        600px -----
   * |         |          |        |        |                *
   * +---------+----------+--------+--------+  ----- ----- -----
   * </pre>
   */
  public void test() {
    Container container = new ContainerImpl();
    Component span = new ComponentImpl();
    GridConstraints bConstraint = GridConstraints.getConstraints(span);
    bConstraint.setRowSpan(2);

    container.getComponents().add(new ComponentImpl());
    container.getComponents().add(span);
    container.getComponents().add(new ComponentImpl());
    container.getComponents().add(new ComponentImpl());

    Container subContainer = new ContainerImpl();

    container.getComponents().add(subContainer);
    GridLayoutManager manager = new GridLayoutManager("*;2*;500px", "*;600px");
    container.setLayoutManager(manager);

    GridLayoutManager subManager = new GridLayoutManager("7*;3*", "*;*");
    subContainer.setLayoutManager(subManager);
    subContainer.getComponents().add(new ComponentImpl());
    subContainer.getComponents().add(new ComponentImpl());
    subContainer.getComponents().add(new ComponentImpl());
    subContainer.getComponents().add(new ComponentImpl());

    LayoutContext layoutContext = new LayoutContext();

    EquationManager horizontal = layoutContext.getHorizontal();
    horizontal.setFixedLength(0, 800);
    horizontal.descend(0, 1);

    EquationManager vertial = layoutContext.getVertical();
    vertial.setFixedLength(0, 800);
    vertial.descend(0, 1);

    manager.layout(layoutContext, container);

    horizontal.ascend();
    vertial.ascend();

    double[] result = layoutContext.getHorizontal().solve();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{800, 100, 200, 500, 350, 150}, result, 0.000001);


    result = layoutContext.getVertical().solve();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{800, 200, 600, 300, 300}, result, 0.000001);
  }

  /**
   * <pre>
   * |               1000px              |
   * |     *     |     *     |     *     |
   * |           |   *   |   *   |   *   |
   * |   *   |   *   |   *   |           |
   * </pre>
   */
  public void testSpanOverlapsSpan() {
    Container container = new ContainerImpl();

    Container span1 = new ContainerImpl();
    GridConstraints constraint1 = GridConstraints.getConstraints(span1);
    constraint1.setColumnSpan(2);

    Container span2 = new ContainerImpl();
    GridConstraints constraint2 = GridConstraints.getConstraints(span2);
    constraint2.setColumnSpan(2);

    container.getComponents().add(new ComponentImpl());
    container.getComponents().add(span1);
    container.getComponents().add(span2);
    container.getComponents().add(new ComponentImpl());

    container.setLayoutManager(new GridLayoutManager("*;*;*", "*;*"));
    span1.setLayoutManager(new GridLayoutManager("*;*;*", "*"));
    span2.setLayoutManager(new GridLayoutManager("*;*;*", "*"));

    LayoutContext layoutContext = new LayoutContext();

    EquationManager horizontal = layoutContext.getHorizontal();
    horizontal.setFixedLength(0, 900);
    horizontal.descend(0, 1);

    EquationManager vertial = layoutContext.getVertical();
    vertial.setFixedLength(0, 200);
    vertial.descend(0, 1);

    LOG.info(((GridLayoutManager)container.getLayoutManager()).getGrid());
    container.getLayoutManager().layout(layoutContext, container);

    horizontal.ascend();
    vertial.ascend();

    double[] result = layoutContext.getHorizontal().solve();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{900, 300, 300, 300, 200, 200, 200, 200, 200, 200}, result, 0.000001);


    result = layoutContext.getVertical().solve();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{200, 100, 100, 100, 100}, result, 0.000001);
  }
}

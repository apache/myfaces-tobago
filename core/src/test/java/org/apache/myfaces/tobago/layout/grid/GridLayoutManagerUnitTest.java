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
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutComponentImpl;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutContainerImpl;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.math.EquationManager;

import java.util.Arrays;

public class GridLayoutManagerUnitTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(GridLayoutManagerUnitTest.class);

  public void testDummy() {
  }

  /**
   * <pre>
   * |                  800px               |
   * |    *    |    2*    |       500px     |
   * |         |          |    7*   |   3*  |
   *
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
    LayoutContainer container = new LayoutContainerImpl();
    LayoutComponent span = new LayoutComponentImpl();
    GridComponentConstraints bConstraint = GridComponentConstraints.getConstraints(span);
    bConstraint.setRowSpan(2);

    container.getComponents().add(new LayoutComponentImpl());
    container.getComponents().add(span);
    container.getComponents().add(new LayoutComponentImpl());
    container.getComponents().add(new LayoutComponentImpl());

    LayoutContainer subContainer = new LayoutContainerImpl();

    container.getComponents().add(subContainer);
    GridLayoutManager manager = new GridLayoutManager(container, "*;2*;500px", "*;600px");
    container.setLayoutManager(manager);

    GridLayoutManager subManager = new GridLayoutManager(subContainer, "7*;3*", "*;*");
    subContainer.setLayoutManager(subManager);
    subContainer.getComponents().add(new LayoutComponentImpl());
    subContainer.getComponents().add(new LayoutComponentImpl());
    subContainer.getComponents().add(new LayoutComponentImpl());
    subContainer.getComponents().add(new LayoutComponentImpl());

    LayoutContext layoutContext = new LayoutContext();

    EquationManager horizontal = layoutContext.getHorizontal();
    horizontal.setFixedLength(0, 800);
    horizontal.descend(0);

    EquationManager vertial = layoutContext.getVertical();
    vertial.setFixedLength(0, 800);
    vertial.descend(0);

    manager.layout(layoutContext);

    horizontal.ascend();
    vertial.ascend();

    double[] result = layoutContext.getHorizontal().solve();
    LOG.info("result: " + Arrays.toString(result));
    assertEquals(new double[]{800, 100, 200, 500, 350, 150}, result, 0.000001);


    result = layoutContext.getVertical().solve();
    LOG.info("result: " + Arrays.toString(result));
    assertEquals(new double[]{800, 200, 600, 300, 300}, result, 0.000001);
  }
  
  public static void assertEquals(double[] expected, double[] result, double delta) {
    assertEquals(expected.length, result.length);
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], result[i], delta);
    }
  }
}

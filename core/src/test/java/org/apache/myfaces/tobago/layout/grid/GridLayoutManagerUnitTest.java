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
import org.apache.myfaces.tobago.component.AbstractUIGridConstraints;
import org.apache.myfaces.tobago.layout.Component;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.MockComponent;
import org.apache.myfaces.tobago.layout.MockContainer;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.apache.myfaces.tobago.layout.math.AssertUtils;

import java.util.Arrays;

public class GridLayoutManagerUnitTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(GridLayoutManagerUnitTest.class);

  /**
   * <pre>
   * |        300px       |
   * |    *    |    2*    |
   * <p/>
   * +---------+----------+  -----
   * |         |          |   20px
   * +---------+----------+  -----
   * </pre>
   */
  public void testSimple() {
    MockContainer container = newContainer();
    MockComponent c1 = newComponent();
    container.getComponents().add(c1);
    MockComponent c2 = newComponent();
    container.getComponents().add(c2);

    GridLayoutManager manager = new GridLayoutManager("*;2*", "*");
    container.setLayoutManager(manager);

    ((AbstractUIGridConstraints) container.getConstraints()).setWidth(new PixelMeasure(300));
    ((AbstractUIGridConstraints) container.getConstraints()).setHeight(new PixelMeasure(20));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{300, 100, 200}, result, 0.000001);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{20, 20}, result, 0.000001);

    assertEquals("width of container", 300, ((AbstractUIGridConstraints) container.getConstraints()).getWidth().getPixel());
    assertEquals("width of component 1", 100, ((AbstractUIGridConstraints) c1.getConstraints()).getWidth().getPixel());
    assertEquals("width of component 2", 200, ((AbstractUIGridConstraints) c2.getConstraints()).getWidth().getPixel());
  }

  /**
   * <pre>
   * |            300px             |
   * |    *    |    *     |    *    |
   * <p/>
   * +---------+----------+---------+  -----
   * |         |                    |   20px
   * +---------+----------+---------+  -----
   * </pre>
   */
  public void testSpan() {
    MockContainer container = newContainer();
    MockComponent c = newComponent();
    container.getComponents().add(c);
    MockComponent span = newComponent();
    container.getComponents().add(span);
    ((AbstractUIGridConstraints) span.getConstraints()).setColumnSpan(2);

    GridLayoutManager manager = new GridLayoutManager("*;*;*", "*");
    container.setLayoutManager(manager);

    ((AbstractUIGridConstraints) container.getConstraints()).setWidth(new PixelMeasure(300));
    ((AbstractUIGridConstraints) container.getConstraints()).setHeight(new PixelMeasure(20));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{300, 100, 100, 100}, result, 0.000001);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{20, 20}, result, 0.000001);

    assertEquals("width of container", 300, ((AbstractUIGridConstraints) container.getConstraints()).getWidth().getPixel());
    assertEquals("width of component", 100, ((AbstractUIGridConstraints) c.getConstraints()).getWidth().getPixel());
    assertEquals("width of span", 200, ((AbstractUIGridConstraints) span.getConstraints()).getWidth().getPixel());
  }

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
  public void testSpanAndSubLayout() {
    MockContainer container = newContainer();
    Component span = newComponent();
    AbstractUIGridConstraints bConstraint = (AbstractUIGridConstraints) span.getConstraints();
    bConstraint.setColumnSpan(2);

    container.getComponents().add(newComponent());
    container.getComponents().add(span);
    container.getComponents().add(newComponent());
    container.getComponents().add(newComponent());

    MockContainer subContainer = newContainer();

    container.getComponents().add(subContainer);
    GridLayoutManager manager = new GridLayoutManager("*;2*;500px", "*;600px");
    container.setLayoutManager(manager);

    GridLayoutManager subManager = new GridLayoutManager("7*;3*", "*;*");
    subContainer.setLayoutManager(subManager);
    subContainer.getComponents().add(newComponent());
    subContainer.getComponents().add(newComponent());
    subContainer.getComponents().add(newComponent());
    subContainer.getComponents().add(newComponent());

    ((AbstractUIGridConstraints) container.getConstraints()).setWidth(new PixelMeasure(800));
    ((AbstractUIGridConstraints) container.getConstraints()).setHeight(new PixelMeasure(800));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{800, 100, 200, 500, 350, 150}, result, 0.000001);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{800, 200, 600, 300, 300}, result, 0.000001);

    assertEquals("width of container", 800, ((AbstractUIGridConstraints) container.getConstraints()).getWidth().getPixel());
    assertEquals("width of span", 700, ((AbstractUIGridConstraints) span.getConstraints()).getWidth().getPixel());
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
    MockContainer container = newContainer();

    MockContainer span1 = newContainer();
    AbstractUIGridConstraints constraint1 = (AbstractUIGridConstraints) span1.getConstraints();
    constraint1.setColumnSpan(2);

    MockContainer span2 = newContainer();
    AbstractUIGridConstraints constraint2 = (AbstractUIGridConstraints) span2.getConstraints();
    constraint2.setColumnSpan(2);

    container.getComponents().add(newComponent());
    container.getComponents().add(span1);
    container.getComponents().add(span2);
    container.getComponents().add(newComponent());

    container.setLayoutManager(new GridLayoutManager("*;*;*", "*;*"));
    span1.setLayoutManager(new GridLayoutManager("*;*;*", "*"));
    span2.setLayoutManager(new GridLayoutManager("*;*;*", "*"));

    ((AbstractUIGridConstraints) container.getConstraints()).setWidth(new PixelMeasure(900));
    ((AbstractUIGridConstraints) container.getConstraints()).setHeight(new PixelMeasure(200));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    LOG.info(((GridLayoutManager) container.getLayoutManager()).getGrid());

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{900, 300, 300, 300, 200, 200, 200, 200, 200, 200}, result, 0.000001);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{200, 100, 100, 100, 100}, result, 0.000001);

    assertEquals("width of container", 900, ((AbstractUIGridConstraints) container.getConstraints()).getWidth().getPixel());
    assertEquals("width of span 1", 600, ((AbstractUIGridConstraints) span1.getConstraints()).getWidth().getPixel());
    assertEquals("width of span 2", 600, ((AbstractUIGridConstraints) span2.getConstraints()).getWidth().getPixel());
  }

  private MockContainer newContainer() {
    return new MockContainer(new AbstractUIGridConstraints(){});
  }

  private MockComponent newComponent() {
    return new MockComponent(new AbstractUIGridConstraints(){});
  }
}

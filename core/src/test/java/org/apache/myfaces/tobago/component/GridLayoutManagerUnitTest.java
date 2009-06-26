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

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.MockComponent;
import org.apache.myfaces.tobago.layout.MockContainer;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.apache.myfaces.tobago.layout.math.AssertUtils;
import org.apache.myfaces.tobago.layout.math.SystemOfEquations;

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
    MockContainer container = new MockContainer();
    MockComponent c1 = new MockComponent();
    container.getComponents().add(c1);
    MockComponent c2 = new MockComponent();
    container.getComponents().add(c2);

    UIGridLayout manager = new UIGridLayout();
    manager.setColumns("*;2*");
    manager.setRows("*");
    manager.onComponentCreated(null, manager);
    container.setLayoutManager(manager);

    container.setWidth(new PixelMeasure(300));
    container.setHeight(new PixelMeasure(20));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{300, 100, 200, 100, 200}, result, 0.000001);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{20, 20, 20, 20}, result, 0.000001);

    assertEquals("width of container", 300, container.getWidth().getPixel());
    assertEquals("width of component 1", 100, c1.getWidth().getPixel());
    assertEquals("width of component 2", 200, c2.getWidth().getPixel());
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
    MockContainer container = new MockContainer();
    MockComponent c = new MockComponent();
    container.getComponents().add(c);
    MockComponent span = new MockComponent();
    container.getComponents().add(span);
    span.setColumnSpan(2);

    UIGridLayout manager = new UIGridLayout();
    manager.setColumns("*;*;*");
    manager.setRows("*");
    manager.onComponentCreated(null, manager);
    container.setLayoutManager(manager);

    container.setWidth(new PixelMeasure(300));
    container.setHeight(new PixelMeasure(20));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{300, 100, 100, 100, 100, 200}, result, 0.000001);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{20, 20, 20, 20}, result, 0.000001);

    assertEquals("width of container", 300, container.getWidth().getPixel());
    assertEquals("width of component", 100, c.getWidth().getPixel());
    assertEquals("width of span", 200, span.getWidth().getPixel());
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
    MockContainer container = new MockContainer();
    MockComponent span = new MockComponent();
    span.setColumnSpan(2);

    container.getComponents().add(new MockComponent());
    container.getComponents().add(span);
    container.getComponents().add(new MockComponent());
    container.getComponents().add(new MockComponent());

    MockContainer subContainer = new MockContainer();

    container.getComponents().add(subContainer);
    UIGridLayout manager = new UIGridLayout();
    manager.setColumns("*;2*;500px");
    manager.setRows("*;600px");
    manager.onComponentCreated(null, manager);
    container.setLayoutManager(manager);

    UIGridLayout subManager = new UIGridLayout();
    subManager.setColumns("7*;3*");
    subManager.setRows("*;*");
    subManager.onComponentCreated(null, manager);
    subContainer.setLayoutManager(subManager);
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());

    container.setWidth(new PixelMeasure(800));
    container.setHeight(new PixelMeasure(800));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{
        800, 100, 200, 500, 100, 700, 100, 200, 500, 350, 150, 350, 150, 350, 150
    }, result, 0.000001);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{
        800, 200, 600, 200, 200, 600, 600, 600, 300, 300, 300, 300, 300, 300
    }, result, 0.000001);

    assertEquals("width of container", 800, container.getWidth().getPixel());
    assertEquals("width of span", 700, span.getWidth().getPixel());
  }

  /**
   * <pre>
   * |               900px              |
   * |     *     |     *     |     *     |
   * |           |   *   |   *   |   *   |
   * |   *   |   *   |   *   |           |
   * </pre>
   */
  public void testSpanOverlapsSpan() {
    MockContainer container = new MockContainer();

    MockContainer span1 = new MockContainer();
    span1.setColumnSpan(2);

    MockContainer span2 = new MockContainer();
    span2.setColumnSpan(2);

    container.getComponents().add(new MockComponent());
    container.getComponents().add(span1);
    container.getComponents().add(span2);
    container.getComponents().add(new MockComponent());

    UIGridLayout manager1 = new UIGridLayout();
    manager1.setColumns("*;*;*");
    manager1.setRows("*;*");
    manager1.onComponentCreated(null, manager1);
    container.setLayoutManager(manager1);

    UIGridLayout manager2 = new UIGridLayout();
    manager2.setColumns("*;*;*");
    manager2.setRows("*");
    manager2.onComponentCreated(null, manager2);
    span1.setLayoutManager(manager2);

    UIGridLayout manager3 = new UIGridLayout();
    manager3.setColumns("*;*;*");
    manager3.setRows("*");
    manager3.onComponentCreated(null, manager3);
    span2.setLayoutManager(manager3);

    container.setWidth(new PixelMeasure(900));
    container.setHeight(new PixelMeasure(200));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    LOG.info(((UIGridLayout) container.getLayoutManager()).getGrid());

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{
        900, 300, 300, 300, 300, 600, 200, 200, 200, 600, 200, 200, 200, 300
    }, result, 0.000001);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{200, 100, 100, 100, 100, 100, 100, 100, 100}, result, 0.000001);

    assertEquals("width of container", 900, container.getWidth().getPixel());
    assertEquals("width of span 1", 600, span1.getWidth().getPixel());
    assertEquals("width of span 2", 600, span2.getWidth().getPixel());
  }

  /**
   * <pre>
   * |        800px       |
   * |    *    |    *     |
   * |         |          |
   * <p/>
   * +---------+----------+ ----- -----
   * |         |          |
   * |         |          |       *
   * |         |          |
   * +         +----------+ 800px -----
   * |         |          |
   * |         |          |       *
   * |         |          |
   * +---------+----------+ ----- -----
   * </pre>
   */
  public void testRowSpan() {
    MockContainer container = new MockContainer();
    MockComponent span = new MockComponent();
    span.setRowSpan(2);

    container.getComponents().add(span);
    container.getComponents().add(new MockComponent());
    container.getComponents().add(new MockComponent());

    UIGridLayout manager = new UIGridLayout();
    manager.setColumns("*;*");
    manager.setRows("*;*");
    manager.onComponentCreated(null, manager);
    container.setLayoutManager(manager);

    container.setWidth(new PixelMeasure(800));
    container.setHeight(new PixelMeasure(800));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{800.0, 400.0, 400.0, 400.0, 400.0, 400.0}, result, SystemOfEquations.EPSILON);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{800.0, 400.0, 400.0, 800.0, 400.0, 400.0}, result, SystemOfEquations.EPSILON);

    assertEquals("width of container", 800, container.getWidth().getPixel());
    assertEquals("height of container", 800, container.getHeight().getPixel());
    assertEquals("width of span", 400, span.getWidth().getPixel());
    assertEquals("height of span", 800, span.getHeight().getPixel());
  }

}

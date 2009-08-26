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
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MockComponent;
import org.apache.myfaces.tobago.layout.MockContainer;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class GridLayoutManagerUnitTest {

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
  @Test
  public void testSimple() {
    MockContainer container = new MockContainer();
    MockComponent c1 = new MockComponent();
    container.getComponents().add(c1);
    MockComponent c2 = new MockComponent();
    container.getComponents().add(c2);

    UIGridLayout manager = new UIGridLayout();
    manager.setColumns("*;2*");
    manager.setRows("*");
    manager.setColumnSpacing(PixelMeasure.ZERO);
    manager.setRowSpacing(PixelMeasure.ZERO);
    manager.setParent(container);
    manager.onComponentCreated(null, manager);
    container.setLayoutManager(manager);

    container.setWidth(new PixelMeasure(300));
    container.setHeight(new PixelMeasure(20));
    container.setLeftOffset(PixelMeasure.ZERO);
    container.setTopOffset(PixelMeasure.ZERO);
    container.setRightOffset(PixelMeasure.ZERO);
    container.setBottomOffset(PixelMeasure.ZERO);
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(300), new PixelMeasure(100), new PixelMeasure(200), PixelMeasure.ZERO,
        new PixelMeasure(100), new PixelMeasure(200)}, result);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(20), new PixelMeasure(20), PixelMeasure.ZERO, new PixelMeasure(20), new PixelMeasure(20)},
        result);

    Assert.assertEquals("width of container", 300, container.getWidth().getPixel());
    Assert.assertEquals("width of component 1", 100, c1.getWidth().getPixel());
    Assert.assertEquals("width of component 2", 200, c2.getWidth().getPixel());
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
  @Test
  public void testSpan() {
    MockContainer container = new MockContainer();
    MockComponent c = new MockComponent();
    container.getComponents().add(c);
    MockComponent span = new MockComponent();
    container.getComponents().add(span);
    container.setLeftOffset(PixelMeasure.ZERO);
    container.setTopOffset(PixelMeasure.ZERO);
    container.setRightOffset(PixelMeasure.ZERO);
    container.setBottomOffset(PixelMeasure.ZERO);
    span.setColumnSpan(2);

    UIGridLayout manager = new UIGridLayout();
    manager.setColumns("*;*;*");
    manager.setRows("*");
    manager.setColumnSpacing(PixelMeasure.ZERO);
    manager.setRowSpacing(PixelMeasure.ZERO);
    manager.setParent(container);
    manager.onComponentCreated(null, manager);
    container.setLayoutManager(manager);

    container.setWidth(new PixelMeasure(300));
    container.setHeight(new PixelMeasure(20));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(300), new PixelMeasure(100), new PixelMeasure(100), new PixelMeasure(100), PixelMeasure.ZERO,
        new PixelMeasure(100), new PixelMeasure(200)}, result);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(20), new PixelMeasure(20), PixelMeasure.ZERO, new PixelMeasure(20), new PixelMeasure(20)},
        result);

    Assert.assertEquals("width of container", 300, container.getWidth().getPixel());
    Assert.assertEquals("width of component", 100, c.getWidth().getPixel());
    Assert.assertEquals("width of span", 200, span.getWidth().getPixel());
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
// XXX @Test
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
    manager.setColumnSpacing(PixelMeasure.ZERO);
    manager.setRowSpacing(PixelMeasure.ZERO);
    manager.setParent(container);
    manager.onComponentCreated(null, manager);
    container.setLayoutManager(manager);

    UIGridLayout subManager = new UIGridLayout();
    subManager.setColumns("7*;3*");
    subManager.setRows("*;*");
    subManager.setColumnSpacing(PixelMeasure.ZERO);
    subManager.setRowSpacing(PixelMeasure.ZERO);
    subManager.setParent(subContainer);
    subManager.onComponentCreated(null, manager);
    subContainer.setLayoutManager(subManager);
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());
    subContainer.setLeftOffset(PixelMeasure.ZERO);
    subContainer.setTopOffset(PixelMeasure.ZERO);
    subContainer.setRightOffset(PixelMeasure.ZERO);
    subContainer.setBottomOffset(PixelMeasure.ZERO);

    container.setWidth(new PixelMeasure(800));
    container.setHeight(new PixelMeasure(800));
    container.setLeftOffset(PixelMeasure.ZERO);
    container.setTopOffset(PixelMeasure.ZERO);
    container.setRightOffset(PixelMeasure.ZERO);
    container.setBottomOffset(PixelMeasure.ZERO);
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(800), new PixelMeasure(100), new PixelMeasure(200), new PixelMeasure(500), PixelMeasure.ZERO,
        new PixelMeasure(100), new PixelMeasure(700), new PixelMeasure(100), new PixelMeasure(200),
        new PixelMeasure(500), new PixelMeasure(350), new PixelMeasure(150), PixelMeasure.ZERO, new PixelMeasure(350),
        new PixelMeasure(150), new PixelMeasure(350), new PixelMeasure(150)
    }, result);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(800), new PixelMeasure(200), new PixelMeasure(600), PixelMeasure.ZERO,
        new PixelMeasure(200), new PixelMeasure(200),
        new PixelMeasure(600), new PixelMeasure(600), new PixelMeasure(600),
        new PixelMeasure(300), new PixelMeasure(300), PixelMeasure.ZERO,
        new PixelMeasure(300), new PixelMeasure(300),
        new PixelMeasure(300), new PixelMeasure(300)
    }, result);

    Assert.assertEquals("width of container", 800, container.getWidth().getPixel());
    Assert.assertEquals("width of span", 700, span.getWidth().getPixel());
  }

  /**
   * <pre>
   * |               900px              |
   * |     *     |     *     |     *     |
   * |           |   *   |   *   |   *   |
   * |   *   |   *   |   *   |           |
   * </pre>
   */
  @Test
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
    manager1.setColumnSpacing(PixelMeasure.ZERO);
    manager1.setRowSpacing(PixelMeasure.ZERO);
    manager1.setParent(container);
    manager1.onComponentCreated(null, manager1);
    container.setLayoutManager(manager1);

    UIGridLayout manager2 = new UIGridLayout();
    manager2.setColumns("*;*;*");
    manager2.setRows("*");
    manager2.setColumnSpacing(PixelMeasure.ZERO);
    manager2.setRowSpacing(PixelMeasure.ZERO);
    manager2.setParent(span1);
    manager2.onComponentCreated(null, manager2);
    span1.setLayoutManager(manager2);

    UIGridLayout manager3 = new UIGridLayout();
    manager3.setColumns("*;*;*");
    manager3.setRows("*");
    manager3.setColumnSpacing(PixelMeasure.ZERO);
    manager3.setRowSpacing(PixelMeasure.ZERO);
    manager3.setParent(span2);
    manager3.onComponentCreated(null, manager3);
    span2.setLayoutManager(manager3);

    container.setWidth(new PixelMeasure(900));
    container.setHeight(new PixelMeasure(200));
    container.setLeftOffset(PixelMeasure.ZERO);
    container.setTopOffset(PixelMeasure.ZERO);
    container.setRightOffset(PixelMeasure.ZERO);
    container.setBottomOffset(PixelMeasure.ZERO);
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    LOG.info(((UIGridLayout) container.getLayoutManager()).getGrid());

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(900), new PixelMeasure(300), new PixelMeasure(300), new PixelMeasure(300), PixelMeasure.ZERO,
        new PixelMeasure(300), new PixelMeasure(600), new PixelMeasure(200), new PixelMeasure(200),
        new PixelMeasure(200), PixelMeasure.ZERO, new PixelMeasure(600), new PixelMeasure(200), new PixelMeasure(200),
        new PixelMeasure(200), PixelMeasure.ZERO, new PixelMeasure(300)
    }, result);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(200), new PixelMeasure(100), new PixelMeasure(100), PixelMeasure.ZERO,
        new PixelMeasure(100), new PixelMeasure(100), new PixelMeasure(100),PixelMeasure.ZERO,
        new PixelMeasure(100), new PixelMeasure(100), PixelMeasure.ZERO,new PixelMeasure(100)},
        result);

    Assert.assertEquals("width of container", 900, container.getWidth().getPixel());
    Assert.assertEquals("width of span 1", 600, span1.getWidth().getPixel());
    Assert.assertEquals("width of span 2", 600, span2.getWidth().getPixel());
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
  @Test
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
    manager.setColumnSpacing(PixelMeasure.ZERO);
    manager.setRowSpacing(PixelMeasure.ZERO);
    manager.setParent(container);
    manager.onComponentCreated(null, manager);
    container.setLayoutManager(manager);

    container.setWidth(new PixelMeasure(800));
    container.setHeight(new PixelMeasure(800));
    container.setLeftOffset(PixelMeasure.ZERO);
    container.setTopOffset(PixelMeasure.ZERO);
    container.setRightOffset(PixelMeasure.ZERO);
    container.setBottomOffset(PixelMeasure.ZERO);
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(800), new PixelMeasure(400), new PixelMeasure(400), PixelMeasure.ZERO,
        new PixelMeasure(400), new PixelMeasure(400), new PixelMeasure(400)}, result);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        new PixelMeasure(800), new PixelMeasure(400), new PixelMeasure(400), PixelMeasure.ZERO,
        new PixelMeasure(800), new PixelMeasure(400), new PixelMeasure(400)}, result);

    Assert.assertEquals("width of container", 800, container.getWidth().getPixel());
    Assert.assertEquals("height of container", 800, container.getHeight().getPixel());
    Assert.assertEquals("width of span", 400, span.getWidth().getPixel());
    Assert.assertEquals("height of span", 800, span.getHeight().getPixel());
  }

}

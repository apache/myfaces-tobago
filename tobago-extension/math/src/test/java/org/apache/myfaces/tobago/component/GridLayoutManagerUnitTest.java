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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.layout.EquationLayoutContext;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MockComponent;
import org.apache.myfaces.tobago.layout.MockContainer;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class GridLayoutManagerUnitTest {

  private static final Logger LOG = LoggerFactory.getLogger(GridLayoutManagerUnitTest.class);

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

    UIEquationGridLayout manager = new UIEquationGridLayout();
    manager.setColumns("*;2*");
    manager.setRows("*");
    manager.setColumnSpacing(Measure.ZERO);
    manager.setRowSpacing(Measure.ZERO);
    manager.setParent(container);
    container.setLayoutManager(manager);

    container.setWidth(px(300));
    container.setHeight(px(20));
    container.setLeftOffset(Measure.ZERO);
    container.setTopOffset(Measure.ZERO);
    container.setRightOffset(Measure.ZERO);
    container.setBottomOffset(Measure.ZERO);
    EquationLayoutContext layoutContext = new EquationLayoutContext(container);
    layoutContext.layout();

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(300), px(100), px(200), Measure.ZERO,
        px(100), px(200)}, result);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(20), px(20), Measure.ZERO, px(20), px(20)},
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
    container.setLeftOffset(Measure.ZERO);
    container.setTopOffset(Measure.ZERO);
    container.setRightOffset(Measure.ZERO);
    container.setBottomOffset(Measure.ZERO);
    span.setColumnSpan(2);

    UIEquationGridLayout manager = new UIEquationGridLayout();
    manager.setColumns("*;*;*");
    manager.setRows("*");
    manager.setColumnSpacing(Measure.ZERO);
    manager.setRowSpacing(Measure.ZERO);
    manager.setParent(container);
    container.setLayoutManager(manager);

    container.setWidth(px(300));
    container.setHeight(px(20));
    EquationLayoutContext layoutContext = new EquationLayoutContext(container);
    layoutContext.layout();

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(300), px(100), px(100), px(100), Measure.ZERO,
        px(100), px(200)}, result);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(20), px(20), Measure.ZERO, px(20), px(20)},
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
    UIEquationGridLayout manager = new UIEquationGridLayout();
    manager.setColumns("*;2*;500px");
    manager.setRows("*;600px");
    manager.setColumnSpacing(Measure.ZERO);
    manager.setRowSpacing(Measure.ZERO);
    manager.setParent(container);
    container.setLayoutManager(manager);

    UIEquationGridLayout subManager = new UIEquationGridLayout();
    subManager.setColumns("7*;3*");
    subManager.setRows("*;*");
    subManager.setColumnSpacing(Measure.ZERO);
    subManager.setRowSpacing(Measure.ZERO);
    subManager.setParent(subContainer);
    subContainer.setLayoutManager(subManager);
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());
    subContainer.getComponents().add(new MockComponent());
    subContainer.setLeftOffset(Measure.ZERO);
    subContainer.setTopOffset(Measure.ZERO);
    subContainer.setRightOffset(Measure.ZERO);
    subContainer.setBottomOffset(Measure.ZERO);

    container.setWidth(px(800));
    container.setHeight(px(800));
    container.setLeftOffset(Measure.ZERO);
    container.setTopOffset(Measure.ZERO);
    container.setRightOffset(Measure.ZERO);
    container.setBottomOffset(Measure.ZERO);
    EquationLayoutContext layoutContext = new EquationLayoutContext(container);
    layoutContext.layout();

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(800), px(100), px(200), px(500), Measure.ZERO,
        px(100), px(700), px(100), px(200),
        px(500), px(350), px(150), Measure.ZERO, px(350),
        px(150), px(350), px(150)
    }, result);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(800), px(200), px(600), Measure.ZERO,
        px(200), px(200),
        px(600), px(600), px(600),
        px(300), px(300), Measure.ZERO,
        px(300), px(300),
        px(300), px(300)
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

    UIEquationGridLayout manager1 = new UIEquationGridLayout();
    manager1.setColumns("*;*;*");
    manager1.setRows("*;*");
    manager1.setColumnSpacing(Measure.ZERO);
    manager1.setRowSpacing(Measure.ZERO);
    manager1.setParent(container);
    container.setLayoutManager(manager1);

    UIEquationGridLayout manager2 = new UIEquationGridLayout();
    manager2.setColumns("*;*;*");
    manager2.setRows("*");
    manager2.setColumnSpacing(Measure.ZERO);
    manager2.setRowSpacing(Measure.ZERO);
    manager2.setParent(span1);
    span1.setLayoutManager(manager2);

    UIEquationGridLayout manager3 = new UIEquationGridLayout();
    manager3.setColumns("*;*;*");
    manager3.setRows("*");
    manager3.setColumnSpacing(Measure.ZERO);
    manager3.setRowSpacing(Measure.ZERO);
    manager3.setParent(span2);
    span2.setLayoutManager(manager3);

    container.setWidth(px(900));
    container.setHeight(px(200));
    container.setLeftOffset(Measure.ZERO);
    container.setTopOffset(Measure.ZERO);
    container.setRightOffset(Measure.ZERO);
    container.setBottomOffset(Measure.ZERO);
    EquationLayoutContext layoutContext = new EquationLayoutContext(container);
    layoutContext.layout();

    LOG.info(((UIEquationGridLayout) container.getLayoutManager()).getGrid());

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(900), px(300), px(300), px(300), Measure.ZERO,
        px(300), px(600), px(200), px(200),
        px(200), Measure.ZERO, px(600), px(200), px(200),
        px(200), Measure.ZERO, px(300)
    }, result);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(200), px(100), px(100), Measure.ZERO,
        px(100), px(100), px(100), Measure.ZERO,
        px(100), px(100), Measure.ZERO, px(100)},
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

    UIEquationGridLayout manager = new UIEquationGridLayout();
    manager.setColumns("*;*");
    manager.setRows("*;*");
    manager.setColumnSpacing(Measure.ZERO);
    manager.setRowSpacing(Measure.ZERO);
    manager.setParent(container);
    container.setLayoutManager(manager);

    container.setWidth(px(800));
    container.setHeight(px(800));
    container.setLeftOffset(Measure.ZERO);
    container.setTopOffset(Measure.ZERO);
    container.setRightOffset(Measure.ZERO);
    container.setBottomOffset(Measure.ZERO);
    EquationLayoutContext layoutContext = new EquationLayoutContext(container);
    layoutContext.layout();

    Measure[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(800), px(400), px(400), Measure.ZERO,
        px(400), px(400), px(400)}, result);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    Assert.assertArrayEquals(new Measure[]{
        px(800), px(400), px(400), Measure.ZERO,
        px(800), px(400), px(400)}, result);

    Assert.assertEquals("width of container", 800, container.getWidth().getPixel());
    Assert.assertEquals("height of container", 800, container.getHeight().getPixel());
    Assert.assertEquals("width of span", 400, span.getWidth().getPixel());
    Assert.assertEquals("height of span", 800, span.getHeight().getPixel());
  }

  private Measure px(int pixel) {
    return Measure.valueOf(pixel);
  }
  
}

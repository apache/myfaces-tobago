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
    Container container = new ContainerImpl();
    ComponentImpl c1 = new ComponentImpl();
    container.getComponents().add(c1);
    ComponentImpl c2 = new ComponentImpl();
    container.getComponents().add(c2);

    GridLayoutManager manager = new GridLayoutManager("*;2*", "*");
    container.setLayoutManager(manager);

    ((GridConstraints) container.getConstraints()).setWidth(new PixelMeasure(300));
    ((GridConstraints) container.getConstraints()).setHeight(new PixelMeasure(20));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{300, 100, 200}, result, 0.000001);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{20, 20}, result, 0.000001);

    assertEquals("width of container", 300, ((GridConstraints) container.getConstraints()).getWidth().getPixel());
    assertEquals("width of component 1", 100, ((GridConstraints) c1.getConstraints()).getWidth().getPixel());
    assertEquals("width of component 2", 200, ((GridConstraints) c2.getConstraints()).getWidth().getPixel());
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
    Container container = new ContainerImpl();
    ComponentImpl c = new ComponentImpl();
    container.getComponents().add(c);
    ComponentImpl span = new ComponentImpl();
    container.getComponents().add(span);
    ((GridConstraints) span.getConstraints()).setColumnSpan(2);

    GridLayoutManager manager = new GridLayoutManager("*;*;*", "*");
    container.setLayoutManager(manager);

    ((GridConstraints) container.getConstraints()).setWidth(new PixelMeasure(300));
    ((GridConstraints) container.getConstraints()).setHeight(new PixelMeasure(20));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{300, 100, 100, 100}, result, 0.000001);

    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{20, 20}, result, 0.000001);

    assertEquals("width of container", 300, ((GridConstraints) container.getConstraints()).getWidth().getPixel());
    assertEquals("width of component", 100, ((GridConstraints) c.getConstraints()).getWidth().getPixel());
    assertEquals("width of span", 200, ((GridConstraints) span.getConstraints()).getWidth().getPixel());
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
    Container container = new ContainerImpl();
    Component span = new ComponentImpl();
    GridConstraints bConstraint = GridConstraints.getConstraints(span);
    bConstraint.setColumnSpan(2);

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

    ((GridConstraints) container.getConstraints()).setWidth(new PixelMeasure(800));
    ((GridConstraints) container.getConstraints()).setHeight(new PixelMeasure(800));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{800, 100, 200, 500, 350, 150}, result, 0.000001);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{800, 200, 600, 300, 300}, result, 0.000001);

    assertEquals("width of container", 800, ((GridConstraints) container.getConstraints()).getWidth().getPixel());
    assertEquals("width of span", 700, ((GridConstraints) span.getConstraints()).getWidth().getPixel());
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

    ((GridConstraints) container.getConstraints()).setWidth(new PixelMeasure(900));
    ((GridConstraints) container.getConstraints()).setHeight(new PixelMeasure(200));
    LayoutContext layoutContext = new LayoutContext(container);
    layoutContext.layout();

    LOG.info(((GridLayoutManager) container.getLayoutManager()).getGrid());

    double[] result = layoutContext.getHorizontal().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{900, 300, 300, 300, 200, 200, 200, 200, 200, 200}, result, 0.000001);


    result = layoutContext.getVertical().getResult();
    LOG.info("result: " + Arrays.toString(result));
    AssertUtils.assertEquals(new double[]{200, 100, 100, 100, 100}, result, 0.000001);

    assertEquals("width of container", 900, ((GridConstraints) container.getConstraints()).getWidth().getPixel());
    assertEquals("width of span 1", 600, ((GridConstraints) span1.getConstraints()).getWidth().getPixel());
    assertEquals("width of span 2", 600, ((GridConstraints) span2.getConstraints()).getWidth().getPixel());
  }
}

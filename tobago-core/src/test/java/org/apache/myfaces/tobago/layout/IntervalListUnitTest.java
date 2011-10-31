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

import org.apache.myfaces.tobago.internal.layout.Interval;
import org.apache.myfaces.tobago.internal.layout.IntervalList;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class IntervalListUnitTest extends ArrayList<Interval> {

  /**
   * nothing defined
   */
  @Test
  public void test0() {
    IntervalList list = new IntervalList();
    list.evaluate();
    Assert.assertEquals(Measure.ZERO, list.getCurrent());
    Assert.assertEquals(Measure.ZERO, list.getMinimum());
  }

  /**
   * f=100
   */
  @Test
  public void test1Fixed() {
    IntervalList list = new IntervalList();
    list.add(new Interval(null, null, null, px(100)));
    list.evaluate();
    Assert.assertEquals(px(100), list.getCurrent());
    Assert.assertEquals(px(100), list.getMinimum());
  }

  /**
   * f=100
   * f=200
   * f=300
   */
  @Test
  public void test3Fixed() {
    IntervalList list = new IntervalList();
    list.add(new Interval(null, null, null, px(100)));
    list.add(new Interval(null, null, null, px(200)));
    list.add(new Interval(null, null, null, px(300)));
    list.evaluate();
    Assert.assertEquals(px(300), list.getCurrent());
    Assert.assertEquals(px(300), list.getMinimum());
  }

  /**
   * 10/100/1000
   */
  @Test
  public void test1MinPrefMax() {
    IntervalList list = new IntervalList();
    list.add(new Interval(px(10), px(100), px(1000), null));
    list.evaluate();
    Assert.assertEquals(px(100), list.getCurrent());
    Assert.assertEquals(px(10), list.getMinimum());
  }

  /**
   * 10/100/1000
   * 20/200/2000
   * 30/300/3000
   */
  @Test
  public void test3MinPrefMax() {
    IntervalList list = new IntervalList();
    list.add(new Interval(px(10), px(100), px(1000), null));
    list.add(new Interval(px(20), px(200), px(2000), null));
    list.add(new Interval(px(30), px(300), px(3000), null));
    list.evaluate();
    Assert.assertEquals(px(300), list.getCurrent());
    Assert.assertEquals(px(30), list.getMinimum());
  }

  /**
   * 10/100/1000
   * 1000/10000/100000
   */
  @Test
  public void test2MinPrefMax() {
    IntervalList list = new IntervalList();
    list.add(new Interval(px(10), px(100), px(1000), null));
    list.add(new Interval(px(1000), px(10000), px(100000), null));
    list.evaluate();
    Assert.assertEquals(px(1000), list.getCurrent());
    Assert.assertEquals(px(1000), list.getMinimum());
  }

  /**
   * 10/100/1000
   * 2000/20000/200000
   */
  @Test
  public void test4MinPrefMax() {
    IntervalList list = new IntervalList();
    list.add(new Interval(px(10), px(100), px(1000), null));
    list.add(new Interval(px(2000), px(20000), px(200000), null));
    list.evaluate();
    Assert.assertEquals(px(2000), list.getCurrent());
    Assert.assertEquals(px(2000), list.getMinimum());
  }

  /**
   * 10/./.
   * ./200/.
   * ././3000
   */
  @Test
  public void test3MinPrefMaxWithGaps() {
    IntervalList list = new IntervalList();
    list.add(new Interval(px(10), null, null, null));
    list.add(new Interval(null, px(200), null, null));
    list.add(new Interval(null, null, px(3000), null));
    list.evaluate();
    Assert.assertEquals(px(200), list.getCurrent());
    Assert.assertEquals(px(10), list.getMinimum());
  }

  /**
   * f=100
   * f=200
   * f=300
   * 10/111/1000
   * 20/222/2000
   * 30/333/3000
   */
  @Test
  public void test6Mixed() {
    IntervalList list = new IntervalList();
    list.add(new Interval(null, null, null, px(100)));
    list.add(new Interval(null, null, null, px(200)));
    list.add(new Interval(null, null, null, px(300)));
    list.add(new Interval(px(10), px(111), px(1000), null));
    list.add(new Interval(px(20), px(222), px(2000), null));
    list.add(new Interval(px(30), px(333), px(3000), null));
    list.evaluate();
    Assert.assertEquals(px(333), list.getCurrent());
    Assert.assertEquals(px(300), list.getMinimum());
  }

  /**
   * f=100
   * f=200
   * f=300
   * 10/111/210
   * 120/222/220
   * 130/333/230
   */
  @Test
  public void test6Squeeze() {
    IntervalList list = new IntervalList();
    list.add(new Interval(null, null, null, px(100)));
    list.add(new Interval(null, null, null, px(200)));
    list.add(new Interval(null, null, null, px(300)));
    list.add(new Interval(px(10), px(111), px(210), null));
    list.add(new Interval(px(120), px(222), px(220), null));
    list.add(new Interval(px(130), px(333), px(230), null));
    list.evaluate();
    Assert.assertEquals(px(300), list.getCurrent());
    Assert.assertEquals(px(300), list.getMinimum());
  }

  /**
   * f=100
   * f=300
   * 10/111/210
   * 120/222/220
   * 130/333/230
   */
  @Test
  public void test5Squeeze() {
    IntervalList list = new IntervalList();
    list.add(new Interval(null, null, null, px(100)));
    list.add(new Interval(null, null, null, px(300)));
    list.add(new Interval(px(10), px(111), px(210), null));
    list.add(new Interval(px(120), px(222), px(220), null));
    list.add(new Interval(px(130), px(333), px(230), null));
    list.evaluate();
    Assert.assertEquals(px(300), list.getCurrent());
    Assert.assertEquals(px(300), list.getMinimum());
  }

  private Measure px(int pixel) {
    return Measure.valueOf(pixel);
  }
}

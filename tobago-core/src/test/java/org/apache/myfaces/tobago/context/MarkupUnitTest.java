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

package org.apache.myfaces.tobago.context;

import org.apache.commons.collections.IteratorUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class MarkupUnitTest {
  
  private static final String[] AB = new String[] {"a", "b"};

  @Test
  public void testString() {
    Assert.assertNull(Markup.valueOf((String) null));
    
    Assert.assertArrayEquals(new String[] {"accent"}, IteratorUtils.toArray(Markup.valueOf("accent").iterator()));
    
    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf("a,b").iterator()));
    
    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf("a, b").iterator()));
    
    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf(", \ta , ,\n b ,").iterator()));
  }
  
  @Test
  public void testStringArray() {
    Assert.assertNull(Markup.valueOf((String[]) null));
    
    Assert.assertNull(Markup.valueOf(new String[] {}));
    
    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf(new String[] {"a", "b"}).iterator()));
    
    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf(new String[] {" a ", " b "}).iterator()));
  }
  
  @Test
  public void testObject() {
    Assert.assertNull(Markup.valueOf((Object) null));

    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf((Object) ", \ta , ,\n b ,").iterator()));

    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf((Object) new String[] {"a", "b"}).iterator()));
    
    Assert.assertArrayEquals(AB,
        IteratorUtils.toArray(Markup.valueOf((Object) new String[] {" a ", " b "}).iterator()));
    
    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf(new StringBuilder("a, b")).iterator()));
    
    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf(AB).iterator()));
    
    Assert.assertArrayEquals(AB, IteratorUtils.toArray(Markup.valueOf(Arrays.asList(AB)).iterator()));
  }
  
  @Test
  public void testMarkup() {
    Assert.assertNull(Markup.valueOf((Markup) null));
    
    final Markup accent = Markup.valueOf("accent");
    Assert.assertSame(accent, Markup.valueOf(accent));
  }

  @Test
  public void testAdd() {
    final Markup a = Markup.valueOf("a");
    final Markup b = Markup.valueOf("b");
    final Markup c = Markup.valueOf("c");
    final Markup ab = Markup.valueOf("a,b");
    final Markup abc = Markup.valueOf("a,b,c");
    Assert.assertEquals(a, Markup.NULL.add(a));
    Assert.assertEquals(ab, a.add(b));
    Assert.assertEquals(abc, ab.add(c));
    Assert.assertEquals(abc, ab.add(abc));
    Assert.assertSame(a, a.add(a));
    Assert.assertSame(ab, ab.add(a));
    Assert.assertSame(ab, ab.add(ab));
  }

  @Test
  public void testRemove() {
    final Markup a = Markup.valueOf("a");
    final Markup b = Markup.valueOf("b");
    final Markup c = Markup.valueOf("c");
    final Markup ab = Markup.valueOf("a,b");
    final Markup bc = Markup.valueOf("b,c");
    final Markup abc = Markup.valueOf("a,b,c");
    Assert.assertEquals(Markup.NULL, Markup.NULL.remove(a));
    Assert.assertEquals(a, a.remove(b));
    Assert.assertEquals(Markup.NULL, a.remove(a));
    Assert.assertEquals(b, ab.remove(a));
    Assert.assertEquals(a, ab.remove(b));
    Assert.assertEquals(c, abc.remove(ab));
    Assert.assertEquals(a, ab.remove(bc));
    Assert.assertEquals(Markup.NULL, abc.remove(abc));
    Assert.assertSame(b, b.remove(a));
    Assert.assertSame(ab, ab.remove(c));
  }

  @Test
  public void testContains() {
    final Markup a = Markup.valueOf("a");
    final Markup ab = Markup.valueOf("a,b");
    Assert.assertFalse(Markup.NULL.contains("a"));
    Assert.assertTrue(a.contains("a"));
    Assert.assertFalse(a.contains("b"));
    Assert.assertTrue(ab.contains("a"));
    Assert.assertTrue(ab.contains("b"));
    Assert.assertFalse(ab.contains("c"));
  }
}

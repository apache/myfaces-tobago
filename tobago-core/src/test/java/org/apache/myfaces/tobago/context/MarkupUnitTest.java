package org.apache.myfaces.tobago.context;

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
    
    Markup accent = Markup.valueOf("accent");
    Assert.assertSame(accent, Markup.valueOf(accent));
  }

  @Test
  public void testAdd() {
    Markup A = Markup.valueOf("a");
    Markup AB = Markup.valueOf("a,b");
    Markup ABC = Markup.valueOf("a,b,c");
    Assert.assertEquals(A, Markup.NULL.add("a"));
    Assert.assertEquals(AB, A.add("b"));
    Assert.assertEquals(ABC, AB.add("c"));
    Assert.assertSame(A, A.add("a"));
    Assert.assertSame(AB, AB.add("a"));
  }
}

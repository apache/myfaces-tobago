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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MarkupUnitTest {

  private static final String[] AB = new String[]{"a", "b"};

  @Test
  public void testString() {
    Assertions.assertNull(Markup.valueOf((String) null));

    Assertions.assertEquals("foobar", Markup.valueOf("foo$bar").toString());

    Assertions.assertEquals("fooBar", Markup.valueOf("fooBar").toString());

    Assertions.assertArrayEquals(new String[]{"accent"}, toArray(Markup.valueOf("accent").iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf("a,b").iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf("a, b").iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf("a b").iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf(", \ta , ,\n b ,").iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf(", \ta\" , ,\n b ,").iterator()));
  }

  @Test
  public void testStringArray() {
    Assertions.assertNull(Markup.valueOf((String[]) null));

    Assertions.assertNull(Markup.valueOf(new String[]{}));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf(new String[]{"a", "b"}).iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf(new String[]{" a ", " b "}).iterator()));
  }

  @Test
  public void testObject() {
    Assertions.assertNull(Markup.valueOf((Object) null));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf((Object) ", \ta , ,\n b ,").iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf((Object) new String[]{"a", "b"}).iterator()));

    Assertions.assertArrayEquals(AB,
        toArray(Markup.valueOf((Object) new String[]{" a ", " b "}).iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf(new StringBuilder("a, b")).iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf(AB).iterator()));

    Assertions.assertArrayEquals(AB, toArray(Markup.valueOf(Arrays.asList(AB)).iterator()));
  }

  @Test
  public void testMarkup() {
    Assertions.assertNull(Markup.valueOf((Markup) null));

    final Markup accent = Markup.valueOf("accent");
    Assertions.assertSame(accent, Markup.valueOf(accent));
  }

  @Test
  public void testAdd() {
    final Markup a = Markup.valueOf("a");
    final Markup b = Markup.valueOf("b");
    final Markup c = Markup.valueOf("c");
    final Markup ab = Markup.valueOf("a,b");
    final Markup abc = Markup.valueOf("a,b,c");
    Assertions.assertEquals(a, Markup.NULL.add(a));
    Assertions.assertEquals(ab, a.add(b));
    Assertions.assertEquals(abc, ab.add(c));
    Assertions.assertEquals(abc, ab.add(abc));
    Assertions.assertSame(a, a.add(a));
    Assertions.assertSame(ab, ab.add(a));
    Assertions.assertSame(ab, ab.add(ab));
  }

  @Test
  public void testRemove() {
    final Markup a = Markup.valueOf("a");
    final Markup b = Markup.valueOf("b");
    final Markup c = Markup.valueOf("c");
    final Markup ab = Markup.valueOf("a,b");
    final Markup bc = Markup.valueOf("b,c");
    final Markup abc = Markup.valueOf("a,b,c");
    Assertions.assertEquals(Markup.NULL, Markup.NULL.remove(a));
    Assertions.assertEquals(a, a.remove(b));
    Assertions.assertEquals(Markup.NULL, a.remove(a));
    Assertions.assertEquals(b, ab.remove(a));
    Assertions.assertEquals(a, ab.remove(b));
    Assertions.assertEquals(c, abc.remove(ab));
    Assertions.assertEquals(a, ab.remove(bc));
    Assertions.assertEquals(Markup.NULL, abc.remove(abc));
    Assertions.assertSame(b, b.remove(a));
    Assertions.assertSame(ab, ab.remove(c));
  }

  @Test
  public void testContainsString() {
    final Markup a = Markup.valueOf("a");
    final Markup ab = Markup.valueOf("a,b");
    Assertions.assertFalse(Markup.NULL.contains("a"));
    Assertions.assertTrue(a.contains("a"));
    Assertions.assertTrue(a.contains((String) null));
    Assertions.assertFalse(a.contains("b"));
    Assertions.assertTrue(ab.contains("a"));
    Assertions.assertTrue(ab.contains("b"));
    Assertions.assertFalse(ab.contains("c"));
  }

  @Test
  public void testContainsMarkup() {
    final Markup a = Markup.valueOf("a");
    final Markup ab = Markup.valueOf("a,b");
    Assertions.assertFalse(Markup.NULL.contains(Markup.valueOf("a")));
    Assertions.assertTrue(a.contains(Markup.NULL));
    Assertions.assertTrue(a.contains((Markup) null));
    Assertions.assertTrue(a.contains(Markup.valueOf("a")));
    Assertions.assertFalse(a.contains(Markup.valueOf("b")));
    Assertions.assertTrue(ab.contains(Markup.valueOf("a")));
    Assertions.assertTrue(ab.contains(Markup.valueOf("b")));
    Assertions.assertFalse(ab.contains(Markup.valueOf("c")));
    Assertions.assertTrue(ab.contains(Markup.valueOf("a,b")));
    Assertions.assertTrue(ab.contains(Markup.valueOf("a,a")));
    Assertions.assertFalse(ab.contains(Markup.valueOf("a,c")));
    Assertions.assertFalse(ab.contains(Markup.valueOf("a,c,d,e,c,f,e,f")));
  }

  @Test
  public void testIsEmpty() {
    final Markup a = Markup.valueOf("a");
    final Markup ab = Markup.valueOf("a,b");

    Assertions.assertFalse(a.isEmpty());
    Assertions.assertFalse(ab.isEmpty());
    Assertions.assertTrue(Markup.NULL.isEmpty());
  }

  public static Object[] toArray(final Iterator<?> iterator) {
    final List<Object> list = new ArrayList<Object>(10);
    while (iterator.hasNext()) {
      list.add(iterator.next());
    }
    return list.toArray();
  }

  @Test
  public void testNames() throws IllegalAccessException {
    final List<Field> markups = new ArrayList<>();
    final List<Field> strings = new ArrayList<>();

    for (final Field field : Markup.class.getFields()) {
      if (field.getName().startsWith("STRING")) {
        strings.add(field);
      } else if (!field.getName().equals("NULL")) {
        markups.add(field);
      }
    }

    Assertions.assertEquals(markups.size(), strings.size(), "Is for every markup a string constant defined?");

    for (final Field markupField : markups) {
      final Markup markup = (Markup) markupField.get(null);

      boolean pendantFound = false;
      for (final Field stringField : strings) {
        if (stringField.getName().equals("STRING_" + markupField.getName())) {
          final String string = (String) stringField.get(null);

          Assertions.assertEquals(markup.toString(), string);
          pendantFound = true;
        }
      }

      Assertions.assertTrue(pendantFound, "Could be a string pendant found for " + markup + "?");
    }
  }
}

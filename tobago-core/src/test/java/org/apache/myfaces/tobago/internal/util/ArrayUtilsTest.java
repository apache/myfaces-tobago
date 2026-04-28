package org.apache.myfaces.tobago.internal.util;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

class ArrayUtilsTest {

  @Test
  void testContainsWithNullArray() {
    String[] array = null;
    Assertions.assertFalse(ArrayUtils.contains(array, "test"));
  }

  @Test
  void testContainsWithEmptyArray() {
    String[] array = {};
    Assertions.assertFalse(ArrayUtils.contains(array, "test"));
  }

  @Test
  void testContainsWithMatchingElement() {
    String[] array = {"first", "second", "third"};
    Assertions.assertTrue(ArrayUtils.contains(array, "second"));
  }

  @Test
  void testContainsWithNonMatchingElement() {
    String[] array = {"first", "second", "third"};
    Assertions.assertFalse(ArrayUtils.contains(array, "fourth"));
  }

  @Test
  void testContainsWithNullValue() {
    String[] array = {"first", null, "third"};
    Assertions.assertTrue(ArrayUtils.contains(array, null));
  }

  @Test
  void testContainsWithEntirelyNullArray() {
    String[] array = {null, null, null};
    Assertions.assertTrue(ArrayUtils.contains(array, null));
  }

  @Test
  void testContainsWithNullElementNotPresent() {
    String[] array = {"first", "second", "third"};
    Assertions.assertFalse(ArrayUtils.contains(array, null));
  }

  @Test
  void testContainsIntArrayWithNull() {
    int[] array = null;
    Assertions.assertFalse(ArrayUtils.contains(array, 5));
  }

  @Test
  void testContainsIntArrayWithEmptyArray() {
    int[] array = {};
    Assertions.assertFalse(ArrayUtils.contains(array, 5));
  }

  @Test
  void testContainsIntArrayWithMatchingElement() {
    int[] array = {1, 2, 3, 4, 5};
    Assertions.assertTrue(ArrayUtils.contains(array, 3));
  }

  @Test
  void testContainsIntArrayWithNonMatchingElement() {
    int[] array = {1, 2, 3, 4, 5};
    assertFalse(ArrayUtils.contains(array, 6));
  }

  @Test
  void testIndexOfWithNullArray() {
    String[] array = null;
    Assertions.assertEquals(-1, ArrayUtils.indexOf(array, "test"));
  }

  @Test
  void testIndexOfWithEmptyArray() {
    String[] array = {};
    Assertions.assertEquals(-1, ArrayUtils.indexOf(array, "test"));
  }

  @Test
  void testIndexOfWithMatchingElement() {
    String[] array = {"first", "second", "third"};
    Assertions.assertEquals(1, ArrayUtils.indexOf(array, "second"));
  }

  @Test
  void testIndexOfWithNonMatchingElement() {
    String[] array = {"first", "second", "third"};
    Assertions.assertEquals(-1, ArrayUtils.indexOf(array, "fourth"));
  }

  @Test
  void testIndexOfWithNullValue() {
    String[] array = {"first", null, "third"};
    Assertions.assertEquals(1, ArrayUtils.indexOf(array, null));
  }

  @Test
  void testIndexOfWithEntirelyNullArray() {
    String[] array = {null, null, null};
    Assertions.assertEquals(0, ArrayUtils.indexOf(array, null));
  }

  @Test
  void testIndexOfWithNullElementNotPresent() {
    String[] array = {"first", "second", "third"};
    Assertions.assertEquals(-1, ArrayUtils.indexOf(array, null));
  }

  /**
   * Tests for the primitive `indexOf` method of `ArrayUtils` class.
   */

  @Test
  void testIndexOfIntArrayWithNull() {
    int[] array = null;
    Assertions.assertEquals(-1, ArrayUtils.indexOf(array, 5));
  }

  @Test
  void testIndexOfIntArrayWithEmptyArray() {
    int[] array = {};
    Assertions.assertEquals(-1, ArrayUtils.indexOf(array, 5));
  }

  @Test
  void testIndexOfIntArrayWithMatchingElement() {
    int[] array = {1, 2, 3, 4, 5};
    Assertions.assertEquals(2, ArrayUtils.indexOf(array, 3));
  }

  @Test
  void testIndexOfIntArrayWithNonMatchingElement() {
    int[] array = {1, 2, 3, 4, 5};
    Assertions.assertEquals(-1, ArrayUtils.indexOf(array, 6));
  }

}

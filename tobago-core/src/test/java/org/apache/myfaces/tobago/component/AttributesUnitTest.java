package org.apache.myfaces.tobago.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AttributesUnitTest {

  @Test
  void testValueOf() {
    for (Attributes a : Attributes.values()) {
      Assertions.assertEquals(a, Attributes.valueOfFailsafe(a.getName()));
    }
  }
}

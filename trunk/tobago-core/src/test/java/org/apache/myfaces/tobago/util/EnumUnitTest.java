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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.junit.Assert;

import java.lang.reflect.Field;

public abstract class EnumUnitTest {

  protected String getEnumRegexp() {
    return "[a-z][a-zA-Z]*";
  }

  protected String constantCaseToEnum(String constant) {
    return StringUtils.constantToLowerCamelCase(constant);
  }

  public <E extends Enum<E>> void testNames(Class<E> enumType)
      throws NoSuchFieldException, IllegalAccessException {

    final Field[] fields = enumType.getFields();
    final E[] values = enumType.getEnumConstants();

    Assert.assertEquals("Is for every enum a string constant defined?", fields.length, 2 * values.length);

    for (final Field field : fields) {
      final Object object = field.get(null);
      final String fieldName = field.getName();
      if (object instanceof String) {
        // case String constant
        final String value = (String) object;
        final String expected = constantCaseToEnum(fieldName);
        Assert.assertEquals(expected, value);
        Assert.assertNotNull("exists", enumType.getField(value));
      } else if (object.getClass().isAssignableFrom(enumType)) {
        // case enum
        Assert.assertTrue("value='" + fieldName + "'", fieldName.matches(getEnumRegexp()));
      } else {
        Assert.fail();
      }
    }
  }
}

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

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class RendererTypesUnitTest {

  @Test
  public void testNames() throws IllegalAccessException {

    for (Field field : RendererTypes.class.getFields()) {
      String value = (String) field.get(null);
      Assert.assertTrue("value='" + value + "'", value.matches("[A-Z][a-zA-Z]*"));
      Assert.assertEquals(StringUtils.constantToCamelCase(field.getName()), value);
    }
  }
}

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

import junit.framework.TestCase;

/**
 * User: lofwyr
 * Date: 25.01.2008 17:05:52
 */
public class GridArrayUnitTest extends TestCase {

  public void test() {

    GridArray gridArray = new GridArray(1, 1);
    gridArray.get(0,0);
    assertEquals (1, gridArray.getRowCount());
    gridArray.get(0,1);
    assertEquals (1, gridArray.getRowCount());
    gridArray.set(0,1, null);
    assertEquals (2, gridArray.getRowCount());
  }
}

package org.apache.myfaces.tobago.layout.math;

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

public interface Equation {

  /**
   * The row has the format: index 0 to n-2 are the factors and index n-1 is the result.
   * @param length The length of the array which has to be created.
   */
  double[] fillRow(int length);

  /**
   * Priority for sorting.
   * @return Large values represent a high priority
   */
  int priority();

}

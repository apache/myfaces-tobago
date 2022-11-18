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

package org.apache.myfaces.tobago.internal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface Fruit extends Serializable {

  static List<Fruit> getFreshFruits() {
    final List<Fruit> list = new ArrayList<>();
    list.add(Apple.GOLDEN_DELICIOUS);
    list.add(Apple.SCHOENER_AUS_BOSKOOP);
    list.add(Pear.WILLIAMS_CHRIST);
    list.add(Pear.KOESTLICHE_AUS_CHARNEUX);
    return list;
  }

  String getName();

  void setName(String name);
}

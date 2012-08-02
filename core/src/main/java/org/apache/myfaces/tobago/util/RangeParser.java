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

/*
 * Created 29.03.2004 at 14:58:11.
 * $Id$
 */

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class RangeParser {

  public static int[] getIndices(String list) {
    List indexList = new ArrayList();
    StringTokenizer st = new StringTokenizer(list, ",");
    while (st.hasMoreTokens()) {
      String token = st.nextToken().trim();
      int idx = token.indexOf('-');
      if (idx == -1) {
        indexList.add(token);
      } else {
        int start = Integer.parseInt(token.substring(0, idx).trim());
        int end = Integer.parseInt(token.substring(idx + 1).trim());
        if (start < end) {
          for (int i = start; i < end + 1; i++) {
            indexList.add(Integer.toString(i));
          }
        } else {
          for (int i = start; i > end - 1; i--) {
            indexList.add(Integer.toString(i));
          }
        }
      }
    }

    int[] indices = new int[indexList.size()];
    for (int i = 0; i < indices.length; i++) {
      indices[i] = Integer.parseInt((String) indexList.get(i));
    }
    return indices;
  }
}

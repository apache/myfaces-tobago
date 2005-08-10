/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 29.03.2004 at 14:58:11.
  * $Id$
  */
package com.atanion.tobago.util;

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
      }
      else {
        int start = Integer.parseInt(token.substring(0, idx).trim());
        int end = Integer.parseInt(token.substring(idx + 1).trim());
        if (start < end) {
          for (int i = start; i < end + 1; i++) {
            indexList.add(Integer.toString(i));
          }
        }
        else {
          for (int i = start; i > end -1; i--){
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
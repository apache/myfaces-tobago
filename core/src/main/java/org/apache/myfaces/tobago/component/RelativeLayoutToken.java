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

/*
 * Date: May 2, 2007
 * Time: 1:34:11 PM
 */
public class RelativeLayoutToken extends LayoutToken {
  public static String DEFAULT_TOKEN_STRING = "1*";
  public static RelativeLayoutToken DEFAULT_INSTANCE = new RelativeLayoutToken(1);
  private int factor = 1;

  public RelativeLayoutToken(int factor) {
    this.factor = factor;
  }


  public int getFactor() {
    return factor;
  }

  public String toString() {
    return factor + "*";
  }
}

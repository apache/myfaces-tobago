package org.apache.myfaces.tobago.example.test;

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

import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.PixelMeasure;

public class MeasureBean {
  
  int intWidth = 200;
  Integer integerWidth = new Integer(250);
  Measure measureWidth = new PixelMeasure(300);
  String stringWidth = "350";
  String stringPxWidth = "400px";
  long longWidth = 450L;
  Any anyWidth = new Any();

  public int getIntWidth() {
    return intWidth;
  }

  public Integer getIntegerWidth() {
    return integerWidth;
  }

  public Measure getMeasureWidth() {
    return measureWidth;
  }

  public String getStringWidth() {
    return stringWidth;
  }

  public String getStringPxWidth() {
    return stringPxWidth;
  }

  public long getLongWidth() {
    return longWidth;
  }

  public Any getAnyWidth() {
    return anyWidth;
  }

  public static class Any {
    @Override
    public String toString() {
      return "500";
    }
  }
}

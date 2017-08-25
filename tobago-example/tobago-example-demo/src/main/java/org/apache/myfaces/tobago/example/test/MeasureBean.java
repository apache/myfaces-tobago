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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.layout.Measure;

public class MeasureBean {

  public int getIntWidth() {
    return 200;
  }

  public int getIntHeight() {
    return 95;
  }

  public Integer getIntegerWidth() {
    return 250;
  }

  public Integer getIntegerHeight() {
    return 95;
  }

  public Measure getMeasureWidth() {
    return Measure.valueOf(300);
  }

  public Measure getMeasureHeight() {
    return Measure.valueOf(95);
  }

  public String getStringWidth() {
    return "350";
  }

  public String getStringHeight() {
    return "95";
  }

  public String getStringPxWidth() {
    return "400px";
  }

  public String getStringPxHeight() {
    return "95px";
  }

  public long getLongWidth() {
    return 450L;
  }

  public long getLongHeight() {
    return 95L;
  }

  public Any getAnyWidth() {
    return new Any("500");
  }

  public Any getAnyHeight() {
    return new Any("95");
  }

  public static class Any {
    
    private String value;

    public Any(final String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }
}

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

public class Partially {

  private int counter;

  private String characters;

  public Partially() {
    final StringBuilder builder = new StringBuilder();
    builder.append("test_characters = [ ");
    for (char c = 0; c < 0x24F; c++) {

      builder.append('\'');
      if (c == '\'' || c == '\\' || c == '\n' || c == '\r') { // to have a valid JavaScript string.
        builder.append('\\');
      }
      builder.append(c);
      builder.append("\', ");
      if (c % 16 == 15) {
        builder.append("\n");
      }
    }
    builder.append("];");
    characters = builder.toString();
  }

  public void resetCounter() {
    counter = 0;
  }

  public void reload() {
    counter++;
  }

  public int getCounter() {
    return counter;
  }

  public void setCounter(final int counter) {
    this.counter = counter;
  }

  public String getCharacters() {
    return characters;
  }

  public void setCharacters(final String characters) {
    this.characters = characters;
  }
}

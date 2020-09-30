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

package org.apache.myfaces.tobago.model;

import org.apache.myfaces.tobago.util.EnumUnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SelectableUnitTest extends EnumUnitTest {

  @Test
  public void testNames() throws IllegalAccessException, NoSuchFieldException {
    testNames(Selectable.class);
  }

  @Test
  public void testTypeScript() throws IOException {
    final Path path = Paths.get("").toAbsolutePath().getParent().resolve(
        Paths.get("tobago-theme", "tobago-theme-standard", "npm", "ts", "tobago-selectable.ts"));

    final List<String> words = getWords(path);

    for (Selectable selectable : Selectable.values()) {
      Assertions.assertTrue(words.contains(selectable.name()),
          selectable.name() + " should be found in tobago-selectable.ts");
    }
  }

  private List<String> getWords(final Path path) throws IOException {
    List<String> words = new ArrayList<>();

    final String fileContent = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);


    StringBuilder stringBuilder = new StringBuilder();

    for (char c : fileContent.toCharArray()) {
      if (('0' <= c && c <= '9')
          || ('A' <= c && c <= 'Z')
          || ('a' <= c && c <= 'z')) {
        stringBuilder.append(c);
      } else {
        words.add(stringBuilder.toString());
        stringBuilder = new StringBuilder();
      }
    }

    return words;
  }
}

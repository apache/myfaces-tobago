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

package org.apache.myfaces.tobago.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class SourceFileReaderUnitTest {

  @Test
  public void testJavaSource() throws IOException {
    try (InputStream stream = SourceFileReader.class.getClassLoader().getResourceAsStream("simple.js")) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
      String content = reader.lines().collect(Collectors.joining("\n"));
      final String result = content.replaceAll(SourceFileReader.JAVA, "").trim();
      Assertions.assertEquals("console.log('simple');", result);
    }
  }

  @Test
  public void testShellSource() throws IOException {
    try (InputStream stream = SourceFileReader.class.getClassLoader().getResourceAsStream("simple.sh")) {
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
      String content = reader.lines().collect(Collectors.joining("\n"));
      final String result = content.replaceAll(SourceFileReader.SHELL, "").trim();
      Assertions.assertEquals("#! /bin/bash\n\n# simple test\necho 'simple'", result);
    }
  }
}

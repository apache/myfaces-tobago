package org.apache.myfaces.tobago.util;

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


import org.apache.myfaces.tobago.internal.util.JavascriptWriterUtils;
import org.apache.myfaces.tobago.internal.util.WriterUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.CharArrayWriter;
import java.io.IOException;

public class JavascriptWriterUtilsTest {

  @Test
  public void test() {
    CharArrayWriter writer = new CharArrayWriter();
    JavascriptWriterUtils helper = new JavascriptWriterUtils(writer, "");
    String text = "\"";
    String result = "\\\"";

    testText(helper, writer, text, result);
    text = "\\\"";
    result = "\\\\\\\"";
    testText(helper, writer, text, result);
  }

  private void testText(WriterUtils writerUtil, CharArrayWriter writer, String text, String escaped) {
    try {
      writer.reset();
      writerUtil.writeText(text);
      String result = String.valueOf(writer.toCharArray());
      Assert.assertEquals(escaped, result);

    } catch (IOException e) {
      // could not occur with CharArrayWriter
    }
  }
}

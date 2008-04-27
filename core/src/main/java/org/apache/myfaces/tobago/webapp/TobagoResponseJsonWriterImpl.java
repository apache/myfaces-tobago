package org.apache.myfaces.tobago.webapp;

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

import org.apache.myfaces.tobago.util.FastStringWriter;

import java.io.Writer;
import java.io.IOException;

public class TobagoResponseJsonWriterImpl extends TobagoResponseWriterImpl {
  private Writer javascriptWriter;
  private boolean javascriptMode;
  public TobagoResponseJsonWriterImpl(Writer writer, String contentType, String characterEncoding) {
    super(writer, contentType, characterEncoding);
    this.javascriptWriter = new FastStringWriter();
  }

  public void endJavascript() throws IOException {
    super.endJavascript();
    javascriptMode = true;
  }

  public void startJavascript() throws IOException {
    super.startJavascript();
    javascriptMode = false;
  }

  @Override
  public void write(String string) throws IOException {
    writeInternal(javascriptMode?javascriptWriter:getWriter(), string);
  }

  public void writeJavascript(String script) throws IOException {
    writeInternal(javascriptWriter, script);
    writeInternal(javascriptWriter, "\n");
  }

  public String getJavascript() {
    return javascriptWriter.toString();
  }
}

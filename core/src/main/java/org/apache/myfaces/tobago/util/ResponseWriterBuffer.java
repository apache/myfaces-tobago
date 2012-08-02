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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;

//
// Buffering scheme: we use a tremendously simple buffering
// scheme that greatly reduces the number of calls into the
// Writer/PrintWriter.  In practice this has produced significant
// measured performance gains (at least in JDK 1.3.1).  We only
// support adding single characters to the buffer, so anytime
// multiple characters need to be written out, the entire buffer
// gets flushed.  In practice, this is good enough, and keeps
// the core simple.
//

/**
 * User: lofwyr
 * Date: 07.05.2007 12:03:26
 */
public class ResponseWriterBuffer {

  private static final Log LOG = LogFactory.getLog(ResponseWriterBuffer.class);

  private static final int BUFFER_SIZE = 64;

  private final char[] buff = new char[BUFFER_SIZE];

  private int bufferIndex;

  private final Writer writer;

  public ResponseWriterBuffer(final Writer writer) {
    this.writer = writer;
  }

  /**
   * Add a character to the buffer, flushing the buffer if the buffer is
   * full
   */
  public void addToBuffer(final char ch) throws IOException {
    if (bufferIndex >= BUFFER_SIZE) {
      writer.write(buff, 0, bufferIndex);
      bufferIndex = 0;
    }

    buff[bufferIndex++] = ch;
  }

  public void addToBuffer(final char[] ch) throws IOException {
    if (bufferIndex + ch.length >= BUFFER_SIZE) {
      writer.write(buff, 0, bufferIndex);
      bufferIndex = 0;
    }

    System.arraycopy(ch, 0, buff, bufferIndex, ch.length);
    bufferIndex += ch.length;
  }

  /**
   * Flush the contents of the buffer to the output stream
   * and return the reset buffer index
   */
  public void flushBuffer() throws IOException {
    if (bufferIndex > 0) {
      writer.write(buff, 0, bufferIndex);
    }
    bufferIndex = 0;
  }
}

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

package org.apache.myfaces.tobago.internal.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TobagoResponse extends HttpServletResponseWrapper {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoResponse.class);

  private PrintWriter printWriter = null;
  private StringWriter bufferedWriter = null;


  public TobagoResponse(final HttpServletResponse base) {
    super(base);
  }

  public void setBuffering() {
    if (bufferedWriter == null) {
      bufferedWriter = new StringWriter();
      printWriter = new PrintWriter(bufferedWriter);
    }
  }

  public String getBufferedString() {
    if (bufferedWriter != null) {
      printWriter.flush();
      return bufferedWriter.toString();
    } else {
      return "";
    }
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    LOG.debug("***** getOutputStream() from " + new Exception().getStackTrace()[1]);
    return getResponse().getOutputStream();
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    LOG.debug("***** getWriter() from " + new Exception().getStackTrace()[1]);
    if (printWriter != null) {
      return printWriter;
    }
    return getResponse().getWriter();
  }


  @Override
  public void setContentType(final String s) {
    LOG.debug("***** setContentType(" + s + ") from " + new Exception().getStackTrace()[1]);
    getResponse().setContentType(s);
  }

}

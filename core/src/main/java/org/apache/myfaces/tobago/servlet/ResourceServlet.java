package org.apache.myfaces.tobago.servlet;

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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: lofwyr
 *
 * @since 1.0.7
 *        <p/>
 *        XXX This class is in development. Please don't use it in
 *        production environment!
 */
public class ResourceServlet extends HttpServlet {

  private static final Log LOG = LogFactory.getLog(ResourceServlet.class);

  @Override
  protected void service(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String requestURI = request.getRequestURI();

    String resource = requestURI.substring(
        request.getContextPath().length() + 1); // todo: make it "stable"

//    response.setHeader("Cache-Control", "max-age=3600");
//    response.setDateHeader("Expires", 3600);
    // todo: maybe support more extensions (configurable?)
    if (requestURI.endsWith(".gif")) {
      response.setContentType("image/gif");
    } else if (requestURI.endsWith(".png")) {
      response.setContentType("image/png");
    } else if (requestURI.endsWith(".jpg")) {
      response.setContentType("image/jpeg");
    } else if (requestURI.endsWith(".js")) {
      response.setContentType("text/javascript");
    } else if (requestURI.endsWith(".css")) {
      response.setContentType("text/css");
    } else {
      LOG.warn("Unsupported file extension, will be ignored for security "
          + "reasons. resource='" + resource + "'");
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
    InputStream inputStream = null;
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      inputStream = classLoader.getResourceAsStream(resource);
      if (inputStream != null) {
        IOUtils.copy(inputStream, response.getOutputStream());
      } else {
        LOG.warn("Resource '" + resource + "' not found!");
      }
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }
}

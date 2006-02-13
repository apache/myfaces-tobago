package org.apache.myfaces.tobago.servlet;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.Theme;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: lofwyr
 * @since 1.0.7
 *
 * XXX This class is in development. Please don't use it in
 * production environment!
 */
public class ResourceServlet extends HttpServlet {

  private static final Log LOG = LogFactory.getLog(ResourceServlet.class);
  private static final int BUFFER_SIZE = 1024;

  protected void service(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    LOG.info("C " + request.getContextPath());
    LOG.info("Q " + request.getQueryString());
    String requestURI = request.getRequestURI();
    LOG.info("R " + requestURI);

    String resource = requestURI.substring(
        request.getContextPath().length() + 1); // todo: make it "stable"

    LOG.info("L " + resource);

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
      LOG.warn("Unsupported file extension, will be ignored for security " +
          "reasons. resource='" + resource + "'");
      response.setStatus(403); // todo: is there a constant in servlet API for "forbidden"?
    }

    InputStream inputStream
        = Theme.class.getClassLoader().getResourceAsStream(resource);
    
    ServletOutputStream outputStream = response.getOutputStream();

    byte[] buffer = new byte[BUFFER_SIZE];
    int number;
    while (-1 != (number = inputStream.read(buffer))) {
      outputStream.write(buffer, 0, number);
    }
  }
}

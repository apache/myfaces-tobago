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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: lofwyr
 * Date: 10.11.2005 21:36:20
 *
 * XXX This class is in development. Please don't use it in
 * production environment!
 */
public class ResourceServlet extends HttpServlet {

  private static final Log LOG = LogFactory.getLog(ResourceServlet.class);

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

    InputStream is
        = Theme.class.getClassLoader().getResourceAsStream(resource);

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
    }

    int c;
    while (-1 != (c = is.read())) {
      response.getOutputStream().write(c);
    }
  }
}

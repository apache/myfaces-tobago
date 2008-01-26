package org.apache.myfaces.tobago.servlet;

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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.util.MimeTypeUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * <p><pre>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;ResourceServlet&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;org.apache.myfaces.tobago.servlet.ResourceServlet&lt;/servlet-class&gt;
 *   &lt;init-param&gt;
 *     &lt;description&gt;The value for the expires header in seconds.
 *            Default is no expires header.&lt;/description&gt;
 *     &lt;param-name&gt;expires&lt;/param-name&gt;
 *     &lt;param-value&gt;14400&lt;/param-value&gt;
 *   &lt;/init-param&gt;
 * &lt;/servlet&gt;
 * &lt;servlet-mapping&gt;
 *   &lt;servlet-name&gt;ResourceServlet&lt;/servlet-name&gt;
 *   &lt;url-pattern&gt;/org/apache/myfaces/tobago/renderkit/*&lt;/url-pattern&gt;
 * &lt;/servlet-mapping&gt;
 * </pre><p>
 *
 * @since 1.0.7
 *        <p/>
 *        XXX This class is in development. Please don't use it in
 *        production environment!
 */
public class ResourceServlet extends HttpServlet {

  private static final long serialVersionUID = -4491419290205206466L;

  private static final Log LOG = LogFactory.getLog(ResourceServlet.class);

  public static final String DOJO_RESOURCE_PREFIX
      = "org/apache/myfaces/tobago/renderkit/html/standard/standard/script/dojo/";

  private Long expires;

  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    String expiresString = servletConfig.getInitParameter("expires");

    expires = null;
    if (expiresString != null) {
      try {
        expires = new Long(expiresString);
      } catch (NumberFormatException e) {
        LOG.error("Caught: " + e.getMessage(), e);
      }
    }
  }

  @Override
  protected void service(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String requestURI = request.getRequestURI();

    String resource = requestURI.substring(
        request.getContextPath().length() + 1); // todo: make it "stable"
    if (resource.startsWith(DOJO_RESOURCE_PREFIX)) {
      // Todo : dojo version
      resource = "dojo-release-1.0.1/" + resource.substring(DOJO_RESOURCE_PREFIX.length());
    }

    if (expires != null) {
      response.setHeader("Cache-Control", "max-age=" + expires);
      response.setDateHeader("Expires", new Date().getTime() + (expires * 1000));
    }
    String contentType = MimeTypeUtils.getMimeTypeForFile(requestURI);
    if (contentType != null) {
      response.setContentType(contentType);
    } else {
      LOG.warn("Unsupported file extension, will be ignored for security "
          + "reasons; resource='" + resource + "'");
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

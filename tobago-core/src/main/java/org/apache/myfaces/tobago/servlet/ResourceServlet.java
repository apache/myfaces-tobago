package org.apache.myfaces.tobago.servlet;

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

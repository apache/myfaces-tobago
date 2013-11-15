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

package org.apache.myfaces.tobago.facelets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/*
 * Was copied from MyFaces-Impl, because there is no JSF base class.
 */
public final class Resource {

  private static final Logger LOG = LoggerFactory.getLogger(Resource.class);

  private Resource() {
  }

  /**
   * Get an URL of an internal resource. First, {@link javax.faces.context.ExternalContext#getResource(String)} is
   * checked for an non-null URL return value. In the case of a null return value (as it is the case for Weblogic 8.1
   * for a packed war), a URL with a special URL handler is constructed, which can be used for <em>opening</em> a
   * serlvet resource later. Internally, this special URL handler will call
   * {@link javax.servlet.ServletContext#getResourceAsStream(String)} when an inputstream is requested.
   * This works even on Weblogic 8.1
   *
   * @param ctx  the faces context from which to retrieve the resource
   * @param path an URL path
   * @return an url representing the URL and on which getInputStream() can be called to get the resource
   * @throws java.net.MalformedURLException
   */
  public static URL getResourceUrl(final FacesContext ctx, final String path) throws MalformedURLException {
    final ExternalContext externalContext = ctx.getExternalContext();
    URL url = externalContext.getResource(path);
    if (LOG.isTraceEnabled()) {
      LOG.trace("Resource-Url from external context: " + url);
    }
    if (url == null) {
      // This might happen on Servlet container which doesnot return
      // anything
      // for getResource() (like weblogic 8.1 for packaged wars) we
      // are trying
      // to use an own URL protocol in order to use
      // ServletContext.getResourceAsStream()
      // when opening the url
      if (resourceExist(externalContext, path)) {
        url = getUrlForResourceAsStream(externalContext, path);
      }
    }
    return url;
  }

  // This method could be used above to provide a 'fail fast' if a
  // resource
  // doesnt exist. Otherwise, the URL will fail on the first access.
  private static boolean resourceExist(final ExternalContext externalContext, final String path) {
    if ("/".equals(path)) {
      // The root context exists always
      return true;
    }
    final Object ctx = externalContext.getContext();
    if (ctx instanceof ServletContext) {
      final ServletContext servletContext = (ServletContext) ctx;
      final InputStream stream = servletContext.getResourceAsStream(path);
      if (stream != null) {
        try {
          stream.close();
        } catch (final IOException e) {
          // Ignore here, since we donnot wanted to read from this
          // resource anyway
        }
        return true;
      }
    }
    return false;
  }

  // Construct URL with special URLStreamHandler for proxying
  // ServletContext.getResourceAsStream()
  private static URL getUrlForResourceAsStream(final ExternalContext externalContext, final String path)
      throws MalformedURLException {
    final URLStreamHandler handler = new URLStreamHandler() {
      protected URLConnection openConnection(final URL u) throws IOException {
        final String file = u.getFile();
        return new URLConnection(u) {
          public void connect() throws IOException {
          }

          public InputStream getInputStream() throws IOException {
            if (LOG.isTraceEnabled()) {
              LOG.trace("Opening internal url to " + file);
            }
            final Object ctx = externalContext.getContext();
            // Or maybe fetch the external context afresh ?
            // Object ctx =
            // FacesContext.getCurrentInstance().getExternalContext().getContext();

            if (ctx instanceof ServletContext) {
              final ServletContext servletContext = (ServletContext) ctx;
              final InputStream stream = servletContext.getResourceAsStream(file);
              if (stream == null) {
                throw new FileNotFoundException("Cannot open resource " + file);
              }
              return stream;
            } else {
              throw new IOException("Cannot open resource for an context of "
                  + (ctx != null ? ctx.getClass() : null));
            }
          }
        };
      }
    };
    return new URL("internal", null, 0, path, handler);
  }
}

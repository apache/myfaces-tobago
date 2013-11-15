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

package org.apache.myfaces.tobago.example.addressbook.web;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.example.addressbook.Address;
import org.apache.myfaces.tobago.example.addressbook.Picture;
import org.apache.myfaces.tobago.servlet.NonFacesRequestServlet;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public class PictureServlet extends NonFacesRequestServlet {
  private static final Logger LOG = LoggerFactory.getLogger(PictureServlet.class);

  public String invokeApplication(final FacesContext facesContext) {
    final Controller controller = (Controller) VariableResolverUtils.resolveVariable(facesContext, "controller");
    final Address address = controller.getCurrentAddress();
    if (address.hasPicture()) {
      final Picture picture = address.getPicture();
      final byte[] content = picture.getContent();
      final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
      if (content != null && content.length > 0) {
        response.setContentType(picture.getContentType());
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        try {
          IOUtils.copy(inputStream, response.getOutputStream());
        } catch (final IOException e) {
          LOG.error("", e);
        } finally{
          IOUtils.closeQuietly(inputStream);
        }
      }
      facesContext.responseComplete();
    }
    return null;  
  }
}

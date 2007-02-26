package org.apache.myfaces.tobago.example.addressbook.web;

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

import org.apache.myfaces.tobago.util.VariableResolverUtil;
import org.apache.myfaces.tobago.example.addressbook.Address;
import org.apache.myfaces.tobago.example.addressbook.Picture;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.FactoryFinder;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import java.io.IOException;
import java.io.ByteArrayInputStream;


public class PictureServlet extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    LifecycleFactory lFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle = lFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    FacesContextFactory fcFactory =
        (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    FacesContext facesContext = fcFactory.getFacesContext(getServletContext(), request, response, lifecycle);
    Controller controller = (Controller) VariableResolverUtil.resolveVariable(facesContext, "controller");
    Address address = controller.getCurrentAddress();
    if (address.hasPicture()) {
      Picture picture = address.getPicture();
      byte[] content = picture.getContent();
      if (content != null && content.length > 0) {
        response.setContentType(picture.getContentType());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content);
        try {
          IOUtils.copy(inputStream, response.getOutputStream());
        } finally{
          IOUtils.closeQuietly(inputStream);
        }
      }
    }
  }
}

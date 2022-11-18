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

package org.apache.myfaces.tobago.example.demo.bestpractice;

import org.apache.commons.io.IOUtils;
import org.apache.myfaces.tobago.example.demo.DemoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;

@RequestScoped
@Named
public class BestPracticeController {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private String status;

  public String throwException() {
    throw new DemoException("This exception is forced by the user.");
  }

  public String viewPdfInBrowser() {
    return viewFile(false, true);
  }

  public String viewPdfOutsideOfBrowser() {
    return viewFile(true, true);
  }

  public String viewFile(final boolean outside, final boolean pdf) {

    final FacesContext facesContext = FacesContext.getCurrentInstance();

    try (InputStream inputStream = getInputStream(pdf, facesContext)) {
      final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
      response.setContentType(pdf ? "application/pdf" : "text/plain");
      if (outside) {
        response.setHeader("Content-Disposition", "attachment; filename=x-sample." + (pdf ? "pdf" : "txt"));
      }
      IOUtils.copy(inputStream, response.getOutputStream());
    } catch (final IOException e) {
      LOG.warn("Cannot deliver " + (pdf ? "pdf" : "txt"), e);
      return "error"; // response via faces
    }
    facesContext.responseComplete();
    return null;
  }

  private InputStream getInputStream(boolean pdf, FacesContext facesContext) {
    final String path = "content/360-non-faces-response/x-sample." + (pdf ? "pdf" : "txt");
    InputStream inputStream = facesContext.getExternalContext().getResourceAsStream(path);
    if (inputStream == null) {
      inputStream = facesContext.getExternalContext().getResourceAsStream("/" + path);
    }
    return inputStream;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }
}

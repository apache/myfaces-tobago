package org.apache.myfaces.tobago.example.demo.bestpractice;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BestPracticeController {

  private static final Logger LOG = LoggerFactory.getLogger(BestPracticeController.class);

  private List<Bird> birds = new ArrayList<Bird>(Arrays.asList(
      new Bird("Amsel", 25),
      new Bird("Drossel", 25),
      new Bird("Fink", 9),
      new Bird("Star", 19))
  );

  private String newBirdName;
  
  private int newBirdSize;
  
  private String status;

  public String throwException() {
    throw new RuntimeException("This exception is forced by the user.");
  }

  public String viewPdfInBrowser() {
    return viewPdf(false);
  }

  public String viewPdfOutsideOfBrowser() {
    return viewPdf(true);
  }

  private String viewPdf(boolean outside) {

    FacesContext facesContext = FacesContext.getCurrentInstance();

    InputStream inputStream = null;
    try {
      inputStream = facesContext.getExternalContext().getResourceAsStream("content/12/03/x-sample.pdf");
      if (inputStream == null) {
        inputStream = facesContext.getExternalContext().getResourceAsStream("/content/12/03/x-sample.pdf");
      }
      HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
      response.setContentType("application/pdf");
      if (outside) {
        response.setHeader("Content-Disposition", "attachment; filename=x-sample.pdf");
      }
      IOUtils.copy(inputStream, response.getOutputStream());
    } catch (IOException e) {
      LOG.warn("Cannot deliver pdf", e);
      return "error"; // response via faces
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
    facesContext.responseComplete();
    return null;
  }

  public List<Bird> getBirds() {
    return birds;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getNewBirdName() {
    return newBirdName;
  }

  public void setNewBirdName(String newBirdName) {
    this.newBirdName = newBirdName;
  }

  public int getNewBirdSize() {
    return newBirdSize;
  }

  public void setNewBirdSize(int newBirdSize) {
    this.newBirdSize = newBirdSize;
  }

  public String addNewBird() {
    birds.add(new Bird(newBirdName, newBirdSize));
    newBirdName = null;
    newBirdSize = 0;
    return null;
  }
}

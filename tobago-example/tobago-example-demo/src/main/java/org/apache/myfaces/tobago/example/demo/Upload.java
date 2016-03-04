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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.internal.util.PartUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class Upload implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(Upload.class);

  private Part file1;
  private Part file2;

  private List<UploadItem> list = new ArrayList<UploadItem>();

  public String upload() {
   upload(file1);
   upload(file2);
    return null;
  }

  public void upload(Part part) {
    LOG.info("checking file item");
    if (part == null || part.getSize() == 0) {
      return;
    }
    LOG.info("type=" + part.getContentType());
    LOG.info("size=" + part.getSize());
    LOG.info("cd = " + part.getHeader("Content-Disposition"));
    final String submittedFileName = PartUtils.getSubmittedFileName(part);
    LOG.info("name=" + submittedFileName);
    list.add(new UploadItem(submittedFileName, part.getSize(), part.getContentType()));
    FacesContext.getCurrentInstance().addMessage(
        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File was uploaded: " + submittedFileName, null));

  }

  public Part getFile1() {
    return file1;
  }

  public void setFile1(Part file1) {
    this.file1 = file1;
  }

  public Part getFile2() {
    return file2;
  }

  public void setFile2(Part file2) {
    this.file2 = file2;
  }

  public List<UploadItem> getList() {
    return list;
  }
}

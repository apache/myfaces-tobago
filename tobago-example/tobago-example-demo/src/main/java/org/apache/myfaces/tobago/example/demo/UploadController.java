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
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class UploadController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(UploadController.class);

  private Part fileBasic;
  private Part fileContentType;
  private Part[] fileMulti;
  private Part fileAjax;
  private List<UploadItem> uploadItems = new ArrayList<UploadItem>();

  public String uploadBasic() {
    upload(fileBasic);
    return null;
  }

  public String uploadContentType() {
    upload(fileContentType);
    return null;
  }

  public String uploadMulti() {
    for (Part part : fileMulti) {
      upload(part);
    }
    return null;
  }

  public void uploadAjax(AjaxBehaviorEvent event) {
    upload(fileAjax);
  }

  private void upload(Part part) {
    LOG.info("checking file item");
    if (part == null || part.getSize() == 0) {
      return;
    }
    LOG.info("type=" + part.getContentType());
    LOG.info("size=" + part.getSize());
    LOG.info("cd = " + part.getHeader("Content-Disposition"));
    final String submittedFileName = PartUtils.getSubmittedFileName(part);
    LOG.info("name=" + submittedFileName);
    uploadItems.add(new UploadItem(submittedFileName, part.getSize(), part.getContentType()));
    FacesContext.getCurrentInstance().addMessage(
            null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File was uploaded: " + submittedFileName, null));
  }

  public Part getFileBasic() {
    return fileBasic;
  }

  public void setFileBasic(Part fileBasic) {
    this.fileBasic = fileBasic;
  }

  public Part getFileContentType() {
    return fileContentType;
  }

  public void setFileContentType(Part fileContentType) {
    this.fileContentType = fileContentType;
  }

  public Part[] getFileMulti() {
    return fileMulti;
  }

  public void setFileMulti(Part[] fileMulti) {
    this.fileMulti = fileMulti;
  }

  public Part getFileAjax() {
    return fileAjax;
  }

  public void setFileAjax(Part fileAjax) {
    this.fileAjax = fileAjax;
  }

  public List<UploadItem> getUploadItems() {
    return uploadItems;
  }
}

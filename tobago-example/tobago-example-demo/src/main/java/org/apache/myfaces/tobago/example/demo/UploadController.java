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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class UploadController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private Part fileBasic;
  private Part fileContentType;
  private Part[] fileMulti;
  private Part fileAjax;
  private Part fileDropZone;
  private Part[] fileDropZoneAjax;
  private final List<UploadItem> uploadItems = new ArrayList<>();

  public String uploadBasic() {
    upload(fileBasic, "uploadBasic");
    return null;
  }

  public String uploadContentType() {
    upload(fileContentType, "uploadContentType");
    return null;
  }

  public String uploadMulti() {
    for (final Part part : fileMulti) {
      upload(part, "uploadMulti");
    }
    return null;
  }

  public void uploadDropZone() {
    upload(fileDropZone, "uploadDropZone");
  }

  public void uploadDropZoneAjax(final AjaxBehaviorEvent event) {
    for (final Part part : fileDropZoneAjax) {
      upload(part, "uploadDropZoneAjax");
    }
  }

  public void uploadAjax(final AjaxBehaviorEvent event) {
    upload(fileAjax, "uploadAjax");
  }

  private void upload(final Part part, final String action) {
    LOG.info("checking file item");
    if (part == null || part.getSize() == 0) {
      return;
    }
    LOG.info("type=" + part.getContentType());
    LOG.info("size=" + part.getSize());
    LOG.info("cd = " + part.getHeader("Content-Disposition"));
    final String submittedFileName = part.getSubmittedFileName();
    LOG.info("name=" + submittedFileName);
    uploadItems.add(new UploadItem(submittedFileName, part.getSize(), part.getContentType(), action));
    FacesContext.getCurrentInstance().addMessage(
        null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File was uploaded: " + submittedFileName, null));
  }

  public Part getFileBasic() {
    return fileBasic;
  }

  public void setFileBasic(final Part fileBasic) {
    this.fileBasic = fileBasic;
  }

  public Part getFileContentType() {
    return fileContentType;
  }

  public void setFileContentType(final Part fileContentType) {
    this.fileContentType = fileContentType;
  }

  public Part[] getFileMulti() {
    return fileMulti;
  }

  public void setFileMulti(final Part[] fileMulti) {
    this.fileMulti = fileMulti;
  }

  public Part getFileAjax() {
    return fileAjax;
  }

  public void setFileAjax(final Part fileAjax) {
    this.fileAjax = fileAjax;
  }

  public Part getFileDropZone() {
    return fileDropZone;
  }

  public void setFileDropZone(Part fileDropZone) {
    this.fileDropZone = fileDropZone;
  }

  public Part[] getFileDropZoneAjax() {
    return fileDropZoneAjax;
  }

  public void setFileDropZoneAjax(Part[] fileDropZoneAjax) {
    this.fileDropZoneAjax = fileDropZoneAjax;
  }

  public List<UploadItem> getUploadItems() {
    return uploadItems;
  }
}

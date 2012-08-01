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

package org.apache.myfaces.tobago.example.test;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Upload {

  private static final Logger LOG = LoggerFactory.getLogger(Upload.class);

  private FileItem file;

  public String upload() {
    if (file == null) {
      FacesContext.getCurrentInstance().addMessage(
          null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No UploadItem found!", null));
      return null;
    }
    LOG.info("type=" + file.getContentType());
    LOG.info("size=" + file.get().length);
    LOG.info("name=" + file.getName());
    return "/tc/file/file.xhtml";
  }
  
  public FileItem getFile() {
    return file;
  }

  public void setFile(FileItem file) {
    this.file = file;
  }
}

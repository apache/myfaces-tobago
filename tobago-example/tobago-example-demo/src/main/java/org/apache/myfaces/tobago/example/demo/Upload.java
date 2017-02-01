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

import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Upload {

  private static final Logger LOG = LoggerFactory.getLogger(Upload.class);

  private FileItem file1;
  private FileItem file2;
  private FileItem[] fileMulti;
  private FileItem[] fileAjax;
  private FileItem[] fileDnd;

  private List<UploadItem> list = new ArrayList<UploadItem>();

  public String upload() {
   upload(file1);
   upload(file2);
   upload(fileMulti);
   upload(fileAjax);
   upload(fileDnd);
      return null;
    }

  public void upload(FileItem[] files) {
    if (files != null) {
      for (FileItem file : files) {
        upload(file);
      }
    }
  }

  public void upload(FileItem file) {
    LOG.info("checking file item");
    if (file == null || file.get().length == 0) {
      return;
    }
    LOG.info("type=" + file.getContentType());
    LOG.info("size=" + file.get().length);
    String name = file.getName();
    final int pos = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
    if (pos >= 0) {
      // some old browsers send the name with path.
      // modern browsers doesn't because of security reasons.
      name = name.substring(pos + 1);
    }
    LOG.info("name=" + name);
    list.add(new UploadItem(name, file.get().length, file.getContentType()));
  }

  public FileItem getFile1() {
    return file1;
  }

  public void setFile1(FileItem file1) {
    this.file1 = file1;
  }

  public FileItem getFile2() {
    return file2;
  }

  public void setFile2(FileItem file2) {
    this.file2 = file2;
  }

  public FileItem[] getFileMulti() {
    return fileMulti;
  }

  public void setFileMulti(FileItem[] fileMulti) {
    this.fileMulti = fileMulti;
  }

  public FileItem[] getFileAjax() {
    return fileAjax;
  }

  public void setFileAjax(FileItem[] fileAjax) {
    this.fileAjax = fileAjax;
  }

  public FileItem[] getFileDnd() {
    return fileDnd;
  }

  public void setFileDnd(FileItem[] fileDnd) {
    this.fileDnd = fileDnd;
  }

  public List<UploadItem> getList() {
    return list;
  }
}

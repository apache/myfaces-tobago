/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created Jan 20, 2003 at 11:33:58 AM.
  * $Id$
  */
package com.atanion.tobago.event;

import org.apache.commons.fileupload.FileItem;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

public class UploadEvent extends TobagoActionEvent{

// ///////////////////////////////////////////// constant

  public static final String CMD_ADD    = "add";
  public static final String CMD_DELETE = "delete";

// ///////////////////////////////////////////// attribute

  FileItem[] files;

// ///////////////////////////////////////////// constructor
  public UploadEvent(UIComponent source, String command, FileItem[] items) {
    super(source, command);
    files = items;
  }
// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter
  public FileItem[] getFiles() {
    return files;
  }

  public void setFiles(FileItem[] files) {
    this.files = files;
  }
}


/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 13.01.2003 at 17:41:19.
  * $Id$
  */
package com.atanion.tobago.component;

import com.atanion.tobago.event.UploadEvent;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.ArrayList;
import java.util.List;

public class UIFile extends UIInput {

// ///////////////////////////////////////////// constant

  private static final Log log = LogFactory.getLog(UIFile.class);

  public static final String COMPONENT_TYPE="com.atanion.tobago.File";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }

  protected Object checkValue(Object currentValue) {

    if (currentValue == null) {
      log.error("currentValue is null: '" + currentValue + "'");
      currentValue = emergencyValue();
    }

    if (! (currentValue instanceof List)) {
      log.error("currentValue is not valid: '" + currentValue + "'");
      log.error("currentValue is not of type '"
          + List.class.getName() + "': '"
          + currentValue.getClass().getName() + "'");
      currentValue = emergencyValue();
    }

    return currentValue;
  }

  protected Object emergencyValue() {
    return new ArrayList();
  }

  public boolean broadcast(FacesEvent event, PhaseId phaseId)
      throws AbortProcessingException {

    // fixme: check specification for implement this method!!!

    if (event instanceof UploadEvent
        && phaseId.equals(PhaseId.APPLY_REQUEST_VALUES)) {
      UploadEvent uploadEvent = (UploadEvent) event;
      if (uploadEvent.getActionCommand().equals(UploadEvent.CMD_ADD)){
        FileItem[] items = uploadEvent.getFiles();
        addFiles(items);
        FacesContext.getCurrentInstance().renderResponse();
        return false;
      }
      else if (uploadEvent.getActionCommand().equals(UploadEvent.CMD_DELETE)){
        FileItem[] items = uploadEvent.getFiles();
        deleteFiles(items);
        FacesContext.getCurrentInstance().renderResponse();
        return false;
      }
    }

    return true;
  }

  public void addFiles(FileItem[] items){
    if (items != null) {
      for (int i = 0; i < items.length; i++) {
        addFile(items[i]);
      }
    }
  }

  public void addFile(FileItem item) {
    List list = getList();
    list.add(item);
  }

  private void deleteFiles(FileItem[] items){
    for (int i = 0; i < items.length; i++) {
      deleteFile(items[i]);
    }
  }

  private void deleteFile(FileItem item) {
    List list = getList();
    list.remove(item);
  }

  private List getList() {
    List list = (List) getValue();
    if (list == null) {
      list = new ArrayList();
      setValue(list);
    }
    return list;
  }

// ///////////////////////////////////////////// bean getter + setter

}


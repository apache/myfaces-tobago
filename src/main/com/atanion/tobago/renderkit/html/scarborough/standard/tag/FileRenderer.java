/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIFile;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.event.UploadEvent;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.webapp.TobagoMultipartFormdataRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileRenderer extends RendererBase implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(FileRenderer.class);

  public static final String ADD_BUTTON    = "/add";
  public static final String DELETE_BUTTON = "/delete/";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UIFile uiFile = (UIFile) component;

    boolean valid = true;
    TobagoMultipartFormdataRequest request =
        (TobagoMultipartFormdataRequest) facesContext.getExternalContext().getRequest();

    String actionId = ComponentUtil.findPage(uiFile).getActionId();
    UploadEvent event;
    boolean addEvent = (actionId != null
        && actionId.equals(uiFile.getClientId(facesContext) + ADD_BUTTON));
    FileItem item = request.getFileItem(uiFile.getClientId(facesContext));
    FileItem[] items = null;
    //TODO: handle multiple uploads
    if (item != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Uploaded file name : \"" + item.getName() +
            "\"  size = " + item.getSize());
      }

      if (item.getName().length() > 0 && item.getSize() > 0) {
        items = new FileItem[1];
        items[0] = item;
      }
      if (addEvent) {
        event = new UploadEvent(uiFile, UploadEvent.CMD_ADD, items);
        LOG.error(
            "This method contains non-active code! Please implement it correctly"); //fixme jsfbeta
//        facesContext.addFacesEvent(event);
      }
      else {
        uiFile.addFiles(items);
      }
    }
    else {
      valid = false;
      LOG.error("No File Item found!");
    }

    List list = (List) uiFile.getValue();
    if (list != null) {
      event = null;
      ArrayList toDelete = new ArrayList();
      for (int i=list.size()-1; i>-1;i--) {
        if (actionId != null && actionId.equals(uiFile.getClientId(facesContext) + DELETE_BUTTON + i)) {
          toDelete.add(list.get(i));
        }
      }
      if (toDelete.size() > 0) {
        event = new UploadEvent(uiFile, UploadEvent.CMD_DELETE,
            (FileItem[])toDelete.toArray(new FileItem[toDelete.size()]));
        LOG.error(
            "This method contains non-active code! Please implement it correctly"); //fixme jsfbeta
//        facesContext.addFacesEvent(event);
      }
    }
    uiFile.setValid(valid);
  }


  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIFile component =   (UIFile) uiComponent;
    String clientId = component.getClientId(facesContext);

    ResponseWriter writer = facesContext.getResponseWriter();

    boolean isInline = ComponentUtil.isInline(component);

    String uploadImage = TobagoResource.getImage(facesContext, "upload.gif");
    String removeImage = TobagoResource.getImage(facesContext, "remove.gif");
    String onClickStart = "submitAction('"
        + ComponentUtil.findPage(component).getFormId(facesContext) + "', '" ;
    boolean multiple= false;
    Boolean multi = (Boolean)
        component.getAttributes().get(TobagoConstants.ATTR_MULTIPLE_FILES);
    if (multi != null && multi.booleanValue()) {
      multiple = true;


      writer.startElement("table", null);
      writer.writeAttribute("table", "", null);

      writer.startElement("tr", null);

      writer.startElement("td", null);
      writer.writeAttribute("colspan", "3", null);
    }
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    if (label != null) {
      writer.writeText("", null);
      RenderUtil.encode(facesContext, label);
    }

    writer.startElement("input", null);
    writer.writeAttribute("type", "file", null);
    // todo: use tobago standard class names
    writer.writeAttribute("class", "file", null);
    if (isInline) {
      writer.writeAttribute("style", "float: left;", null);
    }
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    if (ComponentUtil.isDisabled(component)) {
      writer.writeAttribute("readonly", "readonly", null);
    }
    writer.endElement("input");

    if (multiple) {
      writer.writeText(" ", null);

      writer.startElement("button", component);
      writer.writeAttribute("value", "Upload", null);
      writer.writeAttribute("type", "button", null);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
      writer.writeAttribute("name",
          clientId + FileRenderer.ADD_BUTTON, null);
      writer.writeAttribute("onclick", onClickStart +
          clientId + FileRenderer.ADD_BUTTON + "')",
          null);

      writer.startElement("img", null);
    // todo: use tobago standard class names
      writer.writeAttribute("class", "button", null);
      writer.writeAttribute("src", uploadImage, null);
      writer.writeAttribute("alt", "", null);
      writer.endElement("img");

      writer.write("&nbsp;");
      writer.writeText("Upload", null);

      writer.endElement("button");

      writer.endElement("td");
      writer.endElement("tr");

      List list = (List) component.getValue();
      if (list != null) {

        writer.startElement("tr", null);

        writer.startElement("td", null);
        writer.writeAttribute("colspan", "3", null);

        writer.startElement("br", null);
        writer.writeAttribute("style", "line-height: 10px;", null);

        writer.startElement("b", null);
        writer.writeText("Uploaded Files:", null);
        writer.endElement("b");

        writer.endElement("td");
        writer.endElement("tr");

        writer.startElement("tr", null);

        writer.startElement("td", null);
        writer.startElement("b", null);
        writer.writeText("Filename", null);
        writer.endElement("b");
        writer.endElement("td");

        writer.startElement("td", null);
        writer.startElement("b", null);
        writer.writeText("Size", null);
        writer.endElement("b");
        writer.endElement("td");

        writer.startElement("td", null);
        writer.startElement("b", null);
        writer.write("&nbsp;");
        writer.endElement("b");
        writer.endElement("td");

        writer.endElement("tr");

        for (int i=0; i<list.size();i++) {
          FileItem fileItem = (FileItem) list.get(i);

          writer.startElement("tr", null);

          writer.startElement("td", null);
          writer.writeAttribute("class", "filelist-td", null);
          writer.writeText(fileItem.getName(), null);
          writer.endElement("td");

          writer.startElement("td", null);
          writer.writeAttribute("class", "filelist-td", null);
          writer.writeText(Long.toString(fileItem.getSize()), null);
          writer.endElement("td");

          writer.startElement("td", null);

          String name = clientId + FileRenderer.DELETE_BUTTON + i;
          writer.startElement("button", component);
          writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
          writer.writeAttribute("type", "submit", null);
          writer.writeAttribute("value", "Remove", null);
          writer.writeAttribute("name", name, null);
          writer.writeAttribute("onclick", onClickStart + name + "')", null);


          writer.startElement("img", null);
          // todo: use tobago standard class names
          writer.writeAttribute("class", "button", null);
          writer.writeAttribute("src", removeImage, null);
          writer.write("&nbsp;");
          writer.writeText("Remove", null);
          writer.endElement("img");

          writer.endElement("button");

          writer.endElement("td");
          writer.endElement("tr");
        }

      }
      writer.endElement("table");
    }
    if (!isInline) {
      writer.startElement("br", null);
      writer.writeAttribute("style", "clear: left; line-height: 0px", null);
      writer.endElement("br");
    }
  }
// ///////////////////////////////////////////// bean getter + setter

}


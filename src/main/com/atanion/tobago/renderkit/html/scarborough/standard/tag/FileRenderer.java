/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.webapp.TobagoMultipartFormdataRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class FileRenderer extends InputRendererBase implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(FileRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getComponentExtraWidth(
      FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(TobagoConstants.FACET_LABEL) != null) {
      int labelWidht = LayoutUtil.getLabelWidth(component);
      space += labelWidht != 0
          ? labelWidht : getLabelWidth(facesContext, component);
    }

    return space;
  }

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UIInput input = (UIInput) component;

    TobagoMultipartFormdataRequest request = null;
    Object requestObject = facesContext.getExternalContext().getRequest();
    if (requestObject instanceof TobagoMultipartFormdataRequest) {
      request = (TobagoMultipartFormdataRequest) requestObject;
    } else if (requestObject instanceof HttpServletRequestWrapper) {
      ServletRequest wrappedRequest
          = ((HttpServletRequestWrapper) requestObject).getRequest();
      if (wrappedRequest instanceof TobagoMultipartFormdataRequest) {
        request = (TobagoMultipartFormdataRequest) wrappedRequest;
      }
    }
    if (request == null) {
      throw new FacesException(
          "Cannot find a TobagoMultipartFormdataRequest. "
          + "Please check that you have confitured a "
          + "TobagoMultipartFormdataFilter in your web.xml.");
    }

    FileItem item = request.getFileItem(input.getClientId(facesContext));

    if (LOG.isDebugEnabled()) {
      LOG.debug(
          "Uploaded file name : \"" + item.getName() +
          "\"  size = " + item.getSize());
    }
    input.setSubmittedValue(item);
    input.setValid(true);
  }

  public Object getConvertedValue(
      FacesContext context, UIComponent component, Object submittedValue)
      throws ConverterException {
    return submittedValue;
  }

  public void encodeDirectEnd(
      FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIInput component = (UIInput) uiComponent;
    String clientId = component.getClientId(facesContext);

    ResponseWriter writer = facesContext.getResponseWriter();

    boolean isInline = ComponentUtil.isInline(component);

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    if (label != null) {
      writer.writeText("", null);
      RenderUtil.encode(facesContext, label);
    }

    writer.startElement("input", component);
    writer.writeAttribute("type", "file", null);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
//  fixme?  if (isInline) {
//      writer.writeAttribute("style", "float: left;", null);
//    }
    writer.writeAttribute("name", clientId, null);
    writer.writeAttribute("id", clientId, null);
    if (ComponentUtil.isDisabled(component)) {
      writer.writeAttribute("readonly", "readonly", null);
    }
    writer.endElement("input");

    if (!isInline) {
      writer.startElement("br", null);
      writer.writeAttribute("style", "clear: left; line-height: 0px", null);
      writer.endElement("br");
    }
  }
// ///////////////////////////////////////////// bean getter + setter

}


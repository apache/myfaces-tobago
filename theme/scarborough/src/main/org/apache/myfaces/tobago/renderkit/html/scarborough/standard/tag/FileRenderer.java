/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoMultipartFormdataRequest;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import static org.apache.myfaces.tobago.TobagoConstants.*;

public class FileRenderer extends InputRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(FileRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }

  public int getComponentExtraWidth(
      FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(FACET_LABEL) != null) {
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
    // TODO PortletRequest ??
    if (request == null) {
      // should not be possible, because of the check in UIPage
      LOG.error("Can't process multipart/form-data without TobagoRequest. " +
          "Please check the web.xml and define a TobagoMultipartFormdataFilter. " +
          "See documentation for <tc:file>");
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

  public void encodeEndTobago(
      FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIInput component = (UIInput) uiComponent;
    String clientId = component.getClientId(facesContext);

    TobagoResponseWriter writer
        = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("input", component);
    writer.writeAttribute("type", "file", null);
    writer.writeComponentClass();
    if (!ClientProperties.getInstance(facesContext).getUserAgent().isMozilla()) {
      writer.writeAttribute("style", null, ATTR_STYLE);
    }
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("readonly",
        ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED));
    writer.writeAttribute("title", null, ATTR_TIP);
    writer.endElement("input");

  }
// ///////////////////////////////////////////// bean getter + setter

}


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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.commons.fileupload.FileItem;
import org.apache.myfaces.tobago.internal.component.AbstractUIFile;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.webapp.TobagoMultipartFormdataRequest;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class FileRenderer extends InputRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(FileRenderer.class);

  public void prepareRender(final FacesContext facesContext, final UIComponent component) {
    super.prepareRender(facesContext, component);
    FacesContextUtils.setEnctype(facesContext, "multipart/form-data");
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void decode(final FacesContext facesContext, final UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    final AbstractUIFile input = (AbstractUIFile) component;

    TobagoMultipartFormdataRequest request = null;
    final Object requestObject = facesContext.getExternalContext().getRequest();
    if (requestObject instanceof TobagoMultipartFormdataRequest) {
      request = (TobagoMultipartFormdataRequest) requestObject;
    } else if (requestObject instanceof HttpServletRequestWrapper) {
      final ServletRequest wrappedRequest
          = ((HttpServletRequestWrapper) requestObject).getRequest();
      if (wrappedRequest instanceof TobagoMultipartFormdataRequest) {
        request = (TobagoMultipartFormdataRequest) wrappedRequest;
      }
    }
    // TODO PortletRequest ??
    if (request == null) {
      // should not be possible, because of the check in UIPage
      LOG.error("Can't process multipart/form-data without TobagoRequest. "
          + "Please check the web.xml and define a TobagoMultipartFormdataFilter. "
          + "See documentation for <tc:file>");
    } else {

      final FileItem item = request.getFileItem(input.getClientId(facesContext));
/*
TODO: Using Servlet 3.0 file upload...
      try {
        final Part part = request.getPart(input.getClientId(facesContext));
        LOG.info("content type: " + part.getContentType());
        LOG.info("name:         " + part.getName());
        LOG.info("size:         " + part.getSize());
      } catch (IOException e) {
        e.printStackTrace();
      } catch (ServletException e) {
        e.printStackTrace();
      }
*/

      if (LOG.isDebugEnabled()) {
        LOG.debug("Uploaded file name : \"" + item.getName()
            + "\"  size = " + item.getSize());
      }
      input.setSubmittedValue(item);
      //TODO remove this
      input.setValid(true);
    }
  }

  protected void encodeBeginField(FacesContext facesContext, UIComponent component) throws IOException {

    final AbstractUIFile file = (AbstractUIFile) component;
    final String clientId = file.getClientId(facesContext);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(file), file.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, file);
    writer.writeStyleAttribute(file.getStyle());

    // visible fake input for a pretty look
    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "pretty");
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT);
    writer.writeClassAttribute(Classes.create(file, "pretty"));
    // TODO Focus
    //HtmlRendererUtils.renderFocus(clientId, file.isFocus(), ComponentUtils.isError(file), facesContext, writer);
    writer.endElement(HtmlElements.DIV);

    // invisible file input
    writer.startElement(HtmlElements.INPUT);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "real");
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.FILE);
    writer.writeClassAttribute(Classes.create(file, "real"));
    writer.writeNameAttribute(clientId);
    // readonly seems not making sense in browsers.
    writer.writeAttribute(HtmlAttributes.DISABLED, file.isDisabled() || file.isReadonly());
    writer.writeAttribute(HtmlAttributes.READONLY, file.isReadonly());
    writer.writeAttribute(HtmlAttributes.REQUIRED, file.isRequired());
    writer.writeAttribute(HtmlAttributes.SIZE, "1024", false);
    final Integer tabIndex = file.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, file);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.endElement(HtmlElements.INPUT);
  }

  protected void encodeEndField(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }
}

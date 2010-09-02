package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.fileupload.FileItem;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIFileInput;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.webapp.TobagoMultipartFormdataRequest;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
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

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).setEnctype("multipart/form-data");
    }
  }

  public boolean getRendersChildren() {
    return true;
  }

  public void decode(FacesContext facesContext, UIComponent component) {
    if (!(component instanceof UIFileInput)) {
      LOG.error("Wrong type: Need " + UIFileInput.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    UIFileInput input = (UIFileInput) component;

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
      LOG.error("Can't process multipart/form-data without TobagoRequest. "
          + "Please check the web.xml and define a TobagoMultipartFormdataFilter. "
          + "See documentation for <tc:file>");
    } else {

      FileItem item = request.getFileItem(input.getClientId(facesContext));

      if (LOG.isDebugEnabled()) {
        LOG.debug("Uploaded file name : \"" + item.getName()
            + "\"  size = " + item.getSize());
      }
      input.setSubmittedValue(item);
      //TODO remove this
      input.setValid(true);
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UIFileInput)) {
      LOG.error("Wrong type: Need " + UIFileInput.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    UIFileInput input = (UIFileInput) component;
    String clientId = input.getClientId(facesContext);

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.INPUT, input);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.FILE, false);
    writer.writeClassAttribute(Classes.create(input));
    Style style = new Style(facesContext, input);
    writer.writeStyleAttribute(style);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.READONLY, ComponentUtils.getBooleanAttribute(input, Attributes.DISABLED));
    Integer tabIndex = input.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    HtmlRendererUtils.renderTip(input, writer);
    if (ClientProperties.getInstance(facesContext).getUserAgent().isMsie6()) {
      writer.writeAttribute(HtmlAttributes.ONKEYDOWN, "this.blur();return false;", false);
      writer.writeAttribute("oncontextmenu", "return false;", false);
    }
    writer.endElement(HtmlElements.INPUT);
  }
}


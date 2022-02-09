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

import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIFile;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.HttpPartWrapper;
import org.apache.myfaces.tobago.internal.util.PartUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.validator.FileItemValidator;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileRenderer extends InputRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(FileRenderer.class);

  public void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {
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

    final AbstractUIFile file = (AbstractUIFile) component;
    final boolean multiple = file.isMultiple() && !file.isRequired();
    final Object request = facesContext.getExternalContext().getRequest();
    if (request instanceof HttpServletRequest) {
      try {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (multiple) {
          final List<Part> parts = new ArrayList<Part>();
          for (final Part part : httpServletRequest.getParts()) {
            if (file.getClientId(facesContext).equals(part.getName())) {
              LOG.debug("Uploaded file '{}', size={}, type='{}'",
                  PartUtils.getSubmittedFileName(part), part.getSize(), part.getContentType());
              parts.add(new HttpPartWrapper(part));
            }
            file.setSubmittedValue(parts.toArray(new Part[0]));
          }
        } else {
          final Part part = httpServletRequest.getPart(file.getClientId(facesContext));
          final String submittedFileName = PartUtils.getSubmittedFileName(part);
          if (LOG.isDebugEnabled()) {
            LOG.debug("Uploaded file '{}', size={}, type='{}'",
                submittedFileName, part.getSize(), part.getContentType());
          }
          if (submittedFileName.length() > 0) {
            file.setSubmittedValue(new HttpPartWrapper(part));
          }
        }
      } catch (final Exception e) {
        LOG.error("", e);
        file.setValid(false);
      }
    } else { // todo: PortletRequest
      LOG.warn("Unsupported request type: " + request.getClass().getName());
    }
  }

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIFile file = (AbstractUIFile) component;
    final String clientId = file.getClientId(facesContext);
    final Style style = new Style(facesContext, file);
    final String accept = createAcceptFromValidators(file);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, file);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(getCssClasses(file, null));
    writeDataAttributes(facesContext, writer, file);
    writer.writeStyleAttribute(style);

    // visible fake input for a pretty look
    writeVisibleInput(facesContext, writer, file, clientId, style);

    // invisible file input
    writer.startElement(HtmlElements.INPUT, file);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, file.isMultiple());
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "real");
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.FILE, false);
    writer.writeAttribute(HtmlAttributes.ACCEPT, accept, true);
    writer.writeClassAttribute(getCssClasses(file, "real"));
    writer.writeNameAttribute(clientId);
    String multiFormat = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "tobago.file.multiFormat");
    writer.writeAttribute("data-tobago-file-multi-format", multiFormat, true);
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
    HtmlRendererUtils.renderCommandFacet(file, facesContext, writer);
    writer.endElement(HtmlElements.INPUT);

    writer.endElement(HtmlElements.DIV);
  }

  private String createAcceptFromValidators(final AbstractUIFile file) {
    final StringBuilder builder = new StringBuilder();
    for (Validator validator : file.getValidators()) {
      if (validator instanceof FileItemValidator) {
        final FileItemValidator fileItemValidator = (FileItemValidator) validator;
        for (final String contentType : fileItemValidator.getContentType()) {
          builder.append(",");
          builder.append(contentType);
        }
      }
    }
    if (builder.length() > 0) {
      return builder.substring(1);
    } else {
      return null;
    }
  }

  protected Classes getCssClasses(UIComponent component, String sub) {
    return Classes.create(component, sub);
  }

  protected void writeDataAttributes(FacesContext facesContext, TobagoResponseWriter writer, AbstractUIFile file)
      throws IOException {
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, file);
  }

  protected void writeVisibleInput(FacesContext facesContext, TobagoResponseWriter writer, AbstractUIFile file,
                                   String clientId, Style style) throws IOException {
    final Style inputStyle = new Style();
    final Measure prettyWidthSub = getPrettyWidthSub(facesContext, file);
    inputStyle.setWidth(style.getWidth().subtract(prettyWidthSub));
    writer.startElement(HtmlElements.INPUT, file);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "pretty");
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT, false);
    writer.writeClassAttribute(getCssClasses(file, "pretty"));
    writer.writeStyleAttribute(inputStyle);
    writer.writeAttribute(HtmlAttributes.DISABLED, true);
    if (!file.isDisabled() && !file.isReadonly()) {
      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, file.getPlaceholder(), true);
    }
    // TODO Focus
    //HtmlRendererUtils.renderFocus(clientId, file.isFocus(), ComponentUtils.isError(file), facesContext, writer);
    writer.endElement(HtmlElements.INPUT);
  }

  protected Measure getPrettyWidthSub(FacesContext facesContext, AbstractUIFile file) {
    return getResourceManager().getThemeMeasure(facesContext, file, "prettyWidthSub");
  }
}

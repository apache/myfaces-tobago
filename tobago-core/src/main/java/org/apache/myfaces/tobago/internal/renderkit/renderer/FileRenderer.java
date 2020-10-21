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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.component.AbstractUIFile;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.HttpPartWrapper;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.validator.FileItemValidator;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.validator.Validator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class FileRenderer<T extends AbstractUIFile>
    extends MessageLayoutRendererBase<T> implements ComponentSystemEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_FILE;
  }

  @Override
  public void processEvent(final ComponentSystemEvent event) {
    TobagoContext.getInstance(FacesContext.getCurrentInstance()).setEnctype("multipart/form-data");
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    final boolean multiple = component.isMultiple() && !component.isRequired();
    final Object request = facesContext.getExternalContext().getRequest();
    if (request instanceof HttpServletRequest) {
      try {
        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (multiple) {
          final List<Part> parts = new ArrayList<>();
          for (final Part part : httpServletRequest.getParts()) {
            if (component.getClientId(facesContext).equals(part.getName())) {
              LOG.debug("Uploaded file '{}', size={}, type='{}'",
                  part.getSubmittedFileName(), part.getSize(), part.getContentType());
              parts.add(new HttpPartWrapper(part));
            }
            component.setSubmittedValue(parts.toArray(new Part[0]));
          }
        } else {
          final Part part = httpServletRequest.getPart(component.getClientId(facesContext));
          final String submittedFileName = part.getSubmittedFileName();
          if (LOG.isDebugEnabled()) {
            LOG.debug("Uploaded file '{}', size={}, type='{}'",
                submittedFileName, part.getSize(), part.getContentType());
          }
          if (submittedFileName.length() > 0) {
            component.setSubmittedValue(new HttpPartWrapper(part));
          }
        }
      } catch (final Exception e) {
        LOG.error("", e);
        component.setValid(false);
      }
    } else { // todo: PortletRequest
      LOG.warn("Unsupported request type: " + request.getClass().getName());
    }

    decodeClientBehaviors(facesContext, component);
  }

  @Override
  protected void encodeAttributes(final FacesContext facesContext, final T component) throws IOException {
    final String placeholder = component.getPlaceholder();
    final String multiFormat = ResourceUtils.getString(facesContext, "file.selected");

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.writeAttribute(HtmlAttributes.PLACEHOLDER, placeholder, true);
    writer.writeAttribute(CustomAttributes.MULTI_FORMAT, multiFormat, true);
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {

    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final String accept = createAcceptFromValidators(component);
    final boolean multiple = component.isMultiple() && !component.isRequired();
    final boolean disabled = component.isDisabled();
    final boolean readonly = component.isReadonly();
    if (component.isMultiple() && component.isRequired()) {
      LOG.warn("Required multiple file upload is not supported."); //TODO TOBAGO-1930
    }

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
        BootstrapClass.FORM_FILE,
        TobagoClass.FILE.createMarkup(component.getMarkup()),
        component.getCustomClass(),
        BootstrapClass.FORM_CONTROL_PLAINTEXT);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, multiple);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.FILE);
    writer.writeAttribute(HtmlAttributes.ACCEPT, accept, true);
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.writeIdAttribute(fieldId);
    writer.writeClassAttribute(
        BootstrapClass.FORM_FILE_INPUT,
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(component)));
    writer.writeNameAttribute(clientId);
    // readonly seems not making sense in browsers.
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled || readonly);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.REQUIRED, component.isRequired());
    // TODO Focus
    //HtmlRendererUtils.renderFocus(clientId, file.isFocus(), ComponentUtils.isError(file), facesContext, writer);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.endElement(HtmlElements.INPUT);

    encodeBehavior(writer, facesContext, component);

    writer.startElement(HtmlElements.LABEL);
    writer.writeClassAttribute(BootstrapClass.FORM_FILE_LABEL);
    writer.writeAttribute(HtmlAttributes.FOR, fieldId, false);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(BootstrapClass.FORM_FILE_TEXT);
    writer.endElement(HtmlElements.SPAN);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(BootstrapClass.FORM_FILE_BUTTON);
    writer.startElement(HtmlElements.I);
    // TODO: define a name
    writer.writeAttribute(HtmlAttributes.TITLE, "Browse", false);
    writer.writeClassAttribute(Icons.FA, Icons.FOLDER_OPEN);
    writer.endElement(HtmlElements.I);
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.LABEL);
  }

  private String createAcceptFromValidators(final AbstractUIFile file) {
    final StringBuilder builder = new StringBuilder();
    for (final Validator validator : file.getValidators()) {
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

  @Override
  protected void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }
}

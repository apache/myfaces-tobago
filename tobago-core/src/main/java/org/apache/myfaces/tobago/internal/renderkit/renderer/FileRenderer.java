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

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.component.AbstractUIFile;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.HttpPartWrapper;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class FileRenderer<T extends AbstractUIFile>
    extends DecorationPositionRendererBase<T> implements ComponentSystemEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  protected boolean isOutputOnly(T component) {
    return component.isDisabled() || component.isReadonly();
  }

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
    if (isOutputOnly(component)) {
      return;
    }

    final boolean multiple = component.isMultiple() && !component.isRequired();
    final String clientId = component.getClientId(facesContext);
    final Object request = facesContext.getExternalContext().getRequest();
    if (request instanceof HttpServletRequest) {
      final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      final List<Part> parts = new ArrayList<>();
      try {
        for (final Part part : httpServletRequest.getParts()) {
          if (clientId.equals(part.getName())) {
            final String fileName = part.getSubmittedFileName();
            final String contentType = part.getContentType();
            LOG.debug("Uploaded file '{}', size={}, type='{}'.", fileName, part.getSize(), contentType);
            if (part.getSize() == 0) {
              LOG.debug("Ignoring empty file for clientId='{}'.", clientId);
            } else if (StringUtils.isBlank(fileName)) {
              LOG.warn("No fileName provided for clientId='{}'.", clientId);
            } else if (StringUtils.isBlank(contentType)) {
              LOG.warn("No contentType provided for clientId='{}'.", clientId);
            } else {
              parts.add(new HttpPartWrapper(part));
            }
            if (!multiple && parts.size() > 0) { // found one, and one is enough
              break;
            }
          }
        }
        if (multiple) {
          LOG.debug("Adding {} parts {}.", parts.size(), parts);
          component.setSubmittedValue(parts.toArray(new Part[0]));
        } else {
          if (parts.size() > 0) {
            LOG.debug("Adding one part {}.", parts.get(0));
            component.setSubmittedValue(parts.get(0));
          }
        }
      } catch (final Exception e) {
        LOG.error("clientId='" + clientId + "'", e);
        component.setValid(false);
      }
    } else {
      LOG.warn("Unsupported request type: " + request.getClass().getName());
    }

    decodeClientBehaviors(facesContext, component);
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {

    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final String accept = createAcceptFromValidators(component);
    final boolean multiple = component.isMultiple() && !component.isRequired();
    final boolean disabled = component.isDisabled();
    final boolean readonly = component.isReadonly();
    final Markup markup = component.getMarkup() != null ? component.getMarkup() : Markup.NULL;
    if (component.isMultiple() && component.isRequired()) {
      LOG.warn("Required multiple file upload is not supported."); //TODO TOBAGO-1930
    }

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
        BootstrapClass.INPUT_GROUP,
        component.getCustomClass());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, multiple);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.FILE);
    writer.writeAttribute(HtmlAttributes.ACCEPT, accept, true);
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.writeIdAttribute(fieldId);
    writer.writeClassAttribute(
        BootstrapClass.FORM_CONTROL,
        markup.contains(Markup.LARGE) ? BootstrapClass.FORM_CONTROL_LG : null,
        markup.contains(Markup.SMALL) ? BootstrapClass.FORM_CONTROL_SM : null,
        BootstrapClass.validationColor(ComponentUtils.getMaximumSeverity(component)));
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
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP_TEXT);
    writer.writeAttribute(HtmlAttributes.FOR, fieldId, false);
    writer.startElement(HtmlElements.SPAN);
    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(Icons.FOLDER2_OPEN);
    writer.endElement(HtmlElements.I);
    writer.endElement(HtmlElements.SPAN);
    writer.endElement(HtmlElements.LABEL);
  }

  private String createAcceptFromValidators(final AbstractUIFile file) {
    final StringBuilder builder = new StringBuilder();
    for (final Validator validator : file.getValidators()) {
      if (validator instanceof FileItemValidator) {
        final FileItemValidator fileItemValidator = (FileItemValidator) validator;
        final String[] contentTypes = fileItemValidator.getContentType();
        if (contentTypes != null) {
          for (final String contentType : contentTypes) {
            builder.append(",");
            builder.append(contentType);
          }
        }
      }
    }
    if (builder.length() > 0) {
      return builder.substring(1);
    } else {
      return null;
    }
  }

  private long createMaxSizeFromValidators(final AbstractUIFile file) {
    long maxSize = Long.MAX_VALUE;
    for (final Validator validator : file.getValidators()) {
      if (validator instanceof FileItemValidator) {
        final FileItemValidator fileItemValidator = (FileItemValidator) validator;
        maxSize = Long.min(maxSize, fileItemValidator.getMaxSize());
      }
    }
    return maxSize != Long.MAX_VALUE ? maxSize : 0;
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.TOBAGO_PROGRESS);
    writer.writeClassAttribute(BootstrapClass.PROGRESS, BootstrapClass.D_NONE);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.PROGRESS_BAR);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PROGRESSBAR.toString(), false);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.TOBAGO_PROGRESS);
  }

  @Override
  protected void writeAdditionalAttributes(
      final FacesContext facesContext, final TobagoResponseWriter writer, final T component)
      throws IOException {
    super.writeAdditionalAttributes(facesContext, writer, component);
    final String dropZone = component.getDropZone();
    if (dropZone != null) {
      final String forId = ComponentUtils.evaluateClientId(facesContext, component, dropZone);
      writer.writeAttribute(CustomAttributes.DROP_ZONE, forId, true);
    }
    final long maxSize = createMaxSizeFromValidators(component);
    if (maxSize > 0) {
      writer.writeAttribute(CustomAttributes.MAX_SIZE, maxSize);
      final Locale locale = facesContext.getViewRoot().getLocale();
      final MessageFormat maxSizeMessage
          = new MessageFormat(ResourceUtils.getString(facesContext, "file.maxSizeMessage"), locale);
      writer.writeAttribute(CustomAttributes.MAX_SIZE_MESSAGE, maxSizeMessage.format(new Object[]{maxSize}), true);
    }
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }
}

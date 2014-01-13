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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIMenuBar;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.layout.LayoutContext;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.MimeTypeUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.portlet.PortletUtils;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.EncodeUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.Secret;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.portlet.MimeResponse;
import javax.portlet.ResourceURL;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public class PageRenderer extends PageRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(PageRenderer.class);

  private static final String CLIENT_DEBUG_SEVERITY = "clientDebugSeverity";
  private static final String LAST_FOCUS_ID = "lastFocusId";

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    super.decode(facesContext, component);
    final String clientId = component.getClientId(facesContext);
    final ExternalContext externalContext = facesContext.getExternalContext();

    // severity
    final String severity
        = externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity");
    if (severity != null) {
      externalContext.getRequestMap().put(CLIENT_DEBUG_SEVERITY, severity);
    }

    // last focus
    final String lastFocusId =
        externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + LAST_FOCUS_ID);
    if (lastFocusId != null) {
      FacesContextUtils.setFocusId(facesContext, lastFocusId);
    }

    // scrollbar weight
    final String name = clientId + ComponentUtils.SUB_SEPARATOR + "scrollbarWeight";
    String value = null;
    try {
      value = facesContext.getExternalContext().getRequestParameterMap().get(name);
      if (StringUtils.isNotBlank(value)) {
        final StringTokenizer tokenizer = new StringTokenizer(value, ";");
        final Measure vertical = Measure.valueOf(tokenizer.nextToken());
        final Measure horizontal = Measure.valueOf(tokenizer.nextToken());
        if (vertical.greaterThan(Measure.valueOf(30)) || vertical.lessThan(Measure.valueOf(3))
            || horizontal.greaterThan(Measure.valueOf(30)) || horizontal.lessThan(Measure.valueOf(3))) {
          LOG.error("Ignoring strange values: vertical=" + vertical + " horizontal=" + horizontal);
        } else {
          final ClientProperties client = VariableResolverUtils.resolveClientProperties(facesContext);
          client.setVerticalScrollbarWeight(vertical);
          client.setHorizontalScrollbarWeight(horizontal);
        }
      }
    } catch (final Exception e) {
      LOG.error("Error in decoding '" + name + "': value='" + value + "'", e);
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIPage page = (UIPage) component;
    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);

    // invoke prepareRender
    EncodeUtils.prepareRendererAll(facesContext, page);

    final LayoutContext layoutContext = new LayoutContext(page);
    layoutContext.layout();
    if (FacesContextUtils.getFocusId(facesContext) == null && !StringUtils.isBlank(page.getFocusId())) {
      FacesContextUtils.setFocusId(facesContext, page.getFocusId());
    }
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    // reset responseWriter and render page
    facesContext.setResponseWriter(writer);

    ResponseUtils.ensureNoCacheHeader(facesContext);

    ResponseUtils.ensureContentSecurityPolicyHeader(facesContext, tobagoConfig.getContentSecurityPolicy());

    if (LOG.isDebugEnabled()) {
      for (final Object o : page.getAttributes().entrySet()) {
        final Map.Entry entry = (Map.Entry) o;
        LOG.debug("*** '" + entry.getKey() + "' -> '" + entry.getValue() + "'");
      }
    }

    final ExternalContext externalContext = facesContext.getExternalContext();
    final Object response = externalContext.getResponse();
    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();
    final String viewId = facesContext.getViewRoot().getViewId();
    final String formAction = externalContext.encodeActionURL(viewHandler.getActionURL(facesContext, viewId));
    final String partialAction;
    final boolean portlet = PortletUtils.isPortletApiAvailable() && response instanceof MimeResponse;
    if (portlet) {
      final MimeResponse mimeResponse = (MimeResponse) response;
      final ResourceURL resourceURL = mimeResponse.createResourceURL();
      partialAction = externalContext.encodeResourceURL(resourceURL.toString());
    } else {
      partialAction = null;
    }

    final String contentType = writer.getContentTypeWithCharSet();
    ResponseUtils.ensureContentTypeHeader(facesContext, contentType);
    final String clientId = page.getClientId(facesContext);
    final ClientProperties client = VariableResolverUtils.resolveClientProperties(facesContext);
    final ProjectStage projectStage = tobagoConfig.getProjectStage();
    final boolean developmentMode = projectStage == ProjectStage.Development;
    final boolean debugMode = client.isDebugMode() || developmentMode;
    final boolean productionMode = !debugMode && projectStage == ProjectStage.Production;
    int clientLogSeverity = 2;
    if (debugMode) {
      final String severity = (String) externalContext.getRequestMap().get(CLIENT_DEBUG_SEVERITY);
      if (LOG.isDebugEnabled()) {
        LOG.debug("get " + CLIENT_DEBUG_SEVERITY + " = " + severity);
      }
      if (severity != null) {
        try {
          int index = severity.indexOf(';');
          if (index == -1) {
            index = severity.length();
          }
          clientLogSeverity = Integer.parseInt(severity.substring(0, index));
        } catch (final NumberFormatException e) {
          // ignore; use default
        }
      }
    }
    final boolean preventFrameAttacks = tobagoConfig.isPreventFrameAttacks();

    if (!FacesContextUtils.isAjax(facesContext)) {
      final String title = (String) page.getAttributes().get(Attributes.LABEL);

      writer.startElement(HtmlElements.HEAD, null);

      // meta tags

      // this is needed, because websphere 6.0? ignores the setting of the content type on the response
      writer.startElement(HtmlElements.META, null);
      writer.writeAttribute(HtmlAttributes.HTTP_EQUIV, "Content-Type", false);
      writer.writeAttribute(HtmlAttributes.CONTENT, contentType, false);
      writer.endElement(HtmlElements.META);

      // title
      writer.startElement(HtmlElements.TITLE, null);
      writer.writeText(title != null ? title : "");
      writer.endElement(HtmlElements.TITLE);
      final Theme theme = client.getTheme();

      // style files
      for (final String styleFile : theme.getStyleResources(productionMode)) {
        writeStyle(facesContext, writer, styleFile);
      }

      for (final String styleFile : FacesContextUtils.getStyleFiles(facesContext)) {
        writeStyle(facesContext, writer, styleFile);
      }

      if (!productionMode) {
        checkDuplicates(theme.getStyleResources(productionMode), FacesContextUtils.getStyleFiles(facesContext));
      }

      String icon = page.getApplicationIcon();
      if (icon != null) {
        // XXX unify with image renderer
        if (ResourceManagerUtils.isAbsoluteResource(icon)) {
          // absolute Path to image : nothing to do
        } else {
          icon = ResourceManagerUtils.getImageWithPath(facesContext, icon);
        }

        writer.startElement(HtmlElements.LINK, null);
        if (icon.endsWith(".ico")) {
          writer.writeAttribute(HtmlAttributes.REL, "shortcut icon", false);
          writer.writeAttribute(HtmlAttributes.HREF, icon, false);
        } else {
          // XXX IE only supports ICO files for favicons
          writer.writeAttribute(HtmlAttributes.REL, "icon", false);
          writer.writeAttribute(HtmlAttributes.TYPE, MimeTypeUtils.getMimeTypeForFile(icon), false);
          writer.writeAttribute(HtmlAttributes.HREF, icon, false);
        }
        writer.endElement(HtmlElements.LINK);
      }

      // style sniplets
      final Set<String> styleBlocks = FacesContextUtils.getStyleBlocks(facesContext);
      if (styleBlocks.size() > 0) {
        writer.startElement(HtmlElements.STYLE, null);
        writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
        for (final String cssBlock : styleBlocks) {
          writer.write(cssBlock);
        }
        writer.endElement(HtmlElements.STYLE);
      }

      // render remaining script tags
      for (final String scriptFile : theme.getScriptResources(productionMode)) {
        encodeScript(facesContext, writer, scriptFile);
      }

      for (final String scriptFile : FacesContextUtils.getScriptFiles(facesContext)) {
        encodeScript(facesContext, writer, scriptFile);
      }

      if (!productionMode) {
        checkDuplicates(theme.getScriptResources(productionMode), FacesContextUtils.getScriptFiles(facesContext));
      }

      writer.startJavascript();
      // onload script
      writeEventFunction(writer, FacesContextUtils.getOnloadScripts(facesContext), "load", false);

      // onunload script
      writeEventFunction(writer, FacesContextUtils.getOnunloadScripts(facesContext), "unload", false);

      // onexit script
      writeEventFunction(writer, FacesContextUtils.getOnexitScripts(facesContext), "exit", false);

      writeEventFunction(writer, FacesContextUtils.getOnsubmitScripts(facesContext), "submit", true);

      int debugCounter = 0;
      for (final String scriptBlock : FacesContextUtils.getScriptBlocks(facesContext)) {

        if (LOG.isDebugEnabled()) {
          LOG.debug("write scriptblock " + ++debugCounter + " :\n" + scriptBlock);
        }
        writer.write(scriptBlock);
        writer.write('\n');
      }
      writer.endJavascript();
      writer.endElement(HtmlElements.HEAD);
    }

    if (portlet) {
      writer.startElement(HtmlElements.DIV, page);
      writer.writeClassAttribute(Classes.create(page, Markup.PORTLET));
    } else {
      writer.startElement(HtmlElements.BODY, page);
      writer.writeClassAttribute(Classes.create(page));
    }
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, page);
    HtmlRendererUtils.renderCommandFacet(page, facesContext, writer);

    writer.startElement(HtmlElements.FORM, page);
    if (preventFrameAttacks && !FacesContextUtils.isAjax(facesContext)) {
      writer.writeClassAttribute(Classes.create(page, "preventFrameAttacks", Markup.NULL));
    }
    writer.writeAttribute(HtmlAttributes.ACTION, formAction, true);
    if (partialAction != null) {
      writer.writeAttribute(DataAttributes.PARTIAL_ACTION, partialAction, true);
    }
    LOG.info("partial action = " + partialAction);
    writer.writeIdAttribute(page.getFormId(facesContext));
    writer.writeAttribute(HtmlAttributes.METHOD, getMethod(page), false);
    final String enctype = FacesContextUtils.getEnctype(facesContext);
    if (enctype != null) {
      writer.writeAttribute(HtmlAttributes.ENCTYPE, enctype, false);
    }
    // TODO: enable configuration of  'accept-charset'
    writer.writeAttribute(HtmlAttributes.ACCEPT_CHARSET, AbstractUIPage.FORM_ACCEPT_CHARSET, false);
    // TODO evaluate 'accept' attribute usage
    //writer.writeAttribute(HtmlAttributes.ACCEPT, );
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "form-action");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "form-action");
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "context-path");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "context-path");
    writer.writeAttribute(HtmlAttributes.VALUE, externalContext.getRequestContextPath(), true);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "action-position");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "action-position");
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "form-clientDimension");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "form-clientDimension");
    writer.endElement(HtmlElements.INPUT);

    final boolean calculateScrollbarWeight =
        client.getVerticalScrollbarWeight() == null || client.getHorizontalScrollbarWeight() == null;

    if (calculateScrollbarWeight) {
      writer.startElement(HtmlElements.DIV, null);
      writer.writeClassAttribute(Classes.create(page, "scrollbarWeight", Markup.NULL));
      writer.startElement(HtmlElements.DIV, null);
      writer.endElement(HtmlElements.DIV);
      writer.endElement(HtmlElements.DIV);
    }

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "scrollbarWeight");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "scrollbarWeight");
    if (client.getVerticalScrollbarWeight() != null && client.getHorizontalScrollbarWeight() != null) {
      writer.writeAttribute(
          HtmlAttributes.VALUE,
          client.getVerticalScrollbarWeight().getPixel() + ";" + client.getHorizontalScrollbarWeight().getPixel(),
          false);
    }
    writer.endElement(HtmlElements.INPUT);

    if (TobagoConfig.getInstance(FacesContext.getCurrentInstance()).isCheckSessionSecret()) {
      Secret.encode(facesContext, writer);
    }

    if (debugMode) {
      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.VALUE, clientLogSeverity);
      writer.writeAttribute(HtmlAttributes.ID, clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity", false);
      writer.writeAttribute(HtmlAttributes.NAME, clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity", false);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.endElement(HtmlElements.INPUT);
    }

    if (component.getFacet("backButtonDetector") != null) {
      final UIComponent hidden = component.getFacet("backButtonDetector");
      RenderUtils.encode(facesContext, hidden);
    }

    //checkForCommandFacet(component, facesContext, writer);

// TODO: this is needed for the "BACK-BUTTON-PROBLEM"
// but may no longer needed
/*
    if (ViewHandlerImpl.USE_VIEW_MAP) {
      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", null);
      writer.writeNameAttribute(ViewHandlerImpl.PAGE_ID);
      writer.writeIdAttribute(ViewHandlerImpl.PAGE_ID);
      Object value = facesContext.getViewRoot().getAttributes().get(
          ViewHandlerImpl.PAGE_ID);
      writer.writeAttribute(HtmlAttributes.VALUE, (value != null ? value : ""), null);
      writer.endElement(HtmlElements.INPUT);
    }
*/

    final UIMenuBar menuBar = (UIMenuBar) page.getFacet(Facets.MENUBAR);
    if (menuBar != null) {
      menuBar.getAttributes().put(Attributes.PAGE_MENU, Boolean.TRUE);
      RenderUtils.encode(facesContext, menuBar);
    }
    // write the previously rendered page content
//    AbstractUILayoutBase.getLayout(component).encodeChildrenOfComponent(facesContext, component);

//    page.encodeLayoutBegin(facesContext);

    writer.startElement(HtmlElements.DIV, page);
    if (portlet) {
      writer.writeClassAttribute(Classes.create(page, "content", Markup.PORTLET));
    } else {
      writer.writeClassAttribute(Classes.create(page, "content"));
    }
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "content");
    final Style style = new Style(facesContext, page);
    // XXX position the div, so that the scrollable area is correct.
    // XXX better to take this fact into layout management.
    // XXX is also useful in boxes, etc.
    final Measure border = getBorderBottom(facesContext, page);
    style.setHeight(page.getCurrentHeight().subtract(border));
    style.setTop(border);
    writer.writeStyleAttribute(style);
  }

  private void checkDuplicates(final String[] resources, final Collection<String> files) {
    for (final String resource : resources) {
      if (files.contains(resource)) {
        throw new RuntimeException("The resource '" + resource + "' will be included twice! "
            + "The resource is in the theme list, and explicit in the page. "
            + "Please remove it from the page!");
      }
    }
  }

  private void writeStyle(final FacesContext facesContext, final TobagoResponseWriter writer, final String styleFile)
      throws IOException {
    final List<String> styles = ResourceManagerUtils.getStyles(facesContext, styleFile);
    for (final String styleString : styles) {
      if (styleString.length() > 0) {
        writer.startElement(HtmlElements.LINK, null);
        writer.writeAttribute(HtmlAttributes.REL, "stylesheet", false);
        writer.writeAttribute(HtmlAttributes.HREF, styleString, false);
//          writer.writeAttribute(HtmlAttributes.MEDIA, "screen", false);
        writer.writeAttribute(HtmlAttributes.TYPE, "text/css", false);
        writer.endElement(HtmlElements.LINK);
      }
    }
  }

//  @Override
//  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
//    UIPage page = (UIPage) component;
//    page.encodeLayoutChildren(facesContext);
//  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIPage page = (UIPage) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.endElement(HtmlElements.DIV);

    // write popup components
    // beware of ConcurrentModificationException in cascading popups!
    // no foreach

    final UIPopup[] popupArray = FacesContextUtils.getPopups(facesContext).toArray(
        new UIPopup[FacesContextUtils.getPopups(facesContext).size()]);
    for (final UIPopup popup : popupArray) {
      RenderUtils.encode(facesContext, popup);
    }

    final String clientId = page.getClientId(facesContext);
    final boolean debugMode = VariableResolverUtils.resolveClientProperties(facesContext).isDebugMode();


    // avoid submit page in ie if the form contains only one input and you press the enter key in the input
    if (VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT, false);
      writer.writeAttribute(HtmlAttributes.NAME, "tobago.dummy", false);
      writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
      writer.writeAttribute(HtmlAttributes.STYLE, "visibility:hidden;display:none;", false);
      writer.endElement(HtmlElements.INPUT);
    }

    final List<String> messageClientIds = AjaxInternalUtils.getMessagesClientIds(facesContext);
    if (messageClientIds != null) {
      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.VALUE, StringUtils.join(messageClientIds, ','), true);
      writer.writeAttribute(HtmlAttributes.ID, clientId + ComponentUtils.SUB_SEPARATOR + "messagesClientIds", false);
      writer.writeAttribute(HtmlAttributes.NAME, clientId + ComponentUtils.SUB_SEPARATOR + "messagesClientIds", false);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.endElement(HtmlElements.INPUT);
    }

    // placeholder for menus
    writer.startElement(HtmlElements.DIV, page);
    writer.writeClassAttribute(Classes.create(page, "menuStore"));
    writer.endElement(HtmlElements.DIV);

    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "jsf-state-container");
    writer.flush();
    if (!FacesContextUtils.isAjax(facesContext)) {
      viewHandler.writeState(facesContext);
    }
    writer.endElement(HtmlElements.SPAN);


    writer.endElement(HtmlElements.FORM);

    // The waiting for the next page image
    // Warning: The image must be loaded before the submit, otherwise this feature will not work with webkit
    // browsers. This is the reason, why this code has moved from JavaScript to the renderer here.
    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(page, "overlayWaitPreloadedImage"));
    final String wait = ResourceManagerUtils.getImageWithPath(facesContext, "image/tobago-overlay-wait.gif");
    writer.writeAttribute(HtmlAttributes.SRC, wait, false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(page, "overlayErrorPreloadedImage"));
    final String error = ClientProperties.getInstance(facesContext).getUserAgent().isMsie6()
        ? ResourceManagerUtils.getImageWithPath(facesContext, "image/remove.gif") // XXX why png doesn't work in ie6?
        : ResourceManagerUtils.getImageWithPath(facesContext, "image/dialog-error.png");
    writer.writeAttribute(HtmlAttributes.SRC, error, false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(page, "pngFixBlankImage"));
    final String pngFixBlankImage = ResourceManagerUtils.getImageWithPath(facesContext, "image/blank.gif");
    writer.writeAttribute(HtmlAttributes.SRC, pngFixBlankImage, false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(page, "overlayBackgroundImage"));
    final String overlayBackgroundImage = ResourceManagerUtils.getImageWithPath(facesContext,
        "image/tobago-overlay-background.png");
    writer.writeAttribute(HtmlAttributes.SRC, overlayBackgroundImage, false);
    writer.endElement(HtmlElements.IMG);

    // debugging...
    if (debugMode) {
      final List<String> logMessages = new ArrayList<String>();
      String id = null;
      for (final Iterator<String> ids = facesContext.getClientIdsWithMessages(); ids.hasNext(); id = ids.next()) {
        for (final FacesMessage message : facesContext.getMessageList(id)) {
          logMessages.add(errorMessageForDebugging(id, message));
        }
      }
      if (!logMessages.isEmpty()) {
        logMessages.add(0, "LOG.show();");
      }

      HtmlRendererUtils.writeScriptLoader(facesContext, null, logMessages.toArray(new String[logMessages.size()]));
    }

    writer.startElement(HtmlElements.NOSCRIPT, null);
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(page, "noscript"));
    writer.writeText(ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "pageNoscript"));
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.NOSCRIPT);

    final Object response = facesContext.getExternalContext().getResponse();
    if (PortletUtils.isPortletApiAvailable() && response instanceof MimeResponse) {
      writer.endElement(HtmlElements.DIV);
    } else {
      writer.endElement(HtmlElements.BODY);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("unused AccessKeys    : " + AccessKeyMap.getUnusedKeys(facesContext));
      LOG.debug("duplicated AccessKeys: " + AccessKeyMap.getDublicatedKeys(facesContext));
    }

    if (facesContext.getExternalContext().getRequestParameterMap().get("X") != null) {
      throw new RuntimeException("Debugging activated via X parameter");
    }
  }

  private void writeEventFunction(
      final TobagoResponseWriter writer, final Collection<String> eventFunctions, final String event,
      final boolean returnBoolean)
      throws IOException {
    if (!eventFunctions.isEmpty()) {
      writer.write("Tobago.applicationOn");
      writer.write(event);
      writer.write(" = function(listenerOptions) {\n");
      if (returnBoolean) {
        writer.write("  var result;\n");
      }
      for (final String function : eventFunctions) {
        if (returnBoolean) {
          writer.write("  result = ");
        } else {
          writer.write("  ");
        }
        writer.write(function);
        if (!function.trim().endsWith(";")) {
          writer.write(";\n");
        } else {
          writer.write("\n");
        }
        if (returnBoolean) {
          writer.write("  if (typeof result == \"boolean\" && ! result) {\n");
          writer.write("    return false;\n");
          writer.write("  }\n");
        }
      }
      writer.write("\n  return true;\n}\n");
    }
  }

  private void encodeScript(final FacesContext facesContext, final TobagoResponseWriter writer, final String script)
      throws IOException {
    final List<String> list;
    if (ResourceManagerUtils.isAbsoluteResource(script)) {
      list = new ArrayList<String>();
      list.add(script);
    } else {
      list = ResourceManagerUtils.getScripts(facesContext, script);
    }
    for (final String src : list) {
      if (StringUtils.isNotBlank(src)) {
        writer.startElement(HtmlElements.SCRIPT, null);
        writer.writeAttribute(HtmlAttributes.SRC, src, true);
        // TODO test defer attribute
        //writer.writeAttribute(HtmlAttributes.DEFER, true);
        writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
        writer.endElement(HtmlElements.SCRIPT);
      }
    }
  }

  private void errorMessageForDebugging(final String id, final FacesMessage message, final ResponseWriter writer)
      throws IOException {
    writer.startElement(HtmlElements.DIV, null);
    writer.writeAttribute(HtmlAttributes.STYLE, "color: red", null);
    writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
    writer.write("[");
    writer.write(id != null ? id : "null");
    writer.write("]");
    writer.write("[");
    writer.write(message.getSummary() == null ? "null" : message.getSummary());
    writer.write("/");
    writer.write(message.getDetail() == null ? "null" : message.getDetail());
    writer.write("]");
    writer.endElement(HtmlElements.DIV);
    writer.startElement(HtmlElements.BR, null);
    writer.endElement(HtmlElements.BR);
  }

  private String errorMessageForDebugging(final String id, final FacesMessage message) {
    final StringBuilder sb = new StringBuilder("LOG.info(\"FacesMessage: [");
    sb.append(id != null ? id : "null");
    sb.append("][");
    sb.append(message.getSummary() == null ? "null" : escape(message.getSummary()));
    sb.append("/");
    sb.append(message.getDetail() == null ? "null" : escape(message.getDetail()));
    sb.append("]\");");
    return sb.toString();
  }

  private String escape(final String s) {
    return StringUtils.replace(StringUtils.replace(s, "\\", "\\\\"), "\"", "\\\"");
  }

  private String getMethod(final UIPage page) {
    final String method = (String) page.getAttributes().get(Attributes.METHOD);
    return method == null ? "post" : method;
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public Measure getBorderBottom(final FacesContext facesContext, final Configurable component) {
    // XXX this is a hack. correct would be the top-border, but this would shift the content, because of the
    // XXX hack before the code: writer.writeStyleAttribute(style)
    final UIPage page = (UIPage) component;
    final UIMenuBar menuBar = (UIMenuBar) page.getFacet(Facets.MENUBAR);
    if (menuBar != null) {
      return getResourceManager().getThemeMeasure(facesContext, page, "custom.menuBar-height");
    } else {
      return Measure.ZERO;
    }
  }

  @Override
  public Measure getWidth(final FacesContext facesContext, final Configurable component) {
    // width of the actual browser window
    final Measure width = ClientProperties.getInstance(facesContext).getPageWidth();
    if (width != null) {
      return width;
    } else {
      return super.getWidth(facesContext, component);
    }
  }

  @Override
  public Measure getHeight(final FacesContext facesContext, final Configurable component) {
    // height of the actual browser window
    final Measure height = ClientProperties.getInstance(facesContext).getPageHeight();
    if (height != null) {
      return height;
    } else {
      return super.getHeight(facesContext, component);
    }
  }
}

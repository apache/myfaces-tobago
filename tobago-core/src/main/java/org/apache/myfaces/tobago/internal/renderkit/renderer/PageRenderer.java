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

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.*;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.MimeTypeUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.portlet.PortletUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.Secret;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.MimeResponse;
import javax.portlet.ResourceURL;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// currently using tobago-jsf.js instead
//@ResourceDependency(name="jsf.js", library="javax.faces", target="head")
public class PageRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(PageRenderer.class);

  private static final String LAST_FOCUS_ID = "lastFocusId";
  private static final String HEAD_TARGET = "head";
  public static final String THEME_PARAMETER = "tobago.theme";

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {

    final AbstractUIPage page = (AbstractUIPage) component;
    final String clientId = page.getClientId(facesContext);
    final ExternalContext externalContext = facesContext.getExternalContext();
    final TobagoConfig config = TobagoConfig.getInstance(facesContext);
    final TobagoContext tobagoContext = TobagoContext.getInstance(facesContext);

    // last focus
    final String lastFocusId =
        externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + LAST_FOCUS_ID);
    if (lastFocusId != null) {
      FacesContextUtils.setFocusId(facesContext, lastFocusId);
    }

    // user agent
    final String requestUserAgent = externalContext.getRequestHeaderMap().get("User-Agent");
    final UserAgent userAgent = UserAgent.getInstance(requestUserAgent);
    if (LOG.isDebugEnabled()) {
      LOG.debug("userAgent='" + userAgent + "' from header " + "'User-Agent: " + requestUserAgent + "'");
    }
    tobagoContext.setUserAgent(userAgent);

    // theme
    String themeName = null;
    Object request = externalContext.getRequest();
    if (request instanceof HttpServletRequest) {
      HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      Cookie[] cookies = httpServletRequest.getCookies();
      for (Cookie cookie : cookies) {
        if (THEME_PARAMETER.equals(cookie.getName())) {
          themeName = cookie.getValue();
          if (LOG.isDebugEnabled()) {
            LOG.debug("theme from cookie {}='{}'", THEME_PARAMETER, themeName);
          }
          break;
        }
      }
    }
    tobagoContext.setTheme(config.getTheme(themeName));
    if (LOG.isDebugEnabled()) {
      LOG.debug("theme='{}'", tobagoContext.getTheme().getName());
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIPage page = (UIPage) component;
    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);

    if (FacesContextUtils.getFocusId(facesContext) == null && !StringUtils.isBlank(page.getFocusId())) {
      FacesContextUtils.setFocusId(facesContext, page.getFocusId());
    }
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

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
    if (tobagoConfig.isSetNosniffHeader()) {
      ResponseUtils.ensureNosniffHeader(facesContext);
    }

    final Theme theme = TobagoContext.getInstance(facesContext).getTheme();
    if (response instanceof HttpServletResponse) {
      LOG.info("--------------------------------------------------------------------");
      final HttpServletResponse servletResponse = (HttpServletResponse) response;
      for (String s : servletResponse.getHeaderNames()) {
        LOG.info("'{}' -> '{}'", s, servletResponse.getHeaders(s));
      }
      Cookie cookie = new Cookie(THEME_PARAMETER, theme.getName());
      servletResponse.addCookie(cookie);
      LOG.info("--------------------------------------------------------------------");
      for (String s : servletResponse.getHeaderNames()) {
        LOG.info("'{}' -> '{}'", s, servletResponse.getHeaders(s));
      }
      LOG.info("--------------------------------------------------------------------");
    }

    final String clientId = page.getClientId(facesContext);
    final boolean productionMode = tobagoConfig.getProjectStage() == ProjectStage.Production;
    final boolean preventFrameAttacks = tobagoConfig.isPreventFrameAttacks();

    if (!facesContext.getPartialViewContext().isAjaxRequest()) {
      final String title = page.getLabel();

      writer.startElement(HtmlElements.HEAD);

      // meta tags

      // this is needed, because websphere 6.0? ignores the setting of the content type on the response
      writer.startElement(HtmlElements.META);
      writer.writeAttribute(HtmlAttributes.HTTP_EQUIV, "Content-Type", false);
      writer.writeAttribute(HtmlAttributes.CONTENT, contentType, false);
      writer.endElement(HtmlElements.META);

      writer.startElement(HtmlElements.META);
      writer.writeAttribute(HtmlAttributes.NAME, "viewport", false);
      writer.writeAttribute(HtmlAttributes.CONTENT, "width=device-width, initial-scale=1.0", false);
      writer.endElement(HtmlElements.META);

      // title
      writer.startElement(HtmlElements.TITLE);
      writer.writeText(title != null ? title : "");
      writer.endElement(HtmlElements.TITLE);

      // style files
      for (final String styleFile : theme.getStyleResources(productionMode)) {
        writeStyle(writer, styleFile);
      }

      for (final String styleFile : FacesContextUtils.getStyleFiles(facesContext)) {
        writeStyle(writer, styleFile);
      }

      if (!productionMode) {
        checkDuplicates(theme.getStyleResources(productionMode), FacesContextUtils.getStyleFiles(facesContext));
      }

      final String icon = page.getApplicationIcon();
      if (icon != null) {
        writer.startElement(HtmlElements.LINK);
        if (icon.endsWith(".ico")) {
          writer.writeAttribute(HtmlAttributes.REL, "shortcut icon", false);
          writer.writeAttribute(HtmlAttributes.HREF, icon, true);
        } else {
          // XXX IE only supports ICO files for favicons
          writer.writeAttribute(HtmlAttributes.REL, "icon", false);
          writer.writeAttribute(HtmlAttributes.TYPE, MimeTypeUtils.getMimeTypeForFile(icon), true);
          writer.writeAttribute(HtmlAttributes.HREF, icon, true);
        }
        writer.endElement(HtmlElements.LINK);
      }
      UIViewRoot root = facesContext.getViewRoot();
      List<UIComponent> componentResources = root.getComponentResources(facesContext, HEAD_TARGET);

      for (int i = 0, childCount = componentResources.size(); i < childCount; i++) {
        UIComponent child = componentResources.get(i);
        // XXX hack to remove jsf.js
        if ("jsf.js".equals(child.getAttributes().get("name"))) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Skip rendering resource jsf.js");
          }
          continue;
        }
        child.encodeAll(facesContext);
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

      writer.endElement(HtmlElements.HEAD);
    }

    writer.startElement(portlet ? HtmlElements.DIV : HtmlElements.BODY);
    writer.writeClassAttribute(
        portlet ? Classes.create(page, Markup.PORTLET) : Classes.create(page),
        BootstrapClass.CONTAINER_FLUID,
        page.getCustomClass());
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, page);


    writer.writeCommandMapAttribute(JsonUtils.encode(RenderUtils.getBehaviorCommands(facesContext, page)));

    writer.startElement(HtmlElements.FORM);
    if (preventFrameAttacks && !facesContext.getPartialViewContext().isAjaxRequest()) {
      writer.writeClassAttribute(Classes.create(page, "preventFrameAttacks", Markup.NULL));
    }
    writer.writeAttribute(HtmlAttributes.ACTION, formAction, true);
    if (partialAction != null) {
      writer.writeAttribute(DataAttributes.PARTIAL_ACTION, partialAction, true);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("partial action = " + partialAction);
    }
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
    writer.writeAttribute(DataAttributes.CONTEXT_PATH, externalContext.getRequestContextPath(), true);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeNameAttribute("javax.faces.source");
    writer.writeIdAttribute("javax.faces.source");
    writer.writeAttribute(HtmlAttributes.DISABLED, true);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "lastFocusId");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "lastFocusId");
    writer.writeAttribute(HtmlAttributes.VALUE, FacesContextUtils.getFocusId(facesContext), true);
    writer.endElement(HtmlElements.INPUT);

    if (TobagoConfig.getInstance(FacesContext.getCurrentInstance()).isCheckSessionSecret()) {
      Secret.encode(facesContext, writer);
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
      writer.writeAttribute(HtmlAttributes.type, "hidden", null);
      writer.writeNameAttribute(ViewHandlerImpl.PAGE_ID);
      writer.writeIdAttribute(ViewHandlerImpl.PAGE_ID);
      Object value = facesContext.getViewRoot().getAttributes().get(
          ViewHandlerImpl.PAGE_ID);
      writer.writeAttribute(HtmlAttributes.value, (value != null ? value : ""), null);
      writer.endElement(HtmlElements.INPUT);
    }
*/
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

  private void writeStyle(final TobagoResponseWriter writer, final String styleFile)
      throws IOException {
    final List<String> styles = Collections.singletonList(styleFile);
    for (final String styleString : styles) {
      if (styleString.length() > 0) {
        writer.startElement(HtmlElements.LINK);
        writer.writeAttribute(HtmlAttributes.REL, "stylesheet", false);
        writer.writeAttribute(HtmlAttributes.HREF, styleString, true);
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
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

/*
    writer.endElement(HtmlElements.DIV);
*/

    final String clientId = page.getClientId(facesContext);
    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "jsf-state-container");
    writer.flush();
    if (!facesContext.getPartialViewContext().isAjaxRequest()) {
      viewHandler.writeState(facesContext);
    }
    writer.endElement(HtmlElements.SPAN);


    writer.endElement(HtmlElements.FORM);

    writer.startElement(HtmlElements.NOSCRIPT);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(Classes.create(page, "noscript"));
    writer.writeText(TobagoResourceBundle.getString(facesContext, "pageNoscript"));
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.NOSCRIPT);

    final Object response = facesContext.getExternalContext().getResponse();
    if (PortletUtils.isPortletApiAvailable() && response instanceof MimeResponse) {
      writer.endElement(HtmlElements.DIV);
    } else {
      writer.endElement(HtmlElements.BODY);
    }

    AccessKeyLogger.logStatus(facesContext);

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
    list = new ArrayList<String>();
    list.add(script);
    for (final String src : list) {
      if (StringUtils.isNotBlank(src)) {
        writer.startElement(HtmlElements.SCRIPT);
        writer.writeAttribute(HtmlAttributes.SRC, src, true);
//   XXX with defer activated, pages are not shown reliable
//        writer.writeAttribute(HtmlAttributes.DEFER, true);
        writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
        writer.endElement(HtmlElements.SCRIPT);
      }
    }
  }

  private String getMethod(final UIPage page) {
    return ComponentUtils.getStringAttribute(page, Attributes.method, "post");
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }
}

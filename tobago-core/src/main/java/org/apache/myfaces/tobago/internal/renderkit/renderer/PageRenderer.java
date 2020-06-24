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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.ThemeScript;
import org.apache.myfaces.tobago.context.ThemeStyle;
import org.apache.myfaces.tobago.context.TobagoContext;
import org.apache.myfaces.tobago.internal.component.AbstractUIMeta;
import org.apache.myfaces.tobago.internal.component.AbstractUIMetaLink;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.component.AbstractUIScript;
import org.apache.myfaces.tobago.internal.component.AbstractUIStyle;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.CookieUtils;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.portlet.PortletUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.Secret;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.spi.CDI;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.MimeResponse;
import javax.portlet.ResourceURL;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// using jsf.js from a specific MyFaces version instead, to avoid old bugs
//@ResourceDependency(name="jsf.js", library="javax.faces", target="head")
public class PageRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String LAST_FOCUS_ID = "lastFocusId";
  private static final String HEAD_TARGET = "head";
  private static final String BODY_TARGET = "body";

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {

    final AbstractUIPage page = (AbstractUIPage) component;
    final String clientId = page.getClientId(facesContext);
    final ExternalContext externalContext = facesContext.getExternalContext();

    // last focus
    final String lastFocusId =
        externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + LAST_FOCUS_ID);
    if (lastFocusId != null) {
      TobagoContext.getInstance(facesContext).setFocusId(lastFocusId);
    }
  }

//  @Inject // fixme
  private ProjectStage projectStage;

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIPage page = (AbstractUIPage) component;
    final TobagoConfig tobagoConfig = CDI.current().select(TobagoConfig.class).get(); // todo: may inject
    final TobagoContext tobagoContext = CDI.current().select(TobagoContext.class).get(); // todo: may inject

    if (tobagoContext.getFocusId() == null && !StringUtils.isBlank(page.getFocusId())) {
      tobagoContext.setFocusId(page.getFocusId());
    }
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    // reset responseWriter and render page
    facesContext.setResponseWriter(writer);

    if (tobagoConfig.isPreventFrameAttacks()) {
      ResponseUtils.ensureXFrameOptionsHeader(facesContext);
    }

    ResponseUtils.ensureNoCacheHeader(facesContext);

    ResponseUtils.ensureContentSecurityPolicyHeader(facesContext, tobagoConfig.getContentSecurityPolicy());

    if (LOG.isDebugEnabled()) {
      for (final Object o : page.getAttributes().entrySet()) {
        final Map.Entry entry = (Map.Entry) o;
        LOG.debug("*** '" + entry.getKey() + "' -> '" + entry.getValue() + "'");
      }
    }

    final ExternalContext externalContext = facesContext.getExternalContext();
    final String contextPath = externalContext.getRequestContextPath();
    final Object request = externalContext.getRequest();
    final Object response = externalContext.getResponse();
    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    final String viewId = viewRoot.getViewId();
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

    final Theme theme = tobagoContext.getTheme();
    if (response instanceof HttpServletResponse && request instanceof HttpServletRequest) {
      CookieUtils.setThemeNameToCookie((HttpServletRequest) request, (HttpServletResponse) response, theme.getName());
    }

    final String clientId = page.getClientId(facesContext);
    final boolean productionMode = projectStage == ProjectStage.Production;
    final Markup markup = page.getMarkup();
    final TobagoClass spread = markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null;
    final String title = page.getLabel();

    final Locale locale = viewRoot.getLocale();
    if (!portlet) {
      writer.startElement(HtmlElements.HTML);
      if (locale != null) {
        final String language = locale.getLanguage();
        if (language != null) {
          writer.writeAttribute(HtmlAttributes.LANG, language, false);
        }
      }
    }
    writer.writeClassAttribute(spread);

    writer.startElement(HtmlElements.HEAD);

    final HeadResources headResources = new HeadResources(
        facesContext, viewRoot.getComponentResources(facesContext, HEAD_TARGET), writer.getCharacterEncoding());

    // meta tags
    for (final UIComponent metas : headResources.getMetas()) {
      metas.encodeAll(facesContext);
    }

    // title
    writer.startElement(HtmlElements.TITLE);
    writer.writeText(title != null ? title : "");
    writer.endElement(HtmlElements.TITLE);

    // style files from theme
    AbstractUIStyle style = null;
    for (final ThemeStyle themeStyle : theme.getStyleResources(productionMode)) {
      if (style == null) {
        style = (AbstractUIStyle) facesContext.getApplication()
           .createComponent(facesContext, Tags.style.componentType(), RendererTypes.Style.name());
        style.setTransient(true);
      }
      style.setFile(contextPath + themeStyle.getName());
      style.encodeAll(facesContext);
    }

    // style files individual files
    for (final UIComponent styles : headResources.getStyles()) {
      styles.encodeAll(facesContext);
    }

    // script files from theme
    for (final ThemeScript themeScript : theme.getScriptResources(productionMode)) {
      final AbstractUIScript script = (AbstractUIScript) facesContext.getApplication()
          .createComponent(facesContext, Tags.script.componentType(), RendererTypes.Script.name());
      script.setTransient(true);
      script.setFile(contextPath + themeScript.getName());
      script.setType(themeScript.getType());
      script.encodeAll(facesContext);
    }

    // script files individual files
    for (final UIComponent scripts : headResources.getScripts()) {
      scripts.encodeAll(facesContext);
    }

    for (final UIComponent misc : headResources.getMisc()) {
      misc.encodeAll(facesContext);
    }

    writer.endElement(HtmlElements.HEAD);

    if (!portlet) {
      writer.startElement(HtmlElements.BODY);
      writer.writeClassAttribute(spread);
    }

    writer.startElement(HtmlElements.TOBAGO_PAGE);

    writer.writeAttribute(CustomAttributes.LOCALE, locale.toString(), false);
    writer.writeClassAttribute(
        BootstrapClass.CONTAINER_FLUID,
        TobagoClass.PAGE.createMarkup(portlet ? Markup.PORTLET.add(page.getMarkup()) : page.getMarkup()),
        spread,
        page.getCustomClass());
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, page);

    encodeBehavior(writer, facesContext, page);

    writer.startElement(HtmlElements.FORM);
    writer.writeClassAttribute(spread);
    writer.writeAttribute(HtmlAttributes.ACTION, formAction, true);
    if (partialAction != null) {
      writer.writeAttribute(DataAttributes.PARTIAL_ACTION, partialAction, true);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("partial action = " + partialAction);
    }
    writer.writeIdAttribute(page.getFormId(facesContext));
    writer.writeAttribute(HtmlAttributes.METHOD, getMethod(page), false);
    final String enctype = tobagoContext.getEnctype();
    if (enctype != null) {
      writer.writeAttribute(HtmlAttributes.ENCTYPE, enctype, false);
    }
    // TODO: enable configuration of  'accept-charset'
    writer.writeAttribute(HtmlAttributes.ACCEPT_CHARSET, AbstractUIPage.FORM_ACCEPT_CHARSET.name(), false);
    // TODO evaluate 'accept' attribute usage
    //writer.writeAttribute(HtmlAttributes.ACCEPT, );
    writer.writeAttribute(DataAttributes.CONTEXT_PATH, contextPath, true);

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeNameAttribute("javax.faces.source");
    writer.writeIdAttribute("javax.faces.source");
    writer.writeAttribute(HtmlAttributes.DISABLED, true);
    writer.endElement(HtmlElements.INPUT);

    final String lastFocusId = clientId + ComponentUtils.SUB_SEPARATOR + "lastFocusId";
    writer.startElement(HtmlElements.TOBAGO_FOCUS);
    writer.writeIdAttribute(lastFocusId);
    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeNameAttribute(lastFocusId);
    writer.writeIdAttribute(lastFocusId + ComponentUtils.SUB_SEPARATOR + "field");
    writer.writeAttribute(HtmlAttributes.VALUE, tobagoContext.getFocusId(), true);
    writer.endElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.TOBAGO_FOCUS);

    if (tobagoConfig.isCheckSessionSecret()) {
      writer.startElement(HtmlElements.INPUT);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
      writer.writeAttribute(HtmlAttributes.NAME, Secret.KEY, false);
      writer.writeAttribute(HtmlAttributes.ID, Secret.KEY, false);
//      final Object session = facesContext.getExternalContext().getSession(true);
      final Secret secret = CDI.current().select(Secret.class).get();
      secret.encode(writer);
      writer.endElement(HtmlElements.INPUT);
    }

    if (component.getFacet("backButtonDetector") != null) {
      final UIComponent hidden = component.getFacet("backButtonDetector");
      hidden.encodeAll(facesContext);
    }
  }

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

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIPage page = (AbstractUIPage) component;
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = page.getClientId(facesContext);
    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();
    final Object response = facesContext.getExternalContext().getResponse();
    final boolean portlet = PortletUtils.isPortletApiAvailable() && response instanceof MimeResponse;
    final boolean ajax = facesContext.getPartialViewContext().isAjaxRequest();

    // placeholder for menus
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.PAGE__MENU_STORE);
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "jsf-state-container");
    writer.flush();
    if (!ajax) {
      viewHandler.writeState(facesContext);
    }
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.FORM);

    writer.startElement(HtmlElements.NOSCRIPT);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.PAGE__NOSCRIPT);
    writer.writeText(ResourceUtils.getString(facesContext, "page.noscript"));
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.NOSCRIPT);
    writer.endElement(HtmlElements.TOBAGO_PAGE);

    final List<UIComponent> bodyResources = viewRoot.getComponentResources(facesContext, BODY_TARGET);
    for (final UIComponent bodyResource : bodyResources) {
      bodyResource.encodeAll(facesContext);
    }

    if (!portlet) {
      writer.endElement(HtmlElements.BODY);
      writer.endElement(HtmlElements.HTML);
    }

    AccessKeyLogger.logStatus(facesContext);
  }

  private String getMethod(final AbstractUIPage page) {
    return ComponentUtils.getStringAttribute(page, Attributes.method, "post");
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  /**
   * This class helps to order the head resources.
   */
  private static class HeadResources {

    private List<UIComponent> metas = new ArrayList<>();
    private List<UIComponent> styles = new ArrayList<>();
    private List<UIComponent> scripts = new ArrayList<>();
    private List<UIComponent> misc = new ArrayList<>();

    HeadResources(
        final FacesContext facesContext, final Collection<? extends UIComponent> collection, final String charset) {
      for (final UIComponent uiComponent : collection) {
        if (uiComponent instanceof AbstractUIMeta || uiComponent instanceof AbstractUIMetaLink) {
          metas.add(uiComponent);
        } else if (uiComponent instanceof AbstractUIStyle) {
          styles.add(uiComponent);
        } else if (uiComponent instanceof AbstractUIScript) {
          scripts.add(uiComponent);
        } else {
          if (uiComponent instanceof UIOutput) {
            final Map<String, Object> attributes = uiComponent.getAttributes();
            if ("javax.faces".equals(attributes.get("library"))
                && "jsf.js".equals(attributes.get("name"))) {
              // workaround for WebSphere
              // We don't need jsf.js from the JSF impl, because Tobago comes with its own jsf.js
              if (LOG.isDebugEnabled()) {
                LOG.debug("Skip rendering resource jsf.js");
              }
              continue;
            }
          }
          misc.add(uiComponent);
        }
      }

      if (!containsNameViewport(metas)) {
        final AbstractUIMeta viewportMeta = (AbstractUIMeta) facesContext.getApplication()
            .createComponent(facesContext, Tags.meta.componentType(), RendererTypes.Meta.name());
        viewportMeta.setName("viewport");
        viewportMeta.setContent("width=device-width, initial-scale=1.0");
        viewportMeta.setTransient(true);
        metas.add(0, viewportMeta);
      }

      if (!containsCharset(metas)) {
        final AbstractUIMeta charsetMeta = (AbstractUIMeta) facesContext.getApplication()
            .createComponent(facesContext, Tags.meta.componentType(), RendererTypes.Meta.name());
        charsetMeta.setCharset(charset);
        charsetMeta.setTransient(true);
        metas.add(0, charsetMeta);
      }
    }

    public List<UIComponent> getMetas() {
      return metas;
    }

    public List<UIComponent> getStyles() {
      return styles;
    }

    public List<UIComponent> getScripts() {
      return scripts;
    }

    public List<UIComponent> getMisc() {
      return misc;
    }

    private boolean containsCharset(final List<UIComponent> headComponents) {
      for (final UIComponent headComponent : headComponents) {
        if (headComponent instanceof AbstractUIMeta
            && ((AbstractUIMeta) headComponent).getCharset() != null) {
          return true;
        }
      }
      return false;
    }

    private boolean containsNameViewport(final List<UIComponent> headComponents) {
      for (final UIComponent headComponent : headComponents) {
        if (headComponent instanceof AbstractUIMeta
            && "viewport".equals(((AbstractUIMeta) headComponent).getName())) {
          return true;
        }
      }
      return false;
    }

  }
}

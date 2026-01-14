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
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
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

import jakarta.faces.application.Application;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.application.ViewHandler;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIOutput;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.component.behavior.ClientBehaviorContext;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.PartialViewContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// using faces.js from a specific MyFaces version instead, to avoid old bugs
//@ResourceDependency(name="faces.js", library="jakarta.faces", target="head")
public class PageRenderer<T extends AbstractUIPage> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String LAST_FOCUS_ID = "lastFocusId";
  private static final String HEAD_TARGET = "head";
  private static final String BODY_TARGET = "body";

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {

    final String clientId = component.getClientId(facesContext);
    final ExternalContext externalContext = facesContext.getExternalContext();

    // last focus
    final String lastFocusId =
        externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + LAST_FOCUS_ID);
    if (lastFocusId != null) {
      TobagoContext.getInstance(facesContext).setFocusId(lastFocusId);
    }
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);
    final TobagoContext tobagoContext = TobagoContext.getInstance(facesContext);

    if (tobagoContext.getFocusId() == null && !StringUtils.isBlank(component.getFocusId())) {
      tobagoContext.setFocusId(component.getFocusId());
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
      for (final Object o : component.getAttributes().entrySet()) {
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
    final String formAction = getActionUrl(facesContext, viewId, component);
    final PartialViewContext partialViewContext = facesContext.getPartialViewContext();

    final String contentType = writer.getContentTypeWithCharSet();
    ResponseUtils.ensureContentTypeHeader(facesContext, contentType);
    if (tobagoConfig.isSetNosniffHeader()) {
      ResponseUtils.ensureNosniffHeader(facesContext);
    }

    final String clientId = component.getClientId(facesContext);
    final Markup markup = component.getMarkup();
    final TobagoClass spread = markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null;

    final Locale locale = viewRoot.getLocale();

    if (!partialViewContext.isAjaxRequest() || partialViewContext.isRenderAll()) {
      writer.startElement(HtmlElements.HTML);
      if (locale != null) {
        final String language = locale.getLanguage();
        if (language != null) {
          writer.writeAttribute(HtmlAttributes.LANG, language, false);
        }
      }
      writer.writeClassAttribute(spread);

      encodeHead(facesContext, component, tobagoContext, writer, viewRoot, contextPath);

      writer.startElement(HtmlElements.BODY);
      writer.writeClassAttribute(spread);
    }

    writer.startElement(HtmlElements.TOBAGO_PAGE);

    writer.writeAttribute(CustomAttributes.LOCALE, locale.toString(), false);
    writer.writeClassAttribute(
        BootstrapClass.CONTAINER_FLUID,
        spread,
        component.getCustomClass());
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    // write the config
    writer.writeAttribute(HtmlAttributes.FOCUS_ON_ERROR, Boolean.toString(tobagoContext.getFocusOnError()), false);
    writer.writeAttribute(HtmlAttributes.WAIT_OVERLAY_DELAY_FULL, tobagoContext.getWaitOverlayDelayFull());
    writer.writeAttribute(HtmlAttributes.WAIT_OVERLAY_DELAY_AJAX, tobagoContext.getWaitOverlayDelayAjax());

    encodeBehavior(writer, facesContext, component);

    writer.startElement(HtmlElements.FORM);
    writer.writeClassAttribute(spread);
    writer.writeAttribute(HtmlAttributes.ACTION, formAction, true);
    writer.writeIdAttribute(component.getFormId(facesContext));
    writer.writeAttribute(HtmlAttributes.METHOD, getMethod(component), false);
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
    writer.writeNameAttribute(ClientBehaviorContext.BEHAVIOR_SOURCE_PARAM_NAME);
    writer.writeIdAttribute(ClientBehaviorContext.BEHAVIOR_SOURCE_PARAM_NAME);
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
      final Secret secret = Secret.getInstance(facesContext);
      if (secret != null) {
        writer.startElement(HtmlElements.INPUT);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
        writer.writeAttribute(HtmlAttributes.NAME, Secret.KEY, false);
        writer.writeAttribute(HtmlAttributes.ID, Secret.KEY, false);
        writer.writeAttribute(HtmlAttributes.VALUE, secret.getSecret(), false);
        writer.endElement(HtmlElements.INPUT);
      } else {
        LOG.warn("Missing session secret!");
      }
    }

    // placeholder for menus
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.PAGE__MENU_STORE);
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.PAGE__POPOVER_STORE);
    writer.endElement(HtmlElements.DIV);

    // placeholder for toasts
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.PAGE__TOAST_STORE);
    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "faces-state-container");
    writer.flush();
    if (!partialViewContext.isAjaxRequest() || partialViewContext.isRenderAll()) {
      viewHandler.writeState(facesContext);
    }
    writer.endElement(HtmlElements.SPAN);

    if (component.getFacet("backButtonDetector") != null) {
      final UIComponent hidden = component.getFacet("backButtonDetector");
      hidden.encodeAll(facesContext);
    }
  }

  private String getActionUrl(final FacesContext facesContext, final String viewId, final T component) {
    final ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
    final String actionUrl;
    if (component.isIncludeViewParams()) {
      actionUrl = viewHandler.getBookmarkableURL(facesContext, viewId, Map.of(), true);
    } else {
      actionUrl = viewHandler.getActionURL(facesContext, viewId);
    }
    return facesContext.getExternalContext().encodeActionURL(actionUrl);
  }

  private void encodeHead(
      final FacesContext facesContext, final T component, final TobagoContext tobagoContext,
      final TobagoResponseWriter writer, final UIViewRoot viewRoot, final String contextPath) throws IOException {
    final String title = component.getLabel();
    final Theme theme = tobagoContext.getTheme();
    final boolean productionMode = facesContext.isProjectStage(ProjectStage.Production);

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
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    final UIViewRoot viewRoot = facesContext.getViewRoot();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final PartialViewContext partialViewContext = facesContext.getPartialViewContext();

    writer.endElement(HtmlElements.FORM);

    writer.startElement(HtmlElements.NOSCRIPT);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.PAGE__NOSCRIPT);
    writer.writeText(ResourceUtils.getString(facesContext, "page.noscript"));
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.NOSCRIPT);
    writer.endElement(HtmlElements.TOBAGO_PAGE);

    if (!partialViewContext.isAjaxRequest() || partialViewContext.isRenderAll()) {
      final List<UIComponent> bodyResources = viewRoot.getComponentResources(facesContext, BODY_TARGET);
      for (final UIComponent bodyResource : bodyResources) {
        bodyResource.encodeAll(facesContext);
      }

      writer.endElement(HtmlElements.BODY);
      writer.endElement(HtmlElements.HTML);

      AccessKeyLogger.logStatus(facesContext);
    }
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
            if ("jakarta.faces".equals(attributes.get("library"))
                && "faces.js".equals(attributes.get("name"))) {
              // workaround for WebSphere
              // We don't need faces.js from the JSF impl, because Tobago comes with its own faces.js
              if (LOG.isDebugEnabled()) {
                LOG.debug("Skip rendering resource faces.js");
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

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

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIForm;
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
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.layout.LayoutContext;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.util.FastStringWriter;
import org.apache.myfaces.tobago.internal.util.MimeTypeUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.TobagoResponseStateManager;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.FacesVersion;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.Secret;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
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
  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);
    String clientId = component.getClientId(facesContext);
    ExternalContext externalContext = facesContext.getExternalContext();

    // severity
    String severity = (String)
        externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity");
    if (severity != null) {
      externalContext.getRequestMap().put(CLIENT_DEBUG_SEVERITY, severity);
    }

    // last focus
    String lastFocusId = (String) 
        externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + LAST_FOCUS_ID);
    if (lastFocusId != null) {
      component.getAttributes().put(LAST_FOCUS_ID, lastFocusId);
    }

    // scrollbar weight
    String name = clientId + ComponentUtils.SUB_SEPARATOR + "scrollbarWeight";
    String value = null;
    try {
      value = (String) facesContext.getExternalContext().getRequestParameterMap().get(name);
      if (StringUtils.isNotBlank(value)) {
        StringTokenizer tokenizer = new StringTokenizer(value, ";");
        Measure vertical = Measure.valueOf(tokenizer.nextToken());
        Measure horizontal = Measure.valueOf(tokenizer.nextToken());
        if (vertical.greaterThan(Measure.valueOf(30)) || vertical.lessThan(Measure.valueOf(3))
           || horizontal.greaterThan(Measure.valueOf(30)) || horizontal.lessThan(Measure.valueOf(3))) {
          LOG.error("Ignoring strange values: vertical=" + vertical + " horizontal=" + horizontal);
        } else {
          ClientProperties client = VariableResolverUtils.resolveClientProperties(facesContext);
          client.setVerticalScrollbarWeight(vertical);
          client.setHorizontalScrollbarWeight(horizontal);
        }
      }
    } catch (Exception e) {
      LOG.error("Error in decoding '" + name + "': value='" + value + "'", e);
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    UIPage page = (UIPage) component;

    // invoke prepareRender
    RenderUtils.prepareRendererAll(facesContext, page);

    LayoutContext layoutContext = new LayoutContext(page);
    layoutContext.layout();

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    // reset responseWriter and render page
    facesContext.setResponseWriter(writer);

    ResponseUtils.ensureNoCacheHeader(facesContext);

    if (LOG.isDebugEnabled()) {
      for (Object o : page.getAttributes().entrySet()) {
        Map.Entry entry = (Map.Entry) o;
        LOG.debug("*** '" + entry.getKey() + "' -> '" + entry.getValue() + "'");
      }
    }

    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();
    String viewId = facesContext.getViewRoot().getViewId();
    String formAction = viewHandler.getActionURL(facesContext, viewId);
    formAction = facesContext.getExternalContext().encodeActionURL(formAction);
    String contentType = writer.getContentTypeWithCharSet();
    ResponseUtils.ensureContentTypeHeader(facesContext, contentType);
    String clientId = page.getClientId(facesContext);
    final ClientProperties client = VariableResolverUtils.resolveClientProperties(facesContext);
    final ProjectStage projectStage = TobagoConfig.getInstance(facesContext).getProjectStage();
    final boolean developmentMode =  projectStage == ProjectStage.Development;
    final boolean debugMode = client.isDebugMode() || developmentMode;
    final boolean productionMode = !debugMode && projectStage == ProjectStage.Production;
    boolean calculateScrollbarWeight = false;
    int clientLogSeverity = 2;
    if (debugMode) {
      String severity = (String) facesContext.getExternalContext().getRequestMap().get(CLIENT_DEBUG_SEVERITY);
      LOG.info("get " + CLIENT_DEBUG_SEVERITY + " = " + severity);
      if (severity != null) {
        try {
          int index = severity.indexOf(';');
          if (index == -1) {
            index = severity.length();
          }
            clientLogSeverity = Integer.parseInt(severity.substring(0, index));
        } catch (NumberFormatException e) {/* ignore; use default*/ }
      }
    }

    if (!FacesContextUtils.isAjax(facesContext)) {
      HtmlRendererUtils.renderDojoDndSource(facesContext, component);

      String title = (String) page.getAttributes().get(Attributes.LABEL);

      writer.startElement(HtmlElements.HEAD, null);

      if (debugMode) {
        writer.writeJavascript("var TbgHeadStart = new Date();");
      }

      // meta
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
      for (String styleFile : theme.getStyleResources(productionMode)) {
        writeStyle(facesContext, writer, styleFile);
      }

      for (String styleFile : FacesContextUtils.getStyleFiles(facesContext)) {
        writeStyle(facesContext, writer, styleFile);
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
      Set<String> styleBlocks = FacesContextUtils.getStyleBlocks(facesContext);
      if (styleBlocks.size() > 0) {
        writer.startElement(HtmlElements.STYLE, null);
        for (String cssBlock : styleBlocks) {
          writer.write(cssBlock);
        }
        writer.endElement(HtmlElements.STYLE);
      }

      if (debugMode) {
        boolean hideClientLogging = true;
        String severity = (String) facesContext.getExternalContext().getRequestMap().get(CLIENT_DEBUG_SEVERITY);
        LOG.info("get " + CLIENT_DEBUG_SEVERITY + " = " + severity);
        if (severity != null) {
          try {
            int index = severity.indexOf(';');
            if (index == -1) {
              index = severity.length();
            }
            clientLogSeverity = Integer.parseInt(severity.substring(0, index));
          } catch (NumberFormatException e) {/* ignore; use default*/ }
          hideClientLogging = !severity.contains("show");
        }
        // the jquery ui is used in moment only for the logging area...
        FacesContextUtils.addOnloadScript(facesContext, 0, "new LOG.LogArea({hide: " + hideClientLogging + "});");
      }

      // render remaining script tags
      for (String scriptFile: theme.getScriptResources(productionMode)) {
        encodeScript(facesContext, writer, scriptFile);
      }

      for (String scriptFile : FacesContextUtils.getScriptFiles(facesContext)) {
        encodeScript(facesContext, writer, scriptFile);
      }

      // focus id
      String focusId = page.getFocusId();
      if (focusId != null) {
        writer.startJavascript();
        writer.write("Tobago.focusId = '");
        writer.write(focusId);
        writer.write("';");
        writer.endJavascript();
      }

      if (component.getFacets().containsKey(Facets.ACTION)) {
        UIComponent command = component.getFacet(Facets.ACTION);
        if (command != null && command.isRendered()) {
          int duration = ComponentUtils.getIntAttribute(command, Attributes.DELAY, 100);
          boolean transition = ComponentUtils.getBooleanAttribute(command, Attributes.TRANSITION);
          String target = ComponentUtils.getStringAttribute(command, Attributes.TARGET);
          String action
              = HtmlRendererUtils.createSubmitAction(command.getClientId(facesContext), transition, target, null);
          FacesContextUtils.addOnloadScript(facesContext, "setTimeout(\"" + action  + "\", " + duration + ");\n");
        }
      }


      calculateScrollbarWeight
          = client.getVerticalScrollbarWeight() == null || client.getHorizontalScrollbarWeight() == null;
      if (calculateScrollbarWeight) {
        FacesContextUtils.addOnloadScript(facesContext,
            "Tobago.calculateScrollbarWeights('" + clientId + ComponentUtils.SUB_SEPARATOR + "scrollbarWeight" + "');");
      } else {
        FacesContextUtils.addOnloadScript(facesContext,
            "Tobago.Config.set('Tobago', 'verticalScrollbarWeight', '"
                + client.getVerticalScrollbarWeight().getPixel() + "');");
        FacesContextUtils.addOnloadScript(facesContext,
            "Tobago.Config.set('Tobago', 'horizontalScrollbarWeight', '"
                + client.getHorizontalScrollbarWeight().getPixel() + "');");
      }

      UIComponent command = null;
      if (component.getFacets().containsKey(Facets.RESIZE_ACTION)) {
        Deprecation.LOG.warn("Please use 'resize' instead of 'resizeAction' as facet.");
        UIComponent facet = component.getFacet(Facets.RESIZE_ACTION);
        if (facet instanceof UICommand) {
          command = facet;
        } else if (facet instanceof UIForm && facet.getChildCount() == 1) {
          Deprecation.LOG.warn("Please don't use a form, but a command with immediate=true instead.");
          command = (UIComponent) facet.getChildren().get(0);
        }
      }

      if (component.getFacets().containsKey(Facets.RESIZE)) {
        UIComponent facet = component.getFacet(Facets.RESIZE);
        if (facet instanceof UICommand) {
          command = facet;
        } else if (facet instanceof UIForm && facet.getChildCount() == 1) {
          Deprecation.LOG.warn("Please don't use a form, but a command with immediate=true instead.");
          command = (UIComponent) facet.getChildren().get(0);
        }
      }
      if (command != null && command.isRendered()) {
        final CommandRendererHelper helper = new CommandRendererHelper(facesContext, (AbstractUICommandBase) command);
        if (!helper.isDisabled()) {
          writer.writeJavascript("Tobago.resizeAction = function() {\n" + helper.getOnclick() + "\n};\n");
        }
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
      for (String scriptBlock : FacesContextUtils.getScriptBlocks(facesContext)) {

        if (LOG.isDebugEnabled()) {
          LOG.debug("write scriptblock " + ++debugCounter + " :\n" + scriptBlock);
        }
        writer.write(scriptBlock);
        writer.write('\n');
      }
      writer.endJavascript();
      writer.endElement(HtmlElements.HEAD);
    }

    String defaultActionId = page.getDefaultActionId() != null ? page.getDefaultActionId() : "";
    writer.startElement(HtmlElements.BODY, page);
    writer.writeAttribute(HtmlAttributes.ONLOAD, "Tobago.init('" + clientId + "');", false);
//    writer.writeAttribute("onunload", "Tobago.onexit();", null);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(page));

    writer.startJavascript();
    writer.write("Tobago.pngFixBlankImage = '");
    writer.write(ResourceManagerUtils.getImageWithPath(facesContext, "image/blank.gif"));
    writer.write("';\n");
    writer.write("Tobago.OVERLAY_BACKGROUND = '");
    writer.write(ResourceManagerUtils.getImageWithPath(facesContext, "image/tobago-overlay-background.png"));
    writer.write("';\n");
    writer.write("Tobago.OVERLAY_WAIT = '");
    writer.write(ResourceManagerUtils.getImageWithPath(facesContext, "image/tobago-overlay-wait.gif"));
    writer.write("';\n");
    writer.endJavascript();
/*
    if (debugMode) {
      final String[] jsFiles = new String[]{
          "script/logging.js"
      };
      final String[] jsCommand = new String[]{"new LOG.LogArea({hide: " + hideClientLogging + "});"};
      HtmlRendererUtils.writeScriptLoader(facesContext, jsFiles, jsCommand);
      writer.writeJavascript("TbgTimer.startBody = new Date();");
    }
*/
    if (debugMode) {
      writer.writeJavascript("TbgTimer.startBody = new Date();");
    }

    writer.startElement(HtmlElements.FORM, page);
    writer.writeAttribute(HtmlAttributes.ACTION, formAction, true);
    writer.writeIdAttribute(page.getFormId(facesContext));
    writer.writeAttribute(HtmlAttributes.METHOD, getMethod(page), false);
    String enctype = FacesContextUtils.getEnctype(facesContext);
    if (enctype != null) {
      writer.writeAttribute(HtmlAttributes.ENCTYPE, enctype, false);
    }
    // TODO: enable configuration of  'accept-charset'
    writer.writeAttribute(HtmlAttributes.ACCEPT_CHARSET, AbstractUIPage.FORM_ACCEPT_CHARSET, false);
    // TODO evaluate 'accept' attribute usage
    //writer.writeAttribute(HtmlAttributes.ACCEPT, );
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "form-action");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "form-action");
    writer.writeAttribute(HtmlAttributes.VALUE, defaultActionId, true);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "context-path");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "context-path");
    writer.writeAttribute(HtmlAttributes.VALUE, facesContext.getExternalContext().getRequestContextPath(), true);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "action-position");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "action-position");
    writer.endElement(HtmlElements.INPUT);

    if (calculateScrollbarWeight) {
      writer.startElement(HtmlElements.DIV, null);
      writer.writeClassAttribute(Classes.create(page, "scrollbarWeight", Markup.NULL));
      writer.startElement(HtmlElements.DIV, null);
      writer.endElement(HtmlElements.DIV);
      writer.endElement(HtmlElements.DIV);

      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "scrollbarWeight");
      writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "scrollbarWeight");
      writer.endElement(HtmlElements.INPUT);
    }

    if (TobagoConfig.getInstance(FacesContext.getCurrentInstance()).isCreateSessionSecret()) {
      Secret.encode(facesContext, writer);
    }

    if (debugMode) {
      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.VALUE, clientLogSeverity);
      writer.writeAttribute(HtmlAttributes.ID, clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity", false);
      writer.writeAttribute(HtmlAttributes.NAME, clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity", false);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.endElement(HtmlElements.INPUT);
    }

    if (component.getFacet("backButtonDetector") != null) {
      UIComponent hidden = component.getFacet("backButtonDetector");
      RenderUtils.encode(facesContext, hidden);
    }

    String lastFocusId = (String) component.getAttributes().get(LAST_FOCUS_ID);
    if (lastFocusId != null) {
      writer.writeJavascript("Tobago.lastFocusId = '" + lastFocusId + "';");
      component.getAttributes().remove(LAST_FOCUS_ID);
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

    UIMenuBar menuBar = (UIMenuBar) page.getFacet(Facets.MENUBAR);
    if (menuBar != null) {
      menuBar.getAttributes().put(Attributes.PAGE_MENU, Boolean.TRUE);
      RenderUtils.encode(facesContext, menuBar);
    }
    // write the previously rendered page content
//    AbstractUILayoutBase.getLayout(component).encodeChildrenOfComponent(facesContext, component);

//    page.encodeLayoutBegin(facesContext);
    
    writer.startElement(HtmlElements.DIV, page);
    writer.writeClassAttribute(Classes.create(page, "content"));
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "content");
    Style style = new Style(facesContext, page);
    // XXX position the div, so that the scrollable area is correct.
    // XXX better to take this fact into layout management.
    // XXX is also useful in boxes, etc.
    Measure border = getBorderBottom(facesContext, page);
    style.setHeight(page.getCurrentHeight().subtract(border));
    style.setTop(border);
    writer.writeStyleAttribute(style);
  }

  private void writeStyle(FacesContext facesContext, TobagoResponseWriter writer, String styleFile)
      throws IOException {
    List<String> styles = ResourceManagerUtils.getStyles(facesContext, styleFile);
    for (String styleString : styles) {
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
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {


    UIPage page = (UIPage) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.endElement(HtmlElements.DIV);

    // write popup components
    // beware of ConcurrentModificationException in cascading popups!
    // no foreach

    UIPopup[] popupArray = FacesContextUtils.getPopups(facesContext).toArray(
        new UIPopup[FacesContextUtils.getPopups(facesContext).size()]);
    for (UIPopup popup : popupArray) {
      RenderUtils.encode(facesContext, popup);
    }

    String clientId = page.getClientId(facesContext);
    final boolean debugMode = VariableResolverUtils.resolveClientProperties(facesContext).isDebugMode();


    // avoid submit page in ie if the form contains only one input and you press the enter key in the input
    if (VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT, false);
      writer.writeAttribute(HtmlAttributes.NAME, "tobago.dummy", false);
      writer.writeAttribute(HtmlAttributes.TABINDEX, "-1", false);
      writer.writeAttribute(HtmlAttributes.STYLE, "visibility:hidden;display:none;", false);
      writer.endElement(HtmlElements.INPUT);
    }

    List<String> messageClientIds = AjaxInternalUtils.getMessagesClientIds(facesContext);
    if (messageClientIds != null) {
      writer.startElement(HtmlElements.INPUT, null);
      writer.writeAttribute(HtmlAttributes.VALUE, StringUtils.join(messageClientIds, ','), true);
      writer.writeAttribute(HtmlAttributes.ID, clientId + ComponentUtils.SUB_SEPARATOR + "messagesClientIds", false);
      writer.writeAttribute(HtmlAttributes.NAME, clientId + ComponentUtils.SUB_SEPARATOR + "messagesClientIds", false);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.endElement(HtmlElements.INPUT);
    }

    // placeholder for menus
    writer.startElement(HtmlElements.DIV, page);
    writer.writeClassAttribute(Classes.create(page, "menuStore"));
    writer.endElement(HtmlElements.DIV);

    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "jsf-state-container");
    writer.flush();
    if (!FacesContextUtils.isAjax(facesContext)) {
      if (FacesVersion.supports12()) {
        viewHandler.writeState(facesContext);
      } else {
        // catch the next written stuff into a string and look if it is empty (TOBAGO-909)
        FastStringWriter buffer = new FastStringWriter(40); // usually only the marker...
        TobagoResponseWriter originalWriter = (TobagoResponseWriter) facesContext.getResponseWriter();
        writer = (TobagoResponseWriter) writer.cloneWithWriter(buffer);
        facesContext.setResponseWriter(writer);
        viewHandler.writeState(facesContext);
        final String stateContent = buffer.toString();
        writer = originalWriter;
        facesContext.setResponseWriter(writer);

        if (StringUtils.isBlank(stateContent)) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Writing state will not happen! So we write the hidden field manually.");
          }
          writer.startElement(HtmlElements.INPUT, null);
          writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
          writer.writeAttribute(HtmlAttributes.NAME, TobagoResponseStateManager.VIEW_STATE_PARAM, false);
          writer.writeAttribute(HtmlAttributes.ID, TobagoResponseStateManager.VIEW_STATE_PARAM, false);
          writer.writeAttribute(HtmlAttributes.VALUE, "workaround", false);
          writer.endElement(HtmlElements.INPUT);
        } else {
          writer.write(stateContent);
        }
      }
    }
    writer.endElement(HtmlElements.SPAN);


    writer.endElement(HtmlElements.FORM);

    // debugging...
    if (debugMode) {
      List<String> logMessages = new ArrayList<String>();
      for (Iterator ids = facesContext.getClientIdsWithMessages();
           ids.hasNext();) {
        String id = (String) ids.next();
        for (Iterator messages = facesContext.getMessages(id);
             messages.hasNext();) {
          FacesMessage message = (FacesMessage) messages.next();
          logMessages.add(errorMessageForDebugging(id, message));
        }
        
      }
      if (!logMessages.isEmpty()) {
        logMessages.add(0, "LOG.show();");
      }

      logMessages.add("LOG.info(\"FacesContext = " + facesContext + "\");");

      HtmlRendererUtils.writeScriptLoader(facesContext, null,
          logMessages.toArray(new String[logMessages.size()]));
    }

    if (debugMode) {
      writer.writeJavascript("TbgTimer.endBody = new Date();");
    }

    writer.writeJavascript("setTimeout(\"Tobago.init('" + clientId + "')\", 1000)");

    writer.endElement(HtmlElements.BODY);

    if (LOG.isDebugEnabled()) {
      LOG.debug("unused AccessKeys    : "
          + AccessKeyMap.getUnusedKeys(facesContext));
      LOG.debug("duplicated AccessKeys: "
          + AccessKeyMap.getDublicatedKeys(facesContext));
    }

    if (facesContext.getExternalContext().getRequestParameterMap().get("X") != null) {
      throw new RuntimeException("Debugging activated via X parameter");
    }
  }

  private void writeEventFunction(TobagoResponseWriter writer, Collection<String> eventFunctions,
      String event, boolean returnBoolean) throws IOException {
    if (!eventFunctions.isEmpty()) {
      writer.write("Tobago.applicationOn");
      writer.write(event);
      writer.write(" = function() {\n");
      if (returnBoolean) {
        writer.write("  var result;\n");
      }
      for (String function : eventFunctions) {
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

  private void encodeScript(FacesContext facesContext, TobagoResponseWriter writer, String script) throws IOException {
    List<String> list;
    if (ResourceManagerUtils.isAbsoluteResource(script)) {
      list = new ArrayList<String>();
      list.add(script);
    } else {
      list = ResourceManagerUtils.getScripts(facesContext, script);
    }
    for (String src : list) {
      if (StringUtils.isNotBlank(src)) {
        writer.startElement(HtmlElements.SCRIPT, null);
        writer.writeAttribute(HtmlAttributes.SRC, src, true);
        writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
        writer.endElement(HtmlElements.SCRIPT);
      }
    }
  }

  private void errorMessageForDebugging(String id, FacesMessage message,
      ResponseWriter writer) throws IOException {
    writer.startElement(HtmlElements.DIV, null);
    writer.writeAttribute(HtmlAttributes.STYLE, "color: red", null);
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

  private String errorMessageForDebugging(String id, FacesMessage message) {
    StringBuilder sb = new StringBuilder("LOG.info(\"FacesMessage: [");
    sb.append(id != null ? id : "null");
    sb.append("][");
    sb.append(message.getSummary() == null ? "null" : escape(message.getSummary()));
    sb.append("/");
    sb.append(message.getDetail() == null ? "null" : escape(message.getDetail()));
    sb.append("]\");");
    return sb.toString();
  }

  private String escape(String s) {
    return StringUtils.replace(StringUtils.replace(s, "\\", "\\\\"), "\"", "\\\"");
  }

  private String getMethod(UIPage page) {
    String method = (String) page.getAttributes().get(Attributes.METHOD);
    return method == null ? "post" : method;
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public Measure getBorderBottom(FacesContext facesContext, Configurable component) {
    // XXX this is a hack. correct would be the top-border, but this would shift the content, because of the
    // XXX hack before the code: writer.writeStyleAttribute(style)
    UIPage page = (UIPage) component;
    UIMenuBar menuBar = (UIMenuBar) page.getFacet(Facets.MENUBAR);
    if (menuBar != null) {
      return getResourceManager().getThemeMeasure(facesContext, page, "custom.menuBar-height");
    } else {
      return Measure.ZERO;
    }
  }

  @Override
  public Measure getWidth(FacesContext facesContext, Configurable component) {
    // width of the actual browser window
    Measure width = (Measure) FacesContext.getCurrentInstance().getExternalContext()
        .getRequestMap().get("tobago-page-clientDimension-width");
    if (width != null) {
      return width;
    } else {
      return super.getWidth(facesContext, component);
    }
  }

  @Override
  public Measure getHeight(FacesContext facesContext, Configurable component) {
    // height of the actual browser window
    Measure height = (Measure) FacesContext.getCurrentInstance().getExternalContext()
        .getRequestMap().get("tobago-page-clientDimension-height");
    if (height != null) {
      return height;
    } else {
      return super.getHeight(facesContext, component);
    }
  }
}

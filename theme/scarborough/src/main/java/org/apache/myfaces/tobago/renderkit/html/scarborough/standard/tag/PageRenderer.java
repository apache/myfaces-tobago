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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.component.UIMenuBar;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.context.ResponseWriterDivider;
import org.apache.myfaces.tobago.internal.layout.LayoutContext;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.internal.util.MimeTypeUtils;
import org.apache.myfaces.tobago.internal.util.ResponseUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageRenderer extends PageRendererBase {

  private static final Log LOG = LogFactory.getLog(PageRenderer.class);

  private static final String CLIENT_DEBUG_SEVERITY = "clientDebugSeverity";
  private static final String LAST_FOCUS_ID = "lastFocusId";

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);
    String clientId = component.getClientId(facesContext);
    ExternalContext externalContext = facesContext.getExternalContext();
    String severity = (String) 
        externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity");
    if (severity != null) {
      externalContext.getRequestMap().put(CLIENT_DEBUG_SEVERITY, severity);
    }
    String lastFocusId = (String) 
        externalContext.getRequestParameterMap().get(clientId + ComponentUtils.SUB_SEPARATOR + LAST_FOCUS_ID);
    if (lastFocusId != null) {
      component.getAttributes().put(LAST_FOCUS_ID, lastFocusId);
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContextOrg, UIComponent component) throws IOException {

    UIPage page = (UIPage) component;

    // invoke prepareRender
    TobagoFacesContext facesContext;
    if (facesContextOrg instanceof TobagoFacesContext) {
      facesContext = (TobagoFacesContext) facesContextOrg;
    } else {
      facesContext = new TobagoFacesContext(facesContextOrg);
    }

// LAYOUT Begin

    long begin = System.nanoTime();
    LayoutContext layoutContext = new LayoutContext(page);
    layoutContext.layout();
    LOG.info("Laying out takes: " + new DecimalFormat("#,##0").format(System.nanoTime() - begin) + " ns");

// LAYOUT End

    RenderUtil.prepareRendererAll(facesContext, page);

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
    HtmlRendererUtils.renderDojoDndSource(facesContext, component);

    String title = (String) page.getAttributes().get(Attributes.LABEL);

    writer.startElement(HtmlConstants.HEAD, null);
    final boolean debugMode = VariableResolverUtils.resolveClientProperties(facesContext).isDebugMode();

    if (debugMode) {
      writer.writeJavascript("var TbgHeadStart = new Date();");
    }

    // meta
    // this is needed, because websphere 6.0? ignores the setting of the content type on the response
    writer.startElement(HtmlConstants.META, null);
    writer.writeAttribute("http-equiv", "Content-Type", false);
    writer.writeAttribute("content", contentType, false);
    writer.endElement(HtmlConstants.META);

    // title
    writer.startElement(HtmlConstants.TITLE, null);
    writer.writeText(title != null ? title : "");
    writer.endElement(HtmlConstants.TITLE);

    // style files
    for (String styleFile : facesContext.getStyleFiles()) {
      List<String> styles = ResourceManagerUtil.getStyles(facesContext, styleFile);
      for (String styleString : styles) {
        if (styleString.length() > 0) {
          writer.startElement(HtmlConstants.LINK, null);
          writer.writeAttribute(HtmlAttributes.REL, "stylesheet", false);
          writer.writeAttribute(HtmlAttributes.HREF, styleString, false);
//          writer.writeAttribute(HtmlAttributes.MEDIA, "screen", false);
          writer.writeAttribute(HtmlAttributes.TYPE, "text/css", false);
          writer.endElement(HtmlConstants.LINK);
        }
      }
    }

    String icon = page.getApplicationIcon();
    if (icon != null) {
      // XXX unify with image renderer
      if (icon.startsWith("HTTP:") || icon.startsWith("FTP:")
          || icon.startsWith("/")) {
        // absolute Path to image : nothing to do
      } else {
        icon = ResourceManagerUtil.getImageWithPath(facesContext, icon);
      }

      writer.startElement(HtmlConstants.LINK, null);
      if (icon.endsWith(".ico")) {
        writer.writeAttribute(HtmlAttributes.REL, "shortcut icon", false);
        writer.writeAttribute(HtmlAttributes.HREF, icon, false);
      } else {
        // XXX IE only supports ICO files for favicons
        writer.writeAttribute(HtmlAttributes.REL, "icon", false);
        writer.writeAttribute(HtmlAttributes.TYPE, MimeTypeUtils.getMimeTypeForFile(icon), false);
        writer.writeAttribute(HtmlAttributes.HREF, icon, false);
      }
      writer.endElement(HtmlConstants.LINK);
    }

    // style sniplets
    Set<String> styleBlocks = facesContext.getStyleBlocks();
    if (styleBlocks.size() > 0) {
      writer.startElement(HtmlConstants.STYLE, null);
      for (String cssBlock : styleBlocks) {
        writer.write(cssBlock);
      }
      writer.endElement(HtmlConstants.STYLE);
    }

    // script files
    List<String> scriptFiles = facesContext.getScriptFiles();
    // jquery.js and tobago.js needs to be first!

    int pos = 0;
//    scriptFiles.add(pos++, debugMode ? "script/jquery/1_3_2/jquery.js" : "script/jquery/1_3_2/jquery.min.js");
//    scriptFiles.add(pos++, debugMode ? "script/jquery/1_4_1/jquery.js" : "script/jquery/1_4_1/jquery.min.js");
    scriptFiles.add(pos++, debugMode ? "script/jquery/1_4_2/jquery.js" : "script/jquery/1_4_2/jquery.min.js");
    scriptFiles.add(pos++, "script/tobago.js");
    scriptFiles.add(pos++, "script/tobago-menu.js");
    scriptFiles.add(pos++, "script/theme-config.js");
    
    int clientLogSeverity = 2;
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
      scriptFiles.add("script/jquery-ui/1_7_2/ui.core.min.js");
      scriptFiles.add("script/jquery-ui/1_7_2/ui.draggable.min.js");
      scriptFiles.add("script/logging.js");
      facesContext.getOnloadScripts().add(0, "new LOG.LogArea({hide: " + hideClientLogging + "});");
    }

    // render remaining script tags
    for (String scriptFile : scriptFiles) {
      encodeScripts(writer, facesContext, scriptFile);
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
        String action;
        if (target != null) {
          action = "Tobago.submitAction(this, '" + command.getClientId(facesContext) + "', "
                  + transition + ", '" + target + "' )";
        } else {
          action = "Tobago.submitAction(this, '"+ command.getClientId(facesContext) + "', " + transition + " )";
        }
        facesContext.getOnloadScripts().add("setTimeout(\"" + action  + "\", " + duration + ");\n");
      }
    }

     if (component.getFacets().containsKey(Facets.RESIZE_ACTION)) {
      UIComponent facet = component.getFacet(Facets.RESIZE_ACTION);
      UIComponent command = null;
      if (facet instanceof UICommand) {
        command = facet;
      } else if (facet instanceof UIForm && facet.getChildCount() == 1) {
        command = (UIComponent) facet.getChildren().get(0);
      }
      if (command != null && command.isRendered()) {
        writer.writeJavascript("Tobago.resizeActionId = '" + command.getClientId(facesContext) + "';");
      }
    }

    writer.startJavascript();
    // onload script
    writeEventFunction(writer, facesContext.getOnloadScripts(), "load", false);

    // onunload script
    writeEventFunction(writer, facesContext.getOnunloadScripts(), "unload", false);

    // onexit script
    writeEventFunction(writer, facesContext.getOnexitScripts(), "exit", false);

    writeEventFunction(writer, facesContext.getOnsubmitScripts(), "submit", true);

   int debugCounter = 0;
   for (String scriptBlock : facesContext.getScriptBlocks()) {

      if (LOG.isDebugEnabled()) {
        LOG.debug("write scriptblock " + ++debugCounter + " :\n" + scriptBlock);
      }
      writer.write(scriptBlock);
      writer.write('\n');
    }
    writer.endJavascript();

    String clientId = page.getClientId(facesContext);

    String defaultActionId = page.getDefaultActionId() != null ? page.getDefaultActionId() : "";
    writer.endElement(HtmlConstants.HEAD);
    writer.startElement(HtmlConstants.BODY, page);
    writer.writeAttribute(HtmlAttributes.ONLOAD, "Tobago.init('" + clientId + "');", false);
//    writer.writeAttribute("onunload", "Tobago.onexit();", null);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute();

    writer.startJavascript();
    writer.write("Tobago.pngFixBlankImage = '");
    writer.write(ResourceManagerUtil.getImageWithPath(facesContext, "image/blank.gif"));
    writer.write("';\n");
    writer.write("Tobago.OVERLAY_BACKGROUND = '");
    writer.write(ResourceManagerUtil.getImageWithPath(facesContext, "image/tobago-overlay-background.png"));
    writer.write("';\n");
    writer.write("Tobago.OVERLAY_WAIT = '");
    writer.write(ResourceManagerUtil.getImageWithPath(facesContext, "image/tobago-overlay-wait.gif"));
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

    writer.startElement(HtmlConstants.FORM, page);
    writer.writeAttribute(HtmlAttributes.ACTION, formAction, true);
    writer.writeIdAttribute(page.getFormId(facesContext));
    writer.writeAttribute(HtmlAttributes.METHOD, getMethod(page), false);
    String enctype = facesContext.getEnctype();
    if (enctype != null) {
      writer.writeAttribute(HtmlAttributes.ENCTYPE, enctype, false);
    }
    // TODO: enable configuration of  'accept-charset'
    writer.writeAttribute(HtmlAttributes.ACCEPT_CHARSET, AbstractUIPage.FORM_ACCEPT_CHARSET, false);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "form-action");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "form-action");
    writer.writeAttribute(HtmlAttributes.VALUE, defaultActionId, true);
    writer.endElement(HtmlConstants.INPUT);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "context-path");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "context-path");
    writer.writeAttribute(HtmlAttributes.VALUE, facesContext.getExternalContext().getRequestContextPath(), true);
    writer.endElement(HtmlConstants.INPUT);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "action-position");
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "action-position");
    writer.endElement(HtmlConstants.INPUT);

    if (debugMode) {
      writer.startElement(HtmlConstants.INPUT, null);
      writer.writeAttribute(HtmlAttributes.VALUE, clientLogSeverity);
      writer.writeAttribute(HtmlAttributes.ID, clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity", false);
      writer.writeAttribute(HtmlAttributes.NAME, clientId + ComponentUtils.SUB_SEPARATOR + "clientSeverity", false);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.endElement(HtmlConstants.INPUT);
    }

    if (component.getFacet("backButtonDetector") != null) {
      UIComponent hidden = component.getFacet("backButtonDetector");
      RenderUtil.encode(facesContext, hidden);
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
      writer.startElement(HtmlConstants.INPUT, null);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", null);
      writer.writeNameAttribute(ViewHandlerImpl.PAGE_ID);
      writer.writeIdAttribute(ViewHandlerImpl.PAGE_ID);
      Object value = facesContext.getViewRoot().getAttributes().get(
          ViewHandlerImpl.PAGE_ID);
      writer.writeAttribute(HtmlAttributes.VALUE, (value != null ? value : ""), null);
      writer.endElement(HtmlConstants.INPUT);
    }
*/

    UIMenuBar menuBar = (UIMenuBar) page.getFacet(Facets.MENUBAR);
    if (menuBar != null) {
      menuBar.getAttributes().put(Attributes.PAGE_MENU, Boolean.TRUE);
      RenderUtil.encode(facesContext, menuBar);
    }
    // write the proviously rendered page content
//    UILayoutBase.getLayout(component).encodeChildrenOfComponent(facesContext, component);

//    page.encodeLayoutBegin(facesContext);
    
    writer.startElement(HtmlConstants.DIV, page);
    writer.writeClassAttribute();
    Style style = new Style(facesContext, page);
    // XXX position the div, so that the scrollable area is correct.
    // XXX better to take this fact into layout management.
    // XXX is also useful in boxes, etc.
    Measure topOffset = getBottomOffset(facesContext, page);
    style.setHeight(page.getCurrentHeight().subtract(topOffset));
    style.setTop(topOffset);
    writer.writeStyleAttribute(style);
  }

//  @Override
//  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
//    UIPage page = (UIPage) component;
//    page.encodeLayoutChildren(facesContext);
//  }

  @Override
  public void encodeEnd(FacesContext facesContextOrg, UIComponent component) throws IOException {

    TobagoFacesContext facesContext;
    if (facesContextOrg instanceof TobagoFacesContext) {
      facesContext = (TobagoFacesContext) facesContextOrg;
    } else {
      facesContext = new TobagoFacesContext(facesContextOrg);
    }

    UIPage page = (UIPage) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.endElement(HtmlConstants.DIV);

    // write popup components
    // beware of ConcurrentModificationException in cascading popups!
    // no foreach
    UIPopup[] popupArray = facesContext.getPopups().toArray(new UIPopup[facesContext.getPopups().size()]);
    for (UIPopup popup : popupArray) {
      RenderUtil.encode(facesContext, popup);
    }

    String clientId = page.getClientId(facesContext);
    final boolean debugMode = VariableResolverUtils.resolveClientProperties(facesContext).isDebugMode();

    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();

    writer.startElement(HtmlConstants.SPAN, null);
    writer.writeIdAttribute(clientId + ComponentUtils.SUB_SEPARATOR + "jsf-state-container");
    writer.flush();
    viewHandler.writeState(facesContext);
    writer.endElement(HtmlConstants.SPAN);

    // avoid submit page in ie if the form contains only one input and you press the enter key in the input
    if (VariableResolverUtils.resolveClientProperties(facesContext).getUserAgent().isMsie()) {
      writer.startElement(HtmlConstants.INPUT, null);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT, false);
      writer.writeAttribute(HtmlAttributes.NAME, "tobago.dummy", false);
      writer.writeAttribute(HtmlAttributes.TABINDEX, "-1", false);
      writer.writeAttribute(HtmlAttributes.STYLE, "visibility:hidden;display:none;", false);
      writer.endElement(HtmlConstants.INPUT);
    }

    // todo: check if it is empty
    writer.startElement(HtmlConstants.DIV, page);
    writer.writeClassAttribute("tobago-page-menuStore");
    ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, MenuBarRenderer.DIVIDER);
    divider.writeOutAndCleanUp(facesContext);
    writer.endElement(HtmlConstants.DIV);
    
    writer.endElement(HtmlConstants.FORM);

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

    writer.endElement(HtmlConstants.BODY);

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

  private void encodeScripts(TobagoResponseWriter writer, FacesContext facesContext, String script) throws IOException {
    List<String> list;
    if (StringUtils.startsWith(script, "/") 
        || StringUtils.startsWithIgnoreCase(script, "HTTP:")
        || StringUtils.startsWithIgnoreCase(script, "FTP:")) {
      list = new ArrayList<String>();
      list.add(script);
    } else {
      list = ResourceManagerUtil.getScripts(facesContext, script);
    }
    for (String src : list) {
      if (StringUtils.isNotBlank(src)) {
        writer.startElement(HtmlConstants.SCRIPT, null);
        writer.writeAttribute(HtmlAttributes.SRC, src, true);
        writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
        writer.endElement(HtmlConstants.SCRIPT);
      }
    }
  }

  private void errorMessageForDebugging(String id, FacesMessage message,
      ResponseWriter writer) throws IOException {
    writer.startElement(HtmlConstants.DIV, null);
    writer.writeAttribute(HtmlAttributes.STYLE, "color: red", null);
    writer.write("[");
    writer.write(id != null ? id : "null");
    writer.write("]");
    writer.write("[");
    writer.write(message.getSummary() == null ? "null" : message.getSummary());
    writer.write("/");
    writer.write(message.getDetail() == null ? "null" : message.getDetail());
    writer.write("]");
    writer.endElement(HtmlConstants.DIV);
    writer.startElement(HtmlConstants.BR, null);
    writer.endElement(HtmlConstants.BR);
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
  public Measure getBottomOffset(FacesContext facesContext, Configurable component) {
    // XXX this is a hack. correct whould be the top-offset, but this would shift the content, because of the 
    // XXX hack before the code: writer.writeStyleAttribute(style)
    UIPage page = (UIPage) component;
    UIMenuBar menuBar = (UIMenuBar) page.getFacet(Facets.MENUBAR);
    if (menuBar != null) {
      return getResourceManager().getThemeMeasure(facesContext, menuBar, "fixedHeight");
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

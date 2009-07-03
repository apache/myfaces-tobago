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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.AbstractUIPage;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.layout.LayoutContext;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.util.MimeTypeUtils;
import org.apache.myfaces.tobago.util.ResponseUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class PageRenderer extends PageRendererBase {

  private static final Log LOG = LogFactory.getLog(PageRenderer.class);

//      values for doctype :
//      'strict'   : HTML 4.01 Strict DTD
//      'loose'    : HTML 4.01 Transitional DTD
//      'frameset' : HTML 4.01 Frameset DTD
//      all other values are ignored and no DOCTYPE is set.
//      default value is 'loose'

  private static final String LOOSE =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""
          + /*" \"http://www.w3.org/TR/html4/loose.dtd\*/">";
  // TODO: this is commented, because the some pages in IE and mozilla
  // does work properly with it:
  // tobago-demo: sometimes the body has not height=100% in mozilla.

  private static final String STRICT =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\""
          + " \"http://www.w3.org/TR/html4/strict.dtd\">";

  private static final String FRAMESET =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\""
          + " \"http://www.w3.org/TR/html4/frameset.dtd\">";
  private static final String CLIENT_DEBUG_SEVERITY = "clientDebugSeverity";


  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
    super.decode(facesContext, component);
    String name = component.getClientId(facesContext) + SUBCOMPONENT_SEP + "clientSeverity";
    ExternalContext externalContext = facesContext.getExternalContext();
    String severity = (String) externalContext.getRequestParameterMap().get(name);
    if (severity != null) {
      externalContext.getRequestMap().put(CLIENT_DEBUG_SEVERITY, severity);
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContextOrg, UIComponent component) throws IOException {

//  public void encodeEnd(FacesContext facesContextOrg, UIComponent component) throws IOException {
    UIPage page = (UIPage) component;

    // invoke prepareRender
    TobagoFacesContext facesContext;
    if (facesContextOrg instanceof TobagoFacesContext) {
      facesContext = (TobagoFacesContext) facesContextOrg;
    } else {
      facesContext = new TobagoFacesContext(facesContextOrg);
    }

// LAYOUT Begin

//    try {
      LayoutContext layoutContext = new LayoutContext(page);
      layoutContext.layout();
//    } catch (Throwable e) {
//      LOG.info("testing... ", e);
//    }

// LAYOUT End

    RenderUtil.prepareRendererAll(facesContext, page);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

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
    HtmlRendererUtil.renderDojoDndSource(facesContext, component);

    String title = (String) page.getAttributes().get(Attributes.LABEL);

    String doctype = generateDoctype(page);

    if (doctype != null) {
      writer.write(doctype);
      writer.write("\n");
    }

    writer.startElement(HtmlConstants.HTML, null);
    writer.startElement(HtmlConstants.HEAD, null);
    final boolean debugMode = ClientProperties.getInstance(facesContext.getViewRoot()).isDebugMode();

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
    // dojo.js and tobago.js needs to be first!
    if (debugMode) {
      addScripts(writer, facesContext, "script/dojo/dojo/dojo.js.uncompressed.js");
    } else {
      addScripts(writer, facesContext, "script/dojo/dojo/dojo.js");
    }
    addScripts(writer, facesContext, "script/tobago.js");
    addScripts(writer, facesContext, "script/theme-config.js");
    // remove  dojo.js and tobago.js from list to prevent dublicated rendering of script tags
    if (debugMode) {
      scriptFiles.remove("script/dojo/dojo/dojo.js.uncompressed.js");
    } else {
      scriptFiles.remove("script/dojo/dojo/dojo.js");
    }
    scriptFiles.remove("script/tobago.js");
    scriptFiles.remove("script/theme-config.js");

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
      scriptFiles.add("script/logging.js");
      facesContext.getOnloadScripts().add(0, "new LOG.LogArea({hide: " + hideClientLogging + "});");
    }

    // render remaining script tags
    for (String scriptFile : scriptFiles) {
      addScripts(writer, facesContext, scriptFile);
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
        int duration = ComponentUtil.getIntAttribute(command, Attributes.DELAY, 100);
        boolean transition = ComponentUtil.getBooleanAttribute(command, Attributes.TRANSITION);
        String target = ComponentUtil.getStringAttribute(command, Attributes.TARGET);
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

    UIComponent menubar = page.getFacet(Facets.MENUBAR);
    if (menubar != null) {
      facesContext.getOnloadScripts().add("Tobago.setElementWidth('"
          + menubar.getClientId(facesContext) + "', Tobago.getBrowserInnerWidth())");
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
    //this ist for ie to prevent scrollbars where none are needed
    writer.writeAttribute(HtmlAttributes.SCROLL, "auto", false);
    writer.writeClassAttribute();
    writer.writeStyleAttribute();
    writer.writeIdAttribute(clientId);

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
      HtmlRendererUtil.writeScriptLoader(facesContext, jsFiles, jsCommand);
      writer.writeJavascript("TbgTimer.startBody = new Date();");
    }
*/
    if (debugMode) {
      writer.writeJavascript("TbgTimer.startBody = new Date();");
    }

    writer.startElement(HtmlConstants.FORM, page);
    writer.writeNameAttribute(clientId + SUBCOMPONENT_SEP + "form");
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
    writer.writeNameAttribute(clientId + SUBCOMPONENT_SEP + "form-action");
    writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + "form-action");
    writer.writeAttribute(HtmlAttributes.VALUE, defaultActionId, true);
    writer.endElement(HtmlConstants.INPUT);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + SUBCOMPONENT_SEP + "context-path");
    writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + "context-path");
    writer.writeAttribute(HtmlAttributes.VALUE, facesContext.getExternalContext().getRequestContextPath(), true);
    writer.endElement(HtmlConstants.INPUT);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(clientId + SUBCOMPONENT_SEP + "action-position");
    writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + "action-position");
    writer.endElement(HtmlConstants.INPUT);

    if (debugMode) {
      writer.startElement(HtmlConstants.INPUT, null);
      writer.writeAttribute(HtmlAttributes.VALUE, clientLogSeverity);
      writer.writeAttribute(HtmlAttributes.ID, clientId + SUBCOMPONENT_SEP + "clientSeverity", false);
      writer.writeAttribute(HtmlAttributes.NAME, clientId + SUBCOMPONENT_SEP + "clientSeverity", false);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.endElement(HtmlConstants.INPUT);
    }

    if (component.getFacet("backButtonDetector") != null) {
      UIComponent hidden = component.getFacet("backButtonDetector");
      RenderUtil.encode(facesContext, hidden);
    }

    String lastFocusIdParameter = component.getClientId(facesContext) + SUBCOMPONENT_SEP + "lastFocusId";
    String lastFocusId = (String) facesContext.getExternalContext().getRequestParameterMap().get(lastFocusIdParameter);
    if (lastFocusId != null) {
      writer.writeJavascript("Tobago.lastFocusId = '" + lastFocusId + "';");
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

    if (menubar != null) {
      menubar.getAttributes().put(Attributes.PAGE_MENU, Boolean.TRUE);
      RenderUtil.encode(facesContext, menubar);
    }
    // write the proviously rendered page content
//    UILayout.getLayout(component).encodeChildrenOfComponent(facesContext, component);

//    page.encodeLayoutBegin(facesContext);
  }

//  @Override
//  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
//    UIPage page = (UIPage) component;
//    page.encodeLayoutChildren(facesContext);
//  }

  @Override
  public void encodeEnd(FacesContext facesContextOrg, UIComponent component) throws IOException {

    UIPage page = (UIPage) component;

    TobagoFacesContext facesContext;
    if (facesContextOrg instanceof TobagoFacesContext) {
      facesContext = (TobagoFacesContext) facesContextOrg;
    } else {
      facesContext = new TobagoFacesContext(facesContextOrg);
    }

//    page.encodeLayoutEnd(facesContext);

    // write popup components
    // beware of ConcurrentModificationException in cascating popups!
    // no foreach
    UIPopup[] popupArray = facesContext.getPopups().toArray(new UIPopup[facesContext.getPopups().size()]);
    for (UIPopup popup : popupArray) {
      RenderUtil.encode(facesContext, popup);
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    String clientId = page.getClientId(facesContext);
    final boolean debugMode = ClientProperties.getInstance(facesContext.getViewRoot()).isDebugMode();

    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();

    writer.startElement(HtmlConstants.SPAN, null);
    writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + "jsf-state-container");
    writer.flush();
    viewHandler.writeState(facesContext);
    writer.endElement(HtmlConstants.SPAN);

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

      HtmlRendererUtil.writeScriptLoader(facesContext, null,
          logMessages.toArray(new String[logMessages.size()]));
    }

    if (debugMode) {
      writer.writeJavascript("TbgTimer.endBody = new Date();");
    }

    writer.writeJavascript("setTimeout(\"Tobago.init('" + clientId + "')\", 1000)");

    writer.endElement(HtmlConstants.BODY);
    writer.endElement(HtmlConstants.HTML);

    if (LOG.isDebugEnabled()) {
      LOG.debug("unused AccessKeys    : "
          + AccessKeyMap.getUnusedKeys(facesContext));
      LOG.debug("dublicated AccessKeys: "
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

  private void addScripts(TobagoResponseWriter writer, FacesContext facesContext,
      String script) throws IOException {
    List<String> scripts;
    final String ucScript = script.toUpperCase(Locale.ENGLISH);
    if (ucScript.startsWith("HTTP:") || ucScript.startsWith("FTP:")
        || ucScript.startsWith("/")) {
      scripts = new ArrayList<String>();
      scripts.add(script);
    } else {
      scripts = ResourceManagerUtil.getScripts(facesContext, script);
    }
    for (String scriptString : scripts) {
      if (scriptString.length() > 0) {
        writer.startElement(HtmlConstants.SCRIPT, null);
        writer.writeAttribute(HtmlAttributes.SRC, scriptString, true);
        writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
        writer.endElement(HtmlConstants.SCRIPT);
      }
    }
  }

//  public void encodeEnd(FacesContext facesContext, UIComponent component)
//  public void encodeChildren(FacesContext facesContext, UIComponent component)
//      throws IOException {
//todo: remove this comment    // children are encoded in encodeEndTobago(...)
//  }

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
    sb.append(message.getSummary() == null ? "null"
        : message.getSummary().replace("\\", "\\\\").replace("\"", "\\\""));
    sb.append("/");
    sb.append(message.getDetail() == null ? "null"
        : message.getDetail().replace("\\", "\\\\").replace("\"", "\\\""));
    sb.append("]\");");
    return sb.toString();
  }

  private String getMethod(UIPage page) {
    String method = (String) page.getAttributes().get(Attributes.METHOD);
    return method == null ? "post" : method;
  }

  protected String generateDoctype(UIPage page) {
    String doctype = (String) page.getAttributes().get(Attributes.DOCTYPE);
    String type = null;
    if (doctype == null || "loose".equals(doctype)) {
      //default
      type = LOOSE;
    } else if ("strict".equals(doctype)) {
      type = STRICT;
    } else if ("frameset".equals(doctype)) {
      type = FRAMESET;
    } else {
      LOG.warn("Unsupported DOCTYPE keyword :'" + doctype + "'");
    }
    return type;
  }

  public boolean getRendersChildren() {
    return true;
  }

}


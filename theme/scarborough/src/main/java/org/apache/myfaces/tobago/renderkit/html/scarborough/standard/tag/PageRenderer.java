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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CHARSET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DOCTYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ENCTYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_METHOD;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_PAGE_MENU;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_MENUBAR;
import static org.apache.myfaces.tobago.TobagoConstants.FORM_ACCEPT_CHARSET;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.UILayout;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.util.ResponseUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ExternalContext;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Locale;

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


    public void decode(FacesContext facesContext, UIComponent component) {
        super.decode(facesContext, component);
        String name = component.getClientId(facesContext) + SUBCOMPONENT_SEP + "clientSeverity";
        ExternalContext externalContext = facesContext.getExternalContext();
        String severity = (String) externalContext.getRequestParameterMap().get(name);
        if (severity != null) {
          externalContext.getRequestMap().put(CLIENT_DEBUG_SEVERITY, severity);
        }
    }

  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIPage page = (UIPage) component;

    HtmlRendererUtil.prepareRender(facesContext, page);

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    // replace responseWriter and render page content
    StringWriter content = new StringWriter();
    ResponseWriter contentWriter = writer.cloneWithWriter(content);
    facesContext.setResponseWriter(contentWriter);

    UIComponent menubar = page.getFacet(FACET_MENUBAR);
    if (menubar != null) {
      menubar.getAttributes().put(ATTR_PAGE_MENU, Boolean.TRUE);
      page.getOnloadScripts().add("Tobago.setElementWidth('"
          + menubar.getClientId(facesContext) + "', Tobago.getBrowserInnerWidth())");
      RenderUtil.encode(facesContext, menubar);
    }

    UILayout.getLayout(component).encodeChildrenOfComponent(facesContext, component);
//    RenderUtil.encodeChildren(facesContext, page);

// render popups into buffer
    StringWriter popups = new StringWriter();
    contentWriter = writer.cloneWithWriter(popups);
    facesContext.setResponseWriter(contentWriter);

    // write popup components
    // beware of ConcurrentModificationException in cascating popups!
    for (int i = 0; i < page.getPopups().size(); i++) {
      UIComponent popup = page.getPopups().get(i);
      RenderUtil.encode(facesContext, popup);
    }
    

    // reset responseWriter and render page
    facesContext.setResponseWriter(writer);

    ResponseUtils.ensureNoCacheHeader(facesContext.getExternalContext());

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

    String charset = (String) page.getAttributes().get(ATTR_CHARSET);
    ResponseUtils.ensureContentTypeHeader(facesContext, charset);

    String title = (String) page.getAttributes().get(ATTR_LABEL);

    String doctype = generateDoctype(page);

    if (doctype != null) {
      writer.write(doctype);
      writer.write('\n');
    }

    writer.startElement(HtmlConstants.HTML, null);
    writer.startElement(HtmlConstants.HEAD, null);

    HtmlRendererUtil.writeJavascript(writer, "var TbgHeadStart = new Date();");

    // meta
    // TODO duplicate; see PageTag.doStartTag()
//    writer.startElement(HtmlConstants.META, null);
//    writer.writeAttribute("http-equiv", "Content-Type", null);
//    writer.writeAttribute(
//        "content", generateContentType(facesContext, charset), null);
//    writer.endElement(HtmlConstants.META);
    // title
    writer.startElement(HtmlConstants.TITLE, null);
    writer.writeText(title != null ? title : "", null);
    writer.endElement(HtmlConstants.TITLE);

    // style files
    for (String styleFile : page.getStyleFiles()) {
      List<String> styles = ResourceManagerUtil.getStyles(facesContext, styleFile);
      for (String styleString : styles) {
        if (styleString.length() > 0) {
          writer.startElement(HtmlConstants.LINK, null);
          writer.writeAttribute(HtmlAttributes.REL, "stylesheet", null);
          writer.writeAttribute(HtmlAttributes.HREF, styleString, null);
          writer.writeAttribute(HtmlAttributes.MEDIA, "screen", null);
          writer.writeAttribute(HtmlAttributes.TYPE, "text/css", null);
          writer.endElement(HtmlConstants.LINK);
        }
      }
    }

    // style sniplets
    Set<String> styleBlocks = page.getStyleBlocks();
    if (styleBlocks.size() > 0) {
      writer.startElement(HtmlConstants.STYLE, null);
      for (String cssBlock : styleBlocks) {
        writer.write(cssBlock);
      }
      writer.endElement(HtmlConstants.STYLE);
    }

    // script files
    List<String> scriptFiles = page.getScriptFiles();
    // prototype.js and tobago.js needs to be first!
    addScripts(writer, facesContext, "script/prototype.js");
    addScripts(writer, facesContext, "script/tobago.js");
    addScripts(writer, facesContext, "script/theme-config.js");
    // remove  prototype.js and tobago.js from list to prevent dublicated rendering of script tags
    scriptFiles.remove("script/prototype.js");
    scriptFiles.remove("script/tobago.js");
    scriptFiles.remove("script/theme-config.js");

    int clientLogSeverity = 2;
    boolean hideClientLogging = true;
    final boolean debugMode =
        ClientProperties.getInstance(facesContext.getViewRoot()).isDebugMode();
//        true; hideClientLogging = false;
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
        hideClientLogging = !severity.contains("show");
      }
      scriptFiles.add("script/effects.js");
      scriptFiles.add("script/dragdrop.js");
      scriptFiles.add("script/logging.js");
    }

    // render remaining script tags
    for (String scriptFile : scriptFiles) {
      addScripts(writer, facesContext, scriptFile);
    }

    // focus id
    String focusId = page.getFocusId();
    if (focusId != null) {
      HtmlRendererUtil.writeJavascript(writer, "Tobago.focusId = '" + focusId + "';");
    }

    HtmlRendererUtil.startJavascript(writer);
    // onload script
    writeEventFunction(writer, page.getOnloadScripts(), "load");

    // onunload script
    writeEventFunction(writer, page.getOnunloadScripts(), "unload");

    // onexit script
    writeEventFunction(writer, page.getOnexitScripts(), "exit");


    int debugCounter = 0;
    for (String script : page.getScriptBlocks()) {

      if (LOG.isDebugEnabled()) {
        LOG.debug("write scriptblock " + ++debugCounter + " :\n" + script);
      }
      writer.write(script);
      writer.write('\n');
    }

    String clientId = page.getClientId(facesContext);

    HtmlRendererUtil.endJavascript(writer);
//    writer.endElement("script");
    String defaultActionId = page.getDefaultActionId() != null
        ? page.getDefaultActionId() : "";
    writer.endElement(HtmlConstants.HEAD);
    writer.startElement(HtmlConstants.BODY, page);
    writer.writeAttribute(HtmlAttributes.ONLOAD,
        "Tobago.init('" + clientId + "');", null);
//    writer.writeAttribute("onunload", "Tobago.onexit();", null);
    //this ist for ie to prevent scrollbars where none are needed
    writer.writeAttribute(HtmlAttributes.SCROLL, "auto", null);
    writer.writeComponentClass();
    writer.writeIdAttribute(clientId);

    String blank = ResourceManagerUtil.getImageWithPath(facesContext, "image/blank.gif");

    HtmlRendererUtil.writeJavascript(writer, "Tobago.pngFixBlankImage = \"" + blank + "\";");

      if (debugMode) {
      final String[] jsFiles = new String[] {
          "script/effects.js",
          "script/dragdrop.js",
          "script/logging.js"
      };
      final String[] jsCommand
          = new String[]{"new LOG.LogArea({hide: " + hideClientLogging + "});"};
      HtmlRendererUtil.writeScriptLoader(facesContext, jsFiles, jsCommand);
    }
    HtmlRendererUtil.writeJavascript(writer, "TbgTimer.startBody = new Date();");

    writer.startElement(HtmlConstants.FORM, page);
    writer.writeNameAttribute(
        clientId + SUBCOMPONENT_SEP + "form");
    writer.writeAttribute(HtmlAttributes.ACTION, formAction, null);
    writer.writeIdAttribute(page.getFormId(facesContext));
    writer.writeAttribute(HtmlAttributes.METHOD, getMethod(page), null);
    writer.writeAttribute(HtmlAttributes.ENCTYPE, null, ATTR_ENCTYPE);
    // TODO: enable configuration of  'accept-charset'
    writer.writeAttribute(HtmlAttributes.ACCEPT_CHARSET, FORM_ACCEPT_CHARSET, null);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", null);
    writer.writeNameAttribute(
        clientId + SUBCOMPONENT_SEP + "form-action");
    writer.writeIdAttribute(
        clientId + SUBCOMPONENT_SEP + "form-action");
    writer.writeAttribute(HtmlAttributes.VALUE, defaultActionId, null);
    writer.endElement(HtmlConstants.INPUT);

    if (debugMode) {
      writer.startElement(HtmlConstants.INPUT);
      writer.writeAttribute(HtmlAttributes.VALUE, clientLogSeverity, null);
      writer.writeAttribute(HtmlAttributes.ID, clientId + SUBCOMPONENT_SEP + "clientSeverity", null);
      writer.writeAttribute(HtmlAttributes.NAME, clientId + SUBCOMPONENT_SEP + "clientSeverity", null);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", null);
      writer.endElement(HtmlConstants.INPUT);
    }

    if (component.getFacet("backButtonDetector") != null) {
      UIComponent hidden = component.getFacet("backButtonDetector");
      RenderUtil.encode(facesContext, hidden);
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

    // write the proviously rendered page content
    writer.write(content.toString());

    // write the previously rendered popups
    writer.write(popups.toString());

    writer.startElement(HtmlConstants.SPAN);
    writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + "jsf-state-container");
    viewHandler.writeState(facesContext);
    writer.endElement(HtmlConstants.SPAN);


//    facesContext.getApplication().getViewHandler().writeState(facesContext);

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

    HtmlRendererUtil.writeJavascript(writer, "TbgTimer.endBody = new Date();");
    writer.endElement(HtmlConstants.BODY);
    writer.endElement(HtmlConstants.HTML);

    if (LOG.isDebugEnabled()) {
      LOG.debug("unused AccessKeys    : "
          + AccessKeyMap.getUnusedKeys(facesContext));
      LOG.debug("dublicated AccessKeys: "
          + AccessKeyMap.getDublicatedKeys(facesContext));
    }
  }

  private void writeEventFunction(
      TobagoResponseWriter writer, Set<String> eventFunctions, String event)
      throws IOException {
    if (!eventFunctions.isEmpty()) {
      writer.write("Tobago.applicationOn" + event + " = function() {\n  ");
      for (String function : eventFunctions) {
        writer.write(function);
        if (!function.trim().endsWith(";")) {
          writer.write(";\n  ");
        } else {
          writer.write("\n  ");
        }
      }
      writer.write("\n}\n");
    }
  }

  private void addScripts(ResponseWriter writer, FacesContext facesContext,
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
        writer.writeAttribute(HtmlAttributes.SRC, scriptString, null);
        writer.writeAttribute(HtmlAttributes.TYPE, "text/javascript", null);
        writer.endElement(HtmlConstants.SCRIPT);
      }
    }
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
    // children are encoded in encodeEndTobago(...)
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
    sb.append(message.getSummary() == null ? "null"
        : message.getSummary().replace("\\", "\\\\").replace("\"", "\\\""));
    sb.append("/");
    sb.append(message.getDetail() == null ? "null"
        : message.getDetail().replace("\\", "\\\\").replace("\"", "\\\""));
    sb.append("]\");");
    return sb.toString();
  }

  private String getMethod(UIPage page) {
    String method = (String) page.getAttributes().get(ATTR_METHOD);
    return method == null?"post":method;
  }

  private String generateDoctype(UIPage page) {
    String doctype = (String) page.getAttributes().get(ATTR_DOCTYPE);
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


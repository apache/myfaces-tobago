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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DELAY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DOCTYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ENCTYPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_METHOD;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_PAGE_MENU;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TARGET;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TRANSITION;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_ACTION;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_MENUBAR;
import static org.apache.myfaces.tobago.TobagoConstants.FORM_ACCEPT_CHARSET;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UILayout;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.util.FastStringWriter;
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

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    // replace responseWriter and render page content
    FastStringWriter content = new FastStringWriter(1024*10);
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
    FastStringWriter popups = new FastStringWriter();
    contentWriter = writer.cloneWithWriter(popups);
    facesContext.setResponseWriter(contentWriter);

    // write popup components
    // beware of ConcurrentModificationException in cascating popups!
    // no foreach
    UIPopup[] popupArray = page.getPopups().toArray(new UIPopup[page.getPopups().size()] );
    for (int i = 0; i < popupArray.length; i++) {
      UIComponent popup = popupArray[i];
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
    formAction = facesContext.getExternalContext().encodeActionURL(formAction);
    String charset = (String) page.getAttributes().get(ATTR_CHARSET);
    ResponseUtils.ensureContentTypeHeader(facesContext, charset);

    String title = (String) page.getAttributes().get(ATTR_LABEL);

    String doctype = generateDoctype(page);

    if (doctype != null) {
      writer.write(doctype);
      writer.write("\n");
    }

    writer.startElement(HtmlConstants.HTML, null);
    writer.startElement(HtmlConstants.HEAD, null);
    final boolean debugMode =
            ClientProperties.getInstance(facesContext.getViewRoot()).isDebugMode();

    //if (debugMode) {
    writer.writeJavascript("var TbgHeadStart = new Date();");
    //}
    // meta
    // TODO duplicate; see PageTag.doStartTag()
//    writer.startElement(HtmlConstants.META, null);
//    writer.writeAttribute("http-equiv", "Content-Type", null);
//    writer.writeAttribute(
//        "content", generateContentType(facesContext, charset), null);
//    writer.endElement(HtmlConstants.META);
    // title
    writer.startElement(HtmlConstants.TITLE, null);
    writer.writeText(title != null ? title : "");
    writer.endElement(HtmlConstants.TITLE);

    // style files
    for (String styleFile : page.getStyleFiles()) {
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
      writer.writeJavascript("Tobago.focusId = '" + focusId + "';");
    }

    if (component.getFacets().containsKey(FACET_ACTION)) {
      UIComponent command = component.getFacet(FACET_ACTION);
      if (command != null && command.isRendered()) {
        int duration = ComponentUtil.getIntAttribute(command, ATTR_DELAY, 100);
        boolean transition = ComponentUtil.getBooleanAttribute(command, ATTR_TRANSITION);
        String target = ComponentUtil.getStringAttribute(command, ATTR_TARGET);
        String action;
        if (target != null) {
          action = "Tobago.submitAction('" + command.getClientId(facesContext) + "', " 
                  + transition + ", '" + target + "' )";
        } else {
          action = "Tobago.submitAction('"+ command.getClientId(facesContext) + "', " + transition + " )";
        }
        page.getOnloadScripts().add("setTimeout(\"" + action  + "\", " + duration + ");\n");
      }
    }
    StringBuilder script = new StringBuilder();

    // onload script
    writeEventFunction(script, page.getOnloadScripts(), "load", false);

    // onunload script
    writeEventFunction(script, page.getOnunloadScripts(), "unload", false);

    // onexit script
    writeEventFunction(script, page.getOnexitScripts(), "exit", false);

    writeEventFunction(script, page.getOnsubmitScripts(), "submit", true);

   int debugCounter = 0;
    for (String scriptBlock : page.getScriptBlocks()) {

      if (LOG.isDebugEnabled()) {
        LOG.debug("write scriptblock " + ++debugCounter + " :\n" + scriptBlock);
      }
      script.append(scriptBlock);
      script.append('\n');
    }
    writer.writeJavascript(script.toString());

    String clientId = page.getClientId(facesContext);

    String defaultActionId = page.getDefaultActionId() != null ? page.getDefaultActionId() : "";
    writer.endElement(HtmlConstants.HEAD);
    writer.startElement(HtmlConstants.BODY, page);
    writer.writeAttribute(HtmlAttributes.ONLOAD, "Tobago.init('" + clientId + "');", false);
//    writer.writeAttribute("onunload", "Tobago.onexit();", null);
    //this ist for ie to prevent scrollbars where none are needed
    writer.writeAttribute(HtmlAttributes.SCROLL, "auto", false);
    writer.writeClassAttribute();
    writer.writeIdAttribute(clientId);

    StringBuilder images = new StringBuilder();
    images.append("Tobago.pngFixBlankImage = '");
    images.append(ResourceManagerUtil.getImageWithPath(facesContext, "image/blank.gif"));
    images.append("';\n");
    images.append("Tobago.OVERLAY_BACKGROUND = '");
    images.append(ResourceManagerUtil.getImageWithPath(facesContext, "image/tobago-overlay-background.png"));
    images.append("';\n");
    images.append("Tobago.OVERLAY_WAIT = '");
    images.append(ResourceManagerUtil.getImageWithPath(facesContext, "image/tobago-overlay-wait.gif"));
    images.append("';\n");
    writer.writeJavascript(images.toString());

    if (debugMode) {
      final String[] jsFiles = new String[]{
          "script/effects.js",
          "script/dragdrop.js",
          "script/logging.js"
      };
      final String[] jsCommand = new String[]{"new LOG.LogArea({hide: " + hideClientLogging + "});"};
      HtmlRendererUtil.writeScriptLoader(facesContext, jsFiles, jsCommand);
    }
    //if (debugMode)  {
    writer.writeJavascript("TbgTimer.startBody = new Date();");
    //}

    writer.startElement(HtmlConstants.FORM, page);
    writer.writeNameAttribute(
        clientId + SUBCOMPONENT_SEP + "form");
    writer.writeAttribute(HtmlAttributes.ACTION, formAction, true);
    writer.writeIdAttribute(page.getFormId(facesContext));
    writer.writeAttribute(HtmlAttributes.METHOD, getMethod(page), false);
    String enctype = (String) facesContext.getExternalContext().getRequestMap().get(UIPage.ENCTYPE_KEY);
    if (enctype != null) {
      writer.writeAttribute(HtmlAttributes.ENCTYPE, enctype, false); 
    } else {
      writer.writeAttributeFromComponent(HtmlAttributes.ENCTYPE, ATTR_ENCTYPE);
    }
    // TODO: enable configuration of  'accept-charset'
    writer.writeAttribute(HtmlAttributes.ACCEPT_CHARSET, FORM_ACCEPT_CHARSET, false);

    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeNameAttribute(
        clientId + SUBCOMPONENT_SEP + "form-action");
    writer.writeIdAttribute(
        clientId + SUBCOMPONENT_SEP + "form-action");
    writer.writeAttribute(HtmlAttributes.VALUE, defaultActionId, true);
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

    writer.startElement(HtmlConstants.SPAN, null);
    writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + "jsf-state-container");
    writer.flush();
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

    //if (debugMode) {
    writer.writeJavascript("TbgTimer.endBody = new Date();");
    //}
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
      StringBuilder script, Set<String> eventFunctions, String event, boolean returnBoolean)
      throws IOException {
    if (!eventFunctions.isEmpty()) {
      script.append("Tobago.applicationOn");
      script.append(event);
      script.append(" = function() {\n");
      if (returnBoolean) {
        script.append("  var result;\n");
      }
      for (String function : eventFunctions) {
        if (returnBoolean) {
          script.append("  result = ");
        } else {
          script.append("  ");
        }
        script.append(function);
        if (!function.trim().endsWith(";")) {
          script.append(";\n");
        } else {
          script.append("\n");
        }
        if (returnBoolean) {
          script.append("  if (typeof result == \"boolean\" && ! result) {\n");
          script.append("    return false;\n");
          script.append("  }\n");
        }
      }
      script.append("\n  return true;\n}\n");
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
    return method == null ? "post" : method;
  }

  protected String generateDoctype(UIPage page) {
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


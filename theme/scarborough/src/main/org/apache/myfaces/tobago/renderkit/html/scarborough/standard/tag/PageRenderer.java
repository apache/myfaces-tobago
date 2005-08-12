/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.UILayout;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.PageRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.taglib.component.PageTag;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PageRenderer extends PageRendererBase {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(PageRenderer.class);

//      values for doctype :
//      'strict'   : HTML 4.01 Strict DTD
//      'loose'    : HTML 4.01 Transitional DTD
//      'frameset' : HTML 4.01 Frameset DTD
//      all other values are ignored and no DOCTYPE is set.
//      default value is 'loose'

  private static final String LOOSE =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" +
      /*" \"http://www.w3.org/TR/html4/loose.dtd\*/">";
  // todo: this is commented, because the some pages in IE and mozilla
  // does work properly with it:
  // tobago-demo: sometimes the body has not height=100% in mozilla.

  private static final String STRICT =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"" +
      " \"http://www.w3.org/TR/html4/strict.dtd\">";

  private static final String FRAMESET =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Frameset//EN\"" +
      " \"http://www.w3.org/TR/html4/frameset.dtd\">";

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeEndTobago(FacesContext facesContext,
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
      page.getOnloadScripts().add("setDivWidth('"
          + menubar.getClientId(facesContext) + "', getBrowserInnerWidth())");
      RenderUtil.encode(facesContext, menubar);
    }

    UILayout.getLayout(component).encodeChildrenOfComponent(facesContext, component);
//    RenderUtil.encodeChildren(facesContext, page);

    // reset responseWriter and render page
    facesContext.setResponseWriter(writer);

    HttpServletResponse response = (HttpServletResponse)
        facesContext.getExternalContext().getResponse();

    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 1);

    if (LOG.isDebugEnabled()) {
      for (Iterator i = page.getAttributes().entrySet().iterator();
          i.hasNext();) {
        Map.Entry entry = (Map.Entry) i.next();
        LOG.debug("*** '" + entry.getKey() + "' -> '" + entry.getValue() + "'");
      }
    }

    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();
    String viewId = facesContext.getViewRoot().getViewId();
    String formAction = viewHandler.getActionURL(facesContext, viewId);

    String charset = (String) page.getAttributes().get(ATTR_CHARSET);

    String title = (String) page.getAttributes().get(ATTR_LABEL);

    String doctype = generateDoctype(page);

    if (doctype != null) {
      writer.write(doctype);
      writer.write('\n');
    }

    writer.startElement("html", null);
    writer.startElement("head", null);

    // meta
    // TODO duplicate; see PageTag.doStartTag()
    writer.startElement("meta", null);
    writer.writeAttribute("http-equiv", "Content-Type", null);
    writer.writeAttribute(
        "content", PageTag.generateContentType(charset), null);
    writer.endElement("meta");

    // title
    writer.startElement("title", null);
    writer.writeText(title != null ? title : "", null);
    writer.endElement("title");

    // style files
    for (String styleFile : page.getStyleFiles()) {
      List<String> styles = ResourceManagerUtil.getStyles(facesContext, styleFile);
      for (String styleString : styles) {
        if (styleString.length() > 0) {
          writer.startElement("link", null);
          writer.writeAttribute("rel", "stylesheet", null);
          writer.writeAttribute("href", styleString, null);
          writer.writeAttribute("media", "screen", null);
          writer.writeAttribute("type", "text/css", null);
          writer.endElement("link");
        }
      }
    }

    // style sniplets
    Set<String> styleBlocks = page.getStyleBlocks();
    if (styleBlocks.size() > 0) {
      writer.startElement("style", null);
      for (String cssBlock : styleBlocks) {
        writer.write(cssBlock);
      }
      writer.endElement("style");
    }

    // script files
    List<String> scriptFiles = page.getScriptFiles();
    scriptFiles.add(0, "script/tobago.js");
    for (Iterator i = scriptFiles.iterator(); i.hasNext();) {
      String script = (String) i.next();
      addScripts(writer, facesContext, script);
    }

    // focus id
    String focusId = page.getFocusId();
    if (focusId != null) {
      HtmlRendererUtil.writeJavascript(writer, "focusId = '" + focusId + "';");

//      writer.startElement("script", null);
//      writer.writeAttribute("type", "text/javascript", null);
//      writer.write("focusId = '");
//      writer.write(focusId);
//      writer.write("';");
//      writer.endElement("script");
    }

    // onload script
    HtmlRendererUtil.startJavascript(writer);
//    writer.startElement("script", null);
//    writer.writeAttribute("type", "text/javascript", null);
    writer.write("function onloadScript() {\n");
    writer.write("onloadScriptDefault();\n");

    for (String onload : page.getOnloadScripts()) {
      writer.write(onload);
      writer.write('\n');
    }
    writer.write("}\n");

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

    writer.endElement("head");
    writer.startElement("body", page);
    writer.writeAttribute("onload", "onloadScript()", null);
    //this ist for ie to prevent scrollbars where none are needed
    writer.writeAttribute("scroll", "auto", null);
    writer.writeComponentClass();
    writer.writeIdAttribute(clientId);



    writer.startElement("form", page);
    writer.writeNameAttribute(
        clientId + TobagoConstants.SUBCOMPONENT_SEP + "form");
    writer.writeAttribute("action", formAction, null);
    writer.writeIdAttribute(page.getFormId(facesContext));
    writer.writeAttribute("method", null, TobagoConstants.ATTR_METHOD);
    writer.writeAttribute("enctype", null, TobagoConstants.ATTR_ENCTYPE);
    // Todo: enable configuration of  'accept-charset'
    writer.writeAttribute("accept-charset", FORM_ACCEPT_CHARSET, null);

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute(
        clientId + TobagoConstants.SUBCOMPONENT_SEP + "form-action");
    writer.writeIdAttribute(
        clientId + TobagoConstants.SUBCOMPONENT_SEP + "form-action");
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

// todo: this is needed for the "BACK-BUTTON-PROBLEM"
// but may no longer needed
/*
    if (ViewHandlerImpl.USE_VIEW_MAP) {
      writer.startElement("input", null);
      writer.writeAttribute("type", "hidden", null);
      writer.writeNameAttribute(ViewHandlerImpl.PAGE_ID);
      writer.writeIdAttribute(ViewHandlerImpl.PAGE_ID);
      Object value = facesContext.getViewRoot().getAttributes().get(
          ViewHandlerImpl.PAGE_ID);
      writer.writeAttribute("value", (value != null ? value : ""), null);
      writer.endElement("input");
    }
*/

    // write the proviously rendered page content
    writer.write(content.toString());

    // write popup components
    List popups = (List) page.getAttributes().get(ATTR_POPUP_LIST);
    if (popups != null) {
      for (Iterator iter = popups.iterator(); iter.hasNext();) {
        RenderUtil.encode(facesContext, (UIComponent) iter.next());
      }
    }

    writer.endElement("form");

    // debugging...
    if (ClientProperties.getInstance(FacesContext.getCurrentInstance().getViewRoot())
        .isDebugMode()) {

      for (Iterator ids = facesContext.getClientIdsWithMessages();
          ids.hasNext();) {
        String id = (String) ids.next();
        for (Iterator messages = facesContext.getMessages(id);
            messages.hasNext();) {
          FacesMessage message = (FacesMessage) messages.next();
          errorMessageForDebugging(id, message, writer);
        }
      }
/*      writer.writeText("FacesContext = " + facesContext, null);
      String keys = "";
      if (LOG.isInfoEnabled()) {
        keys = "<li> unused accessKeys : "
            + AccessKeyMap.getUnusedKeys(facesContext) + "</li>";
        String dublicated = AccessKeyMap.getDublicatedKeys(facesContext);
        if (dublicated.length() > 0) {
          keys += "<li> dublicated accessKeys : " + dublicated + "</li>";
        }
      }

      writer.write("<div onmousedown=\"tobagoJsLogMouseDown(event)\" " +
          "onmousemove=\"tobagoJsLogMouseMove(event)\" " +
          "onmouseup=\"tobagoJsLogMouseUp()\" " +
          "id=\"LogDiv\" " +
          "style=\"position:  absolute; right: 2px; top: 30px; " +
          "height: 500px;width: 400px; overflow: auto;border:1px solid red; " +
          "background: #ffffff;\">" +
          "<ol id=\"Log\" style=\"font-family:Arial,sans-serif; " +
          "font-size:10pt\"><li>Ereignisliste</li> " + keys + "</ol> </div>");
*/

    }

    writer.endElement("body");
    writer.endElement("html");
  }

// ----------------------------------------------------------- business methods

  private void addScripts(ResponseWriter writer, FacesContext facesContext,
      String script) throws IOException {
    List<String> scripts;
    final String ucScript = script.toUpperCase();
    if (ucScript.startsWith("HTTP:") || ucScript.startsWith("FTP:")
        || ucScript.startsWith("/")) {
      scripts = new ArrayList<String>();
      scripts.add(script);
    } else {
      scripts = ResourceManagerUtil.getScripts(facesContext, script);
    }
    for (String scriptString : scripts) {
      if (scriptString.length() > 0 ) {
        writer.startElement("script", null);
        writer.writeAttribute("src", scriptString, null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.endElement("script");
      }
    }
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
    // children are encoded in encodeEndTobago(...)
  }

  private void errorMessageForDebugging(String id, FacesMessage message,
      ResponseWriter writer) throws IOException {
    writer.startElement("div", null);
    writer.writeAttribute("style", "color: red", null);
    writer.write("[");
    writer.write(id != null ? id : "null");
    writer.write("]");
    writer.write("[");
    writer.write(message.getSummary() == null ? "null" : message.getSummary());
    writer.write("/");
    writer.write(message.getDetail() == null ? "null" : message.getDetail());
    writer.write("]");
    writer.endElement("div");
    writer.startElement("br", null);
    writer.endElement("br");
  }

  private String generateDoctype(UIPage page) {
    String doctype = (String) page.getAttributes().get(ATTR_DOCTYPE);
    String type = null;
    if ("loose".equals(doctype)) {
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


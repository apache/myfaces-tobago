/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.component.UILayout;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.PageRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.taglib.component.PageTag;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.util.collections.ListOrderedSet;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.ArrayList;

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

    RenderUtil.prepareRender(facesContext, page);

    ResponseWriter writer = facesContext.getResponseWriter();

    // replace responseWriter and render page content
    StringWriter content = new StringWriter();
    ResponseWriter contentWriter = new TobagoResponseWriter(
        content, writer.getContentType(), writer.getCharacterEncoding());
    facesContext.setResponseWriter(contentWriter);
        
    UIComponent menubar = page.getFacet(FACET_MENUBAR);
    if (menubar != null) {
      menubar.getAttributes().put(ATTR_PAGE_MENU, Boolean.TRUE);
      page.getOnloadScripts().add("setDivWidth('"
          + menubar.getClientId(facesContext) + "', getBrowserInnerWidth())");
      RenderUtil.encodeHtml(facesContext, menubar);
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

    String title = (String) page.getAttributes().get(ATTR_TITLE);

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
    Set styleFiles = page.getStyleFiles();
    for (Iterator i = styleFiles.iterator(); i.hasNext();) {
      String styleFile = (String) i.next();
      List styles = ResourceManagerUtil.getStyles(facesContext, styleFile);
      for (Iterator j = styles.iterator(); j.hasNext();) {
        String styleString = (String) j.next();
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
    Set styleBlocks = page.getStyleBlocks();
    if (styleBlocks.size() > 0) {
      writer.startElement("style", null);
      for (Iterator i = styleBlocks.iterator(); i.hasNext();) {
        String cssBlock = (String) i.next();
        writer.write(cssBlock);
      }
      writer.endElement("style");
    }

    // script files
    ListOrderedSet scriptFiles = page.getScriptFiles();
    scriptFiles.add(0, "script/tobago.js");
    for (Iterator i = scriptFiles.iterator(); i.hasNext();) {
      String script = (String) i.next();
      addScripts(writer, facesContext, script);
    }

    // focus id
    String focusId = page.getFocusId();
    if (focusId != null) {
      writer.startElement("script", null);
      writer.writeAttribute("type", "text/javascript", null);
      writer.write("focusId = '");
      writer.write(focusId);
      writer.write("';");
      writer.endElement("script");
    }

    // onload script
    writer.startElement("script", null);
    writer.writeAttribute("type", "text/javascript", null);
    writer.write("function onloadScript() {\n");
    writer.write("onloadScriptDefault();\n");

    for (Iterator i = page.getOnloadScripts().iterator(); i.hasNext();) {
      String onload = (String) i.next();
      writer.write(onload);
      writer.write('\n');
    }
    writer.write("}\n");

    int debugCounter = 0;
    for (Iterator i = page.getScriptBlocks().iterator(); i.hasNext();) {
      String script = (String) i.next();

      if (LOG.isDebugEnabled()) {
        LOG.debug("write scriptblock " + ++debugCounter + " :\n" + script);
      }
      writer.write(script);
      writer.write('\n');
    }

    String clientId = page.getClientId(facesContext);

    writer.endElement("script");

    writer.endElement("head");
    writer.startElement("body", page);
    writer.writeAttribute("onload", "onloadScript()", null);
    //this ist for ie to prevent scrollbars where none are needed
    writer.writeAttribute("scroll", "auto", null);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("id", clientId, null);



    writer.startElement("form", page);
    writer.writeAttribute("name",
        clientId + TobagoConstants.SUBCOMPONENT_SEP + "form", null);
    writer.writeAttribute("action", formAction, null);
    writer.writeAttribute("id", page.getFormId(facesContext), null);
    writer.writeAttribute("method", null, TobagoConstants.ATTR_METHOD);
    writer.writeAttribute("enctype", null, TobagoConstants.ATTR_ENCTYPE);

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name",
        clientId + TobagoConstants.SUBCOMPONENT_SEP + "form-action", null);
    writer.writeAttribute("id",
        clientId + TobagoConstants.SUBCOMPONENT_SEP + "form-action", null);
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

// todo: this is needed for the "BACK-BUTTON-PROBLEM"
// but may no longer needed
/*
    if (ViewHandlerImpl.USE_VIEW_MAP) {
      writer.startElement("input", null);
      writer.writeAttribute("type", "hidden", null);
      writer.writeAttribute("name", ViewHandlerImpl.PAGE_ID, null);
      writer.writeAttribute("id", ViewHandlerImpl.PAGE_ID, null);
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
        RenderUtil.encodeHtml(facesContext, (UIComponent) iter.next());
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
//      writer.write("<div onmousedown=\"tobagoJsLogMouseDown(event)\" onmousemove=\"tobagoJsLogMouseMove(event)\" onmouseup=\"tobagoJsLogMouseUp()\" id=\"LogDiv\" style=\"position:  absolute; left: 760px; top: 300px; height: 370px;width: 400px; overflow: auto;border:1px solid red; background: #ffffff;\"><ol id=\"Log\" style=\"font-family:Arial,sans-serif; font-size:10pt\"><li>Ereignisliste</li></ol> </div>");
    }

    writer.endElement("body");
    writer.endElement("html");
  }

// ----------------------------------------------------------- business methods

  private void addScripts(ResponseWriter writer, FacesContext facesContext,
      String script) throws IOException {
    List scripts;
    final String ucScript = script.toUpperCase();
    if (ucScript.startsWith("HTTP:") || ucScript.startsWith("FTP:")
        || ucScript.startsWith("/")) {
      scripts = new ArrayList();
      scripts.add(script);
    } else {
      scripts = ResourceManagerUtil.getScripts(facesContext, script);
    }
    for (Iterator j = scripts.iterator(); j.hasNext();) {
      String scriptString = (String) j.next();
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


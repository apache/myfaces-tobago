/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 08.04.2003 10:45:30.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.context.ResourceManager;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import java.io.OutputStream;
import java.io.Writer;

public class TobagoRenderKit extends RenderKit {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TobagoRenderKit.class);

  public static final String PACKAGE_PREFIX
      = TobagoRenderKit.class.getName().substring(
          0,
          TobagoRenderKit.class.getName().lastIndexOf('.'));

  public static final int PACKAGE_PREFIX_LENGTH = PACKAGE_PREFIX.length();

// ///////////////////////////////////////////// attribute

  private String renderKitId;

// ///////////////////////////////////////////// constructor

  public TobagoRenderKit(String renderKitId) {
    this.renderKitId = renderKitId;
  }

// ///////////////////////////////////////////// code



  // fixme: use family
  public Renderer getRenderer(String family, String rendererType) {
    String type = rendererType + "Renderer";
    if (type.startsWith("javax.faces.")) { // fixme: this is a hotfix from jsf1.0beta to jsf1.0fr
      type = type.substring("javax.faces.".length());
    }
    ResourceManager resources = ResourceManager.getInstance();
//    Log.info("- - snip - - - - - - - - - - - - - - - - - - - - - - - - - - " + type);
    Renderer renderer = resources.getRenderer(renderKitId, type);
    if (renderer == null) {
      LOG.error(
          "The class witch was found by the ResourceManager can't be " +
          "found, or instanciated: classname='" + type + "'");
    }
    return renderer;
  }

  public ResponseWriter createResponseWriter(
      Writer writer, String contentTypeList, String characterEncoding) {
    // fixme: use contentTypeList AND characterEncoding
    if (contentTypeList == null || contentTypeList.indexOf("text/html") < 0) {
      LOG.warn("Content-Type '" + contentTypeList + "' not supported, forcing text/html");
    }
    String contentType = "text/html";
    if (characterEncoding == null) {
      LOG.warn("No Character-Encoding, forcing UTF-8");
      characterEncoding = "UTF-8";
    }

    return new TobagoResponseWriter(writer, contentType, characterEncoding);
  }


// ///////////////////////////////////////////// todo

  public void addRenderer(String family, String rendererType, Renderer renderer) {
    LOG.debug("addRenderer family='" + family
        + "' rendererType='" + rendererType + "'");
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme jsf1.0
    LOG.error(new Exception().getStackTrace()[0].getMethodName());
  }

  public ResponseStateManager getResponseStateManager() {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme jsfbeta
    return null;
  }

  public ResponseStream createResponseStream(OutputStream outputstream) {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme jsfbeta
    return null;
  }
// ///////////////////////////////////////////// bean getter + setter

}

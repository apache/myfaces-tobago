package org.apache.myfaces.tobago.example.demo;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.InputStream;

public abstract class SourceFileReader {

  private static final Logger LOG = LoggerFactory.getLogger(SourceFileReader.class);

  public String getSource(String filename) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();
    final String viewId = facesContext.getViewRoot().getViewId();
    final String file = viewId.substring(0, viewId.lastIndexOf("/")) + "/" + filename;
    InputStream resourceAsStream = null;
    try {
      resourceAsStream = externalContext.getResourceAsStream(file);
      return IOUtils.toString(resourceAsStream, "UTF-8");
    } catch (final IOException e) {
      LOG.error("", e);
      return "error";
    } finally {
      IOUtils.closeQuietly(resourceAsStream);
    }
  }
}

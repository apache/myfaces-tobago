package com.atanion.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import java.util.ResourceBundle;
import java.util.Enumeration;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 14, 2005
 * Time: 2:24:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TobagoResourceBundle extends ResourceBundle {

  private static final Log LOG = LogFactory.getLog(TobagoResourceBundle.class);

  protected Object handleGetObject(String key) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("search for \"" + key + "\"");
    }
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ResourceManager resourceManager
        = ResourceManagerUtil.getResourceManager(facesContext);
    return resourceManager.getProperty(facesContext.getViewRoot(), "tobago", key);
  }

  public Enumeration<String> getKeys() {
    return Collections.enumeration(Collections.EMPTY_LIST);
  }
}

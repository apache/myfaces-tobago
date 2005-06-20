package com.atanion.tobago.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.context.ResourceManagerUtil;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ResourceBundle;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 14, 2005
 * Time: 5:40:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageFactory {


  private static final Log LOG = LogFactory.getLog(MessageFactory.class);

  private static Map<Locale,ResourceBundle> facesMessagesMap
      = new HashMap<Locale, ResourceBundle>();

  public static FacesMessage createFacesMessage(FacesContext facesContext,
      String key, FacesMessage.Severity severity) {
    return createFacesMessage(facesContext, "tobago", key, severity);
  }
  public static FacesMessage createFacesMessage(FacesContext facesContext,
      String bundle, String key, FacesMessage.Severity severity) {
    String summary = getMessageText(facesContext, bundle, key);
    String detail = getMessageText(facesContext, bundle, key + "_detail");
    return new FacesMessage(severity, summary != null ? summary : key, detail);

  }

  private static String getMessageText(
      FacesContext facesContext, String bundle, String key) {
    String message
        = ResourceManagerUtil.getProperty(facesContext, bundle, key, false);
    if (message == null || message.length() < 1) {
      try {
        Locale locale = facesContext.getViewRoot().getLocale();
        message = getFacesMessages(locale).getString(key);
      } catch (Exception e) {
        // ignore at this point
      }
    }
    return message;
  }

  private static ResourceBundle getFacesMessages(Locale locale) {
    ResourceBundle facesMessages = facesMessagesMap.get(locale);
    if (facesMessages == null) {
      facesMessages
          = ResourceBundle.getBundle(FacesMessage.FACES_MESSAGES, locale);
      facesMessagesMap.put(locale, facesMessages);
    }
    return facesMessages;
  }
}

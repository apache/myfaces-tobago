package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.apache.myfaces.tobago.util.ResourceUtils.TOBAGO_RESOURCE_BUNDLE;

@Named
@SessionScoped
public class BundleController implements Serializable {

  private List<BundleEntry> resources = new ArrayList<>();
  private List<BundleEntry> messages = new ArrayList<>();

  public void clear(@Observes LocaleChanged event) {
    resources.clear();
    messages.clear();
  }

  public List<BundleEntry> getResources() {
    if (resources.size() == 0) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final ResourceBundle bundle =
          facesContext.getApplication().getResourceBundle(facesContext, TOBAGO_RESOURCE_BUNDLE);
      final Enumeration<String> keys = bundle.getKeys();
      while (keys.hasMoreElements()) {
        final String key = keys.nextElement();
        resources.add(new BundleEntry(key, bundle.getString(key)));
      }
    }
    return resources;
  }

  public List<BundleEntry> getMessages() {
    if (messages.size() == 0) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final Application application = facesContext.getApplication();
      final String bundleName = application.getMessageBundle();
      final Locale locale = facesContext.getViewRoot() != null
          ? facesContext.getViewRoot().getLocale() : application.getDefaultLocale();
      ResourceBundle bundle = ResourceBundle.getBundle(bundleName, locale);
      final Enumeration<String> keys = bundle.getKeys();
      while (keys.hasMoreElements()) {
        final String key = keys.nextElement();
        messages.add(new BundleEntry(key, bundle.getString(key)));
      }
    }
    return messages;
  }

}

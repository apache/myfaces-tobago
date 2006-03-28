package org.apache.myfaces.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

/**
 * User: lofwyr
 * Date: 25.03.2006 08:21:06
 */
class ThemeBuilder {

  private static final Log LOG = LogFactory.getLog(ThemeBuilder.class);

  private List<ThemeImpl> availableThemes = new ArrayList<ThemeImpl>();

  public Map<String, Theme> resolveThemes() {
    Map<String, ThemeImpl> map = new HashMap<String, ThemeImpl>();
    for (ThemeImpl theme : availableThemes) {
      LOG.debug("theme from tobago-theme.xml files:" + theme.getName());
      map.put(theme.getName(), theme);
    }
    for (ThemeImpl theme : availableThemes) {
      String fallbackName = theme.getFallbackName();
      ThemeImpl fallback = map.get(fallbackName);
      theme.setFallback(fallback);
    }
    for (ThemeImpl theme : availableThemes) {
      theme.resolveFallbacks();
    }
    Map<String, Theme> result = new HashMap<String, Theme>();
    for (ThemeImpl theme : availableThemes) {
      result.put(theme.getName(), theme);
    }
    for (ThemeImpl theme : availableThemes) {
      if (theme.getDeprecatedName() != null) {
        result.put(theme.getDeprecatedName(), theme);
      }
    }

    return Collections.unmodifiableMap(result);
  }

  public void addTheme(ThemeImpl theme) {
    availableThemes.add(theme);
  }
}

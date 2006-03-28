package org.apache.myfaces.tobago.context;

import java.util.List;

/**
 * User: lofwyr
 * Date: 25.03.2006 10:42:24
 */
public interface Theme {
  String getName();

  List<Theme> getFallbackList();

  String getDisplayName();
}

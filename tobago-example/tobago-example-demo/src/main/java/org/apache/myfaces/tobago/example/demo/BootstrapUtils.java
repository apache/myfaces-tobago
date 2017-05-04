package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;

public class BootstrapUtils {

  /**
   * This method ensures, the used Bootstrap class is really existing.
   */
  public static String valueOf(String name) {
    return BootstrapClass.valueOf(name).getName();
  }
}

/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 31.08.2005 13:21:20.
 * $Id: $
 */
package org.apache.myfaces.tobago.context;

import javax.faces.component.UIViewRoot;
import javax.faces.render.Renderer;
import java.util.Locale;

public interface ResourceManager {

  String getJsp(UIViewRoot viewRoot, String name);

  String getProperty(UIViewRoot viewRoot, String bundle, String propertyKey);

  String getProperty(
      ClientProperties clientProperties, Locale locale,
      String bundle, String propertyKey);

  Renderer getRenderer(UIViewRoot viewRoot, String name);

  String[] getScripts(UIViewRoot viewRoot, String name);

  String[] getStyles(UIViewRoot viewRoot, String name);

  String getThemeProperty(UIViewRoot viewRoot,
      String bundle, String propertyKey);

  String getImage(UIViewRoot viewRoot, String name);

  String getImage(UIViewRoot viewRoot, String name, boolean ignoreMissing);
}

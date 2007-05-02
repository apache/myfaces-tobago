package org.apache.myfaces.tobago.renderkit.html;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.collections.set.ListOrderedSet;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

/*
 * Date: 2007-05-01
 */

public class StyleClasses implements Serializable {

  public static final String PREFIX = "tobago-";
  public static final String SUFFIX_DEFAULT = "-default";
  public static final String SUFFIX_DISABLED = "-disabled";
  public static final String SUFFIX_READONLY = "-readonly";
  public static final String SUFFIX_INLINE = "-inline";
  public static final String SUFFIX_ERROR = "-error";
  public static final String SUFFIX_REQUIRED = "-required";

  private ListOrderedSet classes;

  public StyleClasses() {
    classes = new ListOrderedSet();
  }

  private StyleClasses(StyleClasses base) {
    this();
    classes.addAll(base.classes);
  }

  public static StyleClasses ensureStyleClasses(UIComponent component) {
    Map attributes = component.getAttributes();
    StyleClasses classes = (StyleClasses) attributes.get(ATTR_STYLE_CLASS);
    if (classes == null) {
      classes = new StyleClasses();
      attributes.put(ATTR_STYLE_CLASS, classes);
    }
    return classes;
  }

  public static StyleClasses ensureStyleClassesCopy(UIComponent component) {
    return new StyleClasses(ensureStyleClasses(component));
  }

  public void addClass(String clazz) {
    classes.add(clazz);
  }

  public void addClasses(StyleClasses styleClasses) {
    for (String clazz : (Iterable<String>) styleClasses.classes) {
      classes.add(clazz);
    }
  }

  public void removeClass(String clazz) {
    classes.remove(clazz);
  }

  public void removeTobagoClasses(String rendererName) {
    for (Iterator i = classes.iterator(); i.hasNext();) {
      String clazz = (String) i.next();
      if (clazz.startsWith(PREFIX + rendererName)) {
        i.remove();
      }
    }
  }

  public void updateClassAttribute(String rendererName, UIComponent component) {
    // first remove old tobago-<rendererName>-<type> classes from class-attribute
    removeTobagoClasses(rendererName);

    StringBuilder prefix = new StringBuilder(PREFIX).append(rendererName);
    addClass(prefix + SUFFIX_DEFAULT);
    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      addClass(prefix + SUFFIX_DISABLED);
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_READONLY)) {
      addClass(prefix + SUFFIX_READONLY);
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
      addClass(prefix + SUFFIX_INLINE);
    }
    if (component instanceof UIInput) {
      UIInput input = (UIInput) component;
      if (ComponentUtil.isError(input)) {
        addClass(prefix + SUFFIX_ERROR);
      }
      if (input.isRequired()) {
        addClass(prefix + SUFFIX_REQUIRED);
      }
    }

    HtmlRendererUtil.addMarkupClass(component, rendererName, this);
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for (Iterator i = classes.iterator(); i.hasNext();) {
      String clazz = (String) i.next();
      buffer.append(clazz);
      if (i.hasNext()) {
        buffer.append(' ');
      }
    }
    return buffer.toString();
  }
}

/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 07.09.2004 14:11:53.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;

public final class LabelWithAccessKey {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(LabelWithAccessKey.class);

// ----------------------------------------------------------------- attributes

  public String text;
  public Character accessKey;
  public int pos = -1;
  public static final char INDICATOR = '_';

// --------------------------------------------------------------- constructors

  public LabelWithAccessKey(UIComponent component) {

    if (TobagoConstants.RENDERER_TYPE_LABEL.equals(component.getRendererType())) {
      text = (String) component.getAttributes().get(
          TobagoConstants.ATTR_VALUE);
    } else {
      text = (String) component.getAttributes().get(
              TobagoConstants.ATTR_LABEL);
    }

    accessKey = ComponentUtil.getCharakterAttribute(
        component, TobagoConstants.ATTR_ACCESS_KEY);
    String labelWithAccessKey = (String) component.getAttributes().get(
        TobagoConstants.ATTR_LABEL_WITH_ACCESS_KEY);

    setup(labelWithAccessKey);

  }

  public void setup(String labelWithAccessKey) {
    if (labelWithAccessKey != null && (accessKey != null || text != null)) {
      LOG.warn("labelWithAccessKey overwrites accessKey or label: "
         + "labelWithAccessKey='" + labelWithAccessKey + "', "
         + "accessKey ='" + accessKey + "', "
         + "label='" + text + "'.");
    }

    if (labelWithAccessKey != null) {
      pos = labelWithAccessKey.indexOf(INDICATOR);
      if (pos == -1) {
        LOG.warn("no "+INDICATOR +" in labelWithAccessKey"
            + "labelWithAccessKey='" + labelWithAccessKey + "'.");
        text = labelWithAccessKey;
      } else if (pos == labelWithAccessKey.length() - 1) {
        LOG.warn(INDICATOR + " in labelWithAccessKey is last char, this is not allowed"
          + "labelWithAccessKey='" + labelWithAccessKey + "'.");
        text = labelWithAccessKey.substring(0, labelWithAccessKey.length() - 1);
        pos = -1;
      } else {
        text = labelWithAccessKey.substring(0, pos)
            + labelWithAccessKey.substring(pos + 1);
        accessKey = new Character(text.charAt(pos));
      }
    } else {
      if (accessKey != null && text != null) {
        pos = text.toLowerCase().indexOf(
            Character.toLowerCase(accessKey.charValue()));
      }
    }
  }

// ------------------------------------------------------------ getter + setter

  public String getText() {
    return text;
  }

  public Character getAccessKey() {
    return accessKey;
  }

  public int getPos() {
    return pos;
  }
}


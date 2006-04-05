package org.apache.myfaces.tobago.renderkit;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_LABEL;

import javax.faces.component.UIComponent;

public final class LabelWithAccessKey {

  private static final Log LOG = LogFactory.getLog(LabelWithAccessKey.class);

  private String text;
  private Character accessKey;
  private int pos = -1;
  public static final char INDICATOR = '_';

  public LabelWithAccessKey(UIComponent component) {

    if (RENDERER_TYPE_LABEL.equals(component.getRendererType())) {
      text = (String) component.getAttributes().get(ATTR_VALUE);
    } else {
      text = (String) component.getAttributes().get(ATTR_LABEL);
    }

    setup(text);

  }

  public void setup(String label) {

    if (label != null) {
      pos = label.indexOf(INDICATOR);
      if (pos == -1) {
        text = label;
      } else if (pos == label.length() - 1) {
        LOG.warn(INDICATOR + " in label is last char, this is not allowed"
          + "label='" + label + "'.");
        text = label.substring(0, label.length() - 1);
        pos = -1;
      } else {
        text = label.substring(0, pos)
            + label.substring(pos + 1);
        accessKey = new Character(text.charAt(pos));
      }
    } else {
      if (accessKey != null && text != null) {
        pos = text.toLowerCase().indexOf(
            Character.toLowerCase(accessKey.charValue()));
      }
    }
  }

  public String getText() {
    return text;
  }

  public Character getAccessKey() {
    return accessKey;
  }

  public int getPos() {
    return pos;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setAccessKey(Character accessKey) {
    this.accessKey = accessKey;
  }
}


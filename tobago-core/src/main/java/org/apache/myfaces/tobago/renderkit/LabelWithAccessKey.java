/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.SupportsAccessKey;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.util.Locale;

public final class LabelWithAccessKey {

  private static final Logger LOG = LoggerFactory.getLogger(LabelWithAccessKey.class);

  public static final char INDICATOR = '_';
  private static final String ESCAPED_INDICATOR = "__";

  private final String label;
  private final Character accessKey;
  private final int pos;

  /**
   * @deprecated since 2.0.0. This is a workaround.
   */
  @Deprecated
  public LabelWithAccessKey(final String label) {
    this(new SupportsAccessKey() {
      @Override
      public Character getAccessKey() {
        return null;
      }

      @Override
      public String getLabel() {
        return label;
      }
    });
  }

  public LabelWithAccessKey(final SupportsAccessKey component) {

    String label0;
    Character accessKey0;
    int pos0 = -1;

    label0 = component.getLabel();

    // compatibility since TOBAGO-1093
    if (component instanceof UISelectBooleanCheckbox) {
      final String itemLabel = ((UISelectBooleanCheckbox) component).getItemLabel();
      if (itemLabel != null) {
        label0 = itemLabel;
      }
    }

    accessKey0 = component.getAccessKey();
    if (accessKey0 != null) {
      accessKey0 = Character.toLowerCase(accessKey0);
      if (!isPermitted(accessKey0)) {
        LOG.warn("Ignoring illegal access key: " + accessKey0);
        accessKey0 = null;
      }
    }

    boolean auto = TobagoConfig.getInstance(FacesContext.getCurrentInstance()).isAutoAccessKeyFromLabel();
    // try to find the accessKey from the label if
    // a) it's configured to do that AND
    // b) there wasn't an accessKey defined in the component AND
    // c) there is a label
    if (auto && component.getAccessKey() == null && label0 != null) {

      final int first = label0.indexOf(INDICATOR);
      if (first > -1) {
        final char[] chars = label0.toCharArray();
        int j = first;
        for (int i = first; i < chars.length; i++) {
          if (chars[i] == INDICATOR) {
            if (i + 1 < chars.length) {
              i++; // ignore the first one
              chars[j] = chars[i];
              if (chars[i] != INDICATOR) {
                if (accessKey0 == null) {
                  pos0 = j;
                  accessKey0 = Character.toLowerCase(chars[i]);
                  if (!isPermitted(accessKey0)) {
                    LOG.warn("Ignoring illegal access key: " + accessKey0);
                    accessKey0 = null;
                    pos0 = -1;
                  }
                }
              }
              j++;
            } else {
              LOG.warn("'" + INDICATOR + "' in label is last char, this is not allowed label='" + label0 + "'.");
            }
          } else {
            chars[j] = chars[i];
            j++;
          }
        }
        label0 = new String(chars, 0, j);
      }

    } else {
      if (accessKey0 != null && label0 != null) {
        pos0 = label0.toLowerCase(Locale.ENGLISH).indexOf(accessKey0);
      }
    }

    label = label0;
    accessKey = accessKey0;
    pos = pos0;
  }

/*
  private void findIndicator(final String label, int index, int escapedIndicatorCount) {

    index = label.indexOf(INDICATOR, index);
    if (index == -1) {
      text = label;
    } else {
      if (index == label.length() - 1) {
        LOG.warn(INDICATOR + " in label is last char, this is not allowed"
            + "label='" + label + "'.");
        text = label.substring(0, label.length() - 1);
        pos = -1;
      } else if (label.charAt(index + 1) == INDICATOR) {
        escapedIndicatorCount++;
        findIndicator(label, index + 2, escapedIndicatorCount);
      } else {
        text = label.substring(0, index)
            + label.substring(index + 1);
        setAccessKey(text.charAt(index));
        pos = index - escapedIndicatorCount;
      }
    }
  }
*/

  /**
   * @deprecated since 2.0.0. Attributes are final now, use a new instance.
   */
  @Deprecated
  public void setup(final String label) {
    Deprecation.LOG.error("Ignoring label: " + label);
  }

  /**
   * @deprecated since 2.0.0. Attributes are final now, use a new instance.
   */
  @Deprecated
  public void reset() {
    Deprecation.LOG.error("Ignoring reset.");
  }

  /**
   * @deprecated since 2.0.0. Please use {@link #getLabel()}.
   */
  @Deprecated
  public String getText() {
    return label;
  }

  public String getLabel() {
    return label;
  }

  public Character getAccessKey() {
    return accessKey;
  }

  public int getPos() {
    return pos;
  }

  /**
   * @deprecated since 2.0.0. Attributes are final now, use a new instance.
   */
  @Deprecated
  public void setText(final String text) {
    Deprecation.LOG.error("Ignoring label: " + text);
  }

  /**
   * @deprecated since 2.0.0. Attributes are final now, use a new instance.
   */
  @Deprecated
  public void setAccessKey(Character accessKey) {
    Deprecation.LOG.error("Ignoring accessKey: " + accessKey);
  }

  /**
   * Ensures, that no illegal character will be write out.
   * (If this is changed from only allowing normal letters and numbers, the renderers may change the escaping)
   */
  private boolean isPermitted(final Character accessKey) {
    return accessKey == null || accessKey >= 'a' && accessKey <= 'z' || accessKey >= '0' && accessKey <= '9';
  }
}

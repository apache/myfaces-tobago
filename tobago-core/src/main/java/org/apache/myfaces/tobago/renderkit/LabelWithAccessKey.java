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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class LabelWithAccessKey {

  private static final Logger LOG = LoggerFactory.getLogger(LabelWithAccessKey.class);

  private final String label;
  private final Character accessKey;
  private final int pos;

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

    if (accessKey0 != null && label0 != null) {
      pos0 = label0.toLowerCase(Locale.ENGLISH).indexOf(accessKey0);
    }

    label = label0;
    accessKey = accessKey0;
    pos = pos0;
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
   * Ensures, that no illegal character will be write out.
   * (If this is changed from only allowing normal letters and numbers, the renderers may change the escaping)
   */
  private boolean isPermitted(final Character accessKey) {
    return accessKey == null || accessKey >= 'a' && accessKey <= 'z' || accessKey >= '0' && accessKey <= '9';
  }
}

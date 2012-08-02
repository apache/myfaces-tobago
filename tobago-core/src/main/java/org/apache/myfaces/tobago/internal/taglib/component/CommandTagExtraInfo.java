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

package org.apache.myfaces.tobago.internal.taglib.component;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.ValidationMessage;

/*
 * Date: 19.04.2006
 * Time: 10:56:39
 */
public class CommandTagExtraInfo extends TagExtraInfo {
  private static final ValidationMessage[] EMPTY_MESSAGE = new ValidationMessage[0];

  public ValidationMessage[] validate(TagData data) {
    Object action = data.getAttribute("action");
    Object onclick = data.getAttribute("onclick");
    Object link = data.getAttribute("link");

    if (link != null && !(action == null && onclick == null)) {
      return generateValidationMessages(data);
    }
    return EMPTY_MESSAGE;
  }

  private ValidationMessage[] generateValidationMessages(TagData data) {
    ValidationMessage[] messages = new ValidationMessage[1];
    messages[0] = new ValidationMessage(data.getId(),
        "Only one Attribute of action, onclick and link is allowed");
    return messages;
  }
}

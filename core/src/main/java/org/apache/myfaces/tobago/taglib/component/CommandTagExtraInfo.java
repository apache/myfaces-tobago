package org.apache.myfaces.tobago.taglib.component;

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

import javax.servlet.jsp.tagext.ValidationMessage;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagData;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 19.04.2006
 * Time: 10:56:39
 * To change this template use File | Settings | File Templates.
 */
public class CommandTagExtraInfo extends TagExtraInfo {
  public ValidationMessage[] validate(TagData data) {
    Object action = data.getAttribute("action");
    Object actionScript = data.getAttribute("script");
    Object actionNavigate = data.getAttribute("navigate");
    if (action != null) {
      if (actionScript != null||actionNavigate != null) {
        return generateValidationMessages(data);
      }
    } else if (actionScript != null&&actionNavigate != null) {
      return generateValidationMessages(data);
    }
    return null;
  }

  private ValidationMessage[] generateValidationMessages(TagData data) {
    ValidationMessage messages[]  = new ValidationMessage[1];
        messages [0] = new ValidationMessage(data.getId(),
            "Only one Attribute of action, script and navigate is allowed");
    return messages;
  }
}

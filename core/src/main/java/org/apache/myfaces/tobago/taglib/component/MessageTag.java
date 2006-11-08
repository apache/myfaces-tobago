package org.apache.myfaces.tobago.taglib.component;

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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;


public class MessageTag extends TobagoTag
    implements MessageTagDeclaration {

  private String forComponent;

  public String getComponentType() {
    return UIMessage.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, forComponent);
  }

  public void release() {
    super.release();
    forComponent = null;
  }

  public String getFor() {
    return forComponent;
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }
}

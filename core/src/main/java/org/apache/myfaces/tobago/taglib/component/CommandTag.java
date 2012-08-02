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

package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;

import javax.faces.component.UIComponent;

/*
 * Date: 05.08.2006
 * Time: 11:59:21
 */
public class CommandTag extends AbstractCommandTag implements CommandTagDeclaration {
  private String renderedPartially;

  public void release() {
    super.release();
    renderedPartially = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    if (component instanceof UICommand) {
      ComponentUtil.setRenderedPartially((UICommand) component, renderedPartially);
    }
  }

  public String getRenderedPartially() {
    return renderedPartially;
  }

  public void setRenderedPartially(String renderedPartially) {
    this.renderedPartially = renderedPartially;
  }
}

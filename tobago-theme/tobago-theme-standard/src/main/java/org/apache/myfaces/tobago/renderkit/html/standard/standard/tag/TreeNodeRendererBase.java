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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.component.AbstractUITree;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TreeNodeRendererBase extends RendererBase {

  @Override
  public void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    final AbstractUITreeNode node = (AbstractUITreeNode) component;
    final AbstractUIData data = ComponentUtils.findAncestor(node, AbstractUIData.class);
    if (data instanceof AbstractUITree && data.getSelectedState().isSelected(node.getPath())) {
      ComponentUtils.addCurrentMarkup(node, Markup.SELECTED);
    }
    if (node.isFolder()) {
      ComponentUtils.addCurrentMarkup(node, Markup.FOLDER);
      if (data.getExpandedState().isExpanded(node.getPath())) {
        ComponentUtils.addCurrentMarkup(node, Markup.EXPANDED);
      }
    }
  }

}

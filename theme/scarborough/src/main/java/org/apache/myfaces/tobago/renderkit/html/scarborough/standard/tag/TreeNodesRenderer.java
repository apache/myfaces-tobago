package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.component.UITreeNodes;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Enumeration;

public class TreeNodesRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(TreeNodesRenderer.class);

  @Override
  public void decode(FacesContext facesContext, UIComponent component) {
  }

  @Override
  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    UITreeNodes nodes = (UITreeNodes) component;
    String var = nodes.getVar();

    DefaultMutableTreeNode tree = (DefaultMutableTreeNode) nodes.getValue();

    for (Enumeration e = tree.depthFirstEnumeration(); e.hasMoreElements();) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();

      if (node == null) { // XXX hotfix
        LOG.warn("node is null");
        continue;
      }
      // todo put into var (request context)
      facesContext.getExternalContext().getRequestMap().put(var, node);
      UITreeNode template = nodes.getTemplateComponent();
      template.encodeBegin(facesContext);
      template.encodeChildren(facesContext);
      template.encodeEnd(facesContext);
      facesContext.getExternalContext().getRequestMap().remove(var);
    }
  }
}

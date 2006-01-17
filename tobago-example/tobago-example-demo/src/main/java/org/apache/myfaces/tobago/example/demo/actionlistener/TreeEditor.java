/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * Created 27.05.2003 13:37:22.
 * $Id: TreeEditor.java 1270 2005-08-08 20:21:38 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.example.demo.actionlistener;

import org.apache.myfaces.tobago.example.demo.TobagoDemoController;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.event.DefaultTreeActionListener;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeEditor extends DefaultTreeActionListener {

  protected String nextId() {
    return "m" + System.currentTimeMillis();
  }

  protected DefaultMutableTreeNode create(FacesContext facesContext) {
    String label = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago", "treeNodeNew");
    return new DefaultMutableTreeNode(
        TobagoDemoController.createNode(label, nextId()));
  }

  protected DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
    TobagoDemoController.Node userObject
        = (TobagoDemoController.Node) node.getUserObject();
    userObject.setId(nextId());
    return new DefaultMutableTreeNode(userObject);
  }

  public void processAction(ActionEvent event) throws AbortProcessingException {
    super.processAction(event);
    FacesContext facesContext = FacesContext.getCurrentInstance();
    facesContext.renderResponse();
  }

}

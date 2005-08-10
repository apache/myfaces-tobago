/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 27.05.2003 13:37:22.
 * $Id: TreeEditor.java 1270 2005-08-08 20:21:38 +0200 (Mo, 08 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.demo.actionlistener;

import org.apache.myfaces.tobago.demo.TobagoDemoController;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.event.DefaultTreeActionListener;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeEditor extends DefaultTreeActionListener {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  protected String nextId() {
    return "m" + System.currentTimeMillis();
  }

  protected DefaultMutableTreeNode create(FacesContext facesContext) {
    String label = ResourceManagerUtil.getProperty(facesContext, "tobago", "treeNodeNew");
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

// ///////////////////////////////////////////// bean getter + setter

}

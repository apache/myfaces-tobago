/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 14.02.2003 13:40:19.
 * $Id$
 */
package com.atanion.tobago.event;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UITree;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.util.TreeState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class DefaultTreeActionListener implements ActionListener {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(DefaultTreeActionListener.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  protected MutableTreeNode create() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    String label = TobagoResource.getProperty(facesContext, "tobago", "treeNodeNew");
    return new DefaultMutableTreeNode(label);
  }

  protected MutableTreeNode copy(MutableTreeNode node) {
    if (node instanceof DefaultMutableTreeNode) {
      return new DefaultMutableTreeNode(((DefaultMutableTreeNode) node).getUserObject());
    } else {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      String label = TobagoResource.getProperty(facesContext, "tobago", "treeNodeCopy");
      return new DefaultMutableTreeNode(label);
    }
  }

  public void processAction(ActionEvent actionEvent) throws AbortProcessingException {

    UIComponent component = actionEvent.getComponent().getParent();
    if (!(component instanceof UITree)) {
      LOG.error("No tree found!");
      return;
    }

    UITree tree = (UITree) component;
    TreeState treeState = (TreeState) tree.getValue();
    MutableTreeNode marker = (MutableTreeNode) treeState.getMarker();
    String command = (String) actionEvent.getComponent().getAttributes().get(
        TobagoConstants.ATTR_COMMAND_NAME);

    LOG.debug("marker      " + marker);
    LOG.debug("lastMarker  " + treeState.getLastMarker());
    LOG.debug("root        " + treeState.getRoot());
    LOG.debug("command     " + command);
    LOG.debug("lastCommand " + treeState.getLastCommand());

    if (marker != null) {
      boolean isRoot = treeState.getRoot().equals(marker);
      if (UITree.COMMAND_NEW.equals(command)) {
        marker.insert(create(), 0);
        treeState.setLastMarker(null);
        treeState.setLastCommand(null);
      } else if (UITree.COMMAND_DELETE.equals(command)) {
        if (!isRoot) {
          marker.removeFromParent();
        }
        treeState.setLastMarker(null);
        treeState.setLastCommand(null);
      } else if (UITree.COMMAND_CUT.equals(command)) {
        if (!isRoot) {
          treeState.setLastMarker(marker);
          treeState.setLastCommand(command);
        }
      } else if (UITree.COMMAND_COPY.equals(command)) {
        treeState.setLastMarker(marker);
        treeState.setLastCommand(command);
      } else if (UITree.COMMAND_PASTE.equals(command)) {
        if (treeState.getLastMarker() != null) {
          if (UITree.COMMAND_CUT.equals(treeState.getLastCommand())) {
            marker.insert((MutableTreeNode) treeState.getLastMarker(), 0);
          } else if (UITree.COMMAND_COPY.equals(treeState.getLastCommand())) {
            marker.insert(copy((MutableTreeNode) treeState.getLastMarker()), 0);
          }
          treeState.setLastMarker(null);
          treeState.setLastCommand(null);
        }
      } else if (UITree.COMMAND_MOVE_UP.equals(command)) {
        if (!isRoot) {
          MutableTreeNode node = marker;
          MutableTreeNode parent = (MutableTreeNode) node.getParent();
          int index = parent.getIndex(node);
          index = Math.max(index - 1, 0);
          parent.insert(node, index);
        }
        treeState.setLastMarker(null);
        treeState.setLastCommand(null);
      } else if (UITree.COMMAND_MOVE_DOWN.equals(command)) {
        if (!isRoot) {
          MutableTreeNode node = marker;
          MutableTreeNode parent = (MutableTreeNode) node.getParent();
          int index = parent.getIndex(node);
          index = Math.min(index + 1, parent.getChildCount() - 1);
          parent.insert(node, index);
        }
        treeState.setLastMarker(null);
        treeState.setLastCommand(null);
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}

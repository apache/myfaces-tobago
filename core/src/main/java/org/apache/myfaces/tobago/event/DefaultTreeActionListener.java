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

package org.apache.myfaces.tobago.event;

/*
 * Created 14.02.2003 13:40:19.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UITreeOld;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.model.TreeState;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class DefaultTreeActionListener implements ActionListener {

  private static final Log LOG = LogFactory.getLog(DefaultTreeActionListener.class);

  protected DefaultMutableTreeNode create(FacesContext facesContext) {
    String label = ResourceManagerUtil.getPropertyNotNull(facesContext, "tobago", "treeNodeNew");
    return new DefaultMutableTreeNode(label);
  }

  protected DefaultMutableTreeNode copy(DefaultMutableTreeNode node) {
    return new DefaultMutableTreeNode(node.getUserObject());
  }

  public void processAction(ActionEvent actionEvent) throws AbortProcessingException {

    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIComponent component = actionEvent.getComponent().getParent();
    if (component instanceof UIPanel) {
      // component is toolbar
      component = component.getParent();
    }
    if (!(component instanceof UITreeOld)) {
      LOG.error("No tree found!");
      return;
    }

    UITreeOld tree = (UITreeOld) component;
    TreeState treeState = tree.getState();
    DefaultMutableTreeNode marker = treeState.getMarker();
    String command = actionEvent.getComponent().getId();

    if (LOG.isDebugEnabled()) {
      LOG.debug("marker      " + marker);
      LOG.debug("lastMarker  " + treeState.getLastMarker());
      LOG.debug("root        " + tree.getValue());
      LOG.debug("command     " + command);
      LOG.debug("lastCommand " + treeState.getLastCommand());
    }
    if (marker != null) {
      boolean isRoot = tree.getValue().equals(marker);
      if (UITreeOld.COMMAND_NEW.equals(command)) {
        treeState.commandNew(create(facesContext));
      } else if (UITreeOld.COMMAND_DELETE.equals(command)) {
        if (!isRoot) {
          marker.removeFromParent();
        }
        treeState.setLastMarker(null);
        treeState.setLastCommand(null);
      } else if (UITreeOld.COMMAND_CUT.equals(command)) {
        if (!isRoot) {
          treeState.setLastMarker(marker);
          treeState.setLastCommand(command);
        }
      } else if (UITreeOld.COMMAND_COPY.equals(command)) {
        treeState.setLastMarker(marker);
        treeState.setLastCommand(command);
      } else if (UITreeOld.COMMAND_PASTE.equals(command)) {
        if (treeState.getLastMarker() != null) {
          if (UITreeOld.COMMAND_CUT.equals(treeState.getLastCommand())) {
            marker.insert(treeState.getLastMarker(), 0);
          } else if (UITreeOld.COMMAND_COPY.equals(treeState.getLastCommand())) {
            marker.insert(copy(treeState.getLastMarker()), 0);
          }
          treeState.setLastMarker(null);
          treeState.setLastCommand(null);
        }
      } else if (UITreeOld.COMMAND_MOVE_UP.equals(command)) {
        if (!isRoot) {
          MutableTreeNode node = marker;
          MutableTreeNode parent = (MutableTreeNode) node.getParent();
          int index = parent.getIndex(node);
          index = Math.max(index - 1, 0);
          parent.insert(node, index);
        }
        treeState.setLastMarker(null);
        treeState.setLastCommand(null);
      } else if (UITreeOld.COMMAND_MOVE_DOWN.equals(command)) {
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
}

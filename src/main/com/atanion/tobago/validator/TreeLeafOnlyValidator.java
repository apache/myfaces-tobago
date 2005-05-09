package com.atanion.tobago.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.component.UITree;
import com.atanion.tobago.component.UITreeNode;
import com.atanion.tobago.model.TreeState;
import com.atanion.tobago.context.ResourceManagerUtil;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Set;
import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: May 3, 2005
 * Time: 10:25:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class TreeLeafOnlyValidator implements Validator{

  private static final Log LOG = LogFactory.getLog(TreeLeafOnlyValidator.class);

  public static final String MESSAGE_NOT_LEAF
      = "tobago.TreeLeafOnlyValidator.MESSAGE_NOT_LEAF";

  public void validate(FacesContext facesContext, UIComponent component, Object value)
      throws ValidatorException {
    UITree tree = (UITree) component;

    TreeState treeState = tree.getState();
    Set<DefaultMutableTreeNode> selection = treeState.getSelection();

    for(DefaultMutableTreeNode node : selection) {
      if (!node.isLeaf()) {
        String message = ResourceManagerUtil.getProperty(
            facesContext, "tobago", MESSAGE_NOT_LEAF);
        throw new ValidatorException(new FacesMessage(message));
      }
    }
  }
}

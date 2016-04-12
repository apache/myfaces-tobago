package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.example.data.SolarObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;

@RequestScoped
@Named
public class SheetTreeController implements Serializable {

  private DefaultMutableTreeNode solarTree;

  public SheetTreeController() {
    solarTree = SolarObject.getTree();
  }

  public DefaultMutableTreeNode getSolarTree() {
    return solarTree;
  }
}

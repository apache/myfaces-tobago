package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponentBase;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Mar 21, 2005
 * Time: 10:38:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class UITreeListboxBox extends UIComponentBase {

  private static final Log LOG = LogFactory.getLog(UITreeListboxBox.class);

  public static final String COMPONENT_TYPE="org.apache.myfaces.tobago.TreeListboxBox";
  public static final String RENDERER_TYPE="TreeListboxBox";

  public static final String COMPONENT_FAMILY = "javax.faces.Output";


  private int level;

  private List<UITreeNode> nodes;


  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public List<UITreeNode> getNodes() {
    return nodes;
  }

  public void setNodes(List<UITreeNode> nodes) {
    this.nodes = nodes;
  }

}

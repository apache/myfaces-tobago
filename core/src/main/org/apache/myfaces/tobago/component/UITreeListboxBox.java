/*
 * Copyright 2002-2005 atanion GmbH.
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

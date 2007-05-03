package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.model.MixedTreeModel;

/**
 * User: lofwyr
 * Date: 23.04.2007 18:08:08
 */
public interface TreeModelBuilder {

  void buildBegin(MixedTreeModel model);

  void buildChildren(MixedTreeModel model);

  void buildEnd(MixedTreeModel model);

}

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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.SupportsStyle;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.model.TreeDataModel;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public abstract class AbstractUITreeNode
    extends AbstractUIColumn implements SupportsMarkup, Configurable, SupportsStyle {

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    model.setRowClientId(getClientId(facesContext));

    super.encodeBegin(facesContext);
  }

  /**
   * Returns the level of the tree node inside of the virtual tree. The root node has level 0.
   * The children of the root note have level 1, and so on.
   */
  public int getLevel() {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    return model.getLevel();
  }

  public List<Boolean> getJunctions() {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    return model.getJunctions();
  }

  public boolean isFolder() {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    return model.isFolder();
  }

  public TreePath getPath() {
    final TreeDataModel model = ComponentUtils.findAncestor(this, AbstractUIData.class).getTreeDataModel();
    return model.getPath();
  }

  public String nodeStateId(final FacesContext facesContext) {
    final String clientId = getClientId(facesContext);
    final UIData data = ComponentUtils.findAncestor(this, UIData.class);
    final String dataId = data.getClientId(facesContext);
    return clientId.substring(dataId.length() + 1);
  }
}

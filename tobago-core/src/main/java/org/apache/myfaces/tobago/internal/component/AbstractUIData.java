package org.apache.myfaces.tobago.internal.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.internal.model.TemporaryTreeState;
import org.apache.myfaces.tobago.model.TreeDataModel;

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;

public class AbstractUIData extends javax.faces.component.UIData {

  /**
   * Only for tree model.
   */
  private boolean initialized;

  /**
   * Only for tree model.
   */
  private DataModel dataModel;

  /**
   * Only for tree model.
   * Caches the expanded state information and the path of the nodes,
   * to make these information available in child nodes.
   */
  private TemporaryTreeState temporaryTreeState;

  // XXX Is there a better possibility to support tree data models?
  @Override
  protected DataModel getDataModel() {
    if (!initialized) {
      Object value = getValue();

      if (value instanceof DefaultMutableTreeNode) {
        dataModel = new TreeDataModel((DefaultMutableTreeNode) value);
      }
      initialized = true;
    }

    if (dataModel != null) {
      return dataModel;
    } else {
      return super.getDataModel();
    }
  }

  public boolean hasRows() {
    return getRows() != 0;
  }

  public TemporaryTreeState getTemporaryTreeState() {
    return temporaryTreeState;
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    temporaryTreeState = new TemporaryTreeState();
    super.encodeBegin(context);
  }

  @Override
  public void encodeEnd(FacesContext context) throws IOException {
    super.encodeEnd(context);
    temporaryTreeState = null;
  }
}

package org.apache.myfaces.tobago.model;

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

import javax.faces.model.DataModel;
import java.util.List;

/**
 * Abstract class that represents the data model for a tree.
 */
public abstract class TreeDataModel extends DataModel {

  public abstract void reset();

  public abstract void update(ExpandedState expandedState);

  @Override
  public abstract int getRowCount();

  @Override
  public abstract int getRowIndex();

  public abstract int getLevel();

  public abstract TreePath getPath();

  public abstract boolean isFolder();

  @Override
  public abstract Object getWrappedData();

  @Override
  public abstract boolean isRowAvailable();

  @Override
  public abstract void setRowIndex(int rowIndex);

  @Override
  public abstract void setWrappedData(Object data);

  public abstract boolean isRowVisible();

  public abstract String getRowClientId();

  public abstract void setRowClientId(String clientId);

  public abstract String getRowParentClientId();

  public abstract List<Integer> getRowIndicesOfChildren();

  public abstract List<Boolean> getJunctions();
}

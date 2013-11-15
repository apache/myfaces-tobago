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

import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.TreeModelBuilder;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.model.MixedTreeModel;

import javax.el.ValueExpression;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * @deprecated since 2.0.0
 */
@Deprecated
public abstract class AbstractUITreeData extends javax.faces.component.UIInput
    implements NamingContainer, TreeModelBuilder, OnComponentCreated {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TreeData";

  private String var;

  /**
   * Patch for backward compatibility.
   */
  public void onComponentCreated(final FacesContext facesContext, final UIComponent parent) {
    Deprecation.LOG.warn("Please not use the <tc:treeData> tag.");
    if (parent instanceof AbstractUITree) {
      final ValueExpression ve = getValueExpression("var");
      if (ve != null) {
        parent.setValueExpression("var", ve);
      } else {
        ((AbstractUITree) parent).setVar(getVar());
      }
    }

    if (parent instanceof AbstractUITree) {
      final ValueExpression ve = getValueExpression("value");
      if (ve != null) {
        parent.setValueExpression("value", ve);
      } else {
        ((AbstractUITree) parent).setValue(getValue());
      }
    }
  }

  public String getVar() {
    return var;
  }

  public void setVar(final String var) {
    this.var = var;
  }

  public void restoreState(final FacesContext context, final Object componentState) {
    final Object[] values = (Object[]) componentState;
    super.restoreState(context, values[0]);
    var = (String) values[1];
  }

  public Object saveState(final FacesContext context) {
    final Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = var;
    return values;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void buildTreeModelBegin(final FacesContext facesContext, final MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void buildTreeModelChildren(final FacesContext facesContext, final MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public void buildTreeModelEnd(final FacesContext facesContext, final MixedTreeModel model) {
    Deprecation.LOG.error("Doesn't work anymore.");
  }
}

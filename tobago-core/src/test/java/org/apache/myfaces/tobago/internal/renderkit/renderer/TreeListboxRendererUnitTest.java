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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.component.UITreeListbox;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.renderkit.css.CustomClass;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TreeListboxRendererUnitTest extends RendererTestBase {

  @Test
  public void treeListbox() throws IOException {
    final UITreeListbox treeListbox = (UITreeListbox) ComponentUtils.createComponent(
        facesContext, Tags.treeListbox.componentType(), RendererTypes.TreeListbox, "treeListbox");
    treeListbox.setVar("node");
    treeListbox.setValue(getTreeSample());

    final UITreeNode treeNode = (UITreeNode) ComponentUtils.createComponent(
        facesContext, Tags.treeNode.componentType(), RendererTypes.TreeNode, "treeNode");
    treeListbox.getChildren().add(treeNode);

    final UITreeLabel treeLabel = (UITreeLabel) ComponentUtils.createComponent(
        facesContext, Tags.treeLabel.componentType(), RendererTypes.TreeLabel, "treeLabel");
    treeNode.getChildren().add(treeLabel);

    treeListbox.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/treeListbox/treeListbox.html"), formattedResult());
  }

  @Test
  public void treeListboxCustomClass() throws IOException {
    final UITreeListbox treeListbox = (UITreeListbox) ComponentUtils.createComponent(
        facesContext, Tags.treeListbox.componentType(), RendererTypes.TreeListbox, "treeListbox");
    treeListbox.setVar("node");
    treeListbox.setValue(getTreeSample());
    treeListbox.setCustomClass(new CustomClass("custom-class"));

    final UITreeNode treeNode = (UITreeNode) ComponentUtils.createComponent(
        facesContext, Tags.treeNode.componentType(), RendererTypes.TreeNode, "treeNode");
    treeListbox.getChildren().add(treeNode);

    final UITreeLabel treeLabel = (UITreeLabel) ComponentUtils.createComponent(
        facesContext, Tags.treeLabel.componentType(), RendererTypes.TreeLabel, "treeLabel");
    treeNode.getChildren().add(treeLabel);

    treeListbox.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/treeListbox/treeListbox-customClass.html"), formattedResult());
  }
}

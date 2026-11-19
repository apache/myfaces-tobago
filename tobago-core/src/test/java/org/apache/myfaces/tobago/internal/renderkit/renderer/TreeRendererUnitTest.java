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

import org.apache.myfaces.test.el.MockValueExpression;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UITree;
import org.apache.myfaces.tobago.component.UITreeIcon;
import org.apache.myfaces.tobago.component.UITreeIndent;
import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TreeRendererUnitTest extends RendererTestBase {

  @Test
  public void treeIcon() throws IOException {
    final UITree tree = (UITree) ComponentUtils.createComponent(
        facesContext, Tags.tree.componentType(), RendererTypes.Tree, "tree");
    tree.setVar("node");
    tree.setShowRoot(false);
    tree.setValue(getTreeSample());
    tree.getExpandedState().expandAll();

    final UITreeNode treeNode = (UITreeNode) ComponentUtils.createComponent(
        facesContext, Tags.treeNode.componentType(), RendererTypes.TreeNode, "treeNode");
    tree.getChildren().add(treeNode);

    final UITreeIndent treeIndent = (UITreeIndent) ComponentUtils.createComponent(
        facesContext, Tags.treeIndent.componentType(), RendererTypes.TreeIndent, "treeIndent");
    treeNode.getChildren().add(treeIndent);

    final UITreeIcon treeIcon = (UITreeIcon) ComponentUtils.createComponent(
        facesContext, Tags.treeIcon.componentType(), RendererTypes.TreeIcon, "treeIcon");
    treeIcon.setValue("bi-file-earmark");
    treeIcon.setOpen("bi-folder2-open");
    treeIcon.setClosed("bi-folder2");
    treeNode.getChildren().add(treeIcon);

    final UITreeLabel treeLabel = (UITreeLabel) ComponentUtils.createComponent(
        facesContext, Tags.treeLabel.componentType(), RendererTypes.TreeLabel, "treeLabel");
    treeLabel.setValueExpression("value", new MockValueExpression("#{node.userObject}", String.class));
    treeNode.getChildren().add(treeLabel);

    tree.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/tree/treeIcon.html"), formattedResult());
  }

  @Test
  public void treeIconWithoutAttributes() throws IOException {
    final UITree tree = (UITree) ComponentUtils.createComponent(
        facesContext, Tags.tree.componentType(), RendererTypes.Tree, "tree");
    tree.setVar("node");
    tree.setShowRoot(false);
    tree.setValue(getTreeSample());
    tree.getExpandedState().expandAll();

    final UITreeNode treeNode = (UITreeNode) ComponentUtils.createComponent(
        facesContext, Tags.treeNode.componentType(), RendererTypes.TreeNode, "treeNode");
    tree.getChildren().add(treeNode);

    final UITreeIndent treeIndent = (UITreeIndent) ComponentUtils.createComponent(
        facesContext, Tags.treeIndent.componentType(), RendererTypes.TreeIndent, "treeIndent");
    treeNode.getChildren().add(treeIndent);

    final UITreeIcon treeIcon = (UITreeIcon) ComponentUtils.createComponent(
        facesContext, Tags.treeIcon.componentType(), RendererTypes.TreeIcon, "treeIcon");
    treeNode.getChildren().add(treeIcon);

    final UITreeLabel treeLabel = (UITreeLabel) ComponentUtils.createComponent(
        facesContext, Tags.treeLabel.componentType(), RendererTypes.TreeLabel, "treeLabel");
    treeLabel.setValueExpression("value", new MockValueExpression("#{node.userObject}", String.class));
    treeNode.getChildren().add(treeLabel);

    tree.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/tree/treeIconWithoutAttributes.html"), formattedResult());
  }
}

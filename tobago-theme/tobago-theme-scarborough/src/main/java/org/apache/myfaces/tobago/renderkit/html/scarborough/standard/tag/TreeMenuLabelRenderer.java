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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.component.UITreeLabel;
import org.apache.myfaces.tobago.internal.component.AbstractUITreeNode;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;

public class TreeMenuLabelRenderer extends TreeLabelRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(TreeMenuLabelRenderer.class);

  protected Style createStyle(FacesContext facesContext, UITreeLabel node) {

    final AbstractUITreeNode parent = (AbstractUITreeNode) node.getParent();
    final int level = parent.getLevel();
//    final boolean folder = parent.isFolder();

    Style style = new Style();
    Measure paddingLeft = getResourceManager().getThemeMeasure(facesContext, node, "custom.padding-left");
    paddingLeft = paddingLeft.multiply(level);
    style.setPaddingLeft(paddingLeft);
/*
    Measure width = ((UITreeMenu)(node.getParent().getParent().getParent())).getCurrentWidth();
    width = width.subtract(4); // XXX 4 = border + padding
    width = width.subtractNotNegative(paddingLeft);
    if (folder) {
      width = width.subtract(16);
    }
    style.setWidth(width);
*/
    return style;
  }
}

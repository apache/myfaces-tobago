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

package org.apache.myfaces.tobago.internal.context;

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.internal.webapp.XmlResponseWriter;
import org.junit.Assert;
import org.junit.Test;

import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.io.StringWriter;

public class ResponseWriterDividerUnitTest extends AbstractTobagoTestBase {

  @Test
  public void test() throws IOException {

    final StringWriter stringWriter = new StringWriter();
    getFacesContext().setResponseWriter(new XmlResponseWriter(stringWriter, "text/xml", "ISO-8859-1"));

    final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
    final DefaultMutableTreeNode colors = new DefaultMutableTreeNode("Colors");
    final DefaultMutableTreeNode numbers = new DefaultMutableTreeNode("Numbers");
    final DefaultMutableTreeNode integers = new DefaultMutableTreeNode("Integers");
    final DefaultMutableTreeNode doubles = new DefaultMutableTreeNode("Doubles");
    root.add(colors);
    root.add(numbers);
    numbers.add(integers);
    numbers.add(doubles);
    integers.add(new DefaultMutableTreeNode("1"));
    integers.add(new DefaultMutableTreeNode("2"));
    doubles.add(new DefaultMutableTreeNode("2.7182"));
    doubles.add(new DefaultMutableTreeNode("3.1415"));

    render(getFacesContext(), root);

    final ResponseWriterDivider divider = ResponseWriterDivider.getInstance(getFacesContext(), "unit test");
    divider.writeOutAndCleanUp(getFacesContext());

    final String expected
        = "(Root)\n"
        + "Colors\n"
        + "Numbers\n"
        + "(/Root)\n"
        + "(Numbers)\n"
        + "Integers\n"
        + "Doubles\n"
        + "(/Numbers)\n"
        + "(Integers)\n"
        + "1\n"
        + "2\n"
        + "(/Integers)\n"
        + "(Doubles)\n"
        + "2.7182\n"
        + "3.1415\n"
        + "(/Doubles)\n";

    Assert.assertEquals(expected, stringWriter.toString());
  }

  private void render(final FacesContext facesContext, final DefaultMutableTreeNode node) throws IOException {
    final ResponseWriterDivider divider = ResponseWriterDivider.getInstance(facesContext, "unit test");

    final String label = (String) node.getUserObject();

    // label
    if (!node.isRoot()) {
      facesContext.getResponseWriter().write(label + "\n");
    }

    // tag
    if (node.getChildCount() > 0) {
      divider.activateBranch(facesContext);
      facesContext.getResponseWriter().write("(" + label + ")\n");
      for (int i = 0; i < node.getChildCount(); i++) {
        final TreeNode sub = node.getChildAt(i);
        render(facesContext, (DefaultMutableTreeNode) sub);
      }
      facesContext.getResponseWriter().write("(/" + label + ")\n");
      divider.passivateBranch(facesContext);
    }
  }
}

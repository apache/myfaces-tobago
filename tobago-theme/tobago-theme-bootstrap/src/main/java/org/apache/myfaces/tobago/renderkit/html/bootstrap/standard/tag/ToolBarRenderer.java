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

package org.apache.myfaces.tobago.renderkit.html.bootstrap.standard.tag;

import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.bootstrap.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class ToolBarRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(ToolBarRenderer.class);

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIToolBar toolBar = (UIToolBar) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.NAV, toolBar);
    writer.writeClassAttribute("navbar navbar-form navbar-default");
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.NAVIGATION.toString(), false);

    writer.startElement(HtmlElements.DIV, toolBar);
    writer.writeClassAttribute(BootstrapClass.CONTAINER_FLUID.getName());

//    Brand and toggle get grouped for better mobile display
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute("navbar-header");

    writer.startElement(HtmlElements.BUTTON);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON, false);
    writer.writeClassAttribute("navbar-toggle");
    writer.writeAttribute("data-toggle", "collapse", false);
    writer.writeAttribute("data-target", "#" + toolBar.getClientId(facesContext).replace(":", "\\:"), true);

    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute("sr-only");
    writer.writeText("Toggle navigation");
    writer.endElement(HtmlElements.SPAN);

    for (int i = 0; i < 3; i++) {
      writer.startElement(HtmlElements.SPAN);
      writer.writeClassAttribute("icon-bar");
      writer.endElement(HtmlElements.SPAN);
    }

    writer.endElement(HtmlElements.BUTTON);

    writer.startElement(HtmlElements.A);
    writer.writeClassAttribute("navbar-brand");
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    writer.writeText("Address Book"); // fixme
    writer.endElement(HtmlElements.A);

    writer.endElement(HtmlElements.DIV);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute("collapse navbar-collapse");
    writer.writeIdAttribute(toolBar.getClientId(facesContext));
  }
/*
<nav class="navbar navbar-default" role="navigation">
      <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Brand</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Link</a></li>
            <li><a href="#">Link</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="#">Action</a></li>
                <li><a href="#">Another action</a></li>
                <li><a href="#">Something else here</a></li>
                <li class="divider"></li>
                <li><a href="#">Separated link</a></li>
                <li class="divider"></li>
                <li><a href="#">One more separated link</a></li>
              </ul>
            </li>
          </ul>
          <form class="navbar-form navbar-left" role="search">
            <div class="form-group">
              <input type="text" class="form-control" placeholder="Search"/>
            </div>
            <button type="submit" class="btn btn-default">Submit</button>
          </form>
          <ul class="nav navbar-nav navbar-right">
            <li><a href="#">Link</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="#">Action</a></li>
                <li><a href="#">Another action</a></li>
                <li><a href="#">Something else here</a></li>
                <li class="divider"></li>
                <li><a href="#">Separated link</a></li>
              </ul>
            </li>
          </ul>
        </div><!-- /.navbar-collapse -->
      </div><!-- /.container-fluid -->
    </nav>
* */
  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIToolBar toolBar = (UIToolBar) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

/*

    writer.startElement(HtmlElements.UL);
    writer.writeClassAttribute("nav navbar-nav");

    for (final UIComponent child : toolBar.getChildren()) {
      if (child instanceof AbstractUICommandBase) {
        final AbstractUICommandBase command =  (AbstractUICommandBase) child;
        writer.startElement(HtmlElements.LI);
        writer.writeClassAttribute("nav navbar-nav");

        writer.startElement(HtmlElements.A);
        writer.writeAttribute(HtmlAttributes.HREF, "http://www.heise.de/", true);
        final LabelWithAccessKey label = new LabelWithAccessKey(command);
        writer.writeText(label.getText());
        writer.endElement(HtmlElements.A);

        writer.endElement(HtmlElements.LI);
      } else if (child instanceof UIToolBarSeparator) {
        final UIToolBarSeparator separator =  (UIToolBarSeparator) child;
      } else {
        LOG.error("Illegal UIComponent class in toolbar (not a AbstractUICommandBase):" + child.getClass().getName());
      }
    }

    writer.endElement(HtmlElements.UL);

*/
    writer.endElement(HtmlElements.DIV);

    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.NAV);
  }
}

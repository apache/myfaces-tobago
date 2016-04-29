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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class CkeditorController extends SourceFileReader implements Serializable {

  private boolean editorAvailable;
  private String contentSecurityPolicyMode;
  private String text;
  private boolean collapsed;

  public CkeditorController() {
    editorAvailable = ResourceManagerUtils.getScripts(
            FacesContext.getCurrentInstance(),
            "content/20-component/110-wysiwyg/01-ckeditor/ckeditor/ckeditor.js")
            .size() != 0;

    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(FacesContext.getCurrentInstance());
    contentSecurityPolicyMode = tobagoConfig.getContentSecurityPolicy().getMode().getValue();

    text = "<p><strong>Sun</strong></p>"
            + "<p>The sun is a star in our galaxy.</p>";
    collapsed = true;
  }

  public boolean isEditorAvailable() {
    return editorAvailable;
  }

  public String getContentSecurityPolicyMode() {
    return contentSecurityPolicyMode;
  }

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public void setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
  }

  public void switchCollapsed() {
    collapsed = !collapsed;
  }

  public String getSource() {
    return getSource("ckeditor.js");
  }
}

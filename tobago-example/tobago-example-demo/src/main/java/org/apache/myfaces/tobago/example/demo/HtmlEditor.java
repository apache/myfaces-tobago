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

import org.apache.myfaces.tobago.context.ResourceManagerUtils;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class HtmlEditor implements Serializable {

  private String text;
  private boolean ckeditorAvailable;
  private boolean tinymceAvailable;

  public HtmlEditor() {
    ckeditorAvailable = ResourceManagerUtils.getScripts(
        FacesContext.getCurrentInstance(),
        "content/35-wysiwyg/01-ckeditor/ckeditor/ckeditor.js")
        .size() != 0;
    tinymceAvailable = ResourceManagerUtils.getScripts(
        FacesContext.getCurrentInstance(),
        "content/35-wysiwyg/00-tinymce/tinymce/js/tinymce/tinymce.min.js")
        .size() != 0;
    text = "<h1>Sonne</h1>"
        + "<p>Die Sonne ist ein Stern in der Galaxie Milchstraße. "
        + "Sie ist ein Hauptreihenstern (Zwergstern) und steht im Zentrum des Sonnensystems, "
        + "das sie durch ihre Gravitation dominiert."
        + "Hauptbestandteile"
        + "<ul>\n"
        + "<li>Wasserstoff: 92,1&thinsp;%</li>\n"
        + "<li>Helium: 7,8&thinsp;%</li>\n"
        + "<li>Sauerstoff: 500&nbsp;ppm</li>\n"
        + "<li>Kohlenstoff: 230&nbsp;ppm</li>\n"
        + "<li>Neon: 100&nbsp;ppm</li>\n"
        + "<li>Stickstoff: 70&nbsp;ppm</li>\n"
        + "</ul></p>"
        + "<p>Quelle: Wikipedia</p>";
  }

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public boolean isCkeditorAvailable() {
    return ckeditorAvailable;
  }

  public boolean isTinymceAvailable() {
    return tinymceAvailable;
  }

}

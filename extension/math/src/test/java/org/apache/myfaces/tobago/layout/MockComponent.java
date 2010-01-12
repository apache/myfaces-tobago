package org.apache.myfaces.tobago.layout;

import org.apache.myfaces.tobago.context.Markup;

import javax.faces.component.UIComponentBase;

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

public class MockComponent extends UIComponentBase implements LayoutComponent {

  private Integer columnSpan = 1;

  private Integer rowSpan = 1;

  private Measure width;
  private Measure height;

  private Measure currentWidth;
  private Measure currentHeight;

  private Measure minimumWidth;
  private Measure minimumHeight;

  private Measure preferredWidth;
  private Measure preferredHeight;

  private Measure maximumWidth;
  private Measure maximumHeight;

  private Measure left;
  private Measure top;

  private Integer horizontalIndex;
  private Integer verticalIndex;

  private Display display;
  
  private Markup markup;

  public String getFamily() {
    return null;
  }

  public Integer getColumnSpan() {
    return columnSpan;
  }

  public void setColumnSpan(Integer columnSpan) {
    this.columnSpan = columnSpan;
  }

  public Integer getRowSpan() {
    return rowSpan;
  }

  public void setRowSpan(Integer rowSpan) {
    this.rowSpan = rowSpan;
  }

  public Measure getWidth() {
    return width;
  }

  public void setWidth(Measure width) {
    this.width = width;
  }

  public Measure getHeight() {
    return height;
  }

  public void setHeight(Measure height) {
    this.height = height;
  }

  public Measure getCurrentWidth() {
    return currentWidth;
  }

  public void setCurrentWidth(Measure currentWidth) {
    this.currentWidth = currentWidth;
  }

  public Measure getCurrentHeight() {
    return currentHeight;
  }

  public void setCurrentHeight(Measure currentHeight) {
    this.currentHeight = currentHeight;
  }

  public Measure getMinimumWidth() {
    return minimumWidth;
  }

  public void setMinimumWidth(Measure minimumWidth) {
    this.minimumWidth = minimumWidth;
  }

  public Measure getMinimumHeight() {
    return minimumHeight;
  }

  public void setMinimumHeight(Measure minimumHeight) {
    this.minimumHeight = minimumHeight;
  }

  public Measure getPreferredWidth() {
    return preferredWidth;
  }

  public void setPreferredWidth(Measure preferredWidth) {
    this.preferredWidth = preferredWidth;
  }

  public Measure getPreferredHeight() {
    return preferredHeight;
  }

  public void setPreferredHeight(Measure preferredHeight) {
    this.preferredHeight = preferredHeight;
  }

  public Measure getMaximumWidth() {
    return maximumWidth;
  }

  public void setMaximumWidth(Measure maximumWidth) {
    this.maximumWidth = maximumWidth;
  }

  public Measure getMaximumHeight() {
    return maximumHeight;
  }

  public void setMaximumHeight(Measure maximumHeight) {
    this.maximumHeight = maximumHeight;
  }

  public Measure getLeft() {
    return left;
  }

  public void setLeft(Measure left) {
    this.left = left;
  }

  public Measure getTop() {
    return top;
  }

  public void setTop(Measure top) {

    this.top = top;
  }

  public Integer getHorizontalIndex() {
    return horizontalIndex;
  }

  public void setHorizontalIndex(Integer horizontalIndex) {
    this.horizontalIndex = horizontalIndex;
  }

  public Integer getVerticalIndex() {
    return verticalIndex;
  }

  public void setVerticalIndex(Integer verticalIndex) {
    this.verticalIndex = verticalIndex;
  }

  public Display getDisplay() {
    return display;
  }

  public void setDisplay(Display display) {
    this.display = display;
  }

  public Markup getMarkup() {
    return markup;
  }

  public void setMarkup(Markup markup) {
    this.markup = markup;
  }
}

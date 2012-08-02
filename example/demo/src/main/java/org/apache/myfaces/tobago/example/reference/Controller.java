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

package org.apache.myfaces.tobago.example.reference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.taglib.component.ButtonTag;
import org.apache.myfaces.tobago.taglib.component.LinkTag;
import org.apache.myfaces.tobago.taglib.extension.InExtensionTag;

import javax.servlet.jsp.tagext.TagSupport;
import javax.faces.event.ValueChangeEvent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

public class Controller {

  private static final Log LOG = LogFactory.getLog(Controller.class);

  private List<TagData> tags;

  private List<AttributeData> attributes;

  private String text;

  private boolean bool;

  private SelectItem[] vehicleOptionItems;

  private SelectItem[] carOptionItems;

  private SelectItem[] motorbikeOptionItems;

  private int vehicle;

  private int manufacturer;

  public Controller() {
    tags = new ArrayList<TagData>();
    TagData in = new TagData(InExtensionTag.class);
    in.setName("In");
    in.setTip("Ein In");
    tags.add(in);
    TagData button = new TagData(ButtonTag.class);
    button.setName("Button");
    button.setTip("Ein Knopf");
    tags.add(button);
    TagData link = new TagData(LinkTag.class);
    link.setName("Link");
    link.setTip("Ein Link");
    tags.add(link);
    attributes = new ArrayList<AttributeData>();
    vehicleOptionItems = new SelectItem[]{
        new SelectItem(new Integer(0), "car"),
        new SelectItem(new Integer(1), "motorbike")};
    carOptionItems = new SelectItem[]{
        new SelectItem(new Integer(0), "Audi"),
        new SelectItem(new Integer(1), "BMW"),
        new SelectItem(new Integer(2), "Mercedes")};
    motorbikeOptionItems = new SelectItem[]{
        new SelectItem(new Integer(3), "Moto Guzzi"),
        new SelectItem(new Integer(4), "BMW"),
        new SelectItem(new Integer(5), "KTM")};
  }


  public int getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(int manufacturer) {
    this.manufacturer = manufacturer;
  }

  public SelectItem[] getSelectItems() {
    return vehicleOptionItems;
  }

  public int getVehicle() {
    return vehicle;
  }

  public String action() {
    LOG.error("action invoke");
    return null;
  }

  public void setVehicle(int vehicle) {
    this.vehicle = vehicle;
  }

  public SelectItem[] getManufacturerSelectItems() {
    if (vehicle == 0) {
      return carOptionItems;
    } else {
      return motorbikeOptionItems;
    }
  }


  public void valueChanged(ValueChangeEvent event) {
    LOG.error("value Changed " + event.getComponent().getClientId(FacesContext.getCurrentInstance()));
    LOG.error("value Changed " + event.getOldValue() + " " + event.getNewValue());
  }

  public TagSupport createTag() {
    try {
      Class clazz = tags.get(0).getTagClass();
      InExtensionTag tag = (InExtensionTag) clazz.newInstance();
      tag.setValue("Hallo Tester");
      tag.setLabel("Label");
      return tag;
    } catch (Exception e) {
      LOG.error("", e); // fixme
      throw new RuntimeException(e);
    }
  }

  public List<TagData> getTags() {
    return tags;
  }

  public List<AttributeData> getAttributes() {
    return attributes;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }


  public boolean isBool() {
    return bool;
  }

  public void setBool(boolean bool) {
    this.bool = bool;
  }
}


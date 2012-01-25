package org.apache.myfaces.tobago.example.reference;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

public class Controller {

  private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

  private String text;

  private boolean bool;

  private SelectItem[] vehicleOptionItems;

  private SelectItem[] carOptionItems;

  private SelectItem[] motorbikeOptionItems;

  private int vehicle;

  private int manufacturer;

  public Controller() {

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
    LOG.info("Value change event in component with id: '"
        + event.getComponent().getClientId(FacesContext.getCurrentInstance())
        + "'. Value changed from '" + event.getOldValue() + "' to '" + event.getNewValue() + "'");
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


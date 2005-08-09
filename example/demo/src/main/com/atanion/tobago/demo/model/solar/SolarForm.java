/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 21.08.2002 at 18:00:22.
 * $Id: SolarForm.java 865 2004-04-30 18:02:34 +0200 (Fr, 30 Apr 2004) lofwyr $
 */
package com.atanion.tobago.demo.model.solar;

public class SolarForm {

// ///////////////////////////////////////////// attributes

  private Solar solar;

// ///////////////////////////////////////////// constructor

  public SolarForm() {
    solar = new Solar();
  }

// ///////////////////////////////////////////// bean getter + setter

  public Solar getSolar() {
    return solar;
  }

  public void setSolar(Solar solar) {
    this.solar = solar;
  }
}

/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 15.12.2003 11:46:32.
 * $Id$
 */
package com.atanion.tobago.convert;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UIInput;

import javax.faces.convert.Converter;

public class DurationConverterUnitTest extends ConverterUnitTestBase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private Converter converter;

// ///////////////////////////////////////////// constructor

  public DurationConverterUnitTest(String string) {
    super(string);
  }

// ///////////////////////////////////////////// code

  protected void setUp() throws Exception {
    super.setUp();
    converter = application.createConverter(DurationConverter.CONVERTER_ID);
  }


  public void testFormat() {

    format(null, new Long(1000L), "0:01");
    format("second", new Long(1000L), "16:40");
    format("minute", new Long(-100L), "-1:40:00");
    format("hour", new Long(1L), "1:00:00");
    format("day", new Long(1L), "24:00:00");
    format("year", new Long(1L), "8765:45:36");

  }

  public void testParse() {

    parse(null, new Long(1000L), "0:01");
    parse("second", new Long(1001L), "16:41");
    parse("minute", new Long(-16L), "-16:41");
    parse("hour", new Long(1L), "1:00:00");
    parse("day", new Long(1L), "24:00:00");
    parse("year", new Long(1L), "8765:45:36");
  }

  private void format(String unit, Long aLong, String string) {
    UIInput input = new UIInput();
    String info = "Formatting numbers:"
        + " unit='" + unit + "'"
        + " long='" + aLong + "'";
    String result;
    if (unit != null) {
      input.getAttributes().put(TobagoConstants.ATTR_UNIT, unit);
    }
    result = converter.getAsString(facesContext, input, aLong);
    assertEquals(info, string, result);
  }

  private void parse(String unit, Long aLong, String string) {
    UIInput input = new UIInput();
    String info = "Parsing numbers:"
        + " unit='" + unit + "'"
        + " string='" + string + "'";
    Long result;
    if (unit != null) {
      input.getAttributes().put(TobagoConstants.ATTR_UNIT, unit);
    }
    result = (Long) converter.getAsObject(facesContext, input, string);
    assertEquals(info, aLong, result);
  }


// ///////////////////////////////////////////// bean getter + setter

}

/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 31.03.2004 11:35:55.
 * $Id$
 */
package com.atanion.tobago.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import java.util.List;

public class Converter {

// ///////////////////////////////////////////// constant

  private static final Class[] PRIMITIVE_CLASSES = {
    Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE,
    Integer.TYPE, Long.TYPE, Short.TYPE
  };

// ///////////////////////////////////////////// attribute

  private String converterId;

  private String converterForClass;

  private String converterClass;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  protected static void addConvertersToApplication(
      List converters, Application application) throws ClassNotFoundException {
    ClassLoader classLoader = Converter.class.getClassLoader();
    for (int i = 0; i < converters.size(); i++) {
      Converter converter = (Converter) converters.get(i);
      if (converter.getConverterId() != null) {
        application.addConverter(
            converter.getConverterId(),
            converter.getConverterClass());
      } else {
        boolean primitive = false;
        String forClass = converter.getConverterForClass();
        // add the primitive type, if any
        for (int j = 0; j < PRIMITIVE_CLASSES.length; j++) {
          if (forClass.equals(PRIMITIVE_CLASSES[j].getName())) {
            application.addConverter(
                PRIMITIVE_CLASSES[j],
                converter.getConverterClass());
            primitive = true;
            break;
          }
        }
        if (!primitive) {
          // add the ordinary class
          application.addConverter(
              classLoader.loadClass(forClass),
              converter.getConverterClass());
        }
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getConverterId() {
    return converterId;
  }

  public void setConverterId(String converterId) {
    this.converterId = converterId;
  }

  public String getConverterForClass() {
    return converterForClass;
  }

  public void setConverterForClass(String converterForClass) {
    this.converterForClass = converterForClass;
  }

  public String getConverterClass() {
    return converterClass;
  }

  public void setConverterClass(String converterClass) {
    this.converterClass = converterClass;
  }
}

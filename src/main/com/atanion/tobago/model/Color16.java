/*
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jul 9, 2002
 * Time: 5:42:31 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.atanion.tobago.model;

import com.atanion.model.AbstractSingleSelection;
import com.atanion.model.SingleSelectionModel;
import com.atanion.model.StringSingleSelection;

import java.io.Serializable;

public class Color16 extends AbstractSingleSelection
    implements Serializable, StringSingleSelection, ColorChooser{

  public static final int CUSTOM = 0;
  public static final int SELECT = 1;
  public static final int BLACK = 2;
  public static final int SILVER = 3;
  public static final int GRAY = 4;
  public static final int WHITE = 5;
  public static final int MAROON = 6;
  public static final int RED = 7;
  public static final int PURPLE = 8;
  public static final int FUCHSIA = 9;
  public static final int GREEN = 10;
  public static final int LIME = 11;
  public static final int OLIVE = 12;
  public static final int YELLOW = 13;
  public static final int NAVY = 14;
  public static final int BLUE = 15;
  public static final int TEAL = 16;
  public static final int AQUA = 17;

  private static String[] values = {
    null, // custom color
    null, // select
    "000000", // black
    "C0C0C0", // silver
    "808080", // gray
    "FFFFFF", // white
    "800000", // maroon
    "FF0000", // red
    "800080", // purple
    "FF00FF", // fuchsia
    "008000", // green
    "00FF00", // lime
    "808000", // olive
    "FFFF00", // yellow
    "000080", // navy
    "0000FF", // blue
    "008080", // teal
    "00FFFF"  // aqua
  };

  private static String[] i18nKeys = {
    "color16Custom",
    "color16Select",
    "color16Black",
    "color16Silver",
    "color16Gray",
    "color16White",
    "color16Maroon",
    "color16Red",
    "color16Purple",
    "color16Fuchsia",
    "color16Green",
    "color16Lime",
    "color16Olive",
    "color16Yellow",
    "color16Navy",
    "color16Blue",
    "color16Teal",
    "color16Aqua"
  };

  private String value;

  public Color16() {
    this(SELECT);
  }

  public Color16(int color) {
    super();
    super.setSelectedIndex(color);
  }

  public Color16(String hexValue) {
    super();
    setValue(hexValue);
  }

  public int getCapacity() {
    return values.length;
  }

// //////////////////////////////////////////// interface StringSingleSelection

  public String getString(int i) {
    return i18nKeys[i];
  }

  public String getSelectedString() {
    return getString(getSelectedIndex());
  }

// //////////////////////////////////////////////

  public String getRGBValue() {
    return getValue();
  }

  public void setRGBValue(String value) {
    setValue(value);
  }

  public String getValue() {
    return value;
  }


  public boolean isValid(){
    try {
      int test = Integer.parseInt(value, 16);
      if (test < 0 && test > 0xffffff) {
        throw new NumberFormatException(value);
      }
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  public void setValue(String value) {
    this.value = value;
    int select = 0;
    for (int i = 0; i < values.length; i++) {
      if (value.equalsIgnoreCase(values[i])) {
        select = i;
        break;
      }
    }
    super.setSelectedIndex(select);
  }

  public boolean equals(Object obj) {
    if (obj instanceof Color16) {
      return ((Color16) obj).value.equalsIgnoreCase(value);
    } else {
      return false;
    }
  }

  public SingleSelectionModel getSingleSelectionModel(){
    return this;
  };


  public int hashCode() {
    return value != null && value.length() > 0 ? Integer.parseInt(value, 16) : -1;
  }

  public void setSelectedIndex(int index) {
    // do nothing setting only via setValue();
  }
}

package org.apache.myfaces.tobago.layout;

import org.apache.commons.lang.StringUtils;

/*
 * User: lofwyr
 * Date: 23.01.2008 20:12:30
 */
public abstract class Measure {

  // todo: refactor and consolidate with LayoutToken

  public static Measure parse(String value) {
    if (StringUtils.isEmpty(value)) {
      return new PixelMeasure(0); // fixme: may return a "default measure", or is Pixel the default?
    }
    if (value.toLowerCase().matches("\\d+px")) {// XXX no regexp here: user LayoutTokens.parse !!!
      return new PixelMeasure(Integer.parseInt(value.substring(0, value.length() - 2)));
    }
    throw new IllegalArgumentException("Can't parse to any measure: '" + value + "'");
  }

  public abstract Measure add(Measure m);

  public abstract Measure substractNotNegative(Measure m);

  public abstract int getPixel();
}

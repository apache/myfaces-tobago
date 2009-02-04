package org.apache.myfaces.tobago.layout;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.util.LayoutUtil;

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
    // XXX improve code sharing with LayoutTokens.parse
    if (LayoutUtil.isNumberAndSuffix(value.toLowerCase(), "px")) {
      return new PixelMeasure(Integer.parseInt(LayoutUtil.removeSuffix(value, "px")));
    }
    throw new IllegalArgumentException("Can't parse to any measure: '" + value + "'");
  }

  public abstract Measure add(Measure m);

  public abstract Measure substractNotNegative(Measure m);

  public abstract int getPixel();
}

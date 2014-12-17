package org.apache.myfaces.tobago.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class MimeTypeUtilsUniTest {

  public static final int INT = 100000000;

  public static final String GIF = "http:///localhost:8080/demo/demo.gif";
  public static final String PNG = "http:///localhost:8080/demo/demo.png";
  public static final String JPG = "http:///localhost:8080/demo/demo.jpg";
  public static final String JS = "http:///localhost:8080/demo/demo.js";
  public static final String CSS = "http:///localhost:8080/demo/demo.css";
  public static final String ICO = "http:///localhost:8080/demo/demo.ico";
  public static final String HTML = "http:///localhost:8080/demo/demo.html";
  public static final String HTM = "http:///localhost:8080/demo/demo.htm";
  public static final String MAP = "http:///localhost:8080/demo/demo.map";
  public static final String WOFF = "http:///localhost:8080/demo/demo.woff";

  public static final String UNKNOWN0 = "http:///localhost:8080/demo/demo.PNG";
  public static final String UNKNOWN1 = "http:///localhost:8080/demo/demos._png";
  public static final String UNKNOWN2 = "http:///localhost:8080/demo/demo.ggif";
  public static final String UNKNOWN3 = "http:///localhost:8080/demo/demos.ppg";

  @Test
  public void testMimeTypes() {

    Assert.assertEquals("image/gif", MimeTypeUtils.getMimeTypeForFile(GIF));
    Assert.assertEquals("image/png", MimeTypeUtils.getMimeTypeForFile(PNG));
    Assert.assertEquals("image/jpeg", MimeTypeUtils.getMimeTypeForFile(JPG));
    Assert.assertEquals("text/javascript", MimeTypeUtils.getMimeTypeForFile(JS));
    Assert.assertEquals("text/css", MimeTypeUtils.getMimeTypeForFile(CSS));
    Assert.assertEquals("image/vnd.microsoft.icon", MimeTypeUtils.getMimeTypeForFile(ICO));
    Assert.assertEquals("text/html", MimeTypeUtils.getMimeTypeForFile(HTML));
    Assert.assertEquals("text/html", MimeTypeUtils.getMimeTypeForFile(HTM));
    Assert.assertEquals("application/json", MimeTypeUtils.getMimeTypeForFile(MAP));
    Assert.assertEquals("application/font-woff", MimeTypeUtils.getMimeTypeForFile(WOFF));
  }

  @Test
  public void testMimeTypesUnknown() {

    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN0));
    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN1));
    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN2));
    Assert.assertEquals(null, MimeTypeUtils.getMimeTypeForFile(UNKNOWN3));
  }

//  @Test
  public void testPerformace() {
    final long start = System.currentTimeMillis();
    for (int i = 0; i < INT; i++) {
      MimeTypeUtils.getMimeTypeForFile(GIF);
      MimeTypeUtils.getMimeTypeForFile(PNG);
      MimeTypeUtils.getMimeTypeForFile(JPG);
      MimeTypeUtils.getMimeTypeForFile(JS);
      MimeTypeUtils.getMimeTypeForFile(CSS);
      MimeTypeUtils.getMimeTypeForFile(ICO);
      MimeTypeUtils.getMimeTypeForFile(HTML);
      MimeTypeUtils.getMimeTypeForFile(HTM);
      MimeTypeUtils.getMimeTypeForFile(MAP);
      MimeTypeUtils.getMimeTypeForFile(WOFF);
      MimeTypeUtils.getMimeTypeForFile(UNKNOWN0);
    }
    System.out.println("-----------------------> " + (System.currentTimeMillis() - start) + " ms");
  }

}
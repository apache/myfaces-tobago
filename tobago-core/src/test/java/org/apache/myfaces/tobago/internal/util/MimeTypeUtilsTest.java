package org.apache.myfaces.tobago.internal.util;

import org.junit.Assert;
import org.junit.Test;

public class MimeTypeUtilsTest {

  @Test
  public void testGetMimeTypeForFile() throws Exception {
     MimeTypeUtils.init(null);
    Assert.assertEquals("image/gif", MimeTypeUtils.getMimeTypeForFile("image.gif"));
    Assert.assertEquals("image/png", MimeTypeUtils.getMimeTypeForFile("images/red.png"));
    Assert.assertEquals("image/jpeg", MimeTypeUtils.getMimeTypeForFile("images/button.jpg"));
    Assert.assertEquals("text/javascript", MimeTypeUtils.getMimeTypeForFile("path/tobago.js"));
    Assert.assertEquals("text/css", MimeTypeUtils.getMimeTypeForFile("tobago.css"));
    Assert.assertEquals("image/vnd.microsoft.icon", MimeTypeUtils.getMimeTypeForFile("tobago.ico"));
    Assert.assertEquals("text/html", MimeTypeUtils.getMimeTypeForFile("test.html"));
    Assert.assertEquals("text/html", MimeTypeUtils.getMimeTypeForFile("test.htm"));
    Assert.assertEquals("application/json", MimeTypeUtils.getMimeTypeForFile("object.map"));
    Assert.assertNull(MimeTypeUtils.getMimeTypeForFile("notValid.extension"));
  }
}
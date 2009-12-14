package org.apache.myfaces.tobago.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class LocaleUtilsUnitTest {

  private static final Locale D1 = new Locale("de");
  private static final Locale D2 = new Locale("de", "DE");
  private static final Locale D3 = new Locale("de", "DE", "NS");

  private static final String S1 = "de";
  private static final String S2 = "de_DE";
  private static final String S3 = "de_DE_NS";

  private static final String D = "default";

  @Test
  public void testCreateLocale() {
    Assert.assertEquals(D1, LocaleUtil.createLocale(S1));
    Assert.assertEquals(D2, LocaleUtil.createLocale(S2));
    Assert.assertEquals(D3, LocaleUtil.createLocale(S3));
  }

  @Test
  public void testLocaleList() {
    Assert.assertArrayEquals(new Locale[]{D1}, LocaleUtil.getLocaleList(D1).toArray());
    Assert.assertArrayEquals(new Locale[]{D2, D1}, LocaleUtil.getLocaleList(D2).toArray());
    Assert.assertArrayEquals(new Locale[]{D3, D2, D1}, LocaleUtil.getLocaleList(D3).toArray());
  }

  @Test
  public void testLocaleSuffixList() {
    Assert.assertArrayEquals(new String[]{'_' + S1, ""}, LocaleUtil.getLocaleSuffixList(D1).toArray());
    Assert.assertArrayEquals(new String[]{'_' + S2, '_' + S1, ""}, LocaleUtil.getLocaleSuffixList(D2).toArray());
    Assert.assertArrayEquals(new String[]{'_' + S3, '_' + S2, '_' + S1, ""}, LocaleUtil.getLocaleSuffixList(D3).toArray());
  }
}

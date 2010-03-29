package org.apache.myfaces.tobago.context;

import org.junit.Assert;
import org.junit.Test;

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

public class UserAgentUnitTest {

  private static final String IE_8_WINDOWS_XP
      = "User-Agent: Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727;"
      + " .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)";
  private static final String FIREFOX_3_6_UBUNTU_10_4
      = "User-Agent: Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2) Gecko/20100308 Ubuntu/10.04 (lucid)"
      + " Firefox/3.6";
  private static final String OPERA_10_10_MACOSX
      = "User-Agent: Opera/9.80 (Macintosh; Intel Mac OS X; U; de) Presto/2.2.15 Version/10.10";


  private static final String FIREFOX_3_6_UBUNTU_9_10 =
      "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.3pre) Gecko/20100328 Ubuntu/9.10 (karmic) Namoroka/3.6.3pre";

  private static final String CHROMIUM_5_0_365_UBUNTU_9_10 =
      "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/533.3 (KHTML, like Gecko) Chrome/5.0.365.0 Safari/533.3";

  private static final String CHROME_5_0_360_UBUNTO_9_10 =
      "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/533.3 (KHTML, like Gecko) Chrome/5.0.360.0 Safari/533.3";

  private static final String OPERA_10_51_UBUNTU_9_10 =
      "Opera/9.80 (X11; Linux i686; U; en) Presto/2.5.22 Version/10.51";

  @Test
  public void test() {

    Assert.assertEquals(UserAgent.MSIE_8_0, UserAgent.getInstance(IE_8_WINDOWS_XP));

    Assert.assertEquals(UserAgent.GECKO, UserAgent.getInstance(FIREFOX_3_6_UBUNTU_10_4));

    Assert.assertEquals(UserAgent.OPERA, UserAgent.getInstance(OPERA_10_10_MACOSX));
  }
}

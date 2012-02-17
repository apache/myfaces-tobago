package org.apache.myfaces.tobago.context;

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


import org.apache.myfaces.tobago.util.Parameterized;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class UserAgentUnitTest {

  private UserAgent agent;
  private String headerString;

  public UserAgentUnitTest(String title, UserAgent agent, String headerString) {
    this.agent = agent;
    this.headerString = headerString;
  }

  @Test
  public void test() {
    Assert.assertEquals(agent, UserAgent.getInstance(headerString));
  }

  @Parameterized.Parameters
  public static List<Object[]> data() {
    return Arrays.asList(new Object[][]{

        {"Internet Explorer 6 - Windows 2000",
            UserAgent.MSIE_6_0,
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0"},

        {"Internet Explorer 6 - Windows XP",
            UserAgent.MSIE_6_0,
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)'"},

        {"Internet Explorer 7 - Windows XP",
            UserAgent.MSIE_7_0,
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"},

        {"Internet Explorer 8 - Windows XP",
            UserAgent.MSIE_8_0,
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727;"
            + " .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)"},

        {"Internet Explorer 8 - Compatibility Mode - Windows XP",
            UserAgent.MSIE_7_0_COMPAT,
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0)"},

        {"Internet Explorer 9 preview - Windows 7",
            UserAgent.MSIE_9_0,
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)"},

        {"Internet Explorer 9 - Windows 7",
            UserAgent.MSIE_9_0,
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)"},

        {"Internet Explorer 9 - Compatibility Mode - Windows 7",
            UserAgent.MSIE_7_0_COMPAT,
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Win64; x64; Trident/5.0; "
                + ".NET CLR 2.0.50727; SLCC2; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C)"},

        {"Internet Explorer 10 - Windows 8 - Developer Preview",
            UserAgent.MSIE_10_0,
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)"},

        {"Internet Explorer 10 - Compatibility Mode - Windows 8 - Developer Preview",
            UserAgent.MSIE_7_0_COMPAT,
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.2; WOW64; Trident/6.0; .NET4.0E; .NET4.0C)"},

        {"Firefox 3.6 - Ubuntu 10.4",
            UserAgent.GECKO_1_9,
            "Mozilla/5.0 (X11; U; Linux x86_64; en-US; rv:1.9.2) Gecko/20100308 Ubuntu/10.04 (lucid) Firefox/3.6"},

        {"Firefox 3.6 - Ubuntu 9.10",
            UserAgent.GECKO_1_9,
            "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.3pre) Gecko/20100328 Ubuntu/9.10 (karmic) "
                + "Namoroka/3.6.3pre"},

        {"Firefox 5.0 - Mac OS X Snow Leopard",
            UserAgent.GECKO_5_0,
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:5.0) Gecko/20100101 Firefox/5.0"},

        {"Firefox 4.0 beta 2 - Mac OS X Snow Leopard",
            UserAgent.GECKO_2_0,
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0b2) Gecko/20100720 Firefox/4.0b2"},

        {"Firefox 3.6 - Mac OS X Snow Leopard",
            UserAgent.GECKO_1_9,
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; de; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8"},

        {"Firefox 3.5 - Mac OS X Snow Leopard",
            UserAgent.GECKO_1_9,
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; de; rv:1.9.1.9) Gecko/20100315 Firefox/3.5.9"},

        {"Firefox 3.0 - Mac OS X Snow Leopard",
            UserAgent.GECKO_1_9,
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.6; de; rv:1.9.0.19) Gecko/2010031218 Firefox/3.0.19"},

        {"Firefox 2.0 - Mac OS X Snow Leopard",
            UserAgent.GECKO_1_8,
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de; rv:1.8.1.20) Gecko/20081217 Firefox/2.0.0.20"},

        {"Opera 10.61 - Windows 7",
            UserAgent.PRESTO,
            "Opera/9.80 (Windows NT 6.1; U; de) Presto/2.6.30 Version/10.61"},

        {"Opera 10.10 - Mac OS X Snow Leopard",
            UserAgent.PRESTO,
            "Opera/9.80 (Macintosh; Intel Mac OS X; U; de) Presto/2.2.15 Version/10.10"},

        {"Opera 10.51 - Ubuntu 9.10",
            UserAgent.PRESTO,
            "Opera/9.80 (X11; Linux i686; U; en) Presto/2.5.22 Version/10.51"},

        {"Chrome 4.1 - Windows 7",
            UserAgent.WEBKIT,
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/532.5 (KHTML, like Gecko)"
            + " Chrome/4.1.249.1042 Safari/532.5"},

        {"Chrome 5 - Mac OS X Snow Leopard",
            UserAgent.WEBKIT,
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; en-US) AppleWebKit/533.4 (KHTML, like Gecko)"
            + " Chrome/5.0.375.127 Safari/533.4"},

        {"Chrome 5 - Ubuntu 9.10",
            UserAgent.WEBKIT,
            "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/533.3 (KHTML, like Gecko)"
            + " Chrome/5.0.360.0 Safari/533.3"},

        {"Chromium 5 - Ubuntu 9.10",
            UserAgent.WEBKIT,
            "Mozilla/5.0 (X11; U; Linux i686; en-US) AppleWebKit/533.3 (KHTML, like Gecko)"
            + " Chrome/5.0.365.0 Safari/533.3"},

        {"Safari 5 - Mac OS X Snow Leopard",
            UserAgent.WEBKIT,
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; de-de) AppleWebKit/533.17.8 (KHTML, like Gecko) "
            + "Version/5.0.1 Safari/533.17.8"},

        {"Safari 4 - Mac OS X Snow Leopard",
            UserAgent.WEBKIT,
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_2; de-de) AppleWebKit/531.22.7 (KHTML, like Gecko) "
            + "Version/4.0.5 Safari/531.22.7"},

        {"Safari 4 - iPad",
            UserAgent.WEBKIT,
            "Mozilla/5.0 (iPad; U; CPU OS 3_2_1 like Mac OS X; de-de) AppleWebKit/531.21.10 (KHTML, like Gecko) "
                + "Version/4.0.4 Mobile/7B405 Safari/531.21.10"},

        {"Web-Browser for S60 - Symbian - Nokia E51",
            UserAgent.WEBKIT,
            "Mozilla/5.0 "
                + "(SymbianOS/9.2; U; Series60/3.1 NokiaE51-1/300.34.56; Profile/MIDP-2.0 Configuration/CLDC-1.1 ) "
                + "AppleWebKit/413 (KHTML, like Gecko) Safari/413"},
    });
  }
}

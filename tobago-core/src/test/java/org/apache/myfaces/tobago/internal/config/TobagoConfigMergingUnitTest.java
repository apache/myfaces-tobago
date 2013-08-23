/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.config;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TobagoConfigMergingUnitTest {

  @Test
  public void testPreventFrameAttacksCascadingDefault()
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    final TobagoConfigImpl config = loadAndMerge(
        "tobago-config-0.xml",
        "tobago-config-1.xml");

    Assert.assertFalse(config.isPreventFrameAttacks());
  }

  @Test
  public void testPreventFrameAttacks()
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    final TobagoConfigImpl config = loadAndMerge("tobago-config-0.xml");

    Assert.assertFalse(config.isPreventFrameAttacks());
  }

  @Test
  public void testPreventFrameAttacksDefault()
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    final TobagoConfigImpl config = loadAndMerge("tobago-config-1.xml");

    Assert.assertTrue(config.isPreventFrameAttacks());
  }

  private TobagoConfigImpl loadAndMerge(String... names)
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    final List<TobagoConfigFragment> list = new ArrayList<TobagoConfigFragment>();

    for (String name : names) {
      final URL url = getClass().getClassLoader().getResource(name);
      final TobagoConfigParser parser = new TobagoConfigParser();
      list.add(parser.parse(url));
    }

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.sort();
    return sorter.merge();
  }
}

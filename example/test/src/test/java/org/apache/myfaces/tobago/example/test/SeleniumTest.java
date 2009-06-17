package org.apache.myfaces.tobago.example.test;

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

import com.thoughtworks.selenium.DefaultSelenium;
import org.junit.After;
import org.junit.Before;

public abstract class SeleniumTest {

  private static final String BASE_URL = "http://localhost:8080/";

  protected DefaultSelenium selenium;

  @Before
  public void setUp() throws Exception {
    selenium = createSeleniumClient(BASE_URL);
    selenium.start();
  }

  @After
  public void tearDown() throws Exception {
    selenium.stop();
  }

  protected DefaultSelenium createSeleniumClient(String url) throws Exception {
    return new DefaultSelenium("localhost", 4444, "*firefox", url);
  }
}

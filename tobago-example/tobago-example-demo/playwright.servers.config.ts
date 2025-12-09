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

import {devices, PlaywrightTestConfig} from "@playwright/test";
import defaultConfig from "./playwright.config";

const serversConfig: PlaywrightTestConfig = {
  ...defaultConfig,
  projects: [
    ...defaultConfig.projects,
    {
      name: "Open Liberty - Chromium",
      use: {baseURL: "http://localhost:8081", ...devices["Desktop Chrome"]}
    },
    {
      name: "Tomcat - Chromium",
      use: {baseURL: "http://localhost:8082", ...devices["Desktop Chrome"]}
    },
    {
      name: "TomEE - Chromium",
      use: {baseURL: "http://localhost:8083", ...devices["Desktop Chrome"]}
    }
  ]
};

export default serversConfig;

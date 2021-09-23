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

import {querySelectorFn} from "/script/tobago-test.js";

it("no layout manager", function () {
  const inFn = querySelectorFn("#page\\:mainForm\\:noLayoutIn\\:in\\:\\:field");
  const outFn = querySelectorFn("#page\\:mainForm\\:noLayoutOut\\:out");
  const notRenderedFn = querySelectorFn("#page\\:mainForm\\:noLayoutNotRendered\\:in\\:\\:field");

  expect(inFn()).not.toBeNull();
  expect(outFn()).not.toBeNull();
  expect(notRenderedFn()).toBeNull();
});

it("flowlayout", function () {
  const inFn = querySelectorFn("#page\\:mainForm\\:flowLayoutIn\\:in\\:\\:field");
  const outFn = querySelectorFn("#page\\:mainForm\\:flowLayoutOut\\:out");
  const notRenderedFn = querySelectorFn("#page\\:mainForm\\:flowLayoutNotRendered\\:in\\:\\:field");

  expect(inFn()).not.toBeNull();
  expect(outFn()).not.toBeNull();
  expect(notRenderedFn()).toBeNull();
});

it("flexlayout", function () {
  const inFn = querySelectorFn("#page\\:mainForm\\:flexLayoutIn\\:in\\:\\:field");
  const outFn = querySelectorFn("#page\\:mainForm\\:flexLayoutOut\\:out");
  const notRenderedFn = querySelectorFn("#page\\:mainForm\\:flexLayoutNotRendered\\:in\\:\\:field");

  expect(inFn()).not.toBeNull();
  expect(outFn()).not.toBeNull();
  expect(notRenderedFn()).toBeNull();
});

it("segmentlayout", function () {
  const inFn = querySelectorFn("#page\\:mainForm\\:segLayoutIn\\:in\\:\\:field");
  const outFn = querySelectorFn("#page\\:mainForm\\:segLayoutOut\\:out");
  const notRenderedFn = querySelectorFn("#page\\:mainForm\\:segLayoutNotRendered\\:in\\:\\:field");

  expect(inFn()).not.toBeNull();
  expect(outFn()).not.toBeNull();
  expect(notRenderedFn()).toBeNull();
});

it("gridlayout", function () {
  const inFn = querySelectorFn("#page\\:mainForm\\:gridLayoutIn\\:in\\:\\:field");
  const outFn = querySelectorFn("#page\\:mainForm\\:gridLayoutOut\\:out");
  const notRenderedFn = querySelectorFn("#page\\:mainForm\\:gridLayoutNotRendered\\:in\\:\\:field");

  expect(inFn()).not.toBeNull();
  expect(outFn()).not.toBeNull();
  expect(notRenderedFn()).toBeNull();
});

it("splitlayout", function () {
  const inFn = querySelectorFn("#page\\:mainForm\\:splitLayoutIn\\:in\\:\\:field");
  const outFn = querySelectorFn("#page\\:mainForm\\:splitLayoutOut\\:out");
  const notRenderedFn = querySelectorFn("#page\\:mainForm\\:splitLayoutNotRendered\\:in\\:\\:field");

  expect(inFn()).not.toBeNull();
  expect(outFn()).not.toBeNull();
  expect(notRenderedFn()).toBeNull();
});

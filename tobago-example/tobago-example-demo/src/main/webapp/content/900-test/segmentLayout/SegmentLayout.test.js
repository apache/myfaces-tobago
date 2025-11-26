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

import {elementByIdFn, querySelectorAllFn} from "/script/tobago-test.js";

it("Check DOM of segment layout", function () {
  const segmentLayout = elementByIdFn("page:mainForm:segmentLayout");
  const segLayoutIn = elementByIdFn("page:mainForm:segLayoutIn:in");
  const segLayoutOut = elementByIdFn("page:mainForm:segLayoutOut:out");
  const segLayoutNotRendered = elementByIdFn("page:mainForm:segLayoutNotRendered:in");
  const style = elementByIdFn("page:mainForm:style");
  const segmentLayoutOutputs = querySelectorAllFn("tobago-segment-layout[id^='page:mainForm:segmentLayoutComposite'] tobago-out span");

  expect(segmentLayout().children.length).toBe(4);
  expect(segmentLayout().querySelectorAll("div").length).toBe(3);
  expect(segLayoutIn()).not.toBeNull();
  expect(segLayoutOut()).not.toBeNull();
  expect(segLayoutNotRendered()).toBeNull();
  expect(style()).not.toBeNull();
  expect(style().parentElement.tagName).toBe("TOBAGO-SEGMENT-LAYOUT");
  expect(segmentLayoutOutputs().length).toBe(2);
  expect(segmentLayoutOutputs()[0].textContent).toBe("composite-composite output value");
  expect(segmentLayoutOutputs()[1].textContent).toBe("composite out value");
});

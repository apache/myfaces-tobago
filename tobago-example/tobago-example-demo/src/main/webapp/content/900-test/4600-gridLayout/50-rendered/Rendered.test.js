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

import {elementByIdFn} from "/script/tobago-test.js";
import {JasmineUtils} from "/tobago/test/tobago-test-tool.js";

it("Position of components in grid layout", function () {
  const reference = elementByIdFn("page:mainForm:referenceIn");
  const referenceRect = reference().getBoundingClientRect();
  const in1 = elementByIdFn("page:mainForm:in1");
  const in2 = elementByIdFn("page:mainForm:in2");
  const in2Rect = in2().getBoundingClientRect();
  const in3 = elementByIdFn("page:mainForm:in3");
  const in3Rect = in3().getBoundingClientRect();
  const in4 = elementByIdFn("page:mainForm:in4");
  const in4Rect = in4().getBoundingClientRect();

  expect(in1()).toBeNull();
  expect(referenceRect.width).toBeGreaterThan(in2Rect.width);
  expect(referenceRect.width).toBeGreaterThan(in3Rect.width);
  expect(referenceRect.width).toBeGreaterThan(in4Rect.width);
  expect(in2Rect.left).toBe(referenceRect.left);
  expect(in3Rect.right).toBe(referenceRect.right);
  expect(in4Rect.left).toBe(referenceRect.left);

  JasmineUtils.checkGridCss(in2(), "1", "auto", "1", "auto");
  JasmineUtils.checkGridCss(in3(), "2", "auto", "1", "auto");
  JasmineUtils.checkGridCss(in4(), "1", "auto", "2", "auto");
});

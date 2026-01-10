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

import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";
import {elementByIdFn, querySelectorFn} from "/script/tobago-test.js";

it("height by style tag normal", function (done) {
  const box1Fn = elementByIdFn("page:mainForm:box1");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(box1Fn()).height).toBe("300px"));
  test.start();
});

it("height by style tag ajax", function (done) {
  const box2Fn = elementByIdFn("page:mainForm:box2");
  const ajaxFn = elementByIdFn("page:mainForm:ajax");
  const box2titleFn = querySelectorFn("#page\\:mainForm\\:box2 h3");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(box2Fn()).height).toBe("300px"));
  test.event("click", ajaxFn, () => box2titleFn().innerHTML.trim() === "Box ajax=true");
  test.do(() => expect(getComputedStyle(box2Fn()).height).toBe("300px"));
  test.start();
});

it("height by style tag fake", function (done) {
  const box3Fn = elementByIdFn("page:mainForm:box3");

  const test = new JasmineTestTool(done);
  test.do(() => expect(getComputedStyle(box3Fn()).height).not.toBe("300px"));
  test.start();
});

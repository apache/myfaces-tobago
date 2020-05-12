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
import {querySelectorFn} from "/script/tobago-test.js";

it("SplitLayout Example 1", function (done) {
  let expectedSplitters = getElements("#page\\:mainForm\\:example1") - 1;
  let horizontalSplitters = getHorizontalSplitters("#page\\:mainForm\\:example1");
  let verticalSplitters = getVerticalSplitters("#page\\:mainForm\\:example1");
  let doubleSplitters = hasDoubleSplitter("#page\\:mainForm\\:example1");

  const test = new JasmineTestTool(done);
  test.do(() => expect(horizontalSplitters === expectedSplitters).toBe(true));
  test.do(() => expect(verticalSplitters === 0).toBe(true));
  test.do(() => expect(doubleSplitters === false).toBe(true));
  test.start();
});

it("SplitLayout Example 2", function (done) {
  let expectedSplitters = getElements("#page\\:mainForm\\:example2") - 1;
  let horizontalSplitters = getHorizontalSplitters("#page\\:mainForm\\:example2");
  let verticalSplitters = getVerticalSplitters("#page\\:mainForm\\:example2");
  let doubleSplitters = hasDoubleSplitter("#page\\:mainForm\\:example2");

  const test = new JasmineTestTool(done);
  test.do(() => expect(horizontalSplitters === expectedSplitters).toBe(true));
  test.do(() => expect(verticalSplitters === 0).toBe(true));
  test.do(() => expect(doubleSplitters === false).toBe(true));
  test.start();
});

it("SplitLayout Example 3a", function (done) {
  let expectedSplitters = getElements("#page\\:mainForm\\:example3a") - 1;
  let horizontalSplitters = getHorizontalSplitters("#page\\:mainForm\\:example3a");
  let verticalSplitters = getVerticalSplitters("#page\\:mainForm\\:example3a");
  let doubleSplitters = hasDoubleSplitter("#page\\:mainForm\\:example3a");

  const test = new JasmineTestTool(done);
  test.do(() => expect(horizontalSplitters === expectedSplitters).toBe(true));
  test.do(() => expect(verticalSplitters === 0).toBe(true));
  test.do(() => expect(doubleSplitters === false).toBe(true));
  test.start();
});

it("SplitLayout Example 3b", function (done) {
  let expectedSplitters = getElements("#page\\:mainForm\\:example3b") - 1;
  let horizontalSplitters = getHorizontalSplitters("#page\\:mainForm\\:example3b");
  let verticalSplitters = getVerticalSplitters("#page\\:mainForm\\:example3b");
  let doubleSplitters = hasDoubleSplitter("#page\\:mainForm\\:example3b");

  const test = new JasmineTestTool(done);
  test.do(() => expect(horizontalSplitters === 0).toBe(true));
  test.do(() => expect(verticalSplitters === expectedSplitters).toBe(true));
  test.do(() => expect(doubleSplitters === false).toBe(true));
  test.start();
});

function getHorizontalSplitters(element) {
  return querySelectorFn(element)().querySelectorAll(":scope > .tobago-splitLayout-horizontal").length;
}

function getVerticalSplitters(element) {
  return querySelectorFn(element)().querySelectorAll(":scope > .tobago-splitLayout-vertical").length;
}

function hasDoubleSplitter(element) {
  let counter = 0;
  let elements = querySelectorFn(element)().children;

  for (let item of elements) {
    if (item.classList.contains("tobago-splitLayout-horizontal") || item.classList.contains("tobago-splitLayout-vertical")) {
      counter++;

      if (counter === 2) {
        return true;
      }
    } else {
      counter = 0;
    }
  }
  return false;
}

function getElements(element) {
  let counter = 0;
  let elements = querySelectorFn(element)().children;

  for (let item of elements) {
    if (getComputedStyle(item).display !== "none" && !item.classList.contains("tobago-splitLayout-horizontal") && !item.classList.contains("tobago-splitLayout-vertical")) {
      counter++;
    }
  }
  return counter;
}

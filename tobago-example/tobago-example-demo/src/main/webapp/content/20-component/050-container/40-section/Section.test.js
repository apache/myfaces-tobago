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

import {testFrameQuerySelectorFn} from "/script/tobago-test.js";

QUnit.test("Attribute 'level'", function (assert) {
  assert.expect(1);

  let sectionLevel5Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:sectionLevel5");
  let header5 = sectionLevel5Fn().querySelector("h5");

  assert.ok(header5 !== null);
});

describe("Section", function() {
  it("Attribute 'level'", function() {
    let sectionLevel5Fn = testFrameQuerySelectorFn("#page\\:mainForm\\:sectionLevel5");
    let header5 = sectionLevel5Fn().querySelector("h5");

    expect(header5).not.toBe(null);
  });
});

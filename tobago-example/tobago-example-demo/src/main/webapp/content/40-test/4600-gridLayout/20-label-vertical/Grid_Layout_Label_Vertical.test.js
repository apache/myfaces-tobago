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

import {TobagoTestTool} from "/tobago/test/tobago-test-tool.js";
import {testFrameQuerySelectorFn} from "/script/tobago-test.js";

QUnit.test("test CSS of the fields and labels of 'first1'", function (assert) {
  assert.expect(8);

  let fieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:first1");
  let labelFn = testFrameQuerySelectorFn("#page\\:mainForm\\:first1\\:\\:label");

  TobagoTestTool.checkGridCss(assert, fieldFn(), "1", "auto", "2", "auto");
  TobagoTestTool.checkGridCss(assert, labelFn(), "1", "auto", "1", "auto");
});

QUnit.test("test CSS of the fields and labels of 'last1'", function (assert) {
  assert.expect(8);

  let fieldFn = testFrameQuerySelectorFn("#page\\:mainForm\\:last1");
  let labelFn = testFrameQuerySelectorFn("#page\\:mainForm\\:last1\\:\\:label");

  TobagoTestTool.checkGridCss(assert, fieldFn(), "2", "auto", "1", "auto");
  TobagoTestTool.checkGridCss(assert, labelFn(), "2", "auto", "2", "auto");
});

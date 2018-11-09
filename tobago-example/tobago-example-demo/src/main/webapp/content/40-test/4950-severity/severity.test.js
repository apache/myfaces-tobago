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

QUnit.test("Check severity CSS classes", function (assert) {
  var submitButtonFn = jQueryFrameFn("#page\\:mainForm\\:submit");
  var fatalInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:fatal\\:\\:field");
  var errorInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:error\\:\\:field");
  var warnInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:warn\\:\\:field");
  var infoInputFieldFn = jQueryFrameFn("#page\\:mainForm\\:info\\:\\:field");
  var fatalButtonFn = jQueryFrameFn("#page\\:mainForm\\:fatal .tobago-messages-button");
  var errorButtonFn = jQueryFrameFn("#page\\:mainForm\\:error .tobago-messages-button");
  var warnButtonFn = jQueryFrameFn("#page\\:mainForm\\:warn .tobago-messages-button");
  var infoButtonFn = jQueryFrameFn("#page\\:mainForm\\:info .tobago-messages-button");

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    submitButtonFn().click();
  });
  TTT.waitForResponse();
  TTT.asserts(8, function () {
    assert.ok(fatalInputFieldFn().hasClass("border-danger"));
    assert.ok(errorInputFieldFn().hasClass("border-danger"));
    assert.ok(warnInputFieldFn().hasClass("border-warning"));
    assert.ok(infoInputFieldFn().hasClass("border-info"));

    assert.ok(fatalButtonFn().hasClass("btn-danger"));
    assert.ok(errorButtonFn().hasClass("btn-danger"));
    assert.ok(warnButtonFn().hasClass("btn-warning"));
    assert.ok(infoButtonFn().hasClass("btn-info"));
  });
  TTT.startTest();
});

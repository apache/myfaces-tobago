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

QUnit.test("Add a river and reset.", function (assert) {
  var $name = jQueryFrameFn("#page\\:mainForm\\:add\\:inName\\:\\:field");
  var $length = jQueryFrameFn("#page\\:mainForm\\:add\\:inLength\\:\\:field");
  var $discharge = jQueryFrameFn("#page\\:mainForm\\:add\\:inDischarge\\:\\:field");
  var $add = jQueryFrameFn("#page\\:mainForm\\:add\\:buttonAdd");
  var $reset = jQueryFrameFn("#page\\:mainForm\\:reset\\:buttonReset");
  var $forEachBoxes = jQueryFrameFn("#page\\:mainForm\\:forEach .tobago-box");
  var $uiRepeatSections = jQueryFrameFn("#page\\:mainForm\\:uiRepeat .tobago-section");

  var TTT = new TobagoTestTools(assert);
  TTT.action(function () {
    $reset().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($forEachBoxes().length, 3);
    assert.equal($uiRepeatSections().length, 3);
  });
  TTT.action(function () {
    $name().val("Mississippi");
    $length().val("6275");
    $discharge().val("16200");
    $add().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($forEachBoxes().length, 4);
    assert.equal($uiRepeatSections().length, 4);
  });
  TTT.action(function () {
    $reset().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    assert.equal($forEachBoxes().length, 3);
    assert.equal($uiRepeatSections().length, 3);
  });
  TTT.startTest();
});

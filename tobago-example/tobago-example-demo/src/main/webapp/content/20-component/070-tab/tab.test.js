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

QUnit.test("Client: Select Tab 3", function (assert) {
  var $hiddenInput = jQueryFrameFn("#page\\:mainForm\\:tabGroupClient\\:\\:activeIndex");
  var $tab1 = jQueryFrameFn("#page\\:mainForm\\:tab1Client");
  var $tab3 = jQueryFrameFn("#page\\:mainForm\\:tab3Client");
  var $tab3link = jQueryFrameFn("#page\\:mainForm\\:tab3Client .nav-link");

  var TTT = new TobagoTestTools(assert);
  TTT.asserts(5, function () {
    assert.equal($hiddenInput().val(), 0);
    assert.ok($tab1().hasClass("tobago-tab-markup-selected"));
    assert.notOk($tab3().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab1().hasClass("tobago-tab-markup-one"));
    assert.ok($tab3().hasClass("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    $tab3link().click();
  });
  TTT.asserts(5, function () {
    assert.equal($hiddenInput().val(), 3);
    assert.notOk($tab1().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab3().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab1().hasClass("tobago-tab-markup-one"));
    assert.ok($tab3().hasClass("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

QUnit.test("Ajax: Select Tab 3", function (assert) {
  var $hiddenInput = jQueryFrameFn("#page\\:mainForm\\:tabGroupAjax\\:\\:activeIndex");
  var $tab1 = jQueryFrameFn("#page\\:mainForm\\:tab1Ajax");
  var $tab3 = jQueryFrameFn("#page\\:mainForm\\:tab3Ajax");
  var $tab3link = jQueryFrameFn("#page\\:mainForm\\:tab3Ajax .nav-link");

  var TTT = new TobagoTestTools(assert);
  TTT.asserts(5, function () {
    assert.equal($hiddenInput().val(), 0);
    assert.ok($tab1().hasClass("tobago-tab-markup-selected"));
    assert.notOk($tab3().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab1().hasClass("tobago-tab-markup-one"));
    assert.ok($tab3().hasClass("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    $tab3link().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal($hiddenInput().val(), 3);
    assert.notOk($tab1().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab3().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab1().hasClass("tobago-tab-markup-one"));
    assert.ok($tab3().hasClass("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

QUnit.test("FullReload: Select Tab 3", function (assert) {
  var $hiddenInput = jQueryFrameFn("#page\\:mainForm\\:tabGroupFullReload\\:\\:activeIndex");
  var $tab1 = jQueryFrameFn("#page\\:mainForm\\:tab1FullReload");
  var $tab3 = jQueryFrameFn("#page\\:mainForm\\:tab3FullReload");
  var $tab3link = jQueryFrameFn("#page\\:mainForm\\:tab3FullReload .nav-link");

  var TTT = new TobagoTestTools(assert);
  TTT.asserts(5, function () {
    assert.equal($hiddenInput().val(), 0);
    assert.ok($tab1().hasClass("tobago-tab-markup-selected"));
    assert.notOk($tab3().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab1().hasClass("tobago-tab-markup-one"));
    assert.ok($tab3().hasClass("tobago-tab-markup-three"));
  });
  TTT.action(function () {
    $tab3link().click();
  });
  TTT.waitForResponse();
  TTT.asserts(5, function () {
    assert.equal($hiddenInput().val(), 3);
    assert.notOk($tab1().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab3().hasClass("tobago-tab-markup-selected"));
    assert.ok($tab1().hasClass("tobago-tab-markup-one"));
    assert.ok($tab3().hasClass("tobago-tab-markup-three"));
  });
  TTT.startTest();
});

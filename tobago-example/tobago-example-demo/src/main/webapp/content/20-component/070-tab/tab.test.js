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
  assert.expect(6);

  var $hiddenInput = jQueryFrame("#page\\:mainForm\\:tabGroupClient\\:\\:activeIndex");
  var $tab1 = jQueryFrame("#page\\:mainForm\\:tab1Client");
  var $tab3 = jQueryFrame("#page\\:mainForm\\:tab3Client");
  var $tab3link = $tab3.find(".nav-link");

  assert.equal($hiddenInput.val(), 0);
  assert.ok($tab1.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tab3.hasClass("tobago-tab-markup-selected"));

  $tab3link.click();

  assert.equal($hiddenInput.val(), 3);
  assert.notOk($tab1.hasClass("tobago-tab-markup-selected"));
  assert.ok($tab3.hasClass("tobago-tab-markup-selected"));
});

QUnit.test("Ajax: Select Tab 3", function (assert) {
  assert.expect(6);
  var done = assert.async();
  var step = 1;

  var $hiddenInput = jQueryFrame("#page\\:mainForm\\:tabGroupAjax\\:\\:activeIndex");
  var $tab1 = jQueryFrame("#page\\:mainForm\\:tab1Ajax");
  var $tab3 = jQueryFrame("#page\\:mainForm\\:tab3Ajax");
  var $tab3link = $tab3.find(".nav-link");

  assert.equal($hiddenInput.val(), 0);
  assert.ok($tab1.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tab3.hasClass("tobago-tab-markup-selected"));

  $tab3link.click();

  waitForAjax(function () {
    $hiddenInput = jQueryFrame($hiddenInput.selector);
    return step === 1 && $hiddenInput.val() === "3";
  }, function () {
    $hiddenInput = jQueryFrame($hiddenInput.selector);
    $tab1 = jQueryFrame($tab1.selector);
    $tab3 = jQueryFrame($tab3.selector);

    assert.equal($hiddenInput.val(), 3);
    assert.notOk($tab1.hasClass("tobago-tab-markup-selected"));
    assert.ok($tab3.hasClass("tobago-tab-markup-selected"));

    step++;
    done();
  });
});

QUnit.test("FullReload: Select Tab 3", function (assert) {
  assert.expect(6);
  var done = assert.async();
  var step = 1;

  var $hiddenInput = jQueryFrame("#page\\:mainForm\\:tabGroupFullReload\\:\\:activeIndex");
  var $tab1 = jQueryFrame("#page\\:mainForm\\:tab1FullReload");
  var $tab3 = jQueryFrame("#page\\:mainForm\\:tab3FullReload");
  var $tab3link = $tab3.find(".nav-link");

  assert.equal($hiddenInput.val(), 0);
  assert.ok($tab1.hasClass("tobago-tab-markup-selected"));
  assert.notOk($tab3.hasClass("tobago-tab-markup-selected"));

  $tab3link.click();

  jQuery("#page\\:testframe").load(function () {
    if (step === 1) {
      $hiddenInput = jQueryFrame($hiddenInput.selector);
      $tab1 = jQueryFrame($tab1.selector);
      $tab3 = jQueryFrame($tab3.selector);

      assert.equal($hiddenInput.val(), 3);
      assert.notOk($tab1.hasClass("tobago-tab-markup-selected"));
      assert.ok($tab3.hasClass("tobago-tab-markup-selected"));

      step++;
      done();
    }
  });
});

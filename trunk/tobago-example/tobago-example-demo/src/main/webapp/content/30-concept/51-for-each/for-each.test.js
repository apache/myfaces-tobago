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
  assert.expect(6);
  var done = assert.async(3);
  var step = 1;

  var $name = jQueryFrame("#page\\:mainForm\\:add\\:inName\\:\\:field");
  var $length = jQueryFrame("#page\\:mainForm\\:add\\:inLength\\:\\:field");
  var $discharge = jQueryFrame("#page\\:mainForm\\:add\\:inDischarge\\:\\:field");
  var $add = jQueryFrame("#page\\:mainForm\\:add\\:button");
  var $reset = jQueryFrame("#page\\:mainForm\\:reset\\:button");
  var $forEachBoxes = jQueryFrame("#page\\:mainForm\\:forEach .tobago-box");
  var $uiRepeatSections = jQueryFrame("#page\\:mainForm\\:uiRepeat .tobago-section");

  $reset.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $name = jQueryFrame($name.selector);
      $length = jQueryFrame($length.selector);
      $discharge = jQueryFrame($discharge.selector);
      $add = jQueryFrame($add.selector);
      $forEachBoxes = jQueryFrame($forEachBoxes.selector);
      $uiRepeatSections = jQueryFrame($uiRepeatSections.selector);

      assert.equal($forEachBoxes.length, 3);
      assert.equal($uiRepeatSections.length, 3);

      $name.val("Mississippi");
      $length.val("6275");
      $discharge.val("16200");
      $add.click();

      done();
    } else if (step == 2) {
      $reset = jQueryFrame($reset.selector);
      $forEachBoxes = jQueryFrame($forEachBoxes.selector);
      $uiRepeatSections = jQueryFrame($uiRepeatSections.selector);

      assert.equal($forEachBoxes.length, 4);
      assert.equal($uiRepeatSections.length, 4);

      $reset.click();

      done();
    } else if (step == 3) {
      $forEachBoxes = jQueryFrame($forEachBoxes.selector);
      $uiRepeatSections = jQueryFrame($uiRepeatSections.selector);

      assert.equal($forEachBoxes.length, 3);
      assert.equal($uiRepeatSections.length, 3);

      done();
    }
    step++;
  });
});

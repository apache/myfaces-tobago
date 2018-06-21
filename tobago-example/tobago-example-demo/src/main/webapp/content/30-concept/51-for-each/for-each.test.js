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

  var n = "#page\\:mainForm\\:add\\:inName\\:\\:field";
  var $name = jQueryFrame(n);
  var l = "#page\\:mainForm\\:add\\:inLength\\:\\:field";
  var $length = jQueryFrame(l);
  var d = "#page\\:mainForm\\:add\\:inDischarge\\:\\:field";
  var $discharge = jQueryFrame(d);
  var a = "#page\\:mainForm\\:add\\:button";
  var $add = jQueryFrame(a);
  var r = "#page\\:mainForm\\:reset\\:button";
  var $reset = jQueryFrame(r);
  var feb = "#page\\:mainForm\\:forEach .tobago-box";
  var $forEachBoxes = jQueryFrame(feb);
  var urs = "#page\\:mainForm\\:uiRepeat .tobago-section";
  var $uiRepeatSections = jQueryFrame(urs);

  $reset.click();

  jQuery("#page\\:testframe").on("load", function () {
    if (step === 1) {
      $name = jQueryFrame(n);
      $length = jQueryFrame(l);
      $discharge = jQueryFrame(d);
      $add = jQueryFrame(a);
      $forEachBoxes = jQueryFrame(feb);
      $uiRepeatSections = jQueryFrame(urs);

      assert.equal($forEachBoxes.length, 3);
      assert.equal($uiRepeatSections.length, 3);

      $name.val("Mississippi");
      $length.val("6275");
      $discharge.val("16200");
      $add.click();

      done();
    } else if (step === 2) {
      $reset = jQueryFrame(r);
      $forEachBoxes = jQueryFrame(feb);
      $uiRepeatSections = jQueryFrame(urs);

      assert.equal($forEachBoxes.length, 4);
      assert.equal($uiRepeatSections.length, 4);

      $reset.click();

      done();
    } else if (step === 3) {
      $forEachBoxes = jQueryFrame(feb);
      $uiRepeatSections = jQueryFrame(urs);

      assert.equal($forEachBoxes.length, 3);
      assert.equal($uiRepeatSections.length, 3);

      done();
    }
    step++;
  });
});

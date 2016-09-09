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

QUnit.test("Standard Action Button", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  var $command = jQueryFrame("#page\\:mainForm\\:standardButtonAction");
  var $destinationSection = jQueryFrame("#page\\:actionSection");
  testStandardCommands($command, $destinationSection, assert, done);
});

QUnit.test("Standard Link Button", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  var $command = jQueryFrame("#page\\:mainForm\\:standardButtonLink");
  var $destinationSection = jQueryFrame("#page\\:linkSection");
  testStandardCommands($command, $destinationSection, assert, done);
});

/*QUnit.test("Standard Resource Button", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  var $command = jQueryFrame("#page\\:mainForm\\:standardButtonResource");
  var $destinationSection = jQueryFrame("#page\\:resourceSection");
  testStandardCommands($command, $destinationSection, assert, done);
});*/

QUnit.test("Standard Action Link", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  var $command = jQueryFrame("#page\\:mainForm\\:standardLinkAction");
  var $destinationSection = jQueryFrame("#page\\:actionSection");
  testStandardCommands($command, $destinationSection, assert, done);
});

QUnit.test("Standard Link Link", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  var $command = jQueryFrame("#page\\:mainForm\\:standardLinkLink");
  var $destinationSection = jQueryFrame("#page\\:linkSection");
  testStandardCommands($command, $destinationSection, assert, done);
});

/*QUnit.test("Standard Resource Link", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  var $command = jQueryFrame("#page\\:mainForm\\:standardLinkResource");
  var $destinationSection = jQueryFrame("#page\\:resourceSection");
  testStandardCommands($command, $destinationSection, assert, done);
});*/

function testStandardCommands($command, $destinationSection, assert, done) {
  var step = 1;
  $command[0].click();

  jQuery("#page\\:testframe").load(function() {
    if (step == 1) {
      $destinationSection = jQueryFrame($destinationSection.selector);
      assert.equal($destinationSection.length, 1);

      var $back = jQueryFrame("#page\\:back");
      $back[0].click();
    } else if (step == 2) {
      $command = jQueryFrame($command.selector);
      assert.equal($command.length, 1);
    }
    step++;
    done();
  });
}

// TODO: Test Confirmation: Mock Alerts with sinon


function jQueryTargetFrame(expression) {
  return document.getElementById("page:testframe").contentWindow.jQuery(expression);
}

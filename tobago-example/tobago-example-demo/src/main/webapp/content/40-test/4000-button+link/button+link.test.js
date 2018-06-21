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

  testStandardCommands("#page\\:mainForm\\:standardButtonAction", "#page\\:actionSection", assert, done);
});

QUnit.test("Standard Link Button", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  testStandardCommands("#page\\:mainForm\\:standardButtonLink", "#page\\:linkSection", assert, done);
});

QUnit.test("Standard Action Link", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  testStandardCommands("#page\\:mainForm\\:standardLinkAction", "#page\\:actionSection", assert, done);
});

QUnit.test("Standard Link Link", function(assert) {
  assert.expect(2);
  var done = assert.async(2);

  testStandardCommands("#page\\:mainForm\\:standardLinkLink", "#page\\:linkSection", assert, done);
});

function testStandardCommands(commandSelector, destinationSectionSelector, assert, done) {
  var step = 1;
  var $command = jQueryFrame(commandSelector);
  var $destinationSection = jQueryFrame(destinationSectionSelector);
  $command[0].click();

  jQuery("#page\\:testframe").on("load", function() {
    if (step === 1) {
      $destinationSection = jQueryFrame(destinationSectionSelector);
      assert.equal($destinationSection.length, 1);

      var $back = jQueryFrame("#page\\:back");
      $back[0].click();
    } else if (step === 2) {
      $command = jQueryFrame(commandSelector);
      assert.equal($command.length, 1);
    }
    step++;
    done();
  });
}

QUnit.test("Target Action Button", function(assert) {
  assert.expect(1);
  var done = assert.async();

  var $command = jQueryFrame("#page\\:mainForm\\:targetButtonAction");
  var $destinationSection = jQueryTargetFrame("#page\\:actionSection");
  testTargetCommands($command, $destinationSection, assert, done);
});

QUnit.test("Target Link Button", function(assert) {
  assert.expect(1);
  var done = assert.async();

  var $command = jQueryFrame("#page\\:mainForm\\:targetButtonLink");
  var $destinationSection = jQueryTargetFrame("#page\\:linkSection");
  testTargetCommands($command, $destinationSection, assert, done);
});

QUnit.test("Target Action Link", function(assert) {
  assert.expect(1);
  var done = assert.async();

  testTargetCommands("#page\\:mainForm\\:targetLinkAction", "#page\\:actionSection", assert, done);
});

QUnit.test("Target Link Link", function(assert) {
  assert.expect(1);
  var done = assert.async();

  testTargetCommands("#page\\:mainForm\\:targetLinkLink", "#page\\:linkSection", assert, done);
});

function testTargetCommands(commandSelector, destinationSectionSelector, assert, done) {
  var $command = jQueryFrame(commandSelector);
  var $destinationSection = jQueryFrame(destinationSectionSelector);
  $command[0].click();

  /*
   * phantomJs don't recognize jQueryFrame("#page\\:mainForm\\:targetFrame").load(),
   * so the waitForAjax() method is used instead.
   */
  waitForAjax(function() {
    $destinationSection = jQueryTargetFrame(destinationSectionSelector);
    return $destinationSection.length === 1;
  }, function() {
    $destinationSection = jQueryTargetFrame(destinationSectionSelector);
    assert.equal($destinationSection.length, 1);
    done();
  });
}

function jQueryTargetFrame(expression) {
  return jQueryFrame("#page\\:mainForm\\:targetFrame").contents().find(expression);
}

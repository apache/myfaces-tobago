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

QUnit.test("span(https://example.com/) - inputfield", function(assert) {
  var $mainForm = jQueryFrame("#page\\:mainForm");
  var $inputfield = jQueryFrame("#page\\:mainForm\\:itextbefore");
  var $span = $inputfield.find("span");
  var $input = $inputfield.find("input");
  var combinedWidth = $span.outerWidth(true) + $input.outerWidth(true);

  assert.equal($inputfield.outerWidth(true), $mainForm.width());
  assert.ok(combinedWidth <= $mainForm.width(),
      "Width of inputfield (" + $span.outerWidth(true) + "px) + span (" + $input.outerWidth(true) + "px) (="
      + combinedWidth + "px) must be smaller/equal than the width of the content box (" + $mainForm.width() + "px).");
  assert.equal($span.outerHeight(), $input.outerHeight());
});

QUnit.test("label(Price) - inputfield - span(.00 €)", function(assert) {
  var $mainForm = jQueryFrame("#page\\:mainForm");
  var $inputfield = jQueryFrame("#page\\:mainForm\\:ipriceafter");
  var $label = $inputfield.find("label");
  var $input = $inputfield.find("input");
  var $span = $inputfield.find("span");
  var combinedWidth = $label.outerWidth(true) + $span.outerWidth(true) + $input.outerWidth(true);

  assert.equal($inputfield.outerWidth(true), $mainForm.width());
  assert.ok(combinedWidth <= $mainForm.width(),
      "Width of label (" + $label.outerWidth(true) + "px) + input (" + $input.outerWidth(true) + "px) + span ("
      + $span.outerWidth(true) + "px) (=" + combinedWidth
      + "px) must be smaller/equal than the width of the content box (" + $mainForm.width() + "px).");
  assert.equal($span.outerHeight(), $input.outerHeight());
});

QUnit.test("span(User Two) - inputfield - button(Send)", function(assert) {
  var $mainForm = jQueryFrame("#page\\:mainForm");
  var $inputfield = jQueryFrame("#page\\:mainForm\\:inewmessage");
  var $span = $inputfield.find("span");
  var $input = $inputfield.find("input");
  var $button = $inputfield.find("button");
  var combinedWidth = $span.outerWidth(true) + $input.outerWidth(true) + $button.outerWidth(true);

  assert.equal($inputfield.outerWidth(true), $mainForm.width());
  assert.ok(combinedWidth <= $mainForm.width(),
      "Width of span (" + $span.outerWidth(true) + "px) + input (" + $input.outerWidth(true) + "px) + button ("
      + $button.outerWidth(true) + "px) (=" + combinedWidth
      + "px) must be smaller/equal than the width of the content box (" + $mainForm.width() + "px).");
  assert.equal($span.outerHeight(), $input.outerHeight());
  assert.equal($span.outerHeight(), $button.outerHeight());
});

QUnit.test("dropdown-button - inputfield", function(assert) {
  var $mainForm = jQueryFrame("#page\\:mainForm");
  var $inputfield = jQueryFrame("#page\\:mainForm\\:isendtoc");
  var $button = $inputfield.find("button");
  var $input = $inputfield.find("input");
  var combinedWidth = $button.outerWidth(true) + $input.outerWidth(true);

  assert.equal($inputfield.outerWidth(true), $mainForm.width());
  assert.ok(combinedWidth <= $mainForm.width(),
      "Width of button (" + $button.outerWidth(true) + "px) + input (" + $input.outerWidth(true) + "px) (="
      + combinedWidth + "px) must be smaller/equal than the width of the content box (" + $mainForm.width() + "px).");
  assert.equal($button.outerHeight(), $input.outerHeight());
});

QUnit.test("inputfield - span(Send To:) - dropdown-button", function(assert) {
  var $mainForm = jQueryFrame("#page\\:mainForm");
  var $inputfield = jQueryFrame("#page\\:mainForm\\:isendtorb");
  var $input = $inputfield.find("input");
  var $span = $inputfield.find(".input-group-addon");
  var $button = $inputfield.find("button");
  var combinedWidth = $input.outerWidth(true) + $span.outerWidth(true) + $button.outerWidth(true);

  assert.equal($inputfield.outerWidth(true), $mainForm.width());
  assert.ok(combinedWidth <= $mainForm.width(),
      "Width of input (" + $input.outerWidth(true) + "px) + span (" + $span.outerWidth(true) + "px) + button ("
      + $button.outerWidth(true) + "px) (=" + combinedWidth
      + "px) must be smaller/equal than the width of the content box (" + $mainForm.width() + "px).");
  assert.equal($input.outerHeight(), $span.outerHeight());
  assert.equal($input.outerHeight(), $button.outerHeight());
});

QUnit.test("inputfield - dropdownbox", function(assert) {
  var $mainForm = jQueryFrame("#page\\:mainForm");
  var $inputfield = jQueryFrame("#page\\:mainForm\\:value");
  var $input = $inputfield.find("input");
  var $dropdown = $inputfield.find(".input-group-addon");
  var combinedWidth = $input.outerWidth(true) + $dropdown.outerWidth(true);

  assert.equal($inputfield.outerWidth(true), $mainForm.width());
  assert.ok(combinedWidth <= $mainForm.width(),
      "Width of input (" + $input.outerWidth(true) + "px) + dropdown (" + $dropdown.outerWidth(true) + "px) (="
      + combinedWidth + "px) must be smaller/equal than the width of the content box (" + $mainForm.width() + "px).");
  assert.equal($input.outerHeight(), $dropdown.outerHeight());
});

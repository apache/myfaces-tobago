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

QUnit.test("Open 'Client Popup' and press 'Cancel'.", function(assert) {
  assert.expect(3);
  var step = 1;

  var $popup = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup input");
  var $openButton = jQueryFrame("#page\\:mainForm\\:form2\\:open");
  var $cancelButton = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  assert.equal($popup.attr("value"), "true");
  $openButton.click();
  assert.equal($popup.attr("value"), "false");
  $cancelButton.click();
  assert.equal($popup.attr("value"), "true");
});

QUnit.test("Open 'Client Popup', press 'Submit' while field is empty. Press 'Cancel'.", function(assert) {
  assert.expect(5);
  var done = assert.async(1);

  var $popup = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup input");
  var $openButton = jQueryFrame("#page\\:mainForm\\:form2\\:open");
  var $output = jQueryFrame("#page\\:mainForm\\:form2\\:output span");
  var outputValue = $output.text();
  var $messages = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  var $inputField = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  var $submitButton = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:submit2");
  var $cancelButton = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  assert.equal($popup.attr("value"), "true");
  $openButton.click();
  assert.equal($popup.attr("value"), "false");
  $inputField.val("");
  $submitButton.click();

  waitForAjax(function() {
    $messages = jQueryFrame($messages.selector);
    return $messages.length == 1;
  }, function() {
    $output = jQueryFrame($output.selector);
    $messages = jQueryFrame($messages.selector);
    $cancelButton = jQueryFrame($cancelButton.selector);

    assert.equal($messages.length, 1);
    $cancelButton.click();
    assert.equal($popup.attr("value"), "true");
    assert.equal($output.text(), outputValue);

    done();
  });
});

QUnit.test("Open 'Client Popup', press 'Submit' while field has content. Press 'Cancel'.", function(assert) {
  assert.expect(5);
  var done = assert.async(1);

  var $popup = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup input");
  var $openButton = jQueryFrame("#page\\:mainForm\\:form2\\:open");
  var $output = jQueryFrame("#page\\:mainForm\\:form2\\:out span");
  var $messages = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:messages div");
  var $inputField = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  var $submitButton = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:submit2");
  var $cancelButton = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:cancel2");

  assert.equal($popup.attr("value"), "true");
  $openButton.click();
  assert.equal($popup.attr("value"), "false");
  $inputField.val("test client popup - submit button");
  $submitButton.click();

  waitForAjax(function() {
    $messages = jQueryFrame($messages.selector);
    return $messages.length == 0;
  }, function() {
    $output = jQueryFrame($output.selector);
    $messages = jQueryFrame($messages.selector);
    $cancelButton = jQueryFrame($cancelButton.selector);

    assert.equal($messages.length, 0);
    $cancelButton.click();
    assert.equal($popup.attr("value"), "true");
    assert.equal($output.text(), "test client popup - submit button");

    done();
  });
});

QUnit.test("Open 'Client Popup', press 'Submit & Close' while field is empty.", function(assert) {
  assert.expect(4);
  var done = assert.async(1);

  var $popup = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup input");
  var $openButton = jQueryFrame("#page\\:mainForm\\:form2\\:open");
  var $output = jQueryFrame("#page\\:mainForm\\:form2\\:output span");
  var outputValue = $output.text();
  var $inputField = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  var $submitCloseButton = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:submitClose2");

  assert.equal($popup.attr("value"), "true");
  $openButton.click();
  assert.equal($popup.attr("value"), "false");
  $inputField.val("");
  $submitCloseButton.click();

  jQuery("#page\\:testframe").load(function() {
    $popup = jQueryFrame($popup.selector);
    $output = jQueryFrame($output.selector);

    assert.equal($popup.attr("value"), "true");
    assert.equal($output.text(), outputValue);

    done();
  });
});

QUnit.test("Open 'Client Popup', press 'Submit & Close' while field has content.", function(assert) {
  assert.expect(4);
  var done = assert.async(1);

  var $popup = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup input");
  var $openButton = jQueryFrame("#page\\:mainForm\\:form2\\:open");
  var $output = jQueryFrame("#page\\:mainForm\\:form2\\:out span");
  var $inputField = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:in2\\:\\:field");
  var $submitCloseButton = jQueryFrame("#page\\:mainForm\\:form2\\:clientPopup\\:submitClose2");

  assert.equal($popup.attr("value"), "true");
  $openButton.click();
  assert.equal($popup.attr("value"), "false");
  $inputField.val("test client popup - submit and close button");
  $submitCloseButton.click();

  jQuery("#page\\:testframe").load(function() {
    $popup = jQueryFrame($popup.selector);
    $output = jQueryFrame($output.selector);

    assert.equal($popup.attr("value"), "true");
    assert.equal($output.text(), "test client popup - submit and close button");

    done();
  });
});

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

QUnit.test("tc:in tc:event - change", function(assert) {
  assert.expect(4);
  var done = assert.async();

  var oldActionCount = getActionCount();
  var oldActionListenerCount = getActionListenerCount();
  var oldAjaxListenerCount = getAjaxListenerCount();
  var oldValueChangeListenerCount = getValueChangeListenerCount();

  var $inputField = jQueryFrame("#page\\:mainForm\\:inEventChange\\:\\:field");
  var newValue = "hello";
  if (newValue == $inputField.val()) {
    newValue = "hi there";
  }
  $inputField.val(newValue).trigger("change");

  jQuery("#page\\:testframe").load(function() {
    var newActionCount = getActionCount();
    var newActionListenerCount = getActionListenerCount();
    var newAjaxListenerCount = getAjaxListenerCount();
    var newValueChangeListenerCount = getValueChangeListenerCount();

    assert.ok(newActionCount > oldActionCount);
    assert.ok(newActionListenerCount > oldActionListenerCount);
    assert.ok(newAjaxListenerCount == oldAjaxListenerCount);
    assert.ok(newValueChangeListenerCount > oldValueChangeListenerCount);

    done();
  });
});

QUnit.test("tc:in tc:event - click", function(assert) {
  assert.expect(4);
  var done = assert.async();

  var oldActionCount = getActionCount();
  var oldActionListenerCount = getActionListenerCount();
  var oldAjaxListenerCount = getAjaxListenerCount();
  var oldValueChangeListenerCount = getValueChangeListenerCount();

  var $inputField = jQueryFrame("#page\\:mainForm\\:inEventClick\\:\\:field");
  $inputField.trigger("click");

  jQuery("#page\\:testframe").load(function() {
    var newActionCount = getActionCount();
    var newActionListenerCount = getActionListenerCount();
    var newAjaxListenerCount = getAjaxListenerCount();
    var newValueChangeListenerCount = getValueChangeListenerCount();

    assert.ok(newActionCount > oldActionCount);
    assert.ok(newActionListenerCount > oldActionListenerCount);
    assert.ok(newAjaxListenerCount == oldAjaxListenerCount);
    assert.ok(newValueChangeListenerCount == oldValueChangeListenerCount);

    done();
  });
});

QUnit.test("tc:in tc:event - change + click", function(assert) {
  assert.expect(8);
  var done = assert.async(2);
  var step = 1;

  var oldActionCount = getActionCount();
  var oldActionListenerCount = getActionListenerCount();
  var oldAjaxListenerCount = getAjaxListenerCount();
  var oldValueChangeListenerCount = getValueChangeListenerCount();
  var newActionCount;
  var newActionListenerCount;
  var newAjaxListenerCount;
  var newValueChangeListenerCount;

  var $inputField = jQueryFrame("#page\\:mainForm\\:inEventChangeClick\\:\\:field");
  $inputField.trigger("click");

  jQuery("#page\\:testframe").load(function() {
    if (step == 1) {
      newActionCount = getActionCount();
      newActionListenerCount = getActionListenerCount();
      newAjaxListenerCount = getAjaxListenerCount();
      newValueChangeListenerCount = getValueChangeListenerCount();

      assert.ok(newActionCount > oldActionCount);
      assert.ok(newActionListenerCount > oldActionListenerCount);
      assert.ok(newAjaxListenerCount == oldAjaxListenerCount);
      assert.ok(newValueChangeListenerCount == oldValueChangeListenerCount);

      $inputField = jQueryFrame($inputField.selector);
      var newValue = "hello";
      if (newValue == $inputField.val()) {
        newValue = "hi there";
      }
      $inputField.val(newValue).trigger("change");
    } else if (step == 2) {
      newActionCount = getActionCount();
      newActionListenerCount = getActionListenerCount();
      newAjaxListenerCount = getAjaxListenerCount();
      newValueChangeListenerCount = getValueChangeListenerCount();

      assert.ok(newActionCount > oldActionCount);
      assert.ok(newActionListenerCount > oldActionListenerCount);
      assert.ok(newAjaxListenerCount == oldAjaxListenerCount);
      assert.ok(newValueChangeListenerCount > oldValueChangeListenerCount);

      oldActionCount = getActionCount();
      oldActionListenerCount = getActionListenerCount();
      oldAjaxListenerCount = getAjaxListenerCount();
      oldValueChangeListenerCount = getValueChangeListenerCount();
    }

    step++;
    done();
  });
});

QUnit.test("tc:in tc:ajax - change", function(assert) {
  assert.expect(4);
  var done = assert.async();

  var oldActionCount = getActionCount();
  var oldActionListenerCount = getActionListenerCount();
  var oldAjaxListenerCount = getAjaxListenerCount();
  var oldValueChangeListenerCount = getValueChangeListenerCount();
  var newActionCount;
  var newActionListenerCount;
  var newAjaxListenerCount;
  var newValueChangeListenerCount;

  var $inputField = jQueryFrame("#page\\:mainForm\\:inAjaxChange\\:\\:field");
  var newValue = "hello";
  if (newValue == $inputField.val()) {
    newValue = "hi there";
  }
  $inputField.val(newValue).trigger("change");

  waitForAjax(function() {
    newActionCount = getActionCount();
    newActionListenerCount = getActionListenerCount();
    newAjaxListenerCount = getAjaxListenerCount();
    newValueChangeListenerCount = getValueChangeListenerCount();
    return (newActionCount == oldActionCount)
        && (newActionListenerCount == oldActionListenerCount)
        && (newAjaxListenerCount > oldAjaxListenerCount)
        && (newValueChangeListenerCount > oldValueChangeListenerCount);
  }, function() {
    newActionCount = getActionCount();
    newActionListenerCount = getActionListenerCount();
    newAjaxListenerCount = getAjaxListenerCount();
    newValueChangeListenerCount = getValueChangeListenerCount();

    assert.ok(newActionCount == oldActionCount);
    assert.ok(newActionListenerCount == oldActionListenerCount);
    assert.ok(newAjaxListenerCount > oldAjaxListenerCount);
    assert.ok(newValueChangeListenerCount > oldValueChangeListenerCount);

    done();
  });
});

/*QUnit.test("tc:in tc:ajax - click", function(assert) {
  assert.expect(4);
  var done = assert.async();

  var oldActionCount = getActionCount();
  var oldActionListenerCount = getActionListenerCount();
  var oldAjaxListenerCount = getAjaxListenerCount();
  var oldValueChangeListenerCount = getValueChangeListenerCount();
  var newActionCount;
  var newActionListenerCount;
  var newAjaxListenerCount;
  var newValueChangeListenerCount;

  var $inputField = jQueryFrame("#page\\:mainForm\\:inAjaxClick\\:\\:field");
  $inputField.trigger("click");

  waitForAjax(function() {
    newActionCount = getActionCount();
    newActionListenerCount = getActionListenerCount();
    newAjaxListenerCount = getAjaxListenerCount();
    newValueChangeListenerCount = getValueChangeListenerCount();
    return (newActionCount == oldActionCount)
        && (newActionListenerCount == oldActionListenerCount)
        && (newAjaxListenerCount > oldAjaxListenerCount)
        && (newValueChangeListenerCount == oldValueChangeListenerCount);
  }, function() {
    newActionCount = getActionCount();
    newActionListenerCount = getActionListenerCount();
    newAjaxListenerCount = getAjaxListenerCount();
    newValueChangeListenerCount = getValueChangeListenerCount();

    assert.ok(newActionCount == oldActionCount);
    assert.ok(newActionListenerCount == oldActionListenerCount);
    assert.ok(newAjaxListenerCount > oldAjaxListenerCount);
    assert.ok(newValueChangeListenerCount == oldValueChangeListenerCount);

    done();
  });
});*/

/*QUnit.test("tc:in tc:ajax - change + click", function(assert) {
  assert.expect(8);
  var done = assert.async();
  var step = 1;

  var oldActionCount = getActionCount();
  var oldActionListenerCount = getActionListenerCount();
  var oldAjaxListenerCount = getAjaxListenerCount();
  var oldValueChangeListenerCount = getValueChangeListenerCount();
  var newActionCount;
  var newActionListenerCount;
  var newAjaxListenerCount;
  var newValueChangeListenerCount;

  var $inputField = jQueryFrame("#page\\:mainForm\\:inAjaxChangeClick\\:\\:field");
  $inputField.trigger("click");

  waitForAjax(function() {
    newActionCount = getActionCount();
    newActionListenerCount = getActionListenerCount();
    newAjaxListenerCount = getAjaxListenerCount();
    newValueChangeListenerCount = getValueChangeListenerCount();
    return step == 1
        && (newActionCount == oldActionCount)
        && (newActionListenerCount == oldActionListenerCount)
        && (newAjaxListenerCount > oldAjaxListenerCount)
        && (newValueChangeListenerCount == oldValueChangeListenerCount);
  }, function() {
    newActionCount = getActionCount();
    newActionListenerCount = getActionListenerCount();
    newAjaxListenerCount = getAjaxListenerCount();
    newValueChangeListenerCount = getValueChangeListenerCount();

    assert.ok(newActionCount > oldActionCount);
    assert.ok(newActionListenerCount > oldActionListenerCount);
    assert.ok(newAjaxListenerCount == oldAjaxListenerCount);
    assert.ok(newValueChangeListenerCount > oldValueChangeListenerCount);

    $inputField = jQueryFrame($inputField.selector);
    var newValue = "hello";
    if (newValue == $inputField.val()) {
      newValue = "hi there";
    }
    $inputField.val(newValue).trigger("change");

    waitForAjax(function() {
      newActionCount = getActionCount();
      newActionListenerCount = getActionListenerCount();
      newAjaxListenerCount = getAjaxListenerCount();
      newValueChangeListenerCount = getValueChangeListenerCount();
      return step == 2
          && (newActionCount == oldActionCount)
          && (newActionListenerCount == oldActionListenerCount)
          && (newAjaxListenerCount > oldAjaxListenerCount)
          && (newValueChangeListenerCount > oldValueChangeListenerCount);
    }, function() {
      newActionCount = getActionCount();
      newActionListenerCount = getActionListenerCount();
      newAjaxListenerCount = getAjaxListenerCount();
      newValueChangeListenerCount = getValueChangeListenerCount();

      assert.ok(newActionCount == oldActionCount);
      assert.ok(newActionListenerCount == oldActionListenerCount);
      assert.ok(newAjaxListenerCount > oldAjaxListenerCount);
      assert.ok(newValueChangeListenerCount > oldValueChangeListenerCount);
    });

    step++;
    done();
  });
});*/

QUnit.test("tc:button tc:event - click", function(assert) {
  assert.expect(4);
  var done = assert.async();

  var oldActionCount = getActionCount();
  var oldActionListenerCount = getActionListenerCount();
  var oldAjaxListenerCount = getAjaxListenerCount();
  var oldValueChangeListenerCount = getValueChangeListenerCount();

  var button = jQueryFrame("#page\\:mainForm\\:buttonEventClick");
  button.click();

  jQuery("#page\\:testframe").load(function() {
    var newActionCount = getActionCount();
    var newActionListenerCount = getActionListenerCount();
    var newAjaxListenerCount = getAjaxListenerCount();
    var newValueChangeListenerCount = getValueChangeListenerCount();

    assert.ok(newActionCount > oldActionCount);
    assert.ok(newActionListenerCount > oldActionListenerCount);
    assert.ok(newAjaxListenerCount == oldAjaxListenerCount);
    assert.ok(newValueChangeListenerCount == oldValueChangeListenerCount);

    done();
  });
});

QUnit.test("tc:sheet tc:event - click", function(assert) {
  assert.expect(4);
  var done = assert.async();

  var oldActionCount = getActionCount();
  var oldActionListenerCount = getActionListenerCount();
  var oldAjaxListenerCount = getAjaxListenerCount();
  var oldValueChangeListenerCount = getValueChangeListenerCount();

  var button = jQueryFrame("#page\\:mainForm\\:buttonEventClick");
  button.click();

  jQuery("#page\\:testframe").load(function() {
    var newActionCount = getActionCount();
    var newActionListenerCount = getActionListenerCount();
    var newAjaxListenerCount = getAjaxListenerCount();
    var newValueChangeListenerCount = getValueChangeListenerCount();

    assert.ok(newActionCount > oldActionCount);
    assert.ok(newActionListenerCount > oldActionListenerCount);
    assert.ok(newAjaxListenerCount == oldAjaxListenerCount);
    assert.ok(newValueChangeListenerCount == oldValueChangeListenerCount);

    done();
  });
});

function getActionCount() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:outAction span").text());
}

function getActionListenerCount() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:outActionListener span").text());
}

function getAjaxListenerCount() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:outAjaxListener span").text());
}

function getValueChangeListenerCount() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:outValueChangeListener span").text());
}

function getTimestamp() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:outTimestamp span").text());
}

function getSelectedPlanet() {
  return jQueryFrame("#page\\:mainForm\\:outPlanet span").text();
}

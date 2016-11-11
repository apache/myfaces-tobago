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

QUnit.test("tc:button tc:event - blur", function(assert) {
  testButtonEvent(assert, "blur");
});

QUnit.test("tc:button tc:event - click", function(assert) {
  testButtonEvent(assert, "click");
});

QUnit.test("tc:button tc:event - dblclick", function(assert) {
  testButtonEvent(assert, "dblclick");
});

QUnit.test("tc:button tc:event - focus", function(assert) {
  testButtonEvent(assert, "focus");
});

QUnit.test("tc:button f:ajax - blur", function(assert) {
  testButtonAjax(assert, "blur");
});

QUnit.test("tc:button f:ajax - click", function(assert) {
  testButtonAjax(assert, "click");
});

QUnit.test("tc:button f:ajax - dblclick", function(assert) {
  testButtonAjax(assert, "dblclick");
});

QUnit.test("tc:button f:ajax - focus", function(assert) {
  testButtonAjax(assert, "focus");
});

QUnit.test("tc:in tc:event - blur", function(assert) {
  testInEvent(assert, "blur");
});

QUnit.test("tc:in tc:event - change", function(assert) {
  testInEvent(assert, "change");
});

QUnit.test("tc:in tc:event - click", function(assert) {
  testInEvent(assert, "click");
});

QUnit.test("tc:in tc:event - dblclick", function(assert) {
  testInEvent(assert, "dblclick");
});

QUnit.test("tc:in tc:event - focus", function(assert) {
  testInEvent(assert, "focus");
});

QUnit.test("tc:in f:ajax - blur", function(assert) {
  testInAjax(assert, "blur");
});

QUnit.test("tc:in f:ajax - change", function(assert) {
  testInAjax(assert, "change");
});
QUnit.test("tc:in f:ajax - click", function(assert) {
  testInAjax(assert, "click");
});
QUnit.test("tc:in f:ajax - dblclick", function(assert) {
  testInAjax(assert, "dblclick");
});
QUnit.test("tc:in f:ajax - focus", function(assert) {
  testInAjax(assert, "focus");
});

QUnit.test("tc:row tc:event - click", function(assert) {
  testRowEvent(assert, "click");
});

QUnit.test("tc:row tc:event - dblclick", function(assert) {
  testRowEvent(assert, "dblclick");
});

QUnit.test("tc:row f:ajax - click", function(assert) {
  testRowAjax(assert, "click");
});

QUnit.test("tc:row f:ajax - dblclick", function(assert) {
  testRowAjax(assert, "dblclick");
});

function testButtonEvent(assert, eventName) {
  testEvent(assert, "button", function() {
    return jQueryFrame("#page\\:mainForm\\:buttonevent" + eventName + "\\:\\:command");
  }, eventName, true, true, false, false, false);
}

function testButtonAjax(assert, eventName) {
  testAjax(assert, "button", function() {
    return jQueryFrame("#page\\:mainForm\\:buttonajax" + eventName);
  }, eventName, false, false, true, false, false);
}

function testInEvent(assert, eventName) {
  testEvent(assert, "in", function() {
    var $inputField = jQueryFrame("#page\\:mainForm\\:inevent" + eventName + "\\:\\:field");
    var newValue = "hello";
    if (newValue == $inputField.val()) {
      newValue = "hi there";
    }
    $inputField.val(newValue);
    return $inputField;
  }, eventName, true, true, false, true, false);
}

function testInAjax(assert, eventName) {
  testAjax(assert, "in", function() {
    var $inputField = jQueryFrame("#page\\:mainForm\\:inajax" + eventName + "\\:\\:field");
    var newValue = "hello";
    if (newValue == $inputField.val()) {
      newValue = "hi there";
    }
    $inputField.val(newValue);
    return $inputField;
  }, eventName, false, false, true, true, false);
}

function testRowEvent(assert, eventName) {
  var newSelectedPlanet = "Venus";
  testEvent(assert, "row", function() {
    var $row = jQueryFrame("#page\\:mainForm\\:sheetevent" + eventName + "\\:1\\:selectPlanet");
    if (getSelectedPlanet() == newSelectedPlanet) {
      $row = jQueryFrame("#page\\:mainForm\\:sheetevent" + eventName + "\\:4\\:selectPlanet");
      newSelectedPlanet = "Jupiter";
    }
    return $row;
  }, eventName, true, false, false, false, true);
}

function testRowAjax(assert, eventName) {
  var newSelectedPlanet = "Venus";
  testAjax(assert, "row", function() {
    var $row = jQueryFrame("#page\\:mainForm\\:sheetajax" + eventName + "\\:1\\:selectPlanet");
    if (getSelectedPlanet() == newSelectedPlanet) {
      $row = jQueryFrame("#page\\:mainForm\\:sheetajax" + eventName + "\\:4\\:selectPlanet");
      newSelectedPlanet = "Jupiter";
    }
    return $row;
  }, eventName, false, false, false, false, true);
}

function testEvent(assert, componentName, componentFunc, event,
                   incAction, incActionListener, incAjaxListener, incValueChangeListener, changePlanet) {
  assert.expect(6);
  var changeActiveComponent = jQueryFrame("#page\\:mainForm\\:compTestSection > div > h1 > span").text()
      != "<tc:" + componentName + ">";
  var done = assert.async(1 + changeActiveComponent);

  var oldActionCount;
  var oldActionListenerCount;
  var oldAjaxListenerCount;
  var oldValueChangeListenerCount;
  var oldTimestamp;
  var oldPlanet;

  if (changeActiveComponent) {
    activateComponent(componentName);
    var step = 1;

    jQuery("#page\\:testframe").load(function() {
      if (step == 1) {
        oldActionCount = getActionCount();
        oldActionListenerCount = getActionListenerCount();
        oldAjaxListenerCount = getAjaxListenerCount();
        oldValueChangeListenerCount = getValueChangeListenerCount();
        oldTimestamp = getTimestamp();
        oldPlanet = getSelectedPlanet();

        componentFunc().trigger(event);
      } else if (step == 2) {
        validateEvent(assert, oldActionCount, oldActionListenerCount, oldAjaxListenerCount,
            oldValueChangeListenerCount, oldTimestamp, oldPlanet, incAction, incActionListener, incAjaxListener,
            incValueChangeListener, changePlanet);
      }
      step++;
      done();
    });
  } else {
    oldActionCount = getActionCount();
    oldActionListenerCount = getActionListenerCount();
    oldAjaxListenerCount = getAjaxListenerCount();
    oldValueChangeListenerCount = getValueChangeListenerCount();
    oldTimestamp = getTimestamp();
    oldPlanet = getSelectedPlanet();

    componentFunc().trigger(event);

    jQuery("#page\\:testframe").load(function() {
      validateEvent(assert, oldActionCount, oldActionListenerCount, oldAjaxListenerCount, oldValueChangeListenerCount,
          oldTimestamp, oldPlanet, incAction, incActionListener, incAjaxListener, incValueChangeListener, changePlanet);

      done();
    });
  }
}

function validateEvent(assert, oldActionCount, oldActionListenerCount, oldAjaxListenerCount,
                       oldValueChangeListenerCount, oldTimestamp, oldPlanet, incAction, incActionListener,
                       incAjaxListener, incValueChangeListener, changePlanet) {
  var newActionCount = getActionCount();
  var newActionListenerCount = getActionListenerCount();
  var newAjaxListenerCount = getAjaxListenerCount();
  var newValueChangeListenerCount = getValueChangeListenerCount();
  var newTimestamp = getTimestamp();
  var newPlanet = getSelectedPlanet();

  assert.equal(newActionCount, oldActionCount + incAction, "action");
  assert.equal(newActionListenerCount, oldActionListenerCount + incActionListener, "actionListener");
  assert.equal(newAjaxListenerCount, oldAjaxListenerCount + incAjaxListener, "ajaxListener");
  assert.equal(newValueChangeListenerCount, oldValueChangeListenerCount + incValueChangeListener,
      "valueChangeListener");
  assert.ok(newTimestamp > oldTimestamp, "timestamp");
  if (changePlanet) {
    assert.notEqual(newPlanet, oldPlanet, "selectedPlanet");
  } else {
    assert.equal(newPlanet, oldPlanet, "selectedPlanet");
  }
}

function testAjax(assert, componentName, componentFunc, event,
                  incAction, incActionListener, incAjaxListener, incValueChangeListener, changePlanet) {
  assert.expect(6);
  var changeActiveComponent = jQueryFrame("#page\\:mainForm\\:compTestSection > div > h1 > span").text()
      != "<tc:" + componentName + ">";
  var done = assert.async(1 + changeActiveComponent);

  var oldActionCount;
  var oldActionListenerCount;
  var oldAjaxListenerCount;
  var oldValueChangeListenerCount;
  var oldTimestamp;
  var oldPlanet;

  if (changeActiveComponent) {
    activateComponent(componentName);
    var step = 1;

    jQuery("#page\\:testframe").load(function() {
      if (step == 1) {
        oldActionCount = getActionCount();
        oldActionListenerCount = getActionListenerCount();
        oldAjaxListenerCount = getAjaxListenerCount();
        oldValueChangeListenerCount = getValueChangeListenerCount();
        oldTimestamp = getTimestamp();
        oldPlanet = getSelectedPlanet();

        componentFunc().trigger(event);

        validateAjax(assert, done, oldActionCount, oldActionListenerCount, oldAjaxListenerCount,
            oldValueChangeListenerCount, oldTimestamp, oldPlanet, incAction, incActionListener, incAjaxListener,
            incValueChangeListener, changePlanet);
      }
      step++;
      done();
    });
  } else {
    oldActionCount = getActionCount();
    oldActionListenerCount = getActionListenerCount();
    oldAjaxListenerCount = getAjaxListenerCount();
    oldValueChangeListenerCount = getValueChangeListenerCount();
    oldTimestamp = getTimestamp();
    oldPlanet = getSelectedPlanet();

    componentFunc().trigger(event);

    validateAjax(assert, done, oldActionCount, oldActionListenerCount, oldAjaxListenerCount,
        oldValueChangeListenerCount, oldTimestamp, oldPlanet, incAction, incActionListener, incAjaxListener,
        incValueChangeListener, changePlanet);
  }
}

function validateAjax(assert, done, oldActionCount, oldActionListenerCount, oldAjaxListenerCount,
                      oldValueChangeListenerCount, oldTimestamp, oldPlanet, incAction, incActionListener,
                      incAjaxListener, incValueChangeListener, changePlanet) {
  waitForAjax(function() {
    var newActionCount = getActionCount();
    var newActionListenerCount = getActionListenerCount();
    var newAjaxListenerCount = getAjaxListenerCount();
    var newValueChangeListenerCount = getValueChangeListenerCount();
    var newTimestamp = getTimestamp();
    var newPlanet = getSelectedPlanet();

    return newActionCount == (oldActionCount + incAction)
    && newActionListenerCount == (oldActionListenerCount + incActionListener)
    && newAjaxListenerCount == (oldAjaxListenerCount + incAjaxListener)
    && newValueChangeListenerCount == (oldValueChangeListenerCount + incValueChangeListener)
    && newTimestamp > oldTimestamp
    && changePlanet ? newPlanet != oldPlanet : newPlanet == oldPlanet;
  }, function() {
    var newActionCount = getActionCount();
    var newActionListenerCount = getActionListenerCount();
    var newAjaxListenerCount = getAjaxListenerCount();
    var newValueChangeListenerCount = getValueChangeListenerCount();
    var newTimestamp = getTimestamp();
    var newPlanet = getSelectedPlanet();

    assert.equal(newActionCount, oldActionCount + incAction, "action");
    assert.equal(newActionListenerCount, oldActionListenerCount + incActionListener, "actionListener");
    assert.equal(newAjaxListenerCount, oldAjaxListenerCount + incAjaxListener, "ajaxListener");
    assert.equal(newValueChangeListenerCount, oldValueChangeListenerCount + incValueChangeListener,
        "valueChangeListener");
    assert.ok(newTimestamp > oldTimestamp, "timestamp");
    if (changePlanet) {
      assert.notEqual(newPlanet, oldPlanet, "selectedPlanet");
    } else {
      assert.equal(newPlanet, oldPlanet, "selectedPlanet");
    }

    done();
  }, 3000);
}

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
  return jQueryFrame("#page\\:mainForm\\:inPlanet\\:\\:field").val()
}

function activateComponent(componentName) {
  jQueryFrame("#page\\:mainForm\\:componentTable .tobago-sheet-row").each(function() {
    if (jQuery(this).find("td").eq(0).find("span").text() == componentName) {
      this.click();
    }
  });
}
